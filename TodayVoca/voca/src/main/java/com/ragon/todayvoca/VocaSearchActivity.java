package com.ragon.todayvoca;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class VocaSearchActivity extends AppCompatActivity {
    RecyclerView vrecyclerview;
    ArrayList<VocaListItem> vocaArrayList;
    VocaListAdapter vocaListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_search);

        vrecyclerview = findViewById(R.id.rv_voca_search);
        vrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        vrecyclerview.setHasFixedSize(true);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


     //단어장 이름의 경우 수동 입력

        ArrayList<VocaListItem> items = new ArrayList<>();
        VocaListItem item1 = new VocaListItem("토익 영단어 200");
        VocaListItem item2 = new VocaListItem("토플 영단어 200");
        VocaListItem item3 = new VocaListItem("아이엘츠 영단어 200");
        VocaListItem item4 = new VocaListItem("오픽 영단어 200");
        VocaListItem item5 = new VocaListItem("수능 영단어 200");


        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);

        VocaListAdapter adapter = new VocaListAdapter(this, R.layout.vocasearch_item,items);
        vrecyclerview.setAdapter(adapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    }
