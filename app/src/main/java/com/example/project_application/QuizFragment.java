package com.example.project_application;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class QuizFragment extends Fragment {
  private   DatabaseReference questionsRef;
  private List<Question> questionList;
  private int count =0;
  private int score =0;
  private TextView questionTxt,option_1_txt,option_2_txt,option_3_txt,option_4_txt;
  private MaterialButton submit;
  private String selected ="";
  private LinearProgressIndicator indicator;
  private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView basicTxt = view.findViewById(R.id.basic_txt);
        TextView intermediateTxt = view.findViewById(R.id.intermediate_txt);
        TextView advancedTxt = view.findViewById(R.id.advanced_txt);
        progressBar = view.findViewById(R.id.quiz_progress);
         questionTxt = view.findViewById(R.id.question_txt);
         option_1_txt = view.findViewById(R.id.option1_txt);
         option_2_txt = view.findViewById(R.id.option2_txt);
         option_3_txt = view.findViewById(R.id.option3_txt);
         option_4_txt = view.findViewById(R.id.option4_txt);
         submit = view.findViewById(R.id.submit_btn);
        ImageButton nextBtn = view.findViewById(R.id.next_btn);
        indicator = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        questionList = new ArrayList<>();
        getQuestionsList("Basics");
        basicTxt.setOnClickListener(view1 -> {
            count =0;
            score =0;
            questionList = new ArrayList<>();
            indicator.setProgress(count*10,true);
            getQuestionsList("Basics");
            clearButtons();
            basicTxt.setBackgroundResource(R.drawable.levels_background_selected);
            intermediateTxt.setBackgroundResource(R.drawable.levels_background);
            advancedTxt.setBackgroundResource(R.drawable.levels_background);
        });
        intermediateTxt.setOnClickListener(view1 -> {
            count = 0;
            score =0;
            questionList = new ArrayList<>();
            indicator.setProgress(count*10,true);
            clearButtons();
            getQuestionsList("Intermediate");
            intermediateTxt.setBackgroundResource(R.drawable.levels_background_selected);
            basicTxt.setBackgroundResource(R.drawable.levels_background);
            advancedTxt.setBackgroundResource(R.drawable.levels_background);
        });
        advancedTxt.setOnClickListener(view1 -> {
            count = 0;
            score =0;
            questionList = new ArrayList<>();
            indicator.setProgress(count*10,true);
            clearButtons();
            getQuestionsList("Advanced");
            advancedTxt.setBackgroundResource(R.drawable.levels_background_selected);
            intermediateTxt.setBackgroundResource(R.drawable.levels_background);
            basicTxt.setBackgroundResource(R.drawable.levels_background);
        });

        nextBtn.setOnClickListener(view1 -> {
            count++;
            indicator.setProgress(count*10+10,true);
            clearButtons();
            if(count < questionList.size()){
                if(count == questionList.size()-1){
                    nextBtn.setVisibility(View.GONE);

                }
                populateData();

            }

        });
        option_1_txt.setOnClickListener(view1 -> {
            selected = "option_1";
        });
        option_2_txt.setOnClickListener(view1 -> {
            selected = "option_2";
        });
        option_3_txt.setOnClickListener(view1 -> {
            selected = "option_3";
        });
        option_4_txt.setOnClickListener(view1 -> {
            selected = "option_4";
        });

        submit.setOnClickListener(view1 -> {
            submit.setEnabled(false);
            showCorrectAnswer();
            if(questionList.size()-1==count){
              Navigation.findNavController(view).navigate(QuizFragmentDirections.actionQuizFragmentToCompleteFragment(score));
            }

        });

    }

    private void clearButtons() {
        submit.setEnabled(true);
        option_1_txt.setBackgroundResource(R.drawable.options_background);
        option_2_txt.setBackgroundResource(R.drawable.options_background);
        option_3_txt.setBackgroundResource(R.drawable.options_background);
        option_4_txt.setBackgroundResource(R.drawable.options_background);
    }

    private void showCorrectAnswer() {
        String answer = questionList.get(count).getAnswer();
        Log.d("TAG","selected ="+ selected);
        Log.d("TAG","answer ="+ answer);
        if(selected.equals(answer)){
            score++;
            switch (selected){
                case "option_1":
                option_1_txt.setBackgroundResource(R.drawable.correct_background);
                break;
                case "option_2":
                    option_2_txt.setBackgroundResource(R.drawable.correct_background);
                    break;
                case "option_3":
                    option_3_txt.setBackgroundResource(R.drawable.correct_background);
                    break;
                case "option_4":
                    option_4_txt.setBackgroundResource(R.drawable.correct_background);
                    break;
            }
        }else {
            switch (selected){
                case "option_1":
                    option_1_txt.setBackgroundResource(R.drawable.wrong_background);
                    break;
                case "option_2":
                    option_2_txt.setBackgroundResource(R.drawable.wrong_background);
                    break;
                case "option_3":
                    option_3_txt.setBackgroundResource(R.drawable.wrong_background);
                    break;
                case "option_4":
                    option_4_txt.setBackgroundResource(R.drawable.wrong_background);
                    break;
            }
            if(answer.equals("option_1")){
                option_1_txt.setBackgroundResource(R.drawable.correct_background);

            }else if(answer.equals("option_2")){
                option_2_txt.setBackgroundResource(R.drawable.correct_background);
            }else if (answer.equals("option_3")){
                option_3_txt.setBackgroundResource(R.drawable.correct_background);
            }else {
                option_4_txt.setBackgroundResource(R.drawable.correct_background);
            }
        }
    }

    private void getQuestionsList(String node){
        questionsRef = FirebaseDatabase.getInstance().getReference().child(node);
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                 Question question = dataSnapshot.getValue(Question.class);
                 questionList.add(question);
                 Log.d("TAG","size = "+ questionList.size());
             }
             populateData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

 private void populateData(){
     progressBar.setVisibility(View.GONE);
        if(questionList.size()>0){
         Question question = questionList.get(count);
         questionTxt.setText(question.getQuestion());
         option_1_txt.setText(question.getOption1());
         option_2_txt.setText(question.getOption2());
         option_3_txt.setText(question.getOption3());
         option_4_txt.setText(question.getOption4());

        }
 }
   /* private void showAlertDialog(int layout){
     AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        TextView scoreTxt = layoutView.findViewById(R.id.score_text);
        scoreTxt.setText("Your score is " + score +"/15");
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_Dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = QuizFragmentDirections.actionQuizFragmentToHomeFragment();


            }
        });
    }*/
}
