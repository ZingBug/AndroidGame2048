package com.zingbug.game2048;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.zingbug.game2048.Utils.AnimLayer;
import com.zingbug.game2048.Utils.Card;
import com.zingbug.game2048.Utils.GameView;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.id;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView Restart;
    private TextView Score;
    private TextView Best;
    private int score=0;//当前分数
    private int best=0;//最高分数
    private AnimLayer animLayer = null;
    private GameView gameView=null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Card[][] cardsMapSave=new Card[4][4];
    private boolean IsRestore=false;

    //获取当前类实例
    private static MainActivity mainActivity=null;

    public MainActivity(){  //可以在外界访问MainActivity的实例
        mainActivity=this;  //MainActivity一旦被构建，就给mainActivity静态变量赋值，就可以从外界进行访问它
    }
    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Restart=(TextView)findViewById(R.id.restart);
        Score=(TextView)findViewById(R.id.score);
        Best=(TextView)findViewById(R.id.best);

        animLayer = (AnimLayer) findViewById(R.id.animLayer);
        gameView=(GameView) findViewById(R.id.gameView);

        Score.setText("0");

        Restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                switch(v.getId())
                {
                    case R.id.restart:
                    {
                        //重新打开游戏
                        gameView.startGame();
                        break;
                    }
                    default:break;
                }
            }
        });

        pref=getSharedPreferences("BestScore",MODE_PRIVATE);
        editor=pref.edit();
        best=pref.getInt("Best",0);
        score=pref.getInt("Score",0);
        String cards=pref.getString("CardsMap","0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
        String[] cardsArray=cards.split(",");
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int num=Integer.parseInt(cardsArray[i*4+j]);
                gameView.cardsMap[i][j].setNum(num);
                if(num>0)
                {
                    IsRestore=true;
                }
            }
        }
        showBest();
        showScore();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        best=pref.getInt("Best",0);
        showBest();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //cardsMapSave=gameView.cardsMap;
        StringBuilder cards=new StringBuilder();
        for(int i=0;i<4;++i)
        {
            for(int j=0;j<4;j++)
            {
                cards.append(gameView.cardsMap[i][j].getNum()+",");
            }
        }
        editor.putString("CardsMap",cards.toString());
        editor.putInt("Score",score);
        editor.apply();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        StringBuilder cards=new StringBuilder();
        for(int i=0;i<4;++i)
        {
            for(int j=0;j<4;j++)
            {
                cards.append(gameView.cardsMap[i][j].getNum()+",");
            }
        }
        editor.putString("CardsMap",cards.toString());
        editor.putInt("Score",score);
        editor.apply();
    }
    public boolean getIsRestore()
    {
        return IsRestore;
    }
    public void clearScore()
    {
        score=0;
        showScore();
    }
    public void showScore()
    {
        Score.setText(score+"");
    }
    public void addScore(int s)
    {
        score+=s;
        showScore();
        if(score>best)
        {
            best=score;
            setBest();
        }
    }
    public void setBest()
    {
        editor.putInt("Best",best);
        editor.apply();
        showBest();
    }
    public void showBest()
    {
        Best.setText(best+"");
    }
    public AnimLayer getAnimLayer() {
        return animLayer;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
