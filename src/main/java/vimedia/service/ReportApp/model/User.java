package vimedia.service.ReportApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "usr")
@ToString(of = {"id","role"})
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    @Column(unique=true)
    private String name;

    @OneToMany(mappedBy = "user")
    @JsonView(Views.Id.class)

    private List<Report> reports;

    private String password;

    @JsonView(Views.IdName.class)
    private String role;

    public void addReport(Report report){
        this.getReports().add(report);
        report.setUser(this);
    }
    public void removeReport(Report report){
        this.getReports().remove(report);
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
}
