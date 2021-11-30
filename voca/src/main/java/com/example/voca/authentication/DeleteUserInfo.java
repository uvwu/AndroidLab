package com.example.voca.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DeleteUserInfo extends AppCompatActivity {
    private Context mContext;

    public DeleteUserInfo(Context context)
    {
        mContext = context;
    }

    public void delete()
    {
        AuthUI.getInstance()
                .delete(mContext)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(mContext,"회원 탈퇴", Toast.LENGTH_LONG).show();

                            // TODO: 종료되는지 로그인 화면으로 가는지 다시 확인
                            Intent intent = new Intent(mContext, FirebaseUI.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
    }
}
