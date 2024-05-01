package vimedia.service.ReportApp.service;

public class ReplaceCoastObject {

    private String dateFrom;
    private String dateTo;
    private int salary;

    public ReplaceCoastObject(String dateFrom, String dateTo, int salary) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.salary = salary;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        salary = salary;
    }
}
