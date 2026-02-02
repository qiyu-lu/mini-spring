package cn.yuqi.mini.springframework.beans.factory.config;

//负责 管理已经创建完成的单例 Bean 实例
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object bean);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
}
