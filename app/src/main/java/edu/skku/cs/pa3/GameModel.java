package edu.skku.cs.pa3;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GameModel implements
    GameContract.ContractForModel{

    private final String serverUrl="https://00zds5df2g.execute-api.ap-northeast-2.amazonaws.com/dev";
    private Integer level, time, remain, width, height, N, mCnt, oCnt;
    private String name, msg;
    private ArrayList<Integer> mineMap, userMap;
    private boolean finished;
    private Timer timer;
    private Context context;
    private int[] dy={-1,-1,0,1,1,1,0,-1};
    private int[] dx={0,1,1,1,0,-1,-1,-1};
    public static final String complete = "Successfully found all mines! Congratulation!";
    public static final String fail = "Oops, You clicked a mine";


    public GameModel(String name, Integer level){
        this.name=name;
        this.level=level;
        remain=10; time=-1; width=height=9;
        if(level==1){
            remain=40; width=height=16;
        }
        else if(level==2){
            remain=99; width=16; height=30;
        }
        mCnt=remain; oCnt=0;
        N=width*height;
        finished=false;
    }

    @Override
    public void openCell(Integer idx, onSuccessListener listener) {
        //open
        open(idx);
        listener.updateUserMap();
        //check whether finished
        if(oCnt+mCnt==N){
            finished=true;
            msg = new String(complete);
        }
        if(finished){
            if(msg.equals(fail)){
                for(int i=0 ; i<N ; i++){
                    int tmp=userMap.get(i);
                    if(mineMap.get(i)==-1){
                        if(tmp==9) userMap.set(i,-1);
                    }
                    else{
                        if(tmp==10) userMap.set(i,11);
                    }
                }
                listener.updateUserMap();
            }
            listener.toastFinish();
        }
    }

    @Override
    public void openMultiCell(Integer idx, onSuccessListener listener) {
        //open
        P cur = new P(idx/width,idx%width);
        for(int j=0 ; j<8 ; j++){
            P nxt = new P(cur.y+dy[j],cur.x+dx[j]);
            if(nxt.y<0 || nxt.y>=height || nxt.x<0 || nxt.x>=width) continue;
            if(userMap.get(nxt.y*width+nxt.x)!=9) continue;
            open(nxt.y*width+nxt.x);
        }
        listener.updateUserMap();
        //check whether finished
        if(oCnt+mCnt==N){
            finished=true;
            msg = new String(complete);
        }
        if(finished){
            if(msg.equals(fail)){
                for(int i=0 ; i<N ; i++){
                    int tmp=userMap.get(i);
                    if(mineMap.get(i)==-1){
                        if(tmp==9) userMap.set(i,-1);
                    }
                    else{
                        if(tmp==10) userMap.set(i,11);
                    }
                }
                listener.updateUserMap();
            }
            listener.toastFinish();
        }
    }

    @Override
    public void calculateTime(onSuccessListener listener) {
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                time+=1;
                listener.updateTime();
                if(finished==true){
                    timer.cancel();
                    if(msg.equals(complete)){
                        for(int i=0 ; i<N ; i++){
                            if(mineMap.get(i)==-1) userMap.set(i,10);
                        }
                        listener.updateUserMap();
                        uploadRecord(listener);
                    }
                }
            }
        };
        timer=new Timer();
        timer.schedule(timerTask,0,1000);
    }

    @Override
    public void uploadRecord(onSuccessListener listener) {
        //TODO upload to server
        OkHttpClient client = new OkHttpClient();
        NewRecord newRecord = new NewRecord();
        newRecord.setDifficulty(level);
        newRecord.setName(name);
        newRecord.setSec(time);

        Gson gson = new Gson();
        String json = gson.toJson(newRecord,NewRecord.class);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(serverUrl+"/addrecord").newBuilder();
        String url=urlBuilder.build().toString();

        Request req = new Request.Builder().url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),json)).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse=response.body().string();
                Gson gson = new GsonBuilder().create();
                final UpQuery upQuery = gson.fromJson(myResponse,UpQuery.class);
                if(upQuery.isSuccess()) msg = new String("Record is successfully uploaded");
                listener.toastFinish();
            }
        });
    }

    @Override
    public void createMineMap(onSuccessListener listener) {
        ArrayList<Integer> tmp=new ArrayList<Integer>();
        mineMap = new ArrayList<Integer>();
        userMap = new ArrayList<Integer>();
        for(int i=0 ; i<N ; i++){
            tmp.add(i); mineMap.add(0); userMap.add(9);
        }
        Collections.shuffle(tmp);
        for(int i=0 ; i<remain ; i++) mineMap.set(tmp.get(i),-1);

        for(int i=0 ; i<N ; i++){
            if(mineMap.get(i)==-1) continue;
            P cur=new P(i/width,i%width);
            int cnt=0;
            for(int j=0 ; j<8 ; j++){
                P nxt=new P(cur.y+dy[j],cur.x+dx[j]);
                if(nxt.y<0 || nxt.y>=height || nxt.x<0 || nxt.x>=width) continue;
                if(mineMap.get(nxt.y*width+nxt.x)==-1) cnt++;
            }
            mineMap.set(i,cnt);
        }

        listener.updateUserMap();
    }

    @Override
    public ArrayList<Integer> getMapInfo() {
        return userMap;
    }

    @Override
    public Integer getRemain() {
        return remain;
    }

    @Override
    public Integer getTime() {
        return time;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public boolean getFinished() {
        return finished;
    }

    @Override
    public Integer getSize() {
        return N;
    }

    @Override
    public Integer getItem(int i) {
        return userMap.get(i);
    }

    @Override
    public void setContext(Context context) {
        this.context=context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    @Override
    public void open(int i) {
        Integer val=mineMap.get(i);
        if(val==-1){
            finished=true;
            userMap.set(i,-1);
            oCnt++;
            msg = new String(fail);
            return;
        }
        userMap.set(i,val); oCnt++;
        if(val==0){
            P cur = new P(i/width,i%width);
            for(int j=0 ; j<8 ; j++){
                P nxt = new P(cur.y+dy[j],cur.x+dx[j]);
                if(nxt.y<0 || nxt.y>=height || nxt.x<0 || nxt.x>=width) continue;
                if(userMap.get(nxt.y*width+nxt.x)!=9) continue;
                open(nxt.y*width+nxt.x);
            }
        }
    }

    @Override
    public void setFlag(int i, onSuccessListener listener) {
        remain-=1;
        userMap.set(i,10);
        listener.updateRemain();
        listener.updateUserMap();
    }

    @Override
    public void removeFlag(int i, onSuccessListener listener) {
        remain+=1;
        userMap.set(i,9);
        listener.updateRemain();
        listener.updateUserMap();
    }
}

class P{
    int y, x;
    public P(int y, int x){
        this.y=y;
        this.x=x;
    }
}

class NewRecord{
    private Integer difficulty;
    private String name;
    private Integer sec;

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSec() {
        return sec;
    }

    public void setSec(Integer sec) {
        this.sec = sec;
    }
}

class UpQuery{
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}