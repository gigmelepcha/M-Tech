package edu.sssihl.projects.Helper;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;




public class Ballot 
{
	private HashMap<Integer, HashMap<Integer, String>> ballot;	
	
	// class methods  
	// constructor default
	public Ballot()
	{
	
		ballot= null;
	}
	// constructor with parameters
	public Ballot(HashMap<Integer, HashMap<Integer, String>> b) {
		this.ballot= b;
	}

	// printing the class data
	public void print() {
		System.out.println(" ballot: "+this.ballot);
	}

	// Getter
	
	public HashMap<Integer, HashMap<Integer, String>> getBallot() {
		return this.ballot;
	}

	// Setter
	
	public void setBallot(HashMap<Integer, HashMap<Integer, String>> b) {
		this.ballot= b;
	}
	
	
	// create a ballot with specified candidate 
	public static HashMap<Integer, String> create_map(String candidate) throws IOException, JSONException
	{
		
		HashMap<Integer, String> hm= new HashMap<>();
		JSONObject json= new JSONObject(candidate);
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) json.keys();
		while (keys.hasNext()) {
		    String key = keys.next();
            hm.put(Integer.parseInt(key),json.get(key).toString() );	    
		    
		}
		
		return hm;
		
	}
	


	public static  String create_candidate(ArrayList<String> names) throws IOException, JSONException
	{
		
//		String json_txt= new String(Files.readAllBytes(Paths.get("Candidates.json")));
		JSONObject json= new JSONObject();
		for (int i= 0; i < names.size(); i++) 
			json.put(""+(i+1), names.get(i));
		return(json.toString());
		
	}
	
	public static void main(String[] args) throws JSONException, IOException {
		new Ballot();
	}
}
