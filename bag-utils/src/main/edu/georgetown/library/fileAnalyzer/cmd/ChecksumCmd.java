package edu.georgetown.library.fileAnalyzer.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.georgetown.library.fileAnalyzer.util.Algorithm;
import edu.georgetown.library.fileAnalyzer.util.ChecksumTool;
import edu.georgetown.library.fileAnalyzer.util.CmdUtil;

public class ChecksumCmd {
    public ChecksumCmd(){}
    
    public static enum CompareStatus{NO_MATCH,NAME_MATCH,CHECKSUM_MATCH,NAME_AND_CHECKSUM_MATCH;}
    public static final String CMD = "ChecksumCmd";
    
    public static final String testChecksum(CommandLine cmdLine) throws NoSuchAlgorithmException, IOException {
        String algorithm = Algorithm.MD5.name();
        for(Algorithm a: Algorithm.values()) {
            if (cmdLine.hasOption(a.name())) {
                algorithm = a.name();
                break;
            }
        }
        File f = new File(cmdLine.getOptionValue("f"));
        if (!f.exists()) {
            throw new FileNotFoundException(String.format("Media File [%s] does not exist", f.getAbsolutePath()));
        }
        String checksum = ChecksumTool.getChecksum(algorithm, f);
        if (cmdLine.hasOption("cf")) {
            File cf = new File(cmdLine.getOptionValue("cf"));
            if (!cf.exists()) {
                throw new FileNotFoundException(String.format("Checksum File [%s] does not exist", cf.getAbsolutePath()));
            }
            CompareStatus compareStatus = CompareStatus.NO_MATCH;
            try(BufferedReader br = new BufferedReader(new FileReader(cf))){
                for(String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] parts = line.split(",");
                    if (parts.length < 2) continue;
                    String csum = parts[0];
                    String name = parts[1];
                    
                    if (f.toPath().endsWith(name) && csum.equals(checksum)) {
                        compareStatus = CompareStatus.NAME_AND_CHECKSUM_MATCH;
                        break;
                    }
                    if (csum.equals(checksum)) {
                        compareStatus = CompareStatus.CHECKSUM_MATCH;
                    }
                    if (f.toPath().endsWith(name) && compareStatus != CompareStatus.CHECKSUM_MATCH) {
                        compareStatus = CompareStatus.NAME_MATCH;
                    }
                }
            }
            if (compareStatus != CompareStatus.NAME_AND_CHECKSUM_MATCH) {
                CmdUtil.fail(String.format("%s %s", checksum, compareStatus.name()));
            }
            System.out.println(String.format("%s %s", checksum, compareStatus.name()));
        } else {
            System.out.println(checksum);
        }
        return checksum;
    }
    
    
    public static final void main(String[] args) {
        CommandLine cmdLine = parseChecksumCommandLine(CMD, args);
        try {
            testChecksum(cmdLine);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            CmdUtil.fail(e.getMessage());
        }
    }

    public static void usage() {
        StringBuilder sb = new StringBuilder();
        sb.append(CMD);
        sb.append(" (");
        boolean first = true;
        for(Algorithm a: Algorithm.values()) {
            if (first) {
                first = false;
            } else {
                sb.append("|");
            }
            sb.append("-");
            sb.append(a.name());
        }
        sb.append(") ");
        sb.append("[-cf <checksumFile>] ");
        sb.append("-f <fileToChecksum>");
        System.out.println(sb.toString());
    }
    
    public static CommandLine parseChecksumCommandLine(String main, String[] args) {
        DefaultParser clParse = new DefaultParser();
        Options opts = new Options();
        OptionGroup optGrp = new OptionGroup();
        for(Algorithm a: Algorithm.values()) {
            String def = a.name().equals("MD5") ? " (default)" : "";
            optGrp.addOption(new Option(a.name(), "Algorithm: "+a.name() + def));
        }
        opts.addOptionGroup(optGrp);
        opts.addOption("cf", true, "Comma Separated Checksum File");
        opts.addOption("f", true, "Media File to Analyze");
        opts.addOption("h", false, "Help Info");
       
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmdLine = clParse.parse(opts, args);
            if (cmdLine.hasOption("h")) {
                usage();
                formatter.printHelp(CMD, opts);
                System.exit(0);
            }
            return cmdLine;
        } catch (ParseException e) {
            usage();
            formatter.printHelp(CMD, opts);
            CmdUtil.fail("Invalid Options");
        }
        return null;
    }
}
