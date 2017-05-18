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

	public FClassReader(String path) {
		this.is = FClassReader.class.getResourceAsStream(path);
		this.isr = new InputStreamReader(this.is);
		this.br = new BufferedReader(isr);
	}

	public FClassReader(String path, FHandler handler) throws IOException {
		this.is = FClassReader.class.getResourceAsStream(path);
		this.isr = new InputStreamReader(this.is);
		this.br = new BufferedReader(isr);
		each(handler);
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
