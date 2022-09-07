package tech.mars.tengen.era.test;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午2:34:15
 */

import org.junit.jupiter.api.Test;

import tech.mars.tengen.era.utils.PasswordUtil;

/**
 * 类Apptest的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 14:34
 */
public class Apptest {
    @Test
    public void testRandomString(){
        System.out.println(PasswordUtil.randomString(32));
    }

    @Test
    public void testGenPasswd(){
        System.out.println(PasswordUtil.genPassword("majunyangovuiCCEXfz8BMOwM"));
    }

    @Test
    public void testGenToken(){
        System.out.println(PasswordUtil.genXtoken());
    }
}
