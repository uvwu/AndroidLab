package com.example.voca.VocaList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.Memorize.Basic.MemorizeActivity;
import com.example.voca.Memorize.MultiChoice.MultiChoiceActivity;
import com.example.voca.Memorize.SpellCheck.SpellCheckActivity;
import com.example.voca.R;
import com.example.voca.VocaVO;

import java.util.ArrayList;
import java.util.Collections;

public class VocaListActivity extends AppCompatActivity {

    ArrayList<VocaVO> vocaData=new ArrayList<>();
    ListView vocaListView;
    ArrayList<VocaVO> showData=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);

        //-----------------리스트 뷰-----------
        vocaListView=findViewById(R.id.voca_listview);

        getListViewItem();//리스트뷰 아이템 가져오기

        showListView();//리스트뷰 보여주기
        //----------------------------------------------

        //암기화면 이동 버튼
        Button memorizeBtn=findViewById(R.id.btn_memorize);
        memorizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VocaListActivity.this, MemorizeActivity.class);
                Bundle bundleData=new Bundle();
                bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                intent.putExtra("vocaData",bundleData);
                //intent.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                startActivity(intent);
            }
        });
        //객관식 화면 이동 버튼
        Button choiceBtn=findViewById(R.id.btn_choice);
        choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(VocaListActivity.this, MultiChoiceActivity.class);
                Bundle bundleData=new Bundle();
                bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                intent1.putExtra("vocaData",bundleData);
                //intent1.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                startActivity(intent1);
            }
        });
        //스펠 체크 화면 이동 버튼
        Button spellCheckBtn=findViewById(R.id.btn_spellcheck);
        spellCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(VocaListActivity.this, SpellCheckActivity.class);
                Bundle bundleData=new Bundle();
                bundleData.putParcelableArrayList("vocaData",(ArrayList<VocaVO>) vocaData);
                intent2.putExtra("vocaData",bundleData);
                // intent2.putParcelableArrayListExtra("vocaData",(ArrayList<VocaVO>) vocaData);
                startActivity(intent2);
            }
        });
    }
    private void getListViewItem(){
        vocaData.add(new VocaVO("door","문",true,true));
        vocaData.add(new VocaVO("cap","모자",true,true));
        vocaData.add(new VocaVO("banana","바나나",true,true));
        vocaData.add(new VocaVO("apple","사과",true,true));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_voca_list,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.time_sort:
                item.setChecked(true);
                vocaData.clear();
                showData.clear();
                getListViewItem();
                showListView();
                break;
            case R.id.alpha_sort:
                item.setChecked(true);
                vocaData.clear();
                showData.clear();
                getListViewItem();
                Collections.sort(vocaData);
                showListView();
                break;
            case R.id.voca_add:
                showToast("단어 추가");
                break;
            case R.id.voca_remove:
                showToast("단어 삭제");
                break;
        }

        return true;
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_SHORT);
        toast.show();
    }
    private void showListView(){
        for(int i=0;i<vocaData.size();i++){
            VocaVO vo=new VocaVO();
            vo.memoCheck=vocaData.get(i).memoCheck;
            vo.vocaEng= vocaData.get(i).vocaEng;
            vo.vocaKor= vocaData.get(i).vocaKor;
            vo.starCheck= vocaData.get(i).starCheck;
            showData.add(vo);
        }
        VocaListAdapter adapter=new VocaListAdapter(this,R.layout.voca_list_item,showData);
        vocaListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        vocaData=null;
        vocaListView=null;
        showData=null;
        super.onDestroy();
    }
}
