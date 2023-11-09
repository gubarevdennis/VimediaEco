package vimedia.service.ReportApp.model.tools;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vimedia.service.ReportApp.model.report.*;
import vimedia.service.ReportApp.model.report.Views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table
@ToString(of = {"id","name"})
@EqualsAndHashCode(of = {"id"})
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private int id;

    @JsonView(Views.IdName.class)
    @NotNull
    private String name;

    @JsonView(Views.IdName.class)
    private String vendor;

    @JsonView(Views.IdName.class)
    private String article;

    @JsonView(Views.IdName.class)
    private Integer quantity;

    @JsonView(Views.IdName.class)
    @Column(unique=true)
    private String serial;

    @JsonView(Views.IdName.class)
//    @Column(unique=true)
    private String image;

    @JsonView(Views.IdName.class)
    private String status;

    @JsonView(Views.IdName.class)
    private String price;

    @JsonView(Views.IdName.class)
    private String provider;

    @JsonView(Views.IdName.class)
    private String paymentInfo;

    @JsonView(Views.IdName.class)
    private String servicePeriod;

    @JsonView(Views.IdName.class)
    private String inventoryPeriod;

    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private String composition;

    @JsonView(Views.IdName.class)
    private String description;

    @JsonView(Views.IdName.class)
    private String category;


//    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yy")  //  HH:mm:ss
    @JsonView(Views.IdName.class)
    private LocalDate purchaseDate;

    @ManyToOne
    @JsonIgnoreProperties({"tools", "subFacilities"})
    @JsonView(Views.IdName.class)
    private Facility facility;

    @ManyToOne
    @JsonView(Views.IdName.class)
    @JsonIgnoreProperties({"tools", "reports", "password"})
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("tools")
    @JsonView(Views.IdName.class)
    private ToolSet toolSet;

    @OneToMany(mappedBy = "tool")
    @JsonIgnore
    @JsonView(Views.IdName.class)
    private List<Event> events;

    public void addTool(Event event){
        this.events.add(event);
        event.setTool(this);
    }

    public void removeTool(Event event){
        this.events.remove(event);
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

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServicePeriod() {
        return servicePeriod;
    }

    public void setServicePeriod(String servicePeriod) {
        this.servicePeriod = servicePeriod;
    }

    public String getInventoryPeriod() {
        return inventoryPeriod;
    }

    public void setInventoryPeriod(String inventoryPeriod) {
        this.inventoryPeriod = inventoryPeriod;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public ToolSet getToolSet() {
        return toolSet;
    }

    public void setToolSet(ToolSet toolSet) {
        this.toolSet = toolSet;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
