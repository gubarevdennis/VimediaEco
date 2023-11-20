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
@ToString(of = {"id","text"})
@EqualsAndHashCode(of = {"id"})
public class InventoryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private int id;

    @JsonView(Views.IdName.class)
    @NotNull
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING)//, pattern = "HH:mm:ss dd.MM.yy")
    @JsonView(Views.IdName.class)
    private LocalDateTime dateAndTime;

    @JsonView(Views.IdName.class)
    private String image;

    @ManyToOne
    @JsonIgnoreProperties({"facility", "user", "toolSet", "inventoryEvents"})
    @JsonView(Views.IdName.class)
    private Tool tool;

    @ManyToOne
    @JsonIgnoreProperties({"events", "reports", "password", "tools"})
    @JsonView(Views.IdName.class)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
