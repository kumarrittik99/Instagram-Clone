package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

public class SocialMediaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private Toolbar  toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media");

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        toolbar = (Toolbar) findViewById(R.id.tbSocialMedia);

        viewPager = (ViewPager) findViewById(R.id.vpViewPager);
        viewPager.setAdapter(tabAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tlLayout);
        tabLayout.setupWithViewPager(viewPager,false);
    }
}
