package managesystem.user;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginClientForm extends CoreDatabase {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;



    public void loginClient(){

        String sql = "SELECT * FROM client WHERE username = ? and password = ?";

        con = AccountDB.getCon();

        try{
            Alert alert;

            st = con.prepareStatement(sql);
            st.setString(1, username.getText());
            st.setString(2, password.getText());

            rs = st.executeQuery();


            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if(rs.next()){

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();


                    btnLogin.getScene().getWindow().hide();

                    /**
                     *
                     *
                     *Phần sách ở đây
                    */


                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password!");
                    alert.showAndWait();

                }
            }
        }catch(Exception e){e.printStackTrace();}

    }

    public void openSignupForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loginRoot));

            newStage.show();

            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openLoginAdmin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginAdmin.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loginRoot));

            newStage.show();

            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


