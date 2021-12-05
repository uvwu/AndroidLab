package com.example.voca.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.voca.R;

import java.util.ArrayList;

// vocaSearchActivity에서 아이템 누른 후 변경되는 화면 관리
public class VocaSearchSubActivity extends AppCompatActivity {
    private static final String TAG = "VocaSearchSubActivity";

    RecyclerView recyclerview;
    ArrayList<VocaListItem> items;
    VocaListSubAdapter vocaListSubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_sub_search);

        recyclerview = findViewById(R.id.rv_voca_search_sub);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setHasFixedSize(true);



        Intent intent = getIntent();
        String title = intent.getExtras().getString("title"); // 액션바 부분에 쓰일 단어장 제목 받아옴
        String name = intent.getExtras().getString("name"); // DB에서 데이터 불러올 때 쓸 이름

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title); // 액션바의 제목 부분
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // week 1부터 week 10까지 나타내주는 리스트
        ArrayList<VocaListItem> items = new ArrayList<>();

        for(int i = 1; i <= 10; ++i){
            VocaListItem item = new VocaListItem(title + "(" + i + ")");
            items.add(item);
        }

        vocaListSubAdapter = new VocaListSubAdapter(this, R.layout.vocadetail_item,items);
        recyclerview.setAdapter(vocaListSubAdapter);

        //리사이클러뷰 아이템 클릭 이벤트
        vocaListSubAdapter.setOnItemClickListener (new VocaListSubAdapter.OnItemClickListener () {

            //아이템 클릭시  VocaDetailProvidedActivity 로 이동
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "position: " + position);
                Intent intent = new Intent(VocaSearchSubActivity.this,  VocaDetailProvidedActivity .class);
                // 선택된 단어장 이름 액션바에 나타내기
                for(int i = 0; i < 10; ++i)
                {
                    if(position == i)
                    {
                        // subTitle: 액션바 부분에 나타날 이름(제목) -> week 1 이런거
                        // subName: 데이터 가져올 때 쓸 이름
                        intent.putExtra("subTitle", items.get(i).getName());
                        intent.putExtra("subName", (i+1) + "일차");
                        Log.d(TAG, "item.get(i): " + items.get(i).getName());

                        break;
                    }
                }
                intent.putExtra("title", title); // 내 단어장에 저장했을 때 단어장 이름으로 사용
                intent.putExtra("name", name);// DB에서 값 불러오려면 어떤 단어장을 선택했는지 알아야함
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
