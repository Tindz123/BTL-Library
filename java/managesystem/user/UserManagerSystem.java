package managesystem.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserManagerSystem {

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

    private Connection con;
    private PreparedStatement prepare;
    private ResultSet rs;


    public ObservableList<User> UserListData() {
        ObservableList<User> list = FXCollections.observableArrayList();
        String sql = "SELECT client.username, client.password, user.name, user.date, user.gender, user.phoneNumber, user.address, user.email "
                + "FROM client "
                + "JOIN user ON client.id = user.id";

        con = AccountDB.getCon();

        try {
            User user;
            prepare = con.prepareStatement(sql);
            rs = prepare.executeQuery();
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
    public void addU(javafx.event.ActionEvent event) {
        String addClient = "INSERT INTO client(username, password) VALUES(?, ?)";
        String addUser = "INSERT INTO user(name, date, gender, phoneNumber, address, email) VALUES(?, ?, ?, ?, ?, ?)";

        con = AccountDB.getCon();

        try {
            prepare = con.prepareStatement(addClient);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            prepare.executeUpdate();

            String getClientId = "SELECT id FROM client WHERE username = ?";
            prepare = con.prepareStatement(getClientId);
            prepare.setString(1, username.getText());
            ResultSet rs = prepare.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("id");

                prepare = con.prepareStatement(addUser);
                prepare.setString(1, tName.getText());
                prepare.setDate(2, java.sql.Date.valueOf(tDateOfBirth.getValue()));
                prepare.setString(3, tGender.getValue());
                prepare.setString(4, tPhoneNumber.getText());
                prepare.setString(5, tAddress.getText());
                prepare.setString(6, tEmail.getText());

                prepare.executeUpdate();

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
    public void updateUser(javafx.event.ActionEvent event) {
        String upClient = "UPDATE client SET password = ? WHERE username = ?";
        String upUser = "UPDATE user SET name = ?, date = ?, gender = ?, phoneNumber = ?, address = ?, email = ? WHERE id = ?";

        con = AccountDB.getCon();
        try {
            prepare = con.prepareStatement(upClient);
            prepare.setString(1, password.getText());
            prepare.setString(2, username.getText());
            prepare.executeUpdate();

            String getClientId = "SELECT id FROM client WHERE username = ?";
            prepare = con.prepareStatement(getClientId);
            prepare.setString(1, username.getText());
            ResultSet rs = prepare.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("id");

                prepare = con.prepareStatement(upUser);
                prepare.setString(1, tName.getText());
                prepare.setDate(2, java.sql.Date.valueOf(tDateOfBirth.getValue()));
                prepare.setString(3, tGender.getValue());
                prepare.setString(4, tPhoneNumber.getText());
                prepare.setString(5, tAddress.getText());
                prepare.setString(6, tEmail.getText());
                prepare.setInt(7, clientId);
                prepare.executeUpdate();

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
    public void searchUser(javafx.event.ActionEvent event) {
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
            prepare = con.prepareStatement(sql);

            int index = 1;
            if (!searchUsername.isEmpty()) prepare.setString(index++, "%" + searchUsername + "%");
            if (!searchPassword.isEmpty()) prepare.setString(index++, "%" + searchPassword + "%");
            if (!searchName.isEmpty()) prepare.setString(index++, "%" + searchName + "%");
            if (!searchPhone.isEmpty()) prepare.setString(index++, "%" + searchPhone + "%");
            if (!searchAddress.isEmpty()) prepare.setString(index++, "%" + searchAddress + "%");
            if (!searchEmail.isEmpty()) prepare.setString(index++, "%" + searchEmail + "%");

            rs = prepare.executeQuery();

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error during search");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    public void initialize() {
        UserShowListData();
    }
}
