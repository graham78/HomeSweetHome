/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.homesweethome.homesweethome.DatabaseFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class CustInfoController implements Initializable {

    @FXML
    private ComboBox Name;
    @FXML
    private TextField Address1;
    @FXML
    private TextField Address2;
    @FXML
    private TextField City;
    @FXML
    private TextField Phone;
    @FXML
    private TextField Email;
    @FXML
    private TextField Income;
    @FXML
    private ToggleGroup Lease;
    @FXML
    private RadioButton Short;
    @FXML
    private RadioButton Long;
    @FXML
    private RadioButton Own;
    @FXML
    private TextField Notes;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection connect =  DatabaseFactory.GetConnection();
       try
       {
       Statement state = connect.createStatement();
       String sql = "SELECT * FROM Cust";
       ResultSet rs = state.executeQuery(sql);
       while(rs.next())
       {
           Name.getItems().add(rs.getString("Name"));
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
    public void OnSaveButtonClick(ActionEvent event)
    {
        try
        {
             Connection connect = DatabaseFactory.GetConnection();
             PreparedStatement prep = connect.prepareStatement("INSERT INTO Cust VALUES(?,?,?,?,?,?,?,?,?)");
             prep.setString(1, Name.getEditor().getText());
             prep.setString(2, Address1.getText());
             prep.setString(3, Address2.getText());
             prep.setString(4, City.getText());
             prep.setString(5, Phone.getText());
             prep.setString(6, Email.getText());
             try
            {
                    int income = Integer.parseInt(Income.getText());
                    prep.setInt(7,income);
             }
             catch(Exception e)
             {
                    prep.setInt(7,0);
                }
             if(Lease.getSelectedToggle() == null)
             {
                prep.setString(8,"None");
            }
              else
             {
            RadioButton button = (RadioButton) Lease.getSelectedToggle();
            prep.setString(8,String.valueOf(button.getText()));
                }
             prep.setString(9, Notes.getText());
             prep.executeUpdate();
            // props.getEditor().getText();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    public void OnLoadButtonClick(ActionEvent Event)
    {
        Connection connect = DatabaseFactory.GetConnection();
        try
       {
       PreparedStatement state = connect.prepareStatement("SELECT * FROM Cust WHERE Name = ?");
       state.setString(1, Name.getEditor().getText());
       ResultSet set = state.executeQuery();
       while(set.next())
       {
           Address1.setText(set.getString("Add1"));
           Address2.setText(set.getString("Add2"));
           City.setText(set.getString("City"));
           Phone.setText(set.getString("Phone"));
           Income.setText(String.valueOf(set.getInt("Income")));
           String Looking = set.getString("Looking");
           if(!Looking.equals("None"))
           {
               if(Looking.equals("Short-term Lease"))
               {
                   Short.setSelected(true);
               }
               else if(Looking.equals("Ownership"))
               {
                    Own.setSelected(true);
               }
               else
               {
                   Long.setSelected(true);
               }
           }
           Notes.setText(set.getString("Notes"));
       }
       }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
