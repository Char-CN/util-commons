package org.blazer.common.test;

import java.io.IOException;

import org.blazer.common.conf.Conf;
import org.blazer.common.conf.ConfUtil;

/**
 * @author Blazer He
 * @__date 2016年4月27日
 */
public class Test {

	public static void main(String[] args) throws IOException {
		Conf conf = ConfUtil.getConf("/test.conf");
		System.out.println(conf.invalidList());
		System.out.println(conf.keyList());
	}

}
