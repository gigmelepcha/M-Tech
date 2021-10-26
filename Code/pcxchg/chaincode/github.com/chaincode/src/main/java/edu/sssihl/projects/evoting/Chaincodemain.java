package edu.sssihl.projects.evoting;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import edu.sssihl.projects.Helper.AsymmetricCryptography;
import edu.sssihl.projects.Helper.Auth;
import edu.sssihl.projects.Helper.Ballot;
import edu.sssihl.projects.Helper.Count;
import edu.sssihl.projects.Helper.GenerateKeys;
import edu.sssihl.projects.Helper.Voter;

public class Chaincodemain extends ChaincodeBase {

		
	
	@SuppressWarnings("deprecation")
	public Response init(ChaincodeStub arg0) {
		return newSuccessResponse("Chaincode initialized successfully");	}
	
	
	public Response invoke(ChaincodeStub stub) 
	{
		
		
		String func= stub.getFunction();
		@SuppressWarnings("unchecked")
		List<String> params= (List<String>) stub.getParameters();
		if(func.equals("Registration"))
		{
			try {
				return Registration(stub, params);
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return newErrorResponse(e);
			}
		}
		else if(func.equals("update_candidates"))
			try {
				return update_candidates(stub,params);
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(func.equals("update_ballot"))
			try {
				return update_ballot(stub,params);
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(func.equals("get_ballot"))
			return get_ballot(stub,params);
		else if(func.equals("vote"))
			try {
				return vote(stub,params);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
					| IllegalBlockSizeException | BadPaddingException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if(func.contains("start"))  //pre-registration
			try {
			return start(stub,params);
			}catch (JSONException e) {
				// TODO: handle exception
			}
		else if(func.contains("add_members")) // add 
			try {
			return add_members(stub,params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		else if(func.contains("signup"))
			try {
				return signup(stub,params);
			}
		catch (Exception e) {
			return newErrorResponse("This is "+ e + "this is para"+params);
		}
		else if(func.contains("count"))
			try {
				return count(stub,params);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | JSONException
					| InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(func.contains("private"))
			return get_private(stub,params);
		else if(func.contains("public"))
			return get_public(stub, params);
		return null;
		
	}

	

	private Response get_private(ChaincodeStub stub, List<String> params) {
        String privvate= stub.getPrivateDataUTF8("collectionPrivate", "private");
        
		return newSuccessResponse("This is private data: "+privvate);
	}
	private Response get_public(ChaincodeStub stub, List<String> params) {
        String privvate= stub.getPrivateDataUTF8("collectionPublic", "public");
        
		return newSuccessResponse("This is public data: "+privvate);
	}


	private Response count(ChaincodeStub stub, List<String> args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, JSONException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		String candidate= stub.getStringState("candidates"), result= stub.getStringState("result"),pub= stub.getPrivateDataUTF8("collectionPublic", "public");
		Count c= new Count();
		try {
		c=c.counter(candidate, result, pub);
		
		int max= Collections.max(c.re.values());
		int l= c.getKey(c.re, max);
		
		return newSuccessResponse("Candidate's List \n"+c.hm+"\n Result to each candidates \n"+c.re +""
				+ "\n The maximum vote goes to "+c.hm.get(l));
		}
		catch(JSONException e)
		{
			return newErrorResponse("json exception "+e);
		}
	}


	@SuppressWarnings("deprecation")
	private Response signup(ChaincodeStub stub, List<String> args) throws NoSuchAlgorithmException, NoSuchProviderException {
		
		     if(args.size() != 1)
			  return newErrorResponse("Error: Wrong Parameter:Should be only one");
			String json_txt= "";

			
			json_txt = stub.getStringState("registered");	
	      //  return newSuccessResponse("this is reg: "+json_txt);
			
		   try {
				JSONObject json= new JSONObject(json_txt);

				if(json.get(args.get(0)).toString().equals("yes")) // check for the varification 
				{
					int max = 10; 
			        int min = 1; 
			        int range = max - min + 1; 
			       
			        // generate random numbers within 1 to 10 
			         int rand = (int)(Math.random() * range) + min;
			         rand=0;
			         // Output is different everytime this code is executed 
			         //fetching a public id from a file
			         int id= json.getInt("public_id");
			 		
			         // createing a jsonobject to write to the file
	                 // called a key generation for the user
			         GenerateKeys gk;
			         gk = new GenerateKeys(1024);
					 gk.createKeys();
					 
					 byte[] encodedPublicKey = gk.getPublicKey().getEncoded();
					 String b64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);
						
					byte[] encodedPrivateKey = gk.getPrivateKey().getEncoded();
					String b64PrivateKey = Base64.getEncoder().encodeToString(encodedPrivateKey);
					 
					 
			         id++;
			         json.put(args.get(0), "reg");
	                 json.put("public_id", id);
	                 stub.putStringState("registered", json.toString());
	                 
	                 
	                 
					 String j_private= stub.getPrivateDataUTF8("collectionPrivate", "private");
	     		     JSONObject json_pri= new JSONObject(j_private);
			         JSONObject details= new JSONObject(); // of the each file
					 details.put("name",args.get(0));
					 details.put("password", rand);
	                 details.put("private_key", b64PrivateKey);
					 json_pri.put(id+"", details);
					 
					 
					 
					 // puting the state in ledger
					 stub.putPrivateData("collectionPrivate", "private", json_pri.toString());
					 
	                                  //this is for ballot 
	                 
	                 String json_ballot = stub.getStringState("voters");
	     		     JSONObject ballot= new JSONObject(json_ballot);
	     		     
	     		     ballot.put(""+id, "yes");
	     		     stub.putStringState("voters", ballot.toString());
	     		     
	     		     
	     		   String pub_k= stub.getPrivateDataUTF8("collectionPublic", "public");
	               JSONObject pub_j= new JSONObject(pub_k);
	               pub_j.put(""+id, b64PublicKey);
	               stub.putPrivateData("collectionPublic", "public", pub_j.toString());
    	   			               
	               return newSuccessResponse("Successfully authicated "
	               		+ " Voters Details :  "
	            		   +"ID: "+id +" Name: "+args.get(0) + "pass word: "+rand);

				}
				if(json.get(args.get(0)).toString().equals("reg"))
					return newErrorResponse("Alredy Registration Done: ");
			return newErrorResponse("Voters registration Failed: Name did not  match");
			}
			catch(JSONException e)
			{
				return newErrorResponse("json exception "+e);
			}

	}


	private Response bae(String string) {
		
		return newSuccessResponse("check this arg : "+string);

	}


	private Response add_members(ChaincodeStub stub, List<String> args)  throws JSONException{
		if(args.size()!= 1)
		  return newErrorResponse("Not correct input:");
		try
		{
			String list= stub.getStringState("registered");
			JSONObject name= new JSONObject(list);
			if(name.has(args.get(0)))
				return newErrorResponse("Name already present");
			name.put(args.get(0), "yes");
			stub.putStringState("registered", name.toString());
			return(newSuccessResponse("Succefully registered: "+name.toString()));
		}
		catch(JSONException e)
		{
			return newErrorResponse("json exception "+e);
		}
		
	}


	private Response start(ChaincodeStub stub, List<String> args) throws JSONException {
		
		if(args.size() != 1)
		 return newErrorResponse("Wrong argument");
		String json_txt= "",name="";
		try
		{
			JSONObject json= new JSONObject();
			JSONObject auth= new JSONObject();
			int count= Integer.parseInt(args.get(0)), id= 0;
			while(count!= 0)
			{
				name= RandomStringUtils.randomAlphabetic(1);
				if(!json.has(name))
				{
					json.put(name, "yes");
					id++;
					count--;
				}	
	
			}
	        json.put("public_id", 1);
			stub.putStringState("registered", json.toString());
			stub.putStringState("voters", auth.toString());
			stub.putStringState("result", auth.toString());
			stub.putPrivateData("collectionPrivate", "private", auth.toString());
			stub.putPrivateData("collectionPublic", "public", auth.toString());

	
			return newSuccessResponse("Generated peoples with count "+json.toString());
		}
		catch(JSONException e)
		{
			return newErrorResponse("json exception "+e);
		}
	}
   
	 

	private Response vote(ChaincodeStub stub, List<String> args) throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		if(args.size() != 3)
			return newErrorResponse("Please give correct inputs");
		try
		{
			
		
		ObjectMapper mapper= new ObjectMapper();
		//String jsonString= null;
		String id= args.get(0),pass= args.get(1),s=args.get(2);

		String json_key= stub.getPrivateDataUTF8("collectionPrivate", "private");
		JSONObject json1= new JSONObject(json_key);
		JSONObject pas= (JSONObject) json1.get(id);
     	//return newErrorResponse("id:"+id+" pass: "+pass+" s: "+s+" json1: "+json1+"pas: "+pas.get("password").toString());
        
        
			
		if(Objects.equals(args.get(1), pas.get("password").toString()))
		{
		
				String json_txt= stub.getStringState("ballot");
				JSONObject json= new JSONObject(json_txt);
				Ballot t1= mapper.readValue(json_txt,Ballot.class);
				if(json.getJSONObject("ballot").has(""+id))
				{
					// now encrypt the ballot 
					
		            
			        // authentication for the voters
					AsymmetricCryptography ac = new AsymmetricCryptography();
//		
//					
//					
						String json_ballot= stub.getStringState("result");
						JSONObject j_ballot= new JSONObject(json_ballot);
						if(j_ballot.has(""+id))
						{
							return newErrorResponse("Vote is already cast");
						}
						
//						// public key not needed 
//						
						 String pri= (String) new JSONObject(json1.get(id).toString()).get("private_key");

						
						byte[] privateBytes = Voter.decode(pri);
						PKCS8EncodedKeySpec keySpecs = new PKCS8EncodedKeySpec(privateBytes);
						KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
						PrivateKey privateke = keyFactory1.generatePrivate(keySpecs);
						
						
						String encrypted_msg = ac.encryptText(s, privateke);
						
						j_ballot.put(id+"", encrypted_msg);
//						// for now
//						
//						
						stub.putStringState("result", j_ballot.toString());
					    return newSuccessResponse("vote is cast");
					
				}
				else
				{
					return newErrorResponse("NO ballot found: "+json.get(id));
				}
		}
		else
	     return newErrorResponse("Wrong password or user id "+id+" pass->"+pass+"real pass is "+pas.get("password").toString() + "After ccomparing the  result is "+Objects.equals(args.get(1), pas.get("password").toString()));
		}
		catch(JSONException e)
		{
			return newErrorResponse("json exception "+e);
		}
	}


	


	private Response get_ballot(ChaincodeStub stub, List<String> args) {

		// show the candidates for the voters
		if(args.size() > 2)
			return newErrorResponse("Error");
		String candicate= stub.getStringState("candidates");
		return newSuccessResponse(candicate);	
		}


	private Response update_ballot(ChaincodeStub stub, List<String> args) throws JSONException, IOException {
	   
		
		if(args.size() != 0)
			  return newErrorResponse("Error: Wrong Parameter:Should be  None");
		
		HashMap<Integer, HashMap<Integer, String>> hm= new HashMap<>(); // ballot
		
		String candidate= stub.getStringState("candidates");
		HashMap<Integer, String> hm1 = Ballot.create_map(candidate);
		String json_txt= stub.getStringState("voters");
		JSONObject json= new JSONObject(json_txt);
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) json.keys();
		while (keys.hasNext()) {
		    String key = keys.next();
            hm.put(Integer.parseInt(key), hm1);	    
		    
		}
		
		Ballot t= new Ballot(hm);
	    stub.putStringState("ballot", new ObjectMapper().writeValueAsString(t));
		

		return newSuccessResponse("ballot created for every voters: "+hm.toString());
	}


	private Response update_candidates(ChaincodeStub stub, List<String> args) throws JSONException, IOException {
		
		if(args.size() == 1)
			  return newErrorResponse("Error: Wrong Parameter:Should be more than one");
		JSONObject json= new JSONObject();
		for (int i= 0; i < args.size(); i++) 
			json.put(""+(i+1), args.get(i));
		stub.putStringState("candidates", json.toString());

		return newSuccessResponse("Candidates updated: "+json.toString());
			
	}


	


	private Response Registration(ChaincodeStub stub, List<String> args) throws  IOException {
		
		// this is registration for people 
		

		return null;
	}

 	



	public static void main(String[] args) {
		
		System.out.println("This is start ");
		new Chaincodemain().start(args);  // this will invoke all the methods
		//new Chaincode().invoke(ChaincodeStub);
	}
	
}