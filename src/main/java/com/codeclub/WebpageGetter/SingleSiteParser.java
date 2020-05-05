package com.codeclub.WebpageGetter;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SingleSiteParser {
	public static void parseSite(String url) {
		//parse a site using JSOUP
		// https://stackoverflow.com/questions/238547/how-do-you-programmatically-download-a-webpage-in-java
	}
	public static String getByTag(String tag, String html) {
		Document doc = Jsoup.parse(html, "", Parser.htmlParser());
		
		String out = "";
		for (Element sentence : doc.getElementsByTag(tag))
		    out = out + StringUtils.lstrip(sentence.wholeText()) + "\n";
		
		if(out.length() > 0)
			return out.substring(0, out.length()-1);
		else return "";
	}
	public static String getByClass(String cssclass, String html) {
		Document doc = Jsoup.parse(html, "", Parser.htmlParser());
		
		String out = "";
		for (Element sentence : doc.getElementsByClass(cssclass))
		    out = out + StringUtils.lstrip(sentence.wholeText()) + "\n";
		
		if(out.length() > 0)
			return out.substring(0, out.length()-1);
		else return "";
	}
}
