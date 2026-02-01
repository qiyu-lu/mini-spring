package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.BeansException;
import cn.yuqi.mini.spring.beans.PropertyValue;
import cn.yuqi.mini.spring.beans.PropertyValues;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;
import cn.yuqi.mini.spring.beans.factory.config.BeanReference;

import java.lang.reflect.Field;

public abstract class AbstractAutowireCapableBeanFactory
                    extends AbstractBeanFactory{
    //构造bean
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException{
        Object bean = doCreateBean(beanName, beanDefinition);
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition){
        Object bean = instantiationStrategy.instantiate(beanDefinition);
        registerSingleton(beanName, bean);
        return bean;
    }

    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws BeansException{
        PropertyValues  pvs = beanDefinition.getPropertyValues();
        if(pvs == null) return; //无参构造或者是采用默认值，不进行填充

        for(PropertyValue pv : pvs.getPropertyValueList()){
            String name = pv.getPropertyName();
            Object value = pv.getPropertyValue();

            try{
                if(value instanceof BeanReference){
                    value = getBean(((BeanReference) value).getBeanName());
                }
                Field field = bean.getClass().getDeclaredField(name);
                field.setAccessible(true);
                field.set(bean, value);
            } catch (Exception e){
                throw new BeansException("failed to set property " + name, e);
            }
        }
    }
    //平时是利用反射进行实例化，特殊情况如需要aop时进行切换
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
