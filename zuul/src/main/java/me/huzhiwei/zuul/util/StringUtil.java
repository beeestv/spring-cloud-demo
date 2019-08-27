package me.huzhiwei.zuul.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 14:06
 */
public class StringUtil extends StringUtils {

	public static String generatePath(String... part) {
		String separator = "/";
		return separator + join(part, separator);
	}

	public static String md5(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(s.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100), 1, 3);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(md5("1"));
	}
}
