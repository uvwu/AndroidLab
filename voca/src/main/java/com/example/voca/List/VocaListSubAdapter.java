package com.example.voca.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voca.R;

import java.util.ArrayList;

// vocaSearchActivity에서 아이템 누른 후 변경되는 화면 관리위한 어뎁터
public class VocaListSubAdapter extends RecyclerView.Adapter<VocaListSubAdapter.MyViewHolder>  implements ItemTouchHelperListener {

    Context context;
    int resId;
    ArrayList<VocaListItem> items;

    public VocaListSubAdapter(Context context,int resId, ArrayList<VocaListItem> items) {
        this.context = context;
        this.items = items;
        this.resId = resId;
    }

    @NonNull
    @Override
    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vocasearch_sub_item, parent, false);
        VocaListSubAdapter.MyViewHolder vh = new VocaListSubAdapter.MyViewHolder(view);

        return vh;
    }

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VocaListItem item = items.get(position);

        holder.nameView.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    } // 데이터 전체 개수 리턴

    @Override
    public boolean onItemMove(int form_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    // 아이템 뷰를 저장하는 뷰 홀더 클래스
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            // 뷰 객체에 대한 참조
            context = itemView.getContext();
            nameView = itemView.findViewById(R.id.voca_search_sub_item);

            // 각 아이템 뷰들이 클릭되었을 때 이벤트 처리
            itemView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,position);
                        }
                    }
                }
            });
        }

    }


}
