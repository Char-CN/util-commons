package org.blazer.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String getStrEmpty(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String getStr(Object o) {
		return o == null ? null : o.toString();
	}

	public static String[] removeIndex(String[] args, int index) {
		if (args == null || args.length == 0) {
			return null;
		} else if (args.length <= index) {
			return null;
		} else if (index == 0) {
			String[] retArgs = new String[args.length - 1];
			System.arraycopy(args, 1, retArgs, 0, retArgs.length);
			return retArgs;
		}
		String[] retArgs = new String[args.length - 1];
		int successIndex = 0;
		for (int i = 0; i < args.length; i++) {
			if (index != i) {
				retArgs[successIndex] = args[i];
				successIndex++;
			}
		}
		return retArgs;
	}

	public static String findOneStrByReg(final String str, final String reg) {
		try {
			return findStrByReg(str, reg).get(0);
		} catch (IndexOutOfBoundsException e) {
		}
		return null;
	}

	public static List<String> findStrByReg(final String str, final String reg) {
		List<String> list = new ArrayList<String>();
		if (str == null || reg == null) {
			return list;
		}
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				list.add(m.group(i));
			}
		}
		return list;
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

	public static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static boolean isNotBlank(String str) {
		return !(str == null || str.trim().isEmpty());
	}

}
