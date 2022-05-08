package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static carsharing.Main.DB_URL;
import static carsharing.Main.JDBC_DRIVER;
import static carsharing.menu_list.switch_menu;

public class table_impl implements table {

    //list is working as a database
    List<data_cars> data_car;
    List<data> data_company;
    List<data_customers> data_customers;
    List<data_father> data_fathers;
        Connection conn;
    Statement stmt;

    public table_impl() throws ClassNotFoundException, SQLException {
        // STEP 1: Register JDBC driver
        Class.forName(JDBC_DRIVER);
        //STEP 2: Open a connection
        conn = DriverManager.getConnection(DB_URL);
        //STEP 3: Execute a query
        stmt = conn.createStatement();
        try {
            data_company = new ArrayList<>();
            data_car = new ArrayList<>();
            data_fathers = new ArrayList<>();
            data_customers = new ArrayList<>();
            ResultSet resultset;
            if(switch_menu == menu_output.CUSTOMERS_MENU){
                resultset = stmt.executeQuery("SELECT * FROM "+"CUSTOMER"+" ORDER BY (ID)");
            }else{
                resultset = stmt.executeQuery("SELECT * FROM "+(switch_menu == menu_output.CAR_MENU?"CAR":"COMPANY")+" ORDER BY (ID)");
            }
            if(switch_menu == menu_output.CAR_MENU){
                while (resultset.next()) {
                    data_car.add(new data_cars(resultset.getInt("ID"), resultset.getString("NAME"),resultset.getInt("COMPANY_ID")));
                    data_fathers.add(new data_father(resultset.getInt("ID"), resultset.getString("NAME")));
                }
            }else if(switch_menu == menu_output.CUSTOMERS_MENU){
                while (resultset.next()) {
                    data_customers.add(new data_customers(resultset.getInt("ID"), resultset.getString("NAME"), resultset.getInt("RENTED_CAR_ID")));
                    data_fathers.add(new data_father(resultset.getInt("ID"), resultset.getString("NAME")));
                }
            }else{
                while (resultset.next()) {
                    data_company.add(new data(resultset.getInt("ID"), resultset.getString("NAME")));
                    data_fathers.add(new data_father(resultset.getInt("ID"), resultset.getString("NAME")));
                }
            }
            stmt.close();
            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
        }
        finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public void deleteDataCom(data data_l) {
        data_company.remove(data_l.getID());
    }
    @Override
    public void deleteDataCar(data_cars data_l) {
        data_car.remove(data_l.getID());
    }

    @Override
    public void deleteDataCus(carsharing.data_customers data_l) {
        data_customers.remove(data_l.getID());
    }


    @Override
    public List<data_cars> getAllDataCar() {
        return data_car;
    }
    @Override
    public List<data> getAllDataCom() {
        return data_company;
    }
    @Override
    public List<carsharing.data_customers> getAllDataCus() {
        return data_customers;
    }
    public List<data_father> getAllData(){
        return data_fathers;
    }
    @Override
    public data getDataCom(int ID) {
        return data_company.get(ID);
    }
    @Override
    public data_cars getDataCar(int ID) {
        return data_car.get(ID);
    }

    @Override
    public carsharing.data_customers getDataCus(int ID) {
        return data_customers.get(ID);
    }

    @Override
    public void updateDataCom(data data_l) {
        data_company.get(data_l.getID()).setName(data_l.getName());
    }
    @Override
    public void updateDataCar(data_cars data_l) {
        data_company.get(data_l.getID()).setName(data_l.getName());
    }

    @Override
    public void updateDataCus(carsharing.data_customers data_l) {
        data_customers.get(data_l.getID()).setName(data_l.getName());
    }
}
