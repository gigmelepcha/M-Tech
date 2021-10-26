package edu.sssihl.projects.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;











public class Count
{
	
	public HashMap<Integer, String> hm= new HashMap<>();
	public HashMap<Integer, Integer> re= new HashMap<>();
	
	public Count()
	{
		hm= null;
		re= null;
			
	}
	public Count(HashMap<Integer, String> m,HashMap<Integer, Integer> r)
	{
		hm= m;
		re= r;
	}
	public Count getCount()
	{
		return this;
	}
	public void setCount(HashMap<Integer, String> m,HashMap<Integer, Integer> r)
	{
		this.hm=m;
		this.re= r;
	}
	public static <K, V> K getKey(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	public static Count counter(String candidates,String result_ballot,String pub_key) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, JSONException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
        AsymmetricCryptography ac = new AsymmetricCryptography();

        Count c= new Count();
		HashMap<Integer, String> hm= new HashMap<>();
		HashMap<Integer, Integer> re= new HashMap<>();
		String json_fill= candidates;
		JSONObject can= new JSONObject(json_fill);
		@SuppressWarnings("unchecked")
		Iterator<String> k = (Iterator<String>) can.keys();
		while (k.hasNext()) {   // get the list of candidate 
		    String ke = k.next();
	//	    JSONObject value = json.getJSONObject(key);
            hm.put(Integer.parseInt(ke), can.get(ke).toString());	  
    		re.put(Integer.parseInt(ke), 0);

		}
		
		
		
		long  g= 0;
		String json_txt= result_ballot;
		JSONObject json= new JSONObject(json_txt);
		String j_key= pub_key;
		JSONObject js= new JSONObject(j_key);
		//iterate to each vote
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) json.keys();
		while (keys.hasNext()) {
		    String key = keys.next();
	//	    JSONObject value = json.getJSONObject(key);
		    
			
			
		//	JSONObject id= new JSONObject(js.get(key));
			String pub= js.getString(key);
			
			byte[] publicBytes = Base64.decodeBase64(pub);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			String result= ac.decryptText(json.get(key).toString(), pubKey);
			g= re.get(Integer.parseInt(result));
			re.put(Integer.parseInt(result), (int) (g+1));
		}
		
		c.setCount(hm, re);
		return c;
	}
   public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, JSONException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException
   {
	   Count c= new Count();
	  // c.counter();
   }
}
