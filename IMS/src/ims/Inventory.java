package ims;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Skye McClure
 */
public class Inventory {
    
    
    // Parts Interface

    @FXML
            private Button b_partsModify;
    @FXML
            private Button b_partsDelete;
    @FXML
            private TextField tf_partsSearch;
    @FXML
            private TableView<Part> t_parts;
    @FXML
            private TableColumn<Part, String> tc_partsID;
    @FXML
            private TableColumn<Part, String> tc_partsName;
    @FXML
            private TableColumn<Part, String> tc_partsIL;
    @FXML
            private TableColumn<Part, String> tc_partsPPU;
    
    

    // Product Interface
    @FXML
            private Button b_productsModify;
    @FXML
            private Button b_productsDelete;
    @FXML
            private TextField tf_productSearch;
    @FXML
            private TableView<Product> t_product;
    @FXML
            private TableColumn<Product, String> tc_productID;
    @FXML
            private TableColumn<Product, String> tc_productName;
    @FXML
            private TableColumn<Product, String> tc_productIL;
    @FXML
            private TableColumn<Product, String> tc_productPPU;
    
    // An observable array list to collect all the Part objects for the program
    private static final ObservableList<Part> partsData = FXCollections.observableArrayList(
        //Creating an instance of inHouse Part for the Parts list
        new inHouse(1, 1, "Particle Beam", 4.99, 3, 0, 3)
    );
    
    // An observable array list to collect all the Product objects for the program
    private static final ObservableList<Product> productsData = FXCollections.observableArrayList(
        //Leaving this list empty to show functionality of empty list
    );
   
    // Formatting purposes for the Price Field
    private static final DecimalFormat priceFormat = new DecimalFormat("0.00");
    
