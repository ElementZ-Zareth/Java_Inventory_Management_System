package ims;

import java.util.ArrayList;

/**
 *
 * @author Skye McClure
 */
public class Product {
    private ArrayList<Part> associatedParts = new ArrayList<>();
    private int productID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;
  
    public Product(int productID, String name, double price, int inStock, int min, int max, ArrayList<Part> associatedParts){
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
        this.associatedParts = associatedParts;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setPrice(double price){
        this.price = price;
    }
    
    public double getPrice(){
        return price;
    }
    
    public void setInStock(int inStock){
        this.inStock = inStock;
    }
    
    public int getInStock(){
        return inStock;
    }
    
    public void setMin(int min){
        this.min = min;
    }
    
    public int getMin(){
        return min;
    }
    
    public void setMax(int max){
        this.max = max;
    }
    
    public int getMax(){
        return max;
    }

    // Method used to grab entire observable array list of associated parts
    
    public void addAssociatedPart(ArrayList<Part> parts){
        this.associatedParts = parts;
    }
    
    /*
    I chose to change this method, shown above, to add an entire array list
        public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    */
    
    
    // returns entire array list
    public ArrayList<Part> lookupAssociatedPart(){
        return associatedParts;
    }
    
    /*
    public Part lookupAssociatedPart(int i){
        return associatedParts.get(i);
    }
    */
    
    // Method used to clear entire associated parts list
    // Purpose is to clear the entire array list and create a new array list after every save
    public void removeAssociatedPart(){
        associatedParts.clear();
    }
        
    public void setProductID(int productID){
        this.productID = productID;
    }
    
    public int getProductID(){
        return productID;
    }
}
