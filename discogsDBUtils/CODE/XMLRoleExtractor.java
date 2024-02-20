import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

//this class is used to extract unique roles from the input XML file
//the input file is input1.xml, change the file name
//the output file is unique_roles.txt, change the file name
//the roles are extracted from the role tags and written to the output file 
public class XMLRoleExtractor {
    public static void main(String[] args) throws Exception {
        String inputXmlFile = "input1.xml";
        String outputFile = "unique_roles.txt";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();

        Set<String> uniqueRoles = new HashSet<>();

        try (InputStream inputStream = new FileInputStream(inputXmlFile);
                PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);

            boolean inRole = false;
            StringBuilder roleContent = new StringBuilder();

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        if ("role".equals(reader.getLocalName())) {
                            inRole = true;
                            roleContent.setLength(0); // Clear StringBuilder
                        }
                        break;
                    case XMLStreamReader.CHARACTERS:
                        if (inRole) {
                            roleContent.append(reader.getText());
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        if ("role".equals(reader.getLocalName())) {
                            inRole = false;
                            String roleText = roleContent.toString().trim();
                            if (!roleText.isEmpty()) {
                                // Split by commas and remove text after "[" or "]"
                                String[] roles = roleText.split(",");
                                for (String role : roles) {
                                    String trimmedRole = removeTextAfterBracket(role.trim());
                                    if (!trimmedRole.isEmpty()) {
                                        uniqueRoles.add(trimmedRole);
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            // Write unique roles to the output file
            for (String uniqueRole : uniqueRoles) {
                writer.println(uniqueRole);
            }
            reader.close();
        }
    }
    private static String removeTextAfterBracket(String text) {
        // Remove all text after "[" or "]"
        Pattern pattern = Pattern.compile("\\[.*?\\]");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("").trim();
    }
}
