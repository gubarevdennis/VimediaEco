package vimedia.service.ReportApp.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties("facility")
    private List<Report> reports;

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
}
