/********************************************************************************/
/*                                                                              */
/*              StemQueryChangedItems.java                                      */
/*                                                                              */
/*      Find the variables/fields/etc. that changed since the entry to method   */
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

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.w3c.dom.Element;

import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.root.RoseException;
import xxx.x.xx.rose.thorn.ThornChangeData;
import xxx.x.xx.rose.thorn.ThornFactory;
import xxx.x.xx.rose.thorn.ThornConstants.ThornVariable;

class StemQueryChangedItems extends StemQueryBase
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/


/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

StemQueryChangedItems(StemMain ctrl,Element xml)
{
   super(ctrl,xml);
}



/********************************************************************************/
/*                                                                              */
/*      Processing methods                                                      */
/*                                                                              */
/********************************************************************************/

void process(StemMain sm,IvyXmlWriter xw) throws RoseException
{
   ASTNode n = getResolvedSourceStatement();
   if (n == null) throw new RoseException("Can't find starting statement");
   ThornFactory tf = new ThornFactory(sm);
   ThornChangeData tcd = tf.getChangedVariables(bud_launch,for_problem,frame_id);
   
   if (tcd == null) throw new RoseException("Problem finding change data");
   List<ThornVariable> rslt = tcd.getTopParameters();
   if (rslt == null) throw new RoseException("Problem finding changed variables");
   
   xw.begin("RESULT");
   for (ThornVariable tv : rslt) {
      xw.begin("VARIABLE");
      xw.field("TYPE",tv.getVariableType());
      xw.field("NAME",tv.getName());
      xw.end("VARIABLE");
    }
   
   xw.end("RESULT");
}





}       // end of class StemQueryChangedItems




/* end of StemQueryChangedItems.java */

