package com.example.voca.VocaList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.voca.List.ItemTouchHelperListener;
import com.example.voca.List.TabListItem;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// TODO: 스와이프시 각 단어 삭제
// TODO: 지금 체크 데이터가 DB에는 저장이 되지만 해당 단어장을 다시 클릭하면 초기화됨
public class VocaListAdapter extends ArrayAdapter<VocaVO> implements ItemTouchHelperListener {
    // 파이어베이스에서 데이터 저장 및 읽어올 부분 연결할 객체들 생성
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;

    // 실시간 사용자의 정보 받아옴
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    Context context;
    int resId;
    ArrayList<VocaVO> data;
    String title; // 선택된 단어장 이름
    int count;

    String today = getToday();

    public VocaListAdapter(Context context, int resId, ArrayList<VocaVO> data, String title, int count){
        super(context, resId);
        this.context=context;
        this.resId=resId;
        this.data=data;
        this.title = title;
        this.count = count;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            VocaListHolder holder = new VocaListHolder(convertView);
            convertView.setTag(holder);
        }

        VocaListHolder holder=(VocaListHolder)convertView.getTag();

        CheckBox memoCheck=holder.memoCheck;
        TextView vocaEng= holder.vocaEng;
        TextView vocaKor= holder.vocaKor;
        CheckBox starCheck=holder.starCheck;

        VocaVO vo= data.get(position);


        //암기여부 버튼 이벤트 처리
        memoCheck.setChecked(vo.memoCheck);// memoCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vo.memoCheck=true;
                    // DB 내 memoCheck
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("memoCheck");
                    mDatabaseReference.setValue("true");

                    count++;
                    // DB 내 count(목표달성률)
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("goals")
                            .child(today)
                            .child("count");
                    mDatabaseReference.setValue(count);

                    Toast toast = Toast.makeText(context,vo.vocaEng+ " 암기 확인", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    vo.memoCheck=false;
                    // DB 내 memoCheck
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("memoCheck");
                    mDatabaseReference.setValue("false");

                    if(count > 0) {count--;}
                    // DB 내 count(목표달성률)
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("goals")
                            .child(today)
                            .child("count");
                    mDatabaseReference.setValue(count);

                    Toast toast = Toast.makeText(context,vo.vocaEng+ " 암기 취소", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        vocaEng.setText(vo.vocaEng);//영어단어 보여주기
        vocaKor.setText(vo.vocaKor);//영어단어 한글 뜻 보여주기


        //즐겨찾기 버튼 이벤트 처리
        starCheck.setChecked(vo.starCheck);// starCheck 값의 여부에 따라 화면에 체크 유무 보여줌
        starCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vo.starCheck=true;
                    // DB 내 starCheck
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("starCheck");
                    mDatabaseReference.setValue("true");

                    // 해당 단어 즐겨찾기 목록에 추가
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child("star");
                    mDatabaseReference.updateChildren(vo.toMap());

                    Toast toast = Toast.makeText(context,vo.vocaEng+ " 즐겨찾기 추가", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    vo.starCheck=false;
                    Toast toast = Toast.makeText(context, vo.vocaEng+" 즐겨찾기 삭제", Toast.LENGTH_SHORT);
                    toast.show();

                    // DB 내 starCheck
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vo.getVocaEng())
                            .child("starCheck");
                    mDatabaseReference.setValue("false");

                    // 해당 단어 즐겨찾기 목록에서 제거
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child("star")
                            .child(vo.getVocaEng());
                    mDatabaseReference.setValue(null);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean onItemMove(int form_position, int to_position) {
        return false;
    }

    @Override
    // TODO:스와이프시 삭제하겠냐는 다이얼로그 띄운 뒤, 사용자가 오케이하면 해당 단어 삭제
    public void onItemSwipe(int position) {
        // TODO: 이곳에 다이얼로그 코드 작성

        removeDB(position); // 해당하는 단어 DB에서 삭제
        data.remove(position);
    }

    private void removeDB(int position) {
        String removeVoca = data.get(position).getVocaEng();

        // 실시간 사용자의 정보 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference =
                firebaseDatabase.getReference("users")
                        .child(uid)
                        .child("userVoca")
                        .child(title)
                        .child(removeVoca);
        // 저장시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca/선택된단어장/삭제할단어

        mDatabaseReference.setValue(null);
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
