package edu.georgetown.library.fileAnalyzer.util;

import java.io.File;
import java.io.IOException;

import edu.georgetown.library.fileAnalyzer.util.TarUtil;

public class TarBagHelper extends FABagHelper {
        private boolean createTar;

        public TarBagHelper(File source) {
                this(source, true);
        }
        public TarBagHelper(File source, boolean createTar) {
                super(source);
                this.createTar = createTar;
        }

        @Override
        public void writeBagFile() throws IOException, IncompleteSettingsException {
                super.writeBagFile();

                if (createTar) {
                        data.newBag = TarUtil.tarFolderAndDeleteFolder(data.newBag);
                }
        }

}
