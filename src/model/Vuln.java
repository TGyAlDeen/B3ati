package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vuln {
	
	StringProperty vulnName;
	StringProperty repLink;
	StringProperty pageTitle;
	
	public Vuln(String vulnName, String repLink,
			String pageTitle) {
	
		this.vulnName = new SimpleStringProperty(vulnName);
		this.repLink = new SimpleStringProperty(repLink);
		this.pageTitle = new SimpleStringProperty(pageTitle);
	}
	
	public String getRepLink(){
		return repLink.get();
	}
	public String toString() {
		return vulnName.get();
	}
}
