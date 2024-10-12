package com.example.controlleruser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerUser implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnShow;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tDateOfBirth;

    @FXML
    private TextField tId;

    @FXML
    private TextField tName;

    @FXML
    private TextField tPhoneNumber;

    @FXML
    private TableColumn<User, String> colDateOfBirth;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colPhoneNumber;

    @FXML
    private TableView<User> table;
    int id = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showUser();
    }

    public ObservableList<User> getUser() {
        ObservableList<User> users = FXCollections.observableArrayList();

        String query = "select* from users";
        con = DBUser.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("Name"));
                user.setDateOfBirth(rs.getString("Date_Of_Birth"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void showUser() {
        ObservableList<User> list = getUser();
        table.setItems(list);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    @FXML
    void deleteStudent(ActionEvent event) {
        String delete = "delete from users where id = ?";
        con = DBUser.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1, id);
            st.executeUpdate();
            showUser();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void saveStudent(ActionEvent event) {

        String insert = "insert into users(Name, Date_Of_Birth, PhoneNumber) values(?,?,?)";
        con = DBUser.getCon();
        try {
            st = con.prepareStatement(insert);
            st.setString(1, tName.getText());
            st.setString(2, tDateOfBirth.getText());
            st.setString(3, tPhoneNumber.getText());
            st.executeUpdate();
            tName.clear();
            tDateOfBirth.clear();
            tPhoneNumber.clear();
            showUser();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void getData(javafx.scene.input.MouseEvent event) {
        User user = table.getSelectionModel().getSelectedItem();
        id = user.getId();
        tName.setText(user.getName());
        tDateOfBirth.setText(user.getDateOfBirth());
        tPhoneNumber.setText(user.getPhoneNumber());
        btnSave.setDisable(false);
    }

    @FXML
    void showStudent(ActionEvent event) {
        showUser();
    }

    @FXML
    void updateStudent(ActionEvent event) {
        String update = "update users set Name = ?, Date_Of_Birth = ?, PhoneNumber = ? where id = ?";
        con = DBUser.getCon();
        try {
            st = con.prepareStatement(update);
            st.setString(1, tName.getText());
            st.setString(2, tDateOfBirth.getText());
            st.setString(3, tPhoneNumber.getText());
            st.setInt(4, id);
            st.executeUpdate();
            showUser();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}


