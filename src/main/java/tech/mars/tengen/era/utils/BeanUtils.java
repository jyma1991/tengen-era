package tech.mars.tengen.era.utils;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2020年11月25日 下午4:31:43
 */

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * 类BeanUtils的实现描述：TODO 类实现描述
 * @author majunyang 2020/11/25 16:31
 */
@Slf4j
public class BeanUtils {
    public static <T> T clone(Object o, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            if (null == o) {
                return null;
            } else {
                org.springframework.beans.BeanUtils.copyProperties(o, t);
                return t;
            }
        } catch (Exception var3) {
            log.error("将目标转换为DTO对象，发生异常:", var3);
            return null;
        }
    }

    public static <T> List<T> cloneList(List<?> os, Class<T> clazz) {
        List<T> ws = new ArrayList();
        if (null == os) {
            return ws;
        } else {
            Iterator var3 = os.iterator();

            while(var3.hasNext()) {
                Object o = var3.next();
                ws.add(clone(o, clazz));
            }

            return ws;
        }
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // then use Spring BeanUtils to copy and ignore null using our function
    public static void copyProperties(Object src, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
    
	public static <T> T copy(Object source, T target){
		org.springframework.beans.BeanUtils.copyProperties(source, target);
		return target;
	}

    public static <T> T copy(Object source, Supplier<T> supplier){
        final T target = supplier.get();
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }
}
