package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setting title to the action bar
        setTitle("Log In");

        etEmail = (EditText) findViewById(R.id.etEmailId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Loging in the user using the enter key

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    clickLogin(btnLogin);
                }
                return false;
            }
        });
        if(ParseUser.getCurrentUser()!=null){
            ParseUser.logOutInBackground();
        }
    }

    public void clickLogin(View view){
        String myEmail = etEmail.getText().toString();
        String myPassword = etPassword.getText().toString();
        if(myEmail.isEmpty() || myPassword.isEmpty()){
            String message = "Email and Password is required!";
            FancyToast.makeText(LoginActivity.this,message,
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false).show();
        }else{
            //Showing progress dialog to show user that something is going on
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loging In ...");
            progressDialog.show();

            ParseUser.logInInBackground(myEmail, myPassword, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e!=null){
                        FancyToast.makeText(LoginActivity.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }else{
                        progressDialog.dismiss();
                        FancyToast.makeText(LoginActivity.this,"Logged in Succesfull!",
                                FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                                false).show();
                        transitionToSocialMediaActivity();
                    }
                }
            });
        }
    }

    public void clickSignup(View view){
        finish();
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
        Intent intent = new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(intent);
    }
}
