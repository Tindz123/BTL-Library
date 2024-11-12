package managesystem.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserManagerSystem extends CoreDatabase {

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colPassword;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, LocalDate> colDateOfBirth;
    @FXML
    private TableColumn<User, String> colGender;
    @FXML
    private TableColumn<User, String> colPhoneNumber;
    @FXML
    private TableColumn<User, String> colAddress;
    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField tName;
    @FXML
    private DatePicker tDateOfBirth;
    @FXML
    private ComboBox<String> tGender;
    @FXML
    private TextField tPhoneNumber;
    @FXML
    private TextField tAddress;
    @FXML
    private TextField tEmail;

    @FXML
    private Button btnSearch;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    
    
    public ObservableList<User> UserListData() {
        ObservableList<User> list = FXCollections.observableArrayList();
        String sql = "SELECT client.username, client.password, user.name, user.date, user.gender, user.phoneNumber, user.address, user.email "
                + "FROM client "
                + "JOIN user ON client.id = user.id";

        con = AccountDB.getCon();

        try {
            User user;
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                user = new User(rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("email"));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ObservableList<User> UserShowListD;
    public void UserShowListData() {
        UserShowListD = UserListData();

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("date"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.setItems(UserShowListD);
    }

    @FXML
    public void addU(ActionEvent event) {
        String addClient = "INSERT INTO client(username, password) VALUES(?, ?)";
        String addUser = "INSERT INTO user(name, date, gender, phoneNumber, address, email) VALUES(?, ?, ?, ?, ?, ?)";

        con = AccountDB.getCon();

        try {
            st = con.prepareStatement(addClient);
            st.setString(1, username.getText());
            st.setString(2, password.getText());
            st.executeUpdate();

            String getClientId = "SELECT id FROM client WHERE username = ?";
            st = con.prepareStatement(getClientId);
            st.setString(1, username.getText());
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("id");

                st = con.prepareStatement(addUser);
                st.setString(1, tName.getText());
                st.setDate(2, Date.valueOf(tDateOfBirth.getValue()));
                st.setString(3, tGender.getValue());
                st.setString(4, tPhoneNumber.getText());
                st.setString(5, tAddress.getText());
                st.setString(6, tEmail.getText());

                st.executeUpdate();

                UserShowListData();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User added successfully!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding user");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void updateUser(ActionEvent event) {
        String upClient = "UPDATE client SET password = ? WHERE username = ?";
        String upUser = "UPDATE user SET name = ?, date = ?, gender = ?, phoneNumber = ?, address = ?, email = ? WHERE id = ?";

        con = AccountDB.getCon();
        try {
            st = con.prepareStatement(upClient);
            st.setString(1, password.getText());
            st.setString(2, username.getText());
            st.executeUpdate();

            String getClientId = "SELECT id FROM client WHERE username = ?";
            st = con.prepareStatement(getClientId);
            st.setString(1, username.getText());
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("id");

                st = con.prepareStatement(upUser);
                st.setString(1, tName.getText());
                st.setDate(2, Date.valueOf(tDateOfBirth.getValue()));
                st.setString(3, tGender.getValue());
                st.setString(4, tPhoneNumber.getText());
                st.setString(5, tAddress.getText());
                st.setString(6, tEmail.getText());
                st.setInt(7, clientId);
                st.executeUpdate();

                UserShowListData();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User updated successfully!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error updating user");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteUser(javafx.event.ActionEvent event) {
        String deleteClient = "DELETE FROM client WHERE username = ?";
        String deleteUser = "DELETE FROM user WHERE phoneNumber = ?";
        con = AccountDB.getCon();

        try {
            con.setAutoCommit(false);

            try (PreparedStatement prepare = con.prepareStatement(deleteClient)) {
                prepare.setString(1, username.getText());
                prepare.executeUpdate();
            }

            try (PreparedStatement prepare = con.prepareStatement(deleteUser)) {
                prepare.setString(1, tPhoneNumber.getText());
                prepare.executeUpdate();
            }

            con.commit();
            UserShowListData();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User deleted successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error deleting user");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    public void clearUser(javafx.event.ActionEvent event) {
        username.clear();
        password.clear();
        tName.clear();
        tDateOfBirth.setValue(null);
        tGender.setValue(null);
        tPhoneNumber.clear();
        tAddress.clear();
        tEmail.clear();
        btnAdd.setDisable(false);
    }

    @FXML
    void getData(javafx.scene.input.MouseEvent event) {
        User user = table.getSelectionModel().getSelectedItem();
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        tName.setText(user.getName());
        tDateOfBirth.setValue(user.getDate());
        tGender.setValue(user.getGender());
        tPhoneNumber.setText(user.getPhoneNumber());
        tAddress.setText(user.getAddress());
        tEmail.setText(user.getEmail());
        btnAdd.setDisable(true);
    }


    @FXML
    public void searchUser(ActionEvent event) {
        String searchUsername = username.getText().toLowerCase().trim();
        String searchPassword = password.getText().toLowerCase().trim();
        String searchName = tName.getText().toLowerCase().trim();
        String searchPhone = tPhoneNumber.getText().toLowerCase().trim();
        String searchAddress = tAddress.getText().toLowerCase().trim();
        String searchEmail = tEmail.getText().toLowerCase().trim();

        ObservableList<User> filteredList = FXCollections.observableArrayList();

        String sql = "SELECT client.username, client.password, user.name, user.date, user.gender, user.phoneNumber, user.address, user.email "
                + "FROM client "
                + "JOIN user ON client.id = user.id WHERE 1=1";

        if (!searchUsername.isEmpty()) {
            sql += " AND LOWER(client.username) LIKE ?";
        }
        if (!searchPassword.isEmpty()) {
            sql += " AND LOWER(client.password) LIKE ?";
        }
        if (!searchName.isEmpty()) {
            sql += " AND LOWER(user.name) LIKE ?";
        }
        if (!searchPhone.isEmpty()) {
            sql += " AND LOWER(user.phoneNumber) LIKE ?";
        }
        if (!searchAddress.isEmpty()) {
            sql += " AND LOWER(user.address) LIKE ?";
        }
        if (!searchEmail.isEmpty()) {
            sql += " AND LOWER(user.email) LIKE ?";
        }

        try {
            con = AccountDB.getCon();
            st = con.prepareStatement(sql);

            int index = 1;
            if (!searchUsername.isEmpty()) st.setString(index++, "%" + searchUsername + "%");
            if (!searchPassword.isEmpty()) st.setString(index++, "%" + searchPassword + "%");
            if (!searchName.isEmpty()) st.setString(index++, "%" + searchName + "%");
            if (!searchPhone.isEmpty()) st.setString(index++, "%" + searchPhone + "%");
            if (!searchAddress.isEmpty()) st.setString(index++, "%" + searchAddress + "%");
            if (!searchEmail.isEmpty()) st.setString(index++, "%" + searchEmail + "%");

            rs = st.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("email"));
                filteredList.add(user);
            }

            table.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error during search");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void openLoginClient() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginClient.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loginRoot));

            newStage.show();

            Stage currentStage = (Stage) btnUpdate.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        UserShowListData();
    }
}
