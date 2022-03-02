package common;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NewUser {

    @FXML
    private TextField text_name;
    @FXML
    private TextField text_surname;
    @FXML
    private TextField text_email;
    @FXML
    private PasswordField text_pass;
    @FXML
    private PasswordField text_repass;
    @FXML
    private Label label_res;
    @FXML
    private Label label_res2;

    public void initialize(){
        label_res.setText("");
        label_res2.setText("");

        text_repass.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onClick_AddUser();
            }
        });
    }


    public void onClick_AddUser(){
        User u = getUser();
        if(u!=null){
            u.addToDB();
            LoginController.userList.add(u);
            text_name.setText("");
            text_surname.setText("");
            text_email.setText("");
            text_pass.setText("");
            text_repass.setText("");
            label_res2.setText("User added");
            label_res.setText("");

            Alert al = new Alert(Alert.AlertType.INFORMATION,"User Added...", ButtonType.OK);
            al.show();
        }

    }

    private User getUser(){
        String name = text_name.getText();
        String surname = text_surname.getText();
        String email = text_email.getText();
        String pass = text_pass.getText();
        String repass = text_repass.getText();
        String[] fields = {name, surname, email, pass, repass};
        for(String s: fields){
            if(s.equals("")){
                System.out.println(s+" field can not be empty");
                label_res.setText(s+" field can not be empty");
                return null;
            }
        }
        if(pass.equals(repass)){
            User u = new User();
            u.setName(name);
            u.setSurname(surname);
            u.setEmail(email);
            u.setPassword(pass);
            return u;
        }else{
            System.out.println("password doesn't match with re-password");
            label_res.setText("password doesn't match with re-password");
        }

        return null;
    }

}
