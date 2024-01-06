package org.unibl.etf.sni.backend.auth;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.catalina.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.user.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@CrossOrigin("https://localhost:4200")
@RestController
@RequestMapping("/api")
public class GithubController {

    @Value("${spring.github.clientId}")
    private String clientId;

    @Value("${spring.github.clientSecret}")
    private String clientSecret;

    @Value("${spring.github.redirectUri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    private final AuthenticationService authenticationService;

    public GithubController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/callback")
    public ResponseEntity<JwtAuthResponse> callback(@RequestParam String code) throws URISyntaxException, IOException, NotFoundException {
        /*String clientId = "3bff39eca726cbbc11e0";
        String clientSecret = "4b757f0034a8acbf540362cef9b8bcdf556c5e18";
        String redirectUri = "https://localhost:4200/callback";*/

        System.out.println("Code " + code);

        // Construct the request URL
        URI uri = new URIBuilder("https://github.com/login/oauth/access_token")
                .addParameter("client_id", clientId)
                .addParameter("client_secret", clientSecret)
                .addParameter("code", code)
                .addParameter("redirect_uri", redirectUri)
                .build();

        // Create an HTTP POST request
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(uri);
        postRequest.setEntity(new StringEntity("", ContentType.APPLICATION_FORM_URLENCODED));

        // Send the request and get the response
        HttpResponse response = client.execute(postRequest);

        // Get the response body as a string
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        String accessToken = extractAccessToken(responseBody);
        String userDetails = getUserDetails(accessToken);
        String email = extractEmail(userDetails);

        UserModel userModel = userService.findByEmail(email);

        return new ResponseEntity<>(authenticationService.githubLogin(userModel), HttpStatus.OK);
    }

    private String extractAccessToken(String responseBody) {
        String prefix = "access_token=";
        String suffix = "&scope";
        int startIndex = responseBody.indexOf(prefix);
        int endIndex = responseBody.indexOf(suffix);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return responseBody.substring(startIndex + prefix.length(), endIndex);
        }
        return null; // Return null if the access token cannot be extracted
    }

    private String getUserDetails(String accessToken) throws URISyntaxException, IOException {
        URI uri = new URIBuilder("https://api.github.com/user")
                .build();

        // Create an HTTP GET request
        CloseableHttpClient client =  HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(uri);
        getRequest.setHeader("Authorization", "Bearer " + accessToken);

        // Send the request and get the response
        CloseableHttpResponse response = client.execute(getRequest);

        // Get the response body as a string
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        // Return the response body to the caller
        return responseBody;
    }


    private String extractEmail(String userDetails) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(userDetails, JsonObject.class);
        System.out.println("Extract login json object " + jsonObject);

        if (jsonObject.has("email")) {
            return jsonObject.get("email").getAsString();
        }

        return null;
    }

    /*private String extractLogin(String userDetails) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(userDetails, JsonObject.class);
        System.out.println("Extract login json object " + jsonObject);
        if (jsonObject.has("login")) {
            return jsonObject.get("login").getAsString();
        }
        return null; // Return null if the login field is not present
    }*/

    /*private String getUserEmail(String accessToken) throws URISyntaxException, IOException {
        URI uri = new URIBuilder("https://api.github.com/user/emails")
                .build();

        // Create an HTTP GET request
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(uri);
        getRequest.setHeader("Authorization", "Bearer " + accessToken);

        // Send the request and get the response
        CloseableHttpResponse response = client.execute(getRequest);

        // Check the status code
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            // If the status code is OK, parse the JSON response
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            // Parse the JSON response to get the email
            JsonArray emailArray = JsonParser.parseString(responseBody).getAsJsonArray();
            if (emailArray.size() > 0) {
                JsonObject emailObject = emailArray.get(0).getAsJsonObject();
                if (emailObject.has("email")) {
                    return emailObject.get("email").getAsString();
                }
            }
        } else {
            // If the status code indicates an error, handle it appropriately
            System.out.println("Error getting user email. Status code: " + statusCode);
        }

        return null; // Return null if the email cannot be extracted
    }*/


}
