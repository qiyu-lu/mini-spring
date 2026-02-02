package cn.yuqi.mini.springframework.core.io.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileSystemResource implements Resource {
    private final String path;

    public FileSystemResource(String path) {
        this.path = path;
    }

    @Override
    public InputStream getInputStream() {
        try{
            return new FileInputStream(path);
        } catch(FileNotFoundException e){
            throw new RuntimeException("File not found!"+ path, e);
        }
    }
}
