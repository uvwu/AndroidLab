package com.example.voca.drawerActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.R;

public class goalSettingActivity extends AppCompatActivity {

    int goalNow;//현재 목표
    AlertDialog alertDialog;// 목표 수정시 띄울 다이얼로그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        goalNow=20;

        //현재 목표 보여주는 textView
        TextView goalNowText=findViewById(R.id.goal_now_text);
        goalNowText.setText("현재 목표 "+goalNow);


        EditText editGoalText=findViewById(R.id.goal_text);

        Button goalBtn=findViewById(R.id.goal_btn);//목표 수정 버튼

        //목표 수정시 보일 다이얼로그
        DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog==alertDialog && which==DialogInterface.BUTTON_POSITIVE){
                    goalNow=Integer.parseInt(String.valueOf(editGoalText.getText()));
                    goalNowText.setText("현재 목표: "+goalNow);
                    showToast("변경되었습니다.");
                }
                editGoalText.setText(null);
            }
        };

        goalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(goalSettingActivity.this);
                builder.setMessage("정말 변경하시겠습니까?");
                builder.setPositiveButton("OK",dialogListener);
                builder.setNegativeButton("No",dialogListener);
                alertDialog=builder.create();
                alertDialog.show();
            }
        });

    }
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_SHORT);
        toast.show();
    }
}