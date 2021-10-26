package edu.sssihl.projects.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class GenerateKeys {

	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keylength);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	public static void main(String[] args) throws JSONException {
		GenerateKeys gk;
		GenerateKeys gk1;
		try {
			gk = new GenerateKeys(2048);
			gk1 = new GenerateKeys(2048);
			gk.createKeys();
			System.out.println("This is public key \n"+gk.getPublicKey());
			
			byte[] encodedPublicKey = gk.getPublicKey().getEncoded();
			String b64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);
			
			byte[] encodedPrivateKey = gk.getPrivateKey().getEncoded();
			String b64PrivateKey = Base64.getEncoder().encodeToString(encodedPrivateKey);
			
			String json_txt= new String(Files.readAllBytes(Paths.get("key.json")));
			JSONObject json= new JSONObject(json_txt);
			JSONObject details= new JSONObject(); // of the each file
			
			 details.put("name","gigme");
			 details.put("password", 12);
			 details.put("public_key",b64PublicKey);
            details.put("private_key", b64PrivateKey);
			 json.put("108", details);
			 try (FileWriter file= new FileWriter("key.json"))
				{
					file.write(json.toString());
					file.flush();
				}
				catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
			gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
			
			gk1.createKeys();
			System.out.println("This is public key \n"+gk1.getPublicKey());
			gk1.writeToFile("KeyPair/publicKey2", gk1.getPublicKey().getEncoded());
			gk1.writeToFile("KeyPair/privateKey2", gk1.getPrivateKey().getEncoded());
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	} 
}
