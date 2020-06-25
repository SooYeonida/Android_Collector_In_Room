package com.ajou.capstone_design_freitag.Work;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.ProblemWithClass;
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoundingBoxPagerAdapter  extends androidx.viewpager.widget.PagerAdapter  {

    private BoundingBoxActivity boundingBoxActivity;
    private static Context mContext;
    private List<ProblemWithClass> problemList;

    static TextView projectName;
    static TextView wayContent;
    static TextView conditionContent;
    static TextView requester;
    static TextView classListView;
    static ImageView exampleContent;
    static ImageView boundingImage;

    Project project;
    List<Bitmap> bitmaps;
    Map<Integer,Uri> uriListMap;
    String label = null;
    static int currentPosition;

    private OnRadioCheckedChanged mOnRadioCheckedChanged;
    private RegisterListener mregisterListener;

    public BoundingBoxPagerAdapter(BoundingBoxActivity boundingBoxActivity, List<ProblemWithClass> problemWithClassList,List<Bitmap> bitmapList,
                                   Project projectInfo,Map<Integer,Uri> uriMap, OnRadioCheckedChanged onRadioCheckedChanged, RegisterListener registerListener){
        this.boundingBoxActivity = boundingBoxActivity;
        mContext = boundingBoxActivity.getApplicationContext();
        project = projectInfo;
        bitmaps = bitmapList;
        uriListMap = uriMap;
        this.mOnRadioCheckedChanged = onRadioCheckedChanged;
        this.mregisterListener = registerListener;
        if(problemWithClassList==null){
            problemList = new ArrayList<>();
        }
        else{
            problemList = problemWithClassList;
        }
    }

    //activity로 데이터 주기 위한 interface들 구현
    public interface OnRadioCheckedChanged {
        void onRadioCheckedChanged(String label,int problemId);
    }
    public interface RegisterListener {
        void clickBtn();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (position==0){ //문제 시작 전 설명 화면
            View view;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_start_boundingbox_problem, container, false);

            projectName = view.findViewById(R.id.boundingbox_project_name);
            wayContent = view.findViewById(R.id.boundingbox_way_content);
            conditionContent = view.findViewById(R.id.boundingbox_condition_content);
            requester = view.findViewById(R.id.boundingbox_requester);
            classListView = view.findViewById(R.id.boundingbox_classlist);
            exampleContent = view.findViewById(R.id.boundingbox_example_content);
            projectName.setText(project.getProjectName());
            wayContent.setText(project.getWayContent());
            conditionContent.setText(project.getConditionContent());
            requester.setText(project.getUserId());
            classListView.setText(project.getClass_list().toString());

            exampleContent.setImageBitmap(bitmaps.get(position));

            container.addView(view);
            return view;
        }
        else {
            View view = null;
            if (mContext != null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_boundingbox_problem_set, container, false);

                TextView work_num = view.findViewById(R.id.boundingbox_work_num);
                work_num.setText(Integer.toString(position));
                TextView problem_num = view.findViewById(R.id.boundingbox_problem_num);
                problem_num.setText(Integer.toString(problemList.get(position-1).getProblem().getProblemId()));
                boundingImage = view.findViewById(R.id.boundingbox_image);

                RadioGroup classList = view.findViewById(R.id.boundingbox_radio_group);
                for (int i = 0; i < problemList.get(position-1).getClassNameList().size(); i++) {
                    final RadioButton radioButton = new RadioButton(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    radioButton.setLayoutParams(param);
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentPosition = position;
                            label = radioButton.getText().toString();
                        }
                    });

                    radioButton.setText(problemList.get(position-1).getClassNameList().get(i).getClassName());
                    radioButton.setId(i);
                    classList.addView(radioButton);
                }

                //bitmap = Bitmap.createScaledBitmap(bitmap, 800, 600, true);
                boundingImage.setImageBitmap(bitmaps.get(position));
            }
            container.addView(view);

            ImageView boundingBoxStart = view.findViewById(R.id.boundingbox_start);
            boundingBoxStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRadioCheckedChanged.onRadioCheckedChanged(label,problemList.get(position-1).getProblem().getProblemId());
                    CropImage.activity(uriListMap.get(position))
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Bounding Box")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setCropMenuCropButtonTitle("Done")
                            .setCropMenuCropButtonIcon(R.drawable.boundingbox)
                            .setAsBoundingBoxSelector()
                            .setOriginalSize(bitmaps.get(position).getWidth(), bitmaps.get(position).getHeight())
                            .start(boundingBoxActivity);
                }
            });

            Button done = view.findViewById(R.id.boundingbox_done);
            done.setVisibility(View.GONE);
            if (position ==5){
                done.setVisibility(View.VISIBLE);
            }

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mregisterListener.clickBtn();
                    boundingBoxActivity.finish();
                }
            });
            return view;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

}