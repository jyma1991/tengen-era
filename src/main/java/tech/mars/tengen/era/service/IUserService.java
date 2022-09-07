package tech.mars.tengen.era.service;

import tech.mars.tengen.era.entity.User;
import tech.mars.tengen.era.entity.dto.LoginDTO;
import tech.mars.tengen.era.entity.dto.TokenDTO;
import tech.mars.tengen.era.entity.dto.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author majunyang
 * @since 2022-09-06
 */
public interface IUserService extends IService<User> {
    User getUserByToken(String token);

    Long saveOrUpdateUser(UserDTO req);

    TokenDTO login(LoginDTO req);
}
