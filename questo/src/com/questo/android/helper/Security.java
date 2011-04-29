package com.questo.android.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.questo.android.model.User;

public class Security {
	
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	public static boolean validatePasswordCorrect(String plaintext, User user) {
		String saltedPw = plaintext + user.getPasswordSalt();
		return md5(saltedPw).equals(user.getPasswordHash());
	}

}
