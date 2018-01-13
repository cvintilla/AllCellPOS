package WoodHouse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class AdminOnly {

    //initialize objects
    double hours;
    protected Scene AdminOnlyScene;
    protected static TableView employeeTable = new TableView();
    protected TextField phone, brand, color, quantity;
    protected CheckBox checkBoxAdmin, checkBoxEmployee;
    protected Boolean isAdmin;
    protected static ComboBox itemSelect, colorSelect, phoneSelect;


    protected static final ObservableList<Employees> data = FXCollections.observableArrayList();

    //constructor for adminOnly stage
    public AdminOnly(Stage primaryStage, DBController dbController) throws Exception {

        //set stage
        Stage parentStage = primaryStage;

        //create previous button
        Button backButton = new Button("Previous Screen");
        backButton.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        backButton.setOnAction(event -> {
            StartScreen.isAdmin = false;
            resetEmployeeTable();
            parentStage.setScene(MainScreen.mScene);
        });

        //add inventory button
        Button addInventory = new Button("ADD INVENTORY");
        addInventory.setOnAction(event -> {

            String phoneName = phoneSelect.getSelectionModel().getSelectedItem().toString().toLowerCase();
            String brandName = itemSelect.getSelectionModel().getSelectedItem().toString().toLowerCase();
            String c = colorSelect.getSelectionModel().getSelectedItem().toString().toLowerCase();
            int q = Integer.parseInt(quantity.getText());

            try {
                MainScreen.db.updateQuantity(phoneName, brandName, c, q);

            } catch (SQLException sqlE) {
                System.out.println("Unable to Checkout");
                sqlE.printStackTrace();
            }
            quantity.setText("");
            colorSelect.getSelectionModel().selectFirst();
            phoneSelect.getSelectionModel().selectFirst();
            itemSelect.getSelectionModel().selectFirst();
        });
        addInventory.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");

        //phone textfield


        phoneSelect = new ComboBox(FXCollections.observableArrayList("iphone7 plus", "iphone7", "iphone6 Plus",
                "iphone6", "iphone se","moto z force","moto z","moto z droid","moto G4 plus",
                "moto G4","moto G4 play","moto x pure","moto G3","droid turbo 2","huawei mate 9"
                ,"nexus 6P","huawei p8Lite","LG Tribute HD","LG Pheonix 2","LG Sylo 2","LG Treasure LTE"
                ,"LG G5","LG X Power","LG Escape 2","LG V10","LG K7 3G","LG G Flex 2","Nokia 6","Nokia 5"
                ,"Nokia 3","Nokia 3310","Nokia 150","Nokia 105","Nokia 230","Nokia 222","Nokia 216",
                "Nokia 130","Galaxy S7 Edge","Galaxy S6 Edge","Galaxy Note 5","Galaxy Note Edge",
                "Galaxy Note 4","Galaxy Note 3","Galaxy S4 Mini","Galaxy J7","Galaxy J3",
                "Galaxy Mega","ZTE Axon 7","ZTE Avid 828","ZTE Blade V8 Pro","ZTE Axon 7 Mini",
                "ZTE ZMAX PRO","ZTE ZMAX 2","ZTE GRAND X MAX 2","ZTE MAX DUO LTE","ZTE IMPERIAL MAX",
                "ZTE GRAND X3","ZTE GRAND MEMO 2","ZTE FANFARE","ZTE OVERTURE 2","ZTE WARP ELITE"));
        phoneSelect.getSelectionModel().selectFirst();

        //brand textfield


        itemSelect = new ComboBox(FXCollections.observableArrayList(
                "OtterBox defender", "OtterBox commuter", "OtterBox symmetry", "Belkin Grip Vue"
                , "Speck Candyshell", "Speck Wallet", "Bodyglove Carbon", "Charger Apple", "Charger Other",
                "Headphones jvc", "Headphones Apple", "Headphones Sony"));
        itemSelect.getSelectionModel().selectFirst();

        //color textfield
        colorSelect = new ComboBox(FXCollections.observableArrayList(
                "Clear", "Black", "Red", "Green", "Yellow", "Blue", "Orange", "White", "Graphics", "Pink"));
        colorSelect.getSelectionModel().selectFirst();

        //quantity textfield
        quantity = new TextField();
        quantity.setPromptText("Quantity");


        //amount textfield
        TextField amount = new TextField();
        amount.setPromptText("Quantity");

//GenOrderButton
        Button order = new Button("Generate Order Report");
        order.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        order.setVisible(true);

        order.setOnAction(event -> {
            try {
                dbController.generateOrderSheet(Integer.parseInt(amount.getText()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        //create hours report button
        Button hoursButton = new Button("Generate Hours Report");
        hoursButton.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        hoursButton.setVisible(true);
        hoursButton.setOnAction(event-> {
            createHoursReport();
        });



        //center image
        Image image = new Image(StartScreen.logo, 500, 600, true, true);
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setCache(true);

        BorderPane leftBorderPane = new BorderPane();
        String leftImage = MainScreen.class.getResource("\\inventory.jpg").toExternalForm();
        leftBorderPane.setStyle("-fx-background-image: url('" + leftImage + "');");

        BorderPane rightBorderPane = new BorderPane();
        String rightImage = MainScreen.class.getResource("\\employee.jpg").toExternalForm();
        rightBorderPane.setStyle("-fx-background-image: url('" + rightImage + "');");

        //gridPane creation
        GridPane leftGridPane = new GridPane();
        //buffer1.setOpacity(.80);
        leftGridPane.setStyle("-fx-border-color: black;");
        leftGridPane.setPrefSize(650, 200);
        leftGridPane.setHgap(15);
        leftGridPane.setVgap(10);
        leftGridPane.setAlignment(Pos.CENTER);
        leftGridPane.setPadding(new Insets(10, 10, 10, 50));
        leftGridPane.setConstraints(phoneSelect, 0, 0);
        leftGridPane.setConstraints(itemSelect, 0, 1);
        leftGridPane.setConstraints(colorSelect, 0, 2);
        leftGridPane.setConstraints(quantity, 0, 3);
        leftGridPane.setConstraints(addInventory, 0, 4);
        leftGridPane.setConstraints(order, 0, 15);
        leftGridPane.setConstraints(amount, 0, 14);
        leftGridPane.getChildren().addAll(addInventory, phoneSelect, itemSelect, colorSelect, quantity, order, amount);
        leftBorderPane.setCenter(leftGridPane);


        //New Username
        TextField username = new TextField();
        username.setPromptText("New Username");

        //New Password
        TextField password = new TextField();
        password.setPromptText("New Password");

        //employee table setup
        TableColumn tableTitle = new TableColumn("Employee");
        tableTitle.setPrefWidth(320);
        tableTitle.setCellValueFactory(
                new PropertyValueFactory<Employees, String>("name"));

        TableColumn tableTitle2 = new TableColumn("Hours");
        tableTitle2.setPrefWidth(100);
        tableTitle2.setCellValueFactory(new PropertyValueFactory<Employees, String>("time"));

        checkBoxAdmin = new CheckBox("Admin");
        checkBoxAdmin.setStyle("-fx-text-fill: white;-fx-font: 22 arial;");
        checkBoxAdmin.setOnAction(event -> {
            isAdmin = true;
            checkBoxEmployee.setSelected(false);
        });

        checkBoxEmployee = new CheckBox("Employee");
        checkBoxEmployee.setStyle("-fx-text-fill: white;-fx-font: 22 arial;");
        checkBoxEmployee.setOnAction(event -> {
            isAdmin = false;
            checkBoxAdmin.setSelected(false);

        });

        HBox checkGroup = new HBox();
        checkGroup.setSpacing(30);
        checkGroup.getChildren().addAll(checkBoxAdmin, checkBoxEmployee);


        //addEmployee button
        Button addEmployee = new Button("ADD Employee");
        addEmployee.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");

        //set addEmployee Action
        addEmployee.setOnAction(event -> {
            data.clear();
            String hours = null;
            String flag = null;

            if (isAdmin) {
                flag = "true";
            } else if (!isAdmin) {
                flag = "false";
            }

            try {
                //connect to database, initialize statements
                Connection connection = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir"), "sa", "");
                Statement statement = connection.createStatement();
                Statement statement1 = connection.createStatement();
                Statement statement2 = connection.createStatement();

                //statement 2 inserts a new employee object into user table
                statement2.execute("insert into user(username, password, adminFlag, clockIn) " +
                        "values('" + username.getText().toLowerCase() +
                        "','" + password.getText().toLowerCase() +
                        "','" + flag + "', '00:00:00');");

                ResultSet results1 = statement.executeQuery("select hoursWorked from user");

                //selects clockin and username from user table
                ResultSet results2 = statement1.executeQuery("select username from user");
                String name;

                while (results2.next() && results1.next()) {
                    int time = results1.getInt("hoursWorked");
                    hours = time + "";
                    name = results2.getString(1);
                    data.add(new Employees(name, hours));
                }

            } catch (SQLException s) {
                s.printStackTrace();
            }

            username.setText("");
            password.setText("");
            employeeTable.setItems(data);
        });

        //adds columns to employee table
        employeeTable.getColumns().addAll(tableTitle, tableTitle2);
        employeeTable.setEditable(true);
        employeeTable.setVisible(true);

        Button removeEmployee = new Button("Remove Employee");
        removeEmployee.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        removeEmployee.setOnAction(event -> {
            Employees empObj = (Employees) employeeTable.getSelectionModel().getSelectedItem();
            employeeTable.getItems().remove(empObj);
            String name = empObj.getName();
            try {
                //connect to database, initialize statements
                Connection connection = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir"), "sa", "");
                Statement statement = connection.createStatement();

                //statement drops employee from the user table.
                statement.execute("delete from user\n" +
                        "where username = '" + name + "';");

            } catch (SQLException s) {
                s.printStackTrace();
            }

        });

        //EmployeePane
        GridPane rightGridpane = new GridPane();
        // rightGridpane.setOpacity(.80);
        rightGridpane.setStyle("-fx-border-color: black;");
        rightGridpane.setPrefSize(650, 200);
        rightGridpane.setHgap(15);
        rightGridpane.setVgap(10);
        rightGridpane.setAlignment(Pos.CENTER);
        rightGridpane.setPadding(new Insets(10, 10, 10, 50));
        rightGridpane.setRowIndex(username, 1);
        rightGridpane.setRowIndex(password, 2);
        rightGridpane.setRowIndex(checkGroup, 3);
        rightGridpane.setRowIndex(addEmployee, 4);
        rightGridpane.setRowIndex(removeEmployee, 6);
        rightGridpane.setRowIndex(employeeTable, 5);
        rightGridpane.getChildren().addAll(addEmployee, removeEmployee,
                hoursButton, checkGroup, username, password, employeeTable);
        rightBorderPane.setCenter(rightGridpane);


        //back button
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setPrefHeight(50);
        hbox.setStyle("-fx-background-color: white;");
        hbox.getChildren().addAll(backButton);

        //sets borderpane
        BorderPane layout = new BorderPane();
        String background = MainScreen.class.getResource("\\light.jpg").toExternalForm();
        // layout.setStyle("-fx-background-image: url('" + background + "');");
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setStyle("-fx-background-color: white;-fx-border-color: black;");
        layout.setCenter(iv1);
        layout.setLeft(leftBorderPane);
        layout.setRight(rightBorderPane);
        layout.setTop(hbox);

        //create and set scene
        AdminOnlyScene = new Scene(layout, 1800, 900);
        parentStage.setScene(AdminOnlyScene);
        parentStage.show();
    }

    public static void resetEmployeeTable() {
        employeeTable.getColumns().clear();
        data.clear();
        employeeTable.setItems(data);
    }

    public void createHoursReport(){
        //initialize writer
        PrintWriter writer = null;

        try{
            Connection connection = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir"), "sa", "");
            Statement query = connection.createStatement();
            ResultSet results;
            writer = new PrintWriter("HoursWorkedReport.txt");

            writer.println("************* Hours Worked Report *************");
            writer.println(" ");

            //query entire user table
            results = query.executeQuery("select username, hoursWorked from user");

            while(results.next()){
                writer.println(results.getString("username") +
                        " has worked " +
                        results.getInt("hoursWorked") +
                        " hours since last payment period");
            }

            writer.println("***************************************");

            //remove old hours worked from database
            Statement erase = connection.createStatement();
            erase.execute("update user set hoursWorked = 0;");

        }catch(SQLException e){
            e.printStackTrace();
        }
        catch(IOException f){
            f.printStackTrace();
        }
        finally {
            //close writer
            if(writer != null)
                writer.close();
        }
    }
}
