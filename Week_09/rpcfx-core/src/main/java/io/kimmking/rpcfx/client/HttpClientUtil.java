package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author yansq
 * @date 2020/11/1
 */
public class HttpClientUtil {

    /**
     * 最大连接数
     */
    private final static int MAX_TOTAL_CONNECTIONS = 300;
    /**
     * 每个路由最大连接数
     */
    private final static int MAX_ROUTE_CONNECTIONS = 200;
    /**
     * 连接超时时间
     */
    private final static int CONNECT_TIMEOUT = 5000;
    /**
     * 读取超时时间
     */
    private final static int SOCKET_TIMEOUT = 5000;

    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

    private static CloseableHttpClient client;
    static {
        //创建httpclient连接池
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        //设置连接池最大数量
        connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        //设置单个路由最大连接数量
        connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        connManager.closeExpiredConnections();
        connManager.closeIdleConnections(30, TimeUnit.SECONDS);
        client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }


    public static void main(String[] args) {
        String response = HttpClientUtil.get("http://localhost:8080");
        System.out.println(response);
    }


    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        return execute(httpGet);
    }


    public static String post(final Object paramObj, final String url) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(paramObj), ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        return execute(httpPost);
    }

    private static String execute(HttpUriRequest request){

        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            request.abort();
        }
        return null;
    }

    public static String httpPostJson(final Object paramObj, final String url) {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
        StringEntity requestEntity = new StringEntity(JSON.toJSONString(paramObj), StandardCharsets.UTF_8);
        httpPost.setEntity(requestEntity);
        try {
            CloseableHttpResponse response = client.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
