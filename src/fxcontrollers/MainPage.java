package fxcontrollers;

import hib.UserHibernateController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import model.Driver;
import utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPage implements Initializable {
    @FXML
    public ListView<Truck> availableTruckList;
    @FXML
    public TextField modelField;
    @FXML
    public TextField yearOfManufactureField;
    @FXML
    public TextField odometerField;
    @FXML
    public TextField tankCapacityField;
    @FXML
    public TextField weightField;
    @FXML
    public ChoiceBox<GearboxType> gearboxTypeField;
    @FXML
    public ChoiceBox<TyreType> tyreTypeField;
    @FXML
    public Tab userManagementTab;
    @FXML
    public TabPane tabPane;
    @FXML
    public Button newTruckButton;
    @FXML
    public TableView<UserTableParameters> tableOfUsers;
    @FXML
    public TableColumn<UserTableParameters, Integer> idColumn;
    @FXML
    public TableColumn<UserTableParameters, String> loginColumn;
    @FXML
    public TableColumn<UserTableParameters, String> passwordColumn;
    @FXML
    public TableColumn<UserTableParameters, String> nameColumn;
    @FXML
    public TableColumn<UserTableParameters, String> surnameColumn;
    @FXML
    public TableColumn<UserTableParameters, String> phoneNrColumn;
    @FXML
    public TextField cargoWeightField;
    @FXML
    public ChoiceBox<CargoType> cargoTypeChoice;
    @FXML
    public CheckBox isFragileCheck;
    @FXML
    public TextField cargoTitleField;
    @FXML
    public ListView<Cargo> availableCargoList;
    @FXML
    public Button newCargoButton;
    @FXML
    public ChoiceBox<Driver> responsibleDriver;
    @FXML
    public ListView<Manager> responsibleManagerList;
    @FXML
    public ChoiceBox<Cargo> assignedCargo;
    @FXML
    public ChoiceBox<Truck> assignedTruck;
    @FXML
    public TextField startCityField;
    @FXML
    public TextField endCityField;
    @FXML
    public TextField startCityLongitudeField;
    @FXML
    public TextField startCityLatitudeField;
    @FXML
    public TextField endCityLongitudeField;
    @FXML
    public TextField endCityLatitudeField;
    @FXML
    public ListView<Checkpoint> checkpointList;
    @FXML
    public CheckBox longStopCheck;
    @FXML
    public TextField checkpointCityField;
    @FXML
    public TableView<DestinationTableParameters> tableOfDestinations;
    @FXML
    public TableColumn<DestinationTableParameters, Integer> destinationId;
    @FXML
    public TableColumn<DestinationTableParameters, String> startCityColumn;
    @FXML
    public TableColumn<DestinationTableParameters, String> endCityColumn;
    @FXML
    public TableColumn<DestinationTableParameters, Driver> driverColumn;
    @FXML
    public TableColumn<DestinationTableParameters, Truck> truckColumn;
    @FXML
    public TableColumn actionField;
    @FXML
    public TableColumn dummyField;


    private User user;
    private Truck selectedTruck;
    private Cargo selectedCargo;
    private ObservableList<UserTableParameters> observableUsers = FXCollections.observableArrayList();
    private ObservableList<DestinationTableParameters> observableDestinations = FXCollections.observableArrayList();
    private EntityManagerFactory entityManagerFactory;
    private UserHibernateController userHibernateController;

    public void setInfo(User user, EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.user = user;
        userHibernateController = new UserHibernateController(this.entityManagerFactory);
        loadInfo(user);
        loadDestinations();
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ForumPage.class.getResource("../fxml/forum-page.fxml"));
        Parent parent = fxmlLoader.load();
        ForumPage forumPage = fxmlLoader.getController();
        forumPage.setInfo(user, this.entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Logistics");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gearboxTypeField.getItems().setAll(GearboxType.values());
        tyreTypeField.getItems().setAll(TyreType.values());
        cargoTypeChoice.getItems().setAll(CargoType.values());
        tableOfUsers.setEditable(true);
        //Id visible but not editable
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        //Login visible and editable
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        loginColumn.setOnEditCommit(
                t -> handleEditCommit(t, "login")
        );
        //Password visible and editable
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(
                t -> handleEditCommit(t, "password")
        );
        //Name visible and editable
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
                t -> handleEditCommit(t, "name")
        );
        //Surname visible and editable
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(
                t -> handleEditCommit(t, "surname")
        );
        //Phone number visible and editable
        phoneNrColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNrColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNrColumn.setOnEditCommit(
                t -> handleEditCommit(t, "phoneNumber")
        );

        Callback<TableColumn<UserTableParameters, Void>, TableCell<UserTableParameters, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<UserTableParameters, Void> call(final TableColumn<UserTableParameters, Void> param) {
                final TableCell<UserTableParameters, Void> cell = new TableCell<>() {
                    private final Button button = new Button("Delete");

                    {
                        button.setOnAction((ActionEvent event) -> {
                            UserTableParameters data = getTableView().getItems().get(getIndex());
                            userHibernateController.delete(userHibernateController.getEntityById(User.class, data.getUserId()));
                            loadUsers();
                        });
                        button.setStyle("-fx-background-color:  #eea4a7");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
                return cell;
            }
        };
        dummyField.setCellFactory(cellFactory);
    }

    private void handleEditCommit(TableColumn.CellEditEvent<UserTableParameters, String> t, String propertyName) {
        UserTableParameters row = t.getTableView().getItems().get(t.getTablePosition().getRow());
        row.setProperty(propertyName, t.getNewValue());
        User user = userHibernateController.getEntityById(User.class, row.getUserId());
        user.setProperty(propertyName, t.getNewValue());
        userHibernateController.update(user);
    }

    public void clearFields(TextField[] fieldsToClear, ChoiceBox[] choiceBoxesToClear) {
        for (TextField field : fieldsToClear) {
            field.clear();
        }
        for (ChoiceBox box : choiceBoxesToClear)
            box.getSelectionModel().clearSelection();
    }

    public boolean anyFieldsEmpty(TextField[] fields, ChoiceBox[] choiceBoxes) {
        boolean isEmpty = false;
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                isEmpty = true;
                field.setStyle("-fx-text-box-border: #B22222;");
            } else {
                field.setStyle("-fx-text-box-border: #999292;");
            }
        }
        for (ChoiceBox box : choiceBoxes) {
            if (box.getSelectionModel().isEmpty()) {
                isEmpty = true;
                box.setStyle("-fx-text-box-border: #B22222;");
            } else {
                box.setStyle("-fx-text-box-border: #999292;");
            }
        }
        return isEmpty;
    }

    private void disableVisuals() {
        if (user.getClass() == Driver.class) {
            tabPane.getTabs().remove(userManagementTab);
        }
    }

    private void loadInfo(User user) {
        if (user.getClass() == Driver.class) {
            disableVisuals();
        } else {
            loadUsers();
        }
    }

    public void loadUsers() {
        tableOfUsers.getItems().clear();
        List<User> users = userHibernateController.getAllRecords(User.class);
        for (User u : users) {
            UserTableParameters userTableParameters = new UserTableParameters();
            userTableParameters.setUserId(u.getId());
            userTableParameters.setLogin(u.getLogin());
            userTableParameters.setPassword(u.getPassword());
            userTableParameters.setName(u.getName());
            userTableParameters.setSurname(u.getSurname());
            userTableParameters.setPhoneNumber(u.getPhoneNumber());
            observableUsers.add(userTableParameters);
        }
        tableOfUsers.setItems(observableUsers);
    }

    public void addNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationPage.class.getResource("../fxml/registration-page.fxml"));
        Parent parent = fxmlLoader.load();
        RegistrationPage registrationPage = fxmlLoader.getController();
        registrationPage.setAllowedToGoToLoginPage(false);
        registrationPage.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Logistics");
        stage.setScene(scene);
        stage.show();
    }

    public void selectTruck() {
        newTruckButton.setDisable(true);
        selectedTruck = availableTruckList.getSelectionModel().getSelectedItem();
        Truck truck = userHibernateController.getEntityById(Truck.class, selectedTruck.getId());
        modelField.setText(truck.getModel());
        yearOfManufactureField.setText(String.valueOf(truck.getYearManufactured()));
        odometerField.setText(String.valueOf(truck.getOdometer()));
        tankCapacityField.setText(String.valueOf(truck.getTankCapacity()));
        weightField.setText(String.valueOf(truck.getWeight()));
        gearboxTypeField.setValue(truck.getGearbox());
        tyreTypeField.setValue(truck.getTyreType());
    }

    public void createTruck() {
        TextField[] fields = {modelField, yearOfManufactureField, odometerField, tankCapacityField, weightField};
        ChoiceBox[] choiceBoxes = {tyreTypeField, gearboxTypeField};
        if (anyFieldsEmpty(fields, choiceBoxes)) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Truck registration", "Error", "Please fill all fields");
        } else {
            try {
                Truck newTruck = new Truck(modelField.getText(), Integer.parseInt(yearOfManufactureField.getText()), Double.parseDouble(odometerField.getText()), Double.parseDouble(tankCapacityField.getText()), Double.parseDouble(weightField.getText()), gearboxTypeField.getValue(), tyreTypeField.getValue());
                userHibernateController.save(newTruck);
                clearFields(fields, choiceBoxes);
                loadTrucks();
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Truck registration", "Error", "Please enter the correct type of values");
            }
        }
    }
    public void loadTrucks() {
        availableTruckList.getItems().clear();
        List<Truck> truckList = userHibernateController.getAllRecords(Truck.class);
        ObservableList<Truck> observableTruckList = FXCollections.observableArrayList();
        observableTruckList.addAll(truckList);
        availableTruckList.setItems(observableTruckList);
    }

    public void updateTruck() {
        selectedTruck.setModel(modelField.getText());
        selectedTruck.setYearManufactured(Integer.parseInt(yearOfManufactureField.getText()));
        selectedTruck.setOdometer(Double.parseDouble(odometerField.getText()));
        selectedTruck.setTankCapacity(Double.parseDouble(tankCapacityField.getText()));
        selectedTruck.setWeight(Double.parseDouble(weightField.getText()));
        selectedTruck.setGearbox(gearboxTypeField.getValue());
        selectedTruck.setTyreType(tyreTypeField.getValue());
        userHibernateController.update(selectedTruck);
        availableTruckList.refresh();
    }

    public void deleteTruck() {
        availableTruckList.getItems().remove(selectedTruck);
        userHibernateController.delete(userHibernateController.getEntityById(Truck.class, selectedTruck.getId()));
    }

    public void pressedOnTruckAnchorPane() {
        TextField[] fields = {modelField, yearOfManufactureField, odometerField, tankCapacityField, weightField};
        ChoiceBox[] choiceBoxes = {tyreTypeField, gearboxTypeField};
        clearFields(fields, choiceBoxes);
        if (newTruckButton.isDisabled())
            newTruckButton.setDisable(false);
    }

    public void truckTabPressed() {
        loadTrucks();
    }

    public void loadCargo(){
        availableCargoList.getItems().clear();
        List<Cargo> cargoList = userHibernateController.getAllRecords(Cargo.class);
        ObservableList<Cargo> observableCargoList = FXCollections.observableArrayList();
        observableCargoList.addAll(cargoList);
        availableCargoList.setItems(observableCargoList);}

    public void selectCargo() {
        newCargoButton.setDisable(true);
        selectedCargo = availableCargoList.getSelectionModel().getSelectedItem();
        Cargo cargo = userHibernateController.getEntityById(Cargo.class, selectedCargo.getId());
        cargoTypeChoice.setValue(cargo.getCargoType());
        cargoTitleField.setText(cargo.getTitle());
        isFragileCheck.setSelected(cargo.isFragile());
        cargoWeightField.setText(Double.toString(cargo.getWeight()));
    }

    public void addCargo() {
        TextField[] fields = {cargoTitleField, cargoWeightField};
        ChoiceBox[] choiceBoxes = {cargoTypeChoice};
        if (anyFieldsEmpty(fields, choiceBoxes)) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Cargo registration", "Error", "Please fill all fields");
        } else {
            try {
                Cargo cargo = new Cargo(cargoTitleField.getText(), cargoTypeChoice.getValue(), Double.parseDouble(cargoWeightField.getText()), isFragileCheck.isSelected());
                userHibernateController.save(cargo);
                clearFields(fields, choiceBoxes);
                loadCargo();
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Cargo registration", "Error", "Please enter the correct type of values");
            }
        }
    }

    public void updateCargo() {
        selectedCargo.setTitle(cargoTitleField.getText());
        selectedCargo.setCargoType(cargoTypeChoice.getValue());
        selectedCargo.setWeight(Double.parseDouble(cargoWeightField.getText()));
        selectedCargo.setFragile(isFragileCheck.isSelected());
        userHibernateController.update(selectedCargo);
        loadCargo();
        availableCargoList.refresh();
    }

    public void deleteCargo() {
        userHibernateController.delete(userHibernateController.getEntityById(Cargo.class, selectedCargo.getId()));
        loadCargo();
    }

    public void pressedOnCargoAnchorPane() {
        TextField[] fields = {cargoTitleField, cargoWeightField};
        ChoiceBox[] choiceBoxes = {cargoTypeChoice};
        clearFields(fields, choiceBoxes);
        if (newCargoButton.isDisabled())
            newCargoButton.setDisable(false);
    }

    public void cargoTabPressed() {
        loadCargo();
    }

    public void destinationTabPressed() {
        // Retrieve data for responsibleManagerList
        List<Manager> managers = userHibernateController.getAllRecords(Manager.class);
        ObservableList<Manager> managerList = FXCollections.observableArrayList(managers);
        responsibleManagerList.setItems(managerList);

        // Retrieve data for assignedCargoList
        List<Cargo> assignedCargos = userHibernateController.getAllRecords(Cargo.class);
        assignedCargo.getItems().setAll(assignedCargos);

        // Retrieve data for responsibleDriver
        List<Driver> drivers = userHibernateController.getAllRecords(Driver.class);
        responsibleDriver.getItems().setAll(drivers);

        // Retrieve data for assignedTruck
        List<Truck> choiceOfTrucks = userHibernateController.getAllRecords(Truck.class);
        assignedTruck.getItems().setAll(choiceOfTrucks);

        // Initialize the destination table
        initializeDestinationTable();
    }
    public void addDestinationButton() {
        TextField[] fields = {startCityField, startCityLatitudeField, startCityLongitudeField, endCityField, endCityLatitudeField, endCityLongitudeField};
        ChoiceBox[] choiceBoxes = {assignedTruck, responsibleDriver, assignedCargo};
        if (anyFieldsEmpty(fields, choiceBoxes)) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "Error", "Please fill all fields");
        } else {
            try {
                Driver driver = userHibernateController.getEntityById(Driver.class, responsibleDriver.getValue().getId());
                if(driver.getMyDestinations() == null){
                    driver.setMyDestinations(new ArrayList<>());
                }
                Truck truck = userHibernateController.getEntityById(Truck.class,assignedTruck.getValue().getId());
                List<Manager> managers = new ArrayList<>(responsibleManagerList.getSelectionModel().getSelectedItems());
                List<Checkpoint> checkpoints = new ArrayList<>(checkpointList.getItems());
                Cargo cargo = userHibernateController.getEntityById(Cargo.class, assignedCargo.getValue().getId());
                Destination destination = new Destination(startCityField.getText(), Long.parseLong(startCityLongitudeField.getText()), Long.parseLong(startCityLatitudeField.getText()), Long.parseLong(endCityLongitudeField.getText()), Long.parseLong(endCityLatitudeField.getText()), endCityField.getText(), driver, managers, cargo, checkpoints, truck);
                userHibernateController.save(destination);
                driver.getMyDestinations().add(destination);
                userHibernateController.update(driver);
                truck.setCurrentDestination(destination);
                userHibernateController.update(truck);
                cargo.setDestination(destination);
                userHibernateController.update(cargo);
                for(Manager m:managers){
                    m = userHibernateController.getEntityById(Manager.class, m.getId());
                    if(m.getMyManagedDestination() == null){
                        m.setMyManagedDestination(new ArrayList<>());
                    }
                    m.getMyManagedDestination().add(destination);
                    userHibernateController.update(m);
                }
                for(Checkpoint ch:checkpoints){
                    ch.setDestination(destination);
                    userHibernateController.update(ch);
                }
                clearFields(fields, choiceBoxes);
                tableOfDestinations.getItems().clear();
                loadDestinations();
                checkpointList.getItems().clear();
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "Error", "Please enter the correct type of values");
            }
        }
    }

    public void addCheckpointButton() {
        if (checkpointCityField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Checkpoint", "Error", "Please enter checkpoint location");
        } else {
            try {
                Checkpoint checkpoint = new Checkpoint(checkpointCityField.getText(), longStopCheck.isSelected());
                checkpointList.getItems().add(checkpoint);
                checkpointCityField.clear();
                longStopCheck.setSelected(false);
                userHibernateController.save(checkpoint);
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Checkpoint", "Error", "Please enter the correct type of values");
            }
        }
    }

    private void initializeDestinationTable() {
        tableOfDestinations.setEditable(true);
        //Id visible but not editable
        destinationId.setCellValueFactory(new PropertyValueFactory<>("destinationId"));

        startCityColumn.setCellValueFactory(new PropertyValueFactory<>("startCity"));
        startCityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        startCityColumn.setOnEditCommit(
                t -> handleDestinationEditCommit(t, "startCity")
        );

        endCityColumn.setCellValueFactory(new PropertyValueFactory<>("endCity"));
        endCityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        endCityColumn.setOnEditCommit(
                t -> handleDestinationEditCommit(t, "endCity")
        );
        driverColumn.setCellValueFactory(cellData -> {
            DestinationTableParameters destination = cellData.getValue();
            Driver driver = destination.getDriver();
            return new SimpleObjectProperty<>(driver);
        });
        List<Driver> drivers = userHibernateController.getAllRecords(Driver.class);
        driverColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn((ObservableList<Driver>) FXCollections.observableArrayList(drivers)));
        driverColumn.setOnEditCommit(event -> {
            event.getRowValue().setDriver(event.getNewValue());
            Destination destination = userHibernateController.getEntityById(Destination.class, event.getRowValue().getDestinationId());
            destination.setDriver(event.getNewValue());
            userHibernateController.update(destination);
        });

        truckColumn.setCellValueFactory(cellData -> {
            DestinationTableParameters destination = cellData.getValue();
            Truck truck = destination.getTruck();
            return new SimpleObjectProperty<>(truck);
        });
        List<Truck> trucks = userHibernateController.getAllRecords(Truck.class);
        truckColumn.setCellFactory(ComboBoxTableCell.forTableColumn((ObservableList<Truck>) FXCollections.observableArrayList(trucks)));
        truckColumn.setOnEditCommit(event -> {
            event.getRowValue().setTruck(event.getNewValue());
            Destination destination = userHibernateController.getEntityById(Destination.class, event.getRowValue().getDestinationId());
            destination.setTruck(event.getNewValue());
            userHibernateController.update(destination);
        });

        Callback<TableColumn<DestinationTableParameters, Void>, TableCell<DestinationTableParameters, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<DestinationTableParameters, Void> call(final TableColumn<DestinationTableParameters, Void> param) {
                final TableCell<DestinationTableParameters, Void> cell = new TableCell<>() {
                    private final Button button = new Button("Delete");

                    {
                        button.setOnAction((ActionEvent event) -> {
                            DestinationTableParameters data = getTableView().getItems().get(getIndex());
                            userHibernateController.delete(userHibernateController.getEntityById(User.class, data.getDestinationId()));
                            loadDestinations();
                        });
                        button.setStyle("-fx-background-color: #eea4a7");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
                return cell;
            }
        };
        actionField.setCellFactory(cellFactory);
    }

    private void loadDestinations() {
        tableOfDestinations.getItems().clear();
        List<Destination> destinations = userHibernateController.getAllRecords(Destination.class);
        if (destinations != null) {
            for (Destination d : destinations) {
                DestinationTableParameters destinationTableParameters = new DestinationTableParameters();
                destinationTableParameters.setDestinationId(d.getId());
                destinationTableParameters.setStartCity(d.getStartCity());
                destinationTableParameters.setEndCity(d.getEndCity());
                destinationTableParameters.setTruck(d.getTruck());
                destinationTableParameters.setDriver(d.getDriver());
                observableDestinations.add(destinationTableParameters);
            }
            tableOfDestinations.setItems(observableDestinations);
        }
    }

    private void handleDestinationEditCommit(TableColumn.CellEditEvent<DestinationTableParameters, String> t, String propertyName) {
        DestinationTableParameters row = t.getTableView().getItems().get(t.getTablePosition().getRow());
        row.setProperty(propertyName, t.getNewValue());
        Destination destination = userHibernateController.getEntityById(Destination.class, row.getDestinationId());
        destination.setProperty(propertyName, t.getNewValue());
        userHibernateController.update(destination);
    }
}
