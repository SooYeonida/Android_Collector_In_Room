package com.ajou.capstone_design_freitag.API;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.util.List;
import java.util.Map;

public class RESTAPI {
    public static final String clientID = "XXyvh2Ij7l9rss0HAVObS880qY3penX57JXkib9q";
    private static RESTAPI instance = null;
    private String baseURL = "http://wodnd999999.iptime.org:8080";
    //private String baseURL = "http://localhost:8080";
    private String token = null;
    private String state = null;
    private String id = null;

    public String getToken() {
        return this.token;
    }
    public String getId() {
        return this.id;
    }

    private RESTAPI() {
    }

    public static RESTAPI getInstance() {
        if(instance == null) {
            instance = new RESTAPI();
        }
        return instance;
    }

    public boolean login(String userID, String userPassword) {
        APICaller login = new APICaller("GET", baseURL + "/api/login");
        login.setQueryParameter("userId", userID);
        login.setQueryParameter("userPassword", userPassword);

        Map<String, List<String>> result;
        try {
            login.request();
            result = login.getHeader();
            id = userID;
            token = result.get("Authorization").get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean signup(String userId, String userPassword, String userName, String userPhone, String userEmail, String userAffiliation) {
        APICaller signup = new APICaller("GET", baseURL + "/api/signup");
        signup.setQueryParameter("userId", userId);
        signup.setQueryParameter("userPassword", userPassword);
        signup.setQueryParameter("userName", userName);
        signup.setQueryParameter("userPhone", userPhone);
        signup.setQueryParameter("userEmail", userEmail);
        signup.setQueryParameter("userAffiliation", userAffiliation);

        String result;
        try {
            signup.request();
            result = signup.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(result.equals("success")) {
            try {
                state = signup.getHeader().get("state").get(0);
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void registerOpenBanking(Activity activity) {
        APICaller registerOpenBanking = new APICaller("GET", "https://testapi.openbanking.or.kr/oauth/2.0/authorize");
        registerOpenBanking.setQueryParameter("auth_type", "0");
        registerOpenBanking.setQueryParameter("scope", "login+transfer+inquiry");
        registerOpenBanking.setQueryParameter("response_type", "code");
        registerOpenBanking.setQueryParameter("redirect_uri", "http%3a%2f%2fwodnd999999.iptime.org%3a8080%2fexternalapi%2fopenbanking%2foauth%2ftoken&lang=kor");
        registerOpenBanking.setQueryParameter("client_id", clientID);
        registerOpenBanking.setQueryParameter("state", state);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(registerOpenBanking.getUrl());
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public String mypage(String userId){
        APICaller mypage = new APICaller("GET", baseURL + "/api/mypage");
        mypage.setQueryParameter("userId",userId);
        mypage.setHeader("Authorization",token);
        String result;
        try {
            mypage.request();
            result = mypage.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
