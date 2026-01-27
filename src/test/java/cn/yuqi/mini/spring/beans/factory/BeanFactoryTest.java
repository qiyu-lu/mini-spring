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

    @Test
    void testGetBeans() {
        //测试同一个 bean 多次 getBean 返回同一个对象
        //BeanFactory 对同一个 beanName，是否始终返回同一个实例
        SimpleBeanFactory beanFactory = new SimpleBeanFactory();
        beanFactory.registerBean("helloService", new HelloService());
        Object bean1 = beanFactory.getBean("helloService");
        Object bean2 = beanFactory.getBean("helloService");
        //单例测试,同一个 name，多次 getBean是否是同一个对象
        assertSame(bean1, bean2);//比较是否是同一个对象
    }
    static class HelloService {
        public String sayHello() {
            System.out.println("hello");
            return "hello";
        }
    }
}
