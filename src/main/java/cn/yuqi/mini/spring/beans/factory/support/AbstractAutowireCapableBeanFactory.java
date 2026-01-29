package cn.yuqi.mini.spring.beans.factory.support;

import cn.yuqi.mini.spring.beans.factory.BeansException;
import cn.yuqi.mini.spring.beans.factory.config.BeanDefinition;

public abstract class AbstractAutowireCapableBeanFactory
                    extends AbstractBeanFactory{
    //构造bean
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException{
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition){
        Object bean = instantiationStrategy.instantiate(beanDefinition);
        registerSingleton(beanName, bean);
        return bean;
    }

    //平时是利用反射进行实例化，特殊情况如需要aop时进行切换
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
