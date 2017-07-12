package billliao.outpricing.dao;

/**
 * Created by billliao on 2017/7/6.
 */

import java.io.*;
import java.math.*;
import java.util.*;

public class MainStatistic {
    private static final double DOLLAR = 6.7980;// america
    private static final double RUBBLE = 0.1142;// moscow
    private static final double EURO = 7.7218;// euro
    private static final double RSD = 0.0641;// serbia
    private static final String[] ORG_TAG = new String[] { "a", "b", "c", "common" };
    private static final String[] PRICING_TAG = new String[] { "eat", "travel", "live", "play", "use" };
    // 给出每家的支出分配
    // output_HomeDistributionAll(list);
    // output_AllExchangeRecord(list);
    // output_AllOutRecord(list);
    // output_AllInRecord(list);
    // outputETPLUSituation(list);
    // output_EveryDayOutBy_place(list,"china");
    // 取得整个旅行中的eat,travel,play,live,use消费值和比重
    public static void outputETPLUSituation(List<PricingObjectBean> list, PrintWriter printWriter) {
        double[] cons = new double[6];
        for (int i = 0; i != PRICING_TAG.length; i++) {
            String pricing_tag = PRICING_TAG[i];
            printWriter.println(pricing_tag);
            List<PricingObjectBean> ls = getListBy_pricing_tag(getAllOutRecord(list), pricing_tag);
            output_AllMoneyRecordBy_list(ls,  printWriter);
            String sum = get_AllMoneyRecordBy_list(ls);
            cons[i] = Double.parseDouble(sum);
        }
        cons[5] = cons[0] + cons[1] + cons[2] + cons[3] + cons[4];
        for (int i = 0; i != PRICING_TAG.length; i++) {
            String pricing_tag = PRICING_TAG[i];
            printWriter.println(pricing_tag);
            double percentage = cons[i] / cons[5];
            String percentage_string = null;
            if (Double.isNaN(percentage)) {
                percentage_string = "0%";
            } else {
                percentage *= 100;
                BigDecimal temp = new BigDecimal(percentage);
                percentage = temp.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                percentage_string = Double.toString(percentage) + "%";
            }
            printWriter.println(percentage_string);
        }
    }

