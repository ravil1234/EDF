package com.example.edf;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
public class OTP_Activity extends AppCompatActivity
{
    private String mVerificationId;
    private String tokenid;
    private OtpTextView otpTextView;
    public FirebaseAuth mAuth;
    TextView otp_send_invalid,resendcodeenabled;
    String code="";
    String mobil="";
    String country_code="";
    TextView resendcode;
    TextView textview_mobile;
    CountDownTimer cTimer = null;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_otp_);
        code="";
        sharedpreferences=getSharedPreferences("details",MODE_PRIVATE);
        sharedpreferences.edit().putString("name","Default_Name").apply();
        sharedpreferences.edit().putString("clg","Default_Clg").apply();
        textview_mobile=findViewById(R.id.text_view_mobile);
        final Button b=findViewById(R.id.verify_btn);
        resendcode=findViewById(R.id.resend_code);
        otp_send_invalid=findViewById(R.id.otp_send_invalid);
        otp_send_invalid.setText("");
        resendcodeenabled=findViewById(R.id.resend_code_enabled);
        progressBar=findViewById(R.id.progress_bar);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        otpTextView = findViewById(R.id.otp_view);
        mobil = intent.getStringExtra("mobile");
        country_code = intent.getStringExtra("country_code");
        textview_mobile.setText("enter the 6 digit OTP sent on "+country_code+mobil+" to proceed");
        Log.d("intented_ph",country_code+mobil);
        final String mobile=country_code+mobil;
        sendVerificationCode(mobile);
        startTimer();
        resendcodeenabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sendVerificationCode(mobile);
                resendcodeenabled.setVisibility(View.INVISIBLE);
                otpTextView.resetState();
                resendcode.setVisibility(View.VISIBLE);
                startTimer();
            }
        });
        otpTextView.setOtpListener(new OTPListener()
        {
            @Override
            public void onInteractionListener()
            {
                b.setEnabled(false);
                b.setBackgroundResource(R.drawable.button_border);
            }
            @Override
            public void onOTPComplete(String otp)
            {
                code=otp;
                b.setEnabled(true);
                b.setBackgroundResource(R.drawable.button_border_enabled);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                otpTextView.resetState();
                progressBar.setVisibility(View.VISIBLE);
                verifyVerificationCode(code);

            }
        });
    }
    public void startTimer()
    {

        cTimer = new CountDownTimer(30000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                String res="Resend Verification Code 00:"+(millisUntilFinished/1000);
                resendcode.setText(res);
            }
            public void onFinish()
            {
                resendcodeenabled.setVisibility(View.VISIBLE);
                String res="Resend Verification Code 00:30";
                resendcode.setText(res);
                resendcode.setVisibility(View.INVISIBLE);
            }
        };
        cTimer.start();
    }
    private void sendVerificationCode(String mobile)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
        {
            String code = phoneAuthCredential.getSmsCode();
            otp_send_invalid.setText("OTP send");
            otp_send_invalid.setTextColor(Color.parseColor("#21D729"));
            if (code != null)
            {
                otpTextView.setOTP(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e)
        {

            Log.d("exception ",e.getMessage());
            Toast.makeText(OTP_Activity.this, "Verification Failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user

            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code)
    {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }
    private void getToken(final boolean isNewUser )
    {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>()
        {
            public void onComplete(@NonNull Task<GetTokenResult> task)
            {
                if (task.isSuccessful())
                {
                    tokenid=task.getResult().getToken();

                }
                else
                {
                    Toast.makeText(OTP_Activity.this, (CharSequence) task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTP_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(!isNewUser)
                            {
                               Intent i=new Intent(OTP_Activity.this,MainActivity.class);
                               startActivity(i);
                            }
                            else
                                {

                                    Intent i=new Intent(OTP_Activity.this,SignUpActivity.class);
                                    startActivity(i);
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            otp_send_invalid.setText("Invalid OTP");
                            otp_send_invalid.setTextColor(Color.parseColor("#ff0000"));
                            Toast.makeText(OTP_Activity.this,"Invalid OTP!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}