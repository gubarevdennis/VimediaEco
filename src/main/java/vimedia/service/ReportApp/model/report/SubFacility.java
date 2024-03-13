package vimedia.service.ReportApp.model.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@ToString(of = {"id","name"})
@EqualsAndHashCode(of = {"id"})
public class SubFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)

    private int id;
    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.IdName.class)
    private String color;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FuLlFacility.class)
    private LocalDateTime creationDate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"subFacilities", "user"})
    @JsonView(Views.IdName.class)
    private Facility facility;

    @OneToMany(mappedBy = "subFacility")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private List<Report> reports;

    @OneToMany(mappedBy = "subFacility")
//    @JsonView(Views.IdName.class)
    @JsonIgnore
    private List<Job> jobs;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("subFacility")
    private User user;


    public void addReport(Report report){
        this.reports.add(report);
        report.setSubFacility(this);
    }
    public void removeReport(Report report){
        this.reports.remove(report);
    }

    public void addJob(Job job){
        this.jobs.add(job);
        job.setSubFacility(this);
    }

    public void removeJob(Job job){
        this.jobs.remove(job);
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
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

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
