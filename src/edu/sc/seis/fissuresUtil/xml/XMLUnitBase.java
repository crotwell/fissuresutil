package edu.sc.seis.fissuresUtil.xml;

import edu.iris.Fissures.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.apache.log4j.*;


/**
 * XMLUnitBase.java
 *
 *
 * Created: Mon Jul  1 15:48:00 2002
 *
 * @author <a href="mailto:">Srinivasa Telukutla</a>
 * @version
 */

public class XMLUnitBase {
    public static void insert(Element element, UnitBase unitBase) {

	Document doc = element.getOwnerDocument();
	element.appendChild(XMLUtil.createTextElement(doc, 
						      "value",
						      ""+unitBase.value()));
    }

}// XMLUnitBase
