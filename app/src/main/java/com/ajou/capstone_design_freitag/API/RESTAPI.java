package com.ajou.capstone_design_freitag.API;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ajou.capstone_design_freitag.ui.home.User;
import com.ajou.capstone_design_freitag.ui.plus.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RESTAPI {
    public static final String clientID = "XXyvh2Ij7l9rss0HAVObS880qY3penX57JXkib9q";
    private static RESTAPI instance = null;
    private String baseURL = "http://10.0.2.2:8080";
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

    public boolean login(String userID, String userPassword) {
        APICaller login = new APICaller("POST", baseURL + "/api/login");
        login.setQueryParameter("userId", userID);
        login.setQueryParameter("userPassword", userPassword);
        Map<String, List<String>> result;
        try {
            login.request();
            result = login.getHeader();
            User.getUserinstance().setUserID(userID);
            User.getUserinstance().setUserPwd(userPassword);
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

        Map<String, List<String>> result;
        try {
            signup.request();
            result = signup.getHeader();
            if(result.get("update").get(0).equals("success")) {
                state = signup.getHeader().get("state").get(0);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void registerOpenBanking(Activity activity) {
        APICaller registerOpenBanking = new APICaller("GET", "https://testapi.openbanking.or.kr/oauth/2.0/authorize");
        registerOpenBanking.setQueryParameter("auth_type", "0");
        registerOpenBanking.setQueryParameter("scope", "login+transfer+inquiry");
        registerOpenBanking.setQueryParameter("response_type", "code");
        registerOpenBanking.setQueryParameter("redirect_uri", "http%3a%2f%2fwodnd999999.iptime.org%3a8080%2fexternalapi%2fopenbanking%2foauth%2ftoken");
        registerOpenBanking.setQueryParameter("lang", "kor");
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
        User.getUserinstance().setName(user.getName());
        user.setEmail(jsonObject.getString("userEmail"));
        User.getUserinstance().setEmail(user.getEmail());
        user.setPhonenumber(jsonObject.getString("userPhone"));
        User.getUserinstance().setPhonenumber(user.getPhonenumber());
        user.setAffiliation(jsonObject.getString("userAffiliation"));
        User.getUserinstance().setAffiliation(user.getAffiliation());
        user.setUserID(jsonObject.getString("username"));
        User.getUserinstance().setUserID(user.getUserID());
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
        APICaller makeProject = new APICaller("POST",baseURL+"/api/project/create");
        makeProject.setQueryParameter("projectName",projectName);
        makeProject.setQueryParameter("workType",workType);
        makeProject.setQueryParameter("dataType",dataType);
        makeProject.setQueryParameter("subject",subject);
        makeProject.setQueryParameter("wayContent",wayContent);
        makeProject.setQueryParameter("conditionContent",conditionContent);
        makeProject.setQueryParameter("description",description);
        makeProject.setQueryParameter("totalData",totalData);
        makeProject.setHeader("Authorization",token);
        String result = null;

        Project.getProjectinstance().setProjectName(projectName);
        try {
            makeProject.request();
            result = makeProject.getHeader().get("create").get(0);
            Project.getProjectinstance().setProjectId(makeProject.getHeader().get("projectId").get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result.equals("success")){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean createClass(List<String> classList) throws Exception {
        APICaller classname = new APICaller("POST",baseURL+"/api/project/class");
        classname.setHeader("Authorization",token);

        List<String> id = new ArrayList<>();
        id.add(Project.getProjectinstance().getProjectId());
        classname.setQueryParameter_class("className",classList);//안들
        classname.setQueryParameter_class("projectId",id);

        classname.request_class();

        String result;
        result = classname.getHeader().get("class").get(0);
        Project.getProjectinstance().setBucketName(classname.getHeader().get("bucketName").get(0));
        if(result.equals("success")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean uploadExampleFile(InputStream inputStream,String fileName,String fileType) throws Exception {
        APICaller uploadFile = new APICaller("POST", baseURL + "/api/project/upload/example");
        Map<String, List<String>> header;
        uploadFile.setHeader("Authorization",token);
        uploadFile.setHeader("bucketName",Project.getProjectinstance().getBucketName());
        header = uploadFile.multipart(inputStream, fileName, fileType);
        String result;
        result = Objects.requireNonNull(header.get("example")).get(0);
        Project.getProjectinstance().setCost(Integer.parseInt(header.get("cost").get(0)));
        if(!result.equals("success")){
            return false;
        }
        return true;
    }


    public Boolean pointPayment() throws Exception {
        APICaller pointPayment  = new APICaller("GET",baseURL+"/api/project/point/payment");
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

    public List<Project> projectList(String workType,String dataType,String subject, String difficulty) throws Exception {
     APICaller projectList = new APICaller("GET",baseURL+"/api/project/list");
     projectList.setHeader("Authorization",token);
     projectList.setQueryParameter("workType",workType);
     projectList.setQueryParameter("dataType",dataType);
     projectList.setQueryParameter("subject",subject);
     projectList.setQueryParameter("difficulty",difficulty);
     projectList.request();
     String result;
     List<Project> project_list = new ArrayList<Project>();
     result = projectList.getBody();
     JSONArray jsonArray = new JSONArray(result);
     for(int i=0;i<jsonArray.length();i++){
         Project project = new Project();
         JSONObject jsonObject;
         jsonObject = jsonArray.getJSONObject(i);
         project.setUserId(jsonObject.getString("userId"));
         project.setProjectName(jsonObject.getString("projectName"));
         project.setWorkType(jsonObject.getString("workType"));
         project.setDataType(jsonObject.getString("dataType"));
         project.setSubject(jsonObject.getString("subject"));
         project.setDifficulty(jsonObject.getInt("difficulty"));
         project.setWayContent(jsonObject.getString("wayContent"));
         project.setConditionContent(jsonObject.getString("conditionContent"));
         project.setExampleContent(jsonObject.getString("description"));
         project_list.add(project);
     }
     return project_list;
    }




}
