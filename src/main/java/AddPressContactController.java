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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class AddPressContactController implements Initializable {
    @FXML
    private ComboBox Company;
    @FXML
    private TextField Add1;
    @FXML
    private TextField Add2;
    @FXML
    private TextField City;
    @FXML
    private TextField Phone;
    @FXML
    private TextField Email;
    @FXML
    private TextField Fax;
    @FXML
    private TextField Rep;
    @FXML
    private TextField Notes;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                Connection connect = DatabaseFactory.GetConnection();
        try
        {
        PreparedStatement state = connect.prepareStatement("SELECT * FROM Press");
        ResultSet set = state.executeQuery();
        while(set.next())
        {
            Company.getItems().add(set.getString("Comp"));
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
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
        PreparedStatement state = connect.prepareStatement("INSERT OR REPLACE INTO Press VALUES(?,?,?,?,?,?,?,?,?)");
        state.setString(1,Company.getEditor().getText());
        state.setString(2, Add1.getText());
        state.setString(3, Add2.getText());
        state.setString(4, City.getText());
        state.setString(5, Phone.getText());
        state.setString(6, Email.getText());
        state.setString(7, Fax.getText());
        state.setString(8, Rep.getText());
        state.setString(9, Notes.getText());
        state.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void OnLoadButtonClick(ActionEvent event)
    {
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
        PreparedStatement state = connect.prepareStatement("SELECT * FROM Press WHERE Comp = ?");
        state.setString(1, Company.getEditor().getText());
        ResultSet set = state.executeQuery();
        while(set.next())
        {
            Add1.setText(set.getString("Address1"));
            Add2.setText(set.getString("Address2"));
            City.setText(set.getString("City"));
            Phone.setText(set.getString("Phone"));
            Email.setText(set.getString("Email"));
            Fax.setText(set.getString("Fax"));
            Rep.setText(set.getString("Rep"));
            Notes.setText(set.getString("Notes"));
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
