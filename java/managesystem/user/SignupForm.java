package managesystem.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupForm implements Initializable {
    private double x = 0;
    private double y = 0;

    @FXML
    private Button btnReturnLogin;

    @FXML
    private Button btnSignUp;

    @FXML
    private PasswordField password;

    @FXML
    private AnchorPane signUpForm;

    @FXML
    private TextField tAddress;

    @FXML
    private DatePicker tDate;

    @FXML
    private TextField tEmail;

    @FXML
    private ComboBox<String> tGender;

    @FXML
    private TextField tName;

    @FXML
    private TextField tPhone;

    @FXML
    private TextField username;

    private Connection con = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;

    public void signup() {
        String sqlCheckUser = "SELECT * FROM client WHERE username = ?";
        String sqlInsertAdmin = "INSERT INTO client (username, password) VALUES (?, ?)";
        String sqlInsertUser = "INSERT INTO user (name, date, gender, phoneNumber, address, email) VALUES (?, ?, ?, ?, ?, ?)";

        con = AccountDB.getCon();

        Alert alert;

        if (username.getText().isEmpty() || password.getText().isEmpty() || tName.getText().isEmpty() ||
                tPhone.getText().isEmpty() || tAddress.getText().isEmpty() || tEmail.getText().isEmpty() ||
                tDate.getValue() == null || tGender.getValue() == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        try (PreparedStatement checkStmt = con.prepareStatement(sqlCheckUser)) {
            checkStmt.setString(1, username.getText());
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists!");
                alert.showAndWait();
            } else {
                try (PreparedStatement insertAdminStmt = con.prepareStatement(sqlInsertAdmin)) {
                    insertAdminStmt.setString(1, username.getText());
                    insertAdminStmt.setString(2, password.getText());
                    int rowsAffectedAdmin = insertAdminStmt.executeUpdate();

                    if (rowsAffectedAdmin > 0) {
                        try (PreparedStatement insertUserStmt = con.prepareStatement(sqlInsertUser)) {
                            insertUserStmt.setString(1, tName.getText());
                            insertUserStmt.setString(2, tDate.getValue().toString());
                            insertUserStmt.setString(3, tGender.getValue());
                            insertUserStmt.setString(4, tPhone.getText());
                            insertUserStmt.setString(5, tAddress.getText());
                            insertUserStmt.setString(6, tEmail.getText());

                            int rowsAffectedUser = insertUserStmt.executeUpdate();

                            if (rowsAffectedUser > 0) {
                                alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Account successfully created!");
                                alert.showAndWait();

                                switchForm();
                            } else {
                                alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Error creating user!");
                                alert.showAndWait();
                            }
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("An error occurred while creating the account.");
                        alert.showAndWait();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    private void switchForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginClient.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loginRoot));

            newStage.show();

            Stage currentStage = (Stage) btnReturnLogin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
