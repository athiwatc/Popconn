package com.saranomy.popconn.debug;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;

public class KeyHash {
	public static void generateKeyHash(Activity activity) {
		// get developer's keyHash for adding into facebook app
		try {
			PackageInfo info = activity.getPackageManager().getPackageInfo("com.saranomy.popconn", PackageManager.GET_SIGNATURES);
			for (android.content.pm.Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	public static void clearApplicationData(Context context) {
		File cache = context.getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				File f = new File(appDir, s);
				if (deleteDir(f))
					Log.e("clearApplicationData", String.format("**************** DELETED -> (%s) *******************", f.getAbsolutePath()));
			}
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
