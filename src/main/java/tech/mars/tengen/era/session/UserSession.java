package tech.mars.tengen.era.session;

import tech.mars.tengen.era.entity.User;


public class UserSession {

    private static ThreadLocal<User> local = new ThreadLocal<User>();

    public static void setUser(User user) {
        local.set(user);
    }

    public static User getSysUser() {
        return local.get();
    }

    public static Boolean isAdmin() {
        return false;
    }

    public static String getUsername() {
        return local.get().getUsername();
    }

    public static Long getUserId() {
        return local.get().getId();
    }

}
