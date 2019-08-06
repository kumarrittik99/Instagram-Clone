package com.example.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.security.Permission;


/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment implements View.OnClickListener {


    private ImageView ivUploadPhoto;
    private EditText etAddCaption;
    private Button btnUploadPhoto;
    private Bitmap receiviedImageBitmap;

    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_picture, container, false);

        ivUploadPhoto = (ImageView) view.findViewById(R.id.ivUploadPhoto);
        etAddCaption = (EditText) view.findViewById(R.id.etAddCaption);
        btnUploadPhoto = (Button) view.findViewById(R.id.btnUploadPhoto);

        ivUploadPhoto.setOnClickListener(PictureFragment.this);
        btnUploadPhoto.setOnClickListener(PictureFragment.this);


//        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
//        parseQuery..getParseObject()


        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivUploadPhoto:

                if(Build.VERSION.SDK_INT > 23 &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else{

                    getChoosenImage();

                }

                break;
            case R.id.btnUploadPhoto:
                if(receiviedImageBitmap!=null){
                    String caption = etAddCaption.getText().toString();
                    if(caption.isEmpty()){
                        FancyToast.makeText(getContext(),"Please add a caption!",
                                FancyToast.LENGTH_SHORT,FancyToast.ERROR,
                                false).show();
                    }else{
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receiviedImageBitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("image.png",bytes);
                        ParseObject parseObject = new ParseObject("photo");
                        parseObject.put("picture",parseFile);
                        parseObject.put("caption",caption);
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Loading ...");
                        progressDialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    FancyToast.makeText(getContext(),e.getMessage(),
                                            FancyToast.LENGTH_SHORT,FancyToast.ERROR,
                                            false).show();
                                }else{
                                    FancyToast.makeText(getContext(),"Image uploaded succesfully!",
                                            FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,
                                            false).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }

                }else{
                    FancyToast.makeText(getContext(),"Please Select a image!",
                            FancyToast.LENGTH_SHORT,FancyToast.ERROR,
                            false).show();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getChoosenImage();
            }
        }
    }

    public void getChoosenImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000){
            if(resultCode == getActivity().RESULT_OK){

                try{
                    Uri selectedImage = data.getData();
                    receiviedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    ivUploadPhoto.setImageBitmap(receiviedImageBitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}
