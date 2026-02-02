package cn.yuqi.mini.springframework.core.io.io;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {

    //ResourceLoader 才是“解析 location 的地方”
    @Override
    public Resource getResource(String location) {
        if(location == null){
            throw new NullPointerException("location is null");
        }

        if(location.startsWith("classpath:")){
            String path = location.substring("classpath:".length());
            return new ClassPathResource(path);
        }
        try{
            URL url = new URL(location);
            return new UrlResource(url);
        } catch (MalformedURLException e){

        }
        return new FileSystemResource(location);
    }
}
