package com.kminfosystems.android.localdatabasecode.web;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebService {

    private static WebService webService;
    DefaultHttpClient httpClient;
    HttpContext localContext;
    HttpResponse response = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;
    String webServiceUrl;
    private String ret;
    public WebService(String serviceName) {
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 3000);
        HttpConnectionParams.setSoTimeout(myParams, 3000);
        httpClient = new DefaultHttpClient();
        localContext = new BasicHttpContext();
        webServiceUrl = serviceName;
    }

    public static WebService getInstance(String serviceName) {

        if (webService == null) {
            webService = new WebService(serviceName);
        }
        webService.webServiceUrl = serviceName;
        return webService;
    }

    public synchronized String webInvoke(String methodName, Map<String, Object> data, String contentType, boolean isApiRequired, String authKey) {
        ret = null;
        httpPost = new HttpPost(webServiceUrl + methodName);
        response = null;
        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        if (isApiRequired) {
            httpPost.setHeader("Authorization", authKey);
        }
        if (data.size() > 0) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            for (String key : data.keySet()) {
                Object object = data.get(key);
                if (object != null) {
                    nameValuePairs.add(new BasicNameValuePair(key, object.toString()));
                }

            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        try {

            response = httpClient.execute(httpPost, localContext);

            if (response != null) {
                ret = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("wwww" + ret.getClass());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return ret;
    }

    public synchronized String webGet(String methodName, Map<String, Object> params) throws Exception {
        String getUrl = webServiceUrl;

        if (methodName != null) {
            getUrl += methodName;
        }

        int i = 0;
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (i == 0) {
                    getUrl += "?";
                } else {
                    getUrl += "&";
                }

                try {
                    getUrl += param.getKey() + "=" + URLEncoder.encode(param.getValue().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                i++;
            }
        }

        httpGet = new HttpGet(getUrl);
        Log.e("WebGetURL: ", getUrl);

        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e1) {
            throw new Exception("Server could not be contacted");
        }

        // we assume that the response body contains the error message
        try {
            ret = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new Exception("Malformed server response");
        }

        return ret;
    }

    public InputStream getHttpStream(String methodName, Map<String, Object> params) throws Exception {
        String getUrl = webServiceUrl + methodName;

        int i = 0;
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (i == 0) {
                    getUrl += "?";
                } else {
                    getUrl += "&";
                }

                try {
                    getUrl += param.getKey() + "=" + URLEncoder.encode(param.getValue().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                i++;
            }
        }

        httpGet = new HttpGet(getUrl);

        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e1) {
            throw new Exception("Server could not be contacted");
        }
        return response.getEntity().getContent();
    }

    public InputStream getHttpStreamFromPost(String methodName, String data, String contentType) throws IllegalStateException, IOException {
        httpPost = new HttpPost(webServiceUrl + methodName);
        response = null;
        StringEntity tmp = null;

        httpPost.setHeader("Content-Type", contentType);

        if (data != null) {
            try {
                tmp = new StringEntity(data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpPost.setEntity(tmp);
        }

        try {
            response = httpClient.execute(httpPost, localContext);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getEntity().getContent();
    }

    public void clearCookies() {
        ((AbstractHttpClient) httpClient).getCookieStore().clear();
    }

    public void abort() {
        try {
            if (httpClient != null) {
                System.out.println("Abort.");
                httpPost.abort();
            }
        } catch (Exception e) {
            System.out.println("Your App Name Here" + e);
        }
    }

    /**
     * @return the httpClient
     */
    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * @return the localContext
     */
    public HttpContext getLocalContext() {
        return localContext;
    }

}
