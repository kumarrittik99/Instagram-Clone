package com.example.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private EditText etName, etBio, etProfession, etHobbies, etFavSport;
    private Button btnUpdate;
    private ImageView ivProfilePic;
    private ParseUser user;
    private ProgressDialog progressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        etName = (EditText) view.findViewById(R.id.etName);
        etProfession = (EditText) view.findViewById(R.id.etProfession);
        etBio = (EditText) view.findViewById(R.id.etBio);
        etHobbies = (EditText) view.findViewById(R.id.etHobbies);
        etFavSport = (EditText) view.findViewById(R.id.etFavSport);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);

        //Initialising progress dialouge
        progressDialog = new ProgressDialog(getContext());

        //Initializing parse user
        user = ParseUser.getCurrentUser();
        //Fetching the data from parse server and setting up the profile
        String name = user.get("profileName") + "";
        String profession = user.get("profileProfession") + "";
        String bio = user.get("profileBio") + "";
        String hobbies = user.get("profileHobbies") + "";
        String favSport = user.get("profileFavSport") + "";
        if(name == "null"){
            name = "";
        }else if(profession == "null"){
            profession = "";
        } else if(bio == "null"){
            bio = "";
        }else if(hobbies == "null"){
            hobbies = "";
        }else if(favSport == "null"){
            favSport = "";
        }

        etName.setText(name);
        etProfession.setText(profession);
        etBio.setText(bio);
        etHobbies.setText(hobbies);
        etFavSport.setText(favSport);

//        user.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e!=null){
//                    FancyToast.makeText(getContext(), e.getMessage(),
//                            FancyToast.LENGTH_LONG, FancyToast.ERROR,
//                            false).show();
//                }
//            }
//        });
        //Fetching data from parse server and setting up of current profile done!

        btnUpdate.setOnClickListener(this);

        return view;
    }


    //Clicking of buttons
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnUpdate:
                String name = etName.getText().toString();
                String profession = etProfession.getText().toString();
                String bio = etBio.getText().toString();
                String hobbies = etHobbies.getText().toString();
                String favSport = etFavSport.getText().toString();

                if(name.isEmpty() || profession.isEmpty()){
                    String message = "Name or profession can not be empty!";
                    FancyToast.makeText(getContext(),message,FancyToast.LENGTH_LONG,
                            FancyToast.WARNING,false).show();
                }else {
                    progressDialog.setMessage("Updating profile .... Please wait!");
                    progressDialog.show();
                    user.put("profileName", name);
                    user.put("profileProfession", profession);
                    user.put("profileBio", bio);
                    user.put("profileHobbies", hobbies);
                    user.put("profileFavSport", favSport);

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                FancyToast.makeText(getContext(), e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR,
                                        false).show();
                            } else {
                                FancyToast.makeText(getContext(), "Profile updated succesfully!",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                        false).show();
                            }

                            progressDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

//    public void clickRoot(View view){
//        try{
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
