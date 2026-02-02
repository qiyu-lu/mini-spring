package cn.yuqi.mini.springframework.beans.factory.support;

import cn.yuqi.mini.springframework.beans.BeansException;
import cn.yuqi.mini.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class SimpleInstantiationStrategy implements InstantiationStrategy{

    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException{

        Object bean = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            Object[] args = beanDefinition.getConstructorArgs();
            if (args == null || args.length == 0) {
                System.out.println("基于 Java 反射的无参构造器实例化");
                bean = beanClass.getDeclaredConstructor().newInstance();
            } else {
                System.out.println("基于 Java 反射的有参构造器实例化");
                // 有参构造
                Class<?>[] paramTypes = Arrays.stream(args)
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new);
                Constructor<?> constructor = beanClass.getDeclaredConstructor(paramTypes);
                bean = constructor.newInstance(args);
            }
        }
        catch (Exception e){
            throw new BeansException("无参构造和有参构造都实例化失败");
        }
        return bean;
    }

}
