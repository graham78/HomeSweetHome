/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.homesweethome.homesweethome.DatabaseFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

/**
 * FXML Controller class
 *
 * @author joean
 */
public class PropertyPortfolioController implements Initializable {
    @FXML
    private ImageView propimage;
    @FXML 
    private ComboBox props; 
    @FXML
    private TextField PropOwn;
    @FXML 
    private TextField PropAdd;
    @FXML
    private TextField PropBed; 
    @FXML
    private TextField PropBath;
    @FXML
    private RadioButton PropPet;
    @FXML
    private Label ErrorBox;
    File isfile = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       Connection connect =  DatabaseFactory.GetConnection();
       try
       {
       Statement state = connect.createStatement();
       String sql = "SELECT * FROM House";
       ResultSet rs = state.executeQuery(sql);
       while(rs.next())
       {
           System.out.println(rs.getString("House_Name"));
           props.getItems().add(rs.getString("House_Name"));
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
    public void OnFileButtonClick(ActionEvent event)
    {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Property Picture");
            File chosen = fileChooser.showOpenDialog(stage);
            InputStream stream = new FileInputStream(chosen);
            Image image = new Image(stream);
            if(image != null)
            {
                propimage.setImage(image);
                propimage.setFitWidth(250);
                propimage.setFitHeight(150);
                propimage.setPreserveRatio(true);
                propimage.setSmooth(true);
                isfile = chosen;
               // propimage.setCache(true);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    
    }
    @FXML
    public void OnDeleteButtonClick(ActionEvent event)
    {
      propimage.setImage(null); 
      isfile = null;
    }
    @FXML 
    public void OnSaveButtonClick(ActionEvent event)
    {
        Connection connect = DatabaseFactory.GetConnection();
        try
        {
        PreparedStatement state = connect.prepareStatement("INSERT INTO HOUSE VALUES(?,?,?,?,?,?,?,?,?)");
        state.setString(1, PropAdd.getText());
        state.setInt(2, new Random().nextInt(1000));
        state.setInt(3, 2);
       // System.out.println(props.getEditor().getText());
        state.setString(4, props.getEditor().getText());
        try
        {
           int bed = Integer.parseInt(PropBed.getText());
           state.setInt(5,bed);
        }
        catch(Exception e)
        {
            state.setInt(5,0);
        }
        try
        {
        state.setInt(6, Integer.parseInt(PropBath.getText()));
        }
        catch(Exception e)
        {
            state.setInt(6, 0);
        }
        boolean truth = PropPet.isSelected();
        if(truth)
        {
            state.setInt(7,1);

        }
        else
        {
            state.setInt(7,0);
        }
        System.out.println(propimage);
        if(isfile != null)
        {
            state.setBytes(8, readFile(isfile));
        }
        else
        {
        state.setBytes(8, new byte[2]);
        }
        state.setString(9, PropOwn.getText());
        state.executeUpdate();
        ErrorBox.setOpacity(0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ErrorBox.setText("Error: Name is not unique");
            ErrorBox.setOpacity(100);
        }
    }
    @FXML
    public void OnLoadButtonClick(ActionEvent event) throws Exception
    {
       Connection connect =  DatabaseFactory.GetConnection();
       try
       {
       PreparedStatement state = connect.prepareStatement("SELECT * FROM House WHERE House_Name = ?");
       state.setString(1, props.getEditor().getText());
       ResultSet set = state.executeQuery();
       boolean blank = true;
       while (set.next()) 
       {
                blank = false;
                PropOwn.setEditable(false);
                PropAdd.setEditable(false);
                PropBed.setEditable(false);
                PropBath.setEditable(false);
                PropPet.setDisable(true);
                PropOwn.setText("landlord");
                PropAdd.setText(set.getString("Address"));
                PropBed.setText(String.valueOf(set.getInt("Bedrooms")));
                PropBath.setText(String.valueOf(set.getInt("Bathroom")));
                int bool = set.getInt("Pet_Friendly");
                if(bool == 1)
                {
                    PropPet.setSelected(true);
                }
                else
                {
                    PropPet.setSelected(false);
                }
                PropOwn.setText(set.getString("Owner_Name"));   
                //InputStream input
                byte[] bytearray = set.getBytes("Image");
                ByteArrayInputStream stream = new ByteArrayInputStream(bytearray);
                Image image = new Image(stream);
                propimage.setImage(image);
                propimage.setFitWidth(250);
                propimage.setFitHeight(150);
                propimage.setPreserveRatio(true);
                propimage.setSmooth(true);
               // propimage.setCache(true);
                //}
       }
       if(blank)
       {
                PropOwn.setEditable(true);
                PropAdd.setEditable(true);
                PropBed.setEditable(true);
                PropBath.setEditable(true);
                PropPet.setDisable(false);
                PropOwn.setText("");
                PropAdd.setText("");
                PropBed.setText("");
                PropBath.setText("");
                PropPet.setSelected(false);
                propimage = null; 
       }
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
    }
     private static byte[] readFile(File file) {
        ByteArrayOutputStream bos = null;
        try {
            //File f = new File(file);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

}
