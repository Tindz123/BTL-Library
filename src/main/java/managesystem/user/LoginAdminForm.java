package managesystem.user;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginAdminForm extends CoreDatabase {

    @FXML
    private Button btnLoginADmin;

    @FXML
    private Button btnReturnClient;

    @FXML
    private AnchorPane loginadmin_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;


    public void loginAdmin() {
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";

        con = AccountDB.getCon();

        try {
            Alert alert;

            st = con.prepareStatement(sql);
            st.setString(1, username.getText());
            st.setString(2, password.getText());

            rs = st.executeQuery();

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                if (rs.next()) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();


                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user.fxml"));
                    Parent managerRoot = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(managerRoot));
                    stage.show();

                    Stage currentStage = (Stage) loginadmin_form.getScene().getWindow();
                    currentStage.close();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openLoginClient() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginClient.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loginRoot));

            newStage.show();

            Stage currentStage = (Stage) btnLoginADmin.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
