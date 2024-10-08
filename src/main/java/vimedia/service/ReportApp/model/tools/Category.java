package vimedia.service.ReportApp.model.tools;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vimedia.service.ReportApp.model.report.Views;

import java.util.List;

@Entity
@Table
@ToString(of = {"id","name"})
@EqualsAndHashCode(of = {"id"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private int id;

    @JsonView(Views.IdName.class)
    @Column(unique=true)
    private String name;

    @JsonView(Views.IdName.class)
    private String text;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Tool> tools;

    public void addTool(Tool tool){
        this.tools.add(tool);
        tool.setCategory(this);
    }

    public void removeTool(Tool tool){
        this.tools.remove(tool);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }
}
