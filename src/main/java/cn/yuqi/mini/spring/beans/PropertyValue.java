package cn.yuqi.mini.spring.beans;

public class PropertyValue {
    String propertyName;
    Object propertyValue;
    public PropertyValue(){
    }
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
