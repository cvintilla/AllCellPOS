
package WoodHouse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBLauncher {

    //DBLauncher constructor
    public DBLauncher(Connection conn) {
        try {
            //creates user table in database
            createUserTable(conn);

            //creates product tables in database
            createProductDB(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*This method is only responsible for creating the User table which is not
    * related to the other tables in any way.*/
    public void createUserTable(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();

        //Creating the user table
        try {
            //creates user table
            statement.execute("create table user (" +
                    "username varchar(20), " +
                    "password varchar(20), " +
                    "adminFlag boolean, " +
                    "clockIn time, " +
                    "hoursWorked int, " +
                    "constraint pk_user primary key(username));");

            //hard code test user
            statement.execute("insert into user(username, password, adminFlag, clockIn) " +
                    "values('admintest','admin', true, '00:00:00');");

        } catch (SQLException e) {
            System.out.println("The user table has already been created.");
        }
    }

    /*This method is responsible for creating all the other tables related
    * to specific products in the DB.*/
    private void createProductDB(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();

        //Creating the Phone table
        try {
            statement.execute("create table Phone(" +
                    "phone_ID smallint unsigned not null, " +
                    "phone_name varchar(20) not null, " +
                    "constraint pk_phone_ID primary key(phone_ID));");

        } catch (SQLException e) {
            System.out.println("The Phone table has already been created.");
        }

        //Creating the Brand table
        try {
            statement.execute("create table Brand(" +
                    "brand_ID smallint unsigned not null, " +
                    "brand_name varchar(20) not null, " +
                    "price decimal(4,2) not null," +
                    "constraint pk_brand_ID primary key(brand_ID));");

        } catch (SQLException e) {
            System.out.println("The Brand table has already been created.");
        }

        //Creating the Phone_Brand table
        try {
            statement.execute("create table Phone_Brand(" +
                    "phone_brand_ID smallint unsigned not null, " +
                    "brand_ID smallint unsigned not null, " +
                    "phone_ID smallint unsigned not null," +
                    "constraint pk_phone_brand_ID primary key(phone_brand_ID)," +
                    "constraint fk_brand_ID foreign key (brand_ID)" +
                    "references Brand(brand_ID)," +
                    "constraint fk_phone_id foreign key (phone_ID)" +
                    "references Phone(phone_ID));");

        } catch (SQLException e) {
            System.out.println("The Phone_Brand table has already been created.");
        }

        //Creating the Color table
        try {
            statement.execute("create table Color(" +
                    "color_ID smallint unsigned not null," +
                    "color_name varchar(20) not null," +
                    "constraint pk_color_ID primary key(color_ID));");

        } catch (SQLException e) {
            System.out.println("The Color table has already been created.");
        }

        //Creating the Product table
        try {
            statement.execute("create table Product(" +
                    "id smallint unsigned not null," +
                    "phone_brand_ID smallint unsigned not null," +
                    "color_ID smallint unsigned not null," +
                    "quantity_on_hand smallint not null," +
                    "constraint pk_Product_ID primary key(id)," +
                    "constraint fk_phone_brand_ID foreign key(phone_brand_ID)" +
                    "references Phone_Brand(phone_brand_ID)," +
                    "constraint fk_color_ID foreign key(color_ID)" +
                    "references Color(color_ID));");

        } catch (SQLException e) {}
    }
}
