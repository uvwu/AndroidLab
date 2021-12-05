package com.example.voca.List;

// 제공해주는 단어장 목록(수능->week1->단어장 목록) 내에서 쓰일 아이템들
public class VocaDetailProvidedItem {
    private String eng;
    private String kor;

    public VocaDetailProvidedItem(){}

    public VocaDetailProvidedItem(String eng, String kor){
        this.eng = eng;
        this.kor = kor;
    }
    public String getEng() {
        return eng;
    }
    public String getKor() {
        return kor;
    }

    public void setEng(String eng) {
        this.eng = eng;

    }
    public void setKor(String kor) {
        this.kor = kor;

    }
}
