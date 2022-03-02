package common;

import com.data.DatabaseConnector;

import java.sql.SQLException;

public class Transaction {

    private int id;
    private String transactionType;
    private String date;
    private double amount;
    private String info;
    private int balance_id;

    public Transaction(){

    }
    public Transaction(String transactionType, String date, double amount){
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
    }
    public Transaction(int id, String transactionType, String date, double amount, String info, int balance_id) {
        this.id = id;
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        this.info = info;
        this.balance_id = balance_id;
    }

    public Transaction(String transactionType, String date, double amount, String info, int balance_id) {
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        this.info = info;
        this.balance_id = balance_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance_id() {
        return balance_id;
    }

    public void setBalance_id(int balance_id) {
        this.balance_id = balance_id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void addToDB(){
        DatabaseConnector dc = new DatabaseConnector();
        dc.open();
        try {
            dc.addToDatabase(this);
            System.out.println("Transaction added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        dc.close();
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType='" + transactionType + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", info='" + info + '\'' +
                '}';
    }
}
