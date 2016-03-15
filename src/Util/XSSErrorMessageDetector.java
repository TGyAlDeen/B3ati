package Util;


import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Vuln;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.form.Form;

import controllers.MainController;

public class XSSErrorMessageDetector extends Thread{
	static ObservableList<Vuln>vulns = FXCollections.observableArrayList();
	static MainController controller;
	public  static void detectErrorMessages(HtmlDocument page,MainController cont) throws IOException {
	 controller = cont;
		 
		StringBuilder content = new StringBuilder();
		List<Form> forms=page.forms().getAll();
//		Resource response=null;
				
			//get every form as String
			for (int i = 0; i < forms.size(); i++) {
				//every element in the form
					for (int j = 0; j < forms.get(i).getNode().getChildren().size(); j++) {
						content.append(forms.get(i).getNode().getChildren().get(j).toString());
					}

				Document doc = Jsoup.parse(content.toString());

				Elements inp = doc.getElementsByTag("input");
				// every input 
				for(int k = 0; k < inp.size(); k++) {
					Element n = inp.get(k);
					 String name = n.attr("name").toString();
					 String rawType = n.attr("type").toString();
					 String type = (rawType == null) ? ("text") : (rawType.toLowerCase());
					 forms.get(i).get(name).setValue("<scRipt>window.location='http://google.com'</scRipt>");
					 System.out.println("Name is "+name+" type is "+type+forms.get(i).get(name).getValue());
					 String value = n.attr("value").toString();
					 if(type.equals("checkbox")) {
						forms.get(i).getCheckbox(type, value).setValue("<scRipt>window.location='http://google.com'</scRipt>");	
					} else if(type.equals("reset")) {
						// do nothing
					} else if(type.equals("submit")) {
						// do nothing
					}
					else if(type.equals("password")||type.equals("pass")){
						forms.get(i).get(name).setValue("<scRipt>window.location='http://google.com'</scRipt>");
					}else if(type.equals("textarea")||type.equals("pass")){
						forms.get(i).get(name).setValue("<scRipt>window.location='http://google.com'</scRipt>");
					}

				}//end of elements for
				HtmlDocument res=forms.get(i).submit();
				System.out.println(res.getTitle().toString());
				final String body = res.getTitle().toString();
				if(body == null || body.isEmpty()) {
					return;
				}
				
					if(body.contains("Google") || res.getResponse().toString().contains("302")) {
						System.out.println("XSS detected");
						processDetectedErrorMessage(page,"NRU Detect Cros Site Scripting (XSS)");
					}
				}

		}//
		
		

	
	
	

	
	private static  void processDetectedErrorMessage(HtmlDocument response, String errorString) throws IOException {
		System.out.println("XSS Detected");
		vulns.add(new Vuln("Cross site scripting",errorString,response.getTitle()));
		Platform.runLater(new Runnable() {
            public void run() {
            	controller.setIssue(vulns);
           
            }
        });
		
	}
	
}
