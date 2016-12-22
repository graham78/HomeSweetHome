/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.homesweethome.homesweethome;
import java.io.File;
import java.sql.*;
/**
 *
 * @author joean
 */
public class DatabaseFactory {
    private static String GetDriver()
    {
        return "jdbc:sqlite::memory:";
    }
    public static Connection GetConnection()
    {
       try
       {
           Connection connection = DriverManager.getConnection("jdbc:sqlite:Homesweethome.db");
           connection.setAutoCommit(true);
           return connection;
       }
       catch(SQLException e)
       {
           e.printStackTrace();
           
       }
       return null;
    }
    public static void SetupDatabase()
    {
        try
        {
        DriverManager.registerDriver(new org.sqlite.JDBC());
        File f = new File("Homesweethome.db");
        f.createNewFile();
        Connection databaseconnection = GetConnection();
        Statement querier = databaseconnection.createStatement();
        String sql  = "CREATE TABLE IF NOT EXISTS Person(Name VARCHAR(200), Person_ID NUMBERIC(10) PRIMARY KEY,"
                + "SSN VARCHAR(10),Phone_Number VARCHAR(10), Email VARCHAR(30))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Login(Username text PRIMARY KEY,"
                + "Password text NOT NULL, Accounttype text NOT NULL,Person_ID NUMBERIC(10), FOREIGN KEY "
                + "(Person_ID) REFERENCES Person(Person_ID))";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO Person VALUES ('admin', 1, '1829292', '493392', 'test@test.edu')";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO Person VALUES('Tom', 4, '838383838', '39393', 'user@user.com')";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO Login VALUES ('John', '123456', 'user', 4)";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO Login VALUES ('admin','123456','admin', 1)";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO PERSON Values('Vlad', 2, '299333', '382822', 'test@landlord.com')";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO Login VALUES ('landlord', '123456', 'landlord', 2)";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO PERSON VALUES('Kathy', 2, '3838833', '372722', 'test@hsh.org')";
        querier.execute(sql);
        sql = "INSERT OR IGNORE INTO Login VALUES ('rep', '123456', 'rep',  3)";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Maintenance(Type_Code CHAR(2) PRIMARY KEY, Type_Description"
                + " VARCHAR(255))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Utility_Bill_Type(Type_Code CHAR(2) PRIMARY KEY, Type_Description "
                + "VARCHAR(255))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS House(Address VARCHAR(255), House_ID NUMERIC(10) PRIMARY KEY, Landlord_ID NUMERIC(10), "
                + " House_Name VARCHAR(255) UNIQUE, Bedrooms NUMERIC(10), Bathroom NUMERIC(10), Pet_Friendly BOOLEAN,"
                + "Image BLOB, Owner_Name VARCHAR(255),"
                + "FOREIGN KEY"
                + "(Landlord_ID) REFERENCES Person(Person_ID))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Appointment(Start_Time DATETIME , Duration Time, "
                + "House_ID NUMBERIC(10), Client_ID NUMBERIC(10), Representative_ID NUMBERIC(10),"
                + "PRIMARY KEY(Start_Time, House_ID, Representative_ID), FOREIGN KEY(House_ID) "
                + "REFERENCES House(House_ID), FOREIGN KEY(Client_ID) REFERENCES Person(Person_ID),"
                + "FOREIGN KEY(Representative_ID) REFERENCES Person(Person_ID))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Rental_Agreement (Tennent_ID NUMBERIC(10), Rent DECIMAL(10,2), Contract_ID NUMBERIC(10) PRIMARY KEY,"
                + "Deposit DECIMAL(10,2), Accepted BOOLEAN, House_ID NUMBERIC(10), FOREIGN KEY(House_ID)"
                + "REFERENCES House(House_ID), FOREIGN KEY(Tennent_ID) REFERENCES Person(Person_ID))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Utility_Bill( Amount DECIMAL(10,2), Bill_ID NUMBERIC(1000) PRIMARY KEY,"
                + "Date_Due DATE, DatePaidInFull DATE, Amount_Paid DECIMAL(10,2), Contract_ID NUMBERIC(10),"
                + "Type_Code CHAR(2), FOREIGN KEY(Contract_ID) REFERENCES Rental_Agreement(Contract_ID), "
                + "FOREIGN KEY(Type_Code) REFERENCES Utility_Bill_Type(Type_Code))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Maintenance_Request(Request_ID NUMBERIC(10) PRIMARY KEY, Description VARCHAR(255),"
                + "Time_Submitted DATETIME, Time_Addressed DATETIME, Type_Code CHAR(2), Contract_ID NUMBERIC(10),"
                + "FOREIGN KEY(Type_Code) REFERENCES Maintenance(Type_Code), FOREIGN KEY(Contract_ID) REFERENCES Rental_Agreement(Contract_ID))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Bill(Description VARCHAR(255) PRIMARY KEY, Amount NUMERIC(10), Method VARCHAR(255), "
                + " Name VARCHAR(255), Address VARCHAR(255), City VARCHAR(255), AutoPay VARCHAR(255)) ";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Cust(Name VARCHAR(255) PRIMARY KEY, Add1 VARCHAR(255), Add2 VARCHAR(255), City VARCHAR(255),"
                + "Phone VARCHAR(255), Email VARCHAR(255), Income NUMERIC(10), Looking VARCHAR(255), Notes VARCHAR(255))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Req(Name VARCHAR(255) PRIMARY KEY, Mait_Date DATE, TimeRange VARCHAR(255), Reqs VARCHAR(255))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Message(Title VARCHAR(255) PRIMARY KEY, Body VARCHAR(255))";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Contract(Tenant VARCHAR(255) PRIMARY KEY, LandLord VARCHAR(255), Agree BOOLEAN, Name VARCHAR(255), Date DATE)";
        querier.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS Press(Comp VARCHAR(255) PRIMARY KEY, Address1 VARCHAR(255), Address2 VARCHAR(255), City VARCHAR(255), Phone VARCHAR(255), Email VARCHAR(255), Fax VARCHAR(255), Rep VARCHAR(255), Notes VARCHAR(255))";
        querier.execute(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
