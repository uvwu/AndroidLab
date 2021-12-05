/*
package com.example.voca.realtimeDB;


import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voca.List.VocaDetailItem;
import com.example.voca.List.VocaDetailProvidedAdapter;
import com.example.voca.List.VocaDetailProvidedItem;
import com.example.voca.List.VocaListItem;
import com.example.voca.List.VocaListSubAdapter;
import com.example.voca.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// ValueEventListener: 전체 데이터 목록이 단일 DataSnapshot으로 반환되며 이를 루프 처리하여 개별 하위 요소에 액세스 가능
// ChildEventListener: 한 번의 작업으로 목록의 모든 하위 요소를 가져오려는 경우

// 제공해주는 단어를 읽어오기 위한 클래스
public class ReadData_providedVoca {
    private static final String TAG = "ReadData_providedVoca";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference; // 데이터베이스의 주소 저장
    private DatabaseReference mDatabase;
    ValueEventListener mValueEventListener; // 리스너 선언

    ArrayList<VocaDetailProvidedItem> items = new ArrayList<>(); // 받아온 영어, 한글 담기 위함

    // name: 수능,토익과 같은 단어장 이름, subName
    public ReadData_providedVoca(String name, String subName) {
        firebaseDatabase = FirebaseDatabase.getInstance(); // 실시간 DB 관리 객체 얻어오기
        mDatabaseReference = firebaseDatabase.getReference("vocavulary"); // 저장시킬 노드 참조객체 가져오기 -> vocavulary
        mDatabase = mDatabaseReference.child(name).child(subName); // 불러올 데이터베이스의 주소


        Log.d(TAG, "ReadData_ProvidedVoca: " + mDatabase );

                                                     // 리스너: 별도의 읽어오기 버튼 없음 -> DB 변경이 발생하면 이에 반응하는 리스너를 통해 DB 읽어옴
                                                     // 리스너 연결
        Log.d(TAG, "addValueEventListener");

                                                     // onDataChange의 방법을 즉시 그 방법을 수행 한 후에 중단
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                             Log.d(TAG, "addValueEventListener: for");
                                                             for (DataSnapshot snapshot: dataSnapshot.getChildren()) { // Eng 값이 각각의 직계 자손으로 인식
                                                                 String Eng = (String)snapshot.getKey();
                                                                 String Kor = snapshot.getValue(String.class);

                                                                 VocaDetailProvidedItem item = new VocaDetailProvidedItem(Eng, Kor);
                                                                 items.add(item);
                                                                 Log.d(TAG, "add item"); //
                                                             }
                                                         }

                                                         @Override
                                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                                         }
        });

        /*
        // 데이터 수신 위한 리스너
        ChildEventListener mChildEventListener = new ChildEventListener() {
            // 데이터가 추가되었을 때
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded start");
                // dataSnapshot: 위의 메소드가 호출 될 때의 data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) // 반복문으로 데이터 리스트 추출해냄, eng 가리킴
                {
                    Log.d(TAG, "onChildAdded:" + (String)snapshot.getKey() + "," + snapshot.getValue(String.class));
                    String Eng = (String)snapshot.getKey();
                    String Kor = snapshot.getValue(String.class);

                    VocaDetailProvidedItem item = new VocaDetailProvidedItem(Eng, Kor);
                    items.add(item);
                }
            }

            // 데이터가 변화되었을 때
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

            }

            // 데이터가 제거되었을 때
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            // 데이터가 파이어베이스 DB 리스트 위치 변경되었을 때
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

            }

            // DB처리 중 오류가 발생했을 때
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addChildEventListener(mChildEventListener); // 리스너 등록

    }

    // 생성자에서 받아온 단어 목록 리턴
    public ArrayList<VocaDetailProvidedItem> vocaDetailProvidedItems()
    {
        Log.d(TAG, "vocaDetailProvidedItems()" + items.size());
        return items;
    }
}

 */
