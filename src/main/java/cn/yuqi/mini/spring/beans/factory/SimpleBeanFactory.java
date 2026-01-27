package cn.yuqi.mini.spring.beans.factory;

import java.util.HashMap;
import java.util.Map;

public class SimpleBeanFactory {
    private  final Map<String, Object> beanMap = new HashMap<String, Object>();

    public SimpleBeanFactory(){}
    public void registerBean(String name, Object bean){
        beanMap.put(name, bean);
    }
    public Object getBean(String name){
        return beanMap.get(name);
    }
}
