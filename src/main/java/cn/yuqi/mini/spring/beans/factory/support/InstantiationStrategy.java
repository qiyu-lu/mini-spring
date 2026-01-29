package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.factory.BeansException;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
