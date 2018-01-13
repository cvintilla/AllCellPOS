package WoodHouse;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static javafx.application.Application.launch;


public class MainScreen {

    protected static List<Cart> cartList = new ArrayList<Cart>();

    //initialize MainScreen variables and objects
    protected static Scene mScene;
    protected static String phoneName;
    protected static Button adminButton = new Button("Admin Only");
    protected static Button button1, button2, button3, button4, button5, button6, button7, addButton;
    protected static ArrayList<String> logoStringArray = new ArrayList<String>();
    protected static ArrayList<String> modelStringArray = new ArrayList<String>();
    protected static ComboBox itemSelect, colorSelect;
    protected static TextField taxBox, totalBox;
    protected static Label colorLabel,itemLabel;

    //radio buttons for color scheme
    protected static RadioButton rb1, rb2, rb3, rb4;

    //initialize border, and trees
    protected static BorderPane border = new BorderPane();
    protected static FlowPane appleModel, motorolaModels, huaweiModels, lgModels, nokiaModels, samsungModels, zteModels;
    protected static TreeItem<String> Products, Brands;
    protected static TreeView<String> tree;
    protected static GridPane productsPage, gridPane;
    protected static HBox hbox;
    protected static HBox hbox2 = new HBox();
    //initialize different scrollpanes
    protected static ScrollPane brandsScrollPane = new ScrollPane();
    protected static ScrollPane motorolaScrollPane = new ScrollPane();
    protected static ScrollPane lgScrollPane = new ScrollPane();
    protected static ScrollPane nokiaScrollPane = new ScrollPane();
    protected static ScrollPane samsungScrollPane = new ScrollPane();
    protected static ScrollPane zteScrollPane = new ScrollPane();
    protected static ScrollPane appleScrollPane = new ScrollPane();
    protected static ScrollPane huaweiScrollPane = new ScrollPane();

    //logoutButton
    protected static Button logOut;
    protected static VBox vbox2;
    protected static VBox vbox;
    protected static Button checkOut;
    protected static Button removeFromCart;

    //initialize table
    protected static TableView table = new TableView();
    final static ObservableList<Cart> data = FXCollections.observableArrayList();

    //initialize Database
    protected static DBController db = null;
    protected static Stage mainStage = new Stage();

    protected static DecimalFormat df = new DecimalFormat("#.##");
    protected static Label signedIn, tax, total;
    protected static double totalPrice = 0;
    protected static double taxAmount = 0;

    //MainScreen Constructor
    public MainScreen(Stage parentStage, DBController dbController) throws Exception {

        //sets stage
        //sets database
        this.db = dbController;
        mainStage = parentStage;



        //sets main scene
        mScene = new Scene(border, 1720, 900);
        initialize();

    }

    public static void reset(){
        StartScreen.isAdmin = false;
        border.setRight(brandsScrollPane);
        data.clear();
        table.setItems(data);
        taxAmount = 0;
        totalPrice = 0;
        taxBox.setText("");
        totalBox.setText("");
        itemSelect.getSelectionModel().selectFirst();
        colorSelect.getSelectionModel().selectFirst();
        adminButton.setVisible(false);
        updatePanes();
        String image = MainScreen.class.getResource("\\light.jpg").toExternalForm();
        border.setStyle("-fx-background-image: url('" + image + "');");
        logOut.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        vbox2.setOpacity(.85);
        vbox.setOpacity(.85);
        addButton.setStyle("-fx-font: 22 tacoma; -fx-base: #FF8C00;");
        removeFromCart.setStyle("-fx-font: 12 arial; -fx-base: #FF8C00;");
        checkOut.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        hbox2.setStyle("-fx-background-color: white;");
        hbox.setStyle("-fx-background-color: white;");
        adminButton.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        tax.setStyle("-fx-text-fill: black;-fx-font: 22 arial;");
        total.setStyle("-fx-text-fill: black;-fx-font: 22 arial;");
        rb1.setSelected(false);
        rb2.setSelected(false);
        rb3.setSelected(false);
    }

