package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.BeansException;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
