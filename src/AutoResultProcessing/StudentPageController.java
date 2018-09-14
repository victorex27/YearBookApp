/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoResultProcessing;

//import AutoResultProcessing.StudentPageController.Course;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author PETER
 */
public class StudentPageController implements Initializable, ControlledScreen {

    public static String matric = "";
    ScreenController myController;

    @Override
    public void setScreenParent(ScreenController screenParent) {
        myController = screenParent;
    }
    /**
     * Initializes the controller class.
     */

    @FXML
    private Button btnClose;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Button viewResultBtn;

    @FXML
    private Button returnBtn;
    @FXML
    private Pane container;
    @FXML
    private Button submitYearBookButton;
    @FXML
    private TextArea lessonQuote;
    @FXML
    private TextArea wisdomQuote;
    @FXML
    private ImageView studentImage;
    @FXML
    private VBox studentQuote;
    @FXML
    private ListView studentListView;
    @FXML
    private ScrollPane viewYearBookPane;
    @FXML
    private GridPane gridPane;
    
    // Variables used when a student profile is clicked in the year bool
    @FXML
    private Label studentFullname;
    @FXML
    private Label studentClass;
    @FXML
    private Label studentPosition;
    @FXML
    private Label studentPaneWisdomQuote;
    @FXML
    private Label studentPaneLessonQuote;
    @FXML
    private Pane studentPane;// this pane will show information for a particular student after the student picture is clicked in the yearbook app
    @FXML
    private ImageView studentPaneImageView;
    
