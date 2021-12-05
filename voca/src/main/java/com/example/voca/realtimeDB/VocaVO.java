package com.example.voca.realtimeDB;

import android.os.Parcel;
import android.os.Parcelable;

public class VocaVO implements Comparable<VocaVO> , Parcelable {
    public String vocaEng;
    public String vocaKor;
    public Boolean memoCheck;
    public Boolean starCheck;

    public VocaVO(){
        vocaEng=null;
        vocaKor=null;
        memoCheck=false;
        starCheck=false;
    }

    public VocaVO(String eng,String kor,Boolean memo,Boolean star){
        this.vocaEng=eng;
        this.memoCheck=memo;
        this.vocaKor=kor;
        this.starCheck=star;
    }

    protected VocaVO(Parcel in) {
        vocaEng = in.readString();
        vocaKor = in.readString();
        byte tmpMemoCheck = in.readByte();
        memoCheck = tmpMemoCheck == 0 ? null : tmpMemoCheck == 1;
        byte tmpStarCheck = in.readByte();
        starCheck = tmpStarCheck == 0 ? null : tmpStarCheck == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vocaEng);
        dest.writeString(vocaKor);
        dest.writeByte((byte) (memoCheck == null ? 0 : memoCheck ? 1 : 2));
        dest.writeByte((byte) (starCheck == null ? 0 : starCheck ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VocaVO> CREATOR = new Creator<VocaVO>() {
        @Override
        public VocaVO createFromParcel(Parcel in) {
            return new VocaVO(in);
        }

        @Override
        public VocaVO[] newArray(int size) {
            return new VocaVO[size];
        }
    };

    @Override
    public int compareTo(VocaVO o) {
        return this.vocaEng.compareTo(o.vocaEng);
    }
}
