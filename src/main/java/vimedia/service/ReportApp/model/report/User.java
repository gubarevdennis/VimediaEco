package vimedia.service.ReportApp.model.report;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vimedia.service.ReportApp.model.bonus.Bonus;
import vimedia.service.ReportApp.model.tools.*;

import java.util.List;

@Entity
@Table(name = "usr")
@ToString(of = {"id","role","salary"})
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    @Column(unique=true)
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonView(Views.IdNameAndReports.class)
    @JsonIgnoreProperties("subFacilities")
    private List<Report> reports;

    private String password;

    @JsonView(Views.Id.class)
    private String telegramId;

    @JsonView(Views.IdName.class)
    private String role;

    @JsonView(Views.IdName.class)
    private Long salary;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Tool> tools;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @JsonView(Views.IdName.class)
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @JsonView(Views.IdName.class)
    private List<InventoryEvent> inventoryEvents;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @JsonView(Views.IdName.class)
    private List<RepairEvent> repairEvents;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @JsonView(Views.IdName.class)
    private List<ServiceEvent> serviceEvents;

    // Связь с работами
    @JsonView(Views.IdName.class)
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.SET_NULL)
//    @JsonIgnoreProperties({"users", "jobs", "bonuses"})
    private List<Job> jobs;

    // Связь с объектами
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
//    @JsonView(Views.IdName.class)
    private List<Facility> facilities;

    // Связь с подобьектами
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
 //   @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("reports")
    private List<SubFacility> subFacilities;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
//    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("user")
    private List<Bonus> bonuses;

    public void addBonus(Bonus bonus){
        this.bonuses.add(bonus);
        bonus.setUser(this);
    }
    public void removeBonus(Bonus bonus){
        this.bonuses.remove(bonus);
    }

    public void addEvent(Event event){
        this.events.add(event);
        event.setUser(this);
    }
    public void removeEvent(Event event){
        this.events.remove(event);
    }

    public void addFacility(Facility facility){
        this.facilities.add(facility);
        facility.setUser(this);
    }
    public void removeFacility(Facility facility){
        this.events.remove(facility);
    }

    public void addSubFacility(SubFacility subFacility){
        this.subFacilities.add(subFacility);
        subFacility.setUser(this);
    }
    public void removeSubFacility(SubFacility subFacility){
        this.subFacilities.remove(subFacility);
    }


    public void addInventoryEvent(InventoryEvent event){
        this.inventoryEvents.add(event);
        event.setUser(this);
    }
    public void removeInventoryEvent(InventoryEvent event){
        this.inventoryEvents.remove(event);
    }

    public void addRepairEvent(RepairEvent event){
        this.repairEvents.add(event);
        event.setUser(this);
    }
    public void removeRepairEvent(RepairEvent event){
        this.repairEvents.remove(event);
    }

    public void addServiceEvent(ServiceEvent event){
        this.serviceEvents.add(event);
        event.setUser(this);
    }

    public void removeServiceEvent(ServiceEvent event){
        this.serviceEvents.remove(event);
    }


    public void addReport(Report report){
        this.getReports().add(report);
        report.setUser(this);
    }
    public void removeReport(Report report){
        this.getReports().remove(report);
    }

    public void addTool(Tool tool){
        this.tools.add(tool);
        tool.setUser(this);
    }
    public void removeTool(Tool tool){
        this.tools.remove(tool);
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<InventoryEvent> getInventoryEvents() {
        return inventoryEvents;
    }

    public void setInventoryEvents(List<InventoryEvent> inventoryEvents) {
        this.inventoryEvents = inventoryEvents;
    }

    public List<RepairEvent> getRepairEvents() {
        return repairEvents;
    }

    public void setRepairEvents(List<RepairEvent> repairEvents) {
        this.repairEvents = repairEvents;
    }

    public List<ServiceEvent> getServiceEvents() {
        return serviceEvents;
    }

    public void setServiceEvents(List<ServiceEvent> serviceEvents) {
        this.serviceEvents = serviceEvents;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public List<SubFacility> getSubFacilities() {
        return subFacilities;
    }

    public void setSubFacilities(List<SubFacility> subFacilities) {
        this.subFacilities = subFacilities;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<Bonus> bonuses) {
        this.bonuses = bonuses;
    }
}
