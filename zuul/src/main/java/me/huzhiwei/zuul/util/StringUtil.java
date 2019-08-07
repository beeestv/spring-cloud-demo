package me.huzhiwei.zuul.util;

import org.apache.commons.lang.StringUtils;

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
}
