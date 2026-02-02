package cn.yuqi.mini.springframework.core.io.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlResource implements Resource{

    private final URL url;

    public UrlResource(URL url){
        this.url = url;
    }

    @Override
    public InputStream getInputStream() {
        try{
            return url.openStream();
        } catch (IOException e){
            throw new RuntimeException("Failed to open URL: " + url, e);
        }
    }
}
