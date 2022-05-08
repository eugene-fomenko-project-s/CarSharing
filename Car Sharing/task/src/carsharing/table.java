package carsharing;

import java.util.List;

public interface table {
    List<data_cars> getAllDataCar();
    List<data> getAllDataCom();
    List<data_customers> getAllDataCus();
    data getDataCom(int ID);
    data_cars getDataCar(int ID);
    data_customers getDataCus(int ID);
    void updateDataCom(data data_l);
    void updateDataCar(data_cars data_l);
    void updateDataCus(data_customers data_l);
    void deleteDataCom(data data_l);
    void deleteDataCar(data_cars data_l);
    void deleteDataCus(data_customers data_l);
}
