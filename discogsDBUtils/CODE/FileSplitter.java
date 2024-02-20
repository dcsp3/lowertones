import java.io.*;

public class FileSplitter {
    public static void main(String[] args) {
        final int BUFFER_SIZE = 8192; // 8 KB buffer size
        final long MAXIMUM_SIZE = 30 * 1024 * 1024; // 10 MB

        try (FileInputStream inputStream = new FileInputStream("discogs_20240201_releases.xml");
             FileOutputStream outputStream = new FileOutputStream("outputFile.txt")) {

            byte[] buffer = new byte[BUFFER_SIZE];
            long totalBytesRead = 0;
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                if (totalBytesRead >= MAXIMUM_SIZE) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

discogs_20240201_releases