package course2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String>fileList=new LinkedList<>();
        
        try {
			Files.walk(Paths.get("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\input")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			        
			        fileList.add(filePath.toString());
			    }
			});
		} catch (IOException e) {			
			e.printStackTrace();
		}
        System.out.println(fileList);
        try {
            fileReader(fileList,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String fileReader(List<String> paths, String encoding) throws IOException {
        
    	String result = null;
        BufferedReader bufferedReader;
        Map<String,Map<Character, Integer>> rezF=new TreeMap<>();
        Map<Character,Integer> symb= new TreeMap<>();
        File file=new File("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\output\\out.txt");
      for(int j=0; j<paths.size();j++){
    	  bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(paths.get(j).toString()),encoding));
        while ((result=bufferedReader.readLine())!=null) {
                for (int i=0; i<result.length();i++){
                    if (symb.containsKey(result.charAt(i))){
                        int newVal=symb.get(result.charAt(i))+1;
                        symb.replace(result.charAt(i), newVal);
                    }else {
                        symb.put(result.charAt(i),1);
                    }
                }
        }
        Path p=Paths.get(paths.get(j).toString());
        rezF.put(p.getFileName().toString(), symb);
        symb=new TreeMap<>();
    }
       

            fileWriter(file,rezF);

        
    return result;
}
    public static void fileWriter(File file,Map<String,Map<Character, Integer>> rezF) throws IOException {
    	Map <Character,Integer> symb=new TreeMap<>();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        ArrayList<String> keys=new ArrayList<>(rezF.keySet());
        Map<Character,Integer>allSymb=new TreeMap<>();
        for(String i : keys){
        	symb=rezF.get(i);
        	
        for (Map.Entry<Character, Integer> entry : symb.entrySet()) {
            Character key = entry.getKey();
            int val = entry.getValue();
            int code = key;
            if(allSymb.get(key)==null){
            	allSymb.put(key, val);
            }else{
            	int newVal=val+allSymb.get(key);
            	allSymb.replace(key, newVal);
            }
            String unicode = String.format("%04x", code);
            String rez;
            String desc="";
            if(Symb.blankSymb.get(unicode) != null){
            	String l=Symb.blankSymb.get(unicode);  
            	desc=matcher(unicode);
            	 rez = String.format("%-8s %8s %8s %8s %-15s %n",i, l, val, unicode,desc);
            	 bw.write(rez+"\n");
            }else{
            	desc=matcher(unicode);
             rez = String.format("%-8s %8s %8s %8s %-15s %n",i, key, val, unicode,desc);
            
            bw.write(rez+"\n");


        }
        }
    }
        bw.write("=======================================================\n");
        for (Map.Entry<Character, Integer> ent : allSymb.entrySet()) {
        	Character allKey = ent.getKey();
            int allVal = ent.getValue();
            int allCode = allKey;
            String unicode = String.format("%04x", allCode);
            String rez;
            String desc="";
            if(Symb.blankSymb.get(unicode) != null){
            	String l=Symb.blankSymb.get(unicode); 
            	desc=matcher(unicode);
            	 rez = String.format("%-8s %8s %15s %-25s  %n", l, allVal, unicode,desc);
            	 bw.write(rez+"\n");
            }else{
            	desc=matcher(unicode);
             rez = String.format("%-8s %8s %15s %-25s %n", allKey, allVal ,unicode,desc);
            
            bw.write(rez+"\n");
            System.out.println(rez);

        }
        }
        
        bw.close();
    }
    
    public static String matcher(String unicode) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Asus\\workspace\\course2\\src\\main\\java\\files\\add\\desc.txt"),"UTF-8"));
		String line=null;
		StringBuilder sb=null;
		while((line=br.readLine())!=null){
			if(line.contains(unicode.toUpperCase())){
				String []ar=line.split(" ");
				sb=new StringBuilder();
				for(int i=4;i<ar.length;i++){
					sb.append(ar[i]+" ");
					
				}
			}
		} br.close();
		String rez;
		try{
		rez=""+sb.toString();
		}catch(Exception e){
			rez="";
		}
    	return rez;
    	
    }
 
}
