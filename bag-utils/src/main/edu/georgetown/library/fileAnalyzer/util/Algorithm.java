package edu.georgetown.library.fileAnalyzer.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum Algorithm {
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");
    String algorithm;
    Algorithm(String s) {algorithm = s;}
    public MessageDigest getInstance() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm);
    }
}
