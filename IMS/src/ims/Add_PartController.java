package ims;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Skye McClure
 */
public class Add_PartController {

    // initialize FXML elements
    
    @FXML
        private ToggleGroup source;
    @FXML
        private RadioButton inHouseBtn;
    @FXML
        private RadioButton outSourcedBtn;
    @FXML
        private Label id_or_name;
    @FXML
        private TextField id_field;
    @FXML
        private TextField name_field;
    @FXML
        private TextField inv_field;
    @FXML
        private TextField price_field;
    @FXML
        private TextField max_field;
    @FXML
        private TextField min_field;
    @FXML
        private TextField mid_cname_field;
    
    // initialize a string to be used for error messages
    private String error_message = "";

    // initialize a random number variable
    private final Random random_Number = new Random();
    // initializes a random integer 1 - 50 to be used for creating a unique id for part
    private int uniqueID = 1 + random_Number.nextInt(50);
    
    // initialize class
    @FXML
    public void initialize() {

        // uses isUniqueID method to continuously look for ID that hasn't been used
        while(!isUniqueID()){
            uniqueID = 1 + random_Number.nextInt(50);
        }
        
        // sets the ID field to uniqueID
        id_field.setText(Integer.toString(uniqueID));

        // Radio Button usage - select inHouse of outSourced and changes the appropriate label and clears the field
        source.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (source.getSelectedToggle() == inHouseBtn) {
                id_or_name.setText("Machine ID");
                mid_cname_field.setText("");
                mid_cname_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        mid_cname_field.setText(oldValue);
                    }
                }
        });
            } else if (source.getSelectedToggle() == outSourcedBtn){
                id_or_name.setText("Comany Name");
                mid_cname_field.setText("");
            }
        });

        // Set fields so user can only input appropriate characters or numbers
        // only whole numbers
        id_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        id_field.setText(oldValue);
                    }
                }
        });
        // only numbers and up to 2 decimal places
        price_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}([\\.]\\d{0,2})?")) {
                        price_field.setText(oldValue);
                    }
                }
        });
        // only whole numbers
        inv_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        inv_field.setText(oldValue);
                    }
                }
        });
        // only whole numbers
        max_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        max_field.setText(oldValue);
                    }
                }
        });
        // only whole numbers
        min_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        min_field.setText(oldValue);
                    }
                }
        });
        
    }
    
    // boolean Method to check if uniqueID matches any ids found in all of parts list
    private boolean isUniqueID() {
        if (!Inventory.getParts().stream().noneMatch((p) -> (uniqueID == p.getPartID()))) {
            return false;
        }
        return true;
    }
    
    // boolean checks if user can save data with appropriate error messages
    private boolean canSave() {
        error_message = "A value for each field is required.";
        if (id_field.getText().trim().isEmpty()) return false;
        if (price_field.getText().trim().isEmpty()) return false;
        if (inv_field.getText().trim().isEmpty()) return false;
        if (max_field.getText().trim().isEmpty()) return false;
        if (min_field.getText().trim().isEmpty()) return false;
        if (mid_cname_field.getText().trim().isEmpty()) return false;
        if (name_field.getText().trim().isEmpty()) return false;
        
        if (Integer.parseInt(inv_field.getText()) > Integer.parseInt(max_field.getText()))
            {
                error_message = "Inventory level entered greater than maximum allowed entered.  Please change values.";
                return false;
            }
        if (Integer.parseInt(inv_field.getText()) < Integer.parseInt(min_field.getText()))
            {
                error_message = "Inventory level entered less than minimum allowed entered.  Please change values.";
                return false;
            }
        if (Integer.parseInt(max_field.getText()) < Integer.parseInt(min_field.getText()))
            {
                error_message = "Maximum allowed entered less than minimum allowed entered.  Please change values.";
                return false;
            }
        if (Integer.parseInt(min_field.getText()) > Integer.parseInt(max_field.getText()))
            {
                error_message = "Minimum level entered greater than maximum allowed entered.  Please change values.";
                return false;
            }
        
        return true;

    }
    
    // Button event to return to home page
    @FXML
    private void show_home_page(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit? All input data will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Parent modify_product_parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Scene modify_product_scene = new Scene(modify_product_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(modify_product_scene);
            app_stage.show();
        }
        
    }
    
    // Button event to save part then return to home page
    @FXML
    private void save_part(ActionEvent event) throws IOException {
        
        if (!canSave()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Procesing Error");
            alert.setHeaderText(null);
            alert.setContentText(error_message);

            alert.showAndWait();
        } else {
            int id = Integer.parseInt(id_field.getText());
            double price = Double.parseDouble(price_field.getText());
            int inv = Integer.parseInt(inv_field.getText());
            int min = Integer.parseInt(min_field.getText());
            int max = Integer.parseInt(max_field.getText());
            String mid_cname = mid_cname_field.getText();
            String name = name_field.getText();

            if (source.getSelectedToggle() == inHouseBtn) {
                Inventory.addPart(new inHouse(
                    Integer.parseInt(mid_cname),
                    id,
                    name,
                    price,
                    inv,
                    min,
                    max
            ));
            } else if (source.getSelectedToggle() == outSourcedBtn){
                Inventory.addPart(new outSourced(
                    mid_cname,
                    id,
                    name,
                    price,
                    inv,
                    min,
                    max
                ));
                
            }
            Parent home_page_parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
        
        
        
    }
    
    
}