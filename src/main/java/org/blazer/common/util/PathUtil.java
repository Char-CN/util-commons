package org.blazer.common.util;

import java.io.File;

public class PathUtil {

	public static final String root;

	public static final String resource;

	static {
		String _root = System.getProperty("user.dir");
		String _resource = System.getProperty("user.dir");
		_root = PathUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		_root = _root.substring(0, _root.lastIndexOf("/"));
		// filter in the eclipse have /bin folder
		if (_root.lastIndexOf("/bin") > -1) {
			_root = _root.substring(0, _root.lastIndexOf("/bin"));
		}
		File file = new File(_root);
		_root = file.getAbsolutePath().replace("\\", "/") + "/";
		_resource = _root + "resource/";
		root = _root;
		resource = _resource;
		System.out.println("init:"+root);
		System.out.println("init:"+resource);
	}

}
