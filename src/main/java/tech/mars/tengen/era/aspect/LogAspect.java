package tech.mars.tengen.era.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tech.mars.tengen.era.aspect.annotation.LogAround;
import tech.mars.tengen.era.utils.GsonUtils;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(logAround)")
    public Object doAround(ProceedingJoinPoint joinPoint, LogAround logAround) throws Throwable {
        long startTimeMillis = System.currentTimeMillis();
        long start = System.nanoTime();
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "#" + signature.getName();
        Logger logger = LoggerFactory.getLogger(methodName);
        Map<String, Object> jsonBefore = new HashMap<>(3);
        jsonBefore.put("start", startTimeMillis);
        jsonBefore.put("args", ArrayUtils.toString(joinPoint.getArgs()));
        jsonBefore.put("app", methodName);
        logger.info(GsonUtils.toJson(jsonBefore));

        Object result = joinPoint.proceed();
        long costTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        Map<String, Object> jsonAfter = new HashMap<>(5);
        jsonAfter.put("start", startTimeMillis);
        jsonAfter.put("end", System.currentTimeMillis());
        jsonAfter.put("cost", costTime);

        if(logAround.logReturn()){
            jsonAfter.put("result", result);
        }
        jsonAfter.put("app", methodName);
        logger.info(GsonUtils.toJson(jsonAfter));
        return result;
    }
}
