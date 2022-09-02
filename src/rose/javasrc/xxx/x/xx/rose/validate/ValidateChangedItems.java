/********************************************************************************/
/*										*/
/*		ValidateChangedItems.java					*/
/*										*/
/*	Handle finding changed items for a stack frame				*/
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



package xxx.x.xx.rose.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import xxx.x.xx.rose.bud.BudLaunch;
import xxx.x.xx.rose.bud.BudStack;
import xxx.x.xx.rose.bud.BudStackFrame;
import xxx.x.xx.rose.bud.BudValue;
import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RootProblem;
import xxx.x.xx.rose.thorn.ThornChangeData;
import xxx.x.xx.rose.thorn.ThornFactory;
import xxx.x.xx.rose.thorn.ThornConstants.ThornVariable;
import xxx.x.xx.rose.thorn.ThornConstants.ThornVariableType;

class ValidateChangedItems implements ValidateConstants
{


/********************************************************************************/
/*										*/
/*	Private Storage 							*/
/*										*/
/********************************************************************************/

private RootControl	root_control;
private BudLaunch	for_launch;
private String		for_frame;
private RootProblem     for_problem;
private ThornChangeData change_data;



/********************************************************************************/
/*										*/
/*	Constructors								*/
/*										*/
/********************************************************************************/

ValidateChangedItems(BudLaunch bl,String frame,RootProblem prob)
{
   root_control = bl.getControl();
   for_launch = bl;
   for_frame = frame;
   for_problem = prob;
   
   ThornFactory tf = new ThornFactory(root_control);
   change_data = tf.getChangedVariables(for_launch,for_problem,for_frame);
}



/********************************************************************************/
/*										*/
/*	Processing methods: Simple parameters           			*/
/*										*/
/********************************************************************************/

List<ValidateAction> getParameterActions()
{
   Collection<ThornVariable> items = change_data.getTopParameters();

   if (items == null) return null;

   List<ValidateAction> rslt = new ArrayList<>();

   Set<ThornVariable> params = new HashSet<>();
   for (ThornVariable tv : items) {
      if (tv.getVariableType() == ThornVariableType.PARAMETER) {
	 params.add(tv);
       }
    }
   if (!params.isEmpty()) {
      Map<String,BudValue> pvals = for_launch.getParameterValues();
      if (pvals != null) {
         for (ThornVariable tv : params) {
            BudValue bv = pvals.get(tv.getName());
            if (bv != null) rslt.add(ValidateAction.createSetAction(tv.getName(),bv));
          }
       }
    }

   return rslt;
}


/********************************************************************************/
/*                                                                              */
/*      Processing methods -- all possible actions                              */
/*                                                                              */
/********************************************************************************/

List<ValidateAction> getResetActions(ValidateContext ctx)
{
   BudStack bs = for_launch.getStack();
   BudStackFrame startframe = null;
   for (BudStackFrame bsf : bs.getFrames()) {
      if (bsf.getFrameId().equals(for_frame)) {
         startframe = bsf;
         break;
       }
    }
   
   ValidateExecution ve = ctx.getBaseExecution();
   ValidateTrace vt = ve.getSeedeResult();
   ValidateCall vc = vt.getRootContext();
   
   ValidateSetup vs = new ValidateSetup(ctx,change_data,startframe,vc);

   List<ValidateAction> rslt = vs.findResets();
   
   return rslt;
}



}	// end of class ValidateChangedItems




/* end of ValidateChangedItems.java */

