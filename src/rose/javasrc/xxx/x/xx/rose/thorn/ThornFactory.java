/********************************************************************************/
/*                                                                              */
/*              ThornFactory.java                                               */
/*                                                                              */
/*      Factory for recreating conditions at start of method                    */
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



package xxx.x.xx.rose.thorn;


import xxx.x.xx.rose.bud.BudLaunch;
import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RootProblem;

public class ThornFactory implements ThornConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private RootControl     rose_control;


/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

public ThornFactory(RootControl rc)
{
   rose_control = rc;
}


/********************************************************************************/
/*                                                                              */
/*      Find changed variables                                                  */
/*                                                                              */
/********************************************************************************/

public ThornChangeData getChangedVariables(BudLaunch bl,RootProblem prob,String topframe)
{
   ThornChangedFinder tcf = new ThornChangedFinder(rose_control);
   ThornChangeData tcd = tcf.process(bl,prob,topframe);
   
   return tcd;
}



}       // end of class ThornFactory




/* end of ThornFactory.java */

