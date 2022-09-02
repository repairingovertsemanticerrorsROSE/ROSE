/********************************************************************************/
/*                                                                              */
/*              StemQueryExpressionHistory.java                                 */
/*                                                                              */
/*      Handle queries for expressions                                          */
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
import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.root.RootProblem;
import xxx.x.xx.rose.root.RoseException;

class StemQueryExpressionHistory extends StemQueryHistory
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private String expression_name;
private String current_value;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

StemQueryExpressionHistory(StemMain ctrl,RootProblem prob)
{ 
   super(ctrl,prob);
   expression_name = prob.getProblemDetail();
   current_value = prob.getOriginalValue();
//    shouldbe_value = prob.getTargetValue();
}
   

/********************************************************************************/
/*                                                                              */
/*      Process expression query                                                */
/*                                                                              */
/********************************************************************************/

@Override void process(StemMain stem,IvyXmlWriter xw) throws RoseException 
{
   Element hrslt = getHistoryData(stem);
   outputGraph(hrslt,xw);
}



/********************************************************************************/
/*                                                                              */
/*      Set up appropriate query                                                */
/*                                                                              */
/********************************************************************************/

private Element getHistoryData(StemMain stem)
{
   stem.waitForAnalysis();
   
   CommandArgs args = new CommandArgs("FILE",for_file.getAbsolutePath(),
         "QTYPE","EXPRESSION",
         "CURRENT",current_value,
         "LINE",line_number,
         "TOKEN",expression_name,
         "METHOD",method_name);
   
   String qxml = null;
   if (node_context != null) {
      IvyXmlWriter xw = new IvyXmlWriter();
      node_context.outputXml("EXPRESSION",xw);
      qxml = xw.toString();
      xw.close();
    }
   String sxml = getXmlForStack();
   if (qxml == null) qxml = sxml;
   else if (sxml != null) qxml += sxml; 
   
   Element rslt = stem.sendFaitMessage("FLOWQUERY",args,qxml);
   
   return rslt;
}








}       // end of class StemQueryExpressionHistory




/* end of StemQueryExpressionHistory.java */

