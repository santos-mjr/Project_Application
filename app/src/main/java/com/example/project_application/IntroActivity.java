package com.example.project_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class IntroActivity extends AppCompatActivity {

    ViewPager slideViewPager;
    LinearLayout dotLayout;

    TextView[] mDots;

    IntroAdapter sliderAdapter;

    Button nextBtn, backBtn, skipBtn;

    int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        slideViewPager = findViewById(R.id.slideViewPager);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        skipBtn = (Button) findViewById(R.id.skipBtn);

        sliderAdapter = new IntroAdapter(this);

        slideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        slideViewPager.addOnPageChangeListener(viewListener);

        //Onclick Listeners for Next, Back and Skip

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPage + 1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPage - 1);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, Login.class));
            }
        });


    }

    public void addDotsIndicator(int position) {

        mDots = new TextView[3];
        dotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setLetterSpacing(0.2f);
            mDots[i].setTextColor(getResources().getColor(R.color.blue_gray_1));

            dotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.blue_gray_2));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            currentPage = i;

            if(i == 0){
                //First page
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("");
            }else if(i == mDots.length - 1){
                //Last Page
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Finish");
                backBtn.setText("Back");
            }else{
                //Not first or last page
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}