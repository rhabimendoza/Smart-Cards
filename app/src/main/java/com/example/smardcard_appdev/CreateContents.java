package com.example.smardcard_appdev;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateContents extends AppCompatActivity {

    EditText txtQuestion, txtAnswer;
    Button btnCreateContent;

    DatabaseHelper dbHelper;

    long stackId;
    String stackName, stackDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contents);

        Intent i = getIntent();

        stackId = i.getLongExtra("STACK_ID", 0);
        stackName = i.getStringExtra("STACK_NAME");
        stackDesc = i.getStringExtra("STACK_DESC");

        txtQuestion = findViewById(R.id.txtCreateQuestion);
        txtAnswer = findViewById(R.id.txtCreateAnswer);
        btnCreateContent = findViewById(R.id.btnCreateContent);

        dbHelper = new DatabaseHelper(this);

        btnCreateContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContent();
            }
        });
    }

    public void createContent(){
        String questionText = txtQuestion.getText().toString().trim();
        String answerText = txtAnswer.getText().toString().trim();

        if(questionText.isEmpty() || answerText.isEmpty()){
            Toast.makeText(this, "Please enter a question and an answer!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUESTION_STACK_ID, stackId);
        values.put(DatabaseHelper.COLUMN_QUESTION, questionText);
        values.put(DatabaseHelper.COLUMN_ANSWER, answerText);

        long questionId = db.insert(DatabaseHelper.TABLE_QUESTIONS, null, values);

        if(questionId != -1){
            Toast.makeText(CreateContents.this, "Content created successfully!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(CreateContents.this, ContentsList.class);
            i.putExtra("STACK_ID", stackId);
            i.putExtra("STACK_NAME", stackName);
            i.putExtra("STACK_DESC", stackDesc);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}