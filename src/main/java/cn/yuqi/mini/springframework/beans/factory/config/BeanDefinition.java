package cn.yuqi.mini.springframework.beans.factory.config;

import cn.yuqi.mini.springframework.beans.PropertyValues;

public class BeanDefinition {
    private Class<?> beanClass;
    private Object[] constructorArgs;
    private PropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    public void setConstructorArgs(Object... constructorArgs){
        this.constructorArgs = constructorArgs;
    }
    public void setPropertyValues(PropertyValues propertyValues){
        this.propertyValues = propertyValues;
    }


    public Class<?> getBeanClass(){
        return this.beanClass;
    }
    public Object[] getConstructorArgs(){
        return this.constructorArgs;
    }
    public PropertyValues getPropertyValues(){return this.propertyValues;}
}