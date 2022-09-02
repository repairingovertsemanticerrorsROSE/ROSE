/********************************************************************************/
/*                                                                              */
/*              ValidateRunner.java                                             */
/*                                                                              */
/*      Do a validation for a potential repair                                  */
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



package xxx.x.xx.rose.validate;

import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.root.RootConstants.PriorityTask;
import xxx.x.xx.rose.root.RootProcessor;
import xxx.x.xx.rose.root.RootRepair;
import xxx.x.xx.rose.root.RootTask;
import xxx.x.xx.rose.root.RootTestCase;
import xxx.x.xx.rose.root.RoseLog;

class ValidateRunner extends RootTask implements ValidateConstants, PriorityTask
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private ValidateContext         base_context;
private RootProcessor           root_processor;
private RootRepair              for_repair;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

ValidateRunner(ValidateContext ctx,RootProcessor rp,RootRepair rr)
{
   base_context = ctx;
   root_processor = rp;
   for_repair = rr;
}



/********************************************************************************/
/*                                                                              */
/*      Do the validation                                                       */
/*                                                                              */
/********************************************************************************/

@Override public void run()
{
   // check if we want to use SEEDE here rather than after edit
   if (!base_context.canCheckResult()) {
      noteDone();
      return;
    }
   
   ValidateExecution ve = base_context.getSubsession(for_repair);
   if (ve == null) {
      sendRepair();
      noteDone();
      return;
    }
   
   String ssid = ve.getSessionId();
   try { 
      IvyXmlWriter xw = new IvyXmlWriter();
      for_repair.getEdit().outputXml(xw);
      String cnts = xw.toString();
      xw.close();
      RoseLog.logD("VALIDATE","VALIDATE FOR " + ssid + ": " + cnts);
      
      String sts = base_context.handleEdits(ssid,cnts);
      switch (sts) {
         case "OK" :
            break;
         case "FAIL" :
         case "ERROR" :
            return;
         case "WARNING" :
            break;
         default :
            RoseLog.logE("VALIDATE","Unknown status from edit: " + sts);
            break; 
       }
      
      ve.start(root_processor.getController());
      
      double score = base_context.checkValidResult(ve);
      if (score > 0) {
         RootTestCase tc = base_context.getProblem().getCurrentTest();
         RoseLog.logD("VALIDATE","Use test case " + tc + " " + score);
         if (tc != null) {
            double tscore = ve.checkTest(tc);
            score *= tscore;
          }
       }
      
      base_context.noteSeedeLength(ve.getExecutionTime(),for_repair,score);
      
      if (score > MINIMUM_SCORE) {
         for_repair.noteValidateScore(score);
         sendRepair();
       }
    }
   finally {
      base_context.removeSubsession(ssid);
      noteDone();
    }
}



private void sendRepair()
{
   root_processor.sendRepair(for_repair);
}



@Override public double getTaskPriority() 
{
   return for_repair.getPriority();
}


}       // end of class ValidateRunner








/* end of ValidateRunner.java */

