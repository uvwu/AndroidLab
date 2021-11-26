package com.example.voca;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.voca.drawerActivity.DarkModeActivity;
import com.example.voca.drawerActivity.EditAccountActivity;
import com.example.voca.drawerActivity.GoalSettingActivity;
import com.example.voca.drawerActivity.NoticeActivity;
import com.example.voca.drawerActivity.PushAlertActivity;
import com.firebase.ui.auth.IdpResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IdpResponse mIdpResponse;

    private ActionBarDrawerToggle toggle;//메뉴 화면을 여는 버튼

    private SwitchCompat darkModeSwitch;//다크모드 스위치 버튼
    private SwitchCompat pushAlertSwitch;//푸쉬알림 스위치 버튼

    AlertDialog alertDialog;//로그아웃시 띄울 다이얼로그

    String themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdpResponse = IdpResponse.fromResultIntent(getIntent());

        themeColor = DarkModeUtil.modLoad(getApplicationContext());
        DarkModeUtil.applyTheme(themeColor);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button testBtn=findViewById(R.id.btn_test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VocaListActivity.class));
            }
        });

        //일일 기록
        CircleProgressBar dailyChart=findViewById(R.id.circle_bar);
        int goal=20;
        int achivement=16;
        dailyChart.setProgress((int)((float)achivement/goal*100));
        TextView dayRecord=findViewById(R.id.day_record);
        dayRecord.setText(achivement+"/"+goal);
        //주간 기록 보여줄 수평 스크롤뷰
        HorizontalScrollView horizonScroll=findViewById(R.id.horizon_scroll);
        //왼쪽방향으로 스크롤 되게 하기
        horizonScroll.post(new Runnable() {
            @Override
            public void run() {
                horizonScroll.fullScroll(horizonScroll.FOCUS_RIGHT);
            }
        });

        //주간 기록 차트
        BarChart weekChart=findViewById(R.id.bar_chart);

        //성취 데이터
        List<BarEntry> achivements=new ArrayList<BarEntry>();
        for(int i=0;i<10;i++)
            achivements.add(new BarEntry(i,(i+1)*10));//x,y

        BarDataSet dataSet1=new BarDataSet(achivements,"외운 단어수");
        dataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet1.setColor(ColorTemplate.JOYFUL_COLORS[1]);

        //목표 데이터
        List<BarEntry> goals=new ArrayList<>();
        for (int i=0;i<10;i++)
            goals.add(new BarEntry(i,100));

        BarDataSet dataSet=new BarDataSet(goals,"목표");
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setColor(R.color.theme1);
        dataSet.setFormLineWidth(10f);


        //차트에 데이터 넣기
        BarData bardata=new BarData(dataSet,dataSet1);
        bardata.setBarWidth(0.7f);//차트 폭폭
        bardata.setValueTextSize(15);

        //차트 그래프 가공(격자선 없애기 등등)
        weekChart.getDescription().setEnabled(false);
        weekChart.getXAxis().setDrawGridLines(false);
        weekChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        weekChart.getAxisLeft().setDrawGridLines(false);
        weekChart.getAxisRight().setDrawGridLines(false);
        weekChart.getAxisRight().setEnabled(false);
        weekChart.getAxisLeft().setEnabled(false);
        weekChart.setDrawGridBackground(false);
        weekChart.setData(bardata);
        XAxis xAxis=weekChart.getXAxis();
        String[] date={"11/1","11/2","11/3","11/4","11/5","11/6","11/7","11/8","11/9","11/10"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(date));
        xAxis.setAxisLineWidth(10);
        xAxis.setTextSize(15);
        weekChart.setTouchEnabled(false);
        weekChart.setDrawValueAboveBar(false);
        weekChart.setFitBars(true);
        weekChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        weekChart.invalidate();


        //단어 학습하기 버튼 이벤트 처리하는 곳
        Button learningBtn=findViewById(R.id.learning_btn);
        learningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("go to learning page");
            }
        });


        /*---------------------여기서부터 NavigationView code---------------------*/
        // drawer icon 보이게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer);// drawerLayout
        NavigationView navigationView = findViewById(R.id.main_drawer_view);//navigationView

        /* to tie together the functionality of DrawerLayout and the framework ActionBar
        * 상단 왼쪽 네비게이션뷰 버튼 눌렀을때 이벤트 처리하는 코드
        * */
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) /*{
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
         }*/;

        // Set the drawer toggle as the DrawerListener.
        drawerLayout.addDrawerListener(toggle); // If omitted, the toggle icon is not changed.


        //로그아웃 클릭시 보여줄 dialog + 이벤트처리/
        DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog==alertDialog && which==DialogInterface.BUTTON_POSITIVE)
                    showToast("로그아웃 하셨습니다.");
            }
        };

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            /*네비게이션 뷰를 클릭 했을 때*/
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id;
                id = item.getItemId();
                switch (id) {
                    case R.id.menu_drawer_editAccountInformation:
                        Intent intent1=new Intent(MainActivity.this, EditAccountActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menu_drawer_notice:
                        Intent intent2=new Intent(MainActivity.this, NoticeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menu_drawer_goalSetting:
                        Intent intent3=new Intent(MainActivity.this, GoalSettingActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.menu_drawer_darkMode:
                        Intent intent4=new Intent(MainActivity.this, DarkModeActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.menu_drawer_pushAlert:
                        Intent intent5=new Intent(MainActivity.this, PushAlertActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.menu_drawer_logout:
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setTitle("알림");
                        builder.setMessage("로그아웃 하시겠습니까?");
                        builder.setPositiveButton("OK",dialogListener);
                        builder.setNegativeButton("No",null);
                        alertDialog=builder.create();
                        alertDialog.show();
                        break;
                }
                return true;
            }
        });

        //다크모드 이벤트처리
        darkModeSwitch=(SwitchCompat) navigationView.getMenu().findItem(R.id.menu_drawer_darkMode).getActionView().findViewById(R.id.switch_dark_mode);
        if(themeColor.equals(DarkModeUtil.DARK_MODE))
            darkModeSwitch.setChecked(true);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    showToast("다크모드 off");
                    themeColor=DarkModeUtil.LIGHT_MODE;
                    DarkModeUtil.applyTheme(themeColor);
                    DarkModeUtil.modSave(getApplicationContext(),themeColor);
                }
                else {
                    showToast("다크모드 on");
                    themeColor=DarkModeUtil.DARK_MODE;
                    DarkModeUtil.applyTheme(themeColor);
                    DarkModeUtil.modSave(getApplicationContext(),themeColor);
                }
            }
        });
        //푸쉬알림 이벤트처리
        pushAlertSwitch=(SwitchCompat) navigationView.getMenu().findItem(R.id.menu_drawer_pushAlert).getActionView().findViewById(R.id.switch_push_alert);
        pushAlertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                    showToast("푸쉬알림 off");
                else showToast("푸쉬알림 on");
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    // Whenever the Up button is clicked, this method is called
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Pass the event to ActionBarDrawerToggle,
// if it returns true, then it has handled the Up button touch event.

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
// Handle other action bar items.
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_SHORT);
        toast.show();
    }
}