module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires ormlite.jdbc;
    requires java.sql;


    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;

    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson;
    opens at.ac.fhcampuswien.fhmdb.data to ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb.controllers to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.ui to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
}