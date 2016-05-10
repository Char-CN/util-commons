package org.blazer.common.file;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FReader {

	BufferedReader br;
	FileReader fr;

	String c = null;

	public FReader(String path) throws FileNotFoundException {
		this.fr = new FileReader(path);
		this.br = new BufferedReader(this.fr);
	}

	public FReader(String path, FHandler handler) throws IOException {
		this.fr = new FileReader(path);
		this.br = new BufferedReader(this.fr);
		each(handler);
	}

	public boolean hasNext() throws IOException {
		return (c = br.readLine()) != null;
	}

	public String next() {
		return c;
	}

	public void each(FHandler handler) throws IOException {
		while (hasNext()) {
			handler.handle(next());
		}
		try {
			this.br.close();
		} catch (Exception e) {
		}
		try {
			this.fr.close();
		} catch (Exception e) {
		}
	}

}
