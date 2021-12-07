package com.example.voca;

import android.content.Context;
import android.content.SharedPreferences;
/*통계 기록 세이브하고 로드하는 곳
* goal:목표치 achievement 외운 단어수
* SharedPreference로 저장함.
* */
public class RecordUtil {
    //public static int goal=20;
    //public static int achievement=0;
    //목표치 저장하기
    public static void saveGoal(Context context,int goal){
        SharedPreferences sp;
        sp=context.getSharedPreferences("record",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("goal",goal);
        editor.commit();
    }
    //외운단어수 저장하기 12시에 저장해야하지만 아직 구현X
    public static void saveMemorizedVocaNum(Context context,int achievement){
        SharedPreferences sp;
        sp=context.getSharedPreferences("record",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("achievement",achievement);
        editor.commit();
    }
    //목표치 불러오기
    public static int loadGoal(Context context){
        SharedPreferences sp;
        sp= context.getSharedPreferences("record",context.MODE_PRIVATE);
        int goal=sp.getInt("goal",30);
        return goal;
    }
    public static int loadMemorizedVocaNum(Context context){
        SharedPreferences sp;
        sp= context.getSharedPreferences("record",context.MODE_PRIVATE);
        int memorizedVocaNum=sp.getInt("achievement",0);
        return memorizedVocaNum;
    }
}
