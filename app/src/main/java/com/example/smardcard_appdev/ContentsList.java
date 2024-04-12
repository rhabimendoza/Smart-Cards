package com.example.smardcard_appdev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContentsList extends AppCompatActivity implements ContentsAdapter.OnItemClickListener {

    TextView disStackName, disStackDesc;
    Button btnEditStack, btnDeleteStack, btnPlayContent, btnAddContent;
    EditText txtSearchContent;
    RecyclerView recyclerViewContents;

    DatabaseHelper dbHelper;
    ContentsAdapter contentAdapter;

    List<ContentItem> contentList;
    List<ContentItem> filteredContent;

    long stackId;
    String stackName, stackDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_list);

        Intent i = getIntent();

        stackId = i.getLongExtra("STACK_ID", 0);
        stackName = i.getStringExtra("STACK_NAME");
        stackDesc = i.getStringExtra("STACK_DESC");

        disStackName = findViewById(R.id.disStackName);
        disStackDesc = findViewById(R.id.disStackDesc);
        btnEditStack = findViewById(R.id.btnEditStack);
        btnDeleteStack = findViewById(R.id.btnDeleteStack);
        btnPlayContent = findViewById(R.id.btnPlayContent);
        btnAddContent = findViewById(R.id.btnAddContent);
        txtSearchContent = findViewById(R.id.txtSearchContent);
        recyclerViewContents = findViewById(R.id.recyclerViewContents);

        dbHelper = new DatabaseHelper(this);

        disStackName.setText(stackName);
        disStackDesc.setText(stackDesc);

        btnEditStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContentsList.this, EditStack.class);
                i.putExtra("STACK_ID", stackId);
                i.putExtra("STACK_NAME", stackName);
                i.putExtra("STACK_DESC", stackDesc);
                startActivity(i);
                finish();
            }
        });

        btnDeleteStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteQuestionsByStackId(stackId);
                dbHelper.deleteStack(stackId);
                finish();
            }
        });

        btnPlayContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContentsList.this, PlayContents.class);
                i.putExtra("STACK_ID", stackId);
                i.putExtra("STACK_NAME", stackName);
                i.putExtra("STACK_DESC", stackDesc);
                startActivity(i);
            }
        });

        btnAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContentsList.this, CreateContents.class);
                i.putExtra("STACK_ID", stackId);
                i.putExtra("STACK_NAME", stackName);
                i.putExtra("STACK_DESC", stackDesc);
                startActivity(i);
                finish();
            }
        });

        recyclerViewContents.setLayoutManager(new LinearLayoutManager(this));

        contentList = dbHelper.getAllQuestions(stackId);

        filteredContent = new ArrayList<>(contentList);
        contentAdapter = new ContentsAdapter(this, filteredContent, this);
        recyclerViewContents.setAdapter(contentAdapter);

        txtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filteredContent(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        contentList.clear();
        contentList.addAll(dbHelper.getAllQuestions(stackId));
        filteredContent(txtSearchContent.getText().toString());
    }

    @Override
    public void onItemClick(ContentItem contentItem, int position, View clickedView){
        long questionId = contentItem.getquestionId();
        String contentQuestion = contentItem.getQuestionText();
        String contentAnswer = contentItem.getAnswerText();

        Intent i = new Intent(ContentsList.this, EditContent.class);
        i.putExtra("STACK_ID", stackId);
        i.putExtra("STACK_NAME", stackName);
        i.putExtra("STACK_DESC", stackDesc);
        i.putExtra("QUESTION_ID", questionId);
        i.putExtra("QUESTION", contentQuestion);
        i.putExtra("ANSWER", contentAnswer);
        startActivity(i);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        onResume();
    }

    public void filteredContent(String searchText){
        filteredContent.clear();
        for(ContentItem contentItem: contentList){
            if(contentItem.getQuestionText().toLowerCase().contains(searchText.toLowerCase())){
                filteredContent.add(contentItem);
            }
        }

        contentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(ContentItem contentItem, int position){
        long contentId = contentItem.getquestionId();
        dbHelper.deleteQuestion(contentId);
        contentList.remove(position);
        onResume();
        contentAdapter.notifyDataSetChanged();
    }
}