import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.nio.charset.StandardCharsets;

//this file is used to remove some tags from the releases XML file
//the input file is discogs_20240201_releases.xml, change the file name
//the output file is output_java.xml, change the file name

public class XMLProcessor {
    public static void main(String[] args) throws Exception {
        String inputXmlFile = "discogs_20240201_releases.xml";
        String outputXmlFile = "output_java.xml";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try (InputStream inputStream = new FileInputStream(inputXmlFile);
                OutputStream outputStream = new FileOutputStream(outputXmlFile)) {

            XMLStreamReader reader = inputFactory
                    .createXMLStreamReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(outputStream, StandardCharsets.UTF_8.name());

            boolean skip = false;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        String localName = reader.getLocalName();
                        //the tags to be removed are: videos, notes, images, labels, formats, data_quality, country, position, duration, 
                        //anv, tracks, companies, identifiers
                        if ("videos".equals(localName) || "notes".equals(localName) || "images".equals(localName)
                                || "labels".equals(localName) || "formats".equals(localName)
                                || "data_quality".equals(localName) || "country".equals(localName)
                                || "position".equals(localName) || "duration".equals(localName)
                                || "anv".equals(localName) || "tracks".equals(localName)
                                || "companies".equals(localName) || "identifiers".equals(localName)) {
                            skip = true;
                        }
                        if (!skip) {
                            writer.writeStartElement(localName);
                            int attributeCount = reader.getAttributeCount();
                            for (int i = 0; i < attributeCount; i++) {
                                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                            }
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        localName = reader.getLocalName();
                        if (!skip) {
                            writer.writeEndElement();
                        }
                        if ("videos".equals(localName) || "notes".equals(localName) || "images".equals(localName)
                                || "labels".equals(localName) || "formats".equals(localName)
                                || "data_quality".equals(localName) || "country".equals(localName)
                                || "position".equals(localName) || "duration".equals(localName)
                                || "anv".equals(localName) || "tracks".equals(localName)
                                || "companies".equals(localName) || "identifiers".equals(localName)) {
                            skip = false;
                        }
                        break;
                    case XMLStreamReader.CHARACTERS:
                        if (!skip) {
                            writer.writeCharacters(reader.getText());
                        }
                        break;
                    default:
                        break;
                }
            }

            reader.close();
            writer.close();
        }
    }
}
