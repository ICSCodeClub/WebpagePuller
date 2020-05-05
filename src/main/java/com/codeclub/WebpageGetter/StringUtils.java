package com.codeclub.WebpageGetter;

public class StringUtils {	
	public static String completeFormat(String str) {
		return StringUtils.addNewlineBeforeQuotes(StringUtils.smartNewline(StringUtils.removeGarbage(StringUtils.deleteBetweenParenthesis(str)),60));
	}
	
	private static final String[] garbage = {"â", "€", "·", "Â", "¨", "©", "Ã", "”", "[", "]", "(", ")"};
	private static final String[] smallWords = {"an","a","i","do","we","go","let","of","on","to","in"};
	private static final String[] possibleLineBreaks = {".","?","!",":",";"};
	
	public static String lstrip(String str) {
		String out = "";
		for(String line : str.split("\n"))
			if(!line.strip().isBlank())
				out = out + line.strip() + "\n";
		
		if(out.length() > 0) return out.substring(0, out.length()-1);
		else return str;
	}
	public static String removeGarbage(String str) {
		for(String trash : garbage) str = str.replace(trash, "");
		return str;
	}
	
	public static String smartNewline(String line, int minCharsToBreak) {
		line = line.replaceAll("\n", "");
		if(line.length() == 0) return line;
		
		String split = "";
		int charCount = 0;
		for(int i = 0; i < line.toCharArray().length-1; i++) {
			char c = line.toCharArray()[i];
			charCount++;
			split = split + c;
			if(strContains(c+"", possibleLineBreaks) && line.toCharArray()[i+1] != '"' && charCount > minCharsToBreak) {
				charCount = 0;
				split = split + "\n";
			}
		}
		
		//remove leading spaces from newlines
		String[] strs = split.split("\n");
		String out = "";
		for(String s : strs) out = out + s.strip() + "\n";
		
		//delete last newline
		return out.substring(0,out.length()-1);
	}
	public static String addNewlineBeforeQuotes(String str) {
		if(str.strip().length() <= 2) return str;
		String out = "";
		while(!str.isBlank() && str.strip().length() > 0) {
			if(str.indexOf("\"") == -1 || str.indexOf("\"",str.indexOf("\"")+1) == -1) {
				str = "";
				out = out + str;
			}
			else {
				out = out + "\n" + str.substring(0,str.indexOf("\"")) + "\n" + str.substring(str.indexOf("\""),str.indexOf("\"",str.indexOf("\"")+1)+1);
				str = str.substring(str.indexOf("\"",str.indexOf("\"")+1)+1).strip();
			}
		}
		if(out.length() == 0) return "";
		return out.substring(1);
	}
	public static String deleteBetween(String str, String s1, String s2, boolean replaceWithNewline) {
		while (str.contains(s1) && str.contains(s2)) {
			if(s1 == s2) 
				if(str.indexOf(s1) != -1 && str.indexOf(s2,str.indexOf(s1)+1) != -1) str = str.substring(0, str.indexOf(s1)) + str.substring(str.indexOf(s2,str.indexOf(s1)+1)+1);
			else
				str = str.substring(0, str.indexOf(s1)) + str.substring(str.indexOf(s2)+1);
			if(replaceWithNewline) str = str + "\n";
		}
		return str;
	}
	public static String deleteBetweenParenthesis(String str) {
		return deleteBetweenParenthesis(str, false);
	}
	public static String deleteBetweenParenthesis(String str, boolean replaceWithNewline) {
		/*while(str.contains("(") && str.contains(")")) {
			str = str.substring(0, str.indexOf("(")) + str.substring(str.indexOf(")")+1);
			if(replaceWithNewline) str = str + "\n";
		}
		return str;*/
		return str.replaceAll("\\s*\\([^\\)]*\\)\\s*", " ");
	}
	public static String extractTitleFromUrl(String url) {
		if(!url.contains("/")) return correctlyCapitalize(url).strip();
		String rawtitle = url.substring(url.lastIndexOf('/')+1);
		return correctlyCapitalize(rawtitle.replaceAll("_", " ")).strip();
	}
	public static String correctlyCapitalize(String str) {
		String out = "";
		
		for(String word : str.split(" ")) {
			word = word.toLowerCase();
			if(word.isBlank()) {
				continue;
			}
			//check if it's a contraction (a short word that isn't an, a, or I
			else if(word.length() <= 2 && !strContains(word.toLowerCase(), smallWords)) {
				out = out +"\'"+word;
			}
			else { //otherwise just add it
				//don't capitalize the first letter if it's not the first word and is longer than 3
				if(!out.isBlank() && word.length() <= 3) {
					out = out + " " + word;
				}
				else { //otherwise do capitalize
					out = out + " " + word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
				}
			}
		}
		
		//remember to delete the first char
		return out.substring(1);
	}
	public static boolean strContains(String str, String[] ary) {
		for(String o : ary) if(str.equalsIgnoreCase(o)) return true;
		return false;
	}
}
