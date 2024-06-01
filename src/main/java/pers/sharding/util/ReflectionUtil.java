package pers.sharding.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

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

    public static <T, R> List<T> convertList(List<R> source, Class<T> targetClass) {
        Assert.notNull(source, "source can't be null ");
        Assert.notNull(targetClass, "targetClass can't be null ");
        List<T> targetList = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            T targetInstance;
            try {
                targetInstance = targetClass.newInstance();
                BeanUtils.copyProperties(source.get(i), targetInstance);
                targetList.add(targetInstance);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("convert failed, targetClass:{}, source:{}", targetClass, source);
            }
        }
        return targetList;
    }
}
