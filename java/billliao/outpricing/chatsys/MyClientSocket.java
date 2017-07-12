package billliao.outpricing.chatsys;

import java.io.*;
import java.net.*;
import java.util.*;

public class MyClientSocket {
	private static MyClientSocket thisInstance;
	public static MyClientSocket getInstance(){
		if(thisInstance == null){
			thisInstance = new MyClientSocket();
		}
		return thisInstance;
	}
	private MyClientSocket(){
		broadcast_ip = "192.168.1.255";
		port = 25565;
	}
	
	private String broadcast_ip;
	private int port;
	private String user_name;
	
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getBroadcast_ip() {
		return broadcast_ip;
	}
	public void setBroadcast_ip(String broadcast_ip) {
		this.broadcast_ip = broadcast_ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	private int MAX_LENGTH = 4096;
	
	public void sendMessage(String msg){
		//pre:msg!=null and msg!equals("")
		DatagramSocket datagramSocket = null;
		try{
			byte[] current_msg = msg.getBytes("utf-8");
			//if message too long, split it
			if(current_msg.length>4096){
				List<String> messages = new ArrayList<String>();
				int step_length = 100;
				int loop_time = (int)Math.ceil((double)msg.length()/step_length);
				int index =0;
				int msg_length = msg.length();
				while(loop_time!=0){
					if(index+step_length>step_length){
						messages.add(msg.substring(index));
					}
					else{
						messages.add(msg.substring(index,index+step_length));
					}
					index+=step_length;
					loop_time--;
				}
				for(int i =0;i!=messages.size();i++){
					//send_message
					sendMessage(user_name+" : "+messages.get(i));
				}
			}
			else{
				msg = user_name+" : "+msg;
				byte[] now_msg = msg.getBytes("utf-8");
				DatagramPacket datagramPacket = new DatagramPacket(now_msg, now_msg.length,InetAddress.getByName(broadcast_ip),port);
				SendMessageThread sendMessageThread = new SendMessageThread();
				sendMessageThread.setDatagramPacket(datagramPacket);
				Thread send_message_thread = new Thread(sendMessageThread);
				send_message_thread.start();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if(datagramSocket!=null){
				datagramSocket.close();
			}
		}
	}
}

class SendMessageThread implements Runnable{
	private DatagramPacket datagramPacket;
	public DatagramPacket getDatagramPacket() {
		return datagramPacket;
	}
	public void setDatagramPacket(DatagramPacket datagramPacket) {
		this.datagramPacket = datagramPacket;
	}

	@Override
	public void run() {
		DatagramSocket datagramSocket = null;
		try{
			datagramSocket = new DatagramSocket();
			datagramSocket.send(datagramPacket);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(datagramSocket!=null){
				datagramSocket.close();
			}
		}

	}
}
