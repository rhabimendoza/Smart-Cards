package com.example.smardcard_appdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditStack extends AppCompatActivity {

    EditText txtEditName, txtEditDesc;
    Button btnUpdateStack;

    DatabaseHelper dbHelper;

    long stackId;
    String stackName, stackDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stack);

        Intent i = getIntent();

        stackId = i.getLongExtra("STACK_ID", 0);
        stackName = i.getStringExtra("STACK_NAME");
        stackDesc = i.getStringExtra("STACK_DESC");

        txtEditName = findViewById(R.id.txtEditName);
        txtEditDesc = findViewById(R.id.txtEditDesc);
        btnUpdateStack = findViewById(R.id.btnUpdateStack);

        dbHelper = new DatabaseHelper(this);

        txtEditName.setText(stackName);
        txtEditDesc.setText(stackDesc);

        btnUpdateStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtEditName.getText().toString().trim();
                String desc = txtEditDesc.getText().toString().trim();

                if(name.isEmpty() || desc.isEmpty()){
                    Toast.makeText(EditStack.this, "Please enter a stack name and a description!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    int rows = dbHelper.updateStack(stackId, name, desc);

                    if(rows > 0){
                        Toast.makeText(EditStack.this, "Stack successfully updated!", Toast.LENGTH_SHORT).show();

                        stackName = name;
                        stackDesc = desc;

                        Intent i = new Intent(EditStack.this, ContentsList.class);
                        i.putExtra("STACK_ID", stackId);
                        i.putExtra("STACK_NAME", stackName);
                        i.putExtra("STACK_DESC", stackDesc);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(EditStack.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}