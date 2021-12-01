package com.example.voca;

public class VocaVO implements Comparable<VocaVO> {
    public String vocaEng;
    public String vocaKor;
    public Boolean memoCheck;
    public Boolean starCheck;

    public VocaVO(){}

    public VocaVO(String eng,String kor,Boolean memo,Boolean star){
        this.vocaEng=eng;
        this.memoCheck=memo;
        this.vocaKor=kor;
        this.starCheck=star;
    }

    @Override
    public int compareTo(VocaVO o) {
        return this.vocaEng.compareTo(o.vocaEng);
    }
}
