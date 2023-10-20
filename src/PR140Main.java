import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PR140Main {
    public static void main(String[] args) {
        String filePath = "data/persones.xml";
        Document doc = UtilsXML.read(filePath);
        // Exemple de llistar tots els elements d'un XPath
        
        NodeList llista0 = UtilsXML.getNodeList(doc, "/persones/persona");
        System.out.println(String.format("%-10s %-15s %-5s %-10s", "Nom", "Cognom", "Edat", "Ciutat"));
        System.out.println("---------------------------------------------");

        printLlistaMenjars(llista0);
    }
    static void printLlistaMenjars (NodeList llista) {
        for(int cnt = 0; cnt < llista.getLength(); cnt = cnt + 1) {
            Node nodeFood = llista.item(cnt);
            if(nodeFood.getNodeType() == Node.ELEMENT_NODE) {
                // Si és de tipus "ELEMENT_NODE" podem fer el cast a Element
                Element elmFood = (Element) nodeFood;
                Element childNom = UtilsXML.getFirstChildByName(elmFood, "nom");
                Element childcognom = UtilsXML.getFirstChildByName(elmFood, "cognom");
                Element childedat = UtilsXML.getFirstChildByName(elmFood, "edat");
                Element childciutat = UtilsXML.getFirstChildByName(elmFood, "cognom");
                String txtNom = childNom.getTextContent();
                String txtcognom = childcognom.getTextContent();
                String txtedat = childedat.getTextContent();
                String txtciutat = childciutat.getTextContent();


                System.out.println(String.format("%-10s %-15s %-5s %-10s", txtNom, txtcognom, txtedat, txtciutat));

            }
        }
    }
}
//C:\Users\Cristian\AMS-2\MP06 Acces a Dades\Uf1\practica_xml\data\persones.xml