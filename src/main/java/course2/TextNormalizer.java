package course2;

import org.apache.tika.language.LanguageIdentifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class TextNormalizer {
	private String path;
	private static BufferedWriter bw;
	
	private static List<String> getFiles(String path) throws IOException {
		List<String> fileList = new LinkedList<String>();
		Files.walk(Paths.get(path)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		        
		        fileList.add(filePath.toString());
		    }
		});
		return fileList;
	}
	
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public static void paragraphMatcher (String path) throws IOException {
		BufferedReader bufferedReader;
		List<String> fileList = getFiles(path);
		String p;
		File file = new File("C:\\Users\\Asus\\workspace\\course2\\src\\files\\output\\test.txt");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    bw = new BufferedWriter(fw);
		for (int i = 0; i < fileList.size(); i++) {
			bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(fileList.get(i).toString()),"UTF-8"));
			while ((p = bufferedReader.readLine()) != null) {
				
				if (p.length() != 0){
					p = sentenceMatcher(p);
					int start = 0;
					int end = 0;
					start = start + end;
					end = p.length() + end;
					StringBuilder str = new StringBuilder();
					
					str.append(p);
					str.insert(end, "</p>");
					str.insert(start, "<p>");
					System.out.println(str);
					bw.write(str.toString()+"\n");
				}else{
					bw.write("\n");
					System.out.println();
				}
				
				//System.out.println(start+" "+end);
			}
			
		}
		bw.close();
	}
	
	public static String sentenceMatcher (String strings) {
		StringBuilder str = new StringBuilder();
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ROOT);
		iterator.setText(strings);
		int start = iterator.first();
		for (int end = iterator.next();
		    end != BreakIterator.DONE;
		    start = end, end = iterator.next()) {
			String substr = strings.substring(start,end);
			substr = substr.replace("«", "\"");
			substr = substr.replace("»", "\"");
			substr = substr.replace("[\\s]{2,}", " ");
			str.append("<s>"+wordMatcher(substr)+"</s>");
		    //str.append(strings.substring(start,end)); 
		}
		return str.toString();
	}
	
	@SuppressWarnings("deprecation")
	public static String wordMatcher(String str){
		String prevUk = "звати";
		String prevPl = "nie";
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings({ })
		LanguageIdentifier identifier;
		BreakIterator boundary = BreakIterator.getWordInstance(Locale.US);
		boundary.setText(str);
		int start = boundary.first();
	     for (int end = boundary.next();
	          end != BreakIterator.DONE;
	          start = end, end = boundary.next()) {
	    	  if(str.substring(start, end).matches("[\\d	—*\"\\(\\),.   !?\\-:;' ]")==true){
	    		  sb.append(str.substring(start, end));
	    	  }else{
	    		  identifier = new LanguageIdentifier(str.substring(start, end));
	    		  if((identifier.getLanguage().equals("ru")||identifier.getLanguage().equals("lt")||identifier.getLanguage().equals("be"))&&identifier.getLanguage().matches("(\\w+)")){
	    			  identifier = new LanguageIdentifier(prevUk+str.substring(start, end));
	    			  sb.append("<word lang=\""+identifier.getLanguage()+"\">"+str.substring(start, end)+"</word>");
	    		  }else if(identifier.getLanguage().equals("ro")||identifier.getLanguage().equals("en")||identifier.getLanguage().equals("lt")||identifier.getLanguage().equals("sk")){
	    			  identifier = new LanguageIdentifier(prevPl+str.substring(start, end));
	    			  sb.append("<word lang=\""+identifier.getLanguage()+"\">"+str.substring(start, end)+"</word>");
	    			  prevPl = str.substring(start, end);
	    		  }else if(identifier.getLanguage().equals("unknown")){
	    			  sb.append("<word lang=\"uk\">"+str.substring(start, end)+"</word>");
	    		  }else{
	    			  sb.append("<word lang=\""+identifier.getLanguage()+"\">"+str.substring(start, end)+"</word>");
	    		  }
	    	  }
	    	  
	     }
	     System.out.println(sb);
		return sb.toString();
	}
	
	public static String direcSpeechMatcher(String string) {
		Pattern p1 = Pattern.compile("[\\wА-Яа-я,\"  ]+?.+[:]+?.[\"]+[\\wА-Яа-я,\" ]+[\"].", Pattern.UNICODE_CHARACTER_CLASS);
		Pattern p2 = Pattern.compile("[\\wА-Яа-я,\"  ]+[:]+[\"]+[\\wА-Яа-я,\" ]+[\"].",Pattern.UNICODE_CHARACTER_CLASS);
		Pattern p3 = Pattern.compile("[\"]+[\\wА-Яа-я,\" ]+[\"].?—[\\wА-Яа-я,\"  ]+.",Pattern.UNICODE_CHARACTER_CLASS);
		Pattern p4 = Pattern.compile("[\\wА-Яа-я,\"  ]+?.+[:]+?.[\"]+[\\wА-Яа-я, ]+.\"+?.\\—.[\\wА-Яа-я,\"  ]+.",Pattern.UNICODE_CHARACTER_CLASS);
		Pattern p5 = Pattern.compile("\"+[\\wА-Яа-я,  ]+\"+?..— [\\wА-Яа-я,  ]+.",Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p1.matcher(string);
		StringBuilder sb= new StringBuilder();
		if (m.find()){
			sb.append(m.group());
			sb = sb.insert(sb.length(), "</speechpersonage>");
			sb = sb.insert(sb.indexOf(":")+1, "<speechpersonage>");
			sb = sb.insert(0,"<speechwriter>");
			sb = sb.insert(sb.indexOf(":")+1, "</speechwriter>");
			//System.out.println("<directspeech>"+sb+"</directspeech>");
			return sb.toString();
		}else{
			m = p2.matcher(string);
			if (m.find()){
				sb.append(m.group());
				sb = sb.insert(sb.length(), "</speechpersonage>");
				sb = sb.insert(sb.indexOf(":")+1, "<speechpersonage>");
				sb = sb.insert(0,"<speechwriter>");
				sb = sb.insert(sb.indexOf(":")+1, "</speechwriter>");
				//System.out.println("<directspeech>"+sb+"</directspeech>");
				return sb.toString();
			}else{
				m = p5.matcher(string);
				if(m.find()){
					sb.append(m.group());
					
					sb = sb.insert(0,"<speechpersonage>");
					sb = sb.insert(sb.indexOf("—")-1, "</speechpersonage>");
					sb = sb.insert(sb.length(), "</speechwriter>");
					sb = sb.insert(sb.indexOf("—")-1, "<speechwriter>");
					//System.out.println("<directspeech>"+sb+"</directspeech>");
					return sb.toString();
				}else{
					m = p3.matcher(string);
					if(m.find()){
						sb.append(m.group());
						
						sb = sb.insert(0,"<speechpersonage>");
						sb = sb.insert(sb.indexOf("—")-1, "</speechpersonage>");
						sb = sb.insert(sb.length(), "</speechwriter>");
						sb = sb.insert(sb.indexOf("—")-1, "<speechwriter>");
						//System.out.println("<directspeech>"+sb+"</directspeech>");
						return sb.toString();
				}
			}
		}
		return string;
	}
	
}
	}