    public static void initialize(){
        //Products
        Products = new TreeItem<>();
        Products.setExpanded(true);


        // making the treeview of the different brands
        Brands = makeBranch("Brands", Products);
        Brands.setExpanded(true);
        makeBranch("Apple", Brands);
        makeBranch("Huawei", Brands);
        makeBranch("Motorola", Brands);
        makeBranch("LG", Brands);
        makeBranch("Nokia", Brands);
        makeBranch("Samsung", Brands);
        makeBranch("ZTE", Brands);

        if (logoStringArray.isEmpty() && modelStringArray.isEmpty()) {

            // loading file paths to logo pictures
            for (int i = 1; i < 8; i++) {
                String word = MainScreen.class.getResource("\\pictures\\logo" + i + ".png").toExternalForm();
                logoStringArray.add(word);
            }

            // loading file paths to model pictures
            for (int i = 0; i < 64; i++) {
                String word = MainScreen.class.getResource("\\pictures\\model" + i + ".png").toExternalForm();
                modelStringArray.add(word);
            }
        }


        // making logout button
        logOut = new Button("Log-Out");
        logOut.setOnAction(event -> {

            try{
                logOut();
                StartScreen.initialize();
                reset();
                mainStage.setScene(StartScreen.initialScene);
            }catch(Exception e){

            }

        });
        logOut.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");

        // making admin button
        adminButton.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
        adminButton.setOnAction(event -> {
            try {
                new AdminOnly(mainStage, db);
                setEmployeeTable();
            } catch (Exception e) {}
        });


        //Buttons for each brand name
        setBrandButtons();

        //calls methods to create phone model layouts
        createApple();
        createMotorola();
        createHuawei();
        createLG();
        createNokia();
        createSamsung();
        createZTE();

        // Creating Brand Name button layout
        FlowPane brandsFlowPane = new FlowPane();
        brandsFlowPane.getChildren().addAll(button1, button2, button3, button4, button5, button6, button7);
        CreateRightPane(brandsFlowPane);
        brandsScrollPane.setContent(brandsFlowPane);
        brandsScrollPane.setPrefSize(500, 900);

        // used for checkout table
        checkOut = new Button("Check-Out");
        checkOut.setOnAction(event -> {
            removeFromDataBase();
        });
        checkOut.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");


        // creating remove from cart button
        removeFromCart = new Button("Remove Item");
        removeFromCart.setOnAction(event -> {
            removeItemFromCart();
        });
        removeFromCart.setStyle("-fx-font: 12 arial; -fx-base: #FF8C00;");

        //creating label and textField for the total.
        total = new Label("Total: ");
        total.setStyle("-fx-text-fill: black;-fx-font: 22 arial;");
        totalBox = new TextField();
        totalBox.setPrefWidth(20);
        totalBox.setEditable(false);


        //creates tax label
        tax = new Label("Tax: ");
        tax.setStyle("-fx-text-fill: black;-fx-font: 22 arial;");
        taxBox = new TextField();
        taxBox.setPrefWidth(80);
        taxBox.setEditable(false);

        //gridPane creation
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.TOP_RIGHT);
        gridPane.getChildren().addAll(total, tax, totalBox, taxBox, checkOut);
        gridPane.setConstraints(tax, 0, 0);
        gridPane.setConstraints(taxBox, 1, 0);
        gridPane.setConstraints(total, 0, 1);
        gridPane.setConstraints(totalBox, 1, 1);
        gridPane.setConstraints(checkOut,0, 9);

        // creating productsPage gridpane for item
        //creating item combo box
        itemLabel = new Label("Item");
        itemSelect = new ComboBox(FXCollections.observableArrayList(
                "OtterBox defender", "OtterBox commuter", "OtterBox symmetry", "Belkin Grip Vue"
                , "Speck Candyshell", "Speck Wallet", "Bodyglove Carbon","Charger Apple", "Charger Other",
                "Headphones jvc","Headphones Apple","Headphones Sony"));
        itemSelect.getSelectionModel().selectFirst();
        // creating color combo box
        colorLabel = new Label("Color");
        colorSelect = new ComboBox(FXCollections.observableArrayList(
                "Clear", "Black", "Red", "Green", "Yellow","Blue","Orange", "White", "Graphics","Pink"));
        colorSelect.getSelectionModel().selectFirst();
        //creates "add to cart" button
        addButton = new Button("ADD TO CART");
        addButton.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");

        //sets action for "add to cart" button
        addButton.setOnAction(event -> {
            addToCart();
        });

        //sets up product page
        setupProductPage();

