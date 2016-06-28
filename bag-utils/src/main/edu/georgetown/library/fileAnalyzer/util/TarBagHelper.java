package edu.georgetown.library.fileAnalyzer.util;

import java.io.File;
import java.io.IOException;

import edu.georgetown.library.fileAnalyzer.util.TarUtil;

public class TarBagHelper extends FABagHelper {
    File finalTarFile;
    public TarBagHelper(File source) {
    	super(source);
    	finalTarFile = TarUtil.getTarFile(source);
    }
    
    @Override public void writeBagFile() throws IOException, IncompleteSettingsException {
    	super.writeBagFile();

    	data.newBag = TarUtil.tarFolderAndDeleteFolder(data.newBag, finalTarFile);
    }
    
    public void createBagFile() throws IncompleteSettingsException {
        validate();
        
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        data.newBag = new File(tmpdir, data.source.getName() + "_bag");
    }
}
