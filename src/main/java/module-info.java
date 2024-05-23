module carshow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires opencsv;
    requires org.apache.commons.lang3;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens carshow to javafx.fxml, org.hibernate.orm.core;
    exports carshow;
}