package edu.skku.cs.pa3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity
    implements GameContract.ContractForView{

    private TextView remain, time;
    private GridView mineMap;
    private GamePresenter presenter;
    private MineMapAdapter mineMapAdapter;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        getSupportActionBar().hide();
        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        Integer level = Integer.valueOf(intent.getStringExtra("level"));
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        remain=findViewById(R.id.remainView);
        time=findViewById(R.id.timeView);
        presenter=new GamePresenter(this,new GameModel(userName,level));
        presenter.startTime();
        presenter.initRemain();
        presenter.setContext(getApplicationContext());

        mineMap=findViewById(R.id.mineMap);
        if(level==0) mineMap.setNumColumns(9);
        else mineMap.setNumColumns(16);
        mineMapAdapter = new MineMapAdapter(presenter);
        presenter.createMineMap();

        mineMap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(presenter.getFinished()==false) {
                    Integer val = presenter.getItem(i);
                    if (1 <= val && val <= 8) {
                        presenter.openMultiCell(i);
                    } else if (val == 9) {
                        presenter.openCell(i);
                    }
                }
            }
        });
        mineMap.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(presenter.getFinished()==false) {
                    Integer val = presenter.getItem(i);
                    if (val == 9) {
                        presenter.downRemain(i);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(50);
                        }
                    } else if (val == 10) {
                        presenter.upRemain(i);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(50);
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void updateRemain(Integer rem) {
        GameActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                remain.setText("Remain : "+rem);
            }
        });
    }

    @Override
    public void updateTime(Integer t) {
        GameActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                time.setText("Time : "+t);
            }
        });
    }

    @Override
    public void updateUserMap() {
        GameActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mineMap.setAdapter(mineMapAdapter);
            }
        });
    }

    @Override
    public void toastFinish(String msg) {
        //TODO toast message (finish, fail, ...)
        GameActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}