package com.example.voca.List;

public interface ItemTouchHelperListener {
    boolean onItemMove(int form_position, int to_position);
    void onItemSwipe(int position);
}
