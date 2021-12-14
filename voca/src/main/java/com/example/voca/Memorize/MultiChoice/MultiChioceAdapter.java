package com.example.voca.Memorize.MultiChoice;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MultiChioceAdapter extends RecyclerView.Adapter<MultiChoiceViewHolderPage> {
    String today;
    DatabaseReference mDatabaseReference;

    // 실시간 사용자의 정보 받아옴
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    int count;
    String title;

    private ArrayList<VocaVO> listVoca;
    ArrayList<String> multiChoiceRandom=new ArrayList<>();
    TextToSpeech tts;

    MultiChioceAdapter(ArrayList<VocaVO> data, TextToSpeech tts, String title,ArrayList<String> multiChoiceRandom){
        this.listVoca=data;
        this.tts=tts;
        this.title =title;
        this.multiChoiceRandom=multiChoiceRandom;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_multichoice_viewpager,parent,false);
        getCount();
        Collections.shuffle(Arrays.asList(multiChoiceRandom));
        return new MultiChoiceViewHolderPage(view, count, title,multiChoiceRandom);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolderPage holder, int position) {
        if(holder instanceof MultiChoiceViewHolderPage){
            MultiChoiceViewHolderPage viewHolderPage=(MultiChoiceViewHolderPage) holder;
            viewHolderPage.onBind(listVoca.get(position% listVoca.size()),getItemCount(),listVoca.size());
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
