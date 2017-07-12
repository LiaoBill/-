package billliao.outpricing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import billliao.outpricing.dao.PricingObjectList;

public class CurrencyChange extends AppCompatActivity {
    private EditText china_yuan;
    private EditText serbia_yuan;
    private EditText currency;
    private int status = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_change);
        china_yuan = (EditText) findViewById(R.id.editText_china_yuan);
        serbia_yuan = (EditText) findViewById(R.id.editText_serbia_yuan);
        currency = (EditText) findViewById(R.id.editText_currency);
        currency.setText(Double.toString(15.5625));
        //events
        china_yuan.setOnFocusChangeListener(new MyFocusListener());
        serbia_yuan.setOnFocusChangeListener(new MyFocusListener());
        china_yuan.addTextChangedListener(new ChinaYuanListener());
        serbia_yuan.addTextChangedListener(new SerbiaYuanListener());

    }

    class MyFocusListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.editText_china_yuan:{
                    if(hasFocus){
                        status = 0;
                    }
                    break;
                }
                case R.id.editText_serbia_yuan:{
                    if(hasFocus){
                        status = 1;
                    }
                    break;
                }
            }
        }
    }

    class ChinaYuanListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(status == 1){
                return;
            }
            if(currency.getText().toString() == null || !currency.getText().toString().matches("[0-9]+.[0-9]*")){
                serbia_yuan.setText("CURRENCY_ERROR");
                return;
            }
            double currency_double = Double.parseDouble(currency.getText().toString());
            if(!(china_yuan.getText().toString() == null||!china_yuan.getText().toString().matches("[0-9]+.[0-9]*"))){
                double current = Double.parseDouble(china_yuan.getText().toString());
                double serbia_cons = currency_double*current;
                serbia_yuan.setText(Double.toString(serbia_cons));
            }
            else{
                serbia_yuan.setText("");
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }
    class SerbiaYuanListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(status == 0){
                return;
            }
            if(currency.getText().toString() == null ||!currency.getText().toString().matches("[0-9]+.[0-9]*")){
                serbia_yuan.setText("CURRENCY_ERROR");
                return;
            }
            if(!(serbia_yuan.getText().toString() == null||!serbia_yuan.getText().toString().matches("[0-9]+.[0-9]*"))) {
                double currency_double = Double.parseDouble(currency.getText().toString());
                double current = Double.parseDouble(serbia_yuan.getText().toString());
                double china_cons = current / currency_double;
                china_yuan.setText(Double.toString(china_cons));
            }
            else {
                china_yuan.setText("");
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }
}
