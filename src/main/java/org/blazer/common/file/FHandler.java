package org.blazer.common.file;

import java.io.IOException;

public interface FHandler {

	void handle(String row) throws IOException;

}
