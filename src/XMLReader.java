import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.File;

public class XMLReader {

    public static String readXML(String filePath) throws Exception {

        StringBuilder content = new StringBuilder();

        File file = new File(filePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {

            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                NodeList children = element.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {

                    Node child = children.item(j);

                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        content.append(child.getNodeName())
                               .append("=")
                               .append(child.getTextContent().trim())
                               .append(", ");
                    }
                }

                content.append("\n");
            }
        }

        return content.toString();
    }
}
