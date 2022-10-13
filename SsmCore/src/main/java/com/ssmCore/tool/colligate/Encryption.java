package com.ssmCore.tool.colligate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密工具
 *
 */
public class Encryption {

	public final static String MD5 = "MD5";
	public final static String SHA1 = "SHA-1";
	public final static String ALGO = "AES";
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM_MD5 = "MD5withRSA";
	public static final String SIGNATURE_ALGORITHM_SHA1 = "SHA1withRSA";
	public static final String SIGNATURE_ALGORITHM_SHA2 = "SHA2withRSA";

	public static final String HMAC_SHA1 = "HmacSHA1";
	// /**
	// * 获取公钥的key
	// */
	// private static final String PUBLIC_KEY = "RSAPublicKey";
	//
	// /**
	// * 获取私钥的key
	// */
	// private static final String PRIVATE_KEY = "RSAPrivateKey";
	//
	// /**
	// * RSA最大加密明文大小
	// */
	// private static final int MAX_ENCRYPT_BLOCK = 117;
	//
	// /**
	// * RSA最大解密密文大小
	// */
	// private static final int MAX_DECRYPT_BLOCK = 128;

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteArrayToHexString(byte[] b) {

		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 加密
	 * 
	 * @param origin
	 *            要加密的字串
	 * @param type
	 *            加密类型
	 * @return
	 */
	public static String Encode(String origin, String type) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance(type);
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	/**
	 * HmacSHA1加密
	 * 
	 * @param data
	 *            要加密的字串
	 * @param key
	 * @param type
	 *            加密类型
	 * @return
	 * @throws Exception
	 */
	public static String MAC_SHA1(String data, String key, String type) {
		String hexBytes = null;
		try {
			byte[] keyBytes = key.getBytes();
			// 根据给定的字节数组构造一个密钥。
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, type);
			Mac mac = Mac.getInstance(HMAC_SHA1);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			hexBytes = (new sun.misc.BASE64Encoder()).encode(rawHmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hexBytes;
	}

	public static String byte2hex(final byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			// 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式。
			stmp = (java.lang.Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs;
	}

	/**
	 * 加密
	 * 
	 * @param origin
	 *            要加密的字串
	 * @param type
	 *            加密类型
	 * @return
	 */
	public static String Encode64(String origin, String type) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance(type);
			resultString = Base64.encodeBase64String(md.digest(origin.getBytes("UTF-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	public static String getBASE64(String s) {
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	public static String Encode16(String origin, String type) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance(type);
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
			resultString = StringUtils.substring(resultString, 8, 24);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	public static String encrypt(String context, String iv) throws Exception {
		String key = Encode(context, MD5);
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		int blockSize = cipher.getBlockSize();

		byte[] dataBytes = context.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}

		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		byte[] encrypted = cipher.doFinal(plaintext);

		return new sun.misc.BASE64Encoder().encode(encrypted);
	}

	public static String desEncrypt(String data, String iv) throws Exception {

		String key = "1234567812345678";

		byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original);
		return originalString;
	}

	/**
	 * 用来进行加密的操作
	 * 
	 * @param Data
	 * @return
	 * @throws Exception
	 */
	// {0x12, 0x34, 0x56, 0x78, 0x90, 0xAB, 0xCD, 0xEF}
	public static String encrypt1(String Data, String val) throws Exception {
		Key key = generateKey(val.getBytes());
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData, String val) throws Exception {
		Key key = generateKey(val.getBytes());
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	/**
	 * 用来进行解密的操作
	 * 
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 解密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 字符中添加一个解密密钥 并压缩数据
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	// public static byte[] Encrypt(Object obj) throws Exception {
	// String aseKey = RegExp.UUID16();
	// String context = new String(JsonTransfer.getJson(obj).getBytes(),
	// "UTF-8");
	// context = encrypt(context, aseKey);
	// StringBuffer buffter = new StringBuffer(context);
	// buffter.insert(context.length()-8, aseKey);
	//// return context.getBytes();
	// return LzmaRar.CompressStringLZMA(context);
	// }

	private static Key generateKey(byte[] val) throws Exception {
		Key key = new SecretKeySpec(val, ALGO);
		return key;
	}

	public static final byte[] zip(String str) throws IOException {
		if (str == null)
			return null;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes("UTF-8"));
			zout.closeEntry();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
			zout.close();
		}
		return null;
	}

	/**
	 * RSA数字签名(md5)校验
	 * 
	 * @param data
	 *            数据
	 * @param publicKey
	 *            密钥
	 * @param sign
	 *            签名
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String publicKey, String sign, String type) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = new BASE64Decoder().decodeBuffer(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			Signature signature = Signature.getInstance(type);
			signature.initVerify(pubKey);
			signature.update(data.getBytes("utf-8"));
			boolean bverify = signature.verify(new BASE64Decoder().decodeBuffer(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static KeyPair genKeyPair(int keyLength) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keyLength);
		return keyPairGenerator.generateKeyPair();
	}
}
