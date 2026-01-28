package cn.yuqi.mini.spring.beans.factory;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
}
