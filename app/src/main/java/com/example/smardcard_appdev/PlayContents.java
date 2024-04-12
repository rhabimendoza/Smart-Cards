package com.example.smardcard_appdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class PlayContents extends AppCompatActivity {

    TextView tvName, tvScore, tvContent;
    Button btnSkip, btnDone;

    DatabaseHelper dbHelper;

    List<ContentItem> contents;
    Vector<String> questions;
    Vector<String> answers;

    long stackId;
    String stackName, stackDesc;

    int score = 0;
    int page = 1;
    int index = 0;
    boolean empty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_contents);
        
        Intent i = getIntent();
        
        stackId = i.getLongExtra("STACK_ID", 0);
        stackName = i.getStringExtra("STACK_NAME");
        stackDesc = i.getStringExtra("STACK_DESC");

        tvName = findViewById(R.id.tvName);
        tvScore = findViewById(R.id.tvScore);
        tvContent = findViewById(R.id.tvContent);
        btnSkip = findViewById(R.id.btnSkip);
        btnDone = findViewById(R.id.btnDone);

        dbHelper = new DatabaseHelper(this);

        tvName.setText(stackName);
        tvScore.setText("Score: " + score);

        contents = dbHelper.getAllQuestions(stackId);

        questions = new Vector<>();
        answers = new Vector<>();

        for(ContentItem contentItem : contents){
            questions.add(contentItem.getQuestionText());
            answers.add(contentItem.getAnswerText());
        }

        if(questions.size() == 0){
            btnSkip.setEnabled(false);
            btnDone.setEnabled(false);
            empty = true;
        }
        else{
            tvScore.setText("Score: " + score + "/" + questions.size());
            tvContent.setText(questions.get(0));
        }

        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!empty){
                    page++;
                    if(page % 2 != 0){
                        tvContent.setText(questions.get(index));
                        tvContent.setBackgroundResource(R.drawable.box1);
                    }
                    else{
                        tvContent.setText(answers.get(index));
                        tvContent.setBackgroundResource(R.drawable.box2);
                    }

                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions.add(questions.get(index));
                answers.add(answers.get(index));

                tvScore.setText("Score: " + score + "/" + questions.size());
                index++;
                page = 1;
                tvContent.setText(questions.get(index));
                tvContent.setBackgroundResource(R.drawable.box1);

                Toast.makeText(PlayContents.this, "Question skipped!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score++;
                tvScore.setText("Score: " + score + "/" + questions.size());

                if(index == questions.size() - 1){
                    btnDone.setEnabled(false);
                    btnSkip.setEnabled(false);
                }
                else{
                    index++;
                    page = 1;
                    tvContent.setText(questions.get(index));
                    tvContent.setBackgroundResource(R.drawable.box1);

                    Toast.makeText(PlayContents.this, "Score +1", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}