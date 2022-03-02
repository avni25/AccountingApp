package common;

import com.data.DatabaseConnector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<Balance> blist = null;

    public User(){
        blist = new ArrayList<>();
    }

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        blist = new ArrayList<>();
    }

    public User(int id, String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.email = email;
        this.password = password;
        blist = new ArrayList<>();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Balance> getBlist() {
        return blist;
    }

    public void setBlist(List<Balance> blist) {
        this.blist = blist;
    }

    public void add(Balance b){
        blist.add(b);
    }

    public void addToDB(){
        DatabaseConnector dc = new DatabaseConnector();
        dc.open();
        try {
            dc.addToDatabase(this);
            System.out.println("User "+this.getName()+" added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        dc.close();
    }

    public List<Balance> getBalanceList(){
        List<Balance> bl = new ArrayList<>();
        for(int i=0;i<LoginController.balanceList.size();i++){
            if(this.getId() == LoginController.balanceList.get(i).getUser_id()){
                bl.add(LoginController.balanceList.get(i));
            }
        }
        return bl;
    }

    public List<Balance> getBalancesFromDB(){
        List<Balance> bl = new ArrayList<>();
        List<Balance> bl2 = new ArrayList<>();
        DatabaseConnector dc = new DatabaseConnector();
        dc.open();
        try {
           bl = dc.loadBalances();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        dc.close();
        for(int i=0;i<bl.size();i++){
            if(this.getId() == bl.get(i).getUser_id()){
                bl2.add(bl.get(i));
            }
        }
        return bl2;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
