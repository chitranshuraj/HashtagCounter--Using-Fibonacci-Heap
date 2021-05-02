/**
 * 
 * @author Chitranshu Raj
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;



class hashtagcounter {
	static Read_Write_TaskHandler hcounter=new Read_Write_TaskHandler();
	public static void main(String[] args) {
		String inp_file=args[0];
		if(args.length<2){
			String out_file="";
			hcounter.read_file(inp_file,out_file);
		}
		else{
			hcounter.read_file(inp_file,args[1]);
		}
	}
}

class Read_Write_TaskHandler {
	static String in_file="";
	static String out_file="";
	File return_file = new File("Out.txt");
	
	/* MAX FIBONACCI HEAP Initialized */
	
	FibonacciHeap fheap = new FibonacciHeap();
	
	/* HashTable is to be initialized to store the hashtags 
	 * and the respective nodes.
	 */
	 
	Hashtable<String, Node> htable = new Hashtable<String, Node>();
	
	/*
	 * An Array is to be initialized to store the removed nodes
	 * in one place.
	 */
	 
	ArrayList<Node> removed_nodes = new ArrayList<Node>();


	/*
	 * The read_file operation takes as parameters the input_file name 
	 * and the output_file name as arguments.
	 * It generates the output using generate method which is embedded 
	 * in the read_file method.
	 */
	 
	public void read_file(String input_file,String output_file) {
		try {
			in_file=input_file;
			out_file=output_file;
			BufferedReader in = new BufferedReader(new FileReader(in_file));
			String in_line;
			while ((in_line = in.readLine()) != null) {
				
				/* Checks if the line starts with "#" */
				if(in_line.startsWith("#")){
					
					/* It splits the line as the hashtags are encountered and stores hashtags 
					 * in 'htag' based on white spaces encountered 
					 */
					String htag=in_line.split(" ")[0].substring(1, in_line.split(" ")[0].length());
					
					/* The hashtag is followed by the frequency of its occurence,
					 * the following is stored as 'freq'
					 */
					int freq=Integer.parseInt(in_line.split(" ")[1]);
					
					/* Add the node recursively until no other hashtag is encountered */
					if(!htable.containsKey(htag)){
						Node new_n=new Node(freq, htag);
						fheap.add_node(new_n, freq);
						htable.put(htag,new_n);
					}
					
					/* If the same node exists in the hashtable, then increment the frequency 
					 * respectively
					 */
					else{
						int newfreq=htable.get(htag).GET_KEY()+freq;
						Node x=fheap.increment_key(htable.get(htag),newfreq);
						htable.remove(htag);
						htable.put(htag, x);
					}       
				}
				else {
					/* If "stop" keyword is encountered, terminate the program */
					if(in_line.equalsIgnoreCase("stop")){
						return;
					}
					else{  
						/* If no "stop" is encountered, generate max node list */
						generate(in_line);
					}
				}
			}
		}
		catch (FileNotFoundException e) {          
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * The write method fetches the most occuring hashtags and 
	 * puts them into the specified output file recursively.
	 */
	public void write(File output_file, String out_line){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(out_file, true));
			bw.write(out_line);
			bw.newLine();
			bw.flush();
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally 
		{                      
			if (bw != null)
			{
				try {
					bw.close();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * The generate method is used to generate a max fibonacci heap
	 * and it recursively invokes the remove max method to find the 
	 * most frequent hastags in the input line.
	 */
	public void generate(String q_str){
		int q=0;
		try{
			q=Integer.parseInt(q_str) ;
		}
		catch(NumberFormatException e){
			e.getStackTrace();
		}
		String out_line="";
		while(q>0){		
			/* The remove max operation is invoked here */
			Node MaxNode=fheap.removeMAX_operation();
			Node removedNode=new Node(MaxNode.GET_KEY(),MaxNode.GET_TAG());
			removed_nodes.add(removedNode);
			String MaxN_htag = removedNode.GET_TAG();
			/* The max node is removed from the hashtable */
			htable.remove(MaxN_htag);
			/* An output line is initialized, which stores the removed max nodes */
			out_line=out_line+MaxN_htag+",";
			q--;
		}
		if(out_file.equals("")){
				System.out.println(out_line);
		}
		else{
			return_file=new File(out_file);
			/* The write operation is invoked to capture the most frequent hashtags */
			write(return_file,out_line.substring(0, out_line.length()-1));
		}
		for(int remncount=0;remncount<removed_nodes.size();remncount++){
			fheap.add_node(removed_nodes.get(remncount), removed_nodes.get(remncount).GET_KEY());
			htable.put(removed_nodes.get(remncount).GET_TAG(), removed_nodes.get(remncount));
		}
		/* Clear the removed nodes list after the generate process terminates */
		removed_nodes.clear();
	}
}





