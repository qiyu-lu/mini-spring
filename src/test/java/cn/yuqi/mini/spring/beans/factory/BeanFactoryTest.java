package cn.yuqi.mini.spring.beans.factory;

import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;
import cn.yuqi.mini.spring.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

class BeanFactoryTest {

    @Test
    void testGetBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);

        beanFactory.registerBeanDefinition("helloService",beanDefinition);

        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        helloService.sayHello();
    }

    //此时如果使用下面这个类内定义的类进行测试会报错,需要一个顶级类
//    static class HelloService {
//        public String sayHello() {
//            System.out.println("hello");
//            return "hello";
//        }
//    }
}
