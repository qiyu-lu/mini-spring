package cn.yuqi.mini.spring.beans.factory.support;


import cn.yuqi.mini.spring.beans.factory.BeanFactory;
import cn.yuqi.mini.spring.beans.factory.BeansException;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;
import cn.yuqi.mini.spring.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory
        extends AbstractAutowireCapableBeanFactory
        implements BeanDefinitionRegistry{

    private Map<String, BeanDefinition>  beanDefinitionMap = new HashMap<>();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition){
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null){
            throw new BeansException("Bean with name " + beanName + " not found");
        }
        return beanDefinition;
    }


    public boolean containsBeanDefinition(String beanName){
        return beanDefinitionMap.containsKey(beanName);
    }
}

