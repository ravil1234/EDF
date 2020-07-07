package com.example.edf;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
public class PhoneAuthActivity extends AppCompatActivity
{
    Button b;
    CountryCodePicker ccp;
    EditText ph_number;
    ImageView cross_tic_img;
    String phn="",cc="+91";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_phone_auth);
        ph_number = findViewById(R.id.phone_number);
        b = findViewById(R.id.proceed_btn);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        cross_tic_img=findViewById(R.id.cross_tic_img);
        ccp.setCountryForPhoneCode(91);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                cc=ccp.getSelectedCountryCodeWithPlus();
                // Toast.makeText(PhoneAuthActivity.this, "Updated " +selectedCountry.getName().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        ph_number.addTextChangedListener(loginTextWatcher);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(PhoneAuthActivity.this, OTP_Activity.class);
                Log.d("country_code",cc+phn);
                i.putExtra("mobile",phn);
                i.putExtra("country_code",cc);
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
            String ph = ph_number.getText().toString().trim();
            cross_tic_img.setVisibility(View.VISIBLE);
            if(ph.isEmpty()||ph.length()<8)
            {
                b.setBackgroundResource(R.drawable.button_border);
                b.setEnabled(false);
                cross_tic_img.setImageResource(R.drawable.cross_imgphone);
            }
            else
            {
                cross_tic_img.setImageResource(R.drawable.tick_img);
                phn=ph;
                b.setBackgroundResource(R.drawable.button_border_enabled);
                b.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
