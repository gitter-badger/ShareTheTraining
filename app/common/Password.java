package common;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

import play.Logger;

public class Password {
    // The higher the number of iterations the more 
    // expensive computing the hash is for us
    // and also for a brute force attack.
    private static final int iterations = 10*1024;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported. */
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        return Base64.encodeBase64URLSafeString(salt) + "$" + hash(password, salt);
    }

    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    public static boolean check(String password, String stored){
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2)
            return false;
        String hashOfInput = null;
		try {
			hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
        return hashOfInput.equals(saltAndPass[1]);
    }
    
    
    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64URLSafeString(key.getEncoded());
    }

}