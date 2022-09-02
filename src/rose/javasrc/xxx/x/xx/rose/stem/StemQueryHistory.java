/********************************************************************************/
/*										*/
/*		StemQueryHistory.java						*/
/*										*/
/*	Handle history request to get potential location information		*/
/*										*/
/********************************************************************************/
/*********************************************************************************
 *										 *
 *			  All Rights Reserved					 *
 *										 *
 *  Permission to use, copy, modify, and distribute this software and its	 *
 *  documentation for any purpose other than its incorporation into a		 *
 *  commercial product is hereby granted without fee, provided that the 	 *
 *  above copyright notice appear in all copies and that both that		 *
 *  copyright notice and this permission notice appear in supporting		 *
 *  documentation, and that the name of Anonymous Institution X not be used in 	 *
 *  advertising or publicity pertaining to distribution of the software 	 *
 *  without specific, written prior permission. 				 *
 *										 *
 *  x UNIVERSITY DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS		 *
 *  SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND		 *
 *  FITNESS FOR ANY PARTICULAR PURPOSE.  IN NO EVENT SHALL x UNIVERSITY	 *
 *  BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY 	 *
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,		 *
 *  WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS		 *
 *  ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE 	 *
 *  OF THIS SOFTWARE.								 *
 *										 *
 ********************************************************************************/



package xxx.x.xx.rose.stem;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import xxx.x.xx.ivy.xml.IvyXml;
import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.bract.BractFactory;
import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RootLocation;
import xxx.x.xx.rose.root.RootMetrics;
import xxx.x.xx.rose.root.RootProblem;
import xxx.x.xx.rose.root.RoseException;

abstract class StemQueryHistory extends StemQueryBase implements StemConstants
{


/********************************************************************************/
/*										*/
/*	Factory methods 							*/
/*										*/
/********************************************************************************/

static StemQueryHistory createHistoryQuery(StemMain ctrl,RootProblem prob)
{
   // Rather than passing xml, the RootProblem should include an optional expession
   //   context (START/END/NODETYPE/NODETYPEID/AFTER/AFTERSTART/AFTEREND/AFTERTYPE/
   //   AFTERTYPEID).
   
   switch (prob.getProblemType()) {
      case VARIABLE :
	 return new StemQueryVariableHistory(ctrl,prob);
      case EXPRESSION :
         return new StemQueryExpressionHistory(ctrl,prob);
      case EXCEPTION :
         return new StemQueryExceptionHistory(ctrl,prob);
      case ASSERTION :
         return new StemQueryAssertionHistory(ctrl,prob);
      case OTHER :
      case NONE :
      case LOCATION :
         return new StemQueryLocationHistory(ctrl,prob);
    }

   return null;
}



/********************************************************************************/
/*										*/
/*	Constructors								*/
/*										*/
/********************************************************************************/

protected StemQueryHistory(StemMain ctrl,RootProblem prob)
{
   super(ctrl,prob);
}



/********************************************************************************/
/*										*/
/*	Process methods 							*/
/*										*/
/********************************************************************************/

abstract void process(StemMain stem,IvyXmlWriter xw) throws RoseException;




/********************************************************************************/
/*                                                                              */
/*      Helper methods                                                          */
/*                                                                              */
/********************************************************************************/

protected void outputGraph(Element hrslt,IvyXmlWriter xw) throws RoseException
{
// RoseLog.logD("STEM","HISTORY RESULT: " + IvyXml.convertXmlToString(hrslt));
   
   if (hrslt == null) throw new RoseException("Can't find history");
   xw.begin("RESULT");
   if (for_problem != null) for_problem.outputXml(xw);
   xw.begin("NODES");
   int lsz = 0;
   int tsz = 0;
   long ttim = 0;
   for (Element qrslt : IvyXml.children(hrslt,"QUERY")) {
      Element grslt = IvyXml.getChild(qrslt,"GRAPH");
      int sz = IvyXml.getAttrInt(grslt,"SIZE");
      tsz += sz;
      ttim += IvyXml.getAttrLong(grslt,"TIME");
      if (sz > 0) lsz += processGraphNodes(grslt,xw);
    }
   xw.end("NODES");
   xw.end("RESULT");
   
   RootMetrixx.noteCommand("STEM","HISTORYRESULT",tsz,lsz,ttim);
}



private int processGraphNodes(Element gelt,IvyXmlWriter xw)
{
   Map<String,GraphNode> locs = new HashMap<>();
   
   for (Element nelt : IvyXml.children(gelt,"NODE")) {
      GraphNode gn = new GraphNode(stem_control,nelt);
      if (!gn.isValid()) continue;
      String id = gn.getLocationString();
      GraphNode ogn = loxx.get(id);
      if (ogn != null) {
         if (ogn.getPriority() >= gn.getPriority()) continue;
       }
      loxx.put(id,gn);
    }
   for (GraphNode gn : loxx.values()) {
      gn.outputXml(xw);
    }
   
   return loxx.size();
}




private static class GraphNode {
    
   private RootLocation node_location;
   private double node_priority;
   private String node_reason;
   private String node_type;
   
   GraphNode(RootControl ctrl,Element nelt) {
      Element locelt = IvyXml.getChild(nelt,"LOCATION");
      node_location = BractFactory.getFactory().createLocation(ctrl,locelt);
      node_reason = IvyXml.getAttrString(nelt,"REASON");
      node_priority = IvyXml.getAttrDouble(nelt,"PRIORITY",0.5);
      Element point = IvyXml.getChild(nelt,"POINT");
      node_type = IvyXml.getAttrString(point,"NODETYPE");
    }
   
   boolean isValid() {
      if (node_location == null || node_reason == null) return false;
      if (node_location.getFile() == null) return false;
      if (!node_location.getFile().exists()) return false;
      if (node_location.getLineNumber() <= 0) return false;
      if (node_type == null) return false;
      switch (node_type) {
         case "MethodDeclaration" :
            return false;
         default :
            
       }
      
      return true;
    }
   
   double getPriority()                    { return node_priority; }
   
   String getLocationString() {
      String s = node_location.getFile().getPath();
      s += "@" + node_location.getLineNumber();
      s += ":" + node_location.getStartOffset();
      s += "-" + node_location.getEndOffset();
      return s;
    }
   
   void outputXml(IvyXmlWriter xw) {
      xw.begin("NODE");
      xw.field("PRIORITY",node_priority);
      xw.field("REASON",node_reason);
      node_location.outputXml(xw);
      xw.end("NODE");
    }
   
}       // end of inner class GraphNode





}	// end of class StemQueryHistory




/* end of StemQueryHistory.java */

