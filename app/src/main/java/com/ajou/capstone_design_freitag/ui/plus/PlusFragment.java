package com.ajou.capstone_design_freitag.ui.plus;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ajou.capstone_design_freitag.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PlusFragment extends Fragment implements View.OnClickListener{
    private static final int EXAMPLE_PICTURE_IMAGE_REQUEST_CODE = 100;
    private static final int LABELING_PICTURE_IMAGE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;

    private ArrayList<Uri> examplePictures;
    private ArrayList<Uri> labelingPictures;

     ProjectMakeFragment projectMakeFragment = new ProjectMakeFragment();
    PayFragment payFragment = new PayFragment();
  
   FragmentTransaction fragmentTransaction;

    Button make_button;
    Button example_data_button;
    Button labelling_data_button;

    TextView examplePicturesURI;
    private Context context;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
       View view = inflater.inflate(R.layout.fragment_plus,container,false);
        replaceFragment(0);
       return view;
    }


    public void replaceFragment(int index) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (index == 0) {
            if (!projectMakeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_plus, projectMakeFragment);
                fragmentTransaction.commit();
                }
        } else if (index == 1) {
            if (!payFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_plus, payFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

    public void upload_example_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_PICTURE_IMAGE_REQUEST_CODE);
    }

    public void upload_labelling_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_PICTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EXAMPLE_PICTURE_IMAGE_REQUEST_CODE || requestCode == LABELING_PICTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (requestCode == EXAMPLE_PICTURE_IMAGE_REQUEST_CODE) {
                        examplePictures.add(clipData.getItemAt(i).getUri());
                        examplePicturesURI.setText(examplePicturesURI.getText() + "\n" + clipData.getItemAt(i).getUri());
                        try {
                            AsyncTask<Uri, Void, Boolean> loginTask = new AsyncTask<Uri, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Uri... uris) {
                                    try {
                                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                                        boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream);
                                        return new Boolean(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return new Boolean(false);
                                    }
                                }
                            };
                            loginTask.execute(clipData.getItemAt(i).getUri());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (requestCode == LABELING_PICTURE_IMAGE_REQUEST_CODE) {
                        labelingPictures.add(clipData.getItemAt(i).getUri());
                    }
                }

}
