package sb.basket;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import sb.basket.model.BasketListWrapper;
import sb.basket.model.Items;
import sb.basket.view.BasketEditDialogController;
import sb.basket.view.BasketOverviewController;
import sb.basket.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	public static ObservableList<Items> itemData = FXCollections
			.observableArrayList();

	public MainApp() {
		// Add some sample data
		itemData.add(new Items("PS4", 1, 120));
		itemData.add(new Items("Xbox One", 1, 250));
	}

	/**
	 * Returns the data as an observable list of Shopping Items.
	 * 
	 * @return
	 */
	public ObservableList<Items> getItemData() {
		return itemData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Shopping Basket");
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(600);
		primaryStage.setMaxHeight(600);
		primaryStage.setMaxWidth(700);
		this.primaryStage.getIcons().add(new Image("file:resources/images/shopping_basket_32.png"));
		initRootLayout();
		showBasketOverview();

	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try to load last opened person file.
		File file = getItemFilePath();
		if (file != null) {
			loadItemDataFromFile(file);
		}
	}

	/**
	 * Opens a dialog to edit details for the specified basket item. If the user
	 * clicks OK, the changes are saved into the provided basket item and true
	 * is returned.
	 * 
	 * @param basket item
	 *            the basket object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */


	public void showBasketOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/BasketOverview.fxml"));
			AnchorPane BasketOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(BasketOverview);

			// Give the controller access to the main app.
			BasketOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public boolean showBasketEditDialog(Items item) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/BasketEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Basket");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			BasketEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setItems(item);
			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public File getItemFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("Filepath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	public void setItemFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			primaryStage.setTitle("Shopping Basket - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("Shopping Basket");
		}
	}

	public void loadItemDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext
					.newInstance(BasketListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			BasketListWrapper wrapper = (BasketListWrapper) um.unmarshal(file);

			itemData.clear();
			itemData.addAll(wrapper.getItems());
			

			setItemFilePath(file);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n"
					+ file.getPath());

			alert.showAndWait();

		}
	}

	public void saveItemDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext
					.newInstance(BasketListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			BasketListWrapper wrapper = new BasketListWrapper();
			wrapper.setItems(itemData);

			m.marshal(wrapper, file);

			setItemFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n"
					+ file.getPath());

			alert.showAndWait();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}