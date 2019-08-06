package com.example.instagramclone;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.security.Permission;

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
    private Bitmap myProfilePic;

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

        if(!name.equals("null")){
            etName.setText(name);
        }
        if(!profession.equals("null")){
            etProfession.setText(profession);
        }
        if(!bio.equals("null")){
            etBio.setText(bio);
        }
        if(!hobbies.equals("null")){
            etHobbies.setText(hobbies);
        }
        if(!favSport.equals("null")){
            etFavSport.setText(favSport);
        }

        //Downloading the images from the parse server and settting it up in the profile picture image view
        ParseFile pictureParseFile  = (ParseFile) ParseUser.getCurrentUser().get("profilePic");
        try{
            pictureParseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if(e!=null){

                        FancyToast.makeText(getContext(),e.getMessage(),
                                FancyToast.LENGTH_LONG,FancyToast.ERROR,
                                false).show();

                    }else if(data != null){
                        myProfilePic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivProfilePic.setImageBitmap(myProfilePic);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        btnUpdate.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);

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

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    myProfilePic.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    ParseFile parseFile = new ParseFile("profile.png", bytes);
                    user.put("profilePic",parseFile);
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
            case R.id.ivProfilePic:
                if(Build.VERSION.SDK_INT > 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5000);
                }else{
                    changeProfilePic();
                }
                break;
        }
    }

    private void changeProfilePic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 6000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 6000 && resultCode == getActivity().RESULT_OK){
            try{
                Uri selectedImage = data.getData();
                myProfilePic = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                ivProfilePic.setImageBitmap(myProfilePic);
            }catch (Exception e){
                FancyToast.makeText(getContext(), e.getMessage(),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR,
                            false).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 5000){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                changeProfilePic();
            }
        }
    }
}
