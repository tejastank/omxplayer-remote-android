package com.maximgalushka.omxremote.service;

import com.google.gson.Gson;
import com.maximgalushka.omxremote.model.Command;
import com.maximgalushka.omxremote.model.FileSystem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Maxim Galushka
 */
public class RemoteControl {

    //private DefaultHttpClient httpClient;
    private String ROOT = "http://192.168.1.103/omx/command.php";
    private int TIMEOUT = 5000;

    public void init() {
    }

    public void clear() {
    }

    public FileSystem browse(String path) {
        Command command = new Command("browse", path);

        String json = sendToServer(command);

        Gson gson = new Gson();
        FileSystem fs = gson.fromJson(json, FileSystem.class);

        System.out.println(fs);
        return fs;
    }

    public String sendToServer(Command command) {
        HttpClientBuilder b = HttpClientBuilder.create();
        CloseableHttpClient httpClient = b.build();

        try {
//            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
//            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);

            System.out.println("Execute command: " + command);

            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            String request = gson.toJson(command);

            System.out.println("json request: " + request);

            HttpPost httpPost = new HttpPost(ROOT);
            httpPost.setEntity(new StringEntity(request));

            System.out.println("before execute");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println("after execute");
            HttpEntity entity = httpResponse.getEntity();
            System.out.println("after get entity");

            if (entity != null) {
                String response = EntityUtils.toString(entity);
                System.out.println("json response: " + response);
                EntityUtils.consumeQuietly(entity);
                return response;
            }
        } catch (Exception io) {
            io.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}