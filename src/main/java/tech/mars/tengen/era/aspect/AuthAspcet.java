package tech.mars.tengen.era.aspect;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import tech.mars.tengen.era.aspect.annotation.Auth;
import tech.mars.tengen.era.constants.MtResponseStatus;
import tech.mars.tengen.era.entity.User;
import tech.mars.tengen.era.exception.MtException;
import tech.mars.tengen.era.service.IUserService;
import tech.mars.tengen.era.session.UserSession;

/**
 * Create by majunyang
 */
@Slf4j
@Component
@Aspect
public class AuthAspcet {

    @Autowired
    private IUserService userService;


    @Around("@annotation(auth)")
    public Object authCheck(ProceedingJoinPoint joinPoint, Auth auth) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("x-token");
        if (StringUtils.isEmpty(token)) {
            throw new MtException(MtResponseStatus.HTTP_STATUS_401);
        }
        User user=userService.getUserByToken(token);
        if(user==null){
            throw new MtException(MtResponseStatus.HTTP_STATUS_401);
        }

        UserSession.setUser(user);

        Object result = joinPoint.proceed();
        return result;
    }
}
