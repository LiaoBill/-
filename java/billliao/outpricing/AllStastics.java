package billliao.outpricing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;

import org.w3c.dom.Text;

import billliao.outpricing.dao.*;

public class AllStastics extends AppCompatActivity {
    private TextView list;
    private EditText order_text;
    private Button order_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stastics);
        list = (TextView) findViewById(R.id.editText_all_stastics);
        list.setMovementMethod(ScrollingMovementMethod.getInstance());
        order_text = (EditText) findViewById(R.id.editText_order_name);
        order_button = (Button) findViewById(R.id.button_ORDER);
        //events
        order_button.setOnClickListener(new MyOnClickListener());
    }
    private Context context = this;
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String order = order_text.getText().toString().toLowerCase();
            String cons = PricingObjectList.getInstance().executeOrder(context,order);
            if(cons==null||cons.equals("input invalid")){
                list.setText("款项负责方的金额分布情况(a/b/c/common)[d]\r\n所有换钱项(exchange)[e]\r\n所有支出项(out)[o]\r\n所有收入项(in)[i]\r\n按消费类型统计所有(eat/travel/live/play/use)[t]\r\n");
            }
            else{
                list.setText(cons);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        list.setText("款项负责方的金额分布情况(a/b/c/common)[d]\r\n所有换钱项(exchange)[e]\r\n所有支出项(out)[o]\r\n所有收入项(in)[i]\r\n按消费类型统计所有(eat/travel/live/play/use)[t]\r\n");
    }
}
