package com.example.voca.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// TODO: 탈퇴시 데이터베이스 내 데이터 모두 삭제 -> 정현
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

                            Intent intent = new Intent(mContext, FirebaseUI.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
    }
}
