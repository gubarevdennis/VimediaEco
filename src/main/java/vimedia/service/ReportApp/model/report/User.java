package vimedia.service.ReportApp.model.report;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.model.tools.Tool;

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
    @JsonView(Views.Id.class)
    private List<Report> reports;

    private String password;

    private String telegramId;

    @JsonView(Views.IdName.class)
    private String role;

    @JsonView(Views.IdName.class)
    private Long salary;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Tool> tools;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Event> events;

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

    public void addEvent(Event event){
        this.events.add(event);
        event.setUser(this);
    }
    public void removeEvent(Event event){
        this.events.remove(event);
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
}
