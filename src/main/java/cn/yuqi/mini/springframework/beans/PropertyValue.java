package cn.yuqi.mini.springframework.beans;

public class PropertyValue {
    String propertyName;
    Object propertyValue;
    public PropertyValue(){
    }

    //任务：
    //1. 要给哪个属性赋值？
    //2. 赋的值是什么（或引用谁）？

    //我一开始的思路是//如果是BeanReference 类的话，去单例池中找这个对应的名字的这个单例，如果没有的话新建
    //但是这样就会有问题，如果要实现这个的话，就需要反向依赖 BeanFactory
    public PropertyValue(String propertyName, Object propertyValue){
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    public Object getPropertyValue() {
        return propertyValue;
    }
    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }
}
