package com.saranomy.popconn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class DatabaseHandler {
	// twt, isg are written/read to save login/logout state
	public static void saveFile(Context context, String name, String data) throws IOException {
		FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
		fos.write(data.getBytes());
		fos.close();
		Log.e("DatabaseHandler.saveFile", name + " : " + data);
	}

	public static String loadFile(Context context, String name) throws IOException {
		FileInputStream fis = context.openFileInput(name);
		InputStreamReader inputStreamReader = new InputStreamReader(fis);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		Log.e("DatabaseHandler.loadFile", name + " : " + sb.toString());
		return sb.toString();
	}

	public static String loadFileOrSaveFileIfNotExist(Context context, String name, String data) {
		try {
			return loadFile(context, name);
		} catch (IOException e) {
			try {
				saveFile(context, name, data);
				return loadFileOrSaveFileIfNotExist(context, name, data);
			} catch (IOException e2) {
				return null;
			}
		}
	}
}
