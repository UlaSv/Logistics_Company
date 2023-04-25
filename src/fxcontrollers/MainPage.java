package fxcontrollers;

import fxmlTableParameters.DestinationTableParameters;
import fxmlTableParameters.DriverTableParameters;
import fxmlTableParameters.ManagerTableParameters;
import fxmlTableParameters.UserTableParameters;
import hib.HibernateController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import model.Driver;
import utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

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
    public TableColumn<UserTableParameters, Integer> userIdColumn;
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
    public Button newCheckpointButton;
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
    public TableColumn<DestinationTableParameters, Cargo> cargoColumn;
    @FXML
    public TableColumn<DestinationTableParameters, StatusType> orderStatusColumn;
    @FXML
    public TableColumn actionField;
    @FXML
    public TableColumn dummyField;
    @FXML
    public Button updateCargoButton;
    @FXML
    public Button deleteCargoButton;
    @FXML
    public Button updateTruckButton;
    @FXML
    public Button deleteTruckButton;
    @FXML
    public SplitPane splitPaneDestination;
    @FXML
    public Tab truckManagementTab;
    @FXML
    public Tab cargoManagementTab;
    @FXML
    public BarChart<String, Integer> destinationChart;
    @FXML
    public CategoryAxis chartCategoryAxis;
    @FXML
    public NumberAxis chartNumberAxis;
    @FXML
    public Label directionsForDriver;
    @FXML
    public Label directionsForManager;
    @FXML
    public TableView<DriverTableParameters> tableOfDrivers;
    @FXML
    public TableColumn<DriverTableParameters, Integer> driverIdColumn;
    @FXML
    public TableColumn<DriverTableParameters, String> driversLicenseColumn;
    @FXML
    public TableColumn<DriverTableParameters, String> emergencyNrColumn;
    @FXML
    public TableColumn<DriverTableParameters, String> medInsuranceColumn;
    @FXML
    public TableColumn<DriverTableParameters, LocalDate> medDateColumn;
    @FXML
    public TableView<ManagerTableParameters> tableOfManagers;
    @FXML
    public TableColumn<ManagerTableParameters, Integer> managerIdColumn;
    @FXML
    public TableColumn<ManagerTableParameters, String> emailColumn;
    @FXML
    public TableColumn<ManagerTableParameters, LocalDate> employmentDateColumn;
    @FXML
    public TableColumn<ManagerTableParameters, Boolean> isAdminColumn;
    @FXML
    public Label directionsForAdmin;


    private User user;
    private Truck selectedTruck;
    private Cargo selectedCargo;
    private Checkpoint selectedCheckpoint;
    private ObservableList<UserTableParameters> observableUsers = FXCollections.observableArrayList();
    private ObservableList<DestinationTableParameters> observableDestinations = FXCollections.observableArrayList();
    private ObservableList<DriverTableParameters> observableDrivers = FXCollections.observableArrayList();
    private ObservableList<ManagerTableParameters> observableManagers = FXCollections.observableArrayList();
    private EntityManagerFactory entityManagerFactory;
    private HibernateController hibernateController;


    public void setInfo(User user, EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.user = user;
        hibernateController = new HibernateController(this.entityManagerFactory);
        disableVisuals();
        loadDestinations();
        destinationChartInitialize();
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
        responsibleManagerList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        gearboxTypeField.getItems().setAll(GearboxType.values());
        tyreTypeField.getItems().setAll(TyreType.values());
        cargoTypeChoice.getItems().setAll(CargoType.values());
        directionsForDriver.setVisible(false);
        directionsForManager.setVisible(false);
        directionsForAdmin.setVisible(false);
        initializeUserTable();
        initializeDriverTable();
        initializeManagerTable();
    }

    public void initializeUserTable() {
        tableOfUsers.setEditable(true);
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        loginColumn.setOnEditCommit(
                t -> handleEditCommit(t, "login")
        );
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(
                t -> handleEditCommit(t, "password")
        );
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
                t -> handleEditCommit(t, "name")
        );
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(
                t -> handleEditCommit(t, "surname")
        );
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
                            removeUser(data.getUserId());
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
        User currentUser = user;
        if (((Manager) user).getIsAdmin() || currentUser.getId() == row.getUserId()) {
            row.setProperty(propertyName, t.getNewValue());
            User user = hibernateController.getEntityById(User.class, row.getUserId());
            user.setProperty(propertyName, t.getNewValue());
            hibernateController.update(user);
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "", "You are not authorized to edit this user's data.", "");
            loadUsers();
        }
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
                field.setStyle("-fx-border-color:  #B22222;");
            } else {
                field.setStyle("-fx-border-color: #999292;");
            }
        }
        for (ChoiceBox box : choiceBoxes) {
            if (box.getSelectionModel().isEmpty()) {
                isEmpty = true;
                box.setStyle("-fx-border-color: #B22222;");
            } else {
                box.setStyle("-fx-border-color: #999292;");
            }
        }
        return isEmpty;
    }

    private void disableVisuals() {
        if (user.getClass() == Driver.class) {
            tabPane.getTabs().remove(userManagementTab);
            tabPane.getTabs().remove(cargoManagementTab);
            tabPane.getTabs().remove(truckManagementTab);
            splitPaneDestination.setDividerPositions(1, 1);
            actionField.setVisible(false);
            tableOfDestinations.setPrefWidth(500);
            tableOfDestinations.setLayoutX(40);
            directionsForDriver.setVisible(true);
            hideNonAdminMethods();
        } else if (user.getClass() == Manager.class && !((Manager) user).getIsAdmin()) {
            directionsForManager.setVisible(true);
            tableOfUsers.getColumns().remove(dummyField);
            tableOfUsers.setPrefWidth(470);
            hideNonAdminMethods();
            loadUsers();
        } else {
            updateTruckButton.setVisible(true);
            updateCargoButton.setVisible(true);
            deleteCargoButton.setVisible(true);
            deleteTruckButton.setVisible(true);
            directionsForAdmin.setVisible(true);
        }
    }

    private void hideNonAdminMethods() {
        updateTruckButton.setVisible(false);
        updateCargoButton.setVisible(false);
        deleteCargoButton.setVisible(false);
        deleteTruckButton.setVisible(false);
    }

    public void loadUsers() {
        tableOfUsers.getItems().clear();
        List<User> users = hibernateController.getAllRecords(User.class);
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
        Truck truck = hibernateController.getEntityById(Truck.class, selectedTruck.getId());
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
                hibernateController.save(newTruck);
                clearFields(fields, choiceBoxes);
                loadTrucks();
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Truck registration", "Error", "Please enter the correct type of values");
            }
        }
    }

    public void loadTrucks() {
        availableTruckList.getItems().clear();
        List<Truck> truckList = hibernateController.getAllRecords(Truck.class);
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
        hibernateController.update(selectedTruck);
        availableTruckList.refresh();
    }

    public void deleteTruck() {
        availableTruckList.getItems().remove(selectedTruck);
        hibernateController.delete(hibernateController.getEntityById(Truck.class, selectedTruck.getId()));
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

    public void loadCargo() {
        availableCargoList.getItems().clear();
        List<Cargo> cargoList = hibernateController.getAllRecords(Cargo.class);
        ObservableList<Cargo> observableCargoList = FXCollections.observableArrayList();
        observableCargoList.addAll(cargoList);
        availableCargoList.setItems(observableCargoList);
    }

    public void selectCargo() {
        newCargoButton.setDisable(true);
        selectedCargo = availableCargoList.getSelectionModel().getSelectedItem();
        Cargo cargo = hibernateController.getEntityById(Cargo.class, selectedCargo.getId());
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
                hibernateController.save(cargo);
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
        hibernateController.update(selectedCargo);
        loadCargo();
        availableCargoList.refresh();
    }

    public void deleteCargo() {
        hibernateController.delete(hibernateController.getEntityById(Cargo.class, selectedCargo.getId()));
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
        initializeDestinationTable();
        loadDestinations();

    }

    public void refreshChoices() {
        List<Manager> managers = hibernateController.getAllRecords(Manager.class);
        ObservableList<Manager> managerList = FXCollections.observableArrayList(managers);
        responsibleManagerList.setItems(managerList);
        responsibleManagerList.getSelectionModel().clearSelection();
        List<Driver> drivers = hibernateController.getAllRecords(Driver.class);
        responsibleDriver.getItems().setAll(drivers);
        checkpointList.getItems().clear();
        responsibleDriver.getSelectionModel().clearSelection();
        assignedTruck.getSelectionModel().clearSelection();
        assignedCargo.getSelectionModel().clearSelection();
        freeCargoAndTrucks();
        initializeDestinationTable();
    }

    private void freeCargoAndTrucks() {
        List<Truck> choiceOfTrucks = hibernateController.getAllTrucksThatAreNotTaken();
        if (choiceOfTrucks != null) {
            assignedTruck.getItems().setAll(choiceOfTrucks);
            assignedTruck.setDisable(false);
        } else {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Truck", "All trucks are assigned to destinations. Please wait or add new trucks", "for manager: add new truck");
            assignedTruck.setDisable(true);
        }
        List<Cargo> assignedCargos = hibernateController.getAllCargoThatIsNotTaken();
        if (assignedCargos != null) {
            assignedCargo.getItems().setAll(assignedCargos);
            assignedCargo.setDisable(false);

        } else {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Cargo", "All cargos are being delivered or have been delivered. Please wait until new cargo arrives", "for manager: add new cargo");
            assignedCargo.setDisable(true);
        }
    }

    public void addDestination() {
        TextField[] fields = {startCityField, startCityLatitudeField, startCityLongitudeField, endCityField, endCityLatitudeField, endCityLongitudeField};
        ChoiceBox[] choiceBoxes = {assignedCargo};
        if (anyFieldsEmpty(fields, choiceBoxes) || responsibleManagerList.getSelectionModel().isEmpty() || checkpointList.getItems().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "Error", "Please fill all fields");
        } else {
            try {
                Driver driver = null;
                if (responsibleDriver.getValue() != null) {
                    driver = hibernateController.getEntityById(Driver.class, responsibleDriver.getValue().getId());
                    if (driver.getMyDestinations() == null) {
                        driver.setMyDestinations(new ArrayList<>());
                    }
                }
                Truck truck = null;
                if (assignedTruck.getValue() != null) {
                    truck = hibernateController.getEntityById(Truck.class, assignedTruck.getValue().getId());
                }
                List<Manager> managers = new ArrayList<>(responsibleManagerList.getSelectionModel().getSelectedItems());
                if (user.getClass() == Manager.class && !managers.contains(user)) {
                    managers.add((Manager) user);
                }
                List<Checkpoint> checkpoints = new ArrayList<>(hibernateController.getUnassignedCheckpoints());
                Cargo cargo = hibernateController.getEntityById(Cargo.class, assignedCargo.getValue().getId());
                Destination destination = new Destination(
                        startCityField.getText(),
                        Long.parseLong(startCityLongitudeField.getText()),
                        Long.parseLong(startCityLatitudeField.getText()),
                        Long.parseLong(endCityLongitudeField.getText()),
                        Long.parseLong(endCityLatitudeField.getText()),
                        endCityField.getText(),
                        driver,
                        managers,
                        cargo,
                        checkpoints,
                        truck
                );
                hibernateController.save(destination);
                if (driver != null) {
                    driver.getMyDestinations().add(destination);
                    if (truck != null) {
                        driver.getMyOwnedTrucks().add(truck);
                    }
                    hibernateController.update(driver);
                }
                else{
                    destination.setStatus(StatusType.WAITING_FOR_INFO);
                }
                if (truck != null) {
                    truck.setCurrentDestination(destination);
                    if (driver != null) {
                        truck.setOwner(driver);
                    }
                    hibernateController.update(truck);
                }
                else{
                    destination.setStatus(StatusType.WAITING_FOR_INFO);
                }
                cargo.setDestination(destination);
                hibernateController.update(cargo);
                for (Manager m : managers) {
                    m = hibernateController.getEntityById(Manager.class, m.getId());
                    if (m.getMyManagedDestination() == null) {
                        m.setMyManagedDestination(new ArrayList<>());
                    }
                    m.getMyManagedDestination().add(destination);
                    hibernateController.update(m);
                }
                for (Checkpoint ch : checkpoints) {
                    ch.setDestination(destination);
                    hibernateController.update(ch);
                }
                clearFields(fields, choiceBoxes);
                loadDestinations();
                refreshChoices();

            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "Error", "Please enter the correct type of values");
            }
        }
    }

    public void addCheckpoint() {
        if (checkpointCityField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Checkpoint", "Error", "Please enter checkpoint location");
        } else {
            try {
                Checkpoint checkpoint = new Checkpoint(checkpointCityField.getText(), longStopCheck.isSelected());
                checkpointCityField.clear();
                longStopCheck.setSelected(false);
                hibernateController.save(checkpoint);
                checkpointList.getItems().add(checkpoint);
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Checkpoint", "Error", "Please enter the correct type of values");
            }
        }
    }

    public void checkpointSelected() {
        newCheckpointButton.setDisable(true);
        selectedCheckpoint = checkpointList.getSelectionModel().getSelectedItem();
        Checkpoint checkpoint = hibernateController.getEntityById(Checkpoint.class, selectedCheckpoint.getId());
        checkpointCityField.setText(checkpoint.getPlace());
        longStopCheck.setSelected(checkpoint.isLongStop());
    }

    public void deleteCheckpoint() {
        hibernateController.delete(selectedCheckpoint);
        checkpointList.getItems().remove(selectedCheckpoint);
        checkpointCityField.clear();
        longStopCheck.setSelected(false);
        newCheckpointButton.setDisable(false);
    }

    public void updateCheckpoint() {
        selectedCheckpoint.setPlace(checkpointCityField.getText());
        selectedCheckpoint.setLongStop(longStopCheck.isSelected());
        hibernateController.update(selectedCheckpoint);
        checkpointList.refresh();
        checkpointCityField.clear();
        longStopCheck.setSelected(false);
        newCheckpointButton.setDisable(false);
    }

    private void initializeDestinationTable() {
        tableOfDestinations.setEditable(true);
        tableOfDestinations.getSortOrder().add(orderStatusColumn);
        orderStatusColumn.setSortType(TableColumn.SortType.ASCENDING);
        if (user.getClass() == Driver.class) {
            driverColumn.setEditable(true);
            truckColumn.setEditable(true);
            startCityColumn.setEditable(false);
            endCityColumn.setEditable(false);
            cargoColumn.setEditable(false);
            orderStatusColumn.setEditable(false);

        }
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
        List<Driver> drivers = hibernateController.getAllRecords(Driver.class);
        driverColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn((ObservableList<Driver>) FXCollections.observableArrayList(drivers)));
        driverColumn.setOnEditCommit(event -> {
            if (orderStatusColumn.getCellData(event.getRowValue()) != StatusType.DONE && orderStatusColumn.getCellData(event.getRowValue()) != StatusType.ON_ROAD) {
                Destination destination = hibernateController.getEntityById(Destination.class, event.getRowValue().getDestinationId());
                Driver newDriver = event.getNewValue();
                Driver oldDriver = destination.getDriver();
                if (oldDriver != newDriver) {
                    if (oldDriver != null) {
                        oldDriver.getMyDestinations().remove(destination);
                        hibernateController.update(oldDriver);
                    }
                    destination.setDriver(newDriver);
                    newDriver.getMyDestinations().add(destination);
                    hibernateController.update(newDriver);
                    hibernateController.update(destination);

                    Truck truck = destination.getTruck();
                    if (truck != null) {
                        if (truck.getOwner() != null) {
                            Driver oldTruckOwner = truck.getOwner();
                            oldTruckOwner.getMyOwnedTrucks().remove(truck);
                            hibernateController.update(oldTruckOwner);
                        }
                        truck.setOwner(newDriver);
                        newDriver.getMyOwnedTrucks().add(truck);
                        hibernateController.update(truck);
                        hibernateController.update(newDriver);
                    }
                }
            } else {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "No changes are allowed on a finished order.", "");
            }
        });

        truckColumn.setCellValueFactory(cellData -> {
            DestinationTableParameters destination = cellData.getValue();
            Truck truck = destination.getTruck();
            return new SimpleObjectProperty<>(truck);
        });

        List<Truck> trucks = hibernateController.getAllTrucksThatAreNotTaken();
        if (trucks != null) {
            truckColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn((ObservableList<Truck>) FXCollections.observableArrayList(trucks)));
            truckColumn.setOnEditCommit(event -> {
                if (orderStatusColumn.getCellData(event.getRowValue()) != StatusType.DONE && orderStatusColumn.getCellData(event.getRowValue()) != StatusType.ON_ROAD) {
                    DestinationTableParameters destination = event.getRowValue();
                    Truck newTruck = event.getNewValue();
                    Truck oldTruck = event.getOldValue();
                    Destination destinationEntity = hibernateController.getEntityById(Destination.class, destination.getDestinationId());
                    if (oldTruck != newTruck) {
                        if (oldTruck != null) {
                            oldTruck.setCurrentDestination(null);
                            oldTruck.setOwner(null);
                            hibernateController.update(oldTruck);
                        }
                        newTruck.setCurrentDestination(destinationEntity);
                        if (destination.getDriver() != null) {
                            Driver driver = hibernateController.getEntityById(Driver.class, destination.getDriver().getId());
                            newTruck.setOwner(driver);
                            driver.getMyOwnedTrucks().add(newTruck);
                            hibernateController.update(driver);
                        } else {
                            newTruck.setOwner(null);
                        }
                        destinationEntity.setTruck(newTruck);
                        hibernateController.update(newTruck);
                        hibernateController.update(destinationEntity);
                    }
                } else {
                    FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "No changes are allowed on a finished order.", "");
                }
            });
        }
        cargoColumn.setCellValueFactory(cellData -> {
            DestinationTableParameters destination = cellData.getValue();
            Cargo cargo = destination.getCargo();
            return new SimpleObjectProperty<>(cargo);
        });
        List<Cargo> cargo = hibernateController.getAllCargoThatIsNotTaken();
        if (cargo != null) {
            cargoColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn((ObservableList<Cargo>) FXCollections.observableArrayList(cargo)));
            cargoColumn.setOnEditCommit(event -> {
                if (orderStatusColumn.getCellData(event.getRowValue()) != StatusType.DONE && orderStatusColumn.getCellData(event.getRowValue()) != StatusType.ON_ROAD) {
                    DestinationTableParameters destination = event.getRowValue();
                    Cargo newCargo = event.getNewValue();
                    Cargo oldCargo = event.getOldValue();
                    Destination rowDestination = hibernateController.getEntityById(Destination.class, destination.getDestinationId());
                    rowDestination.setCargo(newCargo);
                    newCargo.setDestination(rowDestination);
                    oldCargo.setDestination(null);
                    hibernateController.update(rowDestination);
                    hibernateController.update(newCargo);
                    hibernateController.update(oldCargo);
                }
                else{
                    FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "No changes are allowed on a finished order.", "");
                }
            });
        }

        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(StatusType.values()));
        orderStatusColumn.setOnEditCommit(t -> {
            DestinationTableParameters row = t.getTableView().getItems().get(t.getTablePosition().getRow());
            row.setOrderStatus(hibernateController.getEntityById(Destination.class, row.getDestinationId()).getStatus());
            if (t.getNewValue() == StatusType.DONE) {
                Truck truck = row.getTruck();
                Driver driver = row.getDriver();
                truck.setCurrentDestination(null);
                truck.setOwner(null);
                driver.getMyOwnedTrucks().remove(truck);
                hibernateController.update(driver);
                hibernateController.update(truck);
                Destination destination = hibernateController.getEntityById(Destination.class, row.getDestinationId());
                destination.setStatus(t.getNewValue());
                destination.setTruck(null);
                hibernateController.update(destination);
            } else {
                Destination destination = hibernateController.getEntityById(Destination.class, row.getDestinationId());
                destination.setStatus(t.getNewValue());
                hibernateController.update(destination);
            }
            loadDestinations();
        });

        Callback<TableColumn<DestinationTableParameters, Void>, TableCell<DestinationTableParameters, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<DestinationTableParameters, Void> call(final TableColumn<DestinationTableParameters, Void> param) {
                final TableCell<DestinationTableParameters, Void> cell = new TableCell<>() {
                    private final Button button = new Button("Delete");

                    {
                        button.setOnAction((ActionEvent event) -> {
                            DestinationTableParameters data = getTableView().getItems().get(getIndex());
                            removeDestination(data.getDestinationId());
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
                            DestinationTableParameters data = getTableView().getItems().get(getIndex());
                            if (data.getOrderStatus().equals(StatusType.DONE) || (data.getOrderStatus().equals(StatusType.ON_ROAD))) {
                                setGraphic(null);
                            } else {
                                setGraphic(button);
                            }
                        }
                    }
                };
                return cell;
            }
        };
        actionField.setCellFactory(cellFactory);
        tableOfDestinations.setRowFactory(tv -> new TableRow<DestinationTableParameters>() {
            @Override
            protected void updateItem(DestinationTableParameters item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getOrderStatus() == StatusType.DONE) {
                    setStyle("-fx-background-color: #d8ecfd;");
                } else if (item.getOrderStatus() == StatusType.ON_ROAD) {
                    setStyle("-fx-background-color: #ddc3d8;");
                } else if (item.getOrderStatus() == StatusType.WAITING_FOR_INFO) {
                    setStyle("-fx-background-color: #e3dfd5;");
                } else if (item.getOrderStatus() == StatusType.READY) {
                    setStyle("-fx-background-color: #e6f3e6;");
                }
            }
        });
    }


    private void loadDestinations() {
        tableOfDestinations.getItems().clear();
        List<Destination> destinations = hibernateController.getAllRecords(Destination.class);
        if (destinations != null) {
            for (Destination d : destinations) {
                DestinationTableParameters destinationTableParameters = new DestinationTableParameters();
                destinationTableParameters.setDestinationId(d.getId());
                destinationTableParameters.setStartCity(d.getStartCity());
                destinationTableParameters.setEndCity(d.getEndCity());
                destinationTableParameters.setTruck(d.getTruck());
                destinationTableParameters.setDriver(d.getDriver());
                destinationTableParameters.setCargo(d.getCargo());
                destinationTableParameters.setOrderStatus(d.getStatus());
                observableDestinations.add(destinationTableParameters);
            }
            tableOfDestinations.setItems(observableDestinations);
            tableOfDestinations.getSortOrder().add(orderStatusColumn);
            orderStatusColumn.setSortType(TableColumn.SortType.ASCENDING);
        }
    }

    private void handleDestinationEditCommit(TableColumn.CellEditEvent<DestinationTableParameters, String> t, String propertyName) {
        DestinationTableParameters row = t.getTableView().getItems().get(t.getTablePosition().getRow());
        if (row.getOrderStatus().equals(StatusType.DONE) && row.getOrderStatus().equals(StatusType.ON_ROAD)) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination", "No changes are allowed on a finished order.", "");
        } else {
            row.setProperty(propertyName, t.getNewValue());
            Destination destination = hibernateController.getEntityById(Destination.class, row.getDestinationId());
            destination.setProperty(propertyName, t.getNewValue());
            hibernateController.update(destination);
        }
    }

    private void removeUser(int userId) {
        User user = hibernateController.getEntityById(User.class, userId);
        user.getMyForums().forEach(forum -> {
                    forum.setUser(null);
                    hibernateController.update(forum);
                }
        );
        user.getMyForums().clear();
        user.getMyComments().forEach(comment -> {
            comment.setUser(null);
            hibernateController.update(comment);
        });
        user.getMyComments().clear();
        if (user.getClass() == Driver.class) {
            Driver driver = (Driver) user;
            driver.getMyOwnedTrucks().forEach(truck -> {
                truck.setOwner(null);
                hibernateController.update(truck);
            });
            driver.getMyOwnedTrucks().clear();
            driver.getMyDestinations().forEach(destination -> {
                destination.setDriver(null);
                hibernateController.update(destination);
            });
            driver.getMyDestinations().clear();
        } else {
            Manager manager = (Manager) user;
            manager.getMyManagedDestination().forEach(destination -> {
                destination.getResponsibleManagers().remove(manager);
                hibernateController.update(destination);
                if (destination.getResponsibleManagers().isEmpty())
                    removeDestination(destination.getId());
            });
            manager.getMyManagedDestination().clear();
        }
        hibernateController.delete(user);
    }

    private void removeDestination(int destinationId) {
        Destination destination = hibernateController.getEntityById(Destination.class, destinationId);
        if (destination.getDriver() != null) {
            Driver driver = destination.getDriver();
            driver.getMyDestinations().remove(destination);
            driver.getMyOwnedTrucks().remove(destination.getTruck());
            destination.setDriver(null);
            hibernateController.update(driver);
        }
        if (destination.getCargo() != null) {
            Cargo cargo = destination.getCargo();
            cargo.setDestination(null);
            destination.setCargo(null);
            hibernateController.update(cargo);
        }

        List<Manager> managers = new ArrayList<>(destination.getResponsibleManagers());
        for (Manager manager : managers) {
            manager.getMyManagedDestination().remove(destination);
            destination.getResponsibleManagers().remove(manager);
            hibernateController.update(manager);
        }

        List<Checkpoint> checkpoints = new ArrayList<>(destination.getCheckpoints());
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.setDestination(null);
            destination.getCheckpoints().remove(checkpoint);
            hibernateController.update(checkpoint);
        }
        if (destination.getTruck() != null) {
            Truck truck = destination.getTruck();
            truck.setOwner(null);
            truck.setCurrentDestination(null);
            destination.setTruck(null);
            hibernateController.update(truck);
        }
        hibernateController.update(destination);
        hibernateController.delete(destination);
    }

    public void userTabPressed() {
        tableOfUsers.refresh();
    }

    public void destinationChartInitialize() {
        chartCategoryAxis.setLabel("Status Type");
        chartNumberAxis.setLabel("Number of Destinations");
        chartNumberAxis.setTickUnit(1);
        loadBarChart();
    }
    public void loadBarChart(){
        destinationChart.getData().clear();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Destination Status");
        Map<StatusType, Long> statusCounts = hibernateController.getDestinationStatusCounts();
        for (StatusType status : StatusType.values()) {
            int count = statusCounts.getOrDefault(status, 0L).intValue();
            series.getData().add(new XYChart.Data<>(status.toString(), count));
        }
        destinationChart.getData().add(series);
    }

    public void statisticsTabPressed() {
        loadBarChart();
    }

    public void updateUserTable() {
        initializeUserTable();
        loadUsers();
    }

    public void initializeDriverTable() {
        tableOfDrivers.setEditable(true);
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        driversLicenseColumn.setCellValueFactory(new PropertyValueFactory<>("driversLicenceNumber"));
        driversLicenseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        driversLicenseColumn.setOnEditCommit(
                t -> handleDriverEditCommit(t, "driversLicenceNumber")
        );
        emergencyNrColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyContactPhoneNumber"));
        emergencyNrColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emergencyNrColumn.setOnEditCommit(
                t -> handleDriverEditCommit(t, "emergencyContactPhoneNumber")
        );
        medInsuranceColumn.setCellValueFactory(new PropertyValueFactory<>("medicalInsurance"));
        medInsuranceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        medInsuranceColumn.setOnEditCommit(
                t -> handleDriverEditCommit(t, "medicalInsurance")
        );
        medDateColumn.setCellValueFactory(new PropertyValueFactory<>("medicalCheckDate"));
        //medDateColumn.setCellFactory(DatePicker);
        medDateColumn.setOnEditCommit(
                t -> handleDriverEditCommit(t, "medicalCheckDate")
        );
    }

    private void handleDriverEditCommit(TableColumn.CellEditEvent<DriverTableParameters, ?> t, String fieldName) {
        Driver driver = hibernateController.getEntityById(Driver.class, t.getRowValue().getDriverId());
        if (((Manager) user).getIsAdmin()) {
            if ("driversLicenceNumber".equals(fieldName)) {
                driver.setDriversLicenceNumber((String) t.getNewValue());
            } else if ("emergencyContactPhoneNumber".equals(fieldName)) {
                driver.setEmergencyContactPhoneNumber((String) t.getNewValue());
            } else if ("medicalInsurance".equals(fieldName)) {
                driver.setMedicalInsurance((String) t.getNewValue());
            } else if ("medicalCheckDate".equals(fieldName)) {
                LocalDate newDate = ((DatePicker) t.getNewValue()).getValue();
                driver.setMedicalCheckDate(newDate);
            }
            hibernateController.update(driver);
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "", "You are not authorized to edit this user's data.", "");
            loadDrivers();
        }
    }

    public void loadDrivers() {
        tableOfDrivers.getItems().clear();
        List<Driver> drivers = hibernateController.getAllRecords(Driver.class);
        for (Driver driver : drivers) {
            DriverTableParameters driverTableParameters = new DriverTableParameters();
            driverTableParameters.setDriverId(driver.getId());
            driverTableParameters.setDriversLicenceNumber(driver.getDriversLicenceNumber());
            driverTableParameters.setEmergencyContactPhoneNumber(driver.getEmergencyContactPhoneNumber());
            driverTableParameters.setMedicalInsurance(driver.getMedicalInsurance());
            driverTableParameters.setMedicalCheckDate(driver.getMedicalCheckDate());
            observableDrivers.add(driverTableParameters);
        }
        tableOfDrivers.setItems(observableDrivers);
    }


    public void updateDriverTable() {
        loadDrivers();
    }

    public void initializeManagerTable() {
        tableOfManagers.setEditable(true);
        managerIdColumn.setCellValueFactory(new PropertyValueFactory<>("managerId"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(
                t -> handleManagerEditCommit(t, "email")
        );
        employmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("employmentDate"));
        //employmentDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        employmentDateColumn.setOnEditCommit(
                t -> handleManagerEditCommit(t, "employmentDate")
        );
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));
    }

    private void handleManagerEditCommit(TableColumn.CellEditEvent<ManagerTableParameters, ?> t, String fieldName) {
        Manager manager = hibernateController.getEntityById(Manager.class, t.getRowValue().getManagerId());
        if (((Manager) user).getIsAdmin() || user.getId() == manager.getId()) {
            if ("email".equals(fieldName)) {
                manager.setEmail((String) t.getNewValue());
            } else if ("employmentDate".equals(fieldName)) {
                LocalDate newDate = ((DatePicker) t.getNewValue()).getValue();
                manager.setEmploymentDate(newDate);
            }
            hibernateController.update(manager);
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "", "You are not authorized to edit this user's data.", "");
            loadManagers();
        }
    }

    public void loadManagers() {
        tableOfManagers.getItems().clear();
        List<Manager> managers = hibernateController.getAllRecords(Manager.class);
        for (Manager manager : managers) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setManagerId(manager.getId());
            managerTableParameters.setEmail(manager.getEmail());
            managerTableParameters.setEmploymentDate(manager.getEmploymentDate());
            managerTableParameters.setIsAdmin(manager.getIsAdmin());
            observableManagers.add(managerTableParameters);
        }
        tableOfManagers.setItems(observableManagers);
    }

    public void updateManagerTable() {
        loadManagers();
    }
}
