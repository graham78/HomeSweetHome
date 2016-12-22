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
public class PayBillController implements Initializable {

    @FXML
    private ComboBox Desc;
    @FXML
    private TextField Amount;
    @FXML
    private ToggleGroup payment;
    @FXML
    private TextField Name;
    @FXML
    private TextField Add;
    @FXML
    private TextField City;
    @FXML
    private ToggleGroup auto;   
    @FXML 
    private RadioButton eCheck;
    @FXML
    private RadioButton Credit;
    @FXML
    private RadioButton Yes;
    @FXML
    private RadioButton No;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection connect =  DatabaseFactory.GetConnection();
       try
       {
       Statement state = connect.createStatement();
       String sql = "SELECT * FROM Bill";
       ResultSet rs = state.executeQuery(sql);
       while(rs.next())
       {
           Desc.getItems().add(rs.getString("Description"));
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
        PreparedStatement prep = connect.prepareStatement("INSERT INTO Bill VALUES(?,?,?,?,?,?,?)");
        String text = Desc.getEditor().getText();
        prep.setString(1, text);
        try 
        {
           int bed = Integer.parseInt(Amount.getText());
           prep.setInt(2,bed);
        }
        catch(Exception e)
        {
            prep.setInt(2,0);
        }
        if(payment.getSelectedToggle() == null)
            
        {
            prep.setString(3,"None");
        }
        else
        {
            RadioButton button = (RadioButton) payment.getSelectedToggle();
            prep.setString(3,String.valueOf(button.getText()));
        }
        prep.setString(4, Name.getText());
        prep.setString(5, Add.getText());
        prep.setString(6, City.getText());
        if(auto.getSelectedToggle() == null)
            
        {
            prep.setString(7,"None");
        }
        else
        {
            RadioButton button = (RadioButton) auto.getSelectedToggle();
            prep.setString(7,String.valueOf(button.getText()));
        }
        prep.executeUpdate();   
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       /* Button button = (Button) event.getSource();
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
        }*/
    }
    @FXML 
    public void OnLoadButtonClick(ActionEvent event)
    {
        try
        {
        Connection connect = DatabaseFactory.GetConnection();
        PreparedStatement prep = connect.prepareStatement("SELECT * FROM Bill WHERE Description = ?");
        prep.setString(1,Desc.getEditor().getText());
        ResultSet set = prep.executeQuery();
        boolean blank = true;
       while (set.next()) 
       {
           Amount.setText(String.valueOf(set.getInt("Amount")));
           String payment = set.getString("Method");
           if(!payment.equals("None"))
           {
               if(payment.equals("eCheck"))
               {
                   eCheck.setSelected(true);
               }
               else
               {
                    Credit.setSelected(true);
               }
           }
           Name.setText(set.getString("Name"));
           Add.setText(set.getString("Address"));
           City.setText(set.getString("City"));
           String autoo = set.getString("AutoPay");
           if(!autoo.equals("None"))
           {
               if(payment.equals("Yes"))
               {
                   Yes.setSelected(true);
               }
               else
               {
                    No.setSelected(true);
               }
           }
       }
       }
       catch(Exception e)
       {
            e.printStackTrace();
       }
    }
    
}
