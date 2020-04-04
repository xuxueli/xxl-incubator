package com.xxl.util.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * rsa util
 *
 * @author xuxueli 2018-04-02 15:47:47
 */
public class RSAUtil {

	private static final String ALGORITHM = "RSA";
	private static final int KEYSIZE = 1024;


	/**
	 * 生成RSA密钥对
	 *
	 * @return
	 */
	public static KeyPair generateKeyPair() {
		KeyPairGenerator keyPairGenerator = null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGenerator.initialize(KEYSIZE);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	/**
	 * base64 String 2 Key
	 *
	 * @param Key
	 * @return
	 */
	public static String getKeyStr(Key Key){
		Base64 base64 = new Base64();
		String publicKeyStr = base64.encodeToString(Key.getEncoded());
		return publicKeyStr;
	}

	/**
	 * base64 String 2 publicKey
	 *
	 * @param publicKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKeyStr)  {
		try {
			byte[] keyBytes;
			//keyBytes = (new BASE64Decoder()).decodeBuffer(publicKeyStr);
			Base64 base64 = new Base64();
			keyBytes = base64.decode(publicKeyStr);

			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * base64 String 2 publicKey
	 *
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKeyStr) {
		try {
			byte[] keyBytes;
			//keyBytes = (new BASE64Decoder()).decodeBuffer(privateKeyStr);
			Base64 base64 = new Base64();
			keyBytes = base64.decode(privateKeyStr);

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * RSA加密（使用RSA公钥）
	 *
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String encode(String data, Key publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA解密（使用RSA私钥）
	 *
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public static String decode(String data, Key privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(Base64.decodeBase64(data)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		KeyPair keyPair = generateKeyPair();

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String publicKeyStr = getKeyStr(publicKey);
		String privateKeyStr = getKeyStr(privateKey);

		System.out.println("publicKeyStr=" + publicKeyStr);
		System.out.println("privateKeyStr=" + privateKeyStr);

		Key publicKey2 = getPublicKey(publicKeyStr);
		Key privateKey2 = getPrivateKey(privateKeyStr);

		String temp = "data123明文";
		temp = encode(temp, publicKey2);
		System.out.println("密文=" + temp);
		temp = decode(temp, privateKey2);
		System.out.println("明文=" + temp);

	}

}