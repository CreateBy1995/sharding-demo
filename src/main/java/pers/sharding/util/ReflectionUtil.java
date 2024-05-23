package pers.sharding.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

@Slf4j
public class ReflectionUtil {

    public static <T> T convert(Object source, Class<T> targetClass) {
        Assert.notNull(source, "source can't be null ");
        Assert.notNull(targetClass, "targetClass can't be null ");
        T targetInstance = null;
        try {
            targetInstance = targetClass.newInstance();
            BeanUtils.copyProperties(source, targetInstance);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("convert failed, targetClass:{}, source:{}", targetClass, source);
        }
        return targetInstance;
    }
}
