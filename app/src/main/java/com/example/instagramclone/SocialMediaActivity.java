package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.security.Permission;

public class SocialMediaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        getSupportActionBar().setTitle("Social Media");

        tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.vpViewPager);
        viewPager.setAdapter(tabAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tlLayout);
        tabLayout.setupWithViewPager(viewPager,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getOrder()){
            case 1:
                if(Build.VERSION.SDK_INT > 23 &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            3000);
                }else{
                    captureImage();
                }
                break;
            case 2:
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging out ...");
                progressDialog.show();

                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if(e!=null){
                            FancyToast.makeText(getApplicationContext(),e.getMessage(),
                                    FancyToast.LENGTH_LONG,FancyToast.ERROR,
                                    false).show();
                        }else{
                            FancyToast.makeText(getApplicationContext(),"Logged Out succesfully!",
                                    FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                                    false).show();
                        }
                    }
                });
                finish();
                Intent intent = new Intent(SocialMediaActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 3000){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }

    public void captureImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 4000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4000 && resultCode == Activity.RESULT_OK){
            try{
                Log.e("Error34 "," entered here");
                //Selecting the image from the gallery
                Uri selectedImage = data.getData();
                //Storing the image as Bitmap
                Bitmap myImage = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), selectedImage);

                //Initialising ByteArrayOutputStream as we need to send big files in terms of byte to the server
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //Compressing the bitmap image
                myImage.compress(Bitmap.CompressFormat.PNG, 100,
                        byteArrayOutputStream);
                byte[] bytes =  byteArrayOutputStream.toByteArray();

                Log.e("Error34 "," Converted into bytes");

                ParseFile  parseFile = new ParseFile("image.png",bytes);
                ParseObject parseObject = new ParseObject("photo");
                parseObject.put("picture",parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                Log.e("Error34 "," Putting values");

                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setMessage("Laoding ....");
                progressDialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if(e!=null){
                            FancyToast.makeText(getApplicationContext(),e.getMessage(),
                                    FancyToast.LENGTH_LONG,FancyToast.ERROR,
                                    false).show();
                        }else{
                            FancyToast.makeText(getApplicationContext(),"Image uploaded succesfully!",
                                    FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                                    false).show();
                        }
                    }
                });

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
