package org.blazer.common.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FReader {

	BufferedReader br = null;
	FileInputStream fis = null;
	InputStreamReader isr = null;

	String c = null;

	private FReader() {
	}

	private FReader(String path) throws FileNotFoundException, UnsupportedEncodingException {
		this.fis = new FileInputStream(path);
		this.isr = new InputStreamReader(this.fis, "UTF-8");
		this.br = new BufferedReader(this.isr);
	}

	private FReader(String path, FHandler handler) throws IOException {
		this.fis = new FileInputStream(path);
		this.isr = new InputStreamReader(this.fis, "UTF-8");
		this.br = new BufferedReader(this.isr);
		each(handler);
	}

	public static FReader create(String path) throws IOException {
		return new FReader(path);
	}

	public static FReader create(String path, FHandler handler) throws IOException {
		return new FReader(path, handler);
	}

	public boolean hasNext() throws IOException {
		return (c = br.readLine()) != null;
	}

	public String next() {
		return c;
	}

	public void close() {
		try {
			this.br.close();
		} catch (Exception e) {
		}
		try {
			this.fis.close();
		} catch (Exception e) {
		}
		try {
			this.isr.close();
		} catch (Exception e) {
		}
	}

	public void each(FHandler handler) throws IOException {
		while (hasNext()) {
			handler.handle(next());
			handler.add();
		}
		close();
	}

}
