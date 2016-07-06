package edu.georgetown.library.fileAnalyzer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumTool {
    public static String getChecksum(String palgorithm, File f) throws NoSuchAlgorithmException, IOException {
        Algorithm algorithm = Algorithm.valueOf(palgorithm);
        MessageDigest md = algorithm.getInstance();
        try(FileInputStream fis = new FileInputStream(f)){
            byte[] dataBytes = new byte[1204];
            int nread = 0;
            while((nread = fis.read(dataBytes)) != -1){
                md.update(dataBytes, 0, nread);
            }            
        };
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<mdbytes.length; i++){
            sb.append(Integer.toString((mdbytes[i] & 0xFF) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
