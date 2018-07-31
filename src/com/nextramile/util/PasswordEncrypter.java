package com.nextramile.util;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Sha256Hash;

public class PasswordEncrypter {

	public static String encryptPassword(String password)
	{
		DefaultHashService hashService = new DefaultHashService();
		hashService.setHashIterations(512);
		hashService.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
		//hashService.setPrivateSalt(new SimpleByteSource(PRIVATE_SALT)); // Same salt as in shiro.ini, but NOT base64-encoded.
		hashService.setGeneratePublicSalt(true);
		DefaultPasswordService passwordService = new DefaultPasswordService();
		passwordService.setHashService(hashService);
		String encryptedPassword = passwordService.encryptPassword(password);
		return encryptedPassword;
	}
}
