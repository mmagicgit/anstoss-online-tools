package de.mmagic.anstoss.anstosstransfermarket.service;

import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class AnstossOnlineHttp {

    static final String BASE_URL = "https://www.anstoss-online.de/";
    private final HttpClient httpClient;

    AnstossOnlineHttp() {
        httpClient = HttpClient.newBuilder().cookieHandler(new CookieManager()).followRedirects(HttpClient.Redirect.ALWAYS).build();
    }

    void login(String user, String password) {
        try {
            HttpRequest loginRequest = request("content/fixed/login.php")
                    .POST(HttpRequest.BodyPublishers.ofString("login_name=" + user + "&login_pw=" + password))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    HttpResponse<String> get(String urlPath) {
        try {
            HttpRequest request2 = request(urlPath).GET().build();
            return httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest.Builder request(String urlPath) throws URISyntaxException {
        return HttpRequest.newBuilder().uri(new URI(BASE_URL + urlPath));
    }
}
