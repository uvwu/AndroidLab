package com.example.voca.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;

import java.util.ArrayList;

public class TabListAdapter extends RecyclerView.Adapter<TabListAdapter.ItemViewHolder>
        implements ItemTouchHelperListener {

private ArrayList<TabListItem> items = new ArrayList<>();

public TabListAdapter() {
        }

@NonNull
@Override
public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tablist_item, parent, false);
        return new ItemViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(items.get(position), position);
        }

@Override
public int getItemCount() {
        return items.size();
        }

public void setItems(ArrayList<TabListItem> itemList) {
        items = itemList;
        notifyDataSetChanged();
        }

@Override
public boolean onItemMove(int form_position, int to_position) {
    TabListItem item = items.get(form_position);
        items.remove(form_position);
        items.add(to_position, item);
        // item.setNumber(to_position);
        notifyItemMoved(form_position, to_position);
        return true;
        }

@Override
public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        }

public class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView tabName;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tabName = itemView.findViewById(R.id.tab_list_item);
    }

    public void onBind(TabListItem item, int position) {
        tabName.setText(item.getName());
        //item.setNumber(position);
    }
}
}