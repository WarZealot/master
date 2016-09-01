package random;

import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class RandomCode {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        System.setProperty("http.proxyHost", "proxy.materna.de");
        System.setProperty("http.proxyPort", "8080");

        String urlString = "http://www.google.de";

        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            String result = IOUtils.toString(connection.getInputStream());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
