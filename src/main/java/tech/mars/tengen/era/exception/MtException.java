package tech.mars.tengen.era.exception;

import lombok.Getter;
import lombok.Setter;

import tech.mars.tengen.era.constants.MtResponseStatus;


public class MtException extends  RuntimeException  {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String            errorCode;
    /**
     * 针对属性为空异常异常
     */
    @Getter
    @Setter
    private String            attribute;            // 属性名称

    /**
     * 针对属性为空异常异常
     *
     * @param errorCode
     * @param msg
     * @param attribute
     */
    public MtException(String errorCode, String msg, String attribute) {
        super(msg);
        this.errorCode = errorCode;
        this.attribute = attribute;
    }

    public MtException(String errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;

    }

    public MtException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public MtException(String msg) {
        super(msg);
        this.errorCode = "500";
    }

    public MtException(MtResponseStatus error) {
        super(error.getDescription());
        this.errorCode = error.getResponseCode();
    }

}
