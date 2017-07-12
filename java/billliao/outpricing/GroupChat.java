package billliao.outpricing;

import android.app.Dialog;
import android.content.Context;
import android.net.*;
import android.net.wifi.*;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;

import org.w3c.dom.Text;

import billliao.outpricing.chatsys.DialogList;
import billliao.outpricing.chatsys.MyClientSocket;
import billliao.outpricing.chatsys.MyServerSocket;

public class GroupChat extends AppCompatActivity {
    private Button send;
    private Button set;
    private Button save;
    private Button cls;
    private TextView chat_list;
    private EditText chat_value;
    private EditText user_name;
    private Handler handler;
    private EditText editText_port;
    private TextView group_status;
    private boolean is_usable = false;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        chat_list = (TextView) findViewById(R.id.editText_chat_list);
        chat_list.setMovementMethod(ScrollingMovementMethod.getInstance());
        chat_value = (EditText) findViewById(R.id.editText_chat_value);
        chat_value.setText("");
        user_name = (EditText) findViewById(R.id.editText_username);
        send = (Button) findViewById(R.id.button_send);
        set = (Button) findViewById(R.id.button_setport);
        save = (Button) findViewById(R.id.button_save);
        cls = (Button) findViewById(R.id.button_cls);
        editText_port = (EditText) findViewById(R.id.editText_port);
        group_status = (TextView) findViewById(R.id.textView_group_status);
        editText_port.setText("25565");
        //events
        send.setOnClickListener(new MyOnClickListener());
        set.setOnClickListener(new MyOnClickListener());
        save.setOnClickListener(new MyOnClickListener());
        cls.setOnClickListener(new MyOnClickListener());
        //handler
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                Object new_list_string_object = bundle.get("message");
                if(!(new_list_string_object==null)){
                    String new_list_string = (String)new_list_string_object;
                    if(!(new_list_string ==null ||new_list_string.equals(""))){
                        chat_list.setText(new_list_string);
                    }
                }
                Object status_object = bundle.get("port_status");
                if(!(status_object == null)){
                    boolean status = (boolean) status_object;
                    if(!status){
                        group_status.setText("当前端口不可用,请更换");
                        is_usable = false;
                    }
                    else{
                        group_status.setText("连接成功");
                        is_usable = true;
                    }
                }
            }
        };
        //init
        MyServerSocket.getInstance().setContext(context);
    }
    private void resetScrolling(){
        int offset = chat_list.getLineCount()*chat_list.getLineHeight();
        if(offset>chat_list.getHeight()){
            chat_list.scrollTo(0,offset-chat_list.getHeight());
        }
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_setport:{
                    WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
                    if(!wifiManager.isWifiEnabled()){
                        //show not connected to wifi
                        group_status.setText("当前wifi不可用");
                        is_usable = false;
                    }
                    else {
                        //paste the code down here
                        MyServerSocket.getInstance().close_server();
                        connectWIFIAndStart(wifiManager,Integer.parseInt(editText_port.getText().toString()));
                    }
                    break;
                }
                case R.id.button_send:{
                    if(!is_usable){
                        group_status.setText("请检查连接");
                        break;
                    }
                    String message = chat_value.getText().toString();
                    if(message == null||message.equals("")){
                        group_status.setText("消息不能为空");
                        return;
                    }
                    String user_name_string = user_name.getText().toString();
                    if(user_name_string==null||user_name_string.equals("")){
                        //show user_name is null
                        group_status.setText("用户名不能为空");
                        return;
                    }
                    MyClientSocket.getInstance().setUser_name(user_name_string);
                    MyClientSocket.getInstance().sendMessage(message);
                    group_status.setText("消息发送成功");
                    chat_value.setText("");
                    resetScrolling();
                    break;
                }
                case R.id.button_cls:{
                    DialogList.getInstance().cls(context);
                    chat_list.setText("");
                    break;
                }
                case R.id.button_save:{
                    String username = user_name.getText().toString();
                    if(username==null||username.equals("")){
                        break;
                    }
                    DialogList.getInstance().outputUsername(context,username);
                    break;
                }
            }
        }
    }
    private Thread server_thread;
    @Override
    protected void onResume() {
        super.onResume();
        String username = DialogList.getInstance().readUserName(context);
        user_name.setText(username);
        boolean read_result = DialogList.getInstance().read(context);
        chat_list.setText(DialogList.getInstance().getList_string());
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            //show not connected to wifi
            group_status.setText("当前wifi不可用");
        }
        else {
            //paste the code down here
            connectWIFIAndStart(wifiManager,Integer.parseInt(editText_port.getText().toString()));

        }

    }

    private boolean connectWIFIAndStart(WifiManager wifiManager, int port_number){
        //info
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        long gateway_ip = dhcpInfo.gateway;
        String gateway_ip_string = longToIP(gateway_ip);
        long netmask_ip = dhcpInfo.netmask;
        String netmask_ip_string = longToIP(netmask_ip);
        //get broadcast_address
        String broadcast_address = longToIP(((~netmask_ip)|gateway_ip));
        //start server
        MyServerSocket.getInstance().setPort(port_number);
        MyServerSocket.getInstance().setHandler(handler);
        server_thread = new Thread(MyServerSocket.getInstance());
        server_thread.start();
        //start client
        MyClientSocket.getInstance().setBroadcast_ip(broadcast_address);
        MyClientSocket.getInstance().setPort(port_number);
        //check_usable
        boolean is_usable = MyServerSocket.getInstance().isUsable();
        return is_usable;
    }


    @Override
    protected void onPause() {
        super.onPause();
        MyServerSocket.getInstance().close_server();
    }

    private String longToIP(long ip){
        StringBuffer sb=new StringBuffer();
        sb.append(String.valueOf((int)(ip&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>8)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>16)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>24)&0xff)));
        return sb.toString();
    }

}

