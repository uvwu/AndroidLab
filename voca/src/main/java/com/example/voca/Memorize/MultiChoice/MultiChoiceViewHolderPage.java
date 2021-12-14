package com.example.voca.Memorize.MultiChoice;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MultiChoiceViewHolderPage extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView vocaEng;
    private CheckBox memoCheck;
    private CheckBox starCheck;
    private TextView count;
    private Button chioceBtn1;
    private Button chioceBtn2;
    private Button chioceBtn3;
    private Button chioceBtn4;
    Button ttsBtn;

    int mCount;
    String title;

    String today = getToday();

    // 실시간 사용자의 정보 받아옴
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    private DatabaseReference mDatabase_count;
    private DatabaseReference mDatabase_star;


    ArrayList<String> multiChoiceRandom=new ArrayList<>();
    private String realAnswer;//정답인 한글 뜻

    VocaVO vo;


    public MultiChoiceViewHolderPage(View v, int mCount, String title, ArrayList<String> multiChoiceRandom) {
        super(v);
        vocaEng=v.findViewById(R.id.voca_multi);
        memoCheck=v.findViewById(R.id.memo_check_multi);
        starCheck=v.findViewById(R.id.star_btn_multi);
        count=v.findViewById(R.id.count_multi);
        ttsBtn=v.findViewById(R.id.tts_btn_multi);
        chioceBtn1=v.findViewById(R.id.btn1_multi);
        chioceBtn2=v.findViewById(R.id.btn2_multi);
        chioceBtn3=v.findViewById(R.id.btn3_multi);
        chioceBtn4=v.findViewById(R.id.btn4_multi);

        this.mCount = mCount;
        this.title = title;
        this.multiChoiceRandom=multiChoiceRandom;
    }
    public void onBind(VocaVO vo,int totalNum,int dataSize) {
        this.vo = vo;

        //암기버튼 이벤트 처리리
        memoCheck.setChecked(vo.getMemoCheck());// memoCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vo.setMemoCheck(true);
                    // DB 내 memoCheck
                    mDatabase_count = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("memoCheck");
                    mDatabase_count.setValue("true");

                    mCount++;
                    // DB 내 count(목표달성률)
                    mDatabase_count = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("goals")
                            .child(today)
                            .child("count");
                    mDatabase_count.setValue(mCount);

                }
                else {
                    vo.setMemoCheck(false);
                    // DB 내 memoCheck
                    mDatabase_count = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("memoCheck");
                    mDatabase_count.setValue("false");

                    if(mCount > 0) {mCount--;}
                    // DB 내 count(목표달성률)
                    mDatabase_count = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("goals")
                            .child(today)
                            .child("count");
                    mDatabase_count.setValue(mCount);
                }
            }
        });
        //즐겨찾기 버튼 이벤트 처리
        starCheck.setChecked(vo.getStarCheck());// starCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        starCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vo.setStarCheck(true);
                    // DB 내 starCheck
                    mDatabase_star = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("starCheck");
                    mDatabase_star.setValue("true");

                    // 해당 단어 즐겨찾기 목록에 추가
                    mDatabase_star = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child("star");
                    mDatabase_star.updateChildren(vo.toMap());
                } else {
                    vo.setStarCheck(false);
                    // DB 내 starCheck
                    mDatabase_star = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("starCheck");
                    mDatabase_star.setValue("false");

                    // 해당 단어 즐겨찾기 목록에서 제거
                    mDatabase_star = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child("star")
                            .child(vo.getVocaEng());
                    mDatabase_star.setValue(null);
                }
            }
        });

        // 현재단어/전체단어 ex) 3/20
        count.setText((getAdapterPosition()%dataSize + 1) + "/" +(dataSize) );
        vocaEng.setText(vo.getVocaEng());

        //객관식 버튼(1~4) 값 넣는곳
        // TODO: MutiChoiceAdapter 내에 있는 multiChoiceRandom 의 값 중 랜덤하게 뽑아 넣으면 될듯
        Collections.shuffle(Arrays.asList(multiChoiceRandom));

        ArrayList<String> multi4Random=new ArrayList<>();//객관식 4개
        int ran1=multiChoiceRandom.size()% (int) ((Math.random()*200));
        multi4Random.add(vo.getVocaKor());
        multi4Random.add(multiChoiceRandom.get(ran1));
        multi4Random.add(multiChoiceRandom.get(( ran1+3)%multiChoiceRandom.size()     ));
        multi4Random.add(multiChoiceRandom.get( (ran1+5) % multiChoiceRandom.size()  ));

        String temp;
        int ran2=(int)(Math.random()*4);
        temp=multi4Random.get(ran2);
        multi4Random.set(0,temp);
        multi4Random.set(ran2,vo.getVocaKor());


        realAnswer=vo.getVocaKor();//정답인 한글 뜻

        chioceBtn1.setText(multi4Random.get(0));
        chioceBtn1.setOnClickListener(this);

        chioceBtn2.setText(multi4Random.get(1));
        chioceBtn2.setOnClickListener(this);

        chioceBtn3.setText(multi4Random.get(2));
        chioceBtn3.setOnClickListener(this);

        chioceBtn4.setText(multi4Random.get(3));
        chioceBtn4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Button btn=(Button) v;
        if(btn.getText().toString().equals(realAnswer)){
            MediaPlayer mp=MediaPlayer.create(btn.getContext(), R.raw.o);
            mp.start();
            Toast.makeText(v.getContext(),"정답",Toast.LENGTH_SHORT).show();
        }
        else{
            MediaPlayer mp=MediaPlayer.create(btn.getContext(), R.raw.x);
            mp.start();
            Toast.makeText(v.getContext(),"오답",Toast.LENGTH_SHORT).show();
        }
    }

    // 오늘 날짜 계산
    private String getToday()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        String today = sdf.format(date);

        return today;
    }
}
