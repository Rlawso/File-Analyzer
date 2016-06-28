package edu.georgetown.library.fileAnalyzer.util;

import java.io.File;
import java.io.IOException;

import edu.georgetown.library.fileAnalyzer.util.TarUtil;

public class TarBagHelper extends FABagHelper {
    File dest = null;
    public TarBagHelper(File source) {
        super(source);
    }
    public TarBagHelper(File source, File dest) {
    	super(source);
    	this.dest = dest;
    }
    
    @Override public void writeBagFile() throws IOException, IncompleteSettingsException {
    	super.writeBagFile();
        if (dest == null) {
            data.newBag = TarUtil.tarFolderAndDeleteFolder(data.newBag);            
        } else {
            File destfile = new File(dest, data.newBag.getName() + ".tar");
            data.newBag = TarUtil.tarFolderAndDeleteFolder(data.newBag, destfile);                        
        }
    }
    
}
