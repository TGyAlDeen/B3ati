package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class PageDetailController{
	
	MainController controller;
	
	
	@FXML
	private WebView webView;
	@FXML
    public TextArea pageHtml;

    @FXML
    private TextArea pageRequest;

    @FXML
    private TextArea pageResponse;
    
	
	public void setHtml(String html){pageHtml.setText(html);}
	public void setReq(String req){pageRequest.setText(req);}
	public void setRes(String res){pageResponse.setText(res);}
	
	public void setWeb(String web_url){
		final WebEngine webEngine=webView.getEngine();
		webEngine.load(web_url);
	}
	public void setMaincontroller(MainController controller){
		this.controller=controller;
	}
}
