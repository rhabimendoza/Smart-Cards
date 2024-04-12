package com.example.smardcard_appdev;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StacksList extends AppCompatActivity implements StacksAdapter.OnItemClickListener {

    EditText txtSearchStack;
    RecyclerView recyclerViewStacks;
    Button btnAddStack;

    DatabaseHelper dbHelper;
    StacksAdapter stackAdapter;

    List<StackItem> stackList;
    List<StackItem> filteredStack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks_list);

        txtSearchStack = findViewById(R.id.txtSearchStack);
        recyclerViewStacks = findViewById(R.id.recyclerViewStacks);
        btnAddStack = findViewById(R.id.btnAddStack);

        dbHelper = new DatabaseHelper(this);

        recyclerViewStacks.setLayoutManager(new LinearLayoutManager(this));

        btnAddStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StacksList.this, CreateStacks.class);
                startActivity(i);
            }
        });

        stackList = dbHelper.getAllStacks();

        filteredStack = new ArrayList<>(stackList);
        stackAdapter = new StacksAdapter(this, filteredStack, this);
        recyclerViewStacks.setAdapter(stackAdapter);

        txtSearchStack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filteredStack(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onResume(){
       super.onResume();
       stackList.clear();
       stackList.addAll(dbHelper.getAllStacks());
       filteredStack(txtSearchStack.getText().toString());
    }

    public void filteredStack(String searchText){
        filteredStack.clear();
        for(StackItem stackItem: stackList){
            if(stackItem.getStackName().toLowerCase().contains(searchText.toLowerCase()) || stackItem.getStackDesc().toLowerCase().contains(searchText.toLowerCase())){
                filteredStack.add(stackItem);
            }
        }

        stackAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(StackItem stackItem, int position, View clickedView){
        long stackId = stackItem.getstackId();
        String stackName = stackItem.getStackName();
        String stackDesc = stackItem.getStackDesc();

        Intent i = new Intent(StacksList.this, ContentsList.class);
        i.putExtra("STACK_ID", stackId);
        i.putExtra("STACK_NAME", stackName);
        i.putExtra("STACK_DESC", stackDesc);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        onResume();
    }
}