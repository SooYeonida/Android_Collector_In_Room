package com.ajou.capstone_design_freitag.API;

import android.content.ContentResolver;
import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class APICaller {
    private String method;
    private String url;
    private Map<String, String> queryParameters = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> jsonBody = new HashMap<>();
    private HttpURLConnection con = null;

    public APICaller(String method, String baseURL) {
        url = baseURL;
        this.method = method;
    }

    public void setQueryParameter(String key, String value) {
        queryParameters.put(key, value);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setJsonBody(String key, String value) {
        jsonBody.put(key, value);
    }

    public Map<String, List<String>> getHeader() throws Exception {
        if(con == null) {
            throw new Exception("getHeader before request");
        }

        int responseCode = con.getResponseCode();

        if(responseCode == 200) {
            con.disconnect();

            return con.getHeaderFields();
        } else {
            con.disconnect();
            throw new Exception(responseCode + " Error");
        }
    }

    public String getBody() throws Exception {
        if(con == null) {
            throw new Exception("getBody before request");
        }

        int responseCode = con.getResponseCode();

        if(responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
            return response.toString();
        } else {
            con.disconnect();
            throw new Exception(responseCode + " Error");
        }
    }


    public void request() throws IOException {
        makeURL();

        con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod(method);

        if(!headers.isEmpty()) {
            for(String key : headers.keySet()) {
                con.setRequestProperty(key, headers.get(key));
            }
        }

        if(!jsonBody.isEmpty()) {
            con.setDoOutput(true);
            con.getOutputStream().write(new JSONObject(jsonBody).toString().getBytes());
        }

        con.getResponseCode();
    }

    public String getUrl() {
        try {
            makeURL();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        return url;
    }

    private void makeURL() throws UnsupportedEncodingException {
        if(!queryParameters.isEmpty()) {
            Iterator<String> iterator = queryParameters.keySet().iterator();
            String key = iterator.next();
            url += "?" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(queryParameters.get(key), "UTF-8");
            while(iterator.hasNext()) {
                key = iterator.next();
                url += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(queryParameters.get(key), "UTF-8");
            }
        }
    }

    public boolean multipart(InputStream inputStream, String fileName, String type) throws Exception {
        String CRLF = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****b*o*u*n*d*a*r*y*****";

        con = (HttpURLConnection) new URL(url).openConnection();
        con.setDoOutput(true);
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

        DataOutputStream dataStream = new DataOutputStream(con.getOutputStream());
        dataStream.writeBytes(twoHyphens + boundary + CRLF);
        dataStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + CRLF);
        dataStream.writeBytes("Content-Type: " + type +  CRLF);
        dataStream.writeBytes(CRLF);

        int bytesAvailable = inputStream.available();
        int maxBufferSize = 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];
        int bytesRead = inputStream.read(buffer, 0, bufferSize);
        while (bytesRead > 0) {
            dataStream.write(buffer, 0, bufferSize);
            bytesAvailable = inputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = inputStream.read(buffer, 0, bufferSize);
        }
        dataStream.writeBytes(CRLF);
        dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);
        inputStream.close();
        dataStream.flush();
        dataStream.close();
        if(con.getResponseCode() == 200) {
            return true;
        } else {
            return false;
        }
    }
}
