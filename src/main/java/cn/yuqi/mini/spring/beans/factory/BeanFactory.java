package cn.yuqi.mini.spring.beans.factory;

import cn.yuqi.mini.spring.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
}
