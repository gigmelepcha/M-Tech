package edu.sssihl.projects.Helper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Iterator;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;




// this class will read the ballot and cast a vote 
public class Voter {

	
	public static byte[] decode(String pri)
	{
		return Base64.decodeBase64(pri);
				
	}
	
	public void caste_voting(int id,int pass,String s) throws IOException, JSONException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
	// get ballot 
		

		ObjectMapper mapper= new ObjectMapper();
		//String jsonString= null;
		
		String json_txt= new String(Files.readAllBytes(Paths.get("count.json")));
		JSONObject json= new JSONObject(json_txt);
		Ballot t1= mapper.readValue(json_txt,Ballot.class);
		if(t1.getBallot().get(id) != null)
		{
			// now encrypt the ballot 
			
            
	        // authentication for the voters
			AsymmetricCryptography ac = new AsymmetricCryptography();
// 
			
			if(Auth.key(""+id,""+pass))
			{
				String json_ballot= new String(Files.readAllBytes(Paths.get("ballot.json")));
				JSONObject j_ballot= new JSONObject(json_ballot);
				if(j_ballot.has(""+id))
				{
					System.out.println("Vote is already cast");
					return;
				}
				
				// public key not needed 
				
				String pri= Auth.getprivatekey(""+id);

				
				byte[] privateBytes = Base64.decodeBase64(pri);
				PKCS8EncodedKeySpec keySpecs = new PKCS8EncodedKeySpec(privateBytes);
				KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
				PrivateKey privateke = keyFactory1.generatePrivate(keySpecs);
				
				
				String encrypted_msg = ac.encryptText(s, privateke);
				
				System.out.println("This is the original message"+s+"\n Encrypted: "+encrypted_msg);
				
				System.out.println(" Candidate chosen is "+s);
				
				
				j_ballot.put(id+"", encrypted_msg);
				// for now
				
				
				 try (FileWriter file= new FileWriter("ballot.json"))
					{
						file.write(j_ballot.toString());
						file.flush();
					}
					catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			}
			else
			{
				System.out.println("Invalid password ");
			}
			
		}
		else
		{
			System.out.println("NO ballot found ");
		}
		
		
	
	}
	public static void main(String[] args) throws IOException, JSONException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException
	{
	   // Registration 
		
		Voter v= new Voter();
		v.caste_voting(7,3,"2");		
		// voting phase
	}
	
}
