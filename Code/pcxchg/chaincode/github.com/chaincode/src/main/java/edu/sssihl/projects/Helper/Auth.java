package edu.sssihl.projects.Helper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;


import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 





public class Auth  {

	
	 
	 
	synchronized public Boolean addmembers(String name) throws NoSuchAlgorithmException, NoSuchProviderException // only one thread can access this method at a time
	 {
		//create a randomly generated no varying from 5-10.
		//first search for the name in the registration and if it is present then add the details.
		
		
		try {
			System.out.print(name);
			String json_txt= new String(Files.readAllBytes(Paths.get("registration.json")));
			JSONObject json= new JSONObject(json_txt);

			if(json.get(name).toString().equals("yes")) // check for the varification 
			{
				System.out.println(json_txt);
				int max = 10; 
		        int min = 1; 
		        int range = max - min + 1; 
		       
		        // generate random numbers within 1 to 10 
		         int rand = (int)(Math.random() * range) + min; 
		  
		         // Output is different everytime this code is executed 
		         System.out.print(rand+" ");
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
		         json.put(name, "reg");
                 json.put("public_id", id);
                
                 try (FileWriter file= new FileWriter("registration.json"))
					{
						file.write(json.toString());
						file.flush();
					}
					catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                 
                 
                 String json_txt1= new String(Files.readAllBytes(Paths.get("authenticate.json")));
     		     JSONObject json1= new JSONObject(json_txt1);
		         JSONObject details= new JSONObject(); // of the each file
				 details.put("name",name);
				 details.put("password", rand);
			//	 details.put("public_key",b64PublicKey);
                 details.put("private_key", b64PrivateKey);
				 json1.put(id+"", details);
				 try (FileWriter file= new FileWriter("authenticate.json"))
					{
						file.write(json1.toString());
						file.flush();
					}
					catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				 
                                  //this is for ballot 
                 
                 String json_ballot= new String(Files.readAllBytes(Paths.get("Authenticated.json")));
     		     JSONObject ballot= new JSONObject(json_ballot);
     		     
     		     ballot.put(""+id, "yes");
     		    try (FileWriter file= new FileWriter("Authenticated.json"))
				{
					file.write(ballot.toString());
					file.flush();
				}
				catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
     		   String json_publilc= new String(Files.readAllBytes(Paths.get("public.json")));
   		       JSONObject pub= new JSONObject(json_publilc);
               pub.put(""+id, b64PublicKey);
               try (FileWriter file= new FileWriter("public.json"))
 				{
 					file.write(pub.toString());
 					file.flush();
 				}
 				catch (IOException e) {
 					// TODO: handle exception
 					e.printStackTrace();
 				}
                return true;
                

			}
			System.out.println("Registration failed");
			return false;
		
		} catch (IOException | JSONException e) {
			System.err.println(""+e);
			return false;
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	 

	 synchronized  public static String getPublickey(String id) throws IOException, JSONException {
		 String json_key= new String(Files.readAllBytes(Paths.get("authenticate.json")));
		 JSONObject json1= new JSONObject(json_key);
		 String pri= (String) new JSONObject(json1.get(id).toString()).get("public_key");
		 return pri;
		}
	 
	 
	 synchronized  public static boolean key(String id, String password) 
	 {
		 
		 String json_txt1;
		try {
			json_txt1 = new String(Files.readAllBytes(Paths.get("authenticate.json")));
			JSONObject json1= new JSONObject(json_txt1);
			if((boolean) new JSONObject(json1.get(id+"").toString()).get("password").toString().equals(password))
			  return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("tihis101");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Invalid user ");
		}
		
		return false;
	 }
	 
	 
	 public static void main(String[] args) throws IOException, JSONException, NoSuchAlgorithmException, NoSuchProviderException
	{	
		
		try { 
            // set windows look and feel 
            UIManager.setLookAndFeel(UIManager. 
                  getSystemLookAndFeelClassName()); 
        }  
        catch (Exception e) { 
        } 
  
		// createing a jsonobject to write to the file
		Auth obj= new Auth();
		JSONObject temp= new JSONObject();
		if(obj.addmembers("kanchi"))
		{
			System.out.println("it is not an error");
		}

		
		
    
	}
	 // this will create a ballot and gives it to voter
	
	
	
	synchronized  public static String getprivatekey(String id) throws IOException, JSONException
	 {
		 
		 String json_key= new String(Files.readAllBytes(Paths.get("authenticate.json")));
		 JSONObject json1= new JSONObject(json_key);
		 String pri= (String) new JSONObject(json1.get(id).toString()).get("private_key");
		return pri;
		 
	 
	 }
	
}
