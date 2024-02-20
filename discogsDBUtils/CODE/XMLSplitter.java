import java.io.*;
//this class is used to split the input XML file into multiple files for batch processing
public class XMLSplitter {
    public static void main(String[] args) {
        final int batchSize = 1000;
        int releaseCount = 0;
        int fileCount = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader("input.xml"))) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output" + fileCount + ".xml"));
            writer.write("<releases>");

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("<release>")) {
                    if (releaseCount >= batchSize) {
                        writer.write("</releases>");
                        writer.close();
                        fileCount++;
                        writer = new BufferedWriter(new FileWriter("output" + fileCount + ".xml"));
                        writer.write("<releases>");
                        releaseCount = 0;
                    }
                    releaseCount++;
                }
                writer.write(line + System.lineSeparator());
            }
            writer.write("</releases>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
