package com.example.smardcard_appdev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StacksAdapter extends RecyclerView.Adapter<StacksAdapter.StackViewHolder> {

    private Context context;
    private List<StackItem> stackItemList;
    private OnItemClickListener onItemClickListener;

    public StacksAdapter(Context context, List<StackItem> stackItemList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.stackItemList = stackItemList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount(){
        return stackItemList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(StackItem stackItem, int position, View clickedView);
    }

    @NonNull
    @Override
    public StackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.stacks_row, parent, false);
        return new StackViewHolder(view);
    }

    static class StackViewHolder extends RecyclerView.ViewHolder{
        TextView recStackName;
        TextView recStackDesc;

        public StackViewHolder(@NonNull View itemView){
            super(itemView);
            recStackName = itemView.findViewById(R.id.recStackName);
            recStackDesc = itemView.findViewById(R.id.recStackDesc);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull StackViewHolder holder, int position){
        StackItem stackItem = stackItemList.get(position);

        holder.recStackName.setText(stackItem.getStackName());
        holder.recStackDesc.setText(stackItem.getStackDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(stackItem, holder.getAdapterPosition(), v);
                }
            }
        });
    }
}