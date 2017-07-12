package billliao.outpricing.dao;

/**
 * Created by billliao on 2017/7/1.
 */
import java.util.*;
public class StasticDataStructure {
    public String country;
    public String pricing_date;
    public List<PricingObjectBean> play_list;
    public List<PricingObjectBean> eat_list;
    public List<PricingObjectBean> live_list;
    public List<PricingObjectBean> walk_list;
    public double[] sums;
    public StasticDataStructure() {
        // TODO Auto-generated constructor stub
        play_list = new ArrayList<PricingObjectBean>();
        eat_list = new ArrayList<PricingObjectBean>();
        live_list = new ArrayList<PricingObjectBean>();
        walk_list = new ArrayList<PricingObjectBean>();
        sums = new double[5];
    }
}

