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
import vimedia.service.ReportApp.model.bonus.Bonus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@ToString(of = {"id","reportDay"})
@EqualsAndHashCode(of = {"id"})
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private int id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonView(Views.IdName.class)
    private LocalDateTime creationDate;

    @JsonView(Views.IdName.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate reportDay;

    @JsonView(Views.IdName.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties("reports") //"subFacilities"
    private Facility facility;

    @JsonView(Views.IdName.class)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties("reports")
    private SubFacility subFacility;

    @JsonView(Views.IdName.class)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
//    @JsonIgnore
    @JsonIgnoreProperties({"reports","jobs"})
    private User user;

    @JsonView(Views.IdName.class)
    private String typeOfWork;

    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private String cost;

    @JsonView(Views.IdName.class)
    private int hoursOfWorking;

    @JsonView(Views.IdName.class)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
//    @JsonIgnore
    @JsonIgnoreProperties({"reports","jobs","users"})
    private Job job;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getReportDay() {
        return reportDay;
    }

    public void setReportDay(LocalDate reportDay) {
        this.reportDay = reportDay;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public SubFacility getSubFacility() {
        return subFacility;
    }

    public void setSubFacility(SubFacility subFacility) {
        this.subFacility = subFacility;
    }

    public int getHoursOfWorking() {
        return hoursOfWorking;
    }

    public void setHoursOfWorking(int hoursOfWorking) {
        this.hoursOfWorking = hoursOfWorking;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String salary) {
        this.cost = salary;
    }
}

