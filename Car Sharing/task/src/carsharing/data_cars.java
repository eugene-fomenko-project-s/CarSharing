package carsharing;

public class data_cars extends data_father {
    private int company_id;

    public data_cars(int id, String name, int company_id) {
        super(id, name);
        this.company_id = company_id;
    }

    public int getCompanyId() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }
}
