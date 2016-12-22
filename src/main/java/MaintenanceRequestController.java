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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class MaintenanceRequestController implements Initializable {
    @FXML
    private ComboBox Prop;
    @FXML
    private DatePicker Date;
    @FXML
    private ComboBox Time;
    @FXML
    private TextField Req;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
        PreparedStatement prep = connect.prepareStatement("SELECT * FROM Req");
        ResultSet rs = prep.executeQuery();
        while(rs.next())
        {
            Prop.getItems().add(rs.getString("Name"));
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
        try
        {
        Connection connect = DatabaseFactory.GetConnection();
        PreparedStatement prep = connect.prepareStatement("INSERT OR REPLACE INTO Req VALUES(?,?,?,?)");
        prep.setString(1, Prop.getEditor().getText());
        prep.setObject(2, java.sql.Date.valueOf(Date.getValue()));
        prep.setString(3, Time.getEditor().getText());
        prep.setString(4, Req.getText());
        prep.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
      /*  Button button = (Button) event.getSource();
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
        PreparedStatement prep = connect.prepareStatement("SELECT * FROM Req WHERE Name = ?");
        prep.setString(1,Prop.getEditor().getText());
        ResultSet rs = prep.executeQuery();
        while(rs.next())
        { 
            if(rs.getDate("Mait_Date") != null)
            {
            Date.setValue(rs.getDate("Mait_Date").toLocalDate());
            }
            Time.setValue(rs.getString("TimeRange"));
            Req.setText(rs.getString("Reqs"));
            
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
