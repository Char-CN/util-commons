package org.blazer.common.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.blazer.common.file.FClassReader;
import org.blazer.common.file.FHandler;
import org.blazer.common.file.FReader;

public class Conf {

	private HashMap<String, String> contentMap = null;

	private List<String> keyList = null;

	private List<String> invalidList = null;

	private Conf(String filePath, String... args) {
		load(filePath, args);
	}

	public static Conf createConf(String filePath, String... args) {
		return new Conf(filePath, args);
	}

	public Set<String> keySet() {
		return contentMap.keySet();
	}

	public List<String> keyList() {
		return keyList;
	}

	public List<String> invalidList() {
		return invalidList;
	}

	public boolean containsKey(String key) {
		return contentMap.containsKey(key);
	}

	public String get(String key) {
		return contentMap.get(key);
	}

	public Conf appendConf(String filePath, String... args) {
		load(filePath, args);
		return this;
	}

	private void load(String filePath, final String... args) {
		if (contentMap == null) {
			contentMap = new HashMap<String, String>();
		}
		if (keyList == null) {
			keyList = new ArrayList<String>();
		}
		if (invalidList == null) {
			invalidList = new ArrayList<String>();
		}
		try {
			FHandler fHandler = new FHandler() {
				@Override
				public void handle(String row) throws IOException {
					// 过滤空行
					if (row.equals("")) {
						return;
					}
					// 过滤注释
					if (row.matches("\\s*[#].*")) {
						return;
					}
					int eqIndex = row.indexOf("=");
					// 过滤无效行
					if (eqIndex == -1) {
						invalidList.add(row);
						return;
					}
					String key = row.substring(0, eqIndex).trim();
					String value = row.substring(eqIndex + 1).trim();
					// 替换系统参数
					for (int i = 0; i < args.length; i++) {
						value = value.replace("{" + i + "}", args[i]);
					}
					// 替换当前变量
					for (int i = 0; i < keyList.size(); i++) {
						String k = keyList.get(0);
						value = value.replace("{" + k + "}", contentMap.get(k));
					}
					// 存入list
					if (!contentMap.containsKey(key)) {
						keyList.add(key);
					}
					contentMap.put(key, value);
				}
			};
			try {
				FClassReader fcr = new FClassReader(filePath);
				fcr.each(fHandler);
			} catch (Exception e) {
				if (e instanceof FileNotFoundException) {
					FReader fr = new FReader(filePath);
					fr.each(fHandler);
				} else {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> cloneContentMap() {
		HashMap<String, String> newHashMap = new HashMap<String, String>();
		for (String key : contentMap.keySet()) {
			newHashMap.put(key, contentMap.get(key));
		}
		return newHashMap;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : keyList) {
			sb.append(key).append("=").append(contentMap.get(key)).append("\n");
		}
		return sb.toString();
	}

}
