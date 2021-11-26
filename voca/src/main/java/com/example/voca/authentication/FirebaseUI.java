// 릴리즈 버전의 인증서 필요하다면 해야함 - 정현
// TODO: 한글 번역판 구현

package com.example.voca.authentication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.voca.MainActivity;
import com.example.voca.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FirebaseUI extends AppCompatActivity implements View.OnClickListener{
    // FirebaseUI 활동 결과 계약의 콜백을 등록
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide(); // 타이틀바 숨기기
        setContentView(R.layout.activity_firebase_ui);

        // 시작하기 버튼 클릭시 firebaseUI(로그인) 화면으로 넘어감
        Button firebase_ui = (Button)findViewById(R.id.firebase_ui_btn);
        firebase_ui.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        createSignInIntent();
    }

    /* 원하는 로그인 방법으로 로그인 인텐트를 만듦 (google, facebook, email) */
    public void createSignInIntent() {
        // 인증을 제공해주는 리스트들
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        // 로그인 인텐트 작성 및 실행
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.theme_firebaseUI) // 로그인 테마 설정
                // TODO: 앱 아이콘 모양으로 로고 다시 설정
                .setLogo(R.mipmap.ic_launcher) // 로그인 시 맨 위에 나타나는 로고 설정 (임시로 설정)
                .setAvailableProviders(providers) // provider(인증을 제공해주는 리스트들) 설정
                // TODO: 서비스 약관(tosUrl) 및 개인정보처리방침(privacyPolicyUrl) 사이트 설정
                .setTosAndPrivacyPolicyUrls("https://naver.com", "https://google.com") // 개인정보처리방침 및 서비스 약관 설정
                .setIsSmartLockEnabled(true) // smartLock 설정
                .build();
        signInLauncher.launch(signInIntent);
    }

    /* 로그인 과정이 완료되면 onSignInResult로 결과가 수신 */
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse(); // firebase로부터 반환된 IdpResponse type
        if (result.getResultCode() == RESULT_OK) { // Activity 결과 코드를 받음 (int type)
            // 성공적으로 로그인 된 경우 -> 홈화면(MainActivity) 실행
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 현재 클라이언트의 정보
            if(user == null) // 현재 사용자의 정보가 없으면 back 버튼을 누른 것과 동일한 효과 발생
            {
                finish();
                return;
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                // TODO: 성공 토스트
                // ...
            }
        }
        else {
            // TODO: 실패 토스트
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}