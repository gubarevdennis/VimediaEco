package vimedia.service.ReportApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @ManyToOne
    @JsonIgnoreProperties("reports")
    private Facility facility;

    @JsonView(Views.IdName.class)
    @ManyToOne
    @JsonIgnoreProperties("reports")
    private User user;

    @JsonView(Views.IdName.class)
    private String typeOfWork;

    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private int hoursOfWorking;

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

    public int getHoursOfWorking() {
        return hoursOfWorking;
    }

    public void setHoursOfWorking(int hoursOfWorking) {
        this.hoursOfWorking = hoursOfWorking;
    }
}

