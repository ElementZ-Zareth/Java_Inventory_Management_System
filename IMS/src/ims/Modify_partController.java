package ims;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
public class Modify_partController {

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
 
    private String error_message = "";
    
    //initialize variables to be used to transfer from initialization
    private final int id_T;
    private final String name_T;
    private final int inv_T;
    private final double price_T;
    private final int max_T;
    private final int min_T;
    private final String mid_cn_T;
    
    private final boolean outSourcedSelected;
    
    private static final DecimalFormat priceFormat = new DecimalFormat("0.00");
    
    // Controller to initialize inHouse part
    public Modify_partController(int id, String name, int inv, double price, int max, int min, int mid){
        id_T = id;
        name_T = name;
        inv_T = inv;
        price_T = price;
        max_T = max;
        min_T = min;
        mid_cn_T = Integer.toString(mid);
        outSourcedSelected = false;
    }
    
    // Controller to initialize outSourced part
    public Modify_partController(int id, String name, int inv, double price, int max, int min, String cname){
        id_T = id;
        name_T = name;
        inv_T = inv;
        price_T = price;
        max_T = max;
        min_T = min;
        mid_cn_T = cname;
        outSourcedSelected = true;
    }
    
    // initialize class
    @FXML    
    private void initialize() {
        
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

        id_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        id_field.setText(oldValue);
                    }
                }
        });
        price_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}([\\.]\\d{0,2})?")) {
                        price_field.setText(oldValue);
                    }
                }
        });
        inv_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        inv_field.setText(oldValue);
                    }
                }
        });
        max_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        max_field.setText(oldValue);
                    }
                }
        });
        min_field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,20}?")) {
                        min_field.setText(oldValue);
                    }
                }
        });
        
        // if the object is inHouse contain it to an inHouse object
        // else object is outSourced and contain it as an outSourced object
        if(!outSourcedSelected){
            inHouseBtn.setSelected(true);
            outSourcedBtn.setDisable(true);
        } else {
            outSourcedBtn.setSelected(true);
            inHouseBtn.setDisable(true);
        }
        
        // initialize text fields with controller variables
        id_field.setText(Integer.toString(id_T));
        name_field.setText(name_T);
        inv_field.setText(Integer.toString(inv_T));
        price_field.setText(priceFormat.format(price_T));
        max_field.setText(Integer.toString(max_T));
        min_field.setText(Integer.toString(min_T));
        mid_cname_field.setText(mid_cn_T);

    }
    
    // Method checks if modified part can be saved with appropriate error messages
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

    @FXML
    private void save_part(ActionEvent event) throws IOException {
        
        if (!canSave()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Procesing Error");
            alert.setHeaderText(null);
            alert.setContentText(error_message);

            alert.showAndWait();
        }
        else {
            int id = Integer.parseInt(id_field.getText());
            double price = Double.parseDouble(price_field.getText());
            int inv = Integer.parseInt(inv_field.getText());
            int min = Integer.parseInt(min_field.getText());
            int max = Integer.parseInt(max_field.getText());
            String cname = mid_cname_field.getText();
            int mid = Integer.parseInt(mid_cname_field.getText());
            String name = name_field.getText();
            
            if (source.getSelectedToggle() == inHouseBtn) {
                Inventory.updatePart(id, inv, price, max, min, name, mid);
            } else if (source.getSelectedToggle() == outSourcedBtn){
                Inventory.updatePart(id, inv, price, max, min, name, cname);
            }
            Parent home_page_parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
        
        
        
    }
    
    
}