package com.example.smardcard_appdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContent extends AppCompatActivity {

    EditText txtEditQuestion, txtEditAnswer;
    Button btnUpdateQuestion;

    DatabaseHelper dbHelper;

    long stackId, questionId;
    String stackName, stackDesc, questionText, answerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);

        Intent i = getIntent();

        stackId = i.getLongExtra("STACK_ID", 0);
        stackName = i.getStringExtra("STACK_NAME");
        stackDesc = i.getStringExtra("STACK_DESC");
        questionId = i.getLongExtra("QUESTION_ID", 0);
        questionText = i.getStringExtra("QUESTION");
        answerText = i.getStringExtra("ANSWER");

        txtEditQuestion = findViewById(R.id.txtEditQuestion);
        txtEditAnswer = findViewById(R.id.txtEditAnswer);
        btnUpdateQuestion = findViewById(R.id.btnUpdateContent);

        dbHelper = new DatabaseHelper(this);

        txtEditQuestion.setText(questionText);
        txtEditAnswer.setText(answerText);

        btnUpdateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = txtEditQuestion.getText().toString().trim();
                String answer = txtEditAnswer.getText().toString().trim();

                if(question.isEmpty() || answer.isEmpty()){
                    Toast.makeText(EditContent.this, "Please enter a question and an answer!", Toast.LENGTH_SHORT).show();
                }
                else{
                    int rows = dbHelper.updateQuestion(EditContent.this, questionId, question, answer);

                    if(rows > 0){
                        Toast.makeText(EditContent.this, "Content successfully updated!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(EditContent.this, ContentsList.class);
                        i.putExtra("STACK_ID", stackId);
                        i.putExtra("STACK_NAME", stackName);
                        i.putExtra("STACK_DESC", stackDesc);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(EditContent.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}