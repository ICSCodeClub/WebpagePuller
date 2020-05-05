package com.codeclub.WebpageGetter;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		File xmlFile = new File("sitemap.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		
		//https://www.programmergate.com/how-to-read-xml-file-in-java/
		NodeList urls = doc.getElementsByTagName("url");
		
	}
}
