package org.blazer.common.util;

import org.blazer.common.conf.Conf;
import org.blazer.common.conf.ConfUtil;

/**
 * @author Blazer He
 * @date__ 2015年7月24日
 */
public class SysConfig {

	protected static final Conf conf;

	public static boolean test = false;

	static {
		String sysConfPath = PathUtil.resource + "sys-config.properties";
		conf = ConfUtil.getConf(sysConfPath);
		try {
			test = Boolean.parseBoolean(get("test"));
		} catch (Exception e) {
		}
	}

	public static String get(String key) {
		return conf.get(key);
	}

}
