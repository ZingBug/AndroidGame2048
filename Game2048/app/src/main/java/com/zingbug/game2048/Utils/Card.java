package com.zingbug.game2048.Utils;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zingbug.game2048.R;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

/**
 * Created by LZH on 2017/9/6.
 */

public class Card extends FrameLayout {
    private int num=0;
    private TextView label;

    public Card(Context context) {
        super(context);
        label=new TextView(getContext());
        label.setTextSize(32);

        label.setBackground(getResources().getDrawable(R.drawable.flatcard));//设置圆角卡片

        label.setGravity(Gravity.CENTER); //卡片在中心显示

        LayoutParams lp=new LayoutParams(FILL_PARENT,FILL_PARENT); // （-1，-1）表示填充满整个父级容器
        lp.setMargins(10,10,10,10); //设置每个卡片的间隔
        this.addView(label,lp);
        setNum(0);
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
        if(num<=0){
            label.setText("");
        }else{
            label.setText(num+"");
        }
        switch (num) {
            case 0:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard0));//设置圆角卡片
                break;
            case 2:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard2));//设置圆角卡片
                break;
            case 4:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard4));//设置圆角卡片
                break;
            case 8:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard8));//设置圆角卡片
                break;
            case 16:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard16));//设置圆角卡片
                break;
            case 32:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard32));//设置圆角卡片
                break;
            case 64:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard64));//设置圆角卡片
                break;
            case 128:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard128));//设置圆角卡片
                break;
            case 256:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard256));//设置圆角卡片
                break;
            case 512:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard512));//设置圆角卡片
                break;
            case 1024:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard1024));//设置圆角卡片
                break;
            case 2048:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard2048));//设置圆角卡片
                break;
            default:
                label.setBackground(getResources().getDrawable(R.drawable.flatcard));//设置圆角卡片
                break;
        }
    }
    public boolean equals(Card o) {
        return getNum()==o.getNum();
    }
    public TextView getLabel() {
        return label;
    }
    protected Card clone(){
        Card c= new Card(getContext());
        c.setNum(getNum());
        return c;
    }
}
