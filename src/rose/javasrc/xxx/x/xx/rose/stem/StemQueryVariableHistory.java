/********************************************************************************/
/*                                                                              */
/*              StemQueryVariableHistory.java                                   */
/*                                                                              */
/*      Variable history query                                                  */
/*                                                                              */
/********************************************************************************/
/*********************************************************************************
 *                                                                               *
 *                        All Rights Reserved                                    *
 *                                                                               *
 *  Permission to use, copy, modify, and distribute this software and its        *
 *  documentation for any purpose other than its incorporation into a            *
 *  commercial product is hereby granted without fee, provided that the          *
 *  above copyright notice appear in all copies and that both that               *
 *  copyright notice and this permission notice appear in supporting             *
 *  documentation, and that the name of Anonymous Institution X not be used in          *
 *  advertising or publicity pertaining to distribution of the software          *
 *  without specific, written prior permission.                                  *
 *                                                                               *
 *  x UNIVERSITY DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS                *
 *  SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND            *
 *  FITNESS FOR ANY PARTICULAR PURPOSE.  IN NO EVENT SHALL x UNIVERSITY      *
 *  BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY          *
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,              *
 *  WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS               *
 *  ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE          *
 *  OF THIS SOFTWARE.                                                            *
 *                                                                               *
 ********************************************************************************/



package xxx.x.xx.rose.stem;

import org.w3c.dom.Element;

import xxx.x.xx.ivy.mint.MintConstants.CommandArgs;
import xxx.x.xx.ivy.xml.IvyXml;
import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.root.RootMetrics;
import xxx.x.xx.rose.root.RootProblem;
import xxx.x.xx.rose.root.RoseException;
import xxx.x.xx.rose.root.RoseLog;

class StemQueryVariableHistory extends StemQueryHistory
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private String variable_name;
private String current_value;
private String shouldbe_value;




/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

StemQueryVariableHistory(StemMain ctrl,RootProblem prob)
{
   super(ctrl,prob);
   variable_name = prob.getProblemDetail();
   current_value = prob.getOriginalValue();
   shouldbe_value = prob.getTargetValue();
}


/********************************************************************************/
/*                                                                              */
/*      Process the query                                                       */
/*                                                                              */
/********************************************************************************/

@Override void process(StemMain stem,IvyXmlWriter xw) throws RoseException
{
   
   Element qrslt = getVarData(stem);
   if (qrslt == null) throw new RoseException("Can't find variable");
   
   Element hrslt = getHistoryData(stem,qrslt);
   outputGraph(hrslt,xw);
}




/********************************************************************************/
/*                                                                              */
/*      Get variable and location data                                          */
/*                                                                              */
/********************************************************************************/

private Element getVarData(StemMain stem)
{
   stem.waitForAnalysis();
   
   long start = System.currentTimeMillis();
   
   RoseLog.logD("START VAR Query: " + variable_name + " " + current_value + " " +
         shouldbe_value);
   String method = method_name;
   CommandArgs args = new CommandArgs("FILE",for_file.getAbsolutePath(),
         "START",-1,
         "LINE",line_number,
         "TOKEN",variable_name,
         "METHOD",method);
   Element rslt = stem.sendFaitMessage("VARQUERY",args,null);
   RoseLog.logD("VAR Data: " + IvyXml.convertXmlToString(rslt));
   Element vset = IvyXml.getChild(rslt,"VALUESET");
   
   Element refelt = IvyXml.getChild(vset,"REFERENCE");
   Element refval = IvyXml.getChild(refelt,"VALUE");
   
   RootMetrixx.noteCommand("STEM","VARDATA",IvyXml.getAttrInt(refval,"LOCAL"),
         IvyXml.getAttrInt(refval,"STACK"),IvyXml.getAttrInt(refval,"BASELOCAL"),
         IvyXml.getAttrInt(refval,"BASESTACK"),
         System.currentTimeMillis() - start);

   return vset;
}



/********************************************************************************/
/*                                                                              */
/*      Get history data                                                        */
/*                                                                              */
/********************************************************************************/

private Element getHistoryData(StemMain stem,Element vdata) throws RoseException
{
   CommandArgs args = new CommandArgs("FILE",for_file.getAbsolutePath(),
         "QTYPE","VARIABLE",
         "CURRENT",current_value,
         "LINE",line_number,
         "TOKEN",variable_name,
         "METHOD",method_name);
   
   StringBuffer buf = new StringBuffer();
   Element reference = null;
   for (Element refval : IvyXml.children(vdata,"REFVALUE")) {
      Element loc = IvyXml.getChild(refval,"LOCATION");
      Element ref = IvyXml.getChild(refval,"REFERENCE");
      if (loc == null || ref == null) continue;
      if (reference == null) reference = IvyXml.getChild(ref,"VALUE");
      buf.append(IvyXml.convertXmlToString(loc));
      buf.append("\n");
    }
   if (reference == null) throw new RoseException("No reference for variable");
   buf.append(IvyXml.convertXmlToString(reference));
   String qxml = buf.toString();
   String sxml = getXmlForStack();
   if (sxml != null) qxml += sxml;
   Element rslt = stem.sendFaitMessage("FLOWQUERY",args,qxml);
   
   return rslt;
}


}       // end of class StemQueryVariableHistory




/* end of StemQueryVariableHistory.java */

