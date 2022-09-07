package tech.mars.tengen.era.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.mars.tengen.era.aspect.annotation.Auth;
import tech.mars.tengen.era.aspect.annotation.LogAround;
import tech.mars.tengen.era.entity.User;
import tech.mars.tengen.era.entity.dto.LoginDTO;
import tech.mars.tengen.era.entity.dto.TokenDTO;
import tech.mars.tengen.era.entity.dto.UserDTO;
import tech.mars.tengen.era.entity.query.UserQuery;
import tech.mars.tengen.era.entity.response.ResponseResult;
import tech.mars.tengen.era.service.IUserService;
import tech.mars.tengen.era.utils.IpUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author majunyang
 * @since 2022-09-06
 */
@Slf4j
@Tag(name="user manager")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;

    @LogAround
    @Auth
    @Operation(summary ="list")
    @GetMapping("list")
    public ResponseResult<List<User>> getList(){
        LambdaUpdateWrapper<User> queryWrapper =new LambdaUpdateWrapper<>();
        queryWrapper.eq(User::getStatus,1);
        return ResponseResult.success(this.service.list(queryWrapper));
    }

    @LogAround
    @Operation(summary ="login")
    @PostMapping("login")
    public ResponseResult<TokenDTO> login(@RequestBody LoginDTO req, HttpServletRequest request){
        String requestIp = IpUtils.getIpAddress(request);
        log.info("login:"+req.getUserName()+" ip:"+requestIp);
        return ResponseResult.success(this.service.login(req));
    }

    @LogAround
    @Auth
    @Operation(summary ="create or update user")
    @PostMapping("createOrUpdate")
    public ResponseResult<Long> CreateUser(@RequestBody UserDTO req){
        return ResponseResult.success(this.service.saveOrUpdateUser(req));
    }

    @LogAround
    @Auth
    @Operation(summary ="page list")
    @GetMapping("pageList")
    public ResponseResult<PageInfo<User>> getList(@ParameterObject UserQuery query){
        LambdaUpdateWrapper<User> queryWrapper =new LambdaUpdateWrapper<>();
        queryWrapper.eq(User::getStatus,1);
        if(query!=null&& StringUtils.isNotEmpty(query.getName())){
            queryWrapper.and(c->c.like(User::getUsername,query.getName())
                    .or().like(User::getFirstName,query.getName())
                    .or().like(User::getMiddleName,query.getName())
                    .or().like(User::getLastName,query.getName()));
        }
        PageHelper.startPage(query.getPageNum(),query.getPageSize());
        List<User> users=this.service.list(queryWrapper);
        PageInfo<User>page=PageInfo.of(users);
        return ResponseResult.success(page);
    }

}
