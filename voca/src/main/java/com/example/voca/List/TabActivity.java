package com.example.voca.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.voca.MainActivity;
import com.example.voca.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TabActivity extends AppCompatActivity implements VocaDialog.VocaDialogListener {


    private ArrayList<TabListItem> items = new ArrayList<>();
    private RecyclerView tRecyclerView;
    private TabListAdapter tAdapter;
    private ItemTouchHelper tItemTouchHelper;

    //floatingactionbtn

    private FloatingActionButton fabMain;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabSearch;
    private Button rv_btn;

    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        //findViewById,버튼 매핑
        fabMain = findViewById(R.id.fabMain);
        fabEdit = findViewById(R.id.fabEdit);
        fabSearch = findViewById(R.id.fabSearch);
        tRecyclerView = (RecyclerView) findViewById(R.id.rv_tab_list);
        rv_btn = findViewById(R.id.rv_btn);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        tRecyclerView.setLayoutManager(manager);

        tAdapter = new TabListAdapter();
        tRecyclerView.setAdapter(tAdapter);

        tItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(tAdapter));

        tItemTouchHelper.attachToRecyclerView(tRecyclerView);


        TabListItem item1 = new TabListItem("단어장1");
        TabListItem item2 = new TabListItem("단어장2");
        TabListItem item3 = new TabListItem("단어장3");
        TabListItem item4 = new TabListItem("단어장4");
        TabListItem item5 = new TabListItem("단어장5");
        TabListItem item6 = new TabListItem("단어장6");


        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);


        tAdapter.setItems(items);
        fabMain.setOnClickListener(view -> toggleFab());
        //즐겨찾기 단어장, recyclerview에 포함하지 않음
        rv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), VocaSearchActivity.class);
                startActivity(intent2);
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    public void openDialog() {
        VocaDialog vocaDialog = new VocaDialog();
        vocaDialog.show(getSupportFragmentManager(), "voca dialog");
    }

    // 플로팅 액션 버튼 클릭시 애니메이션 효과
    public void toggleFab() {
        if (fabMain_status) {
            // 플로팅 액션 버튼 닫기
            // 애니메이션 추가
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabSearch, "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", 0f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_baseline_add_24);

        } else {
            // 플로팅 액션 버튼 열기
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabSearch, "translationY", -200f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", -400f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_baseline_close_24);
        }
        // 플로팅 버튼 상태 변경
        fabMain_status = !fabMain_status;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
             Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void applyTexts(String vocaname) {
        TabListItem itemadd= new TabListItem(vocaname);
        items.add(itemadd);
        tAdapter.notifyDataSetChanged();
    }
}