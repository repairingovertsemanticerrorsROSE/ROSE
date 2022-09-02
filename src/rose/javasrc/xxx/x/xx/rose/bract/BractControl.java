/********************************************************************************/
/*                                                                              */
/*              BractControl.java                                               */
/*                                                                              */
/*      Semantic quick fix (quickrepair) controller                             */
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



package xxx.x.xx.rose.bract;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import xxx.x.x.ivy.mint.MintConstants.CommandArgs;
import xxx.x.x.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RootLocation;
import xxx.x.xx.rose.root.RootMetrics;
import xxx.x.xx.rose.root.RootProblem;
import xxx.x.xx.rose.root.RootProcessor;
import xxx.x.xx.rose.root.RootRepair;
import xxx.x.xx.rose.root.RootThreadPool;
import xxx.x.xx.rose.root.RootValidate;
import xxx.x.xx.rose.root.RoseLog;
import xxx.x.xx.rose.root.RootRepairFinder;
import xxx.x.xx.rose.root.RootTask;

public class BractControl extends Thread implements RootProcessor, BractConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private RootControl rose_control;
private String reply_id;
private RootProblem for_problem;
private RootLocation at_location;
private List<Class<?>> processor_classes;
private List<Class<?>> location_classes;
private RootValidate base_validator;
private List<RootTask> sub_tasks;
private long    start_time;
private int     num_checked;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

BractControl(RootControl ctrl,String id,RootProblem prob,RootLocation at,
        List<Class<?>> pclass,List<Class<?>> lclass,RootValidate validator)
{ 
   super("BractControl_" + id);
   rose_control = ctrl;
   reply_id = id;
   for_problem = prob;
   at_location = at;
   processor_classes = pclass;
   location_classes = lclass;
   base_validator = validator;
   sub_tasks = new ArrayList<>();
}





/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

public RootControl getController()              { return rose_control; }



/********************************************************************************/
/*                                                                              */
/*      Main processing method                                                  */
/*                                                                              */
/********************************************************************************/

@Override public void run()
{
   try {
      work();
    }
   catch (Throwable t) {
      RoseLog.logE("BRACT","Problem executing control task",t);
    }
}



private void work()
{
   List<ProcessorTask> tasks = new ArrayList<>();
   start_time = System.currentTimeMillis();
   num_checked = 0;
   
   List<RootLocation> uselocs = null;
   if (at_location == null && location_classes.size() > 0) {
      uselocs = getLocations(); 
    }
   else {
      uselocs = new ArrayList<>();
      uselox.add(at_location);
    }
   
   RoseLog.logI("BRACT","Start processing " + uselox.size() + " " +
         location_classes.size());
   
   if (for_problem != null) {
      for (Class<?> cls : processor_classes) {
         ProcessorTask pt = startTask(cls,for_problem,null);
         if (pt != null) tasks.add(pt);
       }
      for (Class<?> cls : location_classes) {
         for (RootLocation loc : uselocs) {
            ProcessorTask pt = startTask(cls,for_problem,loc);
            if (pt != null) tasks.add(pt);
          }
       }
    }
   
   for (ProcessorTask pt : tasks) {
      pt.waitForDone();
    }
   
   while (!sub_tasks.isEmpty()) {
      List<RootTask> t = null;
      synchronized (sub_tasks) {
         t = new ArrayList<>(sub_tasks);
         sub_tasks.clear();
       }
      for (RootTask rt : t) rt.waitForDone();
    }
   
   long t1 = System.currentTimeMillis();
   RootMetrix.noteCommand("BRACT","ENDREPAIR",t1-start_time,num_checked);
   CommandArgs args = new CommandArgs("NAME",reply_id,"CHECKED",num_checked);
   rose_control.sendRoseMessage("ENDSUGGEST",args,null,-1);
}


@Override public void addSubtask(RootTask rt)
{
   synchronized (sub_tasks) {
      sub_tasks.add(rt);
    }
}


private ProcessorTask startTask(Class<?> cls,RootProblem p,RootLocation l)
{
   Constructor<?> cnst = null;
   try {
      cnst = cls.getConstructor();
    }
   catch (NoSuchMethodException e) { }
   if (cnst == null) return null;
   RootRepairFinder rrf = null;
   try {
      rrf = (RootRepairFinder) cnst.newInstance();
      rrf.setup(this,for_problem,l);
    }
   catch (Throwable t) { }
   if (rrf == null) return null;
   ProcessorTask pt = new ProcessorTask(rrf,base_validator);
   RoseLog.logD("BRACT","Queue finder " + rrf.getClass() + " " + l.getLineNumber());
   RootThreadPool.start(pt);
   return pt;
}




private List<RootLocation> getLocations()
{
   // compute the set of all locations -- use stem processing?
   return rose_control.getLocations(for_problem);
}



/********************************************************************************/
/*                                                                              */
/*      Access methods for repair finders                                       */
/*                                                                              */
/********************************************************************************/

@Override public void validateRepair(RootRepair br)
{
   ++num_checked;
   
   if (base_validator != null) {
      base_validator.validateAndSend(this,br);
    }
   else {
      sendRepair(br);
    }
}



@Override public void sendRepair(RootRepair br)
{
   long time = System.currentTimeMillis() - start_time;
   br.setTime(time);
   
   CommandArgs args = new CommandArgs("NAME",reply_id);
   
   IvyXmlWriter xw = new IvyXmlWriter();
   br.outputXml(xw);
   String body = xw.toString();
   
   long t1 = System.currentTimeMillis();
   RootMetrix.noteCommand("BRACT","SENDREPAIR",
         br.getPriority(),br.getValidatedPriority(),t1-start_time,br.getId(),br.getLogData(),br.getDescription());
   
   rose_control.sendRoseMessage("SUGGEST",args,body,0);
}


@Override public boolean haveGoodResult()
{
   if (base_validator == null) return false;
   return base_validator.haveGoodResult();
}

/********************************************************************************/
/*                                                                              */
/*      Processor task                                                          */
/*                                                                              */
/********************************************************************************/

private static class ProcessorTask extends RootTask implements PriorityTask {
   
   private RootRepairFinder repair_finder;
   private RootValidate repair_validate;
   
   ProcessorTask(RootRepairFinder brf,RootValidate rv) {
      repair_finder = brf;
      repair_validate = rv;
    }
   
   @Override public void run() {
      RootLocation loc = repair_finder.getLocation();
      if (loc == null) {
         RoseLog.logD("BRACT","Start repair finder " + repair_finder.getClass());
       }
      else {
         RoseLog.logD("BRACT","Start repair finder " + repair_finder.getClass() + 
               " at " + loc.getFile() + " " + loc.getLineNumber());
       }
   
      try {
         if (repair_validate.canCheckResult()) {
            repair_finder.process();
          }
         else {
            RoseLog.logD("BRACT","Skipping repair");
          }
       }
      catch (Throwable t) {
         RoseLog.logE("BRACT","Problem in repair finder",t);
       }
      finally {
         RoseLog.logD("BRACT","Finish repair finder " + repair_finder.getClass());
         synchronized (this) {
            noteDone();
          }
       }
    }
   
   
   
   @Override public double getTaskPriority() {
      double v = repair_finder.getFinderPriority() * 0.5;
      if (repair_finder.getLocation() != null) {
         double lv = repair_finder.getLocation().getPriority();
         v += lv * 0.01;
       }
      
      return v;
    }
   
}       // end of inner class ProcessorTask


}       // end of class BractControl




/* end of BractControl.java */

