package controllers;
import java.io.IOException;
import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.Page;
import model.Vuln;
import Util.SQLErrorMessageDetector;
import app.MainApp;


public class MainController  {
	
	public PageDetailController pagecontroller;
	ScrollPane pageDetails;
	public DL_NewScan dlcontroller; 
	private MainApp main;
		
	@FXML
     Label lbl_pageNum;

    @FXML
     Label ScanState;

    @FXML
     Label ScanDone;
    
    @FXML 
    Button bt_StopScan;
    
	@FXML
	private ListView<Vuln> lst_issue;
	
	 @FXML
	  ProgressIndicator progressInD;
   
    @FXML
    private Label lbl_sitemap;

    @FXML
    private AnchorPane fr_ScanInfo;

    @FXML
    private Label lbl_sitemap1;

    @FXML
    private ProgressBar statusPr;

    @FXML
    public BorderPane innerBoarder;

	@FXML
    private AnchorPane fr_Issues;

    @FXML
    private Label lbl_fIssue;

    @FXML
    private Label StatusLbl;
	
    @FXML
    public Button bt_StartScan;
    
    @FXML
    private ListView<Page> pagesList;
  
    
    public void setIssue(ObservableList<Vuln> listIssue){
    	obvun=listIssue;
    	lst_issue.setItems(obvun);
    }
    
   
    public void setMainApp(MainApp mainApp) {
    	this.main=mainApp;
    }
    
    ObservableList<Page> obpage = FXCollections.observableArrayList();
    ObservableList<Vuln> obvun = FXCollections.observableArrayList();
 
    
    public void initialize() throws IOException {
    		bt_StopScan.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			dlcontroller.page_nums=0;
			dlcontroller.worker.cancel();
			bt_StartScan.setDisable(false);
			bt_StopScan.setDisable(true);
			}});
	 	   	setPageController();
	 	   	pagesList.setItems(obpage);
	 	   	lst_issue.setItems(obvun);
		  
	 	   	pagesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Page>() {
	        @Override
	        public void changed(ObservableValue<? extends Page> ov,Page oldv, Page newValue) {
	         innerBoarder.getChildren().clear();			
	         innerBoarder.setCenter(pageDetails);
	         pagecontroller.setHtml(newValue.getPageContent().get());
	         pagecontroller.setReq(newValue.getPageRequest().get());
	         pagecontroller.setRes(newValue.getPageResponse().get());
	         pagecontroller.setWeb(newValue.getUrl().get());
			}
	        });
		  
	 	   	lst_issue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vuln>() {
   			@Override
   			public void changed(ObservableValue<? extends Vuln> ov,
			Vuln oldv, Vuln newValue) {
   					innerBoarder.getChildren().clear();
   					WebView web = new WebView();
   					WebEngine webEngine = web.getEngine();
   					webEngine.load(newValue.getRepLink());
    			  	innerBoarder.setCenter(web);
   				}
		  	});
		  
	   	  }  

	  @FXML
	  public void handleNewScan(){
		innerBoarder.setCenter(pageDetails);
		pageDetails.setMaxSize(1500, 500);
		main.showStartScanDialog();
	  	}
	 
	  
	  @FXML
	  public void handleHlelp(){
		  		URL repoUrl;
				repoUrl=SQLErrorMessageDetector.class.getResource("/resource/c.html");
				innerBoarder.getChildren().clear();
				WebView web = new WebView();
				WebEngine webEngine = web.getEngine();
				webEngine.load(repoUrl.toExternalForm());
				innerBoarder.setCenter(web);
		  	  }
	  
	  @FXML
	  public void handleOwasp(){
		 	innerBoarder.getChildren().clear();
			WebView web = new WebView();
			WebEngine webEngine = web.getEngine();
			webEngine.load("http://securitycompass.com/training/free/course-demos/");
			innerBoarder.setCenter(web);
			
		  
	  }
	  @FXML
	  public void handleTop10(){
		 	innerBoarder.getChildren().clear();
			WebView web = new WebView();
			WebEngine webEngine = web.getEngine();
			webEngine.load("https://www.owasp.org/index.php/Top_10_2013-Top_10");
			innerBoarder.setCenter(web);
			
		  
	  }
	  public void setDL(DL_NewScan dlcontroller){
		  	this.dlcontroller=dlcontroller;
	  }
	  
	  
	  
	  @FXML 
	  public void handleClose(){
		  System.exit(0);
	  }
	  
	 
	  @FXML
	    private void handleAbout() {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("NRU Scanner");
	        alert.setHeaderText("About The Scanner");
	        alert.setContentText(resource.Data.aboutText);
	        alert.showAndWait();
	    }
	  
	  /////////////////// page processing ////////////////////
	 


	
	public void update(ObservableList<Page> list) {
		pagesList.setItems(list);
	}
	
	public void setPageController() throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PageDetails.fxml"));
		ScrollPane pane =  loader.load();
		this.pageDetails=pane;
		pagecontroller = loader.<PageDetailController>getController();
		
	}

	
	
}
