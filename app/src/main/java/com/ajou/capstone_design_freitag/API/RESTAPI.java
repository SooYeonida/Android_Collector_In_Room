package com.ajou.capstone_design_freitag.API;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
    private static User userinstance = null;
    private static Project projectinstance = null;

    private String baseURL = "http://172.30.1.27:8080";
    //private String baseURL = "http://localhost:8080";
    private String token = null;
    private String state = null;

    public String getToken() {
        return this.token;
    }

    private RESTAPI() {
    }

    public static RESTAPI getInstance() {
        if(instance == null) {
            instance = new RESTAPI();
        }
        return instance;
    }
    public static User getUserinstance(){
        if(userinstance == null){
            userinstance = new User();
        }
        return userinstance;
    }
    public static Project getProjectinstance(){
        if(projectinstance == null){
            projectinstance = new Project();
        }
        return projectinstance;
    }

    public boolean login(String userID, String userPassword) {
        APICaller login = new APICaller("GET", baseURL + "/api/login");
        login.setQueryParameter("userId", userID);
        login.setQueryParameter("userPassword", userPassword);

        Map<String, List<String>> result;
        try {
            login.request();
            result = login.getHeader();
            getUserinstance().setUserID(userID);
            getUserinstance().setUserPwd(userPassword);
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
        JSONObject jsonObject = new JSONObject(result);
        user.setName(jsonObject.getString("userName"));
        getUserinstance().setName(user.getName());
        user.setEmail(jsonObject.getString("userEmail"));
        getUserinstance().setEmail(user.getEmail());
        user.setPhonenumber(jsonObject.getString("userPhone"));
        getUserinstance().setPhonenumber(user.getPhonenumber());
        user.setAffiliation(jsonObject.getString("userAffiliation"));
        getUserinstance().setAffiliation(user.getAffiliation());
        user.setUserID(jsonObject.getString("username"));
        getUserinstance().setUserID(user.getUserID());
        //level임의로
        user.setLevel("starter");

        return user;
    }

    public boolean update(String userName,String userPhone,String userEmail,String userAffiliation){
        APICaller update = new APICaller("PUT",baseURL+"/api/mypage/update");
        update.setQueryParameter("userName",userName);
        update.setQueryParameter("userPhone",userPhone);
        update.setQueryParameter("userEmail",userEmail);
        update.setQueryParameter("userAffiliation",userAffiliation);
        update.setHeader("Authorization",token);
        String result = null;
        try {
            update.request();
            result = update.getHeader().get("update").get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result.equals("success")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean makeProject(String projectName,String workType,String dataType,String subject,String wayContent,String conditionContent,String description,String totalData)
    {
        APICaller makeProject = new APICaller("GET",baseURL+"/api/project/collection");
        makeProject.setQueryParameter("projectName",projectName);
        makeProject.setQueryParameter("workType",workType);
        makeProject.setQueryParameter("dataType",dataType);
        makeProject.setQueryParameter("subject",subject);
        makeProject.setQueryParameter("wayContent",wayContent);
        makeProject.setQueryParameter("conditionContent",conditionContent);
        makeProject.setQueryParameter("description",description);
        makeProject.setQueryParameter("totalData",totalData);
        makeProject.setHeader("Authorization",token);
        String result = null; //헤더에서 버킷네임 null이면 실패 아니면 성공
        getProjectinstance().setProjectName(projectName);
        try {
            makeProject.request();
            result = makeProject.getHeader().get("bucketName").get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result.getBytes().length!=0){
            return true;
        }
        else{
            return false;
        }
    }
    //example 콘텐트에서 cost 받는거 해야됨.

    public Boolean pointPayment() throws Exception {
        APICaller pointPayment  = new APICaller("GET",baseURL+"/api/project/pointPayment");
        pointPayment.setHeader("Authorization",token);
        pointPayment.request();
        String result = null;
        result = pointPayment.getHeader().get("payment").get(0);

        if(result.equals("success")) {
            return true;
        } else {
            return false;
        }
    }



}