        // creating left layout
        //column for description
        TableColumn descriptionCol = new TableColumn("Item Description");
        descriptionCol.setPrefWidth(400);
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Cart, String>("itemDescription"));

        //column for price
        TableColumn priceCol = new TableColumn("Price");
        priceCol.setPrefWidth(200);
        priceCol.setCellValueFactory(new PropertyValueFactory<Cart, String>("itemPrice"));

        //adds columns to layout
        table.getColumns().addAll(descriptionCol, priceCol);
        table.setOpacity(.7);

        //creates vertical select box
        vbox2 = new VBox();
        vbox2.setAlignment(Pos.TOP_RIGHT);
        vbox2.getChildren().addAll(removeFromCart,table, gridPane);
        vbox2.setPadding(new Insets(30, 10, 10, 10));
        vbox2.setSpacing(15);
        vbox2.setPrefWidth(600);
        vbox2.setStyle("-fx-border-color: black;");

        //creates brand Tree
        createTree();

        //sets logo
        Image image = new Image(StartScreen.logo, 700, 500, true, true);
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setCache(true);

        //Center Layout
        vbox = new VBox();
        vbox.setStyle("-fx-font: 24 arial;");
        vbox.getChildren().addAll(tree);
        vbox.setPadding(new Insets(0, 10, 10, 10));
        vbox.setPrefWidth(100);
        vbox.setSpacing(20);

        //Top layout
        setUpColorButtons();

        hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setPrefHeight(50);
        hbox.setSpacing(30);
        hbox.setStyle("-fx-background-color: white;");
        hbox.getChildren().addAll(rb1,rb2, rb3, rb4, logOut);

        //Bottom Layout
        signedIn = new Label("USER LOGGED-IN: " + StartScreen.userNameField.getText());

        hbox2.setAlignment(Pos.CENTER);
        hbox2.setPadding(new Insets(10, 10, 10, 10));
        hbox2.setPrefHeight(50);
        hbox2.setSpacing(20);
        hbox2.setStyle("-fx-background-color: white;");
        hbox2.getChildren().addAll(signedIn,adminButton);
        adminButton.setVisible(false);



        //Creating base layout
        String rightImage = MainScreen.class.getResource("\\light.jpg").toExternalForm();
        border.setStyle("-fx-background-image: url('" + rightImage + "');");
        border.setCenter(vbox);
        border.setLeft(vbox2);
        border.setRight(brandsScrollPane);
        border.setTop(hbox);
        border.setBottom(hbox2);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), border);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();


    }

    //Create branches
    public static TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);


        parent.getChildren().add(item);

        return item;
    }



    //Create Right component
    public static void CreateRightPane(FlowPane flowP) {
        flowP.setPrefWidth(500);
        flowP.setVgap(15);
        flowP.setPadding(new Insets(20, 10, 10, 10));
        flowP.setStyle("-fx-background-color:white ;-fx-border-color: black;");
    }

    //Method that sets up the model picture Buttons
    public static Button setupModelButton(Button button, String pic) {

        Image logo = new Image(pic);
        ImageView iv = new ImageView(logo);

        iv.setFitHeight(300);
        iv.setFitWidth(230);

        button.setOnAction(event -> {
            Brands.setExpanded(true);
            border.setRight(productsPage);
            phoneName = button.getText();
        });
        button.setPrefWidth(400);
        button.setContentDisplay(ContentDisplay.TOP);
        button.setPrefHeight(230);
        button.setGraphic(iv);
        button.setStyle("-fx-font: 16 arial;-fx-base: clear;");

        return button;
    }

    /*
        Method that sets up the brand logo buttons
     */
    public static Button setupLogoButton(Button button, String pic) {

        Image logo = new Image(pic);
        ImageView iv = new ImageView(logo);

        iv.setFitHeight(200);
        iv.setFitWidth(130);

        button.setOnAction(event -> Brands.setExpanded(true));
        button.setPrefWidth(220);
        button.setPrefHeight(130);
        button.setGraphic(iv);
        button.setStyle("-fx-base:clear ;");

        return button;
    }

    public static void setBrandButtons(){
        button1 = new Button();
        setupLogoButton(button1, logoStringArray.get(0));
        button1.setOnAction(event -> border.setRight(appleScrollPane));

        button2 = new Button();
        setupLogoButton(button2, logoStringArray.get(1));
        button2.setOnAction(event -> border.setRight(huaweiScrollPane));

        button3 = new Button();
        setupLogoButton(button3, logoStringArray.get(2));
        button3.setOnAction(event -> border.setRight(motorolaScrollPane));

        button4 = new Button();
        setupLogoButton(button4, logoStringArray.get(3));
        button4.setOnAction(event -> border.setRight(lgScrollPane));

        button5 = new Button();
        setupLogoButton(button5, logoStringArray.get(4));
        button5.setOnAction(event -> border.setRight(nokiaScrollPane));

        button6 = new Button();
        setupLogoButton(button6, logoStringArray.get(5));
        button6.setOnAction(event -> border.setRight(samsungScrollPane));

        button7 = new Button();
        setupLogoButton(button7, logoStringArray.get(6));
        button7.setOnAction(event -> border.setRight(zteScrollPane));
    }

    public static void addToCart(){

        df.setRoundingMode(RoundingMode.FLOOR);
        try {
            String itemName = itemSelect.getValue().toString().toLowerCase();
            String color = colorSelect.getValue().toString().toLowerCase();
            double price = 0;
            BigDecimal p = db.getPrice(itemName);
            price = price + p.doubleValue();
            String sPrice = String.valueOf(price);
            Cart cart = new Cart(phoneName + " " + colorSelect.getValue().toString() + " " + itemName, sPrice, itemName, phoneName, color);
            data.add(cart);
            cartList.add(cart);
            totalPrice = totalPrice + price;
            taxAmount = totalPrice * .06;
            taxBox.setText(df.format(taxAmount));
            totalBox.setText(df.format(totalPrice + taxAmount));
        } catch (SQLException e) {e.printStackTrace();}

        table.setItems(data);

    }

    public static void removeItemFromCart(){

        try{
            Cart removed_item = (Cart)table.getSelectionModel().getSelectedItem();
            cartList.remove(removed_item);
            table.getItems().remove(removed_item);
            table.refresh();
            String currentTax = taxBox.getText();
            String price = removed_item.getItemPrice();
            String newTaxVal = String.valueOf(round((Double.valueOf(currentTax) +.01 - (Double.valueOf(price) * .06 )),2));
            taxBox.setText(newTaxVal);
            taxAmount = Double.valueOf(newTaxVal);

            String currentTotal = totalBox.getText();
            String newTotal = String.valueOf(round((Double.valueOf(currentTotal) + 0.01 - (Double.valueOf(price) + (Double.valueOf(price) * .06))),2));
            totalBox.setText(newTotal);
            totalPrice = Double.valueOf(newTotal);

        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must first select an item from the table");
            alert.showAndWait();
        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void removeFromDataBase(){
        for(Cart cartItem : cartList){
            String phoneName = cartItem.getPhoneName().toLowerCase();
            String brandName = cartItem.getItemName().toLowerCase();
            String color = cartItem.getItemColor().toLowerCase();
            int quantity = -1;

            try{
                db.updateQuantity(phoneName,brandName,color,quantity);

            }catch(SQLException sqlE){
                System.out.println("Unable to Checkout");
                sqlE.printStackTrace();
            }
        }
        border.setRight(brandsScrollPane);
        totalBox.setText("");
        totalPrice = 0;
        taxBox.setText("");
        taxAmount = 0;

        data.clear();
        data.add(new Cart("","","","",""));
        table.setItems(data);
    };

    public static void createApple(){
        //Creating apple (model layout)
        appleModel = new FlowPane();
        CreateRightPane(appleModel);
        Button aModel0 = new Button("iPhone7 Plus");
        setupModelButton(aModel0, modelStringArray.get(0));
        Button aModel1 = new Button("iPhone7");
        setupModelButton(aModel1, modelStringArray.get(1));
        Button aModel2 = new Button("iPhone6 Plus");
        setupModelButton(aModel2, modelStringArray.get(2));
        Button aModel3 = new Button("iPhone6");
        setupModelButton(aModel3, modelStringArray.get(3));
        Button aModel4 = new Button("iPhone SE");
        setupModelButton(aModel4, modelStringArray.get(4));
        appleModel.getChildren().addAll(aModel0, aModel1, aModel2, aModel3, aModel4);
        appleScrollPane.setContent(appleModel);
        appleScrollPane.setStyle("-fx-background-color: #bcf8ff;");
        appleScrollPane.setPrefWidth(500);
    }

    public static void createMotorola(){
        //Creating motorola (model layout)
        motorolaModels = new FlowPane();
        CreateRightPane(motorolaModels);
        Button mModel5 = new Button("Moto Z Force");
        setupModelButton(mModel5, modelStringArray.get(5));
        Button mModel6 = new Button("Moto Z");
        setupModelButton(mModel6, modelStringArray.get(6));
        Button mModel7 = new Button("Moto Z Droid");
        setupModelButton(mModel7, modelStringArray.get(7));
        Button mModel8 = new Button("Moto Z Play");
        setupModelButton(mModel8, modelStringArray.get(8));
        Button mModel9 = new Button("Moto Z Play Droid");
        setupModelButton(mModel9, modelStringArray.get(9));
        Button mModel10 = new Button("Moto G4  Plus");
        setupModelButton(mModel10, modelStringArray.get(10));
        Button mModel11 = new Button("Moto G4");
        setupModelButton(mModel11, modelStringArray.get(11));
        Button mModel12 = new Button("Moto G4 Play");
        setupModelButton(mModel12, modelStringArray.get(12));
        Button mModel13 = new Button("Moto X  Pure");
        setupModelButton(mModel13, modelStringArray.get(13));
        Button mModel14 = new Button("Moto G3");
        setupModelButton(mModel14, modelStringArray.get(14));
        Button mModel15 = new Button("Droid Turbo 2");
        setupModelButton(mModel15, modelStringArray.get(15));
        motorolaModels.getChildren().addAll(mModel5, mModel6, mModel7, mModel8, mModel9,
                mModel10, mModel11, mModel12, mModel13, mModel14, mModel15);
        motorolaScrollPane.setPrefSize(500, 900);
        motorolaScrollPane.setStyle("-fx-background-color: #bcf8ff;");
        motorolaScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        motorolaScrollPane.setContent(motorolaModels);
    }

    public static void createHuawei(){
        //Creating Huawei (model layout)
        huaweiModels = new FlowPane();
        CreateRightPane(huaweiModels);
        Button hmodel16 = new Button("Huawei Mate 9");
        setupModelButton(hmodel16, modelStringArray.get(16));
        Button hmodel17 = new Button("Huawei Nexus 6P");
        setupModelButton(hmodel17, modelStringArray.get(17));
        Button hmodel18 = new Button("Huawei GX8");
        setupModelButton(hmodel18, modelStringArray.get(18));
        Button hmodel19 = new Button("Huawei P8Lite");
        setupModelButton(hmodel19, modelStringArray.get(19));
        huaweiModels.getChildren().addAll(hmodel16, hmodel17, hmodel18, hmodel19);
        huaweiScrollPane.setContent(huaweiModels);
    }

    public static void createLG(){
        //Creating LG (model layout)
        lgModels = new FlowPane();
        CreateRightPane(lgModels);
        Button lmodel20 = new Button("LG Tribute HD");
        setupModelButton(lmodel20, modelStringArray.get(20));
        Button lmodel21 = new Button("LG Pheonix 2");
        setupModelButton(lmodel21, modelStringArray.get(21));
        Button lmodel22 = new Button("LG Stylo 2");
        setupModelButton(lmodel22, modelStringArray.get(22));
        Button lmodel23 = new Button("LG Treasure  LTE");
        setupModelButton(lmodel23, modelStringArray.get(23));
        Button lmodel24 = new Button("LG G5");
        setupModelButton(lmodel24, modelStringArray.get(24));
        Button lmodel25 = new Button("LG X POWER");
        setupModelButton(lmodel25, modelStringArray.get(25));
        Button lmodel26 = new Button("LG Escape 2");
        setupModelButton(lmodel26, modelStringArray.get(26));
        Button lmodel27 = new Button("LG V10");
        setupModelButton(lmodel27, modelStringArray.get(27));
        Button lmodel28 = new Button("LG K7 3G");
        setupModelButton(lmodel28, modelStringArray.get(28));
        Button lmodel29 = new Button("LG G Flex 2");
        setupModelButton(lmodel29, modelStringArray.get(29));
        lgModels.getChildren().addAll(lmodel20, lmodel21, lmodel22, lmodel23,
                lmodel24, lmodel25, lmodel26, lmodel27, lmodel28, lmodel29);
        lgScrollPane.setContent(lgModels);
        lgScrollPane.setPrefSize(500, 900);
        lgScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public static void createNokia(){
        //Creating Nokia (model layout)
        nokiaModels = new FlowPane();
        CreateRightPane(nokiaModels);
        Button nModel30 = new Button("Nokia 6");
        setupModelButton(nModel30, modelStringArray.get(30));
        Button nModel31 = new Button("Nokia 5");
        setupModelButton(nModel31, modelStringArray.get(31));
        Button nModel32 = new Button("Nokia 3");
        setupModelButton(nModel32, modelStringArray.get(32));
        Button nModel33 = new Button("Nokia 3310");
        setupModelButton(nModel33, modelStringArray.get(33));
        Button nModel34 = new Button("Nokia 150");
        setupModelButton(nModel34, modelStringArray.get(34));
        Button nModel35 = new Button("Nokia 105");
        setupModelButton(nModel35, modelStringArray.get(35));
        Button nModel36 = new Button("Nokia 230");
        setupModelButton(nModel36, modelStringArray.get(36));
        Button nModel37 = new Button("Nokia 222");
        setupModelButton(nModel37, modelStringArray.get(37));
        Button nModel38 = new Button("Nokia 216");
        setupModelButton(nModel38, modelStringArray.get(38));
        Button nModel39 = new Button("Nokia 130");
        setupModelButton(nModel39, modelStringArray.get(39));
        nokiaModels.getChildren().addAll(nModel30, nModel31, nModel32, nModel33, nModel34,
                nModel35, nModel36, nModel37, nModel38, nModel39);
        nokiaScrollPane.setContent(nokiaModels);
        nokiaScrollPane.setPrefSize(500, 900);
        nokiaScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public static void createSamsung(){
        //Creating Samsung (model layout)
        samsungModels = new FlowPane();
        CreateRightPane(samsungModels);
        Button sModel40 = new Button("Galaxy S7 Edge");
        setupModelButton(sModel40, modelStringArray.get(40));
        Button sModel41 = new Button("Galaxy S6 Edge");
        setupModelButton(sModel41, modelStringArray.get(41));
        Button sModel42 = new Button("Galaxy NOTE 5");
        setupModelButton(sModel42, modelStringArray.get(42));
        Button sModel43 = new Button("Galaxy NOTE Edge");
        setupModelButton(sModel43, modelStringArray.get(43));
        Button sModel44 = new Button("Galaxy NOTE 4");
        setupModelButton(sModel44, modelStringArray.get(44));
        Button sModel45 = new Button("Galaxy NOTE 3");
        setupModelButton(sModel45, modelStringArray.get(45));
        Button sModel46 = new Button("Galaxy S4 MINI");
        setupModelButton(sModel46, modelStringArray.get(46));
        Button sModel47 = new Button("Galaxy J7 ");
        setupModelButton(sModel47, modelStringArray.get(47));
        Button sModel48 = new Button("Galaxy J3 ");
        setupModelButton(sModel48, modelStringArray.get(48));
        Button sModel49 = new Button("Galaxy Mega ");
        setupModelButton(sModel49, modelStringArray.get(49));
        samsungModels.getChildren().addAll(sModel40, sModel41, sModel42, sModel43,
                sModel44, sModel45, sModel46, sModel47, sModel48, sModel49);
        samsungScrollPane.setContent(samsungModels);
        samsungScrollPane.setPrefSize(500, 900);
        samsungScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public static void createZTE(){
        //ZTE phone model layout
        zteModels = new FlowPane();
        CreateRightPane(zteModels);
        Button zModel50 = new Button("ZTE AXON 7");
        setupModelButton(zModel50, modelStringArray.get(50));
        Button zModel51 = new Button("ZTE AVID 828");
        setupModelButton(zModel51, modelStringArray.get(51));
        Button zModel52 = new Button("ZTE BLADE V8 PRO");
        setupModelButton(zModel52, modelStringArray.get(52));
        Button zModel53 = new Button("ZTE AXON 7 MINI");
        setupModelButton(zModel53, modelStringArray.get(53));
        Button zModel54 = new Button("ZTE ZMAX PRO");
        setupModelButton(zModel54, modelStringArray.get(54));
        Button zModel55 = new Button("ZTE ZMAX 2");
        setupModelButton(zModel55, modelStringArray.get(55));
        Button zModel56 = new Button("ZTE GRAND X MAX 2");
        setupModelButton(zModel56, modelStringArray.get(56));
        Button zModel57 = new Button("ZTE MAX DUO LTE");
        setupModelButton(zModel57, modelStringArray.get(57));
        Button zModel58 = new Button("ZTE IMPERIAL MAX");
        setupModelButton(zModel58, modelStringArray.get(58));
        Button zModel59 = new Button("ZTE GRAND X3");
        setupModelButton(zModel59, modelStringArray.get(59));
        Button zModel60 = new Button("ZTE GRAND MEMO 2");
        setupModelButton(zModel60, modelStringArray.get(60));
        Button zModel61 = new Button("ZTE FANFARE");
        setupModelButton(zModel61, modelStringArray.get(61));
        Button zModel62 = new Button("ZTE OVERTURE 2");
        setupModelButton(zModel62, modelStringArray.get(62));
        Button zModel63 = new Button("ZTE WARP ELITE");
        setupModelButton(zModel63, modelStringArray.get(63));
        zteModels.getChildren().addAll(zModel50, zModel51, zModel52, zModel53
                , zModel54, zModel55, zModel56, zModel57, zModel58, zModel59, zModel60
                , zModel61, zModel62, zModel63);
        zteScrollPane.setContent(zteModels);
        zteScrollPane.setPrefSize(500, 900);
    }

    public static void setupProductPage(){
        productsPage = new GridPane();
        productsPage.setStyle("-fx-background-color: white;-fx-border-color: black;");
        productsPage.setPadding(new Insets(300, 100, 300, 150));
        productsPage.setVgap(10);
        productsPage.setPrefSize(500, 900);
        productsPage.setPadding(new Insets(200, 100, 200, 150));
        productsPage.getChildren().addAll(itemLabel, itemSelect, colorLabel, colorSelect, addButton);
        productsPage.setConstraints(itemLabel, 0, 2);
        productsPage.setConstraints(itemSelect, 0, 4);
        productsPage.setConstraints(colorLabel, 0, 10);
        productsPage.setConstraints(colorSelect, 0, 12);
        productsPage.setConstraints(addButton, 0, 20);
    }

    public static void createTree(){

        // tree settings
        tree = new TreeView<>(Products);
        tree.setOpacity(.9);
        tree.setStyle("-fx-background-color: black;");

        //ADDING LISTENERS TO THE TREE
        tree.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if (newValue.getValue() == "Apple")
                        border.setRight(appleScrollPane);
                    else if (newValue.getValue() == "Brands")
                        border.setRight(brandsScrollPane);
                    else if (newValue.getValue() == "Motorola")
                        border.setRight(motorolaScrollPane);
                    else if (newValue.getValue() == "LG")
                        border.setRight(lgScrollPane);
                    else if (newValue.getValue() == "Nokia")
                        border.setRight(nokiaScrollPane);
                    else if (newValue.getValue() == "Samsung")
                        border.setRight(samsungScrollPane);
                    else if (newValue.getValue() == "Huawei")
                        border.setRight(huaweiScrollPane);
                    else if (newValue.getValue() == "ZTE")
                        border.setRight(zteScrollPane);
                });
    }

    public static void setEmployeeTable(){
        String hours = null;
        try{
            //connect to database, initialize statements
            Connection connection = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir"), "sa", "");
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();

            ResultSet results1 = statement.executeQuery("select hoursWorked from user");

            //selects clockin and username from user table
            ResultSet results2 = statement1.executeQuery("select username from user");
            String name;

            while(results2.next() && results1.next()){
                int time = results1.getInt(1);
                hours = time + "";
                name = results2.getString(1);
                AdminOnly.data.add(new Employees(name, hours));
            }

        }catch(SQLException s){s.printStackTrace();
        }

        AdminOnly.employeeTable.setItems(AdminOnly.data);
    }

    public static void setUpColorButtons(){
        rb1 = new RadioButton("Green");
        rb1.setOnAction(event -> {
            updatePanes();

            String image = MainScreen.class.getResource("\\river.jpg").toExternalForm();

            border.setStyle("-fx-background-image: url('" + image + "');");
            logOut.setStyle("-fx-font: 22 arial; -fx-base: #03bc1b;");
            removeFromCart.setStyle("-fx-font: 12 arial; -fx-base: #03bc1b;");
            vbox2.setOpacity(.85);
            vbox.setOpacity(.85);
            addButton.setStyle("-fx-font: 22 tacoma; -fx-base: #03bc1b;");

            checkOut.setStyle("-fx-font: 22 arial; -fx-base: #03bc1b;");
            hbox2.setStyle("-fx-background-color: #4c8454;");
            hbox.setStyle("-fx-background-color: #4c8454;");
            adminButton.setStyle("-fx-font: 22 arial; -fx-base: #03bc1b;");
            rb3.setSelected(false);
            rb2.setSelected(false);
            rb4.setSelected(false);
        });

        rb2 = new RadioButton("Blue");
        rb2.setOnAction(event -> {
            updatePanes();

            String image = MainScreen.class.getResource("\\island.jpg").toExternalForm();

            border.setStyle("-fx-background-image: url('" + image + "');");
            logOut.setStyle("-fx-font: 22 arial; -fx-base: #338cc6;");
            addButton.setStyle("-fx-font: 22 tacoma; -fx-base: #338cc6;");
            removeFromCart.setStyle("-fx-font: 12 arial; -fx-base: #338cc6;");

            checkOut.setStyle("-fx-font: 22 arial; -fx-base: #338cc6;");
            hbox2.setStyle("-fx-background-color: #417699;");
            hbox.setStyle("-fx-background-color: #417699;");
            adminButton.setStyle("-fx-font: 22 arial; -fx-base: #338cc6;");
            rb1.setSelected(false);
            rb3.setSelected(false);
            rb4.setSelected(false);
        });

        rb3 = new RadioButton("Red");
        rb3.setOnAction(event -> {
            updatePanes();
            String image = MainScreen.class.getResource("\\mountains.jpg").toExternalForm();

            border.setStyle("-fx-background-image: url('" + image + "');");
            logOut.setStyle("-fx-font: 22 arial; -fx-base: #c4361d;");
            vbox2.setOpacity(.85);
            vbox.setOpacity(.85);
            addButton.setStyle("-fx-font: 22 tacoma; -fx-base: #c4361d;");
            removeFromCart.setStyle("-fx-font: 12 arial; -fx-base: #c4361d;");


            checkOut.setStyle("-fx-font: 22 arial; -fx-base: #c4361d;");
            hbox2.setStyle("-fx-background-color: #8e3636;");
            hbox.setStyle("-fx-background-color: #8e3636;");
            adminButton.setStyle("-fx-font: 22 arial; -fx-base: #c4361d;");
            rb1.setSelected(false);
            rb2.setSelected(false);
            rb4.setSelected(false);
        });

        rb4 = new RadioButton("White");
        rb4.setOnAction(event -> {
            updatePanes();
            String image = MainScreen.class.getResource("\\light.jpg").toExternalForm();
            border.setStyle("-fx-background-image: url('" + image + "');");
            logOut.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
            vbox2.setOpacity(.85);
            vbox.setOpacity(.85);
            addButton.setStyle("-fx-font: 22 tacoma; -fx-base: #FF8C00;");
            removeFromCart.setStyle("-fx-font: 12 arial; -fx-base: #FF8C00;");
            checkOut.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
            hbox2.setStyle("-fx-background-color: white;");
            hbox.setStyle("-fx-background-color: white;");
            adminButton.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");
            tax.setStyle("-fx-text-fill: black;-fx-font: 22 arial;");
            total.setStyle("-fx-text-fill: black;-fx-font: 22 arial;");
            rb1.setSelected(false);
            rb2.setSelected(false);
            rb3.setSelected(false);
        });


    }

    /*
    Method used to change opacity of the different layouts
     */
    public static void updatePanes(){
        tax.setStyle("-fx-text-fill: white;-fx-font: 22 arial;");
        total.setStyle("-fx-text-fill: white;-fx-font: 22 arial;");
        vbox2.setOpacity(.95);
        vbox.setOpacity(.85);
        brandsScrollPane.setOpacity(.7);
        appleScrollPane.setOpacity(.7);
        huaweiScrollPane.setOpacity(.7);
        lgScrollPane.setOpacity(.7);
        motorolaScrollPane.setOpacity(.7);
        nokiaScrollPane.setOpacity(.7);
        zteScrollPane.setOpacity(.7);
        samsungScrollPane.setOpacity(.7);
        productsPage.setOpacity(.7);
    }

    public static void logOut() {
        try {
            String user = null;
            int hours = 0;
            Time clockIn = null;
            Calendar calendar = Calendar.getInstance();

            //connects to database
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir"), "sa", "");
            Class.forName("org.h2.Driver");

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select username, clockIn, hoursWorked from user " +
                    "where clockIn != '00:00:00'");

            while(rs.next()){
                user = rs.getString("username");
                clockIn = rs.getTime("clockIn");
                hours = rs.getInt("hoursWorked");
            }

            java.util.Date now = calendar.getTime();
            java.sql.Time currentTime = new java.sql.Time(now.getTime());

            double timeWorked = subtractTimes(currentTime, clockIn);

            statement.execute("update user set hoursWorked = "
                    + (timeWorked + hours) +
                    ", clockIn = '00:00:00' " +
                    " where username = '" + user + "';");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double subtractTimes(Time t, Time p){

        int inTime = t.getHours();
        int outTime = p.getHours();
        int inMinutes = t.getMinutes();
        int outMinutes = t.getMinutes();

        double minutesWorked;
        int hoursWorked;

        hoursWorked = outTime - inTime;
        minutesWorked = (double)(outMinutes - inMinutes);

        if(minutesWorked < 0){

            hoursWorked--;
            minutesWorked+=60;
        }
        return ((double)hoursWorked + (minutesWorked/60));
    }


    //main function, launches start function
    public static void main(String[] args) {
        launch(args);
    }
}
