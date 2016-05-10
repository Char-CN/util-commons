package org.blazer.common.conf;

import java.util.HashMap;

public class ConfUtil {

	private static HashMap<String, Conf> map;

	static {
		map = new HashMap<String, Conf>();
	}

	public static void loadConf(String filePath, String... args) {
		if (!map.containsKey(filePath)) {
			Conf conf = Conf.createConf(filePath, args);
			map.put(filePath, conf);
		}
	}

	public static Conf getConf(String filePath, String... args) {
		loadConf(filePath, args);
		return map.get(filePath);
	}

}
