package Util;


import java.io.IOException;
import java.net.URL;
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
import com.google.common.collect.ImmutableMap;

import controllers.MainController;

public class SQLErrorMessageDetector extends Thread{
	static Boolean isFound=false;
	static ObservableList<Vuln>vulns = FXCollections.observableArrayList();
	static MainController controller;
	private  enum databaseErrorTypes {SQLSERVER, ASP, MS, ACCESS, MYSQL, POSTGRES, JAVA, ORACLE, DQL, CF, DB2}
	private final static  ImmutableMap<String, databaseErrorTypes> ERROR_STRINGS = ImmutableMap.<String, databaseErrorTypes>builder().put(
																													"Incorrect syntax near",  databaseErrorTypes.SQLSERVER).put(
																								   					"Unclosed quotation mark", databaseErrorTypes.SQLSERVER).put(
																								   					"Dynamic SQL Error", databaseErrorTypes.SQLSERVER).put(			
																								   							
																								   					"SqlClient.SqlException: Syntax error", databaseErrorTypes.ASP).put(
																								   							
																								   				    "[Microsoft][ODBC SQL Server Driver]", databaseErrorTypes.MS).put(
																								   				    "Microsoft OLE DB Provider for ODBC Drivers</font>", databaseErrorTypes.MS).put(
																								   				    "Microsoft OLE DB Provider for ODBC Drivers</FONT>", databaseErrorTypes.MS).put(
																								   
																								   				    "Syntax error in string in query expression",  databaseErrorTypes.ACCESS).put(
																								   
																								   				    "<b>Warning</b>:  MySQL: ", databaseErrorTypes.MYSQL).put(
																								   				    "You have an error in your SQL syntax", databaseErrorTypes.MYSQL).put(
																								   				    "supplied argument is not a valid MySQL", databaseErrorTypes.MYSQL).put(
																								   
																								   				    "PostgreSQL query failed:", databaseErrorTypes.POSTGRES).put(
																								   				    "unterminated quoted string at or near", databaseErrorTypes.POSTGRES).put(
																								   				    "syntax error at or near", databaseErrorTypes.POSTGRES).put(
																								   				    "invalid input syntax for integer:",	databaseErrorTypes.POSTGRES).put(
																								   				    "Query failed: ERROR: syntax error",	databaseErrorTypes.POSTGRES).put(
																								  
																								   				    "Unexpected end of command in statement", databaseErrorTypes.JAVA).put(
																								   				    "java.sql.SQLException:", databaseErrorTypes.JAVA).put(
																								   				    "quoted string not properly terminated",	databaseErrorTypes.ORACLE).put(
																								   				    "SQL command not properly ended", databaseErrorTypes.ORACLE).put(
																								   				    "unable to perform query", databaseErrorTypes.ORACLE).put(
			
																								   				    "[DM_QUERY_E_SYNTAX]", databaseErrorTypes.DQL).put(
																								   
																								   				    "[Macromedia][SQLServer JDBC Driver]", databaseErrorTypes.CF).put(
																								   				    "[Macromedia][MySQL JDBC Driver]", databaseErrorTypes.CF).put(
			
																								   				    "DB2 SQL Error:", databaseErrorTypes.DB2).build();
			
	
	
	public static  void detectErrorMessages(HtmlDocument page,MainController cont) throws IOException {
					controller = cont;
					StringBuilder content = new StringBuilder();
					List<Form> forms=page.forms().getAll();
			//		Resource response=null;
							
						//get every form as String
					if(forms.size() >=1  ){
						for (int i = 0; i < forms.size(); i++) {
								//every element in the form
							for (int j = 0; j < forms.get(i).getNode().getChildren().size(); j++) {
							content.append(forms.get(i).getNode().getChildren().get(j).toString());}
							// insert html of the form into the jsoup to detect tags
							Document doc = Jsoup.parse(content.toString());
							Elements inp = doc.getElementsByTag("input");
							// every input 
							for(int k = 0; k < inp.size(); k++) {
								 Element n = inp.get(k);
								 String name = n.attr("name").toString();
								 String rawType = n.attr("type").toString();
								 String type = (rawType == null) ? ("text") : (rawType.toLowerCase());
								 
								 forms.get(i).get(name).setValue("'123");
								 String value = n.attr("value").toString();
								 if(type.equals("checkbox")) {
									forms.get(i).getCheckbox(type, value).setValue("'123");	
								} else if(type.equals("reset")) {
									// do nothing
								} else if(type.equals("submit")) {
									// do nothing
								}
								else if(type.equals("password")||type.equals("pass")){
									forms.get(i).get(name).setValue("'12345");
								}else if(type.equals("textarea")||type.equals("pass")){
									forms.get(i).get(name).setValue("'12345");
								}
			
							}//end of elements for
							HtmlDocument res=forms.get(i).submit();
							
							final String body = res.asString();
							if(body == null || body.isEmpty()) {
								return;
							}
							for(String errorString: ERROR_STRINGS.keySet()) {
								if(body.contains(errorString)) {
									processDetectedErrorMessage(page, errorString, ERROR_STRINGS.get(errorString).toString());
									isFound=true;
								}}
			if(!isFound){		
			for(int k = 0; k < inp.size(); k++) {
			Element n = inp.get(k);
			 String name = n.attr("name").toString();
			 String rawType = n.attr("type").toString();
			 String type = (rawType == null) ? ("text") : (rawType.toLowerCase());
			 forms.get(i).get(name).setValue("\"123");
			 
			 String value = n.attr("value").toString();
			 if(type.equals("checkbox")) {
				forms.get(i).getCheckbox(type, value).setValue("\"123");	
			} else if(type.equals("reset")) {
				// do nothing
			} else if(type.equals("submit")) {
				// do nothing
			}
			else if(type.equals("password")||type.equals("pass")){
				forms.get(i).get(name).setValue("\"12345");
			}else if(type.equals("textarea")||type.equals("pass")){
				forms.get(i).get(name).setValue("\"12345");
			}

		}//end of elements for
		HtmlDocument res1=forms.get(i).submit();
		
		final String body1 = res1.asString();
		if(body == null || body1.isEmpty()) {
			return;
		}
		for(String errorString1: ERROR_STRINGS.keySet()) {
			if(body1.contains(errorString1)) {
				processDetectedErrorMessage(page, errorString1, ERROR_STRINGS.get(errorString1).toString());
			}
				
			}
	}//end of isFound 
		}//end of else for double "" testing

	}//end of if 		
		}//end of forms loop
		
		
	
	
	
	

	
	private static  void processDetectedErrorMessage(HtmlDocument response, String errorString, String databaseErrorTypes) throws IOException {
		URL repoUrl;
		repoUrl=SQLErrorMessageDetector.class.getResource("/resource/SQLI.html");
		vulns.add(new Vuln("NRU Detect SQL Injection in "+databaseErrorTypes+" DataBase, at "+response.getTitle().substring(0, 15),repoUrl.toExternalForm(),response.getTitle()));
		Platform.runLater(new Runnable() {
            public void run() {
            	controller.setIssue(vulns);
            	
            }
        });
		
	}
	
}
