package com.example.voca.drawerActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.R;
import com.example.voca.realtimeDB.GoalData;
import com.example.voca.realtimeDB.Today;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.voca.RecordUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GoalSettingActivity extends AppCompatActivity {
    private static final String TAG = "GoalSettingActivity";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference; // 데이터베이스의 주소 저장
    private DatabaseReference mDatabase;

    ArrayList<GoalData> goalData; // 날짜,목표,목표달성률 넣을 데이터

    String todayDate;
    String todayGoal;
    String todayCount;

    AlertDialog alertDialog;// 목표 수정시 띄울 다이얼로그

    TextView goalNowText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid(); // 현재 로그인한 사용자의 정보
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("users").child(uid).child("goals"); // DB/users/userUID/goals
        Log.d(TAG, "users" + mDatabaseReference);




        goalData = new ArrayList<>();
        GoalData data = new GoalData();

        // 리스너 설정 및 연결
        // 그 동안의 목표와 목표달성률을 goalData ArrayList에 다 저장함
        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            // 이벤트 발생 시점에 특정 경로에 있던 콘텐츠의 정적 스냅샷을 읽음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot_data : dataSnapshot.getChildren()) { // date
                    int i = 0;
                    data.setDate(snapshot_data.getKey());
                    // Log.d(TAG, "onDataChange: data " + );

                    for (DataSnapshot snapshot : snapshot_data.getChildren()) {
                        switch (i) {
                            case 0:
                                data.setCount(snapshot.getValue().toString());
                                break;

                            case 1:
                                data.setGoal(snapshot.getValue().toString());
                                break;
                        }
                        i++;
                    }

                    goalData.add(data);
                    Log.d(TAG, "onDataChange: goalData " + goalData.get(0).getGoal());
                    Log.d(TAG, "onDataChange: goalData " + goalData.get(0).getCount());
                    Log.d(TAG, "onDataChange: goalData " + goalData.get(0).getDate());
                }

                // 실시간데이터베이스 특성상 실시간데이터베이스가 바뀌면 화면도 바뀔 부분은 이곳에서도 한 번 더 써줘야함
                edit_goal();  // 화면에 보일 현재 목표 및 목표 수정 화면 (오늘의 날짜,목표,달성률 구함)
            }

            // 실패시 호출
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            todayGoal= String.valueOf(RecordUtil.loadGoal(this));
            //현재 목표 보여주는 textView
            goalNowText = findViewById(R.id.goal_now_text);

            EditText editGoalText = findViewById(R.id.goal_text);

            Button goalBtn = findViewById(R.id.goal_btn);//목표 수정 버튼

            //목표 수정시 보일 다이얼로그
            DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(dialog==alertDialog && which==DialogInterface.BUTTON_POSITIVE){
                        todayGoal=String.valueOf(editGoalText.getText());
                        goalNowText.setText("현재 목표: "+todayGoal);
                        RecordUtil.saveGoal(getApplicationContext(),Integer.parseInt(todayGoal));
                        mDatabaseReference.child(todayDate).child("goal").setValue(Integer.parseInt(todayGoal)); // DB에 저장
                        showToast("변경되었습니다.");
                    }
                    editGoalText.setText(null);
                }
            };

            goalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalSettingActivity.this);
                    builder.setMessage("정말 변경하시겠습니까?");
                    builder.setPositiveButton("OK", dialogListener);
                    builder.setNegativeButton("No", dialogListener);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            });


    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_SHORT);
        toast.show();
    }

    // 오늘 날짜 계산
    private String getToday()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        String today = sdf.format(date);
        Log.d(TAG, "today: " + today);

        return today;
    }

    // 화면에 보일 현재 목표 및 목표 수정 화면
    private void edit_goal()
    {
        todayDate = getToday(); // 오늘의 날짜
        Log.d(TAG, "today2: " + todayDate);


        for (GoalData goal : goalData) {
            Log.d(TAG, "date: " + goal);
            if (todayDate.equals(goal.getDate())) {
                todayCount = goal.getCount(); // 오늘의 목표달성률
                todayGoal = goal.getGoal(); // 오늘의 목표
            }
        }
        goalNowText.setText("현재 목표 " + todayGoal); // 실시간데이터베이스 특성상 text변경은 이곳에서도 있어야함
    }

}

