/**
 * parsing an xml file using attributes for each piece of data about a user
 */
package com.bjc.ekk.ccowhib;

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
public class ParsedXML {
	ArrayList<CcowUser> myUsers = new ArrayList<CcowUser>();
	String path;
	String file;
	Document dom;
	private final String[] TITLES = {"dnp", "whnp", "pac", "lcsw", "gnp", "aud", "fnp", "aprn", "do", "cfnp", "anp"};

	/**
	 * @return the myUsers
	 */
	public ArrayList<CcowUser> getMyUsers() {
		return myUsers;
	}

	/**
	 * @param myUsers the myUsers to set
	 */
	public void setMyUsers(ArrayList<CcowUser> myUsers) {
		this.myUsers = myUsers;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
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

	public ParsedXML(String p, String f) {
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
		
		//get list of elements
		
		//get the root element from the DOM object
		Element docEle = dom.getDocumentElement();
		
		//get a node list of elements
		NodeList nl = docEle.getElementsByTagName("user");
		
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
		
				//get the user element
				Element el = (Element)nl.item(i);
			
				//get the user object
				CcowUser u = getUser(el);
			
				//add it to list
				myUsers.add(u);
			}
		}
	}
	
	/**
	* take a user element and read the values in, create
	* a user object and return it
	*/
	private CcowUser getUser(Element userEl) {//read data from each user  dom part C

		//for each <user> element get text or int values of
		//uid, givenName, sn, and appEntryList
		String uid = getTextValue(userEl,"uid").toLowerCase();
		String givenName = getTextValue(userEl,"givenName").toLowerCase();
		String sn = getTextValue(userEl, "sn").toLowerCase();
		String mi = "";
		String title = "";
		
		while (givenName.startsWith(" ")) {
			givenName = givenName.substring(1);
		}
		while (givenName.endsWith(" ")) {
			givenName = givenName.substring(0,givenName.length()-1);
		}
		while (sn.startsWith(" ")) {
			sn = sn.substring(1);
		}
		while (sn.endsWith(" ")) {
			sn = sn.substring(0,sn.length()-1);
		}
		
		String[] fmName = givenName.split(" ");
		String[] ltName = sn.split(" ");
		
		if (fmName.length > 1) {//more than 1 string in array
			mi = fmName[fmName.length-1];
			givenName=fmName[0];
			
			if (mi.endsWith(".")) {
				mi = mi.substring(0,mi.length()-1);
			}
			
			if (fmName.length > 2) {
				for (int s=1;s<fmName.length-1;s++) {
					if (!fmName[s].equals(" ")) {
						givenName += " " + fmName[s];
					}
				}
			}
		}
		
		if (mi.equals("") && !(uid.substring(1,2).equals("x"))) {
			mi = uid.substring(1,2);
		}
		
		if (ltName.length > 1 && Arrays.asList(TITLES).contains(ltName[ltName.length-1])) {
			title = ltName[ltName.length-1];
			sn = ltName[0];
		}
		
		if (ltName.length > 2) {
			if (Arrays.asList(TITLES).contains(ltName[ltName.length-2])) {
				sn = ltName[0];
				ltName[ltName.length-2] = ltName[ltName.length-2] + " " + ltName[ltName.length-1];
				title = ltName[ltName.length-2];// + " " + ltName[ltName.length-1];
				//TODO above line gets error when comment is removed, need to do research
				//System.out.println(Integer.toString(1 + ltName[ltName.length-1].length() + ltName[ltName.length-2].length()));
				if (ltName.length > 3) {
					for (int s=1;s<ltName.length-2;s++) {
						if (!ltName[s].equals(" ")) {
							sn += " " + ltName[s];
						}
					}
				}
			}
			else {// last name has at least 3 parts and title only takes last one or no title
				for (int s=1;s<ltName.length-2;s++) {
					if (!ltName[s].equals(" ")) {
						sn += " " + ltName[s];
					}
				}
			}
		}
		
		//Create a new Employee with the value read from the xml nodes
		CcowUser u = new CcowUser(uid,givenName,sn,mi,title);
		
		//get a node list of elements
		NodeList nl = userEl.getElementsByTagName("appEntry");
		
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
		
				//get the appEntry element
				Element el = (Element)nl.item(i);
			
				//get the appentry object
				//add it to list
				u.setAppEntryData(getAppList(el));
				}
			}
	
		return u;
	}
	
	private String/*AppEntry*/ getAppList(Element appEl) {//get the next suffix + id for current user
		String logonSuffix = getTextValue(appEl,"logonSuffix").toLowerCase();
		String logonId = getTextValue(appEl,"logonId").toLowerCase();
		
		//AppEntry ael = new AppEntry(logonSuffix,logonId);
		//getLogonSuffix() + ":" + getLogonId();
		return logonSuffix + ":" + logonId;//ael.toString();
		//System.out.println(ael.toString());
		//return ael;
	}
	
	/**
	* I take an xml element and the tag name, look for the tag and get
	* the text content
	* i.e for <employee><name>John</name></employee> 
	*  if the Element points to employee node and tagName is 'name' I will return John
	*/
	private String getTextValue(Element ele, String tagName) {//dom part C cont.
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}
}