package com.ajou.capstone_design_freitag.API;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
        if(!queryParameters.isEmpty()) {
            Iterator<String> iterator = queryParameters.keySet().iterator();
            String key = iterator.next();
            url += "?" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(queryParameters.get(key), "UTF-8");
            while(iterator.hasNext()) {
                key = iterator.next();
                url += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(queryParameters.get(key), "UTF-8");
            }
        }

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
}
