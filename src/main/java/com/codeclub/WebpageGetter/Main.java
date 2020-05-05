package com.codeclub.WebpageGetter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		File xml = downloadFromUrl("https://gradesfixer.com/sitemap.xml"); //https://jsoup.org/sitemap.xml
		System.out.println(xml);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xml);
		
		//https://www.programmergate.com/how-to-read-xml-file-in-java/
		NodeList urls = doc.getElementsByTagName("url");
		int essayCount = 0;
		for(int i=0; i<urls.getLength(); i++)
	    {
	        Node studentNode = urls.item(i);
	        if(studentNode.getNodeType() == Node.ELEMENT_NODE)
	        {
	            Element element = (Element) studentNode;
	            String url = element.getElementsByTagName("loc").item(0).getTextContent().strip();
	            if(element.getElementsByTagName("priority").item(0) != null) {
	            	String priority = element.getElementsByTagName("priority").item(0).getTextContent().strip();
	            	if(priority.equalsIgnoreCase("0.7")) {
		            	System.out.println("Found essay at "+url);
		            	essayCount ++;
		            }
	            }
	        }
	    }
		System.out.println("Total: "+essayCount);
	}
	public static File downloadFromUrl(String url) {
		try {
			File temp = File.createTempFile("download", url.substring(url.lastIndexOf(".")));
			
			PrintStream ps = new PrintStream(temp);
			ps.print(Jsoup.connect(url).get().html());
			ps.close();
			
			return temp;
		}
		catch(IOException e) {
			return null;
		}
	}
}
