package org.blazer.common.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FClassReader {

	BufferedReader br = null;
	InputStreamReader isr = null;
	InputStream is = null;

	String c = null;

	private FClassReader() {
	}

	private FClassReader(String path) {
		this.is = FClassReader.class.getResourceAsStream(path);
		this.isr = new InputStreamReader(this.is);
		this.br = new BufferedReader(isr);
	}

	private FClassReader(String path, FHandler handler) throws IOException {
		this.is = FClassReader.class.getResourceAsStream(path);
		this.isr = new InputStreamReader(this.is);
		this.br = new BufferedReader(isr);
		each(handler);
	}

	public static FClassReader create(String path) throws IOException {
		return new FClassReader(path);
	}

	public static FClassReader create(String path, FHandler handler) throws IOException {
		return new FClassReader(path, handler);
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
			this.isr.close();
		} catch (Exception e) {
		}
		try {
			this.is.close();
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
