package WoodHouse;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBController {
    Connection conn = null;
    Statement statement = null;
    public DBController(Connection conn) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
        int tits = 4;
        try {
            this.conn = conn;
            createStatement();
            System.out.println("This worked.");
            generateOrderSheet(tits);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createStatement() throws SQLException {
        statement = conn.createStatement();
    }

    /*
    * Pass the brand of the case as string and the price is returned.
    * */
    public BigDecimal getPrice(String brandName) throws SQLException {
        ResultSet results = statement.executeQuery("select price from brand where brand_name like '" + brandName +"'");
        BigDecimal answer = null;
        while(results.next()){
            answer = results.getBigDecimal(1);
        }
        return answer;
    }
    /*
    * Returns the number of rows given the table name and the column name.
    * */
    public int countGetter(String tableName,String columnName) throws SQLException {
        ResultSet results = statement.executeQuery("select count(" + columnName + ")" + " from " + tableName + "");
        int count = -1;
        while(results.next()){
            count = results.getInt(1);
        }
        return count;
    }
    /*
    *Given the name of the phone, brand, and color this returns the number of items in stock.
    * */
    public int getOnHand(String phoneName, String brandName, String color) throws SQLException {
        int phoneBrand = phoneBrandSubQuery(phoneName,brandName);
        int colorCode = colorSubQuery(color);
        int onHand = -1;

        ResultSet results = statement.executeQuery("select quantity_on_hand from product "
                + "where phone_brand_id = " + phoneBrand + " and color_id = " + colorCode);

        while(results.next()){
            onHand = results.getInt(1);
        }

        return onHand;
    }
    /*
    * Given the name of the phone and the brand this method returns the corresponding code
    * from the phone brand table. This is a separate method so I can re-use it elsewhere.
    * */
    private int phoneBrandSubQuery(String phoneName, String brandName) throws SQLException {
        System.out.println("passed into phoneBrandSubQuery: " + phoneName + " " + brandName);
        int phoneBrandId = -1;
        ResultSet results = statement.executeQuery(" select pbrand.pb_id "
                + "from phone p inner join("
                + "select pb.phone_brand_id pb_id, pb.phone_id p_id "
                + "from phone_brand pb inner join brand b "
                + "where pb.brand_id = b.brand_id and b.brand_name = '" + brandName + "') pbrand "
                + "where pbrand.p_id = p.phone_id and p.phone_name = '" + phoneName + "'");
        while(results.next()){
            phoneBrandId= results.getInt(1);

        }
        return phoneBrandId;
    }
    /*
    * Given the name of the color this returns the code of the color. This method is separate for later re-use.
    * */
    private int colorSubQuery(String color) throws SQLException {
        int colorId = -1;
        ResultSet results = statement.executeQuery("select color_id from color where "
                + "color_name = '"  + color + "'");

        while(results.next()){
            colorId = results.getInt(1);
        }
        return colorId;
    }
    /*
    * This method changes the quantity_on_hand in the product table. To add inventory pass a
    * positive integer, to reduce inventory pass a negative integer.
    * */
    public void updateQuantity(String phoneName, String brandName, String color, int quantity) throws SQLException {
        int phoneBrandId = phoneBrandSubQuery(phoneName, brandName);
        int colorId = colorSubQuery(color);
        int currentAmount = getOnHand(phoneName,brandName,color);
        int newAmount = currentAmount + quantity;

        System.out.println("Phone/brand/color/quantity/ phoneBrandId = " + phoneName + " " + brandName + " " + color + " " + quantity + " " + phoneBrandId);

        try {
            if (newAmount < 0) {
                System.out.println("You can't have less than zero on hand.");
                throw new Exception();
            }else{
                statement.executeUpdate("update product set quantity_on_hand = " + newAmount
                        + " where phone_brand_id = " + (short)phoneBrandId + " and color_id = " + (short)colorId);
            }
        }catch(Exception e){
            System.out.println("Didnt work");
            e.printStackTrace();
        }

    }

    public void generateOrderSheet(int qty) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
        DateFormat df = new SimpleDateFormat("dd/MM/yy \nHH:mm:ss");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));

        PrintWriter writer = new PrintWriter("OrderReport.txt", "UTF-8");
        writer.println("******** Order Report ********");
        writer.println(df.format(dateobj));
        writer.println("******************************");

        List<String> phones = getPhones(qty);
        List<String> cases = getCases(qty);
        List<String> colors = getColors(qty);
        List<String> quantities = getQuantities(qty);

        for(int i  = 0; i < phones.size()-1; i++){
            writer.println("Item: " + colors.get(i) + " " + phones.get(i) + " " + cases.get(i));
                  //  + "On Hand: " + quantities.get(i));
        }

        writer.close();


    }

    public List<String> getColors(int qty) throws SQLException {
        List<String> names = new ArrayList<>();
        ResultSet results = statement.executeQuery("select c.color_name" +
                " from color c inner join product p" +
                " where c.color_id = p.color_id" +
                " and p.quantity_on_hand < " + qty);
        while(results.next()){
            String temp = results.getString(1);
            names.add(temp);
        }

        return names;
    }

    public List<String>getPhones(int qty) throws SQLException {
        List<String> phones = new ArrayList<>();
        ResultSet results = statement.executeQuery("select p.phone_name " +
                "from phone p inner join ( " +
                "select pb.phone_brand_id phone_brand, pr.phone_brand_id prod_brand, pb.phone_id phone_id, " +
                "pr.quantity_on_hand quantity " +
                "from phone_brand pb inner join product pr " +
                "on pb.phone_brand_id = pr.phone_brand_id) products " +
                "on products.phone_id = p.phone_id  " +
                "where products.quantity < 4");
        while(results.next()){
            String temp = results.getString(1);
            phones.add(temp);
        }
        return phones;
    }

    public List<String>getCases(int qty) throws SQLException{
        List<String> cases = new ArrayList<>();
        ResultSet results = statement.executeQuery("select b.brand_name " +
                "from brand b inner join (" +
                "select pb.phone_brand_id phone_brand, pr.phone_brand_id prod_brand, pb.brand_id brand_id, " +
                "pr.quantity_on_hand quantity " +
                "from phone_brand pb inner join product pr " +
                "on pb.phone_brand_id = pr.phone_brand_id) products " +
                "on products.brand_id = b.brand_id " +
                "where products.quantity < 4");
        while(results.next()){
            String temp = results.getString(1);
            cases.add(temp);
        }
        return cases;
    }

    public List<String>getQuantities(int qty) throws SQLException{
        List<String> quantities = new ArrayList();
        ResultSet results = statement.executeQuery("select quantity_on_hand " +
                "from product " +
                "where quantity_on_hand < " + qty);
        return quantities;
    }
}

