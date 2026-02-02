package cn.yuqi.mini.springframework.beans.factory;

import cn.yuqi.mini.springframework.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
}
