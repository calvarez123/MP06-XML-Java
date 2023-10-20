

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UtilsXML {

    // Reads an XML file into W3C Document
    static Document read (String filePath) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File file = new File(filePath);
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) { e.printStackTrace(); 
        } catch (SAXException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
        return doc;
    }

    // Save a Document into an XML file
    static public void write (String path, Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            trimWhitespace(doc);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch (TransformerException e) { e.printStackTrace(); }
    }

    // Removes unnecessary line jumps from XML (prettyfies it)
    public static void trimWhitespace(Node node) {
        NodeList children = node.getChildNodes();
        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if(child.getNodeType() == Node.TEXT_NODE) {
                child.setTextContent(child.getTextContent().trim());
            }
            trimWhitespace(child);
        }
    }

    // Returns the nodes of an XPath expression
    static public NodeList getNodeList (Document doc, String expression) {
        NodeList llista = null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            llista = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) { e.printStackTrace();  }
        return llista;
    }

    // Returns the first Node/Element of a given list
    static public Element getFirstElementInList (NodeList list) {
        Element result = null;
        Node first = list.item(0);
        if (first.getNodeType() == Node.ELEMENT_NODE) {
            result = (Element) first;
        } else {
            for(int cnt = 0; cnt < list.getLength(); cnt = cnt + 1) {
                Node node = list.item(cnt);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    result = (Element) node;
                    break;
                }
            }
        }
        return result;
    }

    // Returns the first element from a given XPath exmpression
    static public Element getFirstElementFromXpath (Document doc, String expression) {
        NodeList list = getNodeList(doc, expression);
        return getFirstElementInList(list);
    }

    // Returns the text contents of a given XPath expression
    static public String getTextFromXPath (Document doc, String expression) {
        NodeList list = getNodeList(doc, expression);
        Element elm = getFirstElementInList(list);
        return elm.getTextContent();
    }

    // Returns the first child named 'childName' from parent element
    static public Element getFirstChildByName (Element parent, String childName) {
        NodeList list = parent.getElementsByTagName(childName);
        return getFirstElementInList(list);
    }
}
