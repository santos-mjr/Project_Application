package com.example.project_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class CompleteFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.completing_dialog,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button okBtn = view.findViewById(R.id.btnDialog);
        TextView scoreTxt = view.findViewById(R.id.score_text);
        int score = CompleteFragmentArgs.fromBundle(getArguments()).getScore();
        scoreTxt.setText("Your Score = " + score +"/15");
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CompleteFragment.this).navigate(CompleteFragmentDirections.actionCompleteFragmentToHomeFragment());

            }
        });

    }
}
