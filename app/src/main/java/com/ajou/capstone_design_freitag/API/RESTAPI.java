package com.ajou.capstone_design_freitag.API;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.ajou.capstone_design_freitag.ui.home.User;
import com.ajou.capstone_design_freitag.ui.plus.Project;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RESTAPI {
    public static final String clientID = "XXyvh2Ij7l9rss0HAVObS880qY3penX57JXkib9q";
    private static RESTAPI instance = null;
    private String baseURL = "http://172.30.1.27:8080";
    //private String baseURL = "http://localhost:8080";
    private String token = null;
    private String state = null;

    private String id = null;
    private String password = null;

    JSONObject jsonObject = null;
    private User info = null;

    public String getToken() {
        return this.token;
    }
    public String getId() {
        return this.id;
    }
    public String getPassword() {
        return password;
    }
    public User getInfo() {
        return this.info;
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
            password = userPassword;
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

    public User mypage(String userId) throws JSONException {
        APICaller mypage = new APICaller("GET", baseURL + "/api/mypage");
        mypage.setQueryParameter("userId",userId);
        mypage.setHeader("Authorization",token);
        String result;
        User user = new User();
        try {
            mypage.request();
            result = mypage.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        jsonObject = new JSONObject(result);
        user.setName(jsonObject.getString("userName"));
        user.setEmail(jsonObject.getString("userEmail"));
        user.setAccount(jsonObject.getString("userOpenBankingAccessToken"));
        user.setBank(jsonObject.getInt("userOpenBankingNum"));
        user.setPhonenumber(jsonObject.getString("userPhone"));
        user.setAffiliation(jsonObject.getString("userAffiliation"));
        user.setUserID(jsonObject.getString("username"));
        //level임의로
        user.setLevel("starter"); //이걸 보내고 밑에만 살리기

        info = user;
        System.out.println("유저정보 in restapi:"+info.getName()+" "+info.getEmail());
        return user;
    }

    public boolean update(String userId,String userPassword,String userName,String userPhone,String userEmail,String userAffiliation){
        APICaller update = new APICaller("PUT",baseURL+"/api/mypage/update");
        update.setQueryParameter("userId",userId);
        update.setQueryParameter("userPassword",userPassword);
        update.setQueryParameter("userName",userName);
        update.setQueryParameter("userPhone",userPhone);
        update.setQueryParameter("userEmail",userEmail);
        update.setQueryParameter("userAffiliation",userAffiliation);
        update.setHeader("Authorization",token);
        String result = null;
        try {
            update.request();
            result = update.getBody();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result.equals("수정 완료")) {
            return true;
        } else {
            return false;
        }

    }

    public void makeProject(Project project, String userId) throws Exception {
       //하눈즁 ㅠ

    }

}
