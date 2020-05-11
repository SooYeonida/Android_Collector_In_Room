package com.ajou.capstone_design_freitag.API;

import java.util.List;
import java.util.Map;

public class RESTAPI {
    private static RESTAPI instance = null;
    private String baseURL = "http://192.168.0.31:8080";
    //private String baseURL = "http://localhost:8080";
    private String token = null;
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
            return true;
        } else {
            return false;
        }
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
