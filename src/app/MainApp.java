package app;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controllers.DL_NewScan;
import controllers.MainController;

public class MainApp extends Application {
	public MainController controller;
	public DL_NewScan dlcontroller;
	
	private Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception{
			this.stage=stage;
			stage.setTitle("NRU Web Application Scanner ");
			initScreen();
				    	
	}//end of start method
	
	
	public void initScreen()throws Exception{
			final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/Main.fxml"));
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/resource/ologo.png")));
			AnchorPane pane = (AnchorPane) loader.load();
			controller = loader.<MainController>getController();
			controller.setMainApp(this);
//			stage.initStyle(StageStyle.TRANSPARENT);
			Scene scene =new Scene(pane);
//			pane.setOnMouseDragReleased(dragStage);
			stage.setScene(scene);
			stage.show();
		
	}
	
	//should change to boolean for the proccess we need in url
	public void showStartScanDialog(){
		
			try {
				//load DL
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("/view/NewScan.fxml"));
				AnchorPane page = (AnchorPane) loader.load();
				//create the stage 
				Stage dialog = new Stage();
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(stage);// to keep the dialog in front Ya Razah 
				Scene scene = new Scene(page);
				dialog.setScene(scene);
				//set controller
				this.dlcontroller = loader.<DL_NewScan>getController();
				dlcontroller.setDialogStage(dialog);
				dlcontroller.setMainController(controller);
				dialog.showAndWait();
				
			} catch (IOException e) {
				
				e.printStackTrace();
							}
	       
	       
		
	
		
		
	}  


		
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	// none wanted methods
	
	//just to kill the app
	  /*  root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent t) {
	          stage.hide();
	        }
	      });*/
	public EventHandler<MouseEvent> dragStage =  new EventHandler<MouseEvent>()
			  {
	    	     class Delta { double x, y; } 
			      final Delta dragDelta = new Delta();
	 	         @Override
	 	         public void handle(MouseEvent mouseEvent) 
	 	         {
	 
	 		       stage.setX(mouseEvent.getScreenX() + dragDelta.x);
	 		       stage.setY(mouseEvent.getScreenY() + dragDelta.y);
	 		  }
	 		};
}
