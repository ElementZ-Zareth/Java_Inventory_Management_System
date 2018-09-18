package ims;

/**
 *
 * @author Skye McClure
 */
public class inHouse extends Part {
    
    private int machineID;
    
    public inHouse(int machineID, int partID, String name, double price, int inStock, int min, int max){
        super(partID, name, price, inStock, min, max);
        this.machineID = machineID;
    }
    
    public void setMachineID(int machineID){
        this.machineID = machineID;
    }
    
    public int getMachineID(){
        return machineID;
    }
}
