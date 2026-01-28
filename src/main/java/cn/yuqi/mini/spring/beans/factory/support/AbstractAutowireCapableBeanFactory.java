package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.factory.BeansException;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;

public abstract class AbstractAutowireCapableBeanFactory
                    extends AbstractBeanFactory{
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException{
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition){
        Class beanClass = beanDefinition.getBeanClass();
        Object bean = null;

        try{
            bean = beanClass.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            throw new BeansException("无法实例化 Bean: " + beanName, e);
        }
        registerSingleton(beanName, bean);
        return bean;
    }

}
