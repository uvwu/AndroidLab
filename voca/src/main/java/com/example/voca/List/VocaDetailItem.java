package com.example.voca.List;

// 사용자 설장 단어장

public class VocaDetailItem {
    private String eng;
    private String kor;

    public VocaDetailItem(String eng, String kor){
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
