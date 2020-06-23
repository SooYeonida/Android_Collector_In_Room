package com.ajou.capstone_design_freitag.UI.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.ClassDto;
import com.ajou.capstone_design_freitag.UI.dto.Problem;
import com.ajou.capstone_design_freitag.UI.dto.ProblemWithClass;
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.ajou.capstone_design_freitag.Work.BoundingBoxActivity;
import com.ajou.capstone_design_freitag.Work.BoundingBoxPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckValidatedDataActivity extends AppCompatActivity {

    Project project;
    static List<Problem> problemList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_validated_data);
        Intent intent = getIntent();
        project = intent.getParcelableExtra("project");
        Project.getProjectinstance().setProjectId(project.getProjectId());
        getValidationProblem();
    }

    private void getValidationProblem() {
        GetValidationProblemTask getValidationProblemTask = new GetValidationProblemTask();
        getValidationProblemTask.execute();
    }

    static class GetValidationProblemTask extends AsyncTask<Void,Void,JSONObject>{
        JSONObject result;
        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                result = RESTAPI.getInstance().validatedProblemList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
          JSONArray problemArray;
            try {
                problemArray = result.getJSONArray("problems");
                for(int i=0;i<problemArray.length();i++){
                    Problem problem = new Problem();
                    JSONObject probleminfo = problemArray.getJSONObject(i);
                    problem.setObjectName(probleminfo.getString("objectName"));
                    System.out.println("objectname:"+problem.getObjectName());
                    problemList.add(problem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
