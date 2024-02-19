package vimedia.service.ReportApp.model.report;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vimedia.service.ReportApp.model.tools.Tool;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@ToString(of = {"id","name"})
@EqualsAndHashCode(of = {"id"})
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)

    private int id;
    @JsonView(Views.IdName.class)
    @Column(unique=true)
    private String name;

    @JsonView(Views.IdName.class)
    private String color;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FuLlFacility.class)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "facility")
//    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("facility")
    private List<Report> reports;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("facility")
    private List<SubFacility> subFacilities;

    @OneToMany(mappedBy = "facility")
//    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("facility")
    private List<Tool> tools;

    @OneToMany(mappedBy = "facility")
//    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("facility")
    private List<Job> jobs;

    @OneToMany(mappedBy = "facility")
//    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("facility")
    private List<User> users;

    public void addReport(Report report){
        this.reports.add(report);
        report.setFacility(this);
    }
    public void removeReport(Report report){
        this.reports.remove(report);
    }
    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void addSubFacility(SubFacility subFacility){
        this.subFacilities.add(subFacility);
        subFacility.setFacility(this);
    }
    public void removeSubFacility(SubFacility subFacility){
        this.subFacilities.remove(subFacility);
    }

    public void addTool(Tool tool){
        this.tools.add(tool);
        tool.setFacility(this);
    }

    public void removeTool(Tool tool){
        this.tools.remove(tool);
    }

    public void addJob(Job job){
        this.jobs.add(job);
        job.setFacility(this);
    }

    public void removeJob(Job job){
        this.jobs.remove(job);
    }

    public void addUser(User user){
        this.users.add(user);
        user.setFacility(this);
    }

    public void removeUser(User user){
        this.users.remove(user);
    }

    public List<SubFacility> getSubFacilities() {
        return subFacilities;
    }

    public void setSubFacilities(List<SubFacility> subFacilities) {
        this.subFacilities = subFacilities;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
