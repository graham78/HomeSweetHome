/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.homesweethome.homesweethome.DatabaseFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class ContractController implements Initializable {
    @FXML
    private ComboBox Tenant;
    @FXML
    private TextField LandLord;
    @FXML 
    private RadioButton Terms;
    @FXML
    private TextField Name;
    @FXML
    private DatePicker Datee;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
        Statement state = connect.createStatement();
        String sql = "SELECT * FROM Contract";
        ResultSet set = state.executeQuery(sql);
        while(set.next())
        {
            Tenant.getItems().add(set.getString("Tenant"));
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }    
    @FXML
    public void OnCancelButtonClick(ActionEvent event)
    {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        try
        {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Home Sweet Home");
        stage.setScene(scene);
        stage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    public void OnSubmitButtonClick(ActionEvent event)
    {
        //Contract
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
           PreparedStatement state = connect.prepareStatement("INSERT OR REPLACE INTO Contract VALUES(?,?,?,?,?)");
           state.setString(1,Tenant.getEditor().getText());
           state.setString(2,LandLord.getText());
           state.setBoolean(3,Terms.isSelected());
           state.setString(4, Name.getText());
           state.setDate(5, Date.valueOf(Datee.getValue()));
           state.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    public void OnLoadButtonClick(ActionEvent event)
    {
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
           PreparedStatement state = connect.prepareStatement("SELECT * FROM Contract WHERE Tenant = ?");
           state.setString(1,Tenant.getEditor().getText());
           ResultSet set = state.executeQuery();
           while(set.next())
           {
               LandLord.setText(set.getString("LandLord"));
               if(set.getInt("Agree") == 1)
               {
                   Terms.setSelected(true);
               }
               else
               {
                   Terms.setSelected(false);
               }
               Name.setText(set.getString("Name"));
              // java.util.Date date = set.getDate("Date");
              // Instant instant = date.toInstant();
               //ZoneId defaultZoneId = ZoneId.systemDefault();
               //LocalDate localdate = instant.atZone(defaultZoneId).toLocalDate();
               Datee.setValue(set.getDate("Date").toLocalDate());
           }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
