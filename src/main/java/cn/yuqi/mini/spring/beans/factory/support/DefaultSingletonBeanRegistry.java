package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry
        implements SingletonBeanRegistry {
    private Map<String, Object> singleMap = new HashMap<>();

    @Override
    public void registerSingleton(String beanName, Object bean) {
        singleMap.put(beanName, bean);
    }
    @Override
    public Object getSingleton(String beanName) {
        return singleMap.get(beanName);
    }
    @Override
    public boolean containsSingleton(String beanName) {
        return singleMap.containsKey(beanName);
    }
}
