package com.example.voca;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class vocaListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);

        //암기버튼
        Button memorizeBtn=findViewById(R.id.btn_memorize);
        memorizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("memorize");
            }
        });
        //객관식 버튼
        Button choiceBtn=findViewById(R.id.btn_choice);
        choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("multi chocie");
            }
        });
        //스펠 체크 버튼
        Button spellCheckBtn=findViewById(R.id.btn_spellcheck);
        spellCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("spell check");
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
