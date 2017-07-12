package billliao.outpricing.dao;

/**
 * Created by billliao on 2017/6/30.
 */
public class PricingObjectBean {
    private String id;
    private String name;
    private String description;
    private String org_tag;
    /*
     * a,b,c...common
     */
    private String price;
    private String price_country;
    /*
     * dollar,rmb,rsd,rubble,euro
     */
    private String price_vector;
    /*
     * out,in,exchange
     */
    private String exchange_price;
    private String exchange_price_country;
    /*
     * dollar,rmb,rsd,rubble,euro
     */
    private String today_time;
    private String pricing_date;
    private String utc;
    /*
     * E1,E2,E3...W1,W2,W3
     */
    private String place;
    /*
     * serbia,moscow,china
     */
    private String pricing_tag;
    /*
     * play,travel,live,eat,use
     */
    public void set(String name
            ,String description
            ,String org_tag
            ,String price
            ,String price_country
            ,String price_vector
            ,String exchange_price
            ,String exchange_price_country
            ,String today_time
            ,String pricing_date
            ,String utc
            ,String place
            ,String pricing_tag){
        this.name = name;
        this.description = description;
        this.org_tag = org_tag;
        this.price = price;
        this.price_country = price_country;
        this.price_vector = price_vector;
        this.exchange_price = exchange_price;
        this.exchange_price_country = exchange_price_country;
        this.today_time = today_time;
        this.pricing_date = pricing_date;
        this.utc = utc;
        this.place = place;
        this.pricing_tag = pricing_tag;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getOrg_tag() {
        return org_tag;
    }
    public void setOrg_tag(String org_tag) {
        this.org_tag = org_tag;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice_country() {
        return price_country;
    }
    public void setPrice_country(String price_country) {
        this.price_country = price_country;
    }
    public String getPrice_vector() {
        return price_vector;
    }
    public void setPrice_vector(String price_vector) {
        this.price_vector = price_vector;
    }
    public String getExchange_price() {
        return exchange_price;
    }
    public void setExchange_price(String exchange_price) {
        this.exchange_price = exchange_price;
    }
    public String getExchange_price_country() {
        return exchange_price_country;
    }
    public void setExchange_price_country(String exchange_price_country) {
        this.exchange_price_country = exchange_price_country;
    }
    public String getToday_time() {
        return today_time;
    }
    public void setToday_time(String today_time) {
        this.today_time = today_time;
    }
    public String getPricing_date() {
        return pricing_date;
    }
    public void setPricing_date(String pricing_date) {
        this.pricing_date = pricing_date;
    }
    public String getUtc() {
        return utc;
    }
    public void setUtc(String utc) {
        this.utc = utc;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getPricing_tag() {
        return pricing_tag;
    }
    public void setPricing_tag(String pricing_tag) {
        this.pricing_tag = pricing_tag;
    }


}
