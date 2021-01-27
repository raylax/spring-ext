package org.inurl.spring.ext.bind;

import org.inurl.spring.ext.bind.annotation.ModelRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 映射处理器
 *
 * @author raylax
 */
public class ModelRequestParamProcessor extends ServletModelAttributeMethodProcessor {

    /**
     * 映射缓存 class -> mapped field -> field
     */
    private final Map<Class<?>, Map<String, String>> mappingCache = new ConcurrentHashMap<>();

    @Autowired
    public ModelRequestParamProcessor() {
        super(true);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
        Object target = binder.getTarget();
        if (target == null) {
            return;
        }
        Class<?> targetClass = target.getClass();
        // 先查询缓存
        Map<String, String> mapping = mappingCache.get(targetClass);
        if (mapping == null) {
            mapping = analyzeMapping(targetClass);
            mappingCache.put(targetClass, mapping);
        }
        // 构造binder
        ModelRequestParamBinder modelRequestParamBinder = new ModelRequestParamBinder(target, binder.getObjectName(), mapping);
        super.bindRequestParameters(modelRequestParamBinder, nativeWebRequest);
    }

    private static Map<String, String> analyzeMapping(Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        Map<String, String> mapping = new HashMap<>();
        for (Field field : fields) {
            ModelRequestParam requestParamAnnotation = AnnotationUtils.findAnnotation(field, ModelRequestParam.class);
            if (requestParamAnnotation != null) {
                String value = requestParamAnnotation.value();
                if (!value.isEmpty()) {
                    mapping.put(value, field.getName());
                }
            }
        }
        return mapping;
    }
}