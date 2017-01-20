package sb.basket.view;

import java.util.ArrayList;

import sb.basket.MainApp;
import sb.basket.model.Items;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of the basket.
 */
public class BasketEditDialogController {

	@FXML
	private TextField nameField;
	@FXML
	private TextField quantityField;
	@FXML
	private TextField priceField;

	private Stage dialogStage;
	private Items item;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the person to be edited in the dialog.
	 * 
	 * @param basket items
	 */
	public void setItems(Items Item) {
		this.item = Item;

		nameField.setText(Item.getName());
		quantityField.setText(Integer.toString(Item.getQuantity()));
		priceField.setText(Double.toString(Item.getPrice()));

	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			item.setName(nameField.getText());
			item.setQuantity(Integer.parseInt(quantityField.getText()));
			item.setPrice(Double.parseDouble(priceField.getText()));

			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		boolean found = false;
		int amountFound = 1;
		ArrayList<Items> itemList = new ArrayList<>(MainApp.itemData);
		
		
		if (nameField.getText() == null || nameField.getText().length() == 0) {
			errorMessage += "No valid  name!\n";
		} else {
			for(Items item : itemList) {
				if(nameField.getText().toLowerCase().equals(item.getName().toLowerCase())) {
					found = true;
					if (found) {
						amountFound++;
					}
				}
			}
			
			if (nameField.getText().toLowerCase().equals(item.getName().toLowerCase())) {
				amountFound--;
			}
		}
		if(amountFound > 1) {
			errorMessage +="Product name already exsists\n";
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

		if (priceField.getText() == null
				|| priceField.getText().length() == 0) {
			errorMessage += "No valid Price!\n";
		} else {
			// try to parse the quantity field into an int.
			try {
				Double.parseDouble(priceField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "Not a valid price (must be an number)!\n";
			}
		}


		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}