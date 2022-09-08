package tech.mars.tengen.era.service.impl;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import tech.mars.tengen.era.constants.MtResponseStatus;
import tech.mars.tengen.era.entity.User;
import tech.mars.tengen.era.entity.dto.LoginDTO;
import tech.mars.tengen.era.entity.dto.LoginFromGoogleDTO;
import tech.mars.tengen.era.entity.dto.TokenDTO;
import tech.mars.tengen.era.entity.dto.UserDTO;
import tech.mars.tengen.era.exception.MtException;
import tech.mars.tengen.era.mapper.UserMapper;
import tech.mars.tengen.era.service.IUserService;
import tech.mars.tengen.era.thirdservice.GoogleService;
import tech.mars.tengen.era.utils.BeanUtils;
import tech.mars.tengen.era.utils.PasswordUtil;
import tech.mars.tengen.era.utils.ValidatorUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author majunyang
 * @since 2022-09-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private GoogleService googleService;

    @Override
    public User getUserByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Date now = new Date();
        return lambdaQuery().eq(User::getXToken, token).ge(User::getTokenExpire, now).one();
    }

    @Override
    public Long saveOrUpdateUser(UserDTO req) {
        if(req==null){
            return null;
        }

        LambdaQueryWrapper<User>queryWrapper=new LambdaQueryWrapper<>();
        //重名校验
        if (StringUtils.isNotBlank(req.getUsername())) {
            queryWrapper.eq(User::getUsername, req.getUsername().replaceAll("\\s*", ""));
            if (req.getId() != null) {
                queryWrapper.ne(User::getId, req.getId());
            }

            if (this.count(queryWrapper) > 0) {
                throw new MtException("username repeat");
            }
        }

        User user=new User();
        BeanUtils.copyProperties(req,user);
        if(req.getId()==null){
            if(StringUtils.isEmpty(req.getPasswd())||StringUtils.isEmpty(req.getUsername())){
                throw new MtException(MtResponseStatus.HTTP_STATUS_400);
            }
            user.setUsername(user.getUsername().replaceAll("\\s*", ""));
            user.setPasswdSalt(PasswordUtil.randomString(16));
            user.setPasswd(PasswordUtil.genPassword(user.getPasswd() + user.getPasswdSalt()));
            user.setStatus(1);

        }else{
            user.setPasswd(null);
            user.setUsername(null);
        }
        this.saveOrUpdate(user);
        return user.getId();
    }

    @Override
    public TokenDTO login(LoginDTO req) {
        ValidatorUtils.validateEntity(req);
        User user=this.lambdaQuery().eq(User::getStatus,1).eq(User::getUsername,req.getUserName()).one();
        if(user==null){
            throw new MtException(MtResponseStatus.HTTP_STATUS_601);
        }

        if(user.getPasswd()==null||!user.getPasswd().equals(PasswordUtil.genPassword(req.getUserPwd()+user.getPasswdSalt()))){
            throw new MtException(MtResponseStatus.HTTP_STATUS_601);
        }

        String token=        PasswordUtil.genXtoken();
        Date expireDate= DateUtils.addDays(new Date(),7);

        user.setXToken(token);
        user.setTokenExpire(expireDate);

        this.updateById(user);

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setXToken(token);
        tokenDTO.setExpireTime(expireDate);
        return tokenDTO;
    }

    @Override
    public TokenDTO loginByGoogleIdToken(LoginFromGoogleDTO req) {
        GoogleIdToken idToken = googleService.verifyIdTokenSimple(req.getIdToken());
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            User user = lambdaQuery().eq(User::getEmail, email).one();
            if (user == null) {
                user = new User();
                user.setThirdUserId(userId);
                user.setUserFrom((String) payload.get("iss"));
                user.setUsername(name);
                user.setEmail(email);
                user.setPasswdSalt(PasswordUtil.randomString(16));
                user.setFromClient((String) payload.get("aud"));
            }

            user.setEmailVerified(emailVerified ? 1 : 0);
            user.setFirstName(givenName);
            user.setLastName(familyName);
            user.setStatus(1);
            user.setPicture(pictureUrl);


            String token = PasswordUtil.genXtoken();
            Date expireDate = DateUtils.addDays(new Date(), 7);

            user.setXToken(token);
            user.setTokenExpire(expireDate);

            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setXToken(token);
            tokenDTO.setExpireTime(expireDate);
            this.saveOrUpdate(user);

            return tokenDTO;
        }
        return null;
    }
}
