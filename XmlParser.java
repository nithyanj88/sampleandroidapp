package com.omt.quikformz.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.omt.quikformz.activity.FormPresenter.IDisplay;

import android.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

//To check the version from XML
public class XmlParser {
	private Context context;
	private IDisplay display;
	
	public XmlParser(Context context,  IDisplay display){
	this.context = context;
	this.display = display;
	}
	
	public interface IDisplay {
		void startNewActivity(Intent intent);
		public void displayAlertBox(final String url);
		public void displayToast();
	}
	
	
	
	Handler handler =new Handler();
	
	//Read the XML, comparing versions & calling the downloader
	public void parseXml(File file, String str)	throws ParserConfigurationException, SAXException, IOException 
	{
		String rootNode = "main";
		String versionNode = "tag1";
		String urlNode = "tag2";
		
		int latestAppVersion = 0;
		String downloadUrl = "";
		
		 StringBuilder strXml = new StringBuilder();

		boolean downloaded = DownloadRes.downloadPoints(strXml, str);
		if (!downloaded) {
			
			return;
		}
		try {
			
			StringReader sr = new StringReader(strXml.toString());
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			myOutWriter.append(strXml.toString());
			myOutWriter.close();
			fOut.close();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(file);
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName(rootNode);
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				NodeList properties = item.getChildNodes();
				for (int j = 0; j < properties.getLength(); j++) {
					final Node property = properties.item(j);
					String name = property.getNodeName();
					if (name.equalsIgnoreCase(versionNode)) {
					   latestAppVersion = Integer.parseInt(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase(urlNode)) {
						downloadUrl =property.getFirstChild().getNodeValue();
					}
				}
			}

			//version comparision
			if (latestAppVersion > Downloader.currentAppVersion){
				final String url =downloadUrl;
				display.displayAlertBox(url);
				
			}
			else{
				display.displayToast();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}