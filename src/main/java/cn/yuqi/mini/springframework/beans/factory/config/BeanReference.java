package cn.yuqi.mini.springframework.beans.factory.config;


//BeanReference 的存在意义只有一个：用一个对象，表示「我依赖容器里另一个 Bean」这件事
public class BeanReference {
    private final String beanName;

    public BeanReference(String beanName){
        this.beanName = beanName;
    }

    public String getBeanName(){
        return this.beanName;
    }
}
