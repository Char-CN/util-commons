package org.blazer.common.run;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.blazer.common.conf.Conf;
import org.blazer.common.conf.ConfUtil;
import org.blazer.common.util.ClassPathHacker;
import org.blazer.common.util.PathUtil;

/**
 * 执行入口
 * 
 * 定义：
 * 
 * 如果jar文件下有目录resource/sys-pkg-register.properties
 * 
 * 则会读取该配置文件，获取 映射名称<<==>>classPath 的关系，执行该Class
 * 
 * 如果jar文件下有目录extlib/*.jar
 * 
 * 则会将该jar文件引入当前环境
 * 
 * @author heyunyang
 * 
 */
public class RunManager {

	private RunManager() {
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("==RunManager== " + "Usage: java -jar pkg.mainclass [parameters...]");
			System.exit(-1);
		}
		String pkgMainClass = convertPkgMainClassMapping(args[0]);
		try {
			Method mainMethod = Class.forName(pkgMainClass).getMethod("main", String[].class);
			// 加载第三方Driver
			addExtLib();
			// 调用
			mainMethod.invoke(null, (Object) removeIndex(args, 0));
		} catch (Exception e) {
			System.out.println("==RunManager== " + e.getMessage() + e);
			e.printStackTrace();
		}
	}

	private static String convertPkgMainClassMapping(String pkgMainClass) {
		try {
			Conf conf = ConfUtil.getConf(PathUtil.resource + "sys-pkg-register.properties");
			if (conf.containsKey(pkgMainClass)) {
				System.out.println("==RunManager== " + "found pkg register : " + pkgMainClass + " to " + conf.get(pkgMainClass));
				pkgMainClass = conf.get(pkgMainClass);
			}
		} catch (Exception e) {
			System.out.println("==convertPkgMainClassMapping ERROR==");
			e.printStackTrace();
		}
		return pkgMainClass;
	}

	public static void addExtLib() {
		try {
			String names[] = findFileNameRegexp(PathUtil.root + "extlib", ".*[.]jar");
			for (int i = 0; i < names.length; i++) {
				System.out.println("==RunManager== " + "classpath add jar: " + PathUtil.root + "extlib/" + names[i]);
				try {
					ClassPathHacker.addFile(PathUtil.root + "extlib/" + names[i]);
				} catch (Exception e) {
					System.out.println("==RunManager== " + e.getMessage() + e);
				}
			}
		} catch (Exception e) {
			System.out.println("==RunManager== " + e.getMessage() + e);
		}
	}

	private static String[] removeIndex(String[] args, int index) {
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

	private static String[] findFileNameRegexp(String filePath, String regexp) {
		File directory = new File(filePath);
		if (!directory.isDirectory()) {
			return new String[0];
		}
		final Pattern p = Pattern.compile(regexp);
		String[] names = directory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return p.matcher(name).matches();
			}
		});
		Arrays.sort(names);
		return names;
	}

}
