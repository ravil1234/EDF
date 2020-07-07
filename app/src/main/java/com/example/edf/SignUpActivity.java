package com.example.edf;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;

public class SignUpActivity extends AppCompatActivity
{
    EditText nameuser,clg_user;
    String name="";
    Button btn_proceed;
    String mVerificationId,mobil,country_code;
    SharedPreferences sharedpreferences;
    String college="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_name);
        getSupportActionBar().hide();
        sharedpreferences=getSharedPreferences("details",MODE_PRIVATE);
        btn_proceed =findViewById(R.id.proceed_btn);
        nameuser=findViewById(R.id.name_user);
        clg_user=findViewById(R.id.clg_user);
        nameuser.addTextChangedListener(loginTextWatcher);
        clg_user.addTextChangedListener(loginTextWatcher);
        Intent i=getIntent();
        mVerificationId=i.getStringExtra("id_token");
        mobil=i.getStringExtra("phone");
        country_code=i.getStringExtra("country_code");
        btn_proceed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sharedpreferences.edit().putString("name",name).apply();
                sharedpreferences.edit().putString("clg",college).apply();
                 Intent i=new Intent(SignUpActivity.this,MainActivity.class);
                 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 i.putExtra("name",name);
                 i.putExtra("clg",college);
                 startActivity(i);
            }
        });
    }
    private TextWatcher loginTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String ph = nameuser.getText().toString().trim();
            String clg=clg_user.getText().toString().trim();
            if(ph.isEmpty()||clg.isEmpty())
            {
                btn_proceed.setBackgroundResource(R.drawable.button_border);
                btn_proceed.setEnabled(false);
            }
            else
            {
                name=ph;
                college=clg;
                btn_proceed.setBackgroundResource(R.drawable.button_border_enabled);
                btn_proceed.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };
}
