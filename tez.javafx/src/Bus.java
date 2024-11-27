public class Bus {
    private String numberPlate;
    private String yearOfRegistration;

    public Bus() {
    }

    public Bus(String numberPlate, String yearOfRegistration) {
        this.numberPlate = numberPlate;
        this.yearOfRegistration = yearOfRegistration;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getYearOfRegistration() {
        return yearOfRegistration;
    }

    public void setYearOfRegistration(String yearOfRegistration) {
        this.yearOfRegistration = yearOfRegistration;
    }
}
