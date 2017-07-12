package billliao.outpricing.chatsys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.*;
import java.net.*;
import java.util.*;


public class MyServerSocket implements Runnable {
	public static MyServerSocket thisInstance;
	public static MyServerSocket getInstance(){
		if(thisInstance == null){
			thisInstance = new MyServerSocket();
		}
		return thisInstance;
	}
	private MyServerSocket(){
		port = 25565;
	}
	
	private int MAX_LENGTH = 4096;
	private int port;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	private DatagramSocket serverSocket;
    private Handler handler;
    public void setHandler(Handler handler){
        this.handler = handler;
    }
    private boolean is_usable = false;
    public boolean isUsable(){
        return is_usable;
    }
    private Context context;
    public void setContext(Context context){
        this.context = context;
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		serverSocket = null;
		try{
			serverSocket = new DatagramSocket(port);
            //let ui know:
            is_usable = true;
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("port_status",is_usable);
            message.setData(bundle);
            handler.sendMessage(message);
            is_close = false;

			while(!is_close){
				byte[] buffer = new byte[MAX_LENGTH];
				DatagramPacket p = new DatagramPacket(buffer,MAX_LENGTH);
				serverSocket.receive(p);
				String message_string = new String(p.getData(), 0, p.getLength(), "utf-8");
				DialogList.getInstance().add(message_string,handler,context);
				//System.out.println(message_string);
			}
		}
		catch(Exception e){
			//e.printStackTrace();
            is_usable = false;
            is_close = false;
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("port_status",is_usable);
            message.setData(bundle);
            handler.sendMessage(message);
		}
		finally{
			if(serverSocket!=null){
				serverSocket.close();
			}
			is_close = false;
            is_usable = false;
		}
	}
	private boolean is_close = false;
	public void close_server(){
		is_close = true;
        if(serverSocket!=null){
            serverSocket.close();
        }
	}
	
}
