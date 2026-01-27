package cn.yuqi.mini.spring.beans.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {

    @Test
    void testGetBean() {
        SimpleBeanFactory beanFactory = new SimpleBeanFactory();
        beanFactory.registerBean("helloService", new HelloService());
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");

        assertNotNull(helloService);
        Object bean = beanFactory.getBean("helloService");
        assertNotNull(bean);

        assertEquals(helloService.sayHello(), "hello");
    }

    static class HelloService {
        public String sayHello() {
            System.out.println("hello");
            return "hello";
        }
    }
}
