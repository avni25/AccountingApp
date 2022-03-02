package com.data;

import common.Balance;
import common.Transaction;
import common.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    public static final String DB_NAME = "Accounter.db";
    public static final String fileURL="jdbc:sqlite:/JavaProjects/AccountingApp/"+DB_NAME;
    public static final String TABLE_BALANCE = "Balance";
    public static final String TABLE_USER = "User";
    public static final String TABLE_TRANSACTION = "Transactions";

    public static final String COLUMN_BALANCE_ID = "id";
    public static final String COLUMN_BALANCE_ACCOUNTNAME = "accountName";
    public static final String COLUMN_BALANCE_USER_ID = "user_id";
    public static final String DELETE_BALANCE = "DELETE FROM "+TABLE_BALANCE+" WHERE "+COLUMN_BALANCE_ID+" = ";
    public static final String CREATE_TABLE_BALANCE ="CREATE TABLE IF NOT EXISTS "+TABLE_BALANCE+"('"+COLUMN_BALANCE_ID+
                                                                "' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, '"+
                                                       COLUMN_BALANCE_ACCOUNTNAME+"' TEXT NOT NULL, '"+
                                                       COLUMN_BALANCE_USER_ID+"' INTEGER NOT NULL)";

    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_SURNAME = "surname";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String DELETE_USER = "DELETE FROM "+TABLE_USER+" WHERE "+COLUMN_USER_ID+" = ";
    public static final String CREATE_TABLE_USER  = "CREATE TABLE IF NOT EXISTS "+TABLE_USER+"('"+COLUMN_USER_ID+"' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, '"+COLUMN_USER_NAME+
                                                    "' TEXT NOT NULL, '"+COLUMN_USER_SURNAME+"' TEXT NOT NULL, '"+COLUMN_USER_EMAIL+
                                                    "' TEXT NOT NULL, '"+COLUMN_USER_PASSWORD+"' TEXT NOT NULL)";

    public static final String COLUMN_TRANSACTIONS_ID = "id";
    public static final String COLUMN_TRANSACTIONS_TYPE = "TrType";
    public static final String COLUMN_TRANSACTIONS_DATE= "TrDate";
    public static final String COLUMN_TRANSACTIONS_AMOUNT = "Amount";
    public static final String COLUMN_TRANSACTIONS_INFO = "info";
    public static final String COLUMN_TRANSACTIONS_BALANCE_ID = "balance_id";
    public static final String DELETE_TRANSACTION = "DELETE FROM "+TABLE_TRANSACTION+" WHERE "+COLUMN_TRANSACTIONS_ID+" =";
    public static final String CREATE_TABLE_TRANSACTIONS ="CREATE TABLE IF NOT EXISTS "+TABLE_TRANSACTION+" ('"+COLUMN_TRANSACTIONS_ID+
                                                            "' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                                                            "'"+COLUMN_TRANSACTIONS_TYPE+"' TEXT NOT NULL, '"+COLUMN_TRANSACTIONS_DATE+"' TEXT, '"+COLUMN_TRANSACTIONS_AMOUNT+
                                                        "' REAL, '"+COLUMN_TRANSACTIONS_INFO+"' TEXT, '"+COLUMN_TRANSACTIONS_BALANCE_ID+"' INTEGER NOT NULL)";

    private Connection conn;

    public boolean open(){
        try{
            conn =  DriverManager.getConnection(fileURL);
            return true;
        }catch(SQLException e){
            System.out.println("couldn't connect "+ e.getMessage());
            return false;
        }
    }

    public void close(){
        try{
            if(conn!=null) conn.close();
        }catch(SQLException e){
            System.out.println("cant close.. "+e.getMessage());
        }
    }

    public void addToDatabase(Transaction t)throws SQLException{
        Statement st = conn.createStatement();

        st.execute(CREATE_TABLE_TRANSACTIONS);
        st.execute("INSERT INTO "+TABLE_TRANSACTION+"("+COLUMN_TRANSACTIONS_TYPE+", "+COLUMN_TRANSACTIONS_DATE+", "+COLUMN_TRANSACTIONS_AMOUNT+", "+
                COLUMN_TRANSACTIONS_INFO+", "+COLUMN_TRANSACTIONS_BALANCE_ID+") VALUES('"+
                t.getTransactionType()+"','"+t.getDate()+"',"+t.getAmount()+",'"+t.getInfo()+"',"+t.getBalance_id()+")");
        st.close();
    }

    public void addToDatabase(User u) throws SQLException{
        Statement st = conn.createStatement();
        st.execute(CREATE_TABLE_USER);
        st.execute("INSERT INTO "+TABLE_USER+"("+COLUMN_USER_NAME+", "+COLUMN_USER_SURNAME+", "+COLUMN_USER_EMAIL+", "+COLUMN_USER_PASSWORD+
                ") VALUES('"+u.getName()+"', '" + u.getSurname() + "', '" + u.getEmail()+"', '"+u.getPassword()+"')");

        st.close();
    }

    public void addToDatabase(Balance b) throws SQLException{
        Statement st = conn.createStatement();
        st.execute(CREATE_TABLE_BALANCE);
        st.execute("INSERT INTO "+TABLE_BALANCE+" ("+COLUMN_BALANCE_ACCOUNTNAME+", "+COLUMN_BALANCE_USER_ID+") VALUES('"+b.getAccountName()+"', " + b.getUser_id()+")");

        st.close();
    }



    public List<User> loadUsers()throws SQLException{
        try(Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM "+TABLE_USER)){
            List<User> uList = new ArrayList<>();
            while(r.next()){
                User u = new User();
                u.setId(r.getInt(COLUMN_USER_ID));
                u.setName(r.getString(COLUMN_USER_NAME));
                u.setSurname(r.getString(COLUMN_USER_SURNAME));
                u.setEmail(r.getString(COLUMN_USER_EMAIL));
                u.setPassword(r.getString(COLUMN_USER_PASSWORD));

                uList.add(u);
            }
            return uList;
        }catch(SQLException e){
            System.out.println("cant load list.." +e.getMessage());
            return null;
        }
    }

    public List<Balance> loadBalances() throws SQLException{
        try(Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM "+TABLE_BALANCE)){
            List<Balance> bList = new ArrayList<>();
            while(r.next()){
                Balance b = new Balance();
                b.setId(r.getInt(COLUMN_BALANCE_ID));
                b.setAccountName(r.getString(COLUMN_BALANCE_ACCOUNTNAME));
                b.setUser_id(r.getInt(COLUMN_BALANCE_USER_ID));

                bList.add(b);
            }
            return bList;
        }catch(SQLException e){
            System.out.println("cant load Balance list.." +e.getMessage());
            return null;
        }
    }

    public List<Transaction> loadTransactions(int balance_id) throws SQLException{
        try(Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM "+TABLE_TRANSACTION+" WHERE "+COLUMN_TRANSACTIONS_BALANCE_ID+"="+balance_id)){
            List<Transaction> tList = new ArrayList<>();
            while(r.next()){
                Transaction t = new Transaction();
                t.setId(r.getInt(COLUMN_TRANSACTIONS_ID));
                t.setTransactionType(r.getString(COLUMN_TRANSACTIONS_TYPE));
                t.setDate(r.getString(COLUMN_TRANSACTIONS_DATE));
                t.setAmount(r.getDouble(COLUMN_TRANSACTIONS_AMOUNT));
                t.setInfo(r.getString(COLUMN_TRANSACTIONS_INFO));
                t.setBalance_id(r.getInt(COLUMN_TRANSACTIONS_BALANCE_ID));

                tList.add(t);
            }
            return tList;
        }catch(SQLException e){
            System.out.println("cant load Balance list.." +e.getMessage());
            return null;
        }
    }

    public void update(Balance b) throws SQLException{
        Statement st = conn.createStatement();
        st.execute("UPDATE "+TABLE_BALANCE+" SET "+COLUMN_BALANCE_ACCOUNTNAME+" = '"+b.getAccountName()+"' WHERE "+COLUMN_BALANCE_ID+" = "+b.getId());
        st.close();
    }
    public void update(User u) throws SQLException{
        Statement st = conn.createStatement();

        st.close();
    }
    public void update(Transaction t) throws SQLException{
        Statement st = conn.createStatement();

        st.close();
    }
    public void delete(Balance b) throws SQLException{
        Statement st = conn.createStatement();
        st.execute(DELETE_BALANCE + b.getId());
        st.close();
    }

    public void delete(Transaction t) throws SQLException{
        Statement st = conn.createStatement();
        st.execute(DELETE_TRANSACTION + t.getId());
        st.close();
    }
}
