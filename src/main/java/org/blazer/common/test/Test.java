package org.blazer.common.test;

import java.io.IOException;

import org.blazer.common.conf.Conf;
import org.blazer.common.conf.ConfUtil;
import org.blazer.common.util.PathConstant;
import org.blazer.common.util.SysConfig;

/**
 * @author Blazer He
 * @__date 2016年4月27日
 */
public class Test extends SysConfig {

	public static void main(String[] args) throws IOException {
		Conf conf = ConfUtil.getConf(PathConstant.sysConfig);
		System.out.println(conf.invalidList());
	}

}
