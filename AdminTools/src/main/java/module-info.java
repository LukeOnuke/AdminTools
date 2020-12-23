module app.admintools {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;

    requires rkon.core;
    requires simplefxdialog;
    requires gson;
    requires discord.rpc;

    requires java.logging;

    opens app.admintools;

    //Gives classpath accses to javafx.fxml
    exports app.admintools.gui to javafx.fxml;

    //For private accses to libraries
    opens app.admintools.gui to javafx.fxml;
    opens app.admintools.security.credentials to gson;
    opens app.admintools.querry.mc to gson;
}