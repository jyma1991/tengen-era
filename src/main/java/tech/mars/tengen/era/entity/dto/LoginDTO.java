package tech.mars.tengen.era.entity.dto;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午5:55:55
 */

import javax.validation.constraints.NotBlank;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;


/**
 * 类LoginDTO的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 17:55
 */
@Data
public class LoginDTO {
    @Parameter(description="userName")
    @NotBlank
    private String userName;
    @NotBlank
    @Parameter(description="password")
    private String userPwd;
}
