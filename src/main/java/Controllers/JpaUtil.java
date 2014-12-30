/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Gabriel
 */
public class JpaUtil {    
       
    private final static String PU = "SistemaTactical1.1PU";
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static final Logger LOG = Logger.getLogger(JpaUtil.class.getName());

    public static EntityManager getEntityManager() {
        if (em == null) {
            JpaUtil.emf = Persistence.createEntityManagerFactory(PU);
            LOG.info("em CREADO");
        }
        em = emf.createEntityManager();
        return em;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return JpaUtil.emf;
    }   
   
    

    
}
