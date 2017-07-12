package billliao.outpricing;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.*;
import android.widget.*;

import billliao.outpricing.dao.PricingObjectList;

public class MainActivity extends AppCompatActivity {
    private Button button_add;
    private Button button_all_stastics;
    private Button button_date_stastics;
    private Button button_delete_and_update;
    private Button currency_change;
    private Button button_group_chat;
    private TextView pricingobject_list;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        button_add = (Button) findViewById(R.id.button_add);
        pricingobject_list = (TextView) findViewById(R.id.editText_pricingobject_list);
        pricingobject_list.setMovementMethod(ScrollingMovementMethod.getInstance());
        button_delete_and_update = (Button) findViewById(R.id.button_update_and_delete);
        button_all_stastics = (Button) findViewById(R.id.button_all_stastics);
        button_date_stastics = (Button) findViewById(R.id.button_date_stastics);
        currency_change = (Button) findViewById(R.id.button_currency_change);
        button_group_chat = (Button) findViewById(R.id.button_group_chat);
        //events
        button_add.setOnClickListener(new MyOnClickListener());
        button_delete_and_update.setOnClickListener(new MyOnClickListener());
        button_all_stastics.setOnClickListener(new MyOnClickListener());
        button_date_stastics.setOnClickListener(new MyOnClickListener());
        currency_change.setOnClickListener(new MyOnClickListener());
        button_group_chat.setOnClickListener(new MyOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean result = PricingObjectList.getInstance().read(context);
        if(!result){
            pricingobject_list.setText("READ_RECORD_ERROR");
            return;
        }
        else{
            String list_string = PricingObjectList.getInstance().getListString();
            pricingobject_list.setText(list_string);
        }
        resetScrolling();
    }

    private void resetScrolling(){
        int offset = pricingobject_list.getLineCount()*pricingobject_list.getLineHeight();
        if(offset>pricingobject_list.getHeight()){
            pricingobject_list.scrollTo(0,offset-pricingobject_list.getHeight());
        }
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_add:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,AddPricingObject.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case R.id.button_update_and_delete:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,UpdateAndDelete.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case R.id.button_all_stastics:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,AllStastics.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case R.id.button_date_stastics:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,DateStastics.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case R.id.button_currency_change:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,CurrencyChange.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case R.id.button_group_chat:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,GroupChat.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
            }
        }
    }
}

