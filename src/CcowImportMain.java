/**
 * 
 */
package com.bjc.ekk.ccowhib;

import java.util.ArrayList;


//import javax.persistence.Query;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author ekk9418
 *
 */
public class CcowImportMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//parse the xml
		ArrayList<CcowUser> user = new ParsedXML("c:/Tools/","uma.xml").getMyUsers();
		
		
		//clear db before adding users
		Session cleardb = HibernateUtil.getSessionFactory().openSession();
		cleardb.beginTransaction();
		Query hqlClear = cleardb.createQuery("delete from CcowUser");
		
		hqlClear.executeUpdate();
		cleardb.getTransaction().commit();
		cleardb.close();
		
		
		//add users
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		int index = 0;
		for (CcowUser u : user) {
			//System.out.println(u.toString());
			session.save(u);
			index++;
			
			//for batch loading
			if (index % 15 == 0) { //number here needs to match hibernate.jdbc.batch_size
				session.flush();
				session.clear();
				//System.out.println("flushed");
			}
		}

		session.flush();
		session.clear();
		
		session.getTransaction().commit();
        //System.out.println("success");
 
        session.close();
        //System.out.println("closed");
        
	}
}