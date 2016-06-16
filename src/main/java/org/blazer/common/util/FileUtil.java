package org.blazer.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtil {


	private final static int BUFFER_SIZE = 1024;

	/**
	 * 功 能: 拷贝文件(只能拷贝文件) 参 数: strSourceFileName:指定的文件全路径名 strDestDir: 移动到指定的文件夹
	 * 返回值: 如果成功true;否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean copyTo(String strSourceFileName, String strDestDir) {
		File fileSource = new File(strSourceFileName);
		File fileDest = new File(strDestDir);
		// 如果源文件不存或源文件是文件夹
		if (!fileSource.exists() || !fileSource.isFile()) {
			System.out.println("源文件[" + strSourceFileName + "],不存在或是文件夹!");
			return false;
		}
		// 如果目标文件夹不存在
		if (!fileDest.isDirectory() || !fileDest.exists()) {
			if (!fileDest.mkdirs()) {
				System.out.println("目录文件夹不存，在创建目标文件夹时失败!");
				return false;
			}
		}
		try {
			if (!strDestDir.endsWith(File.separator)) {
				strDestDir += File.separator;
			}
			String strAbsFilename = strDestDir + fileSource.getName();
			FileInputStream fileInput = new FileInputStream(strSourceFileName);
			FileOutputStream fileOutput = new FileOutputStream(strAbsFilename);
			System.out.println("开始拷贝文件");
			int count = -1;
			long nWriteSize = 0;
			long nFileSize = fileSource.length();
			byte[] data = new byte[BUFFER_SIZE];
			while (-1 != (count = fileInput.read(data, 0, BUFFER_SIZE))) {
				fileOutput.write(data, 0, count);
				nWriteSize += count;
				long size = (nWriteSize * 100) / nFileSize;
				long t = nWriteSize;
				String msg = null;
				if (size <= 100 && size >= 0) {
					msg = "\r拷贝文件进度:   " + size + "%   \t" + "\t   已拷贝:   " + t;
					System.out.println(msg);
				} else if (size > 100) {
					msg = "\r拷贝文件进度:   " + 100 + "%   \t" + "\t   已拷贝:   " + t;
					System.out.println(msg);
				}
			}
			fileInput.close();
			fileOutput.close();
			System.out.println("拷贝文件成功!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 功 能: 删除指定的文件 参 数: 指定绝对路径的文件名 strFileName 返回值: 如果删除成功true否则false;
	 * 
	 * @param strFileName
	 * @return
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);
		if (!fileDelete.exists() || !fileDelete.isFile()) {
			System.out.println(strFileName + "不存在!");
			return false;
		}
		return fileDelete.delete();
	}

	/**
	 * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName: 是指定的文件全路径名 strDestDir:
	 * 移动到指定的文件夹中 返回值: 如果成功true; 否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean moveFile(String strSourceFileName, String strDestDir) {
		if (copyTo(strSourceFileName, strDestDir)) {
			return delete(strSourceFileName);
		} else {
			return false;
		}
	}

	/**
	 * 功 能: 创建文件夹 参 数: strDir 要创建的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean makeDir(String strDir) {
		File fileNew = new File(strDir);
		if (!fileNew.exists()) {
			return fileNew.mkdirs();
		} else {
			return true;
		}
	}

	/**
	 * 功 能: 删除文件夹 参 数: strDir 要删除的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean removeDir(String strDir) {
		File rmDir = new File(strDir);
		if (rmDir.isDirectory() && rmDir.exists()) {
			String[] fileList = rmDir.list();
			for (int i = 0; i < fileList.length; i++) {
				if (!strDir.endsWith(File.separator)) {
					strDir += File.separator;
				}
				String subFile = strDir + fileList[i];
				File tmp = new File(subFile);
				if (tmp.isFile()) {
					tmp.delete();
				} else if (tmp.isDirectory()) {
					removeDir(subFile);
				}
			}
			rmDir.delete();
		} else {
			return false;
		}
		return true;
	}

	@Deprecated
	public static List<String> findFileNameRegexp2(String filePath, String regexp) {
		List<String> nameList = new ArrayList<String>();
		File f = new File(filePath);
		if (f.isDirectory()) {
			for (File sf : f.listFiles()) {
				if (!sf.isFile())
					continue;
				if (sf.getName().matches(regexp))
					nameList.add(sf.getName());
			}
		}
		return nameList;
	}

	public static String[] findFileNameRegexp(String filePath, String regexp) throws FileNotFoundException {
		File directory = new File(filePath);
		if (!directory.isDirectory()) {
			throw new FileNotFoundException(filePath + " is not directory");
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

	public static void main(String[] args) throws FileNotFoundException {
		// System.out.println(StringUtil.findStrByReg("24Money_B.csv",
		// "24Money_(\\d*).csv").get(0));
		System.out.println("ab12d".matches("\\w*\\d*\\w+"));
		for (String name : findFileNameRegexp("D:/data/", "sum_month_daydata_\\w*.csv")) {
			System.out.println(name);
		}
	}

}
