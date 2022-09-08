package tech.mars.tengen.era.utils.http;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月8日 下午7:31:38
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 类OkhttpFactory的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/8 19:31
 */
public class OkhttpFactory {

    public static final String            HTTP_PREFIX                 = "http:";
    public static final String            HTTPS_PREFIX                = "https:";
    public static final MediaType APPLICATION_JSON_UTF8_VALUE = MediaType
            .parse("application/json; charset=utf-8");
    public static final MediaType         APPLICATION_XML_VALUE       = MediaType.parse("application/xml");
    private volatile OkHttpClient client;

    private volatile static OkhttpFactory okHttpFactory               = null;

    /**
     * 创建一个工厂和一个默认的 {@link OkHttpClient} 实例。
     */
    private OkhttpFactory() {
        this.client = new OkHttpClient();
    }

    /**
     * 获取当前实例
     *
     * @return
     */
    public static OkhttpFactory getInstance() {
        if (null == okHttpFactory) {
            //影子实例
            syncInit();
        }
        return okHttpFactory;
    }

    /**
     * 同步初始化当前实例，影子实例.把创建实例和获取实例分开
     *
     * <pre>
     * 影子实例：外部获取不到该实例，更新属性时，直接生成另一个单例对象实例
     *
     * @return
     */
    private static synchronized void syncInit() {
        if (null == okHttpFactory) {
            okHttpFactory = new OkhttpFactory();
        }
    }

    /**
     * 定制自己的client,可自己在外部设置 client 的一些参数,如过期时间或者更多的配置.
     *
     * @param client
     */
    public OkhttpFactory(OkHttpClient client) {
        this.client = client;
    }