    public static List<PricingObjectBean> getListBy_pricing_tag(List<PricingObjectBean> list, String pricing_tag) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String pricing_tag_string = current.getPricing_tag();
            if (pricing_tag_string.equals(pricing_tag)) {
                cons.add(current);
            }
        }
        return cons;
    }

    // 取得特定国家的日程记录
    public static void output_EveryDayOutBy_place(List<PricingObjectBean> list, String place, PrintWriter printWriter) {
        printWriter.println(place + " : ");
        List<PricingObjectBean> place_list = getListBy_place(getAllOutRecord(list), place);
        List<String> date_strings = getDateStringList(place_list);
        for (int i = 0; i != date_strings.size(); i++) {
            String current_date = date_strings.get(i);
            printWriter.println(current_date);
            output_AllMoneyRecordBy_list(getListBy_pricing_date(place_list, current_date),  printWriter);
        }
    }

    public static List<PricingObjectBean> getListBy_pricing_date(List<PricingObjectBean> list, String pricing_date) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String date_string = current.getPricing_date();
            if (date_string.equals(pricing_date)) {
                cons.add(current);
            }
        }
        return cons;
    }

    public static List<String> getDateStringList(List<PricingObjectBean> list) {
        List<String> cons = new ArrayList<String>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String pricing_date = current.getPricing_date();
            boolean is_new = true;
            for (int j = cons.size() - 1; j >= 0; j--) {
                String current_date_string = cons.get(j);
                if (current_date_string.equals(pricing_date)) {
                    is_new = false;
                    break;
                }
            }
            if (is_new) {
                cons.add(pricing_date);
            }
        }
        return cons;
    }

    public static List<PricingObjectBean> getListBy_place(List<PricingObjectBean> list, String place) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String place_string = current.getPlace();
            if (place_string.equals(place)) {
                cons.add(current);
            }
        }
        return cons;
    }

    // 给出所有的收入,支出和换钱的记录
	/*
	 * [--dollar_sum(America)--0.0--] [--rmb_sum(China)--0.0--]
	 * [--rubble_sum(Moscow)--0.0--] [--rsd_sum(Serbia)--0.0--]
	 * [--euro_sum(Euro)--0.0--] {ALL}--0.0 RMB
	 * 晚饭---在一号街泰皇鸡吃晚饭---common---115.8---rmb---out---------20:5:54---2017-7-2--
	 * -E8---china---eat
	 * 早饭---老年超市买早饭---a---20.1---rmb---out---------20:5:54---2017-7-3---E8---
	 * china---eat
	 * 地铁---乘坐地铁---common---4.0---rmb---out---------20:5:54---2017-7-4---E8---
	 * china---travel [--dollar_sum(America)--0.0--] [--rmb_sum(China)--139.9--]
	 * [--rubble_sum(Moscow)--0.0--] [--rsd_sum(Serbia)--0.0--]
	 * [--euro_sum(Euro)--0.0--] {ALL}--139.9 RMB [--dollar_sum(America)--0.0--]
	 * [--rmb_sum(China)--0.0--] [--rubble_sum(Moscow)--0.0--]
	 * [--rsd_sum(Serbia)--0.0--] [--euro_sum(Euro)--0.0--] {ALL}--0.0 RMB
	 */
    // 给出所有的换钱记录
    public static void output_AllExchangeRecord(List<PricingObjectBean> list, PrintWriter printWriter) {
        printWriter.println("EXCHANGES:");
        List<PricingObjectBean> cons = getAllExchangeRecord(list);
        PricingObjectList.getInstance().outputList(cons,  printWriter);
        output_AllMoneyRecordBy_list(cons,  printWriter);
    }

    // 给出所有的支出记录
    public static void output_AllOutRecord(List<PricingObjectBean> list, PrintWriter printWriter) {
        printWriter.println("OUT:");
        List<PricingObjectBean> cons = getAllOutRecord(list);
        PricingObjectList.getInstance().outputList(cons,  printWriter);
        output_AllMoneyRecordBy_list(cons,  printWriter);
    }

    // 给出所有的收入记录
    public static void output_AllInRecord(List<PricingObjectBean> list, PrintWriter printWriter) {
        printWriter.println("IN:");
        List<PricingObjectBean> cons = getAllInRecord(list);
        PricingObjectList.getInstance().outputList(cons,  printWriter);
        output_AllMoneyRecordBy_list(cons,  printWriter);
    }

    private static String get_AllMoneyRecordBy_list(List<PricingObjectBean> list) {
        // printWriter.print("[--dollar_sum(America)--");
        double dollar = getPriceSum(getListBy_price_country(list, "dollar"));
        // printWriter.println(dollar+"--]");

        // printWriter.print("[--rmb_sum(China)--");
        double rmb = getPriceSum(getListBy_price_country(list, "rmb"));
        // printWriter.println(rmb+"--]");

        // printWriter.print("[--rubble_sum(Moscow)--");
        double rubble = getPriceSum(getListBy_price_country(list, "rubble"));
        // printWriter.println(rubble+"--]");

        // printWriter.print("[--rsd_sum(Serbia)--");
        double rsd = getPriceSum(getListBy_price_country(list, "rsd"));
        // printWriter.println(rsd+"--]");

        // printWriter.print("[--euro_sum(Euro)--");
        double euro = getPriceSum(getListBy_price_country(list, "euro"));
        // printWriter.println(euro+"--]");
        // all
        double sum = 0.0f;
        sum += dollar * DOLLAR + rmb + rubble * RUBBLE + rsd * RSD + euro * EURO;
        BigDecimal temp = new BigDecimal(sum);
        sum = temp.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(sum);
        // printWriter.print("{ALL}--");
        // printWriter.println(Double.toString(sum)+" RMB");
    }

    private static void output_AllMoneyRecordBy_list(List<PricingObjectBean> list, PrintWriter printWriter) {
        printWriter.print("[--dollar_sum(America)--");
        double dollar = getPriceSum(getListBy_price_country(list, "dollar"));
        printWriter.println(dollar + "--]");

        printWriter.print("[--rmb_sum(China)--");
        double rmb = getPriceSum(getListBy_price_country(list, "rmb"));
        printWriter.println(rmb + "--]");

        printWriter.print("[--rubble_sum(Moscow)--");
        double rubble = getPriceSum(getListBy_price_country(list, "rubble"));
        printWriter.println(rubble + "--]");

        printWriter.print("[--rsd_sum(Serbia)--");
        double rsd = getPriceSum(getListBy_price_country(list, "rsd"));
        printWriter.println(rsd + "--]");

        printWriter.print("[--euro_sum(Euro)--");
        double euro = getPriceSum(getListBy_price_country(list, "euro"));
        printWriter.println(euro + "--]");
        // all
        double sum = 0.0f;
        sum += dollar * DOLLAR + rmb + rubble * RUBBLE + rsd * RSD + euro * EURO;
        BigDecimal temp = new BigDecimal(sum);
        sum = temp.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        printWriter.print("{ALL}--");
        printWriter.println(Double.toString(sum) + " RMB");
    }

    private static List<PricingObjectBean> getAllExchangeRecord(List<PricingObjectBean> list) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String price_vector = current.getPrice_vector();
            if (price_vector.equals("exchange")) {
                cons.add(current);
            }
        }
        return cons;
    }

    private static List<PricingObjectBean> getAllOutRecord(List<PricingObjectBean> list) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String price_vector = current.getPrice_vector();
            if (price_vector.equals("out")) {
                cons.add(current);
            }
        }
        return cons;
    }

    private static List<PricingObjectBean> getAllInRecord(List<PricingObjectBean> list) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String price_vector = current.getPrice_vector();
            if (price_vector.equals("in")) {
                cons.add(current);
            }
        }
        return cons;
    }

    // 给出每家要付的输出:
	/*
	 * a [--dollar_sum(America)--0.0--] [--rmb_sum(China)--20.1--]
	 * [--rubble_sum(Moscow)--0.0--] [--rsd_sum(Serbia)--0.0--]
	 * [--euro_sum(Euro)--0.0--] {ALL}--20.1 RMB b
	 * [--dollar_sum(America)--0.0--] [--rmb_sum(China)--0.0--]
	 * [--rubble_sum(Moscow)--0.0--] [--rsd_sum(Serbia)--0.0--]
	 * [--euro_sum(Euro)--0.0--] {ALL}--0.0 RMB c [--dollar_sum(America)--0.0--]
	 * [--rmb_sum(China)--0.0--] [--rubble_sum(Moscow)--0.0--]
	 * [--rsd_sum(Serbia)--0.0--] [--euro_sum(Euro)--0.0--] {ALL}--0.0 RMB
	 * common [--dollar_sum(America)--0.0--] [--rmb_sum(China)--119.8--]
	 * [--rubble_sum(Moscow)--0.0--] [--rsd_sum(Serbia)--0.0--]
	 * [--euro_sum(Euro)--0.0--] {ALL}--119.8 RMB
	 */
    public static void output_HomeDistributionAll(List<PricingObjectBean> list, PrintWriter printWriter) {
        for (int i = 0; i != ORG_TAG.length; i++) {
            output_HomeDistributionBy_org_tag(list, ORG_TAG[i], printWriter);
        }
    }

    public static void output_HomeDistributionBy_org_tag(List<PricingObjectBean> list, String org_tag, PrintWriter printWriter) {
        List<PricingObjectBean> l1 = getOutListBy_org_tag(list, org_tag);
        printWriter.println(org_tag);
        output_AllMoneyRecordBy_list(l1, printWriter);
    }

    public static List<PricingObjectBean> getOutListBy_org_tag(List<PricingObjectBean> list, String org_tag) {
        List<PricingObjectBean> out_list = getAllOutRecord(list);
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != out_list.size(); i++) {
            PricingObjectBean current = out_list.get(i);
            String org_tag_string = current.getOrg_tag();
            if (org_tag_string.equals(org_tag)) {
                cons.add(current);
            }
        }
        return cons;
    }

    public static List<PricingObjectBean> getListBy_price_country(List<PricingObjectBean> list, String price_country) {
        List<PricingObjectBean> cons = new ArrayList<PricingObjectBean>();
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            String p_cString = current.getPrice_country();
            if (p_cString.equals(price_country)) {
                cons.add(current);
            }
        }
        return cons;
    }

    public static double getPriceSum(List<PricingObjectBean> list) {
        // 默认所有的price_country都是一样的一个list
        double sum = 0.0f;
        for (int i = 0; i != list.size(); i++) {
            PricingObjectBean current = list.get(i);
            double c = Double.parseDouble(current.getPrice());
            sum += c;
        }
        BigDecimal temp = new BigDecimal(sum);
        sum = temp.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return sum;
    }
}
