package cn.yuqi.mini.spring.beans.factory;

import java.util.HashMap;
import java.util.Map;

//负责 管理 BeanDefinition,规定：容器“必须”能注册和获取 BeanDefinition，那么可以使用接口
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
}
