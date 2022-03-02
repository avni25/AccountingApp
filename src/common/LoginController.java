package common;

import com.data.DatabaseConnector;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.scene.control.Alert.*;

public class LoginController {
    @FXML
    private TextField username_text;
    @FXML
    private PasswordField password_text;
    @FXML
    private Pane accountPane;
    @FXML
    private Button btn_refresh;
    @FXML
    private Pane myPane;
    @FXML
    private Label label_head;

    @FXML
    public ListView<Balance> bList;

    public static List<User> userList = new ArrayList<>();
    public static List<Balance> balanceList = new ArrayList<>();
    public static List<Transaction> tsList = new ArrayList<>();
    public static User u;
    public static Balance userCurrentBalance;


    public void initialize(){

        loadUsersList();
        loadBalances();
        label_head.setText("Welcome");
        showList(tsList);
        showList(userList);
        showList(balanceList);

        final KeyCombination comb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);

        password_text.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (comb.match(event)) {
                onClick_Login_Btn();
            }
        });
//        password_text.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                 onClick_Login_Btn();
//            }
//        });

    }



    public void onClick_Login_Btn(){
        String usernameValue = username_text.getText();
        String passwordValue = password_text.getText();
        for(int i=0;i<userList.size();i++){
            if(usernameValue.equals(userList.get(i).getEmail())){
                if(passwordValue.equals(userList.get(i).getPassword())){

                    u =userList.get(i);
                    //open new window and close this one
                    System.out.println("CORRECT LOGIN");
                    label_head.setText("CORRECT LOGIN");
                    //Main.window.close();
                    Main.window.setHeight(500);
                    accountPane.setVisible(true);
                    bList.getItems().clear();
                    bList.getItems().addAll(u.getBalanceList());
                    bList.requestFocus();
                    return;
                }
                System.out.println(userList.get(i).getEmail()+ ", Invalid password. please try again");
                label_head.setText("Invalid password. please try again");
                password_text.setText("");
                return;
            }
        }
        System.out.println("no user found as "+usernameValue);
        label_head.setText("no user found as "+usernameValue);
    }

    public void loadUsersList(){
        DatabaseConnector ud = new DatabaseConnector();
        ud.open();
        try{
            userList= ud.loadUsers();
            System.out.println("user list loaded...");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        ud.close();
    }

    public void loadBalances(){
        DatabaseConnector ud = new DatabaseConnector();
        ud.open();
        try{
            balanceList= ud.loadBalances();
            System.out.println("Balance list loaded...");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        ud.close();
    }

    @FXML
    public void onClick_refresh(){
        bList.getItems().clear();
        bList.getItems().addAll(u.getBalancesFromDB());
    }

    @FXML
    public void refreshLists(){
        loadUsersList();
        loadBalances();
    }

    @FXML
    public void addBalance(){
        openWindow("NewBalance.fxml","Add new balance",false, 375,400);
    }

    @FXML
    public void addUser(){
        openWindow("NewUser.fxml","Add new user",false, 375,400);
    }

    @FXML
    public void OnClick_GO(){
        userCurrentBalance = bList.getSelectionModel().getSelectedItem();
        if(userCurrentBalance == null){
            System.out.println("choose an account please");
        }else{
            openWindow("MainWin.fxml", "Transactions",true,800,600);
        }

    }

    public static <T> void showList(List<T> l){
        System.out.println("-----------------------");
        for(T a: l){
            System.out.println(a);
        }
        System.out.println("------------------------");
    }

    public void openWindow(String fxmlFile,String title, boolean resizeable, int x, int y){
        try{
            FXMLLoader fxmlLoader  =new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage s = new Stage();
            s.setTitle(title);
            s.setResizable(resizeable);
            s.setScene(new Scene(root1, x,y));
            s.show();

        }catch(Exception e){
            System.out.println("cant open Window. "+e.getMessage());
        }
    }

    @FXML
    public void updateBtn(){
        DatabaseConnector dc = new DatabaseConnector();
        Balance b  = bList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog("Update Account name");
        dialog.setTitle("Update Account");
        dialog.setHeaderText("Update balance name");
        dialog.setContentText("Please enter new name for current account:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            b.setAccountName(result.get());
        }
        dc.open();
        try{
            dc.update(b);
            onClick_refresh();
        }catch(SQLException e){
            e.printStackTrace();
        }
        dc.close();
    }

    @FXML
    public void deleteBtn(){
        DatabaseConnector dc = new DatabaseConnector();
        Balance b  = bList.getSelectionModel().getSelectedItem();
        dc.open();
        try {
            if(b!=null){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + b.getAccountName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    dc.delete(b);
                    onClick_refresh();
                    Alert al = new Alert(AlertType.INFORMATION,"Balance deleted", ButtonType.OK);
                    al.show();
                    System.out.println("balance deleted");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dc.close();
    }


}
