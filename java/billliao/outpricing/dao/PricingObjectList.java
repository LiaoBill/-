package billliao.outpricing.dao;


import android.content.*;
import android.os.Environment;

import java.io.*;
import java.util.*;
/**
 * Created by billliao on 2017/6/30.
 */

public class PricingObjectList {
    private static PricingObjectList thisInstance;
    public static PricingObjectList getInstance(){
        if(thisInstance == null){
            thisInstance = new PricingObjectList();
        }
        return thisInstance;
    }
    private PricingObjectList(){
        //constructor
        list = new ArrayList<PricingObjectBean>();
    }
    private List<PricingObjectBean> list;

    public boolean add(PricingObjectBean current,Context context){
        list.add(current);
        return output(context);
    }

    private int current_index;
    public void readyForNextAndPrevious(){
        current_index = -1;
    }
    public PricingObjectBean getNext(){
        if(list.size() == 0){
            return null;
        }
        current_index++;
        if(current_index>=list.size()){
            current_index = 0;
        }
        list.get(current_index).setId(Integer.toString(current_index));
        return list.get(current_index);
    }
    public PricingObjectBean getPrevious(){
        if(list.size() == 0){
            return null;
        }
        current_index--;
        if(current_index<0){
            current_index = list.size()-1;
        }
        list.get(current_index).setId(Integer.toString(current_index));
        return list.get(current_index);
    }

    public boolean updateWithID(String id,PricingObjectBean other,Context context){
        PricingObjectBean current = list.get(Integer.parseInt(id));
        current.setName(other.getName());
        current.setDescription(other.getDescription());
        current.setPrice(other.getPrice());
        current.setPrice_country(other.getPrice_country());
        current.setPrice_vector(other.getPrice_vector());
        current.setExchange_price(other.getExchange_price());
        current.setExchange_price_country(other.getExchange_price_country());
        current.setToday_time(other.getToday_time());
        current.setPricing_date(other.getPricing_date());
        current.setUtc(other.getUtc());
        current.setPlace(other.getPlace());
        current.setPricing_tag(other.getPricing_tag());
        boolean result = output(context);
        return result;
    }

    public boolean deleteWithID(String id,Context context){
        list.remove(Integer.parseInt(id));
        boolean result = output(context);
        return result;
    }

    //replace String utc_country
    public boolean SetTimeZone(String utc){
        if(!utc.matches("[ewEW][0-9]*")){
            return false;
        }
        String e_or_w = utc.substring(0,1);
        String num = utc.substring(1,2);
        String GMT_string = "GMT+"+num;
        TimeZone china = TimeZone.getTimeZone(GMT_string);
        TimeZone.setDefault(china);
        return true;
    }

