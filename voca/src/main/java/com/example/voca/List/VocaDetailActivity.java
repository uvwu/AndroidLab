package com.example.voca.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.voca.R;

import java.util.ArrayList;

public class VocaDetailActivity extends AppCompatActivity {
    private RecyclerView dRecyclerView;
    private VocaDetailAdapter dAdapter;
    private ItemTouchHelper dItemTouchHelper;

    // final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_detail);

        dRecyclerView = (RecyclerView) findViewById(R.id.rv_detail_list);
        dRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        dRecyclerView.setHasFixedSize(true);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        dAdapter = new VocaDetailAdapter();
        dRecyclerView.setAdapter(dAdapter);

        dItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(dAdapter));

        dItemTouchHelper.attachToRecyclerView(dRecyclerView);

        ArrayList<VocaDetailItem> items = new ArrayList<>();
        VocaDetailItem item1 = new VocaDetailItem("apple","사과");
        VocaDetailItem item2 = new VocaDetailItem("student","학생");
        VocaDetailItem item3 = new VocaDetailItem("english","영어");
        VocaDetailItem item4 = new VocaDetailItem("단어4","뜻4");
        VocaDetailItem item5 = new VocaDetailItem("단어5","뜻5");

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);



        dAdapter.setItems(items);

    }

}