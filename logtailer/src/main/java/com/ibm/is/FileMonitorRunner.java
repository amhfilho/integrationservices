package com.ibm.is;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

public class FileMonitorRunner implements Runnable {
    private String filePath;

    public FileMonitorRunner(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject listendir = fsManager.resolveFile(filePath);
            DefaultFileMonitor fm = new DefaultFileMonitor(new CustomFileListener());
            fm.setRecursive(true);
            fm.addFile(listendir);
            fm.start();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }
}
