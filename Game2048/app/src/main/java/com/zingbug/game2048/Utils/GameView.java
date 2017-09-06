package com.zingbug.game2048.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.zingbug.game2048.MainActivity;
import com.zingbug.game2048.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LZH on 2017/9/6.
 */

public class GameView extends GridLayout {

    public Card[][] cardsMap=new Card[4][4];
    private List<Point> emptyPoints=new ArrayList<Point>();

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {   //构造方法
        super(context, attrs, defStyleAttr);
        initGameView();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    private void initGameView(){      //初始化
        setColumnCount(4);//指定为4列
        setBackgroundColor(getResources().getColor(R.color.blackbrown));

        Config.CARD_WIDTH=GetCardWidth();
        addCards(Config.CARD_WIDTH,Config.CARD_WIDTH);//向布局中加入卡片

        setOnTouchListener(new OnTouchListener() {  //触摸监听
            private float startX,startY,offsetX,offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX=event.getX();
                        startY=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX=event.getX()-startX; //偏移量
                        offsetY=event.getY()-startY;

                        if(Math.abs(offsetX)>Math.abs(offsetY)){  //如果用户斜着滑动，X的偏移量大于Y的偏移量，说明用户为向X方向移动的意图
                            if(offsetX<-5){  //理论上是小于0，由于可能存在误差，故选择-5
                                swipeLeft();
                            }else if(offsetX>5){
                                swipeRight();
                            }
                        }else {
                            if (offsetY < -5) {
                                swipeUp();
                            }else if(offsetY>5){
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  //适应手机屏幕,设定卡片宽高
        //onSizeChange这个方法是在构造函数执行之后才会执行的，而这个时候布局已经完成了，所以无法在后续添加布局
        super.onSizeChanged(w, h, oldw, oldh);
        if(MainActivity.getMainActivity().getIsRestore())
        {
            restoreGame();
        }
        else
        {
            startGame();
        }
    }
    private void addCards(int cardWidth,int cardHeight){
        Card c;
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                c=new Card(getContext());
                c.setNum(0);
                this.addView(c,cardWidth,cardHeight);
                cardsMap[x][y]=c; //把16张卡片全部添加进这个二维数组中，方便使用
            }
        }
    }
    private int GetCardWidth()
    {
        //屏幕信息的对象
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();

        //获取屏幕信息
        int cardWidth;
        cardWidth = displayMetrics.widthPixels;

        //一行有四个卡片，每个卡片占屏幕的四分之一
        return ( cardWidth - 10 ) / 4;

    }
    public void startGame(){ //开始游戏模块
        MainActivity.getMainActivity().clearScore(); //分数清0

        for (int y=0;y<4;y++){
            for(int x=0;x<4;x++){
                cardsMap[x][y].setNum(0);  //清0
            }
        }
        addRandomNum();  //调用
        addRandomNum();
    }
    public void restoreGame()
    {
        //空
    }
    private void addRandomNum(){  //添加随机数模块
        emptyPoints.clear(); //清0
        for (int y=0;y<4;y++){
            for(int x=0;x<4;x++){
                if(cardsMap[x][y].getNum()<=0){  //只有当卡片上没有数字时才会在其上添加
                    emptyPoints.add(new Point(x,y));
                }
            }
        }
        if(emptyPoints.size()>0)
        {
            Point p=emptyPoints.remove((int)(Math.random()*emptyPoints.size())); //Math.random()随机获取0-1之间的数
            cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4); //2和4出现的概率为9:1
            MainActivity.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
        }


    }
    private void swipeLeft(){  //向左滑动模块
        boolean merge=false;

        for(int y=0;y<4;y++){   //向右遍历
            for(int x=0;x<4;x++){

                for(int x1=x+1;x1<4;x1++){
                    if(cardsMap[x1][y].getNum()>0){

                        if (cardsMap[x][y].getNum()<=0){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y],cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge=true;
                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){  //合并
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());  //记分的方式
                            merge=true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){   //判断是否合并，若合并了，则添加一个随机数
            addRandomNum();
            checkComplete();
        }

    }
    private void swipeRight(){
        boolean merge=false;

        for(int y=0;y<4;y++){   //向左遍历
            for(int x=3;x>=0;x--){

                for(int x1=x-1;x1>=0;x1--){
                    if(cardsMap[x1][y].getNum()>0){

                        if (cardsMap[x][y].getNum()<=0){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge=true;
                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge=true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeUp(){
        boolean merge=false;

        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){

                for(int y1=y+1;y1<4;y1++){
                    if(cardsMap[x][y1].getNum()>0){

                        if (cardsMap[x][y].getNum()<=0){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;
                            merge=true;
                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge=true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeDown(){
        boolean merge=false;

        for(int x=0;x<4;x++){
            for(int y=3;y>=0;y--){

                for(int y1=y-1;y1>=0;y1--){
                    if(cardsMap[x][y1].getNum()>0){

                        if (cardsMap[x][y].getNum()<=0){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge=true;
                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge=true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }
    private void checkComplete(){  //判断游戏是否结束模块，当16个卡片填满数字且相邻之间没有相同的数字，则认为游戏结束。
        boolean complete=true;

        ALL: //添加一个标签，其后为冒号
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                if(cardsMap[x][y].getNum()==0||
                        (x>0 && cardsMap[x][y].equals(cardsMap[x-1][y]))||
                        (x<3 && cardsMap[x][y].equals(cardsMap[x+1][y]))||
                        (y>0 && cardsMap[x][y].equals(cardsMap[x][y-1]))||
                        (y<3 && cardsMap[x][y].equals(cardsMap[x][y+1]))){
                    complete=false;
                    break ALL;  //使其跳出这个大循环

                }
            }
        }
        //游戏结束时弹窗
        if(complete){
            new AlertDialog.Builder(getContext()).setTitle("Hello！").setMessage("The game is over ！")
                    .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);  //直接退出游戏
                }
            }).show();
        }
    }

}
