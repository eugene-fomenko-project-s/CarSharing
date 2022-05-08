package carsharing;


import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

enum menu_output {
    PREVIEW_MENU,
    CUSTOMERS_MENU,
    COMPANY_MENU,
    CAR_MENU,
}

enum customer_output {
    PREVIEW,
    MENU,
    RENT,
}

public class menu_list {
    private data current_com;
    private data_customers current_cus;
    static public menu_output switch_menu;
    private customer_output switch_customer;
    private data_cars current_car;
    table_impl a = new table_impl();
    boolean flag = false;
    boolean flag2 = false;
    List<data> fake = a.getAllDataCom();
    Scanner sc = new Scanner(System.in);


    public menu_list() throws SQLException, ClassNotFoundException {
        switch_menu = menu_output.PREVIEW_MENU;
        switch_customer = customer_output.PREVIEW;
    }


    private void list() throws SQLException, ClassNotFoundException {
        table_impl a = new table_impl();
        String name = switch_menu == menu_output.CAR_MENU ? "car" : (switch_menu == menu_output.COMPANY_MENU ? "company" : "customer");
        List<data_father> data_list = a.getAllData();
        if (data_list == null || data_list.isEmpty()) {
            System.out.println("The " + name + " list is empty!");
            if (name.equals("customer")) {
                switch_menu = menu_output.PREVIEW_MENU;
            }
        } else {
            if (switch_menu != menu_output.CAR_MENU) {
                if(!flag2) {
                    System.out.println(switch_menu == menu_output.CUSTOMERS_MENU ? "The customer list:" : "Choose a " + name + ":");
                    int i = 1;
                    for (var f : data_list) {
                        System.out.println(i + ". " + f.getName());
                        i++;
                    }
                    System.out.println("0. Back");
                    show();
                }else{
                    System.out.println("You've already rented a car!");
                    switch_menu = menu_output.CUSTOMERS_MENU;
                    switch_customer = customer_output.MENU;
                }
            } else {
                List<data_cars> list = a.getAllDataCar();
                if ((int) list.stream()
                        .filter(x -> x.getCompanyId() == current_com.getID())
                        .count() == 0) {
                    System.out.println("The car list is empty!");
                } else {
                    int i = 1;
                    if (flag) {
                        System.out.println("Choose a car:");
                    } else {
                        System.out.println("Car list:");
                    }
                    for (data_cars f : list) {
                        if (current_com.getID() == f.getCompanyId()) {
                            System.out.println(i + ". " + f.getName());
                            i++;
                        }
                    }
                    System.out.println("0. Back");
                    show();
                }
            }
        }
    }

    private void show() throws SQLException, ClassNotFoundException {
        int num = sc.nextInt();
        if (num == 0 && switch_menu == menu_output.CAR_MENU) {
            switch_menu = menu_output.COMPANY_MENU;
        }
        if(flag){
            table_impl f = new table_impl();
            List<data_cars> data_cars = f.getAllDataCar();
            switch_menu = menu_output.CUSTOMERS_MENU;
            switch_customer = customer_output.MENU;
            for (data_cars data : data_cars) {
                if (data.getID() == num) {
                    current_car = data;
                    System.out.print("You rented '" +
                            data.getName()+"'\n");
                    Connection_to.connection_to_database("UPDATE CUSTOMER SET RENTED_CAR_ID = "+data.getID()+" WHERE ID = " + current_cus.getID() + ";");
                }
                switch_customer = customer_output.MENU;
                switch_menu = menu_output.CUSTOMERS_MENU;
            }
        }
        if (num != 0 && switch_customer == customer_output.PREVIEW) {
            table_impl das = new table_impl();
            List<data_customers> data_customer = das.getAllDataCus();
            for (data_customers data : data_customer) {
                if (data.getID() == num) {
                    current_cus = data;
                    switch_menu = menu_output.CUSTOMERS_MENU;
                    switch_customer = customer_output.MENU;
                }
            }
        }
        if (switch_menu == menu_output.COMPANY_MENU|| switch_customer == customer_output.RENT) {
            for (carsharing.data data : fake) {
                if (data.getID() == num) {
                    current_com = data;
                    if(switch_customer == customer_output.RENT){
                        flag = true;
                        switch_menu = menu_output.CAR_MENU;
                        list();
                        switch_menu = menu_output.CUSTOMERS_MENU;
                    }else{
                        switch_menu = menu_output.CAR_MENU;
                    }
                }
            }
        }
    }

