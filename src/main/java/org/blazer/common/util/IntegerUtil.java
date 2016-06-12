package org.blazer.common.util;

public class IntegerUtil {

	public static Integer parseInt0(Object obj) {
		if (obj == null) {
			return 0;
		}
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
		}
		return 0;
	}

	public static Integer parseInt(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
		}
		return null;
	}

}
