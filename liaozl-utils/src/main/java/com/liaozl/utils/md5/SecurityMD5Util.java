package com.liaozl.photo.util.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密类
 * 
 * @author yangmin
 * 
 */
public class SecurityMD5Util {

	private static SecurityMD5Util instance;

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private SecurityMD5Util() {
	}

	private String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 加密字符串
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String MD5Encode(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byteArrayToHexString(md.digest(str.getBytes()));
	}

	public static SecurityMD5Util getInstance() {
		if (instance == null) {
			instance = new SecurityMD5Util();
		}
		return instance;
	}

	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(
					1, 3));
		}
		return sb.toString();
	}

	public static String md5(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex(md.digest(text.getBytes()));
		} catch (NoSuchAlgorithmException e) {
		}
		return text;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String newPassword = SecurityMD5Util.getInstance().MD5Encode("201473");
		System.out.println(newPassword);
	}
}
