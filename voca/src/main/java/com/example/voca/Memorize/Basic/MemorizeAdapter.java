package com.example.voca.Memorize.Basic;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;
import com.example.voca.realtimeDB.VocaVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MemorizeAdapter extends RecyclerView.Adapter<MemorizeViewHolderPage> {
    String today;
    DatabaseReference mDatabaseReference;

    // 실시간 사용자의 정보 받아옴
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    int count;
    String title;

    private ArrayList<VocaVO> listVoca;
    TextToSpeech tts;

    MemorizeAdapter(ArrayList<VocaVO> data,TextToSpeech tts, String title){
        this.listVoca=data;
        this.tts=tts;

        this.title =title;
    }

    @NonNull
    @Override
    public MemorizeViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_memorize_viewpager,parent,false);
        getCount();
        return new MemorizeViewHolderPage(view, count, title);
    }

    @Override
    public void onBindViewHolder(@NonNull MemorizeViewHolderPage holder, int position){
        if(holder instanceof MemorizeViewHolderPage){
            MemorizeViewHolderPage viewHolderPage=(MemorizeViewHolderPage) holder;
            viewHolderPage.onBind(listVoca.get(position),getItemCount(), listVoca.size());
            viewHolderPage.ttsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts.speak(viewHolderPage.vocaEng.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    // 달성률 얻어오기
    private void getCount()
    {
        today = getToday();

        mDatabaseReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("goals")
                .child(today)
                .child("count");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
