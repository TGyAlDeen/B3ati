package model;

import java.util.Arrays;
import java.util.regex.Pattern;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;

import com.gistlabs.mechanize.document.html.form.Forms;
import com.gistlabs.mechanize.document.link.Links;

public class Page extends TreeItem<String>{

	private Links pageLinks;
	private IntegerProperty linksSize;
	private Forms pageForms;
	private IntegerProperty pageFormsSize;
	private StringProperty pageContent;
	private StringProperty pageTitle;
	private StringProperty pageRequest;
	private StringProperty pageResponse;
	private StringProperty pageUrl;
	

	public Page(String url,int linksSize, Forms pageForms,int pageFormsSize, String pageContent,
			String pageTitle,String pageReq,String pageResponse) {
		super();
		this.pageUrl = new SimpleStringProperty(url);
		this.linksSize =new SimpleIntegerProperty(linksSize);
		this.pageForms = pageForms;
		this.pageFormsSize = new SimpleIntegerProperty(pageFormsSize);
		this.pageContent = new SimpleStringProperty(pageContent);
		this.pageTitle = new SimpleStringProperty(pageTitle);
		this.pageRequest=new SimpleStringProperty(setPageRequest(pageReq));
		this.pageResponse = new SimpleStringProperty(pageResponse);
	}


	public Links getPageLinks() {
		return pageLinks;
	}


	public void setPageLinks(Links pageLinks) {
		this.pageLinks = pageLinks;
	}


	public IntegerProperty getLinksSize() {
		return linksSize;
	}


	public void setLinksSize(IntegerProperty linksSize) {
		this.linksSize = linksSize;
	}


	public Forms getPageForms() {
		return pageForms;
	}


	public void setPageForms(Forms pageForms) {
		this.pageForms = pageForms;
	}


	public IntegerProperty getPageFormsSize() {
		return pageFormsSize;
	}


	public void setPageFormsSize(IntegerProperty pageFormsSize) {
		this.pageFormsSize = pageFormsSize;
	}


	public StringProperty getPageContent() {
		return pageContent;
	}


	public void setPageContent(StringProperty pageContent) {
		this.pageContent = pageContent;
	}


	public StringProperty getPageTitle() {
		return pageTitle;
	}


	public void setPageTitle(StringProperty pageTitle) {
		this.pageTitle = pageTitle;
	}

	public StringProperty getPageResponse() {
		return pageResponse;
	}
	public StringProperty getPageRequest() {
		return pageRequest;
	}


	public String setPageRequest(String pageInformation) {
		String subdata = pageInformation.substring(17);
		Pattern pattern = Pattern.compile(",");
		pattern = Pattern.compile(Pattern.quote(","));
        String[] data = pattern.split(subdata);
        System.out.println(Arrays.toString(data));
//		this.pageRequest = new SimpleStringProperty(Arrays.toString(data));
		return Arrays.toString(data);
	}
	
	public StringProperty getUrl(){
		return pageUrl;
	}

	
	public String toString(){
		return pageTitle.get();
		
	}
	
}
