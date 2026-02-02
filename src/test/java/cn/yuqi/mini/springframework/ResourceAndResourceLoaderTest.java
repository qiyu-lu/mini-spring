package cn.yuqi.mini.springframework;

import cn.hutool.core.io.IoUtil;
import cn.yuqi.mini.springframework.core.io.io.DefaultResourceLoader;
import cn.yuqi.mini.springframework.core.io.io.FileSystemResource;
import cn.yuqi.mini.springframework.core.io.io.Resource;
import cn.yuqi.mini.springframework.core.io.io.UrlResource;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResourceAndResourceLoaderTest {
    @Test
    public void testResourceLoader() throws Exception {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

        //加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        assertThat(content).isEqualTo("hello world 这是test下resources的文件hello.txt");

        //加载文件系统资源
        resource = resourceLoader.getResource("src/test/resources/hello.txt");
        assertThat(resource instanceof FileSystemResource).isTrue();
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        assertThat(content).isEqualTo("hello world 这是test下resources的文件hello.txt");

        //加载url资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        assertThat(resource instanceof UrlResource).isTrue();
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
}
