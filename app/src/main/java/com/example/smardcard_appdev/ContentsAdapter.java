package com.example.smardcard_appdev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentViewHolder> {

    private Context context;
    private List<ContentItem> contentItemList;
    private OnItemClickListener onItemClickListener;

    public ContentsAdapter(Context context, List<ContentItem> contentItemList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.contentItemList = contentItemList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount(){
        return contentItemList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(ContentItem contentItem, int position, View clickedView);
        void onDeleteClick(ContentItem contentItem, int position);
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.contents_row, parent, false);
        ContentViewHolder viewHolder = new ContentViewHolder(view);

        viewHolder.btnDeleteContent.setOnClickListener( v -> {
            int position = viewHolder.getAdapterPosition();
            if(position != RecyclerView.NO_POSITION && onItemClickListener != null){
                onItemClickListener.onDeleteClick(contentItemList.get(position), position);
            }
        });

        return viewHolder;
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder{
        TextView recQuestionName;
        Button btnDeleteContent;

        public ContentViewHolder(@NonNull View itemView){
            super(itemView);
            recQuestionName = itemView.findViewById(R.id.recQuestionName);
            btnDeleteContent = itemView.findViewById(R.id.btnDeleteContent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position){
        ContentItem contentItem = contentItemList.get(position);

        holder.recQuestionName.setText(contentItem.getQuestionText());

        holder.itemView.setOnClickListener( v -> {
            if(onItemClickListener != null){
                onItemClickListener.onItemClick(contentItem, position, v);
            }
        });
    }
}