    // initialize the class
    @FXML
    private void initialize() {
        
        // Set Parts data to each respective column
        tc_partsID.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getPartID())));
        tc_partsName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        tc_partsIL.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getInStock())));
        tc_partsPPU.setCellValueFactory(c -> new SimpleStringProperty(priceFormat.format(c.getValue().getPrice())));
        
        // Set Products data to each respective column
        tc_productID.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getProductID())));
        tc_productName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        tc_productIL.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getInStock())));
        tc_productPPU.setCellValueFactory(c -> new SimpleStringProperty(priceFormat.format(c.getValue().getPrice())));
        
        // Automatic search functionality
        // convert list data to filtered list
        FilteredList<Part> filteredData = new FilteredList<>(partsData, p -> true);
        FilteredList<Product> filteredData2 = new FilteredList<>(productsData, p -> true);

        // Using filtered data, adding a listener to the textfield to see user input automatically
        // Search Parts 
        tf_partsSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(part -> {
                // Grab the input from field and change to lower case
                String lowerCase = newValue.toLowerCase();
                // Use our lowerCase string we compare to the Name field and return only that data
                return part.getName().toLowerCase().contains(lowerCase); 
            });
        });
        
        // Search Products
        tf_productSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData2.setPredicate(part -> {
                String lowerCaseFilter = newValue.toLowerCase();
                return part.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Sort the filtered data
        SortedList<Part> sortedData = new SortedList<>(filteredData);
        SortedList<Product> sortedData2 = new SortedList<>(filteredData2);

        // Bind the sorted data with the respective table data 
        sortedData.comparatorProperty().bind(t_parts.comparatorProperty());
        sortedData2.comparatorProperty().bind(t_product.comparatorProperty());

        // Finally we set the sorted data items to their respective tables
        t_parts.setItems(sortedData);
        t_product.setItems(sortedData2);
        
        // If no Parts are selected we disable the Modify and Delete buttons
        b_partsModify.disableProperty().bind(Bindings.isEmpty(t_parts.getSelectionModel().getSelectedItems()));
        b_partsDelete.disableProperty().bind(Bindings.isEmpty(t_parts.getSelectionModel().getSelectedItems()));
        // If no Products are selected we disable the Modify and Delete buttons
        b_productsModify.disableProperty().bind(Bindings.isEmpty(t_product.getSelectionModel().getSelectedItems()));
        b_productsDelete.disableProperty().bind(Bindings.isEmpty(t_product.getSelectionModel().getSelectedItems()));
        
        
    }
    
    // Delete a part from the list with confirmation dialogue
    @FXML
    private void deletePart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            partsData.remove(t_parts.getSelectionModel().getSelectedItem());
        }
        
    }
    
    // Static method to add part to list
    @FXML
    public static void addPart(Part p) {
        partsData.add(p);
    }
    
    
    // Static method returns a specific part
    @FXML
    public static Part lookupPart(int i){
        return partsData.get(i);
    }
    
    // Static method to grab entire array list of parts - used to fill list in add and modify products pages
    @FXML
    public static ArrayList<Part> getParts(){
        ArrayList<Part> parts = new ArrayList<>();
        for(Part p: partsData){
            parts.add(p);
        }
        return parts;
    }
    
    // Static method that updates an inHouse part that matches the id given
    @FXML
    public static void updatePart(int i, int inv, double price, int max, int min, String name, int mid){
        for(Part p: partsData){
            if(p.getPartID() == i){
                inHouse x = (inHouse) p;
                    x.setInStock(inv);
                    x.setPrice(price);
                    x.setMax(max);
                    x.setMin(min);
                    x.setName(name);
                    x.setMachineID(mid);
            }
        }
    }
    
    // Static method that updates an outSourced part that mathces the id given
    @FXML
    public static void updatePart(int i, int inv, double price, int max, int min, String name, String cname){
        for(Part p: partsData){
            if(p.getPartID() == i){
                outSourced x = (outSourced) p;
                    x.setInStock(inv);
                    x.setPrice(price);
                    x.setMax(max);
                    x.setMin(min);
                    x.setName(name);
                    x.setCompanyName(cname);
            }
        }
    }
    
    // Method that deletes a product from the list with confirmation dialogue box
    @FXML
    private void deleteProduct() {
        if(!t_product.getSelectionModel().getSelectedItem().lookupAssociatedPart().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part?");
            alert.setHeaderText(null);
            alert.setContentText("This product as at least one part associated with it.  Are you sure you want to delete?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                productsData.remove(t_product.getSelectionModel().getSelectedItem());
            }
        }
        
        
    }
    
    // Static method that adds a product to the list
    @FXML
    public static void addProduct(Product p) {
        productsData.add(p);
    }
    
    // Static method returns a specific product
    @FXML
    public static Product lookupProduct(int i){
        return productsData.get(i);
    }
    
    // Static method that returns the entire products array
    @FXML
    public static ArrayList<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();
        for(Product p: productsData){
            products.add(p);
        }
        return products;
    }
    
    // Static method that updates a product given a specific id
    @FXML
    public static void updateProduct(int i, int inv, double price, int max, int min, String name, ArrayList<Part> parts){
        for(Product p: productsData){
            if(p.getProductID() == i){

                p.setInStock(inv);
                p.setPrice(price);
                p.setMax(max);
                p.setMin(min);
                p.setName(name);
                p.addAssociatedPart(parts);
            }
        }
    }
    
    // Button event to show the Add Part screen
    @FXML
    private void show_add_part(ActionEvent event) throws IOException {
        Parent add_part_parent = FXMLLoader.load(getClass().getResource("add_part.fxml"));
        Scene add_part_scene = new Scene(add_part_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(add_part_scene);
        app_stage.show();
    }
    
    // Buttn even to show the Modify Part screen
    @FXML
    private void show_modify_part(ActionEvent event) throws IOException {
        
        // initializing the variables we will need to push to the controller
        int id_Transfer = t_parts.getSelectionModel().getSelectedItem().getPartID();
        String name_Transfer = t_parts.getSelectionModel().getSelectedItem().getName();
        int inv_Transfer  = t_parts.getSelectionModel().getSelectedItem().getInStock();
        double price_Transfer  = t_parts.getSelectionModel().getSelectedItem().getPrice();
        int max_Transfer = t_parts.getSelectionModel().getSelectedItem().getMax();
        int min_Transfer  = t_parts.getSelectionModel().getSelectedItem().getMin();
        
        // The following code checks whether the Part being sent is inHouse or outSoured
        // inHouse and outSourced use different variables of MachineID int and CompanyName string
        
        // check if the Part being pushed is instance of inHouse 
        if (t_parts.getSelectionModel().getSelectedItem() instanceof inHouse){
            inHouse myObject = (inHouse) t_parts.getSelectionModel().getSelectedItem();
            int mid_Transfer = myObject.getMachineID();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify_part.fxml"));
            Modify_partController controller = new Modify_partController(id_Transfer, name_Transfer, inv_Transfer, price_Transfer, max_Transfer, min_Transfer, mid_Transfer);
            loader.setController(controller);
            AnchorPane page = (AnchorPane) loader.load();
            Scene modify_part_scene = new Scene(page);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(modify_part_scene);
            app_stage.show();
        } 
        // check if the Part being pushed is instance of outSourced
        else if (t_parts.getSelectionModel().getSelectedItem() instanceof outSourced) {
            outSourced myObject = (outSourced) t_parts.getSelectionModel().getSelectedItem();
            String cn_Transfer = myObject.getCompanyName();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify_part.fxml"));
            Modify_partController controller = new Modify_partController(id_Transfer, name_Transfer, inv_Transfer, price_Transfer, max_Transfer, min_Transfer, cn_Transfer);
            loader.setController(controller);
            AnchorPane page = (AnchorPane) loader.load();
            Scene modify_part_scene = new Scene(page);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(modify_part_scene);
            app_stage.show();
        }
        
        
        
    }

    // Button event to show the Add Product screen
    @FXML
    private void show_add_product(ActionEvent event) throws IOException {
        Parent add_product_parent = FXMLLoader.load(getClass().getResource("add_product.fxml"));
        Scene add_product_scene = new Scene(add_product_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(add_product_scene);
        app_stage.show();
    }
    
    // Button event to show the Modify Product screen
    @FXML
    private void show_modify_product(ActionEvent event) throws IOException {
        
        // initializing variables to push to the controller
        int id_Transfer = t_product.getSelectionModel().getSelectedItem().getProductID();
        String name_Transfer = t_product.getSelectionModel().getSelectedItem().getName();
        int inv_Transfer  = t_product.getSelectionModel().getSelectedItem().getInStock();
        double price_Transfer  = t_product.getSelectionModel().getSelectedItem().getPrice();
        int max_Transfer = t_product.getSelectionModel().getSelectedItem().getMax();
        int min_Transfer  = t_product.getSelectionModel().getSelectedItem().getMin();
        ArrayList<Part> parts_Transfer = t_product.getSelectionModel().getSelectedItem().lookupAssociatedPart();
                
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify_product.fxml"));
        Modify_productController controller = new Modify_productController(id_Transfer, name_Transfer, inv_Transfer, price_Transfer, max_Transfer, min_Transfer, parts_Transfer);
        loader.setController(controller);
        AnchorPane page = (AnchorPane) loader.load();
        Scene modify_part_scene = new Scene(page);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(modify_part_scene);
        app_stage.show();
    }

}
