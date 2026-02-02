package cn.yuqi.mini.springframework.core.io.io;

import java.io.InputStream;

public class ClassPathResource implements Resource{

    private final String path;
    private final ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, Thread.currentThread().getContextClassLoader());
    }
    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader;
    }

    @Override
    public InputStream getInputStream() {
        InputStream is = classLoader.getResourceAsStream(path);
        if(is == null){
            throw new RuntimeException("Resource not found: " + path);
        }
        return is;
    }
}
