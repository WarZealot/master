/**
 *
 */
package dropbox;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Konstantin
 *
 */
public class DropboxCollectorIFTTT {
    public final static String OAUTH_CONSUMER_KEY = "czrkhe4jhjqrxkv";
    public final static String OAUTH_CONSUMER_SECRET = "cxaeslwf8paz5il";
    public final static String OAUTH_TOKEN = "82kIknP0uEIAAAAAAAAADIFarwXp0OnQ1wJDKJ7cYIPdO4BtrQ5R1EbO2J1ZM7m7";

    private static final String PATH = "/ifttt/";
    private static final List<String> NAMES = new ArrayList<>();
    static {
        NAMES.add("ifttt_result_15");
        NAMES.add("ifttt_result_30");
        NAMES.add("ifttt_result_45");
        NAMES.add("ifttt_result_60");
        NAMES.add("ifttt_result_75");
        NAMES.add("ifttt_result_150");
    }
    // private static final String NAME = "ifttt_result_60";
    // private static final String SOURCE = PATH + NAME.replace("result", "source");
    // private static final String TARGET = PATH + NAME.replace("result", "target");

    // private static final String OUTPUT = NAME.replace("result_", "").concat(".csv");

    private static final JsonParser PARSER = new JsonParser();
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("FlashApp");
        DbxClientV2 dbxClient = new DbxClientV2(dbxRequestConfig, OAUTH_TOKEN);
        System.out.println("Dropbox Account Name: " + dbxClient.users().getCurrentAccount().getName());

        // listFiles(dbxClient);

        for (String name : NAMES) {
            System.out.println("Evaluating " + name);
            String SOURCE = calcSource(name);
            String TARGET = calcTarget(name);
            String OUTPUT = calcOutput(name);

            Map<String, Long> sources = collectData(dbxClient, SOURCE);
            Map<String, Long> targets = collectData(dbxClient, TARGET);

            if (sources.isEmpty()) {
                System.err.println("No files in folder: " + SOURCE);
                continue;
            }

            if (sources.size() != targets.size()) {
                System.err.println("ERROR: Size mismatch " + sources.size() + " != " + targets.size());
                System.err.println("Evaluating next folder");
                continue;
            }

            String result = "Name\tUploadTime\tFinishTime\tDiff\n";
            Set<String> set = sources.keySet();
            for (String key : set) {
                Long uploadTime = sources.get(key);
                Long finishTime = targets.get(key);
                // System.out.println("upload=" + uploadTime + " finish= " + finishTime);
                result += key + ":\t" + uploadTime + "\t" + finishTime + "\t" + (finishTime - uploadTime + "\n");
            }
            result += "\n";

            // mittelwert
            List<Long> diffs = getDiffs(sources, targets);
            long sum = 0;
            long max = 0;
            for (Long diff : diffs) {
                sum += diff;
                if (diff > max) {
                    max = diff;
                }
            }
            long mittelwert = sum / diffs.size() / 1000;
            result += "Mittelwert:\t" + mittelwert + "\n";
            result += "Longest:\t" + max / 1000 + "\n";

            // System.out.println(result);
            writeToFile(OUTPUT, result);
        }

        System.out.println("Finished");
    }

    private static double calculateMittelwert(Map<String, Long> sources, Map<String, Long> targets) {
        Set<String> set = sources.keySet();
        List<Long> diffs = new ArrayList<>();
        for (String key : set) {
            Long uploadTime = sources.get(key);
            Long finishTime = targets.get(key);
            diffs.add(finishTime - uploadTime);
        }

        long sum = 0;
        for (Long diff : diffs) {
            sum += diff;
        }
        double mittelwert = sum / diffs.size() / 1000;

        return mittelwert;
    }

    private static List<Long> getDiffs(Map<String, Long> sources, Map<String, Long> targets) {
        Set<String> set = sources.keySet();
        List<Long> diffs = new ArrayList<>();
        for (String key : set) {
            Long uploadTime = sources.get(key);
            Long finishTime = targets.get(key);
            diffs.add(finishTime - uploadTime);
        }
        return diffs;
    }

    private static Map<String, Long> collectData(DbxClientV2 client, String directory) throws ParseException {
        HashMap<String, Long> result = new HashMap<>();
        ListFolderResult folderResult;
        try {
            folderResult = client.files().listFolder(directory);
        } catch (DbxException e) {
            System.err.println("Directory '" + directory + "' not found!");
            return Collections.EMPTY_MAP;
        }
        List<Metadata> entries = folderResult.getEntries();
        for (Metadata entry : entries) {
            String json = entry.toString();
            JsonObject root = PARSER.parse(json).getAsJsonObject();
            String dateStr = root.get("server_modified").getAsString();
            Date date = FORMAT.parse(dateStr);

            result.put(entry.getName(), date.getTime());
        }

        return result;
    }

    public static void collectCreateTimes(DbxClientV2 client) throws Exception {
        ListFolderResult listFolder = client.files().listFolder("testfolder");
        List<Metadata> entries = listFolder.getEntries();
        for (Metadata metadata : entries) {
            System.out.println(metadata.getName());
        }
    }

    public static void writeToFile(String path, String text) throws Exception {
        PrintWriter out = new PrintWriter(path);
        out.println(text);
        out.flush();
        out.close();
    }

    public static String calcSource(String name) {
        return PATH + name.replace("result", "source");
    }

    public static String calcTarget(String name) {
        return PATH + name.replace("result", "target");
    }

    public static String calcOutput(String name) {
        return name.replace("result_", "").concat(".csv");
    }
}
