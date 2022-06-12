package edu.skku.cs.pa3;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapSelectionModel implements
    MapSelectionContract.ContractForModel{

    private final String serverUrl="https://00zds5df2g.execute-api.ap-northeast-2.amazonaws.com/dev";
    private Integer level;
    private String name;
    private ArrayList<UserRecord> records=new ArrayList<UserRecord>();
    private Context context;
    public MapSelectionModel(Integer level){
        this.level=level;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<UserRecord> getRecords() {
        return records;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public void nextTouched(OnSuccessListener listener) {
        level=(level+1)%3;
        setRecords(listener);
    }

    @Override
    public void prevTouched(OnSuccessListener listener) {
        level=(level+2)%3;
        setRecords(listener);
    }

    @Override
    public void startTouched(OnSuccessListener listener) {
        listener.onStart();
    }

    @Override
    public void init(OnSuccessListener listener) {
        setRecords(listener);
    }

    @Override
    public UserRecord getItem(int i) {
        return records.get(i);
    }

    @Override
    public void setContext(Context context) {
        this.context=context;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public void setRecords(OnSuccessListener listener){
        //TODO get data from DB
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(serverUrl+"/getrecord").newBuilder();
        urlBuilder.addQueryParameter("difficulty",Integer.toString(level));

        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse=response.body().string();
                Gson gson = new GsonBuilder().create();
                rec[] lst = gson.fromJson(myResponse,rec[].class);
                records.clear();
                int len= lst.length;
                for(int i=0 ; i<len ; i++){
                    records.add(new UserRecord(lst[i].getName(),lst[i].getSec(),1));
                }
                //sort records
                Collections.sort(records);
                int r=1;
                for(int i=0, sz=records.size() ; i<sz ; i++){
                    if(i>0 && records.get(i-1).getSec().equals(records.get(i).getSec())==false) r=i+1;
                    records.get(i).setRank(r);
                }
                listener.onSuccess();
            }
        });
    }
}

class rec{
    private String name;
    private Integer sec;
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