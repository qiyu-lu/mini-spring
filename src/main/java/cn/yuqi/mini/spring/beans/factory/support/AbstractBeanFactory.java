package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.factory.BeanFactory;
import cn.yuqi.mini.spring.beans.factory.BeansException;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory
        extends DefaultSingletonBeanRegistry
        implements BeanFactory {

    @Override
    public Object getBean(String beanName) throws BeansException {
        if(containsSingleton(beanName)){
            System.out.println("单例池中有");
            return getSingleton(beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return createBean(beanName, beanDefinition);
    }
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    protected abstract  BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
