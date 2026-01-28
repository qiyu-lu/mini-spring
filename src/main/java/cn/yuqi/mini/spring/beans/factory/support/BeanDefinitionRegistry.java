package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;

//负责 管理 BeanDefinition,规定：容器“必须”能注册和获取 BeanDefinition，那么可以使用接口
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

}
