package carshow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController {
    private static MainController nst;

    public static MainController getIns() {
        return nst;
    }

    @FXML
    public CarShowroomContainer salons = new CarShowroomContainer();
    @FXML
    private TextField selectedModel;
    @FXML
    private Label selectedSalon;
    @FXML
    private ComboBox<String> salonsBox;
    @FXML
    private TableView<Vehicle> arrayOfVehicles;
    @FXML
    private TableColumn<Vehicle,String> model;
    @FXML
    private TableColumn<Vehicle,String> marka;
    @FXML
    private TableColumn<Vehicle, Double> cena;
    @FXML
    private TableColumn<Vehicle, Integer> rok_produkcji;
    @FXML
    private TableColumn<Vehicle,String> salon;
    @FXML
    private void refreshSelectedModel()
    {
        refreshTable();
        refreshNames();
    }
    @FXML
    private void loadSalon()
    {
        TextInputDialog textInputDialog = new TextInputDialog("");
        textInputDialog.setHeaderText("Wczytaj salon z pliku.");
        Optional<String> result = textInputDialog.showAndWait();

        result.ifPresent(filename -> {
            try {
                salons.addCenter(SerializeCarShow.deseralizeSalon(filename));
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Wczytano salon!");
                alert.show();
                try {
                    refreshTable();
                    refreshNames();
                    salonsBox.getSelectionModel().select("dowolny");
                } catch (RuntimeException e) {
                    System.err.println("¯\\_(ツ)_/¯");
                }

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Nie ma takiego pliku, sprawdz nazwę!");
                alert.show();
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ValueAlreadyExistException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Nie można dodać salonu o nazwie "+e.getMessage()+"!");
                alert.show();
            }
        });


//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Potwierdzić zakup: \{rowData.toString()} ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
//        alert.showAndWait();
//        if(alert.getResult() == ButtonType.YES) {
//            salons.buyCar(rowData.getSalon(), rowData.getMarka(), rowData.getModel());
//            refreshTable();
//
//        }
    }
    private void refreshTable()
    {
        arrayOfVehicles.getItems().removeAll(arrayOfVehicles.getItems());
        ArrayList<Vehicle> t = salons.getVehicles(selectedSalon.getText(),selectedModel.getText());
        t.sort(Comparator.comparing(Vehicle::getMarka));
        arrayOfVehicles.getItems().addAll(t);

    }
    private void refreshNames()
    {
        salonsBox.getItems().removeAll(salonsBox.getItems());
        salonsBox.getItems().addAll(salons.getNames());
    }
    @FXML
    private void comboAction(ActionEvent event)
    {
        refreshTable();
    }
    @FXML
    public void initialize() {
        assert salonsBox != null : "fx:id=\"salonsBox\" was not injected: check your FXML file 'main-view.fxml'.";
        assert selectedSalon != null : "fx:id=\"selectedSalon\" was not injected: check your FXML file 'main-view.fxml'.";
        selectedSalon.textProperty().bind(salonsBox.getSelectionModel().selectedItemProperty());
        nst = this;
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        marka.setSortType(TableColumn.SortType.ASCENDING);
        marka.setCellValueFactory(new PropertyValueFactory<>("marka"));
        cena.setCellValueFactory(new PropertyValueFactory<>("cena"));
        rok_produkcji.setCellValueFactory(new PropertyValueFactory<>("rok_produkcji"));
        salon.setCellValueFactory(new PropertyValueFactory<>("salon"));


        salonsBox.getSelectionModel().select("dowolny");
        try {
            //System.out.println("KK");
            //salons.addCenter(SerializeCarShow.deseralizeSalon("Łódź.csv"));
            //salons.addCenter(SerializeCarShow.deseralizeSalon("Kraków.csv"));
            salons.loadSalons();
        }
        catch (Throwable e) {
            System.err.println(e.getMessage());
        }

        salons.summary();
        arrayOfVehicles.setRowFactory(tableView -> {
            final TableRow<Vehicle> tableRow = new TableRow<>() {
                    final Tooltip tooltip = new Tooltip();
                    @Override
                    public void updateItem(Vehicle vehicle, boolean empty) {
                        super.updateItem(vehicle, empty);
                        if (vehicle == null) {
                            setTooltip(null);
                        } else {
                            tooltip.setText("Pojemność silnika:"+  vehicle.getPojemnosc_silnika() +". Przebieg: " + vehicle.getPrzebieg()+"}");
                            setTooltip(tooltip);
                        }
                    }
            };
            tableRow.setOnMouseClicked( event -> {
                if(event.getClickCount() == 2 && !tableRow.isEmpty())
                {
                    switch (event.getButton()){
                        case MouseButton.PRIMARY:
                            Vehicle rowData = tableRow.getItem();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Potwierdzić zakup: "+rowData.toString() +" ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                            alert.showAndWait();
                            if(alert.getResult() == ButtonType.YES) {
                                salons.buyCar(rowData.getNazwa_salonu(), rowData.getMarka(), rowData.getModel());
                                refreshTable();

                            }
                            break;
                        case MouseButton.SECONDARY:
                            break;
                        default:
                            break;

                    }
                    //rowData.print();
                }
            });
            return tableRow;
        });

        refreshNames();
        refreshTable();
    }
}
