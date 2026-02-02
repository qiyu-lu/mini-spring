package cn.yuqi.mini.springframework.beans.factory.support;

import cn.yuqi.mini.springframework.beans.BeansException;
import cn.yuqi.mini.springframework.beans.factory.config.BeanDefinition;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
