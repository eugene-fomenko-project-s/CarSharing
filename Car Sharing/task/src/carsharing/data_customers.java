package carsharing;

public class data_customers extends data_father {
    private int rented_car_id;

    public data_customers(int id, String name, int rented_car_id) {
        super(id, name);
        this.rented_car_id = rented_car_id;
    }

    public int getRented_car_id() {
        return rented_car_id;
    }

    public void setRented_car_id(int rented_car_id) {
        this.rented_car_id = rented_car_id;
    }

}
