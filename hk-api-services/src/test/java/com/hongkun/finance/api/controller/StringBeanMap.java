package com.hongkun.finance.api.controller;

import com.yirun.framework.core.utils.BeanPropertiesUtil;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.BiFunction;

public class StringBeanMap extends BeanPropertiesUtil {

    /**
     * 获取一个BeanMap
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> getBeanMap(T source) {
        BiFunction<String, Object, Boolean> decideFunction = (x, y) -> true;
        boolean setDefaultValue = true;
        Map<String, String> beanMap = new HashMap<>(16);
        ergodicProperties(source, (property) -> {
            PropertyDescriptor pd = (PropertyDescriptor) property;
            if (!"class".equals(pd.getName())) {
                try {
                    //抽取属性值
                    Object finalValue = extractValueFormReadMethod(source, pd.getReadMethod());
                    //判断决定函数
                    if (decideFunction.apply(pd.getName(), finalValue)) {
                        //判断是否是原生类型或者原生类型的包装类
                        Class<?> propertyType = pd.getPropertyType();
                        boolean primitiveArray = (ClassUtils.isPrimitiveArray(propertyType) || ClassUtils.isPrimitiveWrapperArray(propertyType));
                        //抽取属性值
                        if (finalValue == null && setDefaultValue) {
                            //给该属性一个初始值，只处理原生类型和String类型

                                if (ClassUtils.isPrimitiveOrWrapper(propertyType) || primitiveArray) {
                                //原生类型或者原生数组类型
                                finalValue = initPrimitiveTypeOrTypeArr(propertyType, primitiveArray);
                                if (!primitiveArray) {
                                    beanMap.put(pd.getName(), String.valueOf(finalValue));
                                }
                            } else if (String.class.equals(propertyType)) {
                                //String 类型
                                beanMap.put(pd.getName(), "");
                            }
                        }else{
                            //String 类型
                            if (!primitiveArray) {
                                if (String.class.equals(propertyType)) {
                                    beanMap.put(pd.getName(), (String) finalValue);
                                }else{
                                    beanMap.put(pd.getName(), String.valueOf(finalValue));
                                }
                            }

                        }

                    }
                } catch (Exception e) {
                    //do nothing...
                }
            }

        });
        return beanMap;
    }


}
