package org.inurl.spring.ext.bind;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.util.Map;

public class ModelRequestParamBinder extends ExtendedServletRequestDataBinder {

    private final Map<String, String> mappingTable;

    public ModelRequestParamBinder(Object target, String objectName, Map<String, String> mappingTable) {
        super(target, objectName);
        this.mappingTable = mappingTable;
    }

    @Override
    protected void addBindValues(MutablePropertyValues propertyValues, ServletRequest request) {
        super.addBindValues(propertyValues, request);
        mappingTable.forEach((from, to) -> {
            if (propertyValues.contains(from)) {
                PropertyValue propertyValue = propertyValues.getPropertyValue(from);
                assert propertyValue != null;
                propertyValues.add(to, propertyValue.getValue());
            }
        });
    }

}