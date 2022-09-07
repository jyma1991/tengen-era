package tech.mars.tengen.era.utils;


import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import tech.mars.tengen.era.exception.MtException;


/**
 * Create by user Michael_xu on 2018/1/5
 */
public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws MtException 校验不通过，则报MtException异常
     */
    public static void validateEntity(Object object, Class<?>... groups) throws MtException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                if (constraint.getMessage() != null) {
                    msg.append(constraint.getPropertyPath().toString() + constraint.getMessage()).append("；");
                } else {
                    msg.append(constraint.getPropertyPath().toString() + constraint.getMessage()).append("；");
                }
            }
            throw new MtException(msg.toString());
        }
    }

    public static void validateList(List list, Class<?>... groups) throws MtException {
        for (Object o : list) {
            validateEntity(o, groups);
        }
    }


}
