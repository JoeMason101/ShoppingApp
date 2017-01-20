package sb.basket.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sb.basket.MainApp;

public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;
   

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty basket.
     */
    @FXML
    private void handleNew() {
        mainApp.getItemData().clear();
        mainApp.setItemFilePath(null);
        newApp();
    }
    
    public void newApp() {
    	BasketOverviewController.nIF.clear();
    	BasketOverviewController.tF.clear();
    	BasketOverviewController.nF.clear();
    	BasketOverviewController.pF.clear();
    	BasketOverviewController.qF.clear();
    }
    
    public void openApp() {
    	 BasketOverviewController ovc = new BasketOverviewController();
    	 ovc.getTotalItems();
    	 ovc.getTotalPrice();
    }
    
   

    /**
     * Opens a FileChooser to let the user select an basket to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        


        if (file != null) {
            mainApp.loadItemDataFromFile(file);
            openApp();

        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File itemFile = mainApp.getItemFilePath();
        if (itemFile != null) {
            mainApp.saveItemDataToFile(itemFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveItemDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Shopping Basket");
        alert.setHeaderText("About\nPlease enjoy the shopping basket I have made!");
        alert.setContentText("Author: Joseph Mason\nWebsite: www.josephjamesmason.co.uk");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}