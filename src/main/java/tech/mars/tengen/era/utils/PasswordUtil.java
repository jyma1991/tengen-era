package tech.mars.tengen.era.utils;


import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtil {

    private static String salt = "u09vzT6sejPRiv5H";

    public static String genPassword(String password) {

        return Md5Utils.md5(password + salt);
    }

    public static String randomString(int length){
        return RandomStringUtils.random(length, true, true);
    }

    public static String genXtoken(){
        return UUID.randomUUID().toString().replace("-", "")+randomString(16);
    }

}
