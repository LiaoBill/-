package billliao.outpricing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import billliao.outpricing.dao.PricingObjectBean;
import billliao.outpricing.dao.PricingObjectList;

public class UpdateAndDelete extends AppCompatActivity {
    private EditText name;
    private EditText description;
    private EditText org_tag;
    private EditText price;
    private EditText price_country;
    private EditText price_vector;
    private EditText exchange_price;
    private EditText exchange_price_country;
    private EditText today_time;
    private EditText pricing_date;
    private EditText utc;
    private EditText place;
    private EditText pricing_tag;
    private Button button_update;
    private Button button_delete;
    private Button button_previous;
    private Button button_next;
    private TextView status;
    private TextView id;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_delete);
        //init
        name = (EditText) findViewById(R.id.editText_name);
        description = (EditText) findViewById(R.id.editText_description);
        org_tag = (EditText) findViewById(R.id.editText_org_tag);
        price = (EditText) findViewById(R.id.editText_price);
        price_country = (EditText) findViewById(R.id.editText_price_country);
        price_vector = (EditText) findViewById(R.id.editText_price_vector);
        exchange_price = (EditText) findViewById(R.id.editText_exchange_price);
        exchange_price_country = (EditText) findViewById(R.id.editText_exchange_price_country);
        today_time = (EditText) findViewById(R.id.editText_today_time);
        pricing_date = (EditText) findViewById(R.id.editText_pricing_date);
        id = (TextView) findViewById(R.id.textView_id);
        utc = (EditText) findViewById(R.id.editText_utc);
        place = (EditText) findViewById(R.id.editText_place);
        pricing_tag = (EditText) findViewById(R.id.editText_pricing_tag);

        button_update = (Button) findViewById(R.id.button_update);
        button_delete = (Button) findViewById(R.id.button_delete);
        button_previous = (Button) findViewById(R.id.button_previous);
        button_next = (Button) findViewById(R.id.button_next);
        status = (TextView) findViewById(R.id.textView_status);
        //events
        button_update.setOnClickListener(new MyOnClickListener());
        button_delete.setOnClickListener(new MyOnClickListener());
        button_previous.setOnClickListener(new MyOnClickListener());
        button_next.setOnClickListener(new MyOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //logic init
        PricingObjectList.getInstance().readyForNextAndPrevious();
        PricingObjectBean current = PricingObjectList.getInstance().getNext();
        showUI(current);
    }

    private void showUI(PricingObjectBean current){
        if(current == null){
            name.setText("空");
            description.setText("空");
            org_tag.setText("空");
            price.setText("空");
            price_country.setText("空");
            price_vector.setText("空");
            exchange_price.setText("空");
            exchange_price_country.setText("空");
            today_time.setText("空");
            pricing_date.setText("空");
            id.setText("空");
            utc.setText("空");
            place.setText("空");
            pricing_tag.setText("空");
        }
        else{
            name.setText(current.getName());
            description.setText(current.getDescription());
            org_tag.setText(current.getOrg_tag());
            price.setText(current.getPrice());
            price_country.setText(current.getPrice_country());
            price_vector.setText(current.getPrice_vector());
            exchange_price.setText(current.getExchange_price());
            exchange_price_country.setText(current.getExchange_price_country());
            today_time.setText(current.getToday_time());
            pricing_date.setText(current.getPricing_date());
            id.setText(current.getId());
            utc.setText(current.getUtc());
            place.setText(current.getPlace());
            pricing_tag.setText(current.getPricing_tag());
        }
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_update:{
                    String id_string = id.getText().toString();
                    if(id_string.equals("空")){
                        break;
                    }
                    PricingObjectBean now = new PricingObjectBean();
                    now.set(name.getText().toString(),
                            description.getText().toString(),
                            org_tag.getText().toString().toLowerCase(),
                            price.getText().toString(),
                            price_country.getText().toString().toLowerCase(),
                            price_vector.getText().toString().toLowerCase(),
                            exchange_price.getText().toString(),
                            exchange_price_country.getText().toString().toLowerCase(),
                            today_time.getText().toString(),
                            pricing_date.getText().toString(),
                            utc.getText().toString().toLowerCase(),
                            place.getText().toString().toLowerCase(),
                            pricing_tag.getText().toString().toLowerCase()
                    );
                    boolean result = PricingObjectList.getInstance().updateWithID(id_string,now,context);
                    if(!result){
                        status.setText("UPDATE_ERROR");
                    }
                    else{
                        status.setText("UPDATE_SUCCESS");
                    }
                    break;
                }
                case R.id.button_delete:{
                    String id_string = id.getText().toString();
                    if(id_string.equals("空")){
                        break;
                    }
                    boolean result = PricingObjectList.getInstance().deleteWithID(id_string,context);
                    if(!result){
                        status.setText("DELETE_ERROR");
                    }
                    else{
                        status.setText("DELETE_SUCCESS");
                        PricingObjectBean current = PricingObjectList.getInstance().getNext();
                        showUI(current);
                    }
                    break;
                }
                case R.id.button_previous:{
                    PricingObjectBean current = PricingObjectList.getInstance().getPrevious();
                    showUI(current);
                    break;
                }
                case R.id.button_next:{
                    PricingObjectBean current = PricingObjectList.getInstance().getNext();
                    showUI(current);
                    break;
                }
            }
        }
    }
}

