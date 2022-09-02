/********************************************************************************/
/*                                                                              */
/*              SepalSequencer.java                                             */
/*                                                                              */
/*      Find patch suggestions using Sequncer                                   */
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



package xxx.x.xx.rose.sepal;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;
import org.w3c.dom.Element;

import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RootRepairFinderDefault;
import xxx.x.xx.sequencer.application.ApplicationQuery;
import xxx.x.xx.sequencer.application.ApplicationServerRegulation;
import xxx.x.xx.sequencer.repair.RepairResult;

public class SepalSequencer extends RootRepairFinderDefault
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/


private ApplicationQuery        sequencer_connect;

private boolean use_sequencer = false;

private static final int MAX_LOCAL_CHECK = 5;
private static final int MAX_LOCAL_RESULTS = 5;
private static final double SEARCH_THRESHOLD = 0.01;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

public SepalSequencer()
{
   sequencer_connect = null;
}


@Override synchronized protected void localSetup()
{
// if (pingServer()) return;
   
// String [] args = new String[] { };
// ApplicationServerRegulation asr = new ApplicationServerRegulation(args);
// asr.startServer();
   
   sequencer_connect = new ApplicationQuery();
   if (!sequencer_connect.ping()) {
      String [] args = new String[] { };
      ApplicationServerRegulation asr = new ApplicationServerRegulation(args);
      asr.startServer();
    }
}



/********************************************************************************/
/*                                                                              */
/*      Processing methods                                                      */
/*                                                                              */
/********************************************************************************/

@Override public double getFinderPriority()
{
   return 0.30;
}



@Override public void process()
{
   if (getProcessor().haveGoodResult() || !use_sequencer) return;
   
   RootControl ctrl = getProcessor().getController();
   Statement stmt = (Statement) getResolvedStatementForLocation(null);
   if (stmt == null) return;
   
   File bfile = getLocation().getFile();
   int lno = getLocation().getLineNumber();
   String bcnts = ctrl.getSourceContents(bfile);
   List<RepairResult> rslts = null;
   if (sequencer_connect.ping()) {
      rslts = sequencer_connect.execute(bfile.getPath(),bcnts,lno);
    }
   if (rslts == null || rslts.isEmpty()) return;
   // need to sort the results -- skip first, ignore changes
   // covered by other suggesters, etc.
   // score is meaningless.
   // limit to 3-5 per suggestion
   int rct = 0;
   int fnd = 0;
   // probably want to assign priority and sort the results
   for (RepairResult rr : rslts) {
      double f = rr.getScore();
      if (f < SEARCH_THRESHOLD) continue;
      if (!isRepairRelevant(rr,lno)) continue;
      Element edit = rr.getTextEditXML();
      if (edit == null) continue;
      if (++rct > MAX_LOCAL_RESULTS) break;
      String logdata = getClass().getName() + "@" + rr.getType() + "@" + f;
      String desc = rr.getDescription();
      addRepair(edit,desc,logdata,f);
      if (++fnd > MAX_LOCAL_CHECK) break;
    }
}



private boolean isRepairRelevant(RepairResult rr,int lno) 
{
   String typ = rr.getType();
   int rlno = rr.getLineNumber();
   
   switch (typ) {
      case "REPLACE_OP" :
      case "REPLACE_NAME" :
      case "REPLACE_LITERAL" :
      case "DELETE" :
         return false;
      case "REPLACE_EXPR" :
      case "REPLACE_STATEMENT" :
      case "REPLACE" :
      case "REPLACE_PRIMITIVE_TYPE" :
         if (rlno != lno) 
            return false;
         break;
      default :
         if (rlno != lno) return false;
         break;
    }
   
   // reject illogical repairs as well
   // ( x < x ), (x). (E && E). E && (E). E || E. E || (E)
   
   return true;
}





}       // end of class SepalSequencer




/* end of SepalSequencer.java */

