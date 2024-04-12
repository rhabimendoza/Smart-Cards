package com.example.smardcard_appdev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "smartcard.db";
    static final int DATABASE_VERSION = 4;

    static final String TABLE_STACKS = "stacks";
    static final String COLUMN_STACK_ID = "_id";
    static final String COLUMN_STACK_NAME = "stack_name";
    static final String COLUMN_STACK_DESC = "stack_desc";

    static final String TABLE_QUESTIONS = "questions";
    static final String COLUMN_QUESTION_ID = "_id";
    static final String COLUMN_QUESTION_STACK_ID = "stack_id";
    static final String COLUMN_QUESTION = "question";
    static final String COLUMN_ANSWER = "answer";

    String CREATE_STACKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STACKS + " ("
            + COLUMN_STACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_STACK_NAME + " TEXT, "
            + COLUMN_STACK_DESC + " TEXT);";

    String CREATE_QUESTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS + " ("
            + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_QUESTION_STACK_ID + " INTEGER, "
            + COLUMN_QUESTION + " TEXT, "
            + COLUMN_ANSWER + " TEXT);";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_STACKS_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public List<StackItem> getAllStacks(){
        List<StackItem> stackItemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_STACKS,
                new String[]{COLUMN_STACK_ID, COLUMN_STACK_NAME, COLUMN_STACK_DESC},
                null, null, null, null, null
        );

        int idIndex = cursor.getColumnIndex(COLUMN_STACK_ID);
        int nameIndex = cursor.getColumnIndex(COLUMN_STACK_NAME);
        int descIndex = cursor.getColumnIndex(COLUMN_STACK_DESC);

        while(cursor.moveToNext()){
            long stackId = cursor.getLong(idIndex);
            String stackName = cursor.getString(nameIndex);
            String stackDesc = cursor.getString(descIndex);

            stackItemList.add(new StackItem(stackId, stackName, stackDesc));
        }

        cursor.close();
        db.close();
        return stackItemList;
    }

    public List<ContentItem> getAllQuestions(long id){
        List <ContentItem> questionItemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_QUESTIONS,
                new String[]{COLUMN_QUESTION_ID, COLUMN_QUESTION_STACK_ID, COLUMN_QUESTION, COLUMN_ANSWER},
                COLUMN_QUESTION_STACK_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        int questionId = cursor.getColumnIndex(COLUMN_QUESTION_ID);
        int stackId = cursor.getColumnIndex(COLUMN_QUESTION_STACK_ID);
        int questionIndex = cursor.getColumnIndex(COLUMN_QUESTION);
        int answerIndex = cursor.getColumnIndex(COLUMN_ANSWER);

        while(cursor.moveToNext()){
           long qId = cursor.getLong(questionId);
           long sId = cursor.getLong(stackId);
           String question = cursor.getString(questionIndex);
           String answer = cursor.getString(answerIndex);

           questionItemList.add(new ContentItem(qId, sId, question, answer));
        }

        cursor.close();
        db.close();
        return questionItemList;
    }

    public void deleteQuestionsByStackId(long questionId){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_QUESTIONS, COLUMN_QUESTION_STACK_ID + " = ?", new String[]{String.valueOf(questionId)});
        db.close();
    }

    public void deleteStack(long stackId){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_STACKS, COLUMN_STACK_ID + " = ?", new String[]{String.valueOf(stackId)});
        db.close();
    }

    public void deleteQuestion(long questionId){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_QUESTIONS, COLUMN_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)});
        db.close();
    }

    public int updateStack(long stackId, String updatedName, String updatedDesc){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STACK_NAME, updatedName);
        values.put(COLUMN_STACK_DESC, updatedDesc);

        String whereClause = COLUMN_STACK_ID + " = ?";
        String[] whereArgs = {String.valueOf(stackId)};

        int rowsAffected = db.update(TABLE_STACKS, values, whereClause, whereArgs);

        db.close();

        return rowsAffected;
    }

    public int updateQuestion(Context context, long questionId, String updatedQuestion, String updatedAnswer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, updatedQuestion);
        values.put(COLUMN_ANSWER, updatedAnswer);

        String whereClause = COLUMN_QUESTION_ID + " = ?";
        String[] whereArgs = {String.valueOf(questionId)};

        int rowsAffected = db.update(TABLE_QUESTIONS, values, whereClause, whereArgs);

        db.close();

        return rowsAffected;
    }

}