package com.ibm.is;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;

import java.io.*;
import java.util.List;

public class CustomFileListener implements FileListener {
    @Override
    public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {

    }

    @Override
    public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception {

    }

    @Override
    public void fileChanged(FileChangeEvent fileChangeEvent) throws IOException {
        //System.out.println(String.format("File %s was changed", fileChangeEvent.getFile().getName()));
        FileObject fileObject =  fileChangeEvent.getFile();
        InputStream inputStream = fileObject.getContent().getInputStream();
        List<String> lines = LogReader.build().readLines(inputStream);
        System.out.println(lines.get(lines.size()-1));
    }
}
