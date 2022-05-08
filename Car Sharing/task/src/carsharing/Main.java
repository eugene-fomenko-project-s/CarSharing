package carsharing;


import java.sql.SQLException;

import static carsharing.Connection_to.connection_to_database;


public class Main {
    // JDBC driver name and database URL

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    public static final String TABLE_COMPANY = "COMPANY";
    public static final String COLUMN_COMPANY_ID = "ID";
    public static final String COLUMN_COMPANY_NAME = "NAME";

    public static void main(String[] args) {
        connection_to_database(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_COMPANY + " (" +
                        COLUMN_COMPANY_ID + " INT PRIMARY KEY AUTO_INCREMENT, " +
                        COLUMN_COMPANY_NAME + " VARCHAR(30) UNIQUE NOT NULL);"

        );
        connection_to_database("CREATE TABLE IF NOT EXISTS " +
                "CAR" + " (" +
                "ID" + " INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME" + " VARCHAR(30) UNIQUE NOT NULL," +
                "COMPANY_ID INT NOT NULL," +
                "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) " +
                "REFERENCES COMPANY(ID)" +
                ")");
        connection_to_database("CREATE TABLE IF NOT EXISTS " +
                "CUSTOMER" + " (" +
                "ID" + " INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME" + " VARCHAR(30) UNIQUE NOT NULL," +
                "RENTED_CAR_ID INT ," +
                "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) " +
                "REFERENCES CAR(ID)" +
                ")");
        connection_to_database("ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH 1");
        connection_to_database("ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1");
        connection_to_database("ALTER TABLE CAR ALTER COLUMN ID RESTART WITH 1");
        try {
            menu_list a = new menu_list();
            a.start_menu();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}