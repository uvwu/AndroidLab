package com.example.voca.VocaList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.List.ItemTouchHelperListener;
import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VocaRecyclerAdapter extends RecyclerView.Adapter<VocaRecyclerAdapter.ItemViewHolder>  implements ItemTouchHelperListener {
    private final static String TAG = "VocaRecyclerAdapter";

    // 파이어베이스에서 데이터 저장 및 읽어올 부분 연결할 객체들 생성
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;

    // 실시간 사용자의 정보 받아옴
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    AlertDialog alertDialog;

    Context context;
    int resId;
    ArrayList<VocaVO> vocaData; // 선택된 단어장 내부의 단어 정보 모음(영어단어,뜻,암기여부,즐찾여부)
    String title; // 선택된 단어장 이름
    int count; // 현재 사용자의 단어 암기 개수
    String today; // 오늘 날짜를 알아야 DB에서 오늘의 단어 암기 개수를 찾을 수 있음
    String vocaTitle;


    // VocaRecyclerAdapter 생성자
    public VocaRecyclerAdapter(Context context, int resId, ArrayList<VocaVO> vocaData, String title, int count, String today) {
        this.context=context;
        this.resId=resId;
        this.vocaData = vocaData;
        this.title = title;
        this.count = count;
        this.today = today;
    }

    @NonNull
    @Override
    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.voca_list_item, parent, false);
        VocaRecyclerAdapter.ItemViewHolder vh = new VocaRecyclerAdapter.ItemViewHolder(view);

        return vh;
    }

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }

    private OnItemClickListener mListener = null; //리스너 객체 참조 변수

    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @Override
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        if(title.equals("star")){
//            vocaTitle = vocaData.get(position).getVocaTitle();
//        }
//        Log.d(TAG, "vocaTitle: " + vocaTitle);

        final VocaVO vocaVO = vocaData.get(position);  // final로 선언해야 체크박스의 체크 상태값(T/F)이 바뀌지 않음

        // 먼저 체크박스의 리스너를 null로 초기화
        holder.memoCheck.setOnCheckedChangeListener(null);
        holder.starCheck.setOnCheckedChangeListener(null);

        holder.vocaEng.setText(vocaVO.getVocaEng());
        holder.vocaKor.setText(vocaVO.getVocaKor());
        holder.memoCheck.setChecked(vocaVO.getMemoCheck());
        holder.starCheck.setChecked(vocaVO.getStarCheck());

        // 암기여부의 상태값 변화를 알기 위해 리스너 부착
        holder.memoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            // 체크박스 상태가 바뀌었을때
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // 체크 상태
                if(isChecked)
                {
                    vocaVO.setMemoCheck(true);

//                    // 즐겨찾는 단어장 내에서 체크박스 상태가 바뀔 때 -> 원래 위치하는 단어장 내에서도 체크박스 상태가 변화해야함
//                    if(title.equals("star"))
//                    {
//                        mDatabaseReference = FirebaseDatabase.getInstance()
//                                .getReference("users")
//                                .child(uid)
//                                .child("userVoca
//                                .child(vocaTitle)
//                                .child(vocaVO.getVocaEng())
//                                .child("memoCheck");
//
//                        mDatabaseReference.setValue(true);
//                    }

                    // DB 내 memoCheck -> true로 변경
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vocaVO.getVocaEng())
                            .child("memoCheck");
                    mDatabaseReference.setValue("true");

                    count++;
                    // DB 내 count(목표달성률) -> 현재 개수에서 하나 증가
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("goals")
                            .child(today)
                            .child("count");
                    mDatabaseReference.setValue(count);

                    // 즐겨찾는 단어장에도 선택한 단어가 있다면 그 단어의 정보도 변경해줘야 함
                    if(vocaVO.getStarCheck()) {
                        // star에 있는 단어의 memoCheck -> true로 변경
                        mDatabaseReference = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(uid)
                                .child("userVoca")
                                .child("star")
                                .child(vocaVO.getVocaEng())
                                .child("memoCheck");
                        mDatabaseReference.setValue("true");
                    }
                }
                // 체크 해지 상태
                else
                {
                    vocaVO.setMemoCheck(false);

//                    // 즐겨찾는 단어장 내에서 체크박스 상태가 바뀔 때 -> 원래 위치하는 단어장 내에서도 체크박스 상태가 변화해야함
//                    if(title.equals("star"))
//                    {
//                        mDatabaseReference = FirebaseDatabase.getInstance()
//                                .getReference("users")
//                                .child(uid)
//                                .child("userVoca")
//                                .child(vocaTitle)
//                                .child(vocaVO.getVocaEng())
//                                .child("memoCheck");
//
//                        mDatabaseReference.setValue(false);
//                    }

                    // DB 내 memoCheck -> false로 변경
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vocaVO.getVocaEng())
                            .child("memoCheck");
                    mDatabaseReference.setValue("false");

                    if(count > 0) {count--;} // 오늘 단어 암기 개수가 하나도 없으면 더 이상 감소하지 않음

                    // DB 내 count(목표달성률)
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("goals")
                            .child(today)
                            .child("count");
                    mDatabaseReference.setValue(count);

                    // 즐겨찾는 단어장에도 선택한 단어가 있다면 그 단어의 정보도 변경해줘야 함
                    if(vocaVO.getStarCheck()) {
                        // star에 있는 단어의 memoCheck -> false로 변경
                        mDatabaseReference = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(uid)
                                .child("userVoca")
                                .child("star")
                                .child(vocaVO.getVocaEng())
                                .child("memoCheck");
                        mDatabaseReference.setValue("false");
                    }
                }
            }
        });

        // 즐찾여부의 상태값 변화를 알기 위해 리스너 부착
        holder.starCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            // 체크박스 상태가 바뀌었을때
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // 체크 상태
                if(isChecked)
                {
                    vocaVO.setStarCheck(true);

                    // DB 내 starCheck -> true로 변경
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vocaVO.getVocaEng())
                            .child("starCheck");
                    mDatabaseReference.setValue("true");

                    // 해당 단어 즐겨찾기 목록에 추가
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child("star");
                    mDatabaseReference.updateChildren(vocaVO.toMap());

                    // 즐겨찾는 단어장이 아닌 다른 단어장에서 즐겨찾는 단어장에 단어를 추가했을 때
                    if(!title.equals("star")) {
                        // 해당 단어의 원래 위치도 삽입 (-> starCheck가 발생한 단어장 위치)
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title);
                        mDatabaseReference.child(vocaVO.getVocaEng()).updateChildren(map);
                    }
                }
                // 체크 해지 상태
                else
                {
                    vocaVO.setStarCheck(false);

                    // DB 내 starCheck -> false로 변경
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(vocaVO.getVocaEng())
                            .child("starCheck");
                    mDatabaseReference.setValue("false");

                    // 해당 단어 즐겨찾기 목록에서 제거
                    mDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child("star")
                            .child(vocaVO.getVocaEng());
                    mDatabaseReference.setValue(null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vocaData.size(); // 데이터 전체 개수 리턴
    }

    @Override
    // 사용 x
    public boolean onItemMove(int form_position, int to_position) {
        return false;
    }

    @Override
    // TODO:스와이프시 삭제하겠냐는 다이얼로그 띄운 뒤, 사용자가 오케이하면 해당 단어 삭제
    public void onItemSwipe(int position) {
        // TODO: 이곳에 다이얼로그 코드 작성
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog ==alertDialog && which==DialogInterface.BUTTON_POSITIVE){
                    removeDB(position); // 해당하는 단어 DB에서 삭제
                    notifyItemRemoved(position);
                }
                Intent intent = ((Activity)context).getIntent();
                ((Activity)context).finish(); //현재 액티비티 종료 실시
                ((Activity)context).overridePendingTransition(0, 0); //효과 없애기
                ((Activity)context).startActivity(intent); //현재 액티비티 재실행 실시
                ((Activity)context).overridePendingTransition(0, 0); //효과 없애기
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("OK", dialogListener);
        builder.setNegativeButton("No", dialogListener);
        alertDialog = builder.create();
        alertDialog.show();
        // TODO: 즐겨찾는 단어장 내 단어들은 스와이프가 불가능하도록 설정 -> 즐겨찾는 단어장 내에서는 단어 삭제가 불가능하도록
        //removeDB(position); // 해당하는 단어 DB에서 삭제
        //notifyItemRemoved(position);

        // TODO: 화면 새로고침없이 바로 바뀐 단어장 상태 화면에 뿌리는 방법은 없을까
        // 현재 화면 새로고침
        //Intent intent = ((Activity)context).getIntent();
        //((Activity)context).finish(); //현재 액티비티 종료 실시
        //((Activity)context).overridePendingTransition(0, 0); //효과 없애기
        //((Activity)context).startActivity(intent); //현재 액티비티 재실행 실시
        //((Activity)context).overridePendingTransition(0, 0); //효과 없애기
    }

    // 아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView vocaEng;
        public TextView vocaKor;
        public CheckBox memoCheck;
        public CheckBox starCheck;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            context = itemView.getContext();
            vocaEng = itemView.findViewById(R.id.voca_list_item_voca_eng);
            vocaKor = itemView.findViewById(R.id.voca_list_item_voca_kor);
            memoCheck = itemView.findViewById(R.id.voca_list_item_memo_check);
            starCheck = itemView.findViewById(R.id.voca_list_item_star_check);

        }

    }

    // DB에서 단어 삭제
    private void removeDB(int position)
    {
        String removeVoca = vocaData.get(position).getVocaEng();

        // 즐겨찾는 단어장에도 추가되어 있는 단어이면 즐겨찾는 단어장 내에서도 삭제
        if(vocaData.get(position).getStarCheck()) {
            removeAtStar(removeVoca);
        }

        // 실시간 데이터베이스 연결
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("users")
                            .child(uid)
                            .child("userVoca")
                            .child(title)
                            .child(removeVoca); // 삭시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca/title/삭제할영단어

        mDatabaseReference.setValue(null);
    }

    private void removeAtStar(String removeVoca){
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("users")
                .child(uid)
                .child("userVoca")
                .child("star")
                .child(removeVoca); // 삭제시킬 노드 참조객체 가져오기 -> DB/users/uid/userVoca/star/삭제할영단어

        mDatabaseReference.setValue(null);
    }

}
