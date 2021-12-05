package com.example.voca.drawerActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.R;
import com.example.voca.authentication.DeleteUserInfo;
import com.example.voca.authentication.SignOut;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class EditAccountActivity extends AppCompatActivity {
    private static final String TAG = "EditAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        showUserProfile();

        //회원탈퇴 버튼
        Button accountDelBtn=findViewById(R.id.account_del_btn);

        //회원탈퇴 클릭시 보여줄 dialog + 이벤트처리/
        DeleteUserInfo deleteUserInfo = new DeleteUserInfo(this);
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if(dialog==alertDialog && which==DialogInterface.BUTTON_POSITIVE)
                deleteUserInfo.delete();
            }
        };

        accountDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(EditAccountActivity.this);
                builder.setMessage("정말 탈퇴하시겠습니까?");
                builder.setPositiveButton("OK", dialogListener);
                builder.setNegativeButton("No", null);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }
    private void showUserProfile()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 현재 사용자 정보

        Log.d(TAG, "user: " + user);
        Log.d(TAG, "userName: " + user.getDisplayName());
        // 사용자 e-mail 정보 얻어와서 화면에 보여줌
        TextView email_txt = (TextView)findViewById(R.id.user_email);
        email_txt.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());

        // 사용자 로그인 계정 이름 정보 얻어와서 화면에 보여줌
        TextView name_txt = (TextView)findViewById(R.id.user_display_name);
        name_txt.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());

        // 사용자가 어떤 프로바이더(구글, 페이스북, 이메일)를 이용하여 로그인하였는지 화면에 보여줌
        StringBuilder providerList = new StringBuilder(100); // 모든 로그인 프로바이더들의 길이는 100이 넘지 않음
        if(user.getProviderData() == null || user.getProviderData().isEmpty())
        {
            providerList.append("None");
        }
        else
        {
            for(com.google.firebase.auth.UserInfo profile : user.getProviderData())
            {
                String providerID = profile.getProviderId();
                switch (providerID) {
                    case GoogleAuthProvider.PROVIDER_ID: // 구글을 통해 로그인을 한 경우
                        providerList.append("Google");
                        break;
                    case FacebookAuthProvider.PROVIDER_ID: // 페이스북 통해 로그인을 한 경우
                        providerList.append("Facebook");
                        break;
                    case EmailAuthProvider.PROVIDER_ID: // 이메일을 통해 로그인을 한 경우
                        providerList.append("Email");
                        break;
                    default:
                        providerList.append(providerID); // 그 외 기타 (실질적으로는 발생하지 않음)
                        break;
                }
            }
        }
        TextView provider_txt = (TextView)findViewById(R.id.user_enabled_providers);
        provider_txt.setText(providerList); // 위에서 만든 문장 셋팅
    }
}