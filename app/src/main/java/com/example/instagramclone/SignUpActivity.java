package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private Button btnSignup, btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Setting title to the action bar
        setTitle("Sign Up");

        etEmail =  (EditText) findViewById(R.id.etEmailId);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    clickSignup(btnSignup);
                    return true;
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser()!=null){
            //ParseUser.logOutInBackground();
            transitionToSocialMediaActivity();
        }
    }

    public void clickSignup(View view){
        String myEmail = etEmail.getText().toString();
        String myUsername = etUsername.getText().toString();
        String myPassword = etPassword.getText().toString();

        if(myEmail.isEmpty() || myUsername.isEmpty() || myPassword.isEmpty()){
            String message = "Email Id, Username and Password is required!";
            FancyToast.makeText(SignUpActivity.this,
                    message,
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false).show();
        }else{
            ParseUser user = new ParseUser();
            user.setEmail(myEmail);
            user.setUsername(myUsername);
            user.setPassword(myPassword);

            //Using progress dialog to show the user that something is going on
            final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("Signining up " + myUsername);
            progressDialog.show();

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        FancyToast.makeText(SignUpActivity.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }else{
                        progressDialog.dismiss();
                        FancyToast.makeText(SignUpActivity.this,"Signed in Succesfull!",
                                FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                                false).show();
                        transitionToSocialMediaActivity();
                    }
                }
            });
        }
    }

    public void clickLogin(View view){
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void clickRoot(View view){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void transitionToSocialMediaActivity(){
        Intent intent = new Intent(SignUpActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}
