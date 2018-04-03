package assignment2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IndexBuilder { 

	static int docCount = 1;
	static HashMap<String, String> indexHash = new HashMap<String,String>();
	static HashMap<String, String> indexPositionHash = new HashMap<String,String>();
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		List<String> stopWords = getStopWords();
		List<String> fileNames = getFileNames();
		Iterator<String> ir = fileNames.iterator();
		while(ir.hasNext())
		{
			List<String> fileWords = tokenizer(getFiledata(ir.next()).toString());
			createHasMapEntry(fileWords, docCount,stopWords);
			createPositionHasMapEntry(fileWords, docCount, stopWords);
			docCount++;
		}
		
		writeHashDatatoFile(indexHash,"Frequency");
		writeHashDatatoFile(indexPositionHash,"Position");
	//	System.out.println(Collections.singletonList(indexHash));
				
	}
	
	public static void createHasMapEntry(List<String> wordList , int docNumber,List<String> stopWords)
	{
		List<String> uniqueWords = wordList.stream().distinct().collect(Collectors.toList());
		Iterator ir = uniqueWords.iterator();
		
		while(ir.hasNext())
		{
			String word = ir.next().toString();
			if(!stopWords.contains(word))
			{
			int wordFrequency = Collections.frequency(wordList, word);
				if(indexHash.containsKey(word))
				{
					indexHash.put(word, indexHash.get(word)+"("+docNumber+","+wordFrequency+")");
					//System.out.println(word +":"+indexHash.get(word));
				}
				else
				{
					indexHash.put(word,"("+docNumber+","+wordFrequency+")");
					//System.out.println(word +":"+indexHash.get(word));
				}
			}
		}
	}
	
	public static void createPositionHasMapEntry(List<String> wordList , int docNumber,List<String> stopWords)
	{
		int position = 0;
		List<String> uniqueWords = wordList.stream().distinct().collect(Collectors.toList());
		Iterator ir = uniqueWords.iterator();
		//System.out.println("Document Number"+docNumber+ "  Size :"+wordList.size());
		while(ir.hasNext())
		{
			String word = ir.next().toString();
			if(!stopWords.contains(word))
			{
				for(int i=0 ; i<wordList.size();i++)
				{
					if(wordList.get(i).equals(word))
					{
						if(indexPositionHash.containsKey(word))
						{
							indexPositionHash.put(word, indexPositionHash.get(word)+"("+docNumber+","+i+")");
							//System.out.println(word +":"+indexPositionHash.get(word));
						}
						else
						{
							indexPositionHash.put(word,"("+docNumber+","+i+")");
							//System.out.println(word +":"+indexPositionHash.get(word));
						}
					}
				}
			}
		}
	}
	
	public static List<String> getFileNames() // Returns a list of the files in the folder
	{
		List<String> fileName = new ArrayList<String>();
		 File f = new File("C:/Users/Saya/Documents/NJIT_Course/Java_programs/IS634_InvertIndexing/inputFiles");
	        if(!f.exists())
	            System.out.println("No File/Dir");
	        if(f.isDirectory()){// a directory!
	            for(File file :f.listFiles()){
	            	fileName.add(file.getName());
	            }
	        }
		return fileName;
	}
	
	public static StringBuilder getFiledata(String fileName) throws IOException //Returns the file content in String format
	{
		File f1 = new File("C:/Users/Saya/Documents/NJIT_Course/Java_programs/IS634_InvertIndexing/inputFiles/"+fileName);
        BufferedReader br = new BufferedReader(new FileReader(f1));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while((line = br.readLine())!=null)
        {
            sb.append(line);
        }
        br.close();
	  return sb;
	}
	
	public static List<String> tokenizer(String text) // to tokenize all the words
	{
		StringTokenizer st = new StringTokenizer(text);
		List<String> tokenizedList = new ArrayList<String>();
		String tempString;
		
		while (st.hasMoreElements()) {
			tempString = st.nextElement().toString();
			Pattern p = Pattern.compile("[^a-z]",Pattern.CASE_INSENSITIVE);
		     Matcher m = p.matcher(tempString);
		     boolean b = m.find();
		     if (!b)
		     {
		    	 if(tempString.length()>1)
		    	 tokenizedList.add(tempString.toLowerCase());
		     }    
		}
		return tokenizedList;	
	}
	
	public static void writeHashDatatoFile (HashMap<String,String> hmap , String name) // to save the Hashdata to file
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		int count = 1;
		try  (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name+"InvertedIndex_"+timeStamp+".txt"), "utf-8"))) {

			Iterator ir = hmap.entrySet().iterator();
			while(ir.hasNext())
			{
				Map.Entry<String, String> entry = (Entry<String, String>) ir.next();
				writer.write(entry.getKey()+":"+entry.getValue());
				((BufferedWriter) writer).newLine();
				count++;
			}

			System.out.println("Done with "+name+" file and total words added are :"+count);

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static List<String> getStopWords() throws IOException //Get the list of Stop words from the file 
	{
		File f1 = new File("C:/Users/Saya/Documents/NJIT_Course/Java_programs/IS634_InvertIndexing/StopWords.txt");
        BufferedReader br = new BufferedReader(new FileReader(f1));
        StringBuilder sb = new StringBuilder();
        List<String> stopWords = new ArrayList<String>();
        String line = null;
        while((line = br.readLine())!=null)
        {
        stopWords.add(line);
        }
        br.close();
     return stopWords;
		
	}
}
