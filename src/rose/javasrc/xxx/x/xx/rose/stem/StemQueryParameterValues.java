/********************************************************************************/
/*                                                                              */
/*              StemQueryParameterValues.java                                   */
/*                                                                              */
/*      Find original values of parameters for a call                           */
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import xxx.x.xx.ivy.xml.IvyXml;
import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.bud.BudValue;
import xxx.x.xx.rose.root.RoseException;

class StemQueryParameterValues extends StemQueryBase
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private Set<String>     parameter_set;


/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

StemQueryParameterValues(StemMain ctrl,Element xml)
{
   super(ctrl,xml);
   parameter_set = new HashSet<>();
   for (Element p : IvyXml.children(xml,"PARAMETER")) {
      parameter_set.add(IvyXml.getAttrString(p,"NAME"));
    }
   if (parameter_set.isEmpty()) parameter_set = null;
}



/********************************************************************************/
/*                                                                              */
/*      Processing methods                                                      */
/*                                                                              */
/********************************************************************************/

void process(StemMain sm,IvyXmlWriter xw) throws RoseException
{
   // get the values
   
   Map<String,BudValue> pvals = bud_launch.getParameterValues();
   
   // then output the results
   
   xw.begin("RESULT");
   for (Map.Entry<String,BudValue> ent : pvals.entrySet()) {
      String nm = ent.getKey();
      if (parameter_set != null && !parameter_set.contains(nm)) continue;
      BudValue bv = ent.getValue();
      if (bv != null) {
         xw.begin("PARAMETER");
         xw.field("NAME",nm);
         bv.outputXml(xw);
         xw.end("PARAMETER");
       }
    }
   xw.end("RESULT");
}




}       // end of class StemQueryParameterValues




/* end of StemQueryParameterValues.java */

