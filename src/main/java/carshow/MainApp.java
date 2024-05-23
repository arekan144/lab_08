package carshow;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class MainApp extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        URL locale =MainApp.class.getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(locale);

        Parent root  = fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Lab_06: carshow.");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop(){
        System.out.println("Stage is closing");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Czy zapisać zmodyfikowane dane?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            for(CarShowroom v: MainController.getIns().salons.getSalons())
            {
                System.out.println("K");
                try {
                    DatabaseController.updateDB(v);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }

        }


        // Save file
    }
    public static void main(String[] args) {
        launch();
    }
}

