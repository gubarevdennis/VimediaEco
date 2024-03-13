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
import vimedia.service.ReportApp.model.tools.Tool;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@ToString(of = {"id","name"})
@EqualsAndHashCode(of = {"id"})
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    @Column(unique=true)
    private int id;

    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.IdName.class)
    private String type;

    @JsonView(Views.IdName.class)
    private String color;

    // Планируемый процент маржинальности
    @JsonView(Views.IdName.class)
    private Float marginPercentage;

    // Бонусная база
    @JsonView(Views.IdName.class)
    private Float bonus;

    // Общий бюджет обьекта
    @JsonView(Views.IdName.class)
    private Integer budget;

    // Процент исполнителей работ
    @JsonView(Views.IdName.class)
    private Float workerBonus;

    // Автоматическое или ручное проставление бонусов
    @JsonView(Views.IdName.class)
    private Integer autoBonus;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.IdName.class)
    private LocalDateTime creationDate;

    // Связь с объектами
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"tools", "subFacilities"})
    @JsonView(Views.IdName.class)
    private Facility facility;

    // Связь с подобьектами
    @JsonView(Views.IdName.class)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"jobs"})
    private SubFacility subFacility;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinTable(name = "usr_jobs",
            joinColumns = @JoinColumn(name = "usr_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "job_id",
                    referencedColumnName = "id"))
    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties({"jobs", "reports","bonuses"})
    private List<User> users;

    @OneToMany(mappedBy = "job")
    @JsonView(Views.FuLlFacility.class)
    @JsonIgnoreProperties("job")
    private List<Bonus> bonuses;

    @OneToMany(mappedBy = "job")
    @OnDelete(action = OnDeleteAction.SET_NULL)
//    @JoinTable(name = "job_report",
//            joinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "report_id",
//                    referencedColumnName = "id"))
    @JsonView(Views.IdName.class)
    @JsonIgnore
    private List<Report> reports;

    public void addBonus(Bonus bonus) {
        this.bonuses.add(bonus);
        bonus.setJob(this);
    }
    public void removeBonus(Bonus bonus){
        this.bonuses.remove(bonus);
    }

//    public void addUser(User user){
//        this.users.add(user);
//        user.setJob(this);
//    }
//    public void removeUser(User user){
//        this.users.remove(user);
//    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    public Float getMarginPercentage() {
        return marginPercentage;
    }

    public void setMarginPercentage(Float marginPercentage) {
        this.marginPercentage = marginPercentage;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public SubFacility getSubFacility() {
        return subFacility;
    }

    public void setSubFacility(SubFacility subFacility) {
        this.subFacility = subFacility;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<Bonus> bonuses) {
        this.bonuses = bonuses;
    }

    public Float getWorkerBonus() {
        return workerBonus;
    }

    public void setWorkerBonus(Float workerBonus) {
        this.workerBonus = workerBonus;
    }

    public Integer getAutoBonus() {
        return autoBonus;
    }

    public void setAutoBonus(Integer autoBonus) {
        this.autoBonus = autoBonus;
    }

    //    Сделать пару ключей связи таблиц пользователи и работы уникальной вручную
//    ALTER TABLE usr_jobs
//    ADD CONSTRAINT uq_usr_jobs
//    UNIQUE (usr_id, job_id);


    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
