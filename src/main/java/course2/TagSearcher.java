package course2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagSearcher {
	
	private static BufferedReader bufferedReader;

	public static void main(String ...args){
		int sum = 0;
		Map<String, Double> res = new TreeMap();
		try {
			Map<String, Integer> stat = wordStat("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\output\\test.txt");
			for(int i: stat.values()){
				sum += i;
			}
			for(String key : stat.keySet()){
				double perc = (double)stat.get(key)/sum*100;
				res.put(key, perc);
			}
			System.out.println(res);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			System.out.println(wordStat("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\output\\test.txt"));
			System.out.println(tagSearcher("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\output\\test.txt", "<word lang=\"pl\">(.+?)<\\/word>" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<String> tagSearcher(String path, String pattern) throws IOException{
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
		List<String> speeches = new ArrayList();
		String line;
		Pattern TAG_REGEX = Pattern.compile(pattern);
		Matcher matcher;
		while ((line = bufferedReader.readLine()) != null){
			if (line.length()!=0){
				matcher = TAG_REGEX.matcher(line);
				while(matcher.find()){
					speeches.add(matcher.group(0));
				}
				
			}
		}
		return speeches;
	}
	public static Map<String, Integer> wordStat(String path) throws IOException{
		Map<String, Integer>stat = new TreeMap<String, Integer>();
		List <String> arr = tagSearcher("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\output\\test.txt", "<word lang=(.+?)>(.+?)<\\/word>");
		for (String str : arr){
			String sub = str.substring(11, 14);
			if(stat.containsKey(sub)){
				stat.put(sub, stat.get(sub)+1);
			}else{
			stat.put(sub, 1);
			}
		}
		return stat;
	}
}
