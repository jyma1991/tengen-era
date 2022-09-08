package tech.mars.tengen.era.thirdservice;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月8日 下午7:03:40
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import tech.mars.tengen.era.utils.GsonUtils;
import tech.mars.tengen.era.utils.http.OkhttpFactory;

/**
 * 类GoogleService的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/8 19:03
 */
@Service
@Slf4j
public class GoogleService {

    @Value("${google.testApp.clientId}")
    private String testAppId;

    public GoogleIdToken verifyIdToken(String strIdToken){
        if(StringUtils.isEmpty(strIdToken)){
            return null;
        }
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory())
                .setAudience(Collections.singletonList(testAppId))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(strIdToken);
        }  catch (Exception e) {
            log.error("verifyIdToken error:",e);
        }
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

            return idToken;
        } else {
            System.out.println("Invalid ID token.");
        }
        return null;
    }

    public GoogleIdToken verifyIdTokenSimple(String strIdToken){
        if(StringUtils.isEmpty(strIdToken)){
            return null;
        }
        String url="https://oauth2.googleapis.com/tokeninfo?id_token="+strIdToken;
        OkhttpFactory okhttpFactory=OkhttpFactory.getInstance();

        try {
            String result=okhttpFactory.getByProxy(url);
            GoogleIdToken idToken = GoogleIdToken.parse(Utils.getDefaultJsonFactory(), strIdToken);
            return idToken;
        } catch (Exception e) {
            log.error("http request error",e);
        }
        return null;
    }
}
