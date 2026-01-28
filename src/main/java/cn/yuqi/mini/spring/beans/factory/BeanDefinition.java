package cn.yuqi.mini.spring.beans.factory;

public class BeanDefinition {
    private Class<?> beanClass;
    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    public Class<?> getBeanClass(){
        return this.beanClass;
    }
}
