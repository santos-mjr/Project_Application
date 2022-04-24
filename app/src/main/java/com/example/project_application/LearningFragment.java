package com.example.project_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LearningFragment extends Fragment {
 private int count =0;
 private LinearProgressIndicator indicator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learning,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView detailsTxt = view.findViewById(R.id.details_txt);
        TextView titleTxt = view.findViewById(R.id.title_learning);
        ImageButton nextBtn = view.findViewById(R.id.next_topic_btn);
        indicator = view.findViewById(R.id.progress_learn);

        String [] titles = new String[]{getString(R.string.why_java_title),getString(R.string.java_syntax_title)
                ,getString(R.string.java_variables_title),getString(R.string.java_Data_title),getString(R.string.java_if_title),
                getString(R.string.java_loop_title),getString(R.string.java_arrays_title)};
        String [] details = new String[]{getString(R.string.why_java),getString(R.string.java_syntax)
                ,getString(R.string.java_variables),getString(R.string.java_Data_type),getString(R.string.java_if),
                getString(R.string.java_loop),getString(R.string.java_arrays)};

        titleTxt.setText(titles[count]);
        detailsTxt.setText(details[count]);

        nextBtn.setOnClickListener(view1 -> {
            count++;
            indicator.setProgress(count*10+10,true);
            if(count<titles.length){
                titleTxt.setText(titles[count]);
                detailsTxt.setText(details[count]);
            }else {
                nextBtn.setVisibility(View.GONE);
            }
        });
    }
}