    private void login(String choose) throws SQLException, ClassNotFoundException {
        switch (choose) {
            case "1":
                list();
                break;
            case "2":
                create();
                break;
            case "0":
                if (switch_menu == menu_output.CAR_MENU) {
                    switch_menu = menu_output.PREVIEW_MENU;
                }
                break;
        }
    }

    private void create() {
        String choose;
        String v;
        if (switch_menu == menu_output.CAR_MENU) {
            choose = "car";
            v = "added";
        } else if (switch_menu == menu_output.CUSTOMERS_MENU) {
            choose = "customer";
            v = "added";
            switch_menu = menu_output.PREVIEW_MENU;
        } else {
            choose = "company";
            v = "created";
        }
        System.out.println("Enter the " + choose + " name:");
        Scanner sc = new Scanner(System.in);
        String sql = "INSERT INTO " +
                choose.toUpperCase() +
                (switch_menu == menu_output.CAR_MENU ? " (NAME,COMPANY_ID)" : " (NAME)") +
                " VALUES ('" + sc.nextLine() +
                "'" +
                (switch_menu == menu_output.CAR_MENU ? ("," + current_com.getID()) : "")
                + ")";
        Connection_to.connection_to_database(sql);
        System.out.println("The " + choose + " was " + v + "!");
    }

    public void input(String action) throws SQLException, ClassNotFoundException {
        switch (switch_menu) {
            case PREVIEW_MENU:
                switch (action) {
                    case "1":
                        switch_menu = menu_output.COMPANY_MENU;
                        break;
                    case "2":
                        switch_menu = menu_output.CUSTOMERS_MENU;
                        switch_customer = customer_output.PREVIEW;
                        list();
                        break;
                    case "3":
                        switch_menu = menu_output.CUSTOMERS_MENU;
                        create();
                        break;
                }
                break;
            case COMPANY_MENU:
                if (Objects.equals(action, "0")) {
                    switch_menu = menu_output.PREVIEW_MENU;
                    break;
                } else {
                    login(action);
                }
                break;
            case CAR_MENU:
                if (Objects.equals(action, "0")) {
                    switch_customer = customer_output.MENU;
                    break;
                } else {
                    login(action);
                }
                break;
            case CUSTOMERS_MENU:
                switch (action){
                    case "1":
                        switch_customer = customer_output.RENT;
                        switch_menu = menu_output.COMPANY_MENU;
                        list();
                        break;
                    case "2":
                        if (current_cus == null || current_com == null) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = " + current_cus.getID() + ";";
                            Connection_to.connection_to_database(sql);
                            System.out.println("You've returned a rented car!");
                        }
                        break;
                    case "3":
                        table_impl f = new table_impl();
                        List<data_father> data_cars2 = f.getAllData();
                        List<data_customers> data_customer2  = f.getAllDataCus();
                        data_customers fuck2 = new data_customers(0,"0",0);
                        for(data_customers data : data_customer2){
                            if(data.getID() == current_cus.getID()){
                                fuck2 = data;
                            }
                        }
                        if (current_cus == null || current_com == null) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            for (data_father ads : data_cars2) {
                                if (ads.getID() == fuck2.getRented_car_id()) {
                                    System.out.print("Your rented car:\n" +
                                            current_car.getName() +
                                            "\nCompany:\n" +
                                            current_com.getName() + "\n");
                                    flag2 = true;
                                }
                            }
                        }
                        switch_menu = menu_output.CUSTOMERS_MENU;
                        break;
                }
        }
    }

    public void start_menu() throws SQLException, ClassNotFoundException {
        boolean running = true;
        String action = "";
        Scanner scanner = new Scanner(System.in);
        while (running) {
            switch (switch_menu) {
                case PREVIEW_MENU:
                    System.out.println("1. Log in as a manager\n" +
                            "2. Log in as a customer\n" +
                            "3. Create a customer\n" +
                            "0. Exit");
                    action = scanner.nextLine();
                    break;
                case COMPANY_MENU:
                    System.out.println("1. Company list\n"
                            + "2. Create a company\n"
                            + "0. Back");
                    action = scanner.nextLine();
                    break;
                case CAR_MENU:
                        System.out.print(current_com.getName() + " " + "company \n");
                        System.out.println("1. Car list \n" +
                                "2. Create a car\n" +
                                "0. Back");
                        action = scanner.nextLine();
                    break;
                case CUSTOMERS_MENU:
                    System.out.println("1. Rent a car\n" +
                            "2. Return a rented car\n" +
                            "3. My rented car\n" +
                            "0. Back");
                            action = scanner.nextLine();
                    break;
            }
            if (action.equals("0") && switch_menu == menu_output.PREVIEW_MENU) {
                running = false;
            } else {
                try {
                        input(action);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);
    }

}
