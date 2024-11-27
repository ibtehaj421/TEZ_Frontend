
public class BusDTO {
    public long id;
    public String model;
    public String licNum;
    public int capacity = 20;
    public Long driverID;
    public long orgID;
    public Long route;

    public String getModel(){
        return model;
    }
    public String getLicNum(){
        return licNum;
    }
    public String getNumberPlate() {
        return licNum;
    }

    public void setNumberPlate(String numberPlate) {
        this.licNum = numberPlate;
    }
    @Override
    public String toString(){
        return "Bus ID: " + id + ", Model: " + model + ", License Number: " + licNum;
    }
}
