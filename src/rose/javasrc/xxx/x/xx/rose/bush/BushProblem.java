/********************************************************************************/
/*                                                                              */
/*              BushProblem.java                                                */
/*                                                                              */
/*      description of class                                                    */
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



package xxx.x.xx.rose.bush;

import xxx.x.x.bubbles.bump.BumpConstants.BumpLaunch;
import xxx.x.x.bubbles.bump.BumpConstants.BumpStackFrame;
import xxx.x.x.bubbles.bump.BumpConstants.BumpThread;
import xxx.x.xx.rose.root.RootLocation;
import xxx.x.xx.rose.root.RootNodeContext;
import xxx.x.xx.rose.root.RootProblem;

class BushProblem extends RootProblem implements BushConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private BumpStackFrame  stack_frame;


/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

BushProblem(BumpStackFrame frame,RoseProblemType typ,String item,String orig,String tgt,RootNodeContext ctx)
{
   super(typ,item,orig,tgt,ctx);
   setBugFrame(frame.getThread().getLaunch().getId(),frame.getThread().getId(),frame.getId());  
   if (orig != null && orig.startsWith("(")) {
      int idx = orig.indexOf(") ");
      String vtyp = orig.substring(1,idx);
      String val = orig.substring(idx+2);
      orig = vtyp + " " + val;
      setOriginalValue(orig);
    }
   stack_frame = frame;
   RootLocation floc = new BushLocation(frame);
   setBugLocation(floc);
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

BumpStackFrame getFrame()
{
   return stack_frame;
}


BumpThread getThread()
{
   return stack_frame.getThread();
}


BumpLaunch getLaunch()
{
   return stack_frame.getThread().getLaunch();
}



}       // end of class BushProblem




/* end of BushProblem.java */

