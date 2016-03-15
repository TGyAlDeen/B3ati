package model;

import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class WebSite {

	
	private StringProperty rootURL;
	private StringProperty siteName;
	private HashMap<String, String> WebBaseLinks;
	private ObservableList<Page> pages;
	private String SiteInformation;
	ObservableList<Vuln> vuln;
	
	public WebSite(String rootURL, String siteName,
			HashMap<String, String> webBaseLinks, ObservableList<Page> pages,
			String siteInformation) {
	
		this.rootURL = new SimpleStringProperty(rootURL);
		this.siteName = new SimpleStringProperty(siteName);
		this.WebBaseLinks = webBaseLinks;
		this.pages = pages;
		this.SiteInformation = siteInformation;
		
	}

	public StringProperty getRootURL() {
		return rootURL;
	}

	public void setRootURL(StringProperty rootURL) {
		this.rootURL = rootURL;
	}

	public StringProperty getSiteName() {
		return siteName;
	}

	public void setSiteName(StringProperty siteName) {
		this.siteName = siteName;
	}

	public HashMap<String, String> getWebBaseLinks() {
		return WebBaseLinks;
	}

	public void setWebBaseLinks(HashMap<String, String> webBaseLinks) {
		WebBaseLinks = webBaseLinks;
	}

	public ObservableList<Page> getPages() {
		return pages;
	}

	public void setPages(ObservableList<Page> pages) {
		this.pages = pages;
	}

	public String getSiteInformation() {
		return SiteInformation;
	}

	public void setSiteInformation(String siteInformation) {
		SiteInformation = siteInformation;
	}

	public ObservableList<Vuln> getVuln() {
		return vuln;
	}

	public void setVuln(ObservableList<Vuln> vuln) {
		this.vuln = vuln;
	}
	
	
	
	//generate properties for all fiels that return the property 
	//Date 7/12/2015 @ 3:25 am
	
	//edit Stie information at 16/7/215
	
	
	
	
	
}
 