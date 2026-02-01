package cn.yuqi.mini.spring.beans.factory;

import cn.yuqi.mini.spring.beans.PropertyValue;
import cn.yuqi.mini.spring.beans.PropertyValues;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;
import cn.yuqi.mini.spring.beans.factory.config.BeanReference;
import cn.yuqi.mini.spring.beans.factory.support.CglibSubclassingInstantiationStrategy;
import cn.yuqi.mini.spring.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BeanFactoryTest {

    @Test
    void testGetBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();


        BeanDefinition beanDefinition1 = new BeanDefinition(HelloService.class);

        beanFactory.registerBeanDefinition("无参反射的helloService",beanDefinition1);

        HelloService helloService1 = (HelloService) beanFactory.getBean("无参反射的helloService");
        helloService1.sayHello();

        BeanDefinition beanDefinition2 = new BeanDefinition(HelloService.class);
        beanDefinition2.setConstructorArgs("有参反射的构造传入的参数");
        beanFactory.registerBeanDefinition("有参反射的helloService2",beanDefinition2);

        HelloService helloService2 = (HelloService) beanFactory.getBean("有参反射的helloService2");
        helloService2.sayHello();

        // 尝试测试 cglib：
        beanFactory.setInstantiationStrategy(new CglibSubclassingInstantiationStrategy());
        BeanDefinition beanDefinition3 = new BeanDefinition(HelloService.class);

        beanFactory.registerBeanDefinition("无参CGLIB的helloService",beanDefinition3);

        HelloService helloService3 = (HelloService) beanFactory.getBean("无参CGLIB的helloService");
        helloService3.sayHello();

        BeanDefinition beanDefinition4 = new BeanDefinition(HelloService.class);
        beanDefinition4.setConstructorArgs("有参CGLIB的构造传入的参数");
        beanFactory.registerBeanDefinition("有参CGLIB的helloService2",beanDefinition4);

        HelloService helloService4 = (HelloService) beanFactory.getBean("有参CGLIB的helloService2");
        helloService4.sayHello();
    }
    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues  propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "小明"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class);
        beanDefinition.setPropertyValues(propertyValues);

        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("小明");
        assertThat(person.getAge()).isEqualTo(18);

    }
    //此时如果使用下面这个类内定义的类进行测试会报错,需要一个顶级类
//    static class HelloService {
//        public String sayHello() {
//            System.out.println("hello");
//            return "hello";
//        }
//    }

    @Test
    public void testPopulateBeanWithBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //注册Car实例
        PropertyValues propertyValuesForCar = new PropertyValues();
        propertyValuesForCar.addPropertyValue(new PropertyValue("brand", "porsche"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class);
        carBeanDefinition.setPropertyValues(propertyValuesForCar);
        beanFactory.registerBeanDefinition("car", carBeanDefinition);

        //注册Person实例
        PropertyValues propertyValuesForPerson = new PropertyValues();
        propertyValuesForPerson.addPropertyValue(new PropertyValue("name", "derek"));
        propertyValuesForPerson.addPropertyValue(new PropertyValue("age", 18));
        //Person实例依赖Car实例
        propertyValuesForPerson.addPropertyValue(new PropertyValue("car", new BeanReference("car")));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class);
        beanDefinition.setPropertyValues(propertyValuesForPerson);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("derek");
        assertThat(person.getAge()).isEqualTo(18);
        Car car = person.getCar();
        assertThat(car).isNotNull();
        assertThat(car.getBrand()).isEqualTo("porsche");
    }
}
