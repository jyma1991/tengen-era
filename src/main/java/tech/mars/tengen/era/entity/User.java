package tech.mars.tengen.era.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author majunyang
 * @since 2022-09-06
 */
@Data
@TableName(value = "t_user", autoResultMap = true)
@Schema(name = "UserDO", description = "$!{table.comment}")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Parameter(name="username")
    private String username;

    @Parameter(hidden = true)
    @JsonIgnore
    private String passwd;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private Integer status;

    @TableLogic
    private String isDeleted;

    private String creator;

    private String modifier;

    private Date createdTime;

    private Date modifiedTime;

    @Parameter(hidden = true)
    @JsonIgnore
    private String xToken;

    @Parameter(hidden = true)
    @JsonIgnore
    private Date tokenExpire;

    @Parameter(hidden = true)
    @JsonIgnore
    private String passwdSalt;

    private Integer emailVerified;

    private String picture;

    private String fromClient;

    private String thirdUserId;

    private String userFrom;


}
