package tech.mars.tengen.era.entity.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

import tech.mars.tengen.era.constants.MtResponseStatus;
import tech.mars.tengen.era.exception.MtException;

@Data
@Builder
public class ResponseResult<T> {

    /**
     * response timestamp.
     */
    private long current;

    /**
     * response code, 200 -> OK.
     */
    private String status;

    /**
     * response message.
     */
    private String message;

    /**
     * response data.
     */
    private T data;

    /**
     * response success result wrapper.
     *
     * @param <T> type of data class
     * @return response result
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    /**
     * response success result wrapper.
     *
     * @param data response data
     * @param <T>  type of data class
     * @return response result
     */
    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.<T>builder().data(data)
                .message(MtResponseStatus.SUCCESS.getDescription())
                .status(MtResponseStatus.SUCCESS.getResponseCode())
                .current(System.currentTimeMillis())
                .build();
    }

    /**
     * response error result wrapper.
     *
     * @param message error message
     * @param <T>     type of data class
     * @return response result
     */
    public static <T extends Serializable> ResponseResult<T> fail(String message) {
        return fail(null, message);
    }

    public static <T extends Serializable> ResponseResult<T> fail(String errorCode,String message) {
        return fail(errorCode, message,null);
    }

    /**
     * response error result wrapper.
     *
     * @param data    response data
     * @param message error message
     * @param <T>     type of data class
     * @return response result
     */
    public static <T> ResponseResult<T> fail(T data, String message) {
        return ResponseResult.<T>builder().data(data)
                .message(message)
                .status(MtResponseStatus.FAIL.getResponseCode())
                .current(System.currentTimeMillis())
                .build();
    }

    public static <T> ResponseResult<T> fail(String errorCode,String message,T data) {
        return ResponseResult.<T>builder()
                .data(data)
                .status(errorCode)
                .message(message)
                .status(errorCode)
                .current(System.currentTimeMillis())
                .build();
    }

    public static <T> ResponseResult<T> fail(MtException exception) {
        return ResponseResult.<T>builder()
                .status(exception.getErrorCode())
                .message(exception.getMessage())
                .status(exception.getErrorCode())
                .current(System.currentTimeMillis())
                .build();
    }

}
