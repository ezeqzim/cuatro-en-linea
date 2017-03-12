package com.example.ezeqzim.cuatro_en_linea.FrontEnd;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonCoordinates extends Button {
    private int row, col, size;

    public ButtonCoordinates(Context context, int row, int col, int size) {
        super(context);
        this.row = row;
        this.col = col;
        this.size = size;
    }

    public int getCol() {
        return col;
    }

    public void setBackground(int background) {
        this.setBackgroundResource(background);
    }

    public void setLayoutParams(int margin){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size - 2*margin, size - 2*margin);
        params.setMargins(margin, margin, margin, margin);
        super.setLayoutParams(params);
    }
}
