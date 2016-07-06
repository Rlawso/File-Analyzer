package edu.georgetown.library.fileAnalyzer.util;

public class CmdUtil {
    public static final void fail(String message) {
        System.err.println(message);
        System.exit(FAIL);
    }
    public static final int FAIL = 100;

}
