// 릴리즈 버전의 인증서 필요하다면 해야함 - 정현
// TODO: 한글 번역판 구현

package com.example.voca.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.MainActivity;
import com.example.voca.R;
import com.example.voca.realtimeDB.Today;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FirebaseUI extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "FirebaseUI";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference; // 데이터베이스의 주소 저장
    private DatabaseReference mDatabase;

    ArrayList<String> userUID; // 유저의 로그인 토큰
    String uid;

    ValueEventListener mValueEventListener; // 리스너 선언 -> 경로의 전체 내용을 읽고 변경사항을 수신 대기

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

        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("users"); // 현재 로그인하는 사용자의 정보가 저장될 공간
        Log.d(TAG, "users" + mDatabaseReference);

        // 시작하기 버튼 클릭시 firebaseUI(로그인) 화면으로 넘어감
        Button firebase_ui = (Button)findViewById(R.id.firebase_ui_btn);
        firebase_ui.setOnClickListener(this);

        userUID = new ArrayList<>();

        // 리스너 설정 및 연결 -> DB/users
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            // 이벤트 발생 시점에 특정 경로에 있던 콘텐츠의 정적 스냅샷을 읽음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "addValueEventListener: getChildren() " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    uid = snapshot.getKey().toString(); // DB상에서 User내에 저장되어 있는 User의 토큰들 저장
                    userUID.add(uid);

                    Log.d(TAG, "addValueEventListener: Key " + uid);
                    Log.d(TAG, "add item -> size:" + userUID.size());
                }
            }

            // 실패시 호출
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                String uid = user.getUid();
                Log.d(TAG, "user: " + user);
                Log.d(TAG, "user: " + uid);
                if (user == null) // 현재 사용자의 정보가 없으면 back 버튼을 누른 것과 동일한 효과 발생
                {
                    finish();
                    return;
                } else {
                    //String fixUser = user.toString().substring(34);
                    //Log.d(TAG, "fixUser: " + fixUser);
                    settingDB(uid); // DB에 현재 사용자 정보 저장

                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }

        else {
            Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_LONG).show();
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
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

    // DB setting
    private void settingDB(String user)
    {
        boolean isExist = false; // DB내 user 정보 존재 유무

        // DB에 사용자 정보 저장
        Log.d(TAG, "userID: " + userUID);
        if(userUID != null) {
            for (String userUID : userUID) {   // 기존 회원이면 DB에 값 저장 안함 -> 기존 정보에 계속해서 저장
                if (userUID.equals(user)) {
                    isExist = true;
                    break;
                }
            }
        }
        Log.d(TAG, "TF: " + isExist);

        // 신규회원이면 DB에 사용자 토큰 저장
        if(!isExist)
        {
            // 현재 루트: DB/users
            mDatabaseReference.setValue(user);
            Log.d(TAG, "mDatabase: " + mDatabaseReference);

            mDatabase = mDatabaseReference.child(user); // 현재 루트를 DB/users/user 로 변경
            mDatabase.setValue("goals");
            mDatabase.setValue("userVoca");
            Log.d(TAG, "mDatabase: " + mDatabase);

            mDatabase = mDatabase.child("goals"); // 현재 루트를 DB/users/user/goals 로 변경
            String today = getToday();
            mDatabase.setValue(today);
            Log.d(TAG, "mDatabase: " + mDatabase);

            mDatabase = mDatabase.child(today); // 현재 루트를 DB/users/user/goals/today 로 변경
            Today todayGoal = new Today(-1, 0); // goal이 -1인 상태:목표 설정 안한 상태 (디폴트값)
            mDatabase.updateChildren(todayGoal.toMap());
            Log.d(TAG, "mDatabase: " + mDatabase);

            mDatabase = mDatabaseReference.child(user).child("userVoca"); // 현재 루트를 DB/users/user/userVoca 로 변경
            mDatabase.setValue("stars");
            Log.d(TAG, "mDatabase: " + mDatabase);
        }

    }
}

