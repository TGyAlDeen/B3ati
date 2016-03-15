package controllers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.management.RuntimeErrorException;

import model.Page;
import Util.DB;
import Util.SQLErrorMessageDetector;

//import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.MechanizeAgent;
//import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
//import Util.SQLErrorMessageDetector.databaseErrorTypes;


public class DL_NewScan  {
	MainController controller;
	public AtomicBoolean stopworking = new AtomicBoolean(false);
	Boolean stop = new Boolean(false);
	//thread object
	Worker<String> worker;
	//thread status
	ReadOnlyObjectProperty<Worker.State> stateproperty=null;
	//page list
	ObservableList<Page> pages = FXCollections.observableArrayList();
	//database config
	public static DB db = new DB();
	//stage of the dialog
	private Stage dialogStage;
	boolean  okClicked=false;
	//for updating pages number in the layout 
	static Integer page_nums=0;
	
	//////// FXML Variables /////////////
	  	@FXML
	    private DialogPane NewScanDialog;

	    @FXML
	    private AnchorPane DL_header;

	    @FXML
	    private AnchorPane AP_Middle;

	    @FXML
	    private TextField txt_URL;

	    @FXML
	    private Button bt_Cancel;

	    @FXML
	    private Button bt_StartScan;

	    @FXML
	    private AnchorPane DL_footer;
	   
	    @FXML 
	   public void CancelDL(){
	    	
	    	dialogStage.close();
	    }
	    
	public void initialize()  {
		// create task
		worker = new Task<String>() {
			@Override
			protected String call() throws Exception {
			processPage(getTxt_URL());
			return null;} };//end of task
			
		pages.addListener((ListChangeListener<Page>) c ->
			Platform.runLater(new Runnable() {
            public void run() {
            page_nums+=1;
            controller.update(pages);
            }
        }));
	}// End of init
 	  
	
	@FXML 
	public void handleOK() throws SQLException, Exception{		
		if(inputisValid()){
			
			db.runSql("TRUNCATE RECORD;");
			
			new Thread((Runnable)this.worker).start();
			stateproperty= worker.stateProperty();
			dialogStage.close();
			//for disable the start scan bottom and Stop Scan 
			controller.bt_StartScan.setDisable(true);
			controller.bt_StopScan.setDisable(false);
			controller.ScanState.textProperty().bind(Bindings.format("%s",worker.stateProperty()));
			controller.ScanDone.textProperty().bind(Bindings.format("%s",worker.workDoneProperty().asString()));
			controller.progressInD.setDisable(false);
//			controller.bt_StopScan.disableProperty().bind(
//			stateproperty.isNotEqualTo(Worker.State.RUNNING));
//			controller.bt_StartScan.disableProperty().bind(
//			stateproperty.isNotEqualTo(Worker.State.SUCCEEDED));
			
		}
	}  
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	
    boolean inputisValid(){
    	String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
       	String errorMessage = "";
    	if(txt_URL.getText() == null || txt_URL.getText().length() == 0)
    		errorMessage += "This Field Can't be empty \nPlease Enter URL !\n E.x http:"+"\\"+"\\"+"example.com"; 
    	else if(!(txt_URL.getText().matches(regex))){
    		errorMessage += "No valid Link!\n enter Correct URL";
       		errorMessage += "Please Enter URL !\n E.x http:"+"\\"+"\\"+"example.com"; 
    		}if (errorMessage.length() == 0) {
              return true;
            } else {
            	//Date 6/26/2015
                // Show the error Message.
                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                return false;
            }
    	
    }

    public boolean isOkClick(){
    	return okClicked;
    }

    
    
    /////////////////////////// Crawling Work///////////////////////////////////
    
    public  void processPage(String URL) throws SQLException, InterruptedException, IOException{
    	
    	Thread.sleep(new Random().nextInt(10));
		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = db.runSql(sql);
		if(rs.next()){
			//do nothing
		}else{//never been processed 
		MechanizeAgent agent = new MechanizeAgent();
		HtmlDocument page = agent.get(URL);
		String pageTitle = page.getTitle();
		//form if
		if(page.forms().size() >= 1 || (page.links().size() >= 1)){
			// we want this page 
			//add page
			pages.add(new Page(page.getUri().toString(),page.links().size(), page.forms(), page.forms().size(), page.asString(), page.getTitle(),page.getRequest().toString(), page.getResponse().toString()));
			SQLErrorMessageDetector.detectErrorMessages(page,controller);
//			XSSErrorMessageDetector.detectErrorMessages(page, controller);
			//add from string builder that have 
			sql = "INSERT INTO  Record(URL,PageName) VALUES " + "(?,?)";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.setString(2, pageTitle);
			stmt.execute();
			
			if( (page.links().size() >= 1))	{
				
					for (int i = 0; i < page.links().size(); i++) {
					    if(page.links().get(i).href().startsWith(URL.substring(0,20))){
						if(stopworking.get())//stop Crawling
							throw new RuntimeErrorException(null, "stop Crawling ");
						if(page.links().get(i).href().contains("#")){
				    		continue;
				    		}
									processPage(page.links().get(i).href());
							
					
				}
					}//end of if links contain url
					}//end of links if size
			
			}	//end of form size if or link size
		}		
 }
    
   public void setMainController(MainController contr){
	   this.controller=contr;
   }
   
   public String getTxt_URL() {
		return txt_URL.getText();
	}
   
}
