package org.blazer.common.util;

public class StringUtil {

	public static String getString(Object o) {
		return getString(o, false);
	}

	public static String getString(Object o, boolean defaultEmpty) {
		return defaultEmpty ? o == null ? "" : o.toString() : o == null ? null : o.toString();
	}

	public static String trimAll(String str) {
		return str.replaceAll("\\s", "");
	}

	public static String union(Object... objs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < objs.length; i++) {
			sb.append(objs[i]);
		}
		return sb.toString();
	}

	public static String toLog(String str, String... strs) {
		for (int i = 0; i < strs.length; i++) {
			str = str.replaceFirst("\\{\\%\\}", strs[i]);
		}
		return str;
	}

}
