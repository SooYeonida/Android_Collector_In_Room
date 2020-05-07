package com.ajou.capstone_design_freitag.API;

public class RESTAPI {
    private static RESTAPI instance = null;
    private String baseURL = "http://10.0.2.2:8080";
    private String token = null;

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
        login.setQueryParameter("userID", userID);
        login.setQueryParameter("userPassword", userPassword);
        return true;
    }

    public boolean signup(String userId, String userPassword, String userName, String userPhone, String userEmail, String userAffiliation, String userAccount, String userBank) {
        APICaller signup = new APICaller("GET", baseURL + "/api/signup");
        signup.setQueryParameter("userId", userId);
        signup.setQueryParameter("userPassword", userPassword);
        signup.setQueryParameter("userName", userName);
        signup.setQueryParameter("userBank", userBank);
        signup.setQueryParameter("userAccount", userAccount);
        signup.setQueryParameter("userPhone", userPhone);
        signup.setQueryParameter("userEmail", userEmail);
        signup.setQueryParameter("userAffiliation", userAffiliation);

        String result;
        try {
            result = signup.getResponse();
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
}
