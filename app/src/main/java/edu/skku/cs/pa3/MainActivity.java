package edu.skku.cs.pa3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements LoginContract.ContractForView{

    private EditText name, pw;
    private Button login, register;
    private LoginPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        getSupportActionBar().hide();
        name=findViewById(R.id.nameText);
        pw=findViewById(R.id.pwText);
        login=findViewById(R.id.logInButton);
        register=findViewById(R.id.registerButton);
        presenter=new LoginPresenter(this,new LoginModel());

        login.setOnClickListener(view -> {
            presenter.onLogInButtonTouched(name.getText().toString(),pw.getText().toString());
        });
        register.setOnClickListener(view -> {
            presenter.onRegisterButtonTouched(name.getText().toString(),pw.getText().toString());
        });
    }

    @Override
    public void toastMessage(String msg) {
        if(msg.equals(LoginModel.loginSuccess)){
            // move to next activity
            Intent intent = new Intent(MainActivity.this,MapSelectionActivity.class);
            intent.putExtra(LoginModel.EXT_NAME,name.getText().toString());
            startActivity(intent);
        }
        else {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}