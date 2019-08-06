package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPostActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_post);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        String username = receivedIntentObject.getStringExtra("username");

        setTitle(username + "'s posts!");

        final ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("photo");
        parseQuery.whereEqualTo("username",username);
        parseQuery.orderByDescending("createdAt");

//        final ProgressDialog progressDialog = new ProgressDialog(UsersPostActivity.this);
//        progressDialog.setMessage("Loading ....");
//        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    FancyToast.makeText(getApplicationContext(),e.getMessage(),
                            FancyToast.LENGTH_LONG,FancyToast.ERROR,
                            false).show();

                }else if(objects.size() > 0){

                    for(ParseObject parseObject : objects){

                        final TextView captionText = new TextView(getApplicationContext());
                        captionText.setText(parseObject.get("caption") + "");

                        final ParseFile postImage = (ParseFile) parseObject.get("picture");

                        postImage.getDataInBackground(new GetDataCallback() {

                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e!=null){

                                    FancyToast.makeText(getApplicationContext(),e.getMessage(),
                                            FancyToast.LENGTH_LONG,FancyToast.ERROR,
                                            false).show();

                                }else if(data != null){

                                    Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(getApplicationContext());

                                    LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);

                                    imageView_params.setMargins(10,10,10,10);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(image);

                                    LinearLayout.LayoutParams caption_params = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );

                                    caption_params.setMargins(10,10,10,10);
                                    captionText.setLayoutParams(caption_params);
                                    captionText.setGravity(Gravity.CENTER);
                                    captionText.setTextSize(30f);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(captionText);
                                }
                            }
                        });

                    }

                }else if(objects.size() == 0){

                    FancyToast.makeText(getApplicationContext(),"No posts to show yet!" ,
                            FancyToast.LENGTH_LONG,FancyToast.INFO,
                            false).show();

                    finish();

                }
            }
        });
       // progressDialog.dismiss();
    }
}
