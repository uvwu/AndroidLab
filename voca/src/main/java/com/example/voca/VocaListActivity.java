package com.example.voca;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.Memorize.MemorizeActivity;
import com.example.voca.Memorize.MultiChoiceActivity;
import com.example.voca.Memorize.SpellCheckActivity;

public class VocaListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);

        //암기버튼
        Button memorizeBtn=findViewById(R.id.btn_memorize);
        memorizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VocaListActivity.this, MemorizeActivity.class);
                startActivity(intent);
            }
        });
        //객관식 버튼
        Button choiceBtn=findViewById(R.id.btn_choice);
        choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(VocaListActivity.this, MultiChoiceActivity.class);
                startActivity(intent1);
            }
        });
        //스펠 체크 버튼
        Button spellCheckBtn=findViewById(R.id.btn_spellcheck);
        spellCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(VocaListActivity.this, SpellCheckActivity.class);
                startActivity(intent2);
            }
        });
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
                break;
            case R.id.alpha_sort:
                item.setChecked(true);
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
}
