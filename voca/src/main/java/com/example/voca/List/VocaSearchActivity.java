package com.example.voca.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.voca.R;

import java.util.ArrayList;

public class VocaSearchActivity extends AppCompatActivity {
    RecyclerView vrecyclerview;
    ArrayList<VocaListItem> items;
    VocaListAdapter vocaListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_search);

        vrecyclerview = findViewById(R.id.rv_voca_search);
        vrecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        vrecyclerview.setHasFixedSize(true);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


     //단어장 이름의 경우 수동 입력

        ArrayList<VocaListItem> items = new ArrayList<>();
        VocaListItem item1 = new VocaListItem("초중등 기본 영단어");
        VocaListItem item2 = new VocaListItem("고등 기본 영단어");
        VocaListItem item3 = new VocaListItem("TEPS");
        VocaListItem item4 = new VocaListItem("TOEFL");
        VocaListItem item5 = new VocaListItem("TOEIC");


        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);

        vocaListAdapter = new VocaListAdapter(this, R.layout.vocasearch_item,items);
        vrecyclerview.setAdapter(vocaListAdapter);

        //리사이클러뷰 아이템 클릭 이벤트
        vocaListAdapter.setOnItemClickListener (new VocaListAdapter.OnItemClickListener () {

            //아이템 클릭시 VocaSearchSubActivity로 이동
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(VocaSearchActivity.this, VocaSearchSubActivity.class);
                // 선택된 단어장 이름 액션바에 나타내기
                switch (position)
                {
                    // title: 액션바 부분에 나타날 이름(제목)
                    // name: 데이터 불러올 때 쓸 이름
                    case 0: intent.putExtra("title", "초중등 기본 영단어");
                            intent.putExtra("name", "elementary");
                            break; // item1
                    case 1: intent.putExtra("title", "고등 기본 영단어");
                            intent.putExtra("name", "high");
                            break; // item2
                    case 2: intent.putExtra("title", "TEPS");
                            intent.putExtra("name", "teps");
                            break; // item3
                    case 3: intent.putExtra("title", "TOEFL");
                            intent.putExtra("name", "toefl");
                            break; // item4
                    case 4: intent.putExtra("title", "TOEIC");
                            intent.putExtra("name", "toeic");
                            break; // item5
                    default: break;
                }
                startActivity(intent);
            }

        });

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
