package vimedia.service.ReportApp.model.report;

public final class Views {
    public interface Id {} // маркер для отображения id
    public  interface Event extends Id {}  // маркер для отображения event
    public  interface IdName extends Id {}  // маркер для отображения name и id
    public interface FuLlFacility extends Id {} // маркер для отображения остальных полей и id
}
