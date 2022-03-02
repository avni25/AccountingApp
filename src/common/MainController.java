package common;


import com.data.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {

    @FXML
    private ComboBox<String> combo_Transaction;
    @FXML
    private TextField text_amount;
    @FXML
    private TextField text_info;
    @FXML
    private DatePicker date_transaction;
    @FXML
    private Label label_totalAmount;
    @FXML
    private Label label_expList;
    @FXML
    private Label label_incomeList;
    @FXML
    private Label label_balanceOwner;
    @FXML
    private Label label_balanceName;
    @FXML
    private TableView<Transaction> incomeTable;
    @FXML
    private TableView<Transaction> expenditureTable;
    @FXML
    private ObservableList<Transaction> Olist;
    @FXML
    private ObservableList<Transaction> OExp;
    @FXML
    private ObservableList<Transaction> OInc;



    public void initialize(){
        setLabels();
        combo_Transaction.getItems().addAll("INC", "EXP");
        date_transaction.setValue(LocalDate.now());
        date_transaction.setEditable(true);

        LoginController lc = new LoginController();
        label_balanceOwner.setText(LoginController.u.getName()+" "+LoginController.u.getSurname() );
        label_balanceName.setText(LoginController.userCurrentBalance.getAccountName());
        setTable(expenditureTable, LoginController.userCurrentBalance.loadTrFromDB("EXP"));
        setTable(incomeTable, LoginController.userCurrentBalance.loadTrFromDB("INC"));

        text_amount.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onClick_ADDButton();
            }
        });

        text_info.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onClick_ADDButton();
            }
        });

        incomeTable.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                deleteBtn();
            }
        });

    }

    public Transaction getTransaction(){

        String transType = combo_Transaction.getSelectionModel().getSelectedItem();
        String transDate  =date_transaction.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String str = text_amount.getText();
        double transAmount = Double.parseDouble(str.replace(",","."));
        String transInfo = text_info.getText();
        Transaction tr = new Transaction(transType, transDate, transAmount,transInfo,LoginController.userCurrentBalance.getId());
        System.out.println(tr.toString());
        return tr;
    }

    @FXML
    public void onClick_ADDButton(){

        Transaction t = getTransaction();
        LoginController.userCurrentBalance.add(t);
        t.addToDB();

        setLabels();
//
//        incomeTable.getItems().clear();
//        expenditureTable.getItems().clear();
        setTable(expenditureTable, LoginController.userCurrentBalance.loadTrFromDB("EXP"));
        setTable(incomeTable, LoginController.userCurrentBalance.loadTrFromDB("INC"));
        text_amount.setText("");
        text_info.setText("");
        combo_Transaction.requestFocus();
    }

    @FXML
    public void deleteBtn(){
            DatabaseConnector dc = new DatabaseConnector();
            Transaction t  = incomeTable.getSelectionModel().getSelectedItem();

            dc.open();
            try {
                if(t!=null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + t.getId() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        dc.delete(t);
                        LoginController.userCurrentBalance.remove(t);
//                        incomeTable.getItems().clear();
//                        expenditureTable.getItems().clear();
                        setTable(incomeTable, LoginController.userCurrentBalance.loadTrFromDB("INC"));
                        setTable(expenditureTable, LoginController.userCurrentBalance.loadTrFromDB("EXP"));

                        setLabels();
                        Alert al = new Alert(Alert.AlertType.INFORMATION,"Transaction deleted", ButtonType.OK);
                        al.show();
                        System.out.println("balance deleted");
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dc.close();

    }

    public <T> void setTable(TableView<T> tw, List<T> l){
        tw.getItems().clear();
        ObservableList<T> Olist = FXCollections.observableList(l);
        tw.setItems(Olist);
    }

    public void setLabels(){
        String total = String.format("%.2f",getTotal(LoginController.userCurrentBalance.loadTrFromDB("INC"))-getTotal(LoginController.userCurrentBalance.loadTrFromDB("EXP")));
        String incomes = String.format("%.2f",getTotal(LoginController.userCurrentBalance.loadTrFromDB("INC")));
        String exps = String.format("%.2f",getTotal(LoginController.userCurrentBalance.loadTrFromDB("EXP")));

        label_totalAmount.setText(total);
        label_incomeList.setText(incomes);
        label_expList.setText(exps);
    }

    public double getTotal(List<Transaction> tl){
        double res=0;
        for(int i=0; i<tl.size();i++){
            res +=tl.get(i).getAmount();
        }
        return res;
    }

}
