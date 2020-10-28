import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author yansq
 * @date 2020/10/28
 */
public class HttpClient {
    public static void main(String[] args) {
        String s = HttpClient.get("http://localhost:8808");
        System.out.println(s);
    }

//    @Test
//    public void testGet(){
//        String s = get("http://localhost:8080/keepAlive");
//        System.out.println(s);
//    }

    public static String get(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(get);
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
            get.abort();
        }
        return null;
    }
}
