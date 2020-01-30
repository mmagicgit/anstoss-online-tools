package de.mmagic.anstoss.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class AnstossOnlineHttp implements Closeable {

    static final String BASE_URL = "https://www.anstoss-online.de/";
    CloseableHttpClient httpclient = HttpClients.createDefault();

    void login(String user, String password) {
        try {
            HttpPost httpPost = new HttpPost(BASE_URL + "content/fixed/login.php");
            List<NameValuePair> params = Arrays.asList(
                    new BasicNameValuePair("login_name", user),
                    new BasicNameValuePair("login_pw", password));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpclient.execute(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String get(String urlPath) {
        try {
            HttpGet httpGet = new HttpGet(BASE_URL + urlPath);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (httpclient != null) {
            httpclient.close();
        }
    }
}
