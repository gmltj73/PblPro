package co.junwei.cpabe;

import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCoder {

	private static byte[] getRawKey(byte[] seed) throws Exception {

		byte[] salt = new String("12345678").getBytes();//salt 값에 대한 테스트 필요함.
		int iterationCount = 1024;
		int keyStrength = 128;
		SecretKey key;
		byte[] iv;

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(Arrays.toString(seed).toCharArray(), salt, iterationCount, keyStrength);
		SecretKey tmp = factory.generateSecret(spec);

		byte[] raw  = tmp.getEncoded();
		//Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		System.err.println("sym KeyGen seed = "+ Arrays.toString(seed));
		System.err.println("keygen. sym key = "+ Arrays.toString(raw));

		return raw;
	}

	public static byte[] encrypt(byte[] seed, byte[] plaintext)
			throws Exception {
		byte[] raw = getRawKey(seed);
		String str = Arrays.toString(seed);

		System.err.println("sym enc seed = "+ str);

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		IvParameterSpec ivSpec = new IvParameterSpec(raw);
		System.err.println("sym enc key = "+ Arrays.toString(raw));
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(plaintext);
		return encrypted;
	}

	public static byte[] decrypt(byte[] seed, byte[] ciphertext)
			throws Exception {
		byte[] raw = getRawKey(seed);

		String str = Arrays.toString(seed);

		System.err.println("sym dec seed = "+ str);

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		//AES/CBC/PKCS5Padding
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		System.err.println("sym dec key = "+ Arrays.toString(raw));
		IvParameterSpec ivSpec = new IvParameterSpec(raw);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(ciphertext);

		return decrypted;
	}
}