package vimedia.service.ReportApp.model.tools;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.report.Views;

import java.time.LocalDateTime;

@Entity
@Table
@ToString(of = {"id","name"})
@EqualsAndHashCode(of = {"id"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private int id;

    @JsonView(Views.IdName.class)
    @NotNull
    private String name;

    @JsonView(Views.IdName.class)
    @Column(unique=true)
    private String image;

    @JsonView(Views.IdName.class)
    private String fromFacility;

    @JsonView(Views.IdName.class)
    private String toFacility;

    @JsonView(Views.IdName.class)
    private String toUser;

    @JsonView(Views.IdName.class)
    private String comment;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.IdName.class)
    private LocalDateTime eventTimeAndDate;

    @ManyToOne
    @JsonIgnoreProperties({"events", "reports", "password", "tools"})
    @JsonView(Views.IdName.class)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties({"facility", "user", "toolSet", "events"})
    @JsonView(Views.IdName.class)
    private Tool tool;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFromFacility() {
        return fromFacility;
    }

    public void setFromFacility(String fromFacilityId) {
        this.fromFacility = fromFacilityId;
    }

    public String getToFacility() {
        return toFacility;
    }

    public void setToFacility(String toFacilityId) {
        this.toFacility = toFacilityId;
    }

    public LocalDateTime getEventTimeAndDate() {
        return eventTimeAndDate;
    }

    public void setEventTimeAndDate(LocalDateTime eventTimeAndDate) {
        this.eventTimeAndDate = eventTimeAndDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
