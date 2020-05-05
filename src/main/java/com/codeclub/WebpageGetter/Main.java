package com.codeclub.WebpageGetter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
		System.out.println(findSitemapUrl("https://gradesfixer.com/"));
		File xml = downloadFromUrl(findSitemapUrl("https://gradesfixer.com/")); //https://jsoup.org/sitemap.xml
		
		String[] acceptedUrls = getUrlsFromXML(xml, new String[][] {{"priority", "0.7"}});
		
		System.out.println("Total: "+acceptedUrls.length);
		
		String singleUrl = acceptedUrls[8];
		System.out.println("beginning trial on "+singleUrl);
		
		String singleSite = Jsoup.connect(singleUrl).get().html();
		System.out.println(SingleSiteParser.getByTag("h1", singleSite)); //gets title
		System.out.println(SingleSiteParser.getByClass("single-essay-item", singleSite)); //gets essay
		
	}
	public static String findSitemapUrl(String domain) {
		try {
			//goto robots.txt
			//ex. https://www.google.com/robots.txt https://lms.lwsd.org/robots.txt
			if(domain.endsWith("/")) domain = domain.substring(0, domain.length()-1);
			
			String robots = domain+"/robots.txt";
			String robotsContent = Jsoup.connect(robots).get().html();
			
			//look for Sitemap: ______
			java.util.Scanner rContent = new java.util.Scanner(robotsContent);
			String url = "";
			while(rContent.hasNext()) {
				String word = rContent.next();
				if(word.strip().equals("Sitemap:") && rContent.hasNext())
					url = rContent.next();
			}
			rContent.close();
			return url;
		} catch (IOException e) {
			return "";
		}
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
	public static String[] getUrlsFromXML(File xml) {
		return getUrlsFromXML(xml, new String[][] {});
	}
	public static String[] getUrlsFromXML(File xml, String[][] properties) {
		try {
			//parse xml
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			
			//loop through urls
			java.util.ArrayList<String> acceptedUrls = new java.util.ArrayList<String>();
			NodeList urls = doc.getElementsByTagName("url");
			for(int i=0; i<urls.getLength(); i++)
		    {
		        Node studentNode = urls.item(i);
		        if(studentNode.getNodeType() == Node.ELEMENT_NODE)
		        {
		            Element element = (Element) studentNode;
		            String url = element.getElementsByTagName("loc").item(0).getTextContent().strip();
		            //check all properties (or just add if there are none)
		            boolean meetsAll = true;
		            for(String[] property : properties) {
		            	if(property.length <= 1 || !meetsAll) continue;
		            	meetsAll = meetsAll && element.getElementsByTagName(property[0]).item(0) != null;
		            	meetsAll = meetsAll && element.getElementsByTagName(property[0]).item(0).getTextContent().strip().equalsIgnoreCase(property[1].strip());
		            	//System.out.println("comparing "+element.getElementsByTagName(property[0]).item(0).getTextContent().strip() +" to "+ property[1].strip()+" is "+meetsAll);
		            }
		            if(meetsAll) acceptedUrls.add(url);
		        }
		    }
			return acceptedUrls.toArray(new String[0]);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return new String[0];
		}
	}
}
