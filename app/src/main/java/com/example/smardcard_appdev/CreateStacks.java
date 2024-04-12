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

public class CreateStacks extends AppCompatActivity {

    EditText txtStackName, txtStackDesc;
    Button btnCreateStack;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stacks);

        txtStackName = findViewById(R.id.txtCreateName);
        txtStackDesc = findViewById(R.id.txtCreateDesc);
        btnCreateStack = findViewById(R.id.btnCreateStack);

        dbHelper = new DatabaseHelper(this);

        btnCreateStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStack();
            }
        });
    }

    public void createStack(){
        String stackName = txtStackName.getText().toString().trim();
        String stackDesc = txtStackDesc.getText().toString().trim();

        if(stackName.isEmpty() || stackDesc.isEmpty()){
            Toast.makeText(this, "Please enter a stack name and a description!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STACK_NAME, stackName);
        values.put(DatabaseHelper.COLUMN_STACK_DESC, stackDesc);

        long newRowId = db.insert(DatabaseHelper.TABLE_STACKS, null, values);

        if(newRowId != -1){
            Toast.makeText(CreateStacks.this, "Stack created successfully!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(CreateStacks.this, StacksList.class);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}