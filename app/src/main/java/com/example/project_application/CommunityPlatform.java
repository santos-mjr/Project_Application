package com.example.project_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CommunityPlatform extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_platform);

        //Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(CommunityPlatform.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:
                        Intent intent_two = new Intent(CommunityPlatform.this, CommunityPlatform.class);
                        intent_two.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_two);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        Intent intent_three = new Intent(CommunityPlatform.this, ProfileActivity.class);
                        intent_three.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_three);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}