    /**
     * 发起一个GET请求
     *
     * @param url : 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String get(String url) throws IOException, URISyntaxException {
        return this.get(new URI(url));
    }

    /**
     * 发起一个GET请求
     *
     * @param uri : 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @return
     * @throws IOException
     */
    public String get(URI uri) throws IOException {
        Request request = new Request.Builder().url(uri.toURL()).get().build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个GET请求 fuck the GFW
     *
     * @param url : 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String getByProxy(String url) throws IOException, URISyntaxException {
        return this.getWithProxy(new URI(url));
    }

    public String getWithProxy(URI uri) throws IOException {
        Request request = new Request.Builder().url(uri.toURL()).get().build();
        String hostname = "127.0.0.1";
        int port = 10809;
        Proxy proxy = new Proxy(Type.HTTP,
                new InetSocketAddress(hostname, port));

        client = new OkHttpClient().newBuilder().
                connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).proxy(proxy).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个GET请求
     *
     * @param url 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @param headerName
     * @param headerValue
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String get(String url, String headerName, String headerValue) throws IOException, URISyntaxException {
        return this.get(new URI(url), headerName, headerValue);
    }

    /**
     * 发起一个GET请求
     *
     * @param uri 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @param headerName
     * @param headerValue
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String get(URI uri, String headerName, String headerValue) throws IOException, URISyntaxException {
        Map<String, String> headers = new HashMap<>();
        headers.put(headerName, headerValue);
        return this.get(uri, headers, null);
    }

    /**
     * 发起一个GET请求
     *
     * @param url 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @param headers
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String get(String url, Map<String, String> headers) throws IOException, URISyntaxException {
        return this.get(new URI(url), headers, null);
    }

    /**
     * 发起一个GET请求
     *
     * @param url 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @param headers
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String get(String url, Map<String, String> headers, Map<String, Object> params)
            throws IOException, URISyntaxException {
        return this.get(new URI(url), headers, params);
    }

    /**
     * 发起一个GET请求
     *
     * @param uri 请求地址,需要用 {@link URI} 封装,这样如果地址错了可以在请求前解析出来
     * @param headers
     * @param params
     * @return
     * @throws Exception
     */
    public String get(URI uri, Map<String, String> headers, Map<String, Object> params)
            throws IOException, URISyntaxException {
        Builder builder = new Request.Builder();
        for (String name : headers.keySet()) {
            builder.addHeader(name, headers.get(name));
        }
        uri = appendUri(uri, params);
        Request request = builder.url(uri.toURL()).get().build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public Response getResponse(String url) throws IOException, URISyntaxException {
        Builder builder = new Request.Builder();
        Request request = builder.url(new URI(url).toURL()).get().build();
        return client.newCall(request).execute();
    }

    /**
     * 发起一个 自定义contentType的请求
     *
     * @param uri : 请求地址
     * @param httpMethod : 请求方法 {@link HttpMethod}
     * @param contentType : 请求类型
     * @param content : 请求内容
     * @return
     * @throws IOException
     */
    public String createRequest(URI uri, HttpMethod httpMethod, MediaType contentType, String content)
            throws IOException {

        RequestBody body = RequestBody.create(content,contentType );
        Request request = new Request.Builder().url(uri.toURL()).method(httpMethod.name(), body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个 application/json; charset=utf-8 请求
     *
     * @param url : 请求地址
     * @param httpMethod : 请求方法 {@link HttpMethod}
     * @param content : 请求内容
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String createRequest(String url, HttpMethod httpMethod, String content)
            throws IOException, URISyntaxException {
        return createRequest(new URI(url), httpMethod, content);
    }

    /**
     * 发起一个 application/json; charset=utf-8 请求
     *
     * @param uri : 请求地址
     * @param httpMethod : 请求方法 {@link HttpMethod}
     * @param content : 请求内容
     * @return
     * @throws IOException
     */
    public String createRequest(URI uri, HttpMethod httpMethod, String content) throws IOException {

        RequestBody body = RequestBody.create( content,APPLICATION_JSON_UTF8_VALUE);
        Request request = new Request.Builder().url(uri.toURL()).method(httpMethod.name(), body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个 application/json; charset=utf-8 请求,并携带请求头
     *
     * @param url
     * @param httpMethod
     * @param content
     * @param headers
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String createRequestHeader(String url, HttpMethod httpMethod, String content, Map<String, String> headers)
            throws IOException, URISyntaxException {
        return createRequestHeader(new URI(url), httpMethod, content, headers);
    }

    /**
     * 发起一个 application/json; charset=utf-8 请求,并携带请求头
     *
     * @param uri
     * @param httpMethod
     * @param content
     * @param headers
     * @return
     * @throws IOException
     */
    public String createRequestHeader(URI uri, HttpMethod httpMethod, String content, Map<String, String> headers)
            throws IOException {

        RequestBody body = RequestBody.create( content,APPLICATION_JSON_UTF8_VALUE);
        Request.Builder builder = new Request.Builder().url(uri.toURL()).method(httpMethod.name(), body);
        //       by.jdk1.8写法  headers.forEach( ( key , value ) -> builder.addHeader( key , value ) );
        for (String name : headers.keySet()) {
            builder.addHeader(name, headers.get(name));
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个 application/json; charset=utf-8 请求,并携带请求头
     *
     * @param url
     * @param httpMethod
     * @param headers
     * @return
     * @throws IOException
     */
    public String createRequestHeader(String url, HttpMethod httpMethod, Map<String, String> headers)
            throws IOException, URISyntaxException {
        return this.createRequestHeader(new URI(url), httpMethod, headers);
    }

    /**
     * 发起一个 application/json; charset=utf-8 请求,并携带请求头
     *
     * @param uri
     * @param httpMethod
     * @param headers
     * @return
     * @throws IOException
     */
    public String createRequestHeader(URI uri, HttpMethod httpMethod, Map<String, String> headers) throws IOException {

        RequestBody body = RequestBody.create("{}",APPLICATION_JSON_UTF8_VALUE );
        Request.Builder builder = new Request.Builder().url(uri.toURL()).method(httpMethod.name(), body);
        headers.forEach( ( key , value ) -> builder.addHeader( key , value ) );
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个以 application/json; charset=utf-8 形式的请求
     *
     * @param uri
     * @param httpMethod
     * @param paramName
     * @param paramValue
     * @return
     * @throws IOException
     */
    public String createRequest(URI uri, HttpMethod httpMethod, String paramName, String paramValue)
            throws IOException {

        FormBody body = new FormBody.Builder().addEncoded(paramName, paramValue).build();
        Request request = new Request.Builder().url(uri.toURL()).method(httpMethod.name(), body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发起一个以 application/x-www-form-urlencoded 形式的请求
     *
     * @param url
     * @param httpMethod
     * @param params
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String createFromRequest(String url, HttpMethod httpMethod, Map<String, Object> params)
            throws IOException, URISyntaxException {
        return createFromRequest(new URI(url), httpMethod, params);
    }

    /**
     * 发起一个以 application/x-www-form-urlencoded 形式的请求
     *
     * @param uri
     * @param httpMethod
     * @param params
     * @return
     * @throws IOException
     */
    public String createFromRequest(URI uri, HttpMethod httpMethod, Map<String, Object> params) throws IOException {
        FormBody.Builder body = new FormBody.Builder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            body.addEncoded(param.getKey(), ObjectUtils.toString(param.getValue()));
        }
        Request request = new Request.Builder().url(uri.toURL()).method(httpMethod.name(), body.build()).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String createFromRequest(String url, HttpMethod httpMethod, Map<String, Object> params,
            Map<String, String> headers)
            throws IOException, URISyntaxException {
        return createFromRequest(new URI(url), httpMethod, params, headers);
    }

    public String createFromRequest(URI uri, HttpMethod httpMethod, Map<String, Object> params,
            Map<String, String> headers)
            throws IOException {
        FormBody.Builder body = new FormBody.Builder();
        //      by JDK1.8  params.forEach( body::addEncoded );
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                body.addEncoded(param.getKey(), ObjectUtils.toString(param.getValue()));
            }
        }

        final Request.Builder builder = new Request.Builder().url(uri.toURL());
        //      by JDK1.8       headers.forEach( builder::header );
        for (Map.Entry<String, String> param : headers.entrySet()) {
            builder.header(param.getKey(), param.getValue());
        }
        Request request = builder.method(httpMethod.name(), body.build()).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 设置底层读超时,以毫秒为单位。值0指定无限超时。
     *
     * @see okhttp3.OkHttpClient.Builder#readTimeout(long , TimeUnit)
     */
    public void setReadTimeout(int readTimeout) {
        this.client = this.client.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
    }

    /**
     * 设置底层写超时,以毫秒为单位。值0指定无限超时。
     *
     * @see okhttp3.OkHttpClient.Builder#writeTimeout(long , TimeUnit)
     */
    public void setWriteTimeout(int writeTimeout) {
        this.client = this.client.newBuilder().writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
    }

    /**
     * 设置底层连接超时,以毫秒为单位。值0指定无限超时。
     *
     * @see okhttp3.OkHttpClient.Builder#connectTimeout(long , TimeUnit)
     */
    public void setConnectTimeout(int connectTimeout) {
        this.client = this.client.newBuilder().connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).build();
    }

    /**
     * 忽略SSL
     *
     * @see okhttp3.OkHttpClient.Builder#sslSocketFactory(SSLSocketFactory,
     *      X509TrustManager)
     */
    public OkhttpFactory ignoreSslSocketFactory() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { trustManager }, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        this.client = this.client.newBuilder().sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(this.getHostnameVerifier()).build();
        return this;
    }

    private HostnameVerifier getHostnameVerifier() {
        return DefaultHostnameVerifier.instance;
    }

    private enum DefaultHostnameVerifier implements HostnameVerifier {
        instance;

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    /**
     * URI链接追加参数
     *
     * @param uri
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static URI appendUri(URI uri, Map<String, Object> params) throws URISyntaxException {
        boolean first = Boolean.TRUE;
        StringBuffer newQuery = new StringBuffer(StringUtils.isNotEmpty(uri.getQuery()) ? uri.getQuery() : "");
        if (!CollectionUtils.isEmpty(params)) {
            for (String key : params.keySet()) {
                if (first == Boolean.FALSE) {
                    newQuery.append("&");
                }
                newQuery.append(key);
                newQuery.append("=");
                newQuery.append(params.get(key));
                first = Boolean.FALSE;
            }
        }
        return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery.toString(), uri.getFragment());
    }

}
