package com.example.voca.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignOut extends AppCompatActivity {
    private Context mContext;

    public SignOut(Context context)
    {
        mContext = context;
    }

    public void signOut()
    {
        AuthUI.getInstance()
                .signOut(mContext)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(mContext,"로그아웃", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(mContext, FirebaseUI.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
    }
}
