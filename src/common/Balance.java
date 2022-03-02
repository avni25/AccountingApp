package common;

import com.data.DatabaseConnector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Balance{

    private int id;
    private String accountName;
    private int user_id;
    private List<Transaction> tlist = null;


    public Balance() {
        tlist = new ArrayList<>();
    }

    public Balance(int id, String accountName, int user_id)
    {
        this.id = id;
        this.accountName = accountName;
        this.user_id=user_id;
        tlist = new ArrayList<>();
    }

    public Balance(String accountName, int user_id)
    {

        this.accountName = accountName;
        this.user_id=user_id;
        tlist = new ArrayList<>();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<Transaction> getTlist() {
        return tlist;
    }

    public void setTlist(List<Transaction> tlist) {
        this.tlist = tlist;
    }

    public void add(Transaction t){
       tlist.add(t);
    }
    public void remove(Transaction t){
        tlist.remove(t);
    }

    public void addToDB(){
        DatabaseConnector dc = new DatabaseConnector();
        dc.open();
        try {
            dc.addToDatabase(this);
            System.out.println("Balance "+this.getAccountName()+" added to database. balance id: "+this.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        dc.close();
    }

    public List<Transaction> loadTrFromDB(String type){
        List<Transaction> tl = new ArrayList<>();
        List<Transaction> t2 = new ArrayList<>();
        DatabaseConnector ud = new DatabaseConnector();
        ud.open();
        try{
            tl= ud.loadTransactions(this.getId());
            //System.out.println("Transaction list loaded...");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        ud.close();

        for(int i=0;i<tl.size();i++){
            if(tl.get(i).getTransactionType().equals(type)){
                t2.add(tl.get(i));
            }
        }
        return t2;
    }

    @Override
    public String toString() {
        return "id: " + id +" "+ accountName ;

    }
}
