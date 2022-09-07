package tech.mars.tengen.era.entity.dto;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午5:58:19
 */

import java.util.Date;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 类TokenDTO的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 17:58
 */
@Data
public class TokenDTO {
    @Parameter(description = "token")
    private String xToken;
    @Parameter(description = "expireTime")
    private Date expireTime;
}
