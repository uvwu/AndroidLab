package com.example.voca.List;

import java.io.Serializable;

// 수능/토익/토플 등 list 보여주는 화면과
// Week1/Week2 등 subList 보여주는 화면의 구성 동일하기 때문에(제목만 보여주면 됌) 이 클래스 같이 사용
// Serializable -> 직렬화
public class VocaListItem implements Serializable {
    public String name;

    public VocaListItem(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
//extends RecyclerView.ViewHolder