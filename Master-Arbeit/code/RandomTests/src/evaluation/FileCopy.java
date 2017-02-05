/**
 *
 */
package evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Konstantin
 *
 */
public class FileCopy {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String sourceDirectory = "D:/Programme/Java/Evaluation/dropbox2dropbox/images1000/";
        String sourceName = "stonehenge";
        String fileEnding = ".jpeg";
        File sourceFile = new File(sourceDirectory + sourceName + fileEnding);

        for (int i = 5002; i <= 10000; i++) {
            File targetFile = new File(sourceDirectory + sourceName + i + fileEnding);
            copyFile(sourceFile, targetFile);
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
