import java.io.*;
//this class is used to split the input XML file into a file for testing
public class XMLParser {

    public static void main(String[] args) {
        String inputXmlFile = "../output_java.xml";
        String outputXmlFile = "output500mb.xml";

        try (InputStream inputStream = new FileInputStream(inputXmlFile);
             OutputStream outputStream = new FileOutputStream(outputXmlFile)) {

            long maxBytesToRead = 250 * 1024; // 250 KB

            byte[] buffer = new byte[4096]; // Buffer for reading/writing data
            long totalBytesRead = 0; // Total bytes read from the input XML file

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // Write the buffer to the output file
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // Check if the maximum bytes to read has been reached
                if (totalBytesRead >= maxBytesToRead) {
                    break; // Stop reading further
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
