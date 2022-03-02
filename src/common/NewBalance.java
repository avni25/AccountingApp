package common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NewBalance {
    @FXML
    private TextField text_name;
    @FXML
    private Label label_res;
    @FXML
    private Label label_res2;

    public void initialize(){
        label_res.setText("");
        label_res2.setText("");

        text_name.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onClick_AddBalance();
            }
        });

    }

    @FXML
    public void onClick_AddBalance(){
        Balance b = getBalance();
        LoginController lc = new LoginController();

        if(b!=null){
            b.addToDB();
            lc.u.add(b);
            text_name.setText("");
            label_res.setText("");
            label_res2.setText("Balance added");
            Alert al = new Alert(Alert.AlertType.INFORMATION,"Balance Added...", ButtonType.OK);
            al.show();

            lc.onClick_refresh();


        }
    }

    private Balance getBalance(){
        String account_name = text_name.getText();
        if(account_name.equals("")){
            label_res.setText("name field can not be empty");
        }else{
            Balance b = new Balance();
            b.setAccountName(account_name);
            b.setUser_id(LoginController.u.getId());
            return b;
        }
        return null;
    }


}
