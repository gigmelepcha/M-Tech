package edu.sssihl.projects.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

public class AsymmetricCryptography {
	
	private Cipher cipher;

	public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("RSA");
	}

	// https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
	public PrivateKey getPrivate(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	// https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
	public PublicKey getPublic(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	public void encryptFile(byte[] input, File output, PrivateKey key) 
		throws IOException, GeneralSecurityException {
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
		writeToFile(output, this.cipher.doFinal(input));
	}

	public void decryptFile(byte[] input, File output, PublicKey key) 
		throws IOException, GeneralSecurityException {
		this.cipher.init(Cipher.DECRYPT_MODE, key);
		writeToFile(output, this.cipher.doFinal(input));
	}

	private void writeToFile(File output, byte[] toWrite)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(toWrite);
		fos.flush();
		fos.close();
	}

	public String encryptText(String msg, PrivateKey key) 
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalBlockSizeException, 
			BadPaddingException, InvalidKeyException {
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
	}

	public String decryptText(String msg, PublicKey key)
			throws InvalidKeyException, UnsupportedEncodingException, 
			IllegalBlockSizeException, BadPaddingException {
		this.cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.decodeBase64(msg)), "UTF-8");
	}

	public byte[] getFileInBytes(File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		byte[] fbytes = new byte[(int) f.length()];
		fis.read(fbytes);
		fis.close();
		return fbytes;
	}

	public static void main(String[] args) throws Exception {
		AsymmetricCryptography ac = new AsymmetricCryptography();
		
		String json_txt= new String(Files.readAllBytes(Paths.get("key.json")));
		JSONObject json= new JSONObject(json_txt);
		
		String pub= (String) new JSONObject(json.get("108").toString()).get("public_key");
		System.out.println(pub);
		
		byte[] publicBytes = Base64.decodeBase64(pub);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		System.out.println(pubKey);
		
		String pri= (String) new JSONObject(json.get("108").toString()).get("private_key");

		
		byte[] privateBytes = Base64.decodeBase64(pri);
		PKCS8EncodedKeySpec keySpecs = new PKCS8EncodedKeySpec(privateBytes);
		KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
		PrivateKey privateke = keyFactory1.generatePrivate(keySpecs);
		System.out.println(privateke);

		//PrivateKey p=  (PrivateKey) new JSONObject(json.get("108").toString()).get("private_key");
	   // PublicKey pub= (PublicKey) new JSONObject(json.get("108").toString()).get("public_key");
//	    
//		PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
//		PublicKey publicKey = ac.getPublic("KeyPair/publicKey");
//        AsymmetricCryptography ac2= new AsymmetricCryptography();
//		PrivateKey privateKey2 = ac.getPrivate("KeyPair/privateKey2");
//		PublicKey publicKey2 = ac.getPublic("KeyPair/publicKey2");

//		
		String msg = "Cryptography is fun!";
		String encrypted_msg = ac.encryptText(msg, privateke);
		String decrypted_msg = ac.decryptText(encrypted_msg, pubKey);
//		System.out.println("This is the length of message:"+encrypted_msg.length());
		System.out.println("Original Message: " + msg + 
			"\nEncrypted Message: " + encrypted_msg
			+ "\nDecrypted Message: " + decrypted_msg);
//		
		
		
//		String msg = "Cryptography is fun!";
//		String encrypted_msg = ac.encryptText(msg, privateKey);
//		String decrypted_msg = ac.decryptText(encrypted_msg, publicKey);
//		System.out.println("This is the length of message:"+encrypted_msg.length());
//		System.out.println("Original Message: " + msg + 
//			"\nEncrypted Message: " + encrypted_msg
//			+ "\nDecrypted Message: " + decrypted_msg);
		
//		String en2= ac.encryptText(encrypted_msg, privateKey2);
//		String dec= ac.decryptText(en2, publicKey2);
//		
//		String fianl= ac.decryptText(dec, publicKey);
		
//		System.out.println("ec2 " + encrypted_msg + 
//				"\nEncrypted Message: " + en2
//				+ "\nDecrypted Message: " + dec +"\n final message:  "+fianl);

//		if (new File("KeyPair/text.txt").exists()) {
//			ac.encryptFile(ac.getFileInBytes(new File("KeyPair/text.txt")), 
//				new File("KeyPair/text_encrypted.txt"),privateKey);
//			ac.decryptFile(ac.getFileInBytes(new File("KeyPair/text_encrypted.txt")),
//				new File("KeyPair/text_decrypted.txt"), publicKey);
//		} else {
//			System.out.println("Create a file text.txt under folder KeyPair");
//		}
	}

}
