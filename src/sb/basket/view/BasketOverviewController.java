package sb.basket.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sb.basket.MainApp;
import sb.basket.model.Items;

public class BasketOverviewController implements Initializable {
	@FXML
	private TableView<Items> ItemTable;
	@FXML
	private TableColumn<Items, String> nameColumn;
	@FXML
	private TableColumn<Items, Integer> quantityColumn;
	@FXML
	private TableColumn<Items, Double> priceColumn;
	@FXML
	private TableColumn<Items, Double> totalColumn;

	@FXML
	private TextField nameField;

	@FXML
	private TextField quantityField;

	@FXML
	private TextField priceField;

	@FXML
	private TextField noItemsField;

	@FXML
	private TextField totalField;
	
	public static TextField tF;
	public static TextField nIF;
	public static TextField nF;
	public static TextField qF;
	public static TextField pF;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tF = totalField;
		nIF = noItemsField;
		nF = nameField;
		qF = quantityField;
		pF = priceField;
		// Initialize the Basket table with the two columns.
				nameColumn.setCellValueFactory(cellData -> cellData.getValue()
						.nameProperty());
				quantityColumn.setCellValueFactory(cellData -> cellData.getValue()
						.quantityProperty().asObject());
				priceColumn.setCellValueFactory(cellData -> cellData.getValue()
						.priceProperty().asObject());
				totalColumn.setCellValueFactory(cellData -> cellData.getValue()
						.totalProperty().asObject());
				showBasketDetails(null);
				ItemTable
						.getSelectionModel()
						.selectedItemProperty()
						.addListener(
								(observable, oldValue, newValue) -> showBasketDetails(newValue));
				getTotalItems();
				getTotalPrice();

	}
	
/*	public void setTextFields() {
		pF = priceField;
		nIF = noItemsField;
		pF.setText("");
		nIF.setText("");
	}*/



	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public BasketOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the person table with the two columns.
		nameColumn.setCellValueFactory(cellData -> cellData.getValue()
				.nameProperty());
		quantityColumn.setCellValueFactory(cellData -> cellData.getValue()
				.quantityProperty().asObject());
		priceColumn.setCellValueFactory(cellData -> cellData.getValue()
				.priceProperty().asObject());
		totalColumn.setCellValueFactory(cellData -> cellData.getValue()
				.totalProperty().asObject());
		showBasketDetails(null);
		getTotalItems();
		getTotalPrice();
		ItemTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showBasketDetails(newValue));

	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		ItemTable.setItems(mainApp.getItemData());
	}

	@FXML
	private void handleDeleteItems() {
		int selectedIndex = ItemTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			ItemTable.getItems().remove(selectedIndex);
			getTotalItems();
			getTotalPrice();
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Item Selected");
			alert.setContentText("Please select a Item in the table.");
			alert.showAndWait();

		}

	}

	@FXML
	private void handleNewItems() {

		Items tempItems = new Items(null, 0, 0);
		if (isInputValid()) {

			String nameText = nameField.getText();
			String quantityText = quantityField.getText();
			String priceText = priceField.getText();
			tempItems.setName(nameText);
			tempItems.setPrice(Double.parseDouble(priceText));
			tempItems.setQuantity(Integer.parseInt(quantityText));

			mainApp.getItemData().add(tempItems);
			getTotalItems();
			getTotalPrice();

		}
	}

	private DecimalFormat df = new DecimalFormat("0.00");

	public void getTotalPrice() {
		double total = 0;
		ArrayList<Items> itemList = new ArrayList<>(MainApp.itemData);
		for (Items item : itemList) {
			total += item.getTotal();
		}
		tF.setText("£" + df.format(total));
		
		// returns the total of each item through for loop
	}

	/*
	 * private void refreshItemTable() { int selectedIndex =
	 * ItemTable.getSelectionModel().getSelectedIndex();
	 * ItemTable.setItems(null); ItemTable.layout();
	 * ItemTable.setItems(mainApp.getItemData());
	 * ItemTable.getSelectionModel().select(selectedIndex); }
	 */

	/*
	 * private void getTotalPriceColumn() { Items tempItems = new Items(null, 0,
	 * 0); double total = 0; ArrayList<Items> itemList = new
	 * ArrayList<>(MainApp.itemData); for (Items item : itemList) { total =
	 * item.getPrice() * item.getQuantity(); tempItems.setTotal(total);
	 * tempItems.getTotal();
	 * 
	 * System.out.println(tempItems.getTotal()); }
	 * 
	 * // returns the total of each item through for loop }
	 */

	public void getTotalItems() {
		int total = 0;
		ArrayList<Items> itemList = new ArrayList<>(MainApp.itemData);
		for (Items item : itemList) {
			total += item.getQuantity();
		}
		nIF.setText(total + "");

	}

	private void showBasketDetails(Items item) {
		if (item != null) {
			// Fill the labels with info from the person object.
			ItemTable.setItems(mainApp.getItemData());

		}

	}

	private boolean isInputValid() {
		String errorMessage = "";
		ArrayList<Items> itemList = new ArrayList<>(MainApp.itemData);
		boolean found = false;
		

		if (nameField.getText() == null || nameField.getText().length() == 0) {
			errorMessage += "No valid name!\n";
		} else {
			for (Items item : itemList) {
				if(nameField.getText().toLowerCase().equals(item.getName().toLowerCase())) {
					found = true;
				}
			}
		}
		if (found == true) { 
			errorMessage += "Product already exsist";
		}

		if (quantityField.getText() == null
				|| quantityField.getText().length() == 0) {
			errorMessage += "No valid quantity!\n";
		} else {
			// try to parse the quantity field into an int.
			try {
				Integer.parseInt(quantityField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "No valid quantity (must be an integer)!\n";
			}
		}

		if (priceField.getText() == null || priceField.getText().length() == 0) {
			errorMessage += "No valid Price!\n";
		} else {
			// try to parse the quantity field into an int.
			try {
				Double.parseDouble(priceField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "No valid Price must be a number (must be a number)!\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	@FXML
	private void handleEditItems() {
		Items selectedItems = ItemTable.getSelectionModel().getSelectedItem();
		if (selectedItems != null) {
			boolean okClicked = mainApp.showBasketEditDialog(selectedItems);
			if (okClicked) {
				showBasketDetails(selectedItems);
				getTotalItems();
				getTotalPrice();

			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Item Selected");
			alert.setContentText("Please select a Item in the table.");

			alert.showAndWait();
		}
	}


}
