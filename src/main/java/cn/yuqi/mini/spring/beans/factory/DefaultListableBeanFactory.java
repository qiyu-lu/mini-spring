package cn.yuqi.mini.spring.beans.factory;


import java.util.HashMap;
import java.util.Map;

//为什么非要拆成两个接口,为了职责分离,
//BeanDefinitionRegistry 只关心一件事:能不能存 / 取 BeanDefinition
//SingletonBeanRegistry 也只关心一件事:我能不能存 / 取 已经创建好的对象

//DefaultListableBeanFactory 的作用:把两种能力组合在一起，用来对外提供 getBean
public class DefaultListableBeanFactory
        implements
        BeanDefinitionRegistry,
        SingletonBeanRegistry,
        BeanFactory{

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();
    //单例池
    private Map<String, Object> singleMap = new HashMap<>();



    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition){
        beanDefinitionMap.put(beanName, beanDefinition);
    }
    public BeanDefinition getBeanDefinition(String beanName){
        return beanDefinitionMap.get(beanName);
    }
    public boolean containsBeanDefinition(String beanName){
        return beanDefinitionMap.containsKey(beanName);
    }
    public void registerSingleton(String beanName, Object bean){
        singleMap.put(beanName, bean);
    }
    public Object getSingleton(String beanName){
        return singleMap.get(beanName);
    }
    public boolean containsSingleton(String beanName){
        return singleMap.containsKey(beanName);
    }

    public Object getBean(String beanName) {
        // 1. 先从单例池取
        if (containsSingleton(beanName)) {
            System.out.println("单例池中有："+beanName);
            return getSingleton(beanName);
        }

        // 2. 再从 BeanDefinition 创建
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition != null) {
            try {
                Object bean = beanDefinition.getBeanClass()
                        .getDeclaredConstructor()
                        .newInstance();
                System.out.println("将bean存入单例池中");
                registerSingleton(beanName, bean);
                return bean;
            } catch (Exception e) {
                throw new RuntimeException("无法实例化 Bean: " + beanName, e);
            }
        }

        // 3. 都不存在 → 明确失败
        throw new RuntimeException("No such bean: " + beanName);
    }
}