    private String file_name = "outpricing_recorder.txt";
    public boolean output(Context context){
        FileOutputStream fileOutputStream =null;
        PrintWriter printWriter = null;
        try {
            fileOutputStream = context.openFileOutput(file_name,Context.MODE_PRIVATE);
            printWriter = new PrintWriter(fileOutputStream);
            for(int i =0;i!=list.size();i++){
                PricingObjectBean current = list.get(i);
                printWriter.println(current.getName());
                printWriter.println(current.getDescription());
                printWriter.println(current.getOrg_tag());
                printWriter.println(current.getPrice());
                printWriter.println(current.getPrice_country());
                printWriter.println(current.getPrice_vector());
                printWriter.println(current.getExchange_price());
                printWriter.println(current.getExchange_price_country());
                printWriter.println(current.getToday_time());
                printWriter.println(current.getPricing_date());
                printWriter.println(current.getUtc());
                printWriter.println(current.getPlace());
                printWriter.println(current.getPricing_tag());
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
        list.clear();
        FileInputStream inputStream = null;
        Scanner scanner = null;
        try {
            inputStream = context.openFileInput(file_name);
            scanner = new Scanner(inputStream);
            String line = null;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.equals("")){
                    break;
                }
                String name = line;
                String description = scanner.nextLine();
                String org_tag = scanner.nextLine();
                String price = scanner.nextLine();
                String price_country = scanner.nextLine();
                String price_vector = scanner.nextLine();
                String exchange_price = scanner.nextLine();
                String exchange_price_country = scanner.nextLine();
                String today_time = scanner.nextLine();
                String pricing_date = scanner.nextLine();
                String utc = scanner.nextLine();
                String place = scanner.nextLine();
                String pricing_tag = scanner.nextLine();
                PricingObjectBean pricingObjectBean = new PricingObjectBean();
                pricingObjectBean.set(name, description, org_tag, price, price_country, price_vector, exchange_price, exchange_price_country, today_time, pricing_date, utc, place, pricing_tag);
                list.add(pricingObjectBean);
            }
            scanner.close();
            inputStream.close();
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
    public String getListString(){
        String result = "";
        for(int i =0;i!=list.size();i++){
            PricingObjectBean current = list.get(i);
            result+="[---"+Integer.toString(i)+"---]"+"\r\n"
            +current.getName()+"---"
            +current.getDescription()+"\r\n"
            +current.getOrg_tag()+"\r\n"
            +current.getPrice()+"---"
            +current.getPrice_country()+"\r\n"
            +current.getPrice_vector()+"\r\n"
            +current.getExchange_price()+"---"
            +current.getExchange_price_country()+"\r\n"
            +current.getToday_time()+"---"
            +current.getPricing_date()+"\r\n"
            +current.getUtc()+"---"
            +current.getPlace()+"\r\n"
            +current.getPricing_tag()+"\r\n";
        }
        return result;
    }
    public String getTodayTime(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int seconds = Calendar.getInstance().get(Calendar.SECOND);
        return Integer.toString(hour)+":"+Integer.toString(minute)+":"+Integer.toString(seconds);
    }

    public String getTodayDate(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        month++;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
    }

    public void outputList(List<PricingObjectBean> list,PrintWriter printWriter){
        for(int i =0;i!=list.size();i++){
            PricingObjectBean pricingObjectBean = list.get(i);
            String name =pricingObjectBean.getName();
            String description = pricingObjectBean.getDescription();
            String org_tag = pricingObjectBean.getOrg_tag();
            String price =pricingObjectBean.getPrice();
            String price_country =pricingObjectBean.getPrice_country();
            String price_vector = pricingObjectBean.getPrice_vector();
            String exchange_price =pricingObjectBean.getExchange_price();
            String exchange_price_country = pricingObjectBean.getExchange_price_country();
            String today_time =pricingObjectBean.getToday_time();
            String pricing_date = pricingObjectBean.getPricing_date();
            String utc =pricingObjectBean.getUtc();
            String place =pricingObjectBean.getPlace();
            String pricing_tag =pricingObjectBean.getPricing_tag();
            printWriter.println(name+"---"+
                    description+"---"+
                    org_tag+"---"+
                    price+"---"+
                    price_country+"---"+
                    price_vector+"---"+
                    exchange_price+"---"+
                    exchange_price_country+"---"+
                    today_time+"---"+
                    pricing_date+"---"+
                    utc+"---"+
                    place+"---"+
                    pricing_tag);
        }
    }
    public String executeOrder(Context context, String order){
        output_temp(context,order);
        return read_temp(context);
    }
    public String executeDateStatistics(Context context, String place){
        output_temp2(context,place);
        return read_temp(context);
    }
    public String read_temp(Context context){
        String temp_file_name = "output_temp_file.txt";
        FileInputStream inputStream = null;
        Scanner scanner = null;
        try {
            inputStream = context.openFileInput(temp_file_name);
            scanner = new Scanner(inputStream);
            String line = null;
            String cons = "";
            boolean is_null = true;
            while(scanner.hasNextLine()){
                is_null = false;
                line = scanner.nextLine();
                cons+=line+"\r\n";
            }
            if(is_null){
                cons = null;
            }
            scanner.close();
            inputStream.close();
            return cons;
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
        return null;
    }
    public boolean output_temp2(Context context, String place){
        String temp_file_name = "output_temp_file.txt";
        FileOutputStream fileOutputStream =null;
        PrintWriter printWriter = null;
        try {
            fileOutputStream = context.openFileOutput(temp_file_name,Context.MODE_PRIVATE);
            printWriter = new PrintWriter(fileOutputStream);
            MainStatistic.output_EveryDayOutBy_place(list,place,printWriter);
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
    public boolean output_temp(Context context, String order){
        String temp_file_name = "output_temp_file.txt";
        FileOutputStream fileOutputStream =null;
        PrintWriter printWriter = null;
        try {
            fileOutputStream = context.openFileOutput(temp_file_name,Context.MODE_PRIVATE);
            printWriter = new PrintWriter(fileOutputStream);
            switch (order) {
                case "d":
                    MainStatistic.output_HomeDistributionAll(list,printWriter);
                    break;
                case "e":
                    MainStatistic.output_AllExchangeRecord(list,printWriter);
                    break;
                case "o":
                    MainStatistic.output_AllOutRecord(list,printWriter);
                    break;
                case "i":
                    MainStatistic.output_AllInRecord(list,printWriter);
                    break;
                case "t":
                    MainStatistic.outputETPLUSituation(list,printWriter);
                    break;
                default:
                    System.out.println("input invalid");
                    break;
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
}
