package edu.skku.cs.pa3;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginModel implements
        LoginContract.ContractForModel{
    private String msg;

    private final String serverUrl="https://00zds5df2g.execute-api.ap-northeast-2.amazonaws.com/dev";
    public static final String loginSuccess = "Login Success!";
    public static final String loginFailed = "Login Failed!";
    public static final String registerSuccess = "Register Success!";
    public static final String registerFailed = "Duplicated Name!";
    public static final String checkConstraints = "Please check constraints!";
    public static final String EXT_NAME="Name";

    public LoginModel(){
        msg=new String();
    }

    @Override
    public boolean checkValidity(String name, String pw){
        if(name.length()>15 || pw.length()>30) return false;
        if(name.length()==0 || pw.length()==0) return false;
        int n=name.length();
        for(int i=0 ; i<n ; i++){
            char tmp=name.charAt(i);
            if('a'<=tmp && tmp<='z') continue;
            if('A'<=tmp && tmp<='Z') continue;
            if('0'<=tmp && tmp<='9') continue;
            return false;
        }
        n=pw.length();
        for(int i=0 ; i<n ; i++){
            char tmp=pw.charAt(i);
            if('a'<=tmp && tmp<='z') continue;
            if('A'<=tmp && tmp<='Z') continue;
            if('0'<=tmp && tmp<='9') continue;
            return false;
        }
        return true;
    }
    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void register(OnSuccessListener listener, String name, String pw) {
        boolean tf=checkValidity(name,pw);
        if(tf==false) {
            msg = new String(checkConstraints);
            listener.onSuccess();
            return;
        }
        OkHttpClient client = new OkHttpClient();
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setPasswd(pw);

        Gson gson = new Gson();
        String json = gson.toJson(userInfo,UserInfo.class);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(serverUrl+"/adduser").newBuilder();
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
                final LoginQuery qry=gson.fromJson(myResponse,LoginQuery.class);
                if(qry.isSuccess())  msg = new String(registerSuccess);
                else msg = new String(registerFailed);
                listener.onSuccess();
            }
        });
    }

    @Override
    public void login(OnSuccessListener listener, String name, String pw) {
        boolean tf=checkValidity(name,pw);
        if(tf==false) {
            msg = new String(loginFailed);
            listener.onSuccess();
            return;
        }
        OkHttpClient client = new OkHttpClient();
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setPasswd(pw);

        Gson gson = new Gson();
        String json = gson.toJson(userInfo,UserInfo.class);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(serverUrl+"/login").newBuilder();
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
                final LoginQuery qry=gson.fromJson(myResponse,LoginQuery.class);
                if(qry.isSuccess())  msg = new String(loginSuccess);
                else msg = new String(loginFailed);
                listener.onSuccess();
            }
        });
    }

}

class UserInfo{
    private String name;
    private String passwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}

class LoginQuery{
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}