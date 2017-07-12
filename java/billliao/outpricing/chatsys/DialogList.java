package billliao.outpricing.chatsys;

import android.content.Context;
import android.os.*;
import java.io.*;
import java.util.*;

public class DialogList {
	public static DialogList thisInstance;
	public static DialogList getInstance(){
		if(thisInstance == null){
			thisInstance = new DialogList();
		}
		return thisInstance;
	}
	private DialogList(){
		list_string = "";
		string_list = new ArrayList<String>();
	}
	private String list_string;
	private List<String> string_list;
	public String getList_string() {
		return list_string;
	}
	public void setList_string(String list_string) {
		this.list_string = list_string;
	}
	public void add(String line,Handler handler,Context context){
		string_list.add(line);
		list_string+=line+"\r\n";
		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("message",list_string);
		message.setData(bundle);
		handler.sendMessage(message);
        output(context);
	}
	private String file_name = "group_chat_recorder.txt";
	public boolean output(Context context){
		FileOutputStream fileOutputStream =null;
		PrintWriter printWriter = null;
		try {
			fileOutputStream = context.openFileOutput(file_name,Context.MODE_PRIVATE);
			printWriter = new PrintWriter(fileOutputStream);
			for(int i =0;i!=string_list.size();i++){
                printWriter.println(string_list.get(i));
			}
			printWriter.close();
			fileOutputStream.close();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(printWriter!=null){
					printWriter.close();
				}
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
			}
			catch(Exception e){

			}
		}
		return false;
	}
	public boolean read(Context context){
		string_list.clear();
		FileInputStream inputStream = null;
		Scanner scanner = null;
		try {
			inputStream = context.openFileInput(file_name);
			scanner = new Scanner(inputStream);
			while(scanner.hasNextLine()){
                string_list.add(scanner.nextLine());
			}
			scanner.close();
			inputStream.close();
            getStringFromList();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(scanner!=null){
					scanner.close();
				}
				if(inputStream!=null){
					inputStream.close();
				}
			}
			catch(Exception e){

			}
		}
		return false;
	}
    public void cls(Context context){
        string_list.clear();
        list_string = "";
        output(context);
    }
    private void getStringFromList(){
        list_string="";
        for(int i =0;i!=string_list.size();i++){
            String current = string_list.get(i);
            list_string+=current+"\r\n";
        }
    }
    public void outputUsername(Context context,String username){
        String file_name_username = "username_record.txt";
        FileOutputStream fileOutputStream =null;
        PrintWriter printWriter = null;
        try {
            fileOutputStream = context.openFileOutput(file_name_username,Context.MODE_PRIVATE);
            printWriter = new PrintWriter(fileOutputStream);
            printWriter.println(username);
            printWriter.close();
            fileOutputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(printWriter!=null){
                    printWriter.close();
                }
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
            }
            catch(Exception e){

            }
        }
    }
    public String readUserName(Context context){
        String file_name_username = "username_record.txt";
        FileInputStream inputStream = null;
        Scanner scanner = null;
        String username = null;
        try {
            inputStream = context.openFileInput(file_name_username);
            scanner = new Scanner(inputStream);
            username = "";
            while(scanner.hasNextLine()){
                username = scanner.nextLine();
            }
            scanner.close();
            inputStream.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(scanner!=null){
                    scanner.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
            }
            catch(Exception e){

            }
            return username;
        }
    }
}
