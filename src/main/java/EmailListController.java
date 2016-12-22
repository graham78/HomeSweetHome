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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class EmailListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML 
    private ComboBox Title;
    @FXML
    private TextArea Message;
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       Connection connect =  DatabaseFactory.GetConnection();
       try
       {
       Statement state = connect.createStatement();
       String sql = "SELECT * FROM Message";
       ResultSet rs = state.executeQuery(sql);
       while(rs.next())
       {
           Title.getItems().add(rs.getString("Title"));
       }
       
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
    }    

     @FXML
    public void OnSubmitButtonClick(ActionEvent event)
    {
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
        PreparedStatement prep = connect.prepareStatement("INSERT OR REPLACE INTO Message VALUES(?,?)");
        prep.setString(1, Title.getEditor().getText());
        prep.setString(2, Message.getText());
        prep.executeUpdate();
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
        PreparedStatement prep = connect.prepareStatement("SELECT * FROM Message WHERE Title = ?");
        prep.setString(1, Title.getEditor().getText());
        ResultSet rs = prep.executeQuery();
        while(rs.next())
        {
            Message.setText(rs.getString("Body"));
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
}
