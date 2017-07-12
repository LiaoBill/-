package billliao.outpricing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;
import java.util.*;
import billliao.outpricing.dao.*;

public class DateStastics extends AppCompatActivity {
    private TextView statistic_list;
    private EditText country;
    private Button go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_stastics);
        statistic_list = (TextView) findViewById(R.id.editText_list);
        statistic_list.setMovementMethod(ScrollingMovementMethod.getInstance());
        country = (EditText) findViewById(R.id.editText_country);
        go = (Button) findViewById(R.id.button_statistic);
        //events
        go.setOnClickListener(new MyOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        statistic_list.setText("input your place");
    }
    private Context context = this;
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String place = country.getText().toString().toLowerCase();
            if(place == null || place.equals("")){
                statistic_list.setText("check your input");
                return;
            }
            String cons = PricingObjectList.getInstance().executeDateStatistics(context,place);
            if(cons == null || cons.equals("")){
                statistic_list.setText("system error");
                return;
            }
            statistic_list.setText(cons);
        }
    }
}
