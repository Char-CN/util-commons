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

	/**
	 * 重写该方法如果需要使用外部变量，需要定义成final或者new的时候传递参数
	 * 
	 * @param row
	 * @throws IOException
	 */
	public abstract void handle(String row) throws IOException;

	private int count = 0;

	public void add() {
		count++;
	}

	public int index() {
		return count;
	}

}
