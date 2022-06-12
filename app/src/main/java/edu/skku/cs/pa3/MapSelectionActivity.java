package edu.skku.cs.pa3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MapSelectionActivity extends AppCompatActivity
    implements MapSelectionContract.ContractForView{

    private TextView name, bestRecord, diff;
    private Button prevButton, nextButton, startButton;
    private MapSelectionPresenter presenter;
    private ListView ranking;
    private RankingAdapter rankingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        getSupportActionBar().hide();
        Intent intent = getIntent();
        String userName = intent.getStringExtra(LoginModel.EXT_NAME);
        name=findViewById(R.id.userName);
        bestRecord=findViewById(R.id.bestRecord);
        diff=findViewById(R.id.diffTextView);
        name.setText("User : "+userName);

        prevButton=findViewById(R.id.prevButton);
        nextButton=findViewById(R.id.nextButton);
        startButton=findViewById(R.id.startButton);
        presenter = new MapSelectionPresenter(this,new MapSelectionModel(0));
        presenter.sendUserName(userName);
        presenter.setContext(getApplicationContext());
        presenter.init();
        ranking=findViewById(R.id.rankList);
        rankingAdapter = new RankingAdapter(presenter);

        prevButton.setOnClickListener(view -> {
            presenter.onPrevButtonTouched();
        });
        nextButton.setOnClickListener(view -> {
            presenter.onNextButtonTouched();
        });
        startButton.setOnClickListener(view -> {
            presenter.onStartButtonTouched();
        });
    }

    @Override
    public void updateView(Integer level, ArrayList<UserRecord> records) {
        MapSelectionActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //change level
                if(level==0) diff.setText("Easy");
                else if(level==1) diff.setText("Intermediate");
                else if(level==2) diff.setText("Hard");
                //find best records and update textView
                String uname=presenter.getName();
                Integer best=-1;
                for(int i=0, sz=records.size() ; i<sz ; i++){
                    if(uname.equals(records.get(i).getName())){
                        best=records.get(i).getSec();
                        break;
                    }
                }
                if(best==-1) bestRecord.setText("INFINITY");
                else bestRecord.setText(Integer.toString(best));
                //TODO update ranking with adapter
                ranking.setAdapter(rankingAdapter);
            }
        });

    }

    @Override
    public void startGame(Integer level) {
        //TODO start game with this level
        Intent intent = new Intent(MapSelectionActivity.this,GameActivity.class);
        intent.putExtra("name",presenter.getName());
        intent.putExtra("level",Integer.toString(presenter.getLevel()));
        startActivity(intent);
    }
}