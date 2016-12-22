/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.homesweethome.homesweethome.DatabaseFactory;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class TestsceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
    }    
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label invalidlogin;
    @FXML
    private void SelectButton(ActionEvent event) {
       Button button = (Button) event.getSource();
       //button.setText("test"); 
       Connection connection = DatabaseFactory.GetConnection();
       try
       {
       Statement statement = connection.createStatement();
       String sql = "SELECT username,password, Accounttype from Login";
       ResultSet rs = statement.executeQuery(sql);
       HashMap<String,String> users = new HashMap<>();
       while(rs.next())
       {
           String dbusername = rs.getString("Username");
           String dbpassword = rs.getString("Password");
           String dbusertype = rs.getString("Accounttype");
           users.put(dbusername, dbpassword);
       }
       String inputusername = username.getText();
       String inputpassword = password.getText();
      if(users.keySet().contains(inputusername) && users.get(inputusername).equals(inputpassword))
       {
        Stage stage = (Stage) button.getScene().getWindow();
        try
        {
            //check against user type.  dbusertype
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
      else
      {
          invalidlogin.setOpacity(100);
          invalidlogin.setText("Invalid username or password");
      }
       }
       catch(SQLException e)
       {
           e.printStackTrace();
       }
    }
    
}