    @FXML
    private Label updateTextLabel;

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void hover_in(MouseEvent event) {
        Button actionBtn = ((Button) event.getSource());
        ImageView closeImage = (ImageView) actionBtn.getGraphic();
        actionBtn.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 10, 0, 0, 0));
        closeImage.setEffect(new ColorAdjust(0, 0, 0.70, 0));
    }

    @FXML
    private void hover_out(MouseEvent event) {
        Button actionBtn = ((Button) event.getSource());
        ImageView closeImage = (ImageView) actionBtn.getGraphic();
        actionBtn.setEffect(null);
        closeImage.setEffect(new ColorAdjust(0, 0, 0, 0));
    }

    @FXML
    private void requestfocus(MouseEvent event) {
        container.requestFocus();
    }

    double yOffset = 0;
    double xOffset = 0;

    @FXML
    private void determine(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
    private void pick(MouseEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        ((Node) event.getSource()).setCursor(Cursor.CLOSED_HAND);
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    private void drop(MouseEvent event) {
        ((Node) event.getSource()).setCursor(Cursor.OPEN_HAND);
    }

    @FXML
    private Label institutionLbl;

    @FXML
    private Label departmentLbl;

    @FXML
    private Label matricLbl;

    @FXML
    private Label fullNameLbl;

    @FXML
    private ImageView userImageIV;

    @FXML
    private ImageView institutionImageIV;

    @FXML
    private ScrollPane resultPane;

    private boolean updateState = false;

    private String currentSession = "2017/2018";

    @FXML
    private void logout(ActionEvent event) {
        myController.setScreen(AutoResultProcessing.login);
        Pane loginPageRoot = (Pane) ScreenController.screens.get(AutoResultProcessing.login);
        ((Label) loginPageRoot.lookup("#label2")).setText("");
//        registrationTb.setItems(null);
        matric = "";
        try {
            String queryString = "Update Student SET Status = 'Offline' WHERE matric = ?";
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, matric);
            statement.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        institutionLbl.setPrefWidth(292);

        initializeDB();

        registerBtnSt = new ScaleTransition(Duration.millis(500));
        registerBtnSt.setFromX(1);
        registerBtnSt.setFromY(1);
        registerBtnSt.setToX(0.8);
        registerBtnSt.setToY(0.8);
        registerBtnSt.interpolatorProperty().set(Interpolator.EASE_IN);

        registerBtnFt = new FadeTransition(Duration.millis(400), registerBtn);
        registerBtnFt.setFromValue(1);
        registerBtnFt.setToValue(0);

        viewResultBtnFt = new FadeTransition(Duration.millis(300));
        viewResultBtnFt.setFromValue(1);
        viewResultBtnFt.setToValue(0);

        viewResultBtnSt = new ScaleTransition(Duration.millis(400));
        viewResultBtnSt.setFromX(1);
        viewResultBtnSt.setFromY(1);
        viewResultBtnSt.setToX(0.5);
        viewResultBtnSt.setToY(0.5);

        viewResultBtnPt = new ParallelTransition(viewResultBtn, viewResultBtnSt, viewResultBtnFt);
        viewResultBtnPt.interpolatorProperty().set(Interpolator.EASE_IN);

        logoutBtnFt = new FadeTransition(Duration.millis(400));
        logoutBtnFt.setFromValue(1);
        logoutBtnFt.setToValue(0);

        logoutBtnSt = new ScaleTransition(Duration.millis(300));
        logoutBtnSt.setFromX(1);
        logoutBtnSt.setFromY(1);
        logoutBtnSt.setToX(0.5);
        logoutBtnSt.setToY(0.5);

        logoutBtnPt = new ParallelTransition(logoutBtn, logoutBtnFt, logoutBtnSt);
        logoutBtnSt.interpolatorProperty().set(Interpolator.EASE_IN);
    }

    Connection connection;

    public void initializeDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/studentrecords", "root", "");
            System.out.println("Database Connection");
        } catch (ClassNotFoundException | SQLException ex) {
        }
    }

    FadeTransition returnBtnFt, viewResultBtnFt, logoutBtnFt, registrationTbFt, viewYearBookPaneFt, resultPaneFt, registerBtnFt;
    ScaleTransition registerBtnSt, logoutBtnSt, viewYearBookPaneSt, registrationTbSt, viewResultBtnSt;
    TranslateTransition registerBtnTt, returnBtnTt;
    ParallelTransition registerBtnPt, viewResultBtnPt, logoutBtnPt, registrationTbPt, viewYearBookPanePt, returnBtnPt;

    @FXML
    private void registerStudentAnimation(ActionEvent event) throws SQLException {
        if (registerBtn.getTranslateX() != 315) {
            currentState = "RegisterButton";
            returnBtn.setVisible(true);

            if (returnBtn.getLayoutX() != 14) {
                returnBtn.setLayoutX(14);
            }
            returnBtnFt = new FadeTransition(Duration.millis(400), returnBtn);
            returnBtnFt.setFromValue(0);
            returnBtnFt.setToValue(1);
            returnBtnFt.setDelay(Duration.millis(800));
            returnBtnFt.interpolatorProperty().set(Interpolator.EASE_OUT);
            returnBtnFt.play();

            registerBtnTt = new TranslateTransition(Duration.millis(400));
            registerBtnTt.setFromX(0);
            registerBtnTt.setFromY(0);
            registerBtnTt.setToX(315);
            registerBtnTt.setToY(180);
            registerBtnTt.interpolatorProperty().set(Interpolator.EASE_OUT);

            registerBtnPt = new ParallelTransition(registerBtn, registerBtnSt, registerBtnTt);
            registerBtnPt.interpolatorProperty().set(Interpolator.EASE_BOTH);
            registerBtnPt.play();

            viewResultBtnPt.play();
            viewResultBtn.setVisible(false);

            logoutBtn.setVisible(false);
            logoutBtnPt.play();

            showStudentView("2017/2018");
            viewYearBookPane.setVisible(true);
            viewYearBookPaneFt = new FadeTransition(Duration.millis(500));
            viewYearBookPaneFt.setFromValue(0);
            viewYearBookPaneFt.setToValue(1);

            viewYearBookPaneSt = new ScaleTransition(Duration.millis(500));
            viewYearBookPaneSt.setFromX(0.9);
            viewYearBookPaneSt.setFromY(0.7);
            viewYearBookPaneSt.setToX(1);
            viewYearBookPaneSt.setToY(1);

            viewYearBookPanePt = new ParallelTransition(viewYearBookPane, viewYearBookPaneFt, viewYearBookPaneSt);
            viewYearBookPanePt.setDelay(Duration.millis(400));
            viewYearBookPanePt.interpolatorProperty().set(Interpolator.EASE_OUT);
            viewYearBookPanePt.play();

//            registrationTb.setVisible(true);
            /* registrationTbFt = new FadeTransition(Duration.millis(500));
            registrationTbFt.setFromValue(0);
            registrationTbFt.setToValue(1);

            registrationTbSt = new ScaleTransition(Duration.millis(500));
            registrationTbSt.setFromX(0.9);
            registrationTbSt.setFromY(0.9);
            registrationTbSt.setToX(1);
            registrationTbSt.setToY(1);

//            registrationTbPt = new ParallelTransition(registrationTb, registrationTbFt, registrationTbSt);
            registrationTbPt.setDelay(Duration.millis(400));
            registrationTbPt.interpolatorProperty().set(Interpolator.EASE_OUT);
            registrationTbPt.play();
             */
            // viewCourses(matric);    
        } else {
            returnBtn.setVisible(false);
            registerBtnPt.setRate(-1);
            registerBtnPt.play();
            returnBtnFt.setRate(-1);
            returnBtnFt.play();
            viewResultBtn.setVisible(true);
            viewResultBtnPt.setRate(-1);
            viewResultBtnPt.play();
            logoutBtn.setVisible(true);
            logoutBtnPt.setRate(-1);
            logoutBtnPt.play();
            //registrationTbPt.setRate(-1);
            //registrationTbPt.play();
//            registrationTb.setVisible(false);
            viewYearBookPanePt.setRate(-1);
            viewYearBookPanePt.play();
            viewYearBookPane.setVisible(false);
            //viewCourses("");
        }
    }
    String currentState = "";

    private void getStudent(String name) throws SQLException {

        System.out.println("Getting Page For this  " + name);
        String style = "-fx-text-alignment: justify; -fx-text-fill: white; ";

        String queryString = "SELECT  DISTINCT image, LessonQuote , WisdomQuote ,StudentIDN FROM yearbook INNER JOIN STUDENT USING(studentIDN) WHERE CONCAT_WS(' ',LastName , FirstName, OtherName) = ? ";

        preparedStatement1 = connection.prepareStatement(queryString);
        preparedStatement1.setString(1, name);

        ResultSet resultSet = preparedStatement1.executeQuery();

        while (resultSet.next()) {
            System.out.println("Record Found for  " + name);
            studentQuote.getChildren().clear();

            studentImage.setImage(new Image(resultSet.getBlob("image").getBinaryStream()));
            Label lessonLabel = new Label(resultSet.getString("LessonQuote"));

            Label wisdomLabel = new Label(resultSet.getString("WisdomQuote"));

            lessonLabel.setStyle(style);
            wisdomLabel.setStyle(style);

            studentQuote.getChildren().add(lessonLabel);
            studentQuote.getChildren().add(wisdomLabel);

        }

    }
    
    @FXML
    private void hideStudentPane(ActionEvent evt){
    
        studentPane.setVisible(false);
        
    }

    private void showStudentView(String session) throws SQLException {

//        ObservableList list = FXCollections.observableArrayList();
        System.out.println("Populating the Listview with the list of Graduating students in  " + session);

        //String queryString = "SELECT LastName , FirstName, OtherName, image, LessonQuote , WisdomQuote   FROM yearbook INNER JOIN STUDENT USING(studentIDN)  WHERE session = ?  ";
        String queryString = "SELECT LastName , FirstName, OtherName, StudentLevel, image,LessonQuote , WisdomQuote   FROM STUDENT INNER JOIN Yearbook "
                + "USING(studentidn)";

        preparedStatement1 = connection.prepareStatement(queryString);
        //preparedStatement1.setString(1, session);

        ResultSet resultSet = preparedStatement1.executeQuery();

        int x = 0, y = 0;
        int count = 0;
        while (resultSet.next()) {

            String name = String.format("%s %s %s", resultSet.getString("LastName"), resultSet.getString("FirstName"), resultSet.getString("OtherName"));
            String level = resultSet.getString("StudentLevel");
            String lessonQuote = resultSet.getString("lessonQuote");
            String wisdomQuote = resultSet.getString("WisdomQuote");
            
            Image image = new Image(resultSet.getBlob("image").getBinaryStream());

            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            VBox vBox = new VBox();
            vBox.setPrefSize(120, 120);
            vBox.setMinHeight(120);

            vBox.setPadding(new Insets(5, 5, 5, 5));
            vBox.getChildren().add(imageView);
            vBox.setOnMouseClicked(e -> {
                System.out.println("Opening Information for  " + name);
                studentPaneImageView.setImage(image);
                studentFullname.setText(name);
                studentClass.setText(level);
                studentPaneLessonQuote.setText(lessonQuote );
                studentPaneWisdomQuote.setText(wisdomQuote );
                //studentPosition
                studentPane.setVisible(true);
                e.consume();
            });

            
            gridPane.add(vBox, x++, y);

            if (x == 3) {
                x = 0;
                y++;
            }
            count++;
            //list.add( String.format("%s %s %s", resultSet.getString("LastName"),resultSet.getString("FirstName"),resultSet.getString("OtherName")) );
        }
        int row;
        if ((count % 3) != 0) {
            row = (count + (3 - (count % 3))) / 3;
        } else {
            row = count / 3;
        }

        if (count > 3) {
            gridPane.setPrefHeight(150 * row);
        }

        //System.out.println("count is" + count);
        //System.out.println("the resultset is " + row);
    }

    @FXML
    private void returnAnimation(ActionEvent event) throws SQLException {
        switch (currentState) {
            case "RegisterButton":
                returnBtn.setVisible(false);
                registerBtnPt.setRate(-1);
                registerBtnPt.play();
                returnBtnFt.setRate(-1);
                returnBtnFt.play();
                viewResultBtn.setVisible(true);
                viewResultBtnPt.setRate(-1);
                viewResultBtnPt.play();
                logoutBtn.setVisible(true);
                logoutBtnPt.setRate(-1);
                logoutBtnPt.play();
                //registrationTbPt.setRate(-1);
                //registrationTbPt.play();
//                registrationTb.setVisible(false);
                viewYearBookPanePt.setRate(-1);
                viewYearBookPanePt.play();
                viewYearBookPane.setVisible(false);
                // viewCourses("");
                break;
            case "ViewResultButton":
                returnBtnPt.setRate(-1);
                returnBtnPt.play();
                resultPaneFt.setRate(-1);
                resultPaneFt.play();
                resultPane.setVisible(false);
                registerBtnPt.setRate(-1);
                registerBtnPt.play();
                registerBtn.setVisible(true);
                logoutBtnPt.setRate(-1);
                logoutBtnPt.play();
                logoutBtn.setVisible(true);
                viewResultBtnPt.setRate(-1);
                viewResultBtnPt.play();
                viewResultBtn.setVisible(true);
                //viewYearBookPanePt.setRate(-1);
                //viewYearBookPanePt.play();
                viewYearBookPane.setVisible(false);
                //content.setPrefHeight(0);
                break;
        }
    }

    @FXML
    private void viewResultAnimation(ActionEvent event) throws SQLException {
        currentState = "ViewResultButton";
        returnBtn.setVisible(true);
        returnBtnFt = new FadeTransition(Duration.millis(400), returnBtn);
        returnBtnFt.setFromValue(0);
        returnBtnFt.setToValue(1);
        returnBtnFt.setDelay(Duration.millis(800));
        returnBtnFt.interpolatorProperty().set(Interpolator.EASE_OUT);
        returnBtnFt.setDelay(Duration.millis(200));
        returnBtnFt.setRate(1);
        returnBtnTt = new TranslateTransition(Duration.millis(500));
        if (returnBtn.getLayoutX() != 486) {
            returnBtn.setLayoutX(486);
        }
        returnBtnTt.setFromY(0);
        //returnBtnTt.setToY(-40);//put this back 13-9-2018 
        returnBtnTt.interpolatorProperty().set(Interpolator.EASE_OUT);
        returnBtnPt = new ParallelTransition(returnBtn, returnBtnTt, returnBtnFt,
                new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(returnBtn.blendModeProperty(), BlendMode.EXCLUSION))));
        returnBtnPt.play();
        System.out.println("ReturnBtnLayoutX: " + returnBtn.getLayoutX());
        System.out.println("ReturnBtnTranslateX: " + returnBtn.getTranslateX());

        registerBtnPt = new ParallelTransition(registerBtn, registerBtnFt, registerBtnSt);
        registerBtnPt.interpolatorProperty().set(Interpolator.EASE_OUT);
        registerBtnPt.play();
        registerBtn.setVisible(false);
        viewResultBtnPt.play();
        viewResultBtn.setVisible(false);
        logoutBtnPt.play();
        logoutBtn.setVisible(false);
        resultPane.setVisible(true);
        resultPaneFt = new FadeTransition(Duration.millis(500), resultPane);
        resultPaneFt.setDelay(Duration.millis(400));
        resultPaneFt.setFromValue(0);
        resultPaneFt.setToValue(1);
        resultPaneFt.play();

        submitYearBookButton.setOnAction(e -> {

            if (updateState) {

                try {
                    System.out.println("Update in year book");

                    String queryString = "UPDATE yearbook set LessonQuote = ?, WisdomQuote = ? WHERE StudentIDN = ? AND closed = '0'  ";

                    preparedStatement1 = connection.prepareStatement(queryString);
                    preparedStatement1.setString(1, lessonQuote.getText());
                    preparedStatement1.setString(2, wisdomQuote.getText());
                    preparedStatement1.setString(3, matric);

                    preparedStatement1.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(StudentPageController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {

                try {
                    System.out.println("Fresh Entry into year book");

                    String queryString = "INSERT INTO yearbook set LessonQuote = ?, WisdomQuote = ? ,StudentIDN = ?, Session = ?  ";

                    preparedStatement1 = connection.prepareStatement(queryString);
                    preparedStatement1.setString(1, lessonQuote.getText());
                    preparedStatement1.setString(2, wisdomQuote.getText());
                    preparedStatement1.setString(3, matric);
                    preparedStatement1.setString(4, currentSession);

                    preparedStatement1.execute();

                } catch (SQLException ex) {
                    Logger.getLogger(StudentPageController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            showAndRemoveUpdateSuccessMsg();   
        });
        showGrades(matric);

     
        
    }

    private void showAndRemoveUpdateSuccessMsg(){
    
        updateTextLabel.setVisible(true);
        FadeTransition updateTextLabelFt = new FadeTransition(Duration.millis(700), updateTextLabel);
        updateTextLabelFt.setFromValue(1);
        updateTextLabelFt.setToValue(0);
        updateTextLabelFt.play();
    
    }
    private PreparedStatement preparedStatement2;
    double cgpa, cummulativeGradeUnits, cummulativeSemesterUnits;
    String semester;

    @FXML
    private VBox content;

    public PreparedStatement preparedStatement1;

    private void showGrades(String matricNo) throws SQLException {
        semester = "";
        cgpa = 0;
        cummulativeGradeUnits = 0;
        cummulativeSemesterUnits = 0;
        //content.getChildren().clear();
        System.out.println("Get Year book information");

        String queryString = "SELECT LessonQuote, WisdomQuote FROM yearbook WHERE StudentIDN = ? AND closed = '0' LIMIT 1 ";

        preparedStatement1 = connection.prepareStatement(queryString);
        preparedStatement1.setString(1, matricNo);

        ResultSet resultSet = preparedStatement1.executeQuery();

        String levelString = "";
        if (resultSet.next()) {

            updateState = true;

            lessonQuote.setText(resultSet.getString("LessonQuote"));
            wisdomQuote.setText(resultSet.getString("WisdomQuote"));

        }

    }

}
