/**
 * parsing an xml file using sub elements for each piece of data about a user
 */
package com.bjc.ekk.ngimport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author ekk9418
 *
 */
public class ParseXML {
	ArrayList<NGUser> myUsers = new ArrayList<NGUser>();
	String path;
	String file;
	Document dom;
	private final String[] TITLES = {"dnp", "whnp", "pac", "lcsw", "gnp", "aud", "fnp", "aprn", "do", "cfnp", "anp"};
	
	int tLength = 0;
	
	/**
	 * @return the myUsers
	 */
	public ArrayList<NGUser> getMyUsers() {
		return myUsers;
	}

	/**
	 * @param myUsers the myUsers to set
	 */
	public void setMyUsers(ArrayList<NGUser> myUsers) {
		this.myUsers = myUsers;
	}
	
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	public ParseXML(String p, String f) {
		setFile(f);
		setPath(p);
		parse();
	}
	
	public void parse() {//parse xml using dom library
		//get the factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			//using factory get an instance of the document builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			//parse representation to get DOM representation of the XML file
			dom = builder.parse(path + file);
			
		} catch(ParserConfigurationException pce) {
			
		} catch(SAXException saxe) {
			
		} catch(IOException ioe) {
			
		}
		
		//get the root element from the DOM object
		Element topEl = dom.getDocumentElement();
		
		//NGUser u = new NGUser();
		String fn = "";
		String ln = "";
		String ng = "";
		String ad = "";
		boolean d = false;
		String t = "";
		String mi = "";
		
		NodeList lnNl = topEl.getElementsByTagName("last_name");
		if(lnNl != null && lnNl.getLength() > 0) {
			for (int i=0;i<lnNl.getLength();i++) {
				Element lnEl = (Element)lnNl.item(i);
				ln = lnEl.getAttributes().getNamedItem("last_name").getNodeValue().toLowerCase();
				
				while (ln.startsWith(" ")) {
					ln = ln.substring(1);
				}
				while (ln.endsWith(" ")) {
					ln = ln.substring(0,ln.length()-1);
				}
				
				//get title from end of ln
				String[] lnt = ln.split(" ");
				if (lnt.length > 1) {
					if (Arrays.asList(TITLES).contains(lnt[lnt.length-1])) {
						t = lnt[lnt.length-1];
						ln = lnt[0];
						if (lnt.length > 2) {
							for (int lnint=1;lnint<lnt.length-1;lnint++) {
								if (!lnt[lnint].equals(" ")) {
									ln += lnt[lnint];
								}
							}
						}
					}
					else if (Arrays.asList(TITLES).contains(lnt[lnt.length-2])) {
						lnt[lnt.length-2] = lnt[lnt.length-2] + " " + lnt[lnt.length-1];
						t = lnt[lnt.length-2];
						ln = lnt[0];
						if (lnt.length > 3) {
							for (int lnint=1;lnint<lnt.length-2;lnint++) {
								if (!lnt[lnint].equals(" ")) {
									ln += lnt[lnint];
								}
							}
						}
					}
				}

				NodeList fnNl = lnEl.getElementsByTagName("first_name");
				if(fnNl != null && fnNl.getLength() > 0) {
					for(int j = 0 ; j < fnNl.getLength();j++) {
						Element fnEl = (Element)fnNl.item(j);
						fn = fnEl.getAttributes().getNamedItem("first_name").getNodeValue().toLowerCase();
						
						while (fn.startsWith(" ")) {
							fn = fn.substring(1);
						}
						while (fn.endsWith(" ")) {
							fn = fn.substring(0,fn.length()-1);
						}
						
						//get mi from end of fn
						String[] fnmi = fn.split(" ");
						if (fnmi.length > 1) {
							mi = fnmi[fnmi.length-1];
							//System.out.println(mi);
							fn = fnmi[0];
							if (fnmi.length > 2) {
								for (int fnint=1;fnint<fnmi.length-1;fnint++) {
									if (!fnmi[fnint].equals(" ")) {
										fn += fnmi[fnint];
									}
								}
							}
						}
						
						NodeList detNl = fnEl.getElementsByTagName("Details");
						if(detNl != null && detNl.getLength() > 0) {
							for(int m = 0 ; m < detNl.getLength();m++) {
								Element detEl = (Element)detNl.item(m);

								if (detEl.getAttributes().getLength()>2) {//if all 3 details nodes are present 
									ad = detEl.getAttributes().getNamedItem("login_id").getNodeValue().toLowerCase();
								}
								ng = detEl.getAttributes().getNamedItem("user_id").getNodeValue();
								if (detEl.getAttributes().getNamedItem("Deactivated").getNodeValue().equals("1")) {
									d = true;
								}
								else if (detEl.getAttributes().getNamedItem("Deactivated").getNodeValue().equals("0")){
									d = false;
								}
								else {
									System.out.println(detEl.getAttributes().getNamedItem("Deactivated").getNodeValue());
								}
								if (!(ad=="") && mi.equals("") && !(ad.substring(1,2).equals("x"))) {
									mi = ad.substring(1,2);
									//System.out.println(mi);
								}

								NGUser u = new NGUser(ln,fn,mi,t,ng,ad,d);
								myUsers.add(u);
								ad="";
								ng="";
							}
						}
						fn="";
						mi="";
					}
				}
				ln="";
				t="";
			}
		}
	}
}