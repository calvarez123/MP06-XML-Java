import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PR142Main {
    public static void main(String[] args) {
        try {   
            //casa -->C:\\Users\\Cristian\\AMS-2\\MP06 Acces a Dades\\Uf1\\practica_xml\\data\\cursos.xml
            //clase --> /home/super/AMS-2/MP06 Acces a Dades/Uf1/practica_xml/data/cursos.xml
            
            String filePath = "data/cursos.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(filePath);
            
            
            mostrarTablaCursos(document);
            System.out.println("----------------------------------------");

            mostrarModulosDeCurso(document, "AMS2");
            System.out.println("----------------------------------------");

            listarAlumnosDeCurso(document, "AMS2");
            System.out.println("----------------------------------------");

            agregarAlumnoACurso(document, "AMS2", "susana",filePath);
            System.out.println("----------------------------------------");

            eliminarAlumnoDeCurso(document,filePath , "AMS2","susana");

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mostrarTablaCursos(Document document) {
        NodeList cursos = document.getElementsByTagName("curs");

        System.out.println("Tabla de Cursos:");
        System.out.format("%-10s %-25s %-20s\n", "ID", "Tutor", "Cantidad de Alumnos");

        for (int i = 0; i < cursos.getLength(); i++) {
            Element curso = (Element) cursos.item(i);
            String idCurso = curso.getAttribute("id");
            String tutor = curso.getElementsByTagName("tutor").item(0).getTextContent();

            NodeList alumnos = curso.getElementsByTagName("alumne");
            int cantidadAlumnos = alumnos.getLength();

            System.out.format("%-10s %-25s %-20s\n", idCurso, tutor, cantidadAlumnos);
        }
    }

    public static void mostrarModulosDeCurso(Document document, String cursoId) {
        NodeList cursos = document.getElementsByTagName("curs");

        System.out.println("Módulos del curso con ID " + cursoId + ":");
        System.out.format("%-10s %-30s\n", "ID Módulo", "Título Módulo");

        for (int i = 0; i < cursos.getLength(); i++) {
            Element curso = (Element) cursos.item(i);
            String idCurso = curso.getAttribute("id");

            if (idCurso.equals(cursoId)) {
                NodeList modulos = curso.getElementsByTagName("modul");
                for (int j = 0; j < modulos.getLength(); j++) {
                    Element modulo = (Element) modulos.item(j);
                    String idModulo = modulo.getAttribute("id");
                    String tituloModulo = modulo.getElementsByTagName("titol").item(0).getTextContent();

                    System.out.format("%-10s %-30s\n", idModulo, tituloModulo);
                }
                break;
            }
        }
    }

    public static void listarAlumnosDeCurso(Document document, String cursoId) {
        NodeList cursos = document.getElementsByTagName("curs");

        System.out.println("Alumnos del curso con ID " + cursoId + ":");
        System.out.format("%-25s\n", "Nombre del Alumno");

        for (int i = 0; i < cursos.getLength(); i++) {
            Element curso = (Element) cursos.item(i);
            String idCurso = curso.getAttribute("id");

            if (idCurso.equals(cursoId)) {
                NodeList alumnos = curso.getElementsByTagName("alumne");
                for (int j = 0; j < alumnos.getLength(); j++) {
                    Element alumno = (Element) alumnos.item(j);
                    String nombreAlumno = alumno.getTextContent();

                    System.out.format("%-25s\n", nombreAlumno);
                }
                break;
            }
        }
    }
    public static void agregarAlumnoACurso(Document doc, String cursoId, String nuevoAlumno,String xmlFilePath) {
        try {
            
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            
            String xpathExpression = "/cursos/curs[@id='" + cursoId + "']/alumnes";
            Node alumnesNode = (Node) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.NODE);

            if (alumnesNode != null) {
                // con esto creamos el alumno
                Element nuevoAlumneElement = doc.createElement("alumne");
                nuevoAlumneElement.appendChild(doc.createTextNode(nuevoAlumno));
                alumnesNode.appendChild(nuevoAlumneElement);

                // Con esto guardp el xml
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(xmlFilePath));
                transformer.transform(source, result);

                System.out.println("Nuevo alumno agregado al curso " + cursoId + ": " + nuevoAlumno);
            } else {
                System.out.println("No se encontró el curso con el ID: " + cursoId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void eliminarAlumnoDeCurso(Document doc,String xmlFilePath, String cursoId, String alumnoAEliminar) {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            String xpathExpression = "/cursos/curs[@id='" + cursoId + "']/alumnes/alumne[text()='" + alumnoAEliminar + "']";
            Node alumnoNode = (Node) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.NODE);

            if (alumnoNode != null) {
                // Eliminamos el alumno
                Node parentAlumnesNode = alumnoNode.getParentNode();
                parentAlumnesNode.removeChild(alumnoNode);

                // guardamos la modificacion echa
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(xmlFilePath));
                transformer.transform(source, result);

                System.out.println("Alumno eliminado del curso " + cursoId + ": " + alumnoAEliminar);
            } else {
                System.out.println("No se encontró el alumno en el curso con el ID: " + cursoId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}

