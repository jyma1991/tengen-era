package tech.mars.tengen.era.utils;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月7日 下午2:26:55
 */

import java.security.MessageDigest;

/**
 * 类Md5Utils的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/7 14:26
 */
public class Md5Utils {
    public static String md5(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plaintext.getBytes("UTF-8"));
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 enc error", e);
        }
    }
}
