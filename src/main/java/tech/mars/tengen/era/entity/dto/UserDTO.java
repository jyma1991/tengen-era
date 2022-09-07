package tech.mars.tengen.era.entity.dto;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午5:38:07
 */

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 类UserDTO的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 17:38
 */
@Data
public class UserDTO {
    private Long id;

    @Parameter(description="username")
    private String username;

    @Parameter(description="password")
    private String passwd;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

}
