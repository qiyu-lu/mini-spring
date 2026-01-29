package cn.yuqi.mini.spring.beans.factory.config;

public class BeanDefinition {
    private Class<?> beanClass;
    private Object[] constructorArgs;

    public BeanDefinition(Class<?> beanClass, Object... constructorArgs) {
        this.beanClass = beanClass;
        this.constructorArgs = constructorArgs;
    }
    public Class<?> getBeanClass(){
        return this.beanClass;
    }
    public Object[] getConstructorArgs(){
        return this.constructorArgs;
    }
}