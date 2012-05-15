package com.njnu.kai.feiphoneinfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {

	public String execute(String[] cmmand, String directory) throws IOException {
		String result = "";
		ProcessBuilder builder = new ProcessBuilder(cmmand);
		if (directory != null) {
			builder.directory(new File(directory));
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream is = process.getInputStream();
			byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				result = result + new String(buffer);
			}
			is.close();
		}
		return result;
	}

	public String getProcesRunningInfo() {
		String[] args = { "/system/bin/top", "-n", "1" };
		String result = "";
		try {
			result = execute(args, "/system/bin/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String killProcess(String pid) {
		String[] args = { "kill", pid, "9" };
		String result = "";
		try {
			result = execute(args, "/system/bin/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
