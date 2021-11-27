package com.ragon.todayvoca;

import androidx.recyclerview.widget.RecyclerView;

public class VocaListItem  {
    public String name;

    public VocaListItem(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
//extends RecyclerView.ViewHolder