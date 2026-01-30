package cn.yuqi.mini.spring.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue){
            this.propertyValueList.add(propertyValue);
    }

    public List<PropertyValue> getPropertyValueList(){
        return this.propertyValueList;
    }
}
