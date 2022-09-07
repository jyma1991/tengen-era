package tech.mars.tengen.era.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.mars.tengen.era.constants.MtResponseStatus;
import tech.mars.tengen.era.entity.response.ResponseResult;
import tech.mars.tengen.era.utils.GsonUtils;
import tech.mars.tengen.era.utils.IpUtils;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    /**
     * 业务Service错误，httpStatusCode=200
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<?> exception(HttpServletRequest req, MethodArgumentNotValidException e) {
        log.info("【MethodArgumentNotValidException】=> Host {} invokes url {} param: {} error: {}", IpUtils.getIpAddress(req),
                req.getRequestURL(), GsonUtils.toJson(req.getParameterMap()), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
        return ResponseResult.fail(MtResponseStatus.HTTP_STATUS_400.getResponseCode(),errorMsg.toString());
    }

    @ExceptionHandler(value = MtException.class)
    @ResponseBody
    public ResponseResult errorHandler(HttpServletRequest req, MtException e) {
        log.info("exception", e);
        return ResponseResult.fail(e);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult errorHandler(HttpServletRequest req, Exception e) {
        log.warn("exception", e);
        return ResponseResult.fail(MtResponseStatus.HTTP_STATUS_500.getResponseCode(),e.getMessage());
    }
}
