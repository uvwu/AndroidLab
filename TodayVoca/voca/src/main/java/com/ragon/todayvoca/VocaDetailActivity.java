package com.ragon.todayvoca;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class VocaDetailActivity extends AppCompatActivity {
    private RecyclerView dRecyclerView;
    private VocaDetailAdapter dAdapter;
    private ItemTouchHelper dItemTouchHelper;


    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_detail);

        //findViewById,버튼 매핑
        dRecyclerView = (RecyclerView) findViewById(R.id.rv_detail_list);

        //액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        dRecyclerView.setLayoutManager(manager);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_option, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.menu1:{ //toolbar의 back키 눌렀을 때 동작
                toast.setText("Select Menu1");
                return true;
            }
            case R.id.menu2:{ //toolbar의 back키 눌렀을 때 동작
                toast.setText("Select Menu1");
                return true;
            }
        }
        toast.show();
        return super.onOptionsItemSelected(item);
    }
}