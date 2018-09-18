package ims;

/**
 *
 * @author Skye McClure
 */
public class outSourced extends Part {
    
    private String companyName;
    
    public outSourced(String companyName, int partID, String name, double price, int inStock, int min, int max){
        super(partID, name, price, inStock, min, max);
        this.companyName = companyName;
    }
    
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    
    public String getCompanyName(){
        return companyName;
    }
}
