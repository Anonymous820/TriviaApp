package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.Model.Question;
import com.example.triviaapp.data.AnswerLiseAsyncResponse;
import com.example.triviaapp.data.QuestionBank;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String MESSAGE_KEY = "kEY";
    TextView questv,countertv,cScore,hScore;
    Button truebtn,falsebtn,sharebtn;
    ImageButton preiv,nxtiv;
    CardView cardView;
        int currScore=0, highScore=0;
    public int counter=0;

    private List<Question> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        questv=findViewById(R.id.questv);
        countertv=findViewById(R.id.countertv);
        truebtn=findViewById(R.id.truebtn);
        falsebtn=findViewById(R.id.falsebtn);
        preiv=findViewById(R.id.prebtn);
        nxtiv=findViewById(R.id.nxtbtn);
        cardView=findViewById(R.id.cv);
        cScore=findViewById(R.id.cscoretv);
        hScore=findViewById(R.id.hscoretv);
        sharebtn=findViewById(R.id.sharebtn);


        defaultHighScoreValueAndState();






               arrayList=new QuestionBank().getQuestions(new AnswerLiseAsyncResponse() {
            @Override
            public void processFinished(List<Question> questionsArrayList) {
                Log.d("Tokes", "processFinished: "+questionsArrayList);

                countertv.setText(counter+"/"+arrayList.size());
                  questv.setText(questionsArrayList.get(counter).getAnswer());

            }
        });

        truebtn.setOnClickListener(this);
        falsebtn.setOnClickListener(this);
        preiv.setOnClickListener(this);
        nxtiv.setOnClickListener(this);
        sharebtn.setOnClickListener(this);
    }

    private void defaultHighScoreValueAndState()
    {
        SharedPreferences preferences=getSharedPreferences(MESSAGE_KEY,MODE_PRIVATE);
        highScore =preferences.getInt("highScore",0);
        counter=preferences.getInt("State",0);
        hScore.setText("High Score: "+ highScore);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.truebtn:
                    checkAnswer(true);
                    updateQuestion();
                break;

            case R.id.falsebtn:

                checkAnswer(false);
                updateQuestion();
                break;


            case R.id.prebtn:
                if(counter!=0)
                    counter--;
                updateQuestion();
                break;


            case R.id.nxtbtn:
                nextQuestion();
                break;

            case R.id.sharebtn:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"I am Playing Trivia");
                intent.putExtra(Intent.EXTRA_TEXT,"My Current Score: "+currScore+" My high Score :"+highScore);
                startActivity(intent);

                break;
        }


    }

    private void checkAnswer(boolean userchoice)
    {

        if (userchoice==arrayList.get(counter).isAnswertrue()) {


                currScore = currScore + 100;
                cScore.setText("Current Score: " + currScore);
                Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                fadeAnimation();
        }

        else {


                if (currScore >0)
            currScore = currScore -100;

            cScore.setText("Current Score: "+ currScore);
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
            shakeAnimation();

        }

    }

    private void updateQuestion() {
        String question=arrayList.get(counter).getAnswer();
        questv.setText(question);
        countertv.setText(counter+"/"+arrayList.size());


    }

    private void shakeAnimation()
    {
        Animation shake= AnimationUtils.loadAnimation(this,R.anim.shake_animation);

        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED) ;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                nextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void fadeAnimation()
    {

        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                nextQuestion();
            }



            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void nextQuestion() {
        counter++;
        counter=counter%arrayList.size();
        updateQuestion();
    }
    @Override
    protected void onPause() {

        SharedPreferences preferences=getSharedPreferences(MESSAGE_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        if (currScore > highScore)
        {
            editor.putInt("highScore", currScore);
        }
        editor.putInt("State",counter);
        editor.apply();
            super.onPause();

    }

}
