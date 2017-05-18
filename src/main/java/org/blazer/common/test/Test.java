package org.blazer.common.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

import org.blazer.common.file.FHandler;
import org.blazer.common.file.FReader;

/**
 * @author Blazer He
 * @__date 2016年4月27日
 */
public class Test {

	public static void main(String[] args) throws IOException {
		String path = "/Users/hyy/Downloads/zoc_download/test.csv";
		FileOutputStream fos = new FileOutputStream("/Users/hyy/Downloads/zoc_download/test2.csv");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		FReader fr = new FReader(path, new FHandler() {
			@Override
			public void handle(String row) throws IOException {
				String[] columns = row.split("\t");
				// System.out.println(columns[2] + "\t" + columns[4] + "\t" +
				// columns[7] + "\t" + rand(18));
				String out = "";
				for (int i = 0; i < columns.length; i++) {
					if (i != 0) {
						out += "\t";
					}
					if (i == 4) {
						out += rand(18);
					} else if (i == 1) {
						out += randHanzi(6);
					} else if (i == 2) {
						out += randHanzi(3);
					} else if (i == 7) {
						out += 1 + rand(10);
					} else if (i == columns.length - 1) {
					} else {
						out += columns[i];
					}
				}
				System.out.println(out);
				osw.write(out + "\n");
			}
		});
		osw.flush();
	}

	public static String rand(int size) {
		String s = "";
		Random random = new Random();
		s += random.nextInt(9) + 1;
		for (int i = 0; i < size - 1; i++) {
			s += random.nextInt(10);
		}
		return s;
	}

	public static String randHanzi(int size) {
		Random ran = new Random();
		String s = "";
		for (int i = 0; i < size; i++) {
			s += (char)(0x4e00 + ran.nextInt(0x9fa5 - 0x4e00 + 1));
		}
		return s;
	}

}
