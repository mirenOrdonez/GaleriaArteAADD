
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mirenordonezdearce
 */
public class Gestionar_XPath {
    Document doc;
    
    XPath xpath;
    
    public int abrir_XML_(File fichero) {
        doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(fichero);
            return 0;
        }catch (Exception e) {
            return -1;
        }
    }
    
    public String ejecutarXPath(String consulta) {
        String salida = "";
        Node node = null;
        String datos_nodo[] = null;
        int contador = 1;
        
        try {
            //Crea el objeto XPATH
            xpath = XPathFactory.newInstance().newXPath();
            
            //Crea un XPathExpression con la consulta
            XPathExpression exp = xpath.compile(consulta);
            
            //Ejecuta la consulta
            Object resultado = exp.evaluate (doc, XPathConstants.NODESET);
            
            //Guardamos en lista de nodos el resultado
            NodeList nodeList = (NodeList) resultado;
            
            for (int i=0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                //Para el caso de que la consulta sea sobre Título.
                if (node.getNodeName().equals("Titulo")) {
                    salida = salida + "\n" + "El título del libro " + contador + " es: " + nodeList.item(i).getFirstChild().getNodeValue();
                    contador++;
                }
                //Para el caso de que la consulta sea sobre Autor.
                else if (node.getNodeName().equals("Autor")) {
                    salida = salida + "\n" + "El autor del libro " + contador + " es: " + nodeList.item(i).getFirstChild().getNodeValue();
                    contador++;
                }
                else if (node.getNodeName().equals("publicado_en")) {
                    salida = salida + "\n" + "El libro " + contador + " se publicó en: " + nodeList.item(i).getFirstChild().getNodeValue();
                    contador++;
                }
                //Para el caso de que sea Libro
                else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    datos_nodo = procesarLibro(node);
                    salida = salida + "\n" + "Publicado en: " + datos_nodo[0];
                    salida = salida + "\n" + "El título es: " + datos_nodo[1];
                    salida = salida + "\n" + "El autor es: " + datos_nodo[2];
                    salida = salida + "\n ---------------";
                }
                else {
                    salida = salida + "\n" + nodeList.item(i).getFirstChild().getNodeValue();
                }
                
            }
            return salida;
        } catch (Exception ex) {
            return "Error al ejecutar la consulta.";
        }
    }
    
    private String[] procesarLibro(Node node) {
        String datos[] = new String[3];
        Node ntemp = null;
        int contador = 1;
        
        //Para el atributo, como solo hay uno, no está en un bucle.
        datos[0] = node.getAttributes().item(0).getNodeValue();
        
        //Obtengo los nodos hijos de los hijos Libro (título y autor).
        NodeList nodos = node.getChildNodes();
        
        for (int i = 0; i < nodos.getLength(); i++) {
            ntemp = nodos.item(i);
            if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                datos[contador] = ntemp.getFirstChild().getNodeValue();
                contador++;
            }
        }
        return datos;
    }
    
    
}
