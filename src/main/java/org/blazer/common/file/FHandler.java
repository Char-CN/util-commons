package org.blazer.common.file;

import java.io.IOException;

public abstract class FHandler {

	private Object[] objs;

	public FHandler() {
	}

	public FHandler(Object... objs) {
		this.objs = objs;
	}

	public Object getParameter(int index) {
		return objs[index];
	}

	public abstract void handle(String row) throws IOException;

	private int count = 0;

	public void add() {
		count++;
	}

	public int index() {
		return count;
	}

}
