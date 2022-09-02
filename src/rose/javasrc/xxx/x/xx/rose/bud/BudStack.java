/********************************************************************************/
/*                                                                              */
/*              BudStack.java                                                   */
/*                                                                              */
/*      Representation of the call stack                                        */
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



package xxx.x.xx.rose.bud;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import xxx.x.x.ivy.xml.IvyXml;

public class BudStack implements BudConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private List<BudStackFrame> stack_frames;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

BudStack(Element xml)
{
   stack_frames = new ArrayList<>();
   for (Element felt : IvyXml.children(xml,"STACKFRAME")) {
      BudStackFrame bsf = new BudStackFrame(felt);
      stack_frames.add(bsf);
    }
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

public List<BudStackFrame> getFrames()
{
   return stack_frames;
}


public BudStackFrame getTopFrame()
{
   for (BudStackFrame bsf : stack_frames) {
      if (bsf.isUserFrame()) return bsf;
    }
   
   return null;
}


}       // end of class BudStack




/* end of BudStack.java */

