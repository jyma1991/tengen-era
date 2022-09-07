package tech.mars.tengen.era.entity.query;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午5:18:44
 */

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;


/**
 * 类UserQuery的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 17:18
 */
@Data
public class UserQuery extends PageDTO{
    @Parameter(description="name")
    private String name;
}
