package edu.sc.seis.fissuresUtil.netConnChecker;

//import edu.sc.seis.sac.*;
import edu.sc.seis.fissuresUtil.namingService.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import org.apache.log4j.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;



/**
 * Description: Checks for CORBA Connections. There are two constructors for this. The first constructor
 * takes a CORBA Object and  description. The second constructor takes the dns, interfaceName, objectName,
 * description and fissuresNamingService as parameters.
 *
 * CorbaChecker.java
 *
 * @author <a href="mailto:">Srinivasa Telukutla</a>
 * $Id: CorbaChecker.java 7394 2004-03-03 22:14:06Z crotwell $
 * @version 1.0
 */
public class CorbaChecker extends ConcreteConnChecker  {
    /**
     * Creates a new <code>CorbaChecker</code> instance.
     *
     * @param obj an <code>org.omg.CORBA.Object</code> value
     * @param description a <code>String</code> value
     */
    public CorbaChecker(org.omg.CORBA.Object obj, String description){
        super(description);
        this.obj = obj;
    }

    /** Pre: A Runnable thread calls run()
     *  Post: Attempt to make a Corba connection
     */
    public void run()  {
        try {
            if(obj._non_existent() == true){
                reason = "got non existent";
                setFinished(true);
                setTrying(false);
                setSuccessful(false);
                fireStatusChanged(getDescription(), ConnStatus.FAILED);
            }else {
                setFinished(true);
                setTrying(false);
                setSuccessful(true);
                fireStatusChanged(getDescription(), ConnStatus.SUCCESSFUL);
            }
            return;
        } catch(COMM_FAILURE cf){
            cause = cf;
            setFinished(true);
            setTrying(false);
            setSuccessful(false);
            fireStatusChanged(getDescription(), ConnStatus.FAILED);
        }// close run
    }

    private org.omg.CORBA.Object obj;
    static Category logger = Category.getInstance(CorbaChecker.class);


}// CorbaChecker class
