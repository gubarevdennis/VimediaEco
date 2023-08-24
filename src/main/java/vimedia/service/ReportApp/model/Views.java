package vimedia.service.ReportApp.model;

public final class Views {
    public interface Id {} // маркер для отображения id
    public  interface IdName extends Id {}  // маркер для отображения name и id
    public interface FuLlFacility extends Id {} // маркер для отображения остальных полей и id
}
