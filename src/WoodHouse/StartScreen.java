package WoodHouse;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Calendar;

public class StartScreen extends Application{

    //Initialize objects and variables
    public static Stage mainStage;
    protected static Scene initialScene;
    protected static String logo = StartScreen.class.getResource("\\woodhouse.png").toExternalForm();
    protected static boolean isAdmin = false;
    protected static TextField userNameField;
    protected static PasswordField userPassword;
    protected static DBController dbController = null;
    protected static Stage startStage;

    public void start(Stage primaryStage) throws Exception{

        //Creating a connection to our database so that it can be used by our DB classes.
        startStage = primaryStage;
        initialize();
        try {
            new MainScreen(primaryStage, dbController);

        } catch (Exception e) {
        }
    }

    public static void initialize()throws Exception{
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir"), "sa", "");

        //Instantiating the database before anything else happens.
        DBLauncher db = new DBLauncher(conn);
        dbController = new DBController(conn);

        //set stage
        mainStage = startStage;

        //create text fields, and login button
        userNameField = new TextField();
        userNameField.setPromptText("Enter Name");
        userPassword = new PasswordField();
        userPassword.setPromptText("Enter Password");
        userPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if( event.getCode().equals(KeyCode.K.ENTER)){
                    if(login(conn)) {
                        MainScreen.data.add(new Cart("","","","",""));
                        MainScreen.table.setItems(MainScreen.data);
                        if(isAdmin){
                            MainScreen.adminButton.setVisible(true);
                        }
                        MainScreen.signedIn.setText("USER LOGGED-IN: " + userNameField.getText());
                        mainStage.setScene(MainScreen.mScene);
                        MainScreen.rb4.setSelected(true);
                    }
                }
            }
        });

        Button login = new Button("Log-in");
        login.setStyle("-fx-font: 22 arial; -fx-base: #FF8C00;");

        //set action for login button
        login.setOnAction(event -> {
            if(login(conn)) {
                MainScreen.data.add(new Cart("","","","",""));
                MainScreen.table.setItems(MainScreen.data);
                if(isAdmin){
                    MainScreen.adminButton.setVisible(true);
                }
                MainScreen.signedIn.setText("USER LOGGED-IN: " + userNameField.getText());
                mainStage.setScene(MainScreen.mScene);
                MainScreen.rb4.setSelected(true);
            }
        });


        //sets labels above textfields
        Label usernameLabel = new Label("Username: ");
        Label adminPassword = new Label("Password: ");

        //logo button
        Button woodhouseButton = new Button();
        String word = StartScreen.class.getResource("\\woodhouse.png").toExternalForm();
        Image logo = new Image(word, 1000, 600, true, true);

        //Button Image
        ImageView iv = new ImageView();
        iv.setImage(logo);
        iv.setCache(true);
        iv.setFitHeight(600);
        iv.setFitWidth(300);

        //Image button
        woodhouseButton.setOnAction(event -> {
            if(login(conn)) {
                MainScreen.data.add(new Cart("","","","",""));
                MainScreen.table.setItems(MainScreen.data);
                if(isAdmin){
                    MainScreen.adminButton.setVisible(true);
                    // MainScreen.hbox2.getChildren().add(MainScreen.adminButton);
                }
                MainScreen.signedIn.setText("USER LOGGED-IN: " + userNameField.getText());
                mainStage.setScene(MainScreen.mScene);
                MainScreen.rb4.setSelected(true);
            }
            });
        woodhouseButton.setPrefWidth(250);
        woodhouseButton.setPrefHeight(230);
        woodhouseButton.setGraphic(iv);
        woodhouseButton.setStyle("-fx-base: clear;");


        //title pane
        TilePane tPane = new TilePane();
        tPane.getChildren().addAll(woodhouseButton);
        tPane.setPadding(new Insets(10,10,10,30));
        tPane.setAlignment(Pos.CENTER);
        tPane.setStyle("-fx-background-color: white;");

        //sets gridpane for Left Side
        GridPane buffer1 = new GridPane();
        buffer1.setStyle("-fx-background-color: white;");
        buffer1.setPrefSize(400,200);
        buffer1.setHgap(15);
        buffer1.setVgap(5);
        buffer1.setPadding(new Insets(10, 10, 10, 100));
        buffer1.getChildren().addAll(usernameLabel, userNameField);
        buffer1.setConstraints(userNameField, 0, 8);
        buffer1.setConstraints(usernameLabel, 0, 6);

        //sets pane for the Top
        Pane buffer2 = new Pane();
        buffer2.setStyle("-fx-background-color: white;");
        buffer2.setPrefSize(100,200);

        //Sets pane for Right Side
        GridPane buffer3 = new GridPane();
        buffer3.setStyle("-fx-background-color: white;");
        buffer3.setPrefSize(400,200);
        buffer3.setHgap(15);
        buffer3.setVgap(5);
        buffer3.setPadding(new Insets(10, 100, 10, 10));
        buffer3.getChildren().addAll(adminPassword, userPassword);
        buffer3.setConstraints(userPassword, 0, 8);
        buffer3.setConstraints(adminPassword, 0, 6);

        //bottom info label
        Label bottomLabel = new Label("Click Image to Enter");

        //bottom pane
        TilePane bPane = new TilePane();
        bPane.getChildren().addAll(bottomLabel);
        bPane.setPadding(new Insets(10,10,10,30));
        bPane.setAlignment(Pos.TOP_CENTER);
        bPane.setStyle("-fx-background-color: white;");

        //sets border pane
        BorderPane border = new BorderPane();
        border.setLeft(buffer1);
        border.setCenter(tPane);
        border.setTop(buffer2);
        border.setRight(buffer3);
        border.setBottom((bPane));

        //sets scene
        initialScene = new Scene(border,1720,900);
        mainStage.setScene(initialScene);
        mainStage.setTitle("WoodHouse");
        mainStage.show();
    }

    //main function, launches start function
    public static void main(String[] args) {

        launch(args);
    }

    //method for action on login button
    public static boolean login(Connection conn){

        Calendar calendar = Calendar.getInstance();

        try{
            //connects to database
            String pass = null;
            Class.forName("org.h2.Driver");
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select password from user " +
                    "where username = '" + userNameField.getText() + "';" );

            while(rs.next()) {//if Username is found, sets their password to pass
                pass = rs.getString("password");
            }

            if(userPassword.getText().equals(pass)) {//checks database password vs typed password
                rs = statement.executeQuery("select adminFlag from user " +
                        "where username = '" + userNameField.getText() + "';" );

                while(rs.next()){//checks if user is admin and sets isAdmin bool
                    isAdmin = rs.getBoolean("adminFlag");
                }

                //create time object
                java.util.Date now = calendar.getTime();
                java.sql.Time time = new java.sql.Time(now.getTime());
                //set clockIn time in database
                statement.execute("UPDATE user SET clockIn = '" + time
                        + "' WHERE username = '" + userNameField.getText() +"';");

                return true;
            }
            else{//Pop up alert for wrong credentials

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Username/Password is incorrect");
                alert.showAndWait();

                return false;
            }

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


}
