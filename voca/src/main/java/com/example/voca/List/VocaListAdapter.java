package com.example.voca.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;

import java.util.ArrayList;
//ArrayAdapter<VocaListItem>
public class VocaListAdapter extends RecyclerView.Adapter<VocaListAdapter.MyViewHolder>  implements ItemTouchHelperListener {


    Context context;
    int resId;
    ArrayList<VocaListItem> items;

    public VocaListAdapter(Context context,int resId, ArrayList<VocaListItem> items) {
        this.context = context;
        this.items = items;
        this.resId = resId;
    }

    @NonNull
    @Override
    public VocaListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.vocasearch_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VocaListAdapter.MyViewHolder holder, int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VocaDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int form_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            context = itemView.getContext();
            nameView = itemView.findViewById(R.id.voca_search_item);

        }

    }

}
