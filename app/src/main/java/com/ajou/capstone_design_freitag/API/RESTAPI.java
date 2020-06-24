package com.ajou.capstone_design_freitag.API;

import android.widget.BaseAdapter;

import com.ajou.capstone_design_freitag.UI.dto.LabellingWorkHistory;
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.ajou.capstone_design_freitag.UI.dto.User;
import com.google.common.net.UrlEscapers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import awsauth.AWS4SignerForAuthorizationHeader;

public class RESTAPI {
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_SUCCESS_WITH_REWARD = 1;
    public static final int LOGIN_FAIL = 2;

    public static final int REGISTER_SUCCESS = 0;
    public static final int REGISTER_VALIDATION_FAIL = 1;
    public static final int REGISTER_FAIL = 2;

    private static final String clientID = "R9zNr3OIJNyJfj8pbMnBATU9OF6RtGb7Ih63xAdq";
    private static RESTAPI instance = null;
    private String baseURL = "http://10.0.2.2:8080";
    //private String baseURL = "http://101.101.208.224:8080";
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

    public int login(String userID, String userPassword) {
        APICaller login = new APICaller("POST", baseURL + "/api/login");
        login.setQueryParameter("userId", userID);
        login.setQueryParameter("userPassword", userPassword);
        Map<String, List<String>> result;
        try {
            login.request();
            result = login.getHeader();
            if(result.get("login").get(0).equals("success")) {
                User.getUserinstance().setUserID(userID);
                User.getUserinstance().setUserPwd(userPassword);
                token = result.get("Authorization").get(0);
                System.out.println(token);
                if(result.get("reward") == null) {
                    return LOGIN_SUCCESS;
                } else {
                    return LOGIN_SUCCESS_WITH_REWARD;
                }
            } else {
                return LOGIN_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LOGIN_FAIL;
        }
    }

    public int signup(String userId, String userPassword, String userName, String userPhone, String userEmail, String userAffiliation) {
        APICaller signup = new APICaller("POST", baseURL + "/api/signup");
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
            if(result.get("signup").get(0).equals("success")) {
                state = signup.getHeader().get("state").get(0);
                return REGISTER_SUCCESS;
            } else {
                return REGISTER_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return REGISTER_FAIL;
        }
    }

    public String getRegisterOpenBankingURL() {
        APICaller registerOpenBanking = new APICaller("GET", "https://testapi.openbanking.or.kr/oauth/2.0/authorize");
        registerOpenBanking.setQueryParameter("auth_type", "0");
        registerOpenBanking.setQueryParameter("scope", "login+transfer+inquiry");
        registerOpenBanking.setQueryParameter("response_type", "code");
        registerOpenBanking.setQueryParameter("redirect_uri", "http%3a%2f%2fwodnd999999.iptime.org%3a8080%2fexternalapi%2fopenbanking%2foauth%2ftoken");
        registerOpenBanking.setQueryParameter("lang", "kor");
        registerOpenBanking.setQueryParameter("client_id", clientID);
        registerOpenBanking.setQueryParameter("state", state);

        return registerOpenBanking.getUrl();
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

    public boolean update(String userName, String userPhone, String userEmail, String userAffiliation) {
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

    public boolean makeProject(String projectName, String workType, String dataType, String subject, String wayContent, String conditionContent, String description, String totalData) {
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
            return false;
        }

        return result.equals("success");
    }

    public boolean createClass(List<String> classList) {
        APICaller classname = new APICaller("POST",baseURL+"/api/project/class");
        classname.setHeader("Authorization",token);

        List<String> id = new ArrayList<>();
        id.add(Project.getProjectinstance().getProjectId());
        classname.setQueryParameter_list("className",classList);
        classname.setQueryParameter_list("projectId",id);
        String result;

        try {
            classname.request_list();
            result = classname.getHeader().get("class").get(0);
            Project.getProjectinstance().setBucketName(classname.getHeader().get("bucketName").get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return result.equals("success");
    }

    public boolean uploadExampleFile(InputStream inputStream, String fileName, String fileType, String worktype) throws Exception {
        APICaller uploadFile = new APICaller("POST", baseURL + "/api/project/upload/example");
        Map<String, List<String>> header;
        uploadFile.setHeader("Authorization",token);
        System.out.println(Project.getProjectinstance().getBucketName());
        uploadFile.setHeader("bucketName",Project.getProjectinstance().getBucketName());
        header = uploadFile.multipart(inputStream, UrlEscapers.urlFragmentEscaper().escape(fileName), fileType);
        String result;
        result = header.get("example").get(0);
        if(result.equals("success")) {
            Project.getProjectinstance().setProjectId(header.get("projectId").get(0));//아이디 받아
            if(worktype.equals("collection")) {
                Project.getProjectinstance().setCost(Integer.parseInt(header.get("cost").get(0)));
            } else {
                Project.getProjectinstance().setBucketName(header.get("bucketName").get(0));
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean uploadLabellingFiles(InputStream[] inputStreams, String[] fileNames, String[] contentTypes) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for(int i = 0; i < inputStreams.length; i++) {
            builder.addBinaryBody("files", inputStreams[i], ContentType.create(contentTypes[i]), UrlEscapers.urlFragmentEscaper().escape(fileNames[i]));
        }
        HttpPost httpPost = new HttpPost(baseURL + "/api/project/upload/labelling");
        httpPost.setHeader("Authorization", token);
        httpPost.setHeader("bucketName", Project.getProjectinstance().getBucketName());
        httpPost.setEntity(builder.build());
        HttpResponse httpResponse = httpClient.execute(httpPost);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            if(httpResponse.getFirstHeader("upload").getValue().equals("success")) {
                Project.getProjectinstance().setCost(Integer.parseInt(httpResponse.getFirstHeader("cost").getValue()));
                return true;
            }
        }
        return false;
    }


    public boolean payment(String method) throws Exception {
        APICaller pointPayment  = new APICaller("GET",baseURL+"/api/project/" + method + "/payment");
        pointPayment.setHeader("Authorization",token);
        pointPayment.setQueryParameter("projectId",Project.getProjectinstance().getProjectId());
        pointPayment.request();
        String result = null;
        result = pointPayment.getHeader().get("payment").get(0);

        if(result.equals("success")) {
            return true;
        } else {
            return false;
        }
    }

    public List<Project> projectList(String workType,String dataType,String subject, String difficulty)throws Exception  {
     APICaller projectList = new APICaller("GET",baseURL+"/api/project/list");
     projectList.setQueryParameter("workType",workType);
     projectList.setQueryParameter("dataType",dataType);
     projectList.setQueryParameter("subject",subject);
     projectList.setQueryParameter("difficulty",difficulty);
     projectList.request();
     String result;
     List<Project> project_list = new ArrayList<>();
     result = projectList.getBody();

     JSONArray jsonArray = new JSONArray(result);

     for(int i=0;i<jsonArray.length();i++){

         Project project = new Project();
         JSONObject jsonObject;
         JSONObject project_object;
         JSONArray class_array;

         jsonObject = jsonArray.getJSONObject(i);//dto랑 classlist담고 있는 object
         project_object = jsonObject.getJSONObject("projectDto");

         project.setProjectId(project_object.getString("projectId"));
         project.setBucketName(project_object.getString("bucketName"));
         project.setStatus(project_object.getString("status"));
         project.setUserId(project_object.getString("userId"));
         project.setProjectName(project_object.getString("projectName"));
         project.setWorkType(project_object.getString("workType"));
         project.setDataType(project_object.getString("dataType"));
         project.setSubject(project_object.getString("subject"));
         project.setDifficulty(project_object.getInt("difficulty"));
         project.setWayContent(project_object.getString("wayContent"));
         project.setConditionContent(project_object.getString("conditionContent"));
         project.setExampleContent(project_object.getString("exampleContent"));
         project.setDescription(project_object.getString("description"));
         project.setTotalData(project_object.getInt("totalData"));
         project.setProgressData(project_object.getInt("progressData"));
         project.setCost(project_object.getInt("cost"));
         project_list.add(project);

         class_array = jsonObject.getJSONArray("classNameList");
         List<String> class_list = new ArrayList<>();
         for(int j=0;j<class_array.length();j++){
             JSONObject classobject = class_array.getJSONObject(j);
             class_list.add(classobject.getString("className"));
         }
         project.setClass_list(class_list);
     }
     return project_list;
    }

    public List<Project> requestProjectList() throws Exception {
        APICaller requestproject = new APICaller("GET",baseURL+"/api/project/all");
        requestproject.setHeader("Authorization",token);
        requestproject.request();

        String list;
        List<Project> project_list = new ArrayList<>();
        list = requestproject.getBody();
        System.out.println("request: "+list);
        JSONArray jsonArray = new JSONArray(list);

        for(int i=0;i<jsonArray.length();i++){

            Project project = new Project();
            JSONObject jsonObject;
            JSONObject project_object;
            JSONArray class_array;

            jsonObject = jsonArray.getJSONObject(i);//dto랑 classlist담고 있는 object
            project_object = jsonObject.getJSONObject("projectDto");

            project.setProjectId(project_object.getString("projectId"));
            project.setBucketName(project_object.getString("bucketName"));
            project.setStatus(project_object.getString("status"));
            project.setUserId(project_object.getString("userId"));
            project.setProjectName(project_object.getString("projectName"));
            project.setWorkType(project_object.getString("workType"));
            project.setDataType(project_object.getString("dataType"));
            project.setSubject(project_object.getString("subject"));
            project.setDifficulty(project_object.getInt("difficulty"));
            project.setWayContent(project_object.getString("wayContent"));
            project.setConditionContent(project_object.getString("conditionContent"));
            project.setExampleContent(project_object.getString("exampleContent"));
            project.setDescription(project_object.getString("description"));
            project.setTotalData(project_object.getInt("totalData"));
            project.setValidationData(project_object.getInt("validatedData"));
            project.setProgressData(project_object.getInt("progressData"));
            project.setCost(project_object.getInt("cost"));
            project_list.add(project);

            class_array = jsonObject.getJSONArray("classNameList");

            List<String> class_list = new ArrayList<>();

            for(int j=0;j<class_array.length();j++){
                JSONObject classobject = class_array.getJSONObject(j);
                class_list.add(classobject.getString("className"));
            }
            project.setClass_list(class_list);
        }
        return project_list;
    }

    public Boolean downloadObject(String bucketName, String obejctName, OutputStream outputStream) {
        String url = "http://kr.object.ncloudstorage.com/" + bucketName + "/" + UrlEscapers.urlFragmentEscaper().escape(obejctName.replace(" ", "%20"));
        String accessKey = "sQG5BeaHcnvvqK4FI01A";
        String secretKey = "mvNVjSac240XvnrK4qF39HpoMvvtMQMzUnnNHaRV";
        String serviceName = "s3";
        String regionName = "kr-standard";
        String UNSIGNED_PAYLOAD = "UNSIGNED_PAYLOAD";
        try {
            AWS4SignerForAuthorizationHeader a = new AWS4SignerForAuthorizationHeader(new URL(url),"GET", serviceName, regionName);
            HttpGet httpget = new HttpGet(url);
            System.out.println(httpget.getURI().getPath());
            Map<String,String> header = new HashMap<>();
            Map<String,String> q = new HashMap<>();
            header.put("x-amz-content-sha256", UNSIGNED_PAYLOAD);
            String auth = a.computeSignature(header, q, UNSIGNED_PAYLOAD, accessKey, secretKey);
            HttpClient httpclient = new DefaultHttpClient();

            // Request Headers and other properties.
            for(String key : header.keySet()) {
                httpget.setHeader(key, header.get(key));
            }

            httpget.setHeader("Authorization", auth);
            HttpResponse response = httpclient.execute(httpget);
            if(response.getStatusLine().getStatusCode() == 200) {
                if(outputStream != null) {
                    response.getEntity().writeTo(outputStream);
                }
                return true;
            } else {
                if(outputStream != null) {
                    response.getEntity().writeTo(System.out);
                }
                return false;
            }
        } catch(Exception ex) {
            System.out.print(ex);
            return false;
        }
    }

    public Boolean collectionWork(List<InputStream> inputStreams, List<String> fileNames, String contentType, String classname) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for(int i = 0; i < inputStreams.size(); i++) {
            System.out.println("filename:"+ fileNames.get(i));
            builder.addBinaryBody("files", inputStreams.get(i), ContentType.create(contentType), UrlEscapers.urlFragmentEscaper().escape(fileNames.get(i)));
        }
        builder.addTextBody("className",classname);
        HttpPost httpPost = new HttpPost(baseURL + "/api/work/collection");
        httpPost.setHeader("Authorization", token);
        httpPost.setHeader("bucketName", Project.getProjectinstance().getBucketName());
        httpPost.setHeader("projectId",Project.getProjectinstance().getProjectId());
        httpPost.setHeader("className",classname);
        httpPost.setEntity(builder.build());
        HttpResponse httpResponse = httpClient.execute(httpPost);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            if(httpResponse.getFirstHeader("upload").getValue().equals("success")) {
                return true;
            }
        }
        else{
            System.out.println("response: "+httpResponse.getStatusLine().getStatusCode());
        }
        return false;
    }

    public void logout() {
        token = null;
    }

    public String rankingPoint() throws Exception {
        APICaller rankingPoint = new APICaller("GET",baseURL+"/api/ranking/point");
        String result;
        String list;
        rankingPoint.request();
        result = rankingPoint.getHeader().get("ranking").get(0);

        if(result.equals("fail")){
            System.out.println("point ranking fail");
        }
        list = rankingPoint.getBody();
        return list;
    }

    public String workHistory() throws Exception {
        APICaller workHistory = new APICaller("GET",baseURL+"/api/work/all");
        workHistory.setHeader("Authorization",token);
        String result;
        String list;
        workHistory.request();
        result = workHistory.getHeader().get("workList").get(0);
        if (result.equals("fail")){
            System.out.println("workhistory fail ");
        }
        list = workHistory.getBody();
        System.out.println("workhistory list:"+list);
        return list;
    }

    public String getClassificationProblems(String dataType) throws Exception { //5개
        APICaller provideClassificationProblems = new APICaller("GET",baseURL+"/api/work/classification/start");
        provideClassificationProblems.setHeader("Authorization",token);
        provideClassificationProblems.setHeader("dataType",dataType);
        String result;
        String list;
        provideClassificationProblems.request();
        result = provideClassificationProblems.getHeader().get("problems").get(0);
        list = provideClassificationProblems.getBody();
        String historyId = provideClassificationProblems.getHeader().get("workHistory").get(0);
        LabellingWorkHistory.getInstance().setHistoryId(Integer.parseInt(historyId));

        if(result.equals("fail")){
            System.out.println("classification problem fail");
            return null;
        }
        return list;
    }

    public Boolean classificationWork(List<StringBuffer> answerList, List<String> problemIdList) throws Exception {
        Map<String,String> headers = new HashMap<>();
        HttpURLConnection con;

        headers.put("Content-Type","application/json");
        headers.put("Authorization",token);
        headers.put("historyId",Integer.toString(LabellingWorkHistory.getInstance().getHistoryId()));

        JSONObject jsonBody = new JSONObject();
        for(int i=0;i<problemIdList.size();i++){
           jsonBody.put(problemIdList.get(i),answerList.get(i).toString());
        }
        con = (HttpURLConnection) new URL(baseURL+"/api/work/classification").openConnection();
        con.setRequestMethod("POST");

        if(!headers.isEmpty()) {
            for(String key : headers.keySet()) {
                con.setRequestProperty(key, headers.get(key));
            }
        }

        if(jsonBody.length()!=0) {
            con.setDoOutput(true);
            con.getOutputStream().write(jsonBody.toString().getBytes());
        }

        if(con.getResponseCode()==200){
            return true;
        }
        return false;
    }

    public String getBoundingBoxProblems() throws Exception { //5개
        APICaller getBoundingBoxProblems = new APICaller("GET",baseURL+"/api/work/boundingbox/start");
        getBoundingBoxProblems.setHeader("Authorization",token);
        getBoundingBoxProblems.setHeader("projectId",Project.getProjectinstance().getProjectId());
        String result;
        String list;
        getBoundingBoxProblems.request();
        result = getBoundingBoxProblems.getHeader().get("problems").get(0);
        list = getBoundingBoxProblems.getBody();
        String historyId = getBoundingBoxProblems.getHeader().get("workHistory").get(0);
        LabellingWorkHistory.getInstance().setHistoryId(Integer.parseInt(historyId));

        if(result.equals("fail")){
            System.out.println("boundingBox problem fail");
            return null;
        }
        return list;
    }

    public Boolean boundingBoxWork(List<StringBuffer> coordinateList, String problemId, List<String> className) throws Exception {
        Map<String,String> headers = new HashMap<>();
        HttpURLConnection con;

        headers.put("Content-Type","application/json");
        headers.put("Authorization",token);
        headers.put("historyId",Integer.toString(LabellingWorkHistory.getInstance().getHistoryId()));
        headers.put("projectId",Project.getProjectinstance().getProjectId());

        JSONObject jsonBody = new JSONObject();
        for(int i=0;i<coordinateList.size();i++) {
            jsonBody.put(className.get(i), coordinateList.get(i).toString());
            System.out.println("파라미터에 넣어지는 클래스이름: "+className.get(i));
            System.out.println("파라미터에 넣어지는 박스좌표:"+coordinateList.get(i).toString());
        }

        String url = baseURL+"/api/work/boundingbox";

        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("problemId",problemId);
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
        con.setRequestMethod("POST");

        if(!headers.isEmpty()) {
            for(String key : headers.keySet()) {
                con.setRequestProperty(key, headers.get(key));
            }
        }

        if(jsonBody.length()!=0) {
            con.setDoOutput(true);
            con.getOutputStream().write(jsonBody.toString().getBytes());
        }

        if(con.getResponseCode()==200){
            return true;
        }
        return false;
    }

    public JSONObject validatedProblemList() throws Exception {
        APICaller validatedProblemList = new APICaller("GET",baseURL+"/api/project/crossvalidation");
        validatedProblemList.setHeader("Authorization",token);
        validatedProblemList.setQueryParameter("projectId",Project.getProjectinstance().getProjectId());
        validatedProblemList.request();
        JSONObject list = new JSONObject(validatedProblemList.getBody());

        return list;
    }

    public String terminateProject() throws Exception {
        APICaller terminateProject = new APICaller("GET",baseURL+"/api/project/terminate");
        terminateProject.setHeader("Authorization",token);
        terminateProject.setQueryParameter("projectId",Project.getProjectinstance().getProjectId());
        terminateProject.request();
        String finalCost;
        String result;
        finalCost = terminateProject.getHeader().get("finalCost").get(0);
        result = terminateProject.getHeader().get("project").get(0);
        if(result.equals("fail")){
            System.out.println("terminate project fail");
        }
        return finalCost;
    }

    public Boolean terminatePoint() throws Exception {
        APICaller terminatePoint = new APICaller("GET",baseURL+"/api/project/terminate/point");
        terminatePoint.setHeader("Authorization",token);
        terminatePoint.setQueryParameter("projectId",Project.getProjectinstance().getProjectId());
        terminatePoint.request();
        String result;
        result = terminatePoint.getHeader().get("project").get(0);
        if(result.equals("fail")){
            System.out.println("terminate project fail");
            return false;
        }
        return true;
    }

    public Boolean terminateAccount() throws Exception {
        APICaller terminateAccount = new APICaller("GET",baseURL+"/api/project/terminate/account");
        terminateAccount.setHeader("Authorization",token);
        terminateAccount.setQueryParameter("projectId",Project.getProjectinstance().getProjectId());
        terminateAccount.request();
        String result;
        result = terminateAccount.getHeader().get("payment").get(0);
        if(result.equals("fail")){
            System.out.println("account payment fail");
            return false;
        }
        return true;
    }

}
