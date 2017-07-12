package billliao.outpricing;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import billliao.outpricing.dao.PricingObjectBean;
import billliao.outpricing.dao.PricingObjectList;

public class AddPricingObject extends AppCompatActivity {
    private EditText name;
    private EditText description;
    private EditText price;
    private EditText pricing_tag;
    private EditText place;

    private EditText org_tag;
    private EditText price_country;
    private EditText price_vector;
    private EditText exchange_price;
    private EditText exchange_price_country;
    private EditText utc;

    private Button button_add;
    private TextView status;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pricing_object);
        //init
        name = (EditText) findViewById(R.id.editText_name);
        description = (EditText) findViewById(R.id.editText_description);
        price = (EditText) findViewById(R.id.editText_price);
        pricing_tag = (EditText) findViewById(R.id.editText_pricing_tag);
        place = (EditText) findViewById(R.id.editText_place);

        org_tag = (EditText) findViewById(R.id.editText_org_tag);
        price_country = (EditText) findViewById(R.id.editText_price_country);
        price_vector = (EditText) findViewById(R.id.editText_price_vector);
        exchange_price = (EditText) findViewById(R.id.editText_exchange_price);
        exchange_price_country = (EditText) findViewById(R.id.editText_exchange_price_country);
        utc = (EditText) findViewById(R.id.editText_utc);


        button_add = (Button) findViewById(R.id.button_add);
        status = (TextView) findViewById(R.id.textView_status);
        //events
        button_add.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String name_string = name.getText().toString();
            String description_string = description.getText().toString();
            String org_tag_string = org_tag.getText().toString().toLowerCase();
            String price_string = price.getText().toString();
            String price_country_string = price_country.getText().toString().toLowerCase();
            String price_vector_string = price_vector.getText().toString().toLowerCase();
            String exchange_price_string = exchange_price.getText().toString();
            String exchange_price_country_string = exchange_price_country.getText().toString().toLowerCase();
            String utc_string = utc.getText().toString().toLowerCase();
            String place_string = place.getText().toString().toLowerCase();
            String pricing_tag_string = pricing_tag.getText().toString().toLowerCase();
            boolean result = PricingObjectList.getInstance().SetTimeZone(utc_string);
            if(!result){
                status.setText("UTC_SET_ERROR");
                return;
            }
            String today_time_string = PricingObjectList.getInstance().getTodayTime();
            String pricing_date_string = PricingObjectList.getInstance().getTodayDate();
            PricingObjectBean current_bean = new PricingObjectBean();
            current_bean.set(name_string,description_string,org_tag_string,price_string,price_country_string,price_vector_string,exchange_price_string,exchange_price_country_string
            ,today_time_string,pricing_date_string,utc_string,place_string,pricing_tag_string);
            boolean add_result = PricingObjectList.getInstance().add(current_bean,context);
            if(!add_result){
                status.setText("ADD_ERROR");
                return;
            }
            status.setText("ADD_SUCCESS");
        }
    }

}
