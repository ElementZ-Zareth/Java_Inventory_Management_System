package ims;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Skye McClure
 */
public class Add_ProductController {

    // initialize FXML elements
    @FXML
        private TableView<Part> t_parts_all;
    @FXML
        private TableColumn<Part, String> tc_partsID_all;
    @FXML
        private TableColumn<Part, String> tc_partsName_all;
    @FXML
        private TableColumn<Part, String> tc_partsIL_all;
    @FXML
        private TableColumn<Part, String> tc_partsPPU_all;
    @FXML
        private TableView<Part> t_parts_asso;
    @FXML
        private TableColumn<Part, String> tc_partsID_asso;
    @FXML
        private TableColumn<Part, String> tc_partsName_asso;
    @FXML
        private TableColumn<Part, String> tc_partsIL_asso;
    @FXML
        private TableColumn<Part, String> tc_partsPPU_asso;
    
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
        private TextField search_field;
    
    @FXML
        private Button add;
    @FXML
        private Button delete;
    
    private String error_message = "";
    
    private final ObservableList<Part> partsList = FXCollections.observableArrayList();
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    
    private final Random random_Number = new Random();
    private int uniqueID = 1 + random_Number.nextInt(50);
    
    // initialize double to be used to contain combined associated parts total cost
    double partsCost;
    
    private static final DecimalFormat priceFormat = new DecimalFormat("0.00");
    
    // initialize class
    @FXML
    public void initialize() {
        
        while(!isUniqueID()){
            uniqueID = 1 + random_Number.nextInt(50);
        }
        
        id_field.setText(Integer.toString(uniqueID));
        
        Inventory.getParts().forEach((p) -> {
            partsList.add(p);
        });
        
        //Set Parts Table
        tc_partsID_all.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getPartID())));
        tc_partsName_all.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        tc_partsIL_all.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getInStock())));
        tc_partsPPU_all.setCellValueFactory(c -> new SimpleStringProperty(priceFormat.format(c.getValue().getPrice())));
        
        tc_partsID_asso.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getPartID())));
        tc_partsName_asso.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        tc_partsIL_asso.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getInStock())));
        tc_partsPPU_asso.setCellValueFactory(c -> new SimpleStringProperty(priceFormat.format(c.getValue().getPrice())));
        
        t_parts_asso.setItems(associatedParts);
        
        
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
        
        add.disableProperty().bind(Bindings.isEmpty(t_parts_all.getSelectionModel().getSelectedItems()));
        delete.disableProperty().bind(Bindings.isEmpty(t_parts_asso.getSelectionModel().getSelectedItems()));

        FilteredList<Part> filteredData = new FilteredList<>(partsList, p -> true);
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(part -> {
                String lowerCase = newValue.toLowerCase();
                return part.getName().toLowerCase().contains(lowerCase); 
            });
        });
        SortedList<Part> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(t_parts_all.comparatorProperty());
        t_parts_all.setItems(sortedData);
        
    }    
    
    private boolean isUniqueID() {
        if (!Inventory.getProducts().stream().noneMatch((p) -> (uniqueID == p.getProductID()))) {
            return false;
        }
        return true;
    }
    
    // Method checks if the new product can be saved with appropriate error messages
    private boolean canSave() {
        // Always reset partsCost back to 0 before checking if can be saved
        partsCost = 0;
        
        // set inventory, minimum, and max fields to 0 if fields are empty
        if (inv_field.getText().isEmpty()){
            inv_field.setText("0");
        }
        if (min_field.getText().isEmpty()){
            min_field.setText("0");
        }
        if (max_field.getText().isEmpty()){
            max_field.setText("0");
        }
        
        if (name_field.getText().trim().isEmpty()) {
            error_message = "The product must have a name assigned to it";
            return false;
        }
        
        if (price_field.getText().trim().isEmpty()){
            error_message = "The product must have a price assigned to it";
            return false;
        }
        
        
        if (associatedParts.isEmpty()){
            error_message = "The product must have at least one part associated with it.";
            return false;
        }
        
        // Adds total cost of all associated parts and checks if price is lower than total cost
        associatedParts.forEach((p) -> {
                partsCost = partsCost + p.getPrice();
        });
        if (Double.parseDouble(price_field.getText()) < partsCost){
            error_message = "Price of product cannot be lower than cost of all associated parts: $" + priceFormat.format(partsCost);
            
            return false;
        }
        
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
    
    public void addAssociatedPart(){
        if(!t_parts_all.getSelectionModel().isEmpty()){
            associatedParts.add(t_parts_all.getSelectionModel().getSelectedItem());
        }
    }
    
    @FXML
    private void deleteAssociatedPart() {
        if(!t_parts_asso.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this part?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                associatedParts.remove(t_parts_asso.getSelectionModel().getSelectedItem());
            }
        }
        
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
    
    public void save_product(ActionEvent event) throws IOException {
        if (!canSave()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
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
            
            String name = name_field.getText();
            ArrayList<Part> parts = new ArrayList<>();
            
            associatedParts.forEach((p) -> {
                parts.add(p);
            });
            
            Product product = new Product(id, name, price, inv, min, max, parts);
            
            
            Inventory.addProduct(product);
            
            
            Parent home_page_parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Scene home_page_scene = new Scene(home_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
    }
}
