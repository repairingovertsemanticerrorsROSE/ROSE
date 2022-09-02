/********************************************************************************/
/*                                                                              */
/*              StemTest.java                                                   */
/*                                                                              */
/*      Test cases for STEM                                                     */
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.Assert;
import org.w3c.dom.Element;

import xxx.x.xx.ivy.exec.IvyExec;
import xxx.x.xx.ivy.leash.LeashIndex;
import xxx.x.xx.ivy.mint.MintArguments;
import xxx.x.xx.ivy.mint.MintConstants;
import xxx.x.xx.ivy.mint.MintControl;
import xxx.x.xx.ivy.mint.MintDefaultReply;
import xxx.x.xx.ivy.mint.MintHandler;
import xxx.x.xx.ivy.mint.MintMessage;
import xxx.x.xx.ivy.mint.MintReply;
import xxx.x.xx.ivy.xml.IvyXml;
import xxx.x.xx.ivy.xml.IvyXmlWriter;
import xxx.x.xx.rose.bract.BractFactory;
import xxx.x.xx.rose.root.RootProblem;
import xxx.x.xx.rose.root.RootTestCase;
import xxx.x.xx.rose.root.RoseLog;

public class StemTest implements StemConstants, MintConstants 
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private String                  stopped_thread;
private String                  bedrock_id;
private String                  workspace_path;
private Map<String,SuggestionSet> suggest_set;

private static Map<String,MintControl> mint_map = new HashMap<>();
private static Map<String,Integer>     mint_count = new HashMap<>();
private static String           source_id;
private static Random           random_gen = new Random();

private static final String ECLIPSE_PATH_MAC =
   "/vol/Developer/eclipse-2020-03/Eclipse.app/Contents/MacOS/eclipse";
private static final String ECLIPSE_DIR_MAC = "/Users/xxx/Eclipse/";



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

public StemTest()
{
   int rint = random_gen.nextInt(1000000);
   source_id = "STEM_" + rint;
   stopped_thread = null;
   bedrock_id = null;
   workspace_path = null;
   suggest_set = new HashMap<>();
}



/********************************************************************************/
/*                                                                              */
/*      Test using RoseTest suite                                               */
/*                                                                              */
/********************************************************************************/

@Test
public void testRoseNullPointer()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test01";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","EXCEPTION");
      xw.textElement("ITEM","java.lang.NullPointerException");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
      throw t;
    }
   finally {
      shutdownBedrock(workspace);
    }
}




@Test
public void testRoseArrayIndex()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test02";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","EXCEPTION");
      xw.textElement("ITEM","java.lang.ArrayIndexOutOfBoundsException");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();  
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}



@Test
public void testRoseWrongVariable()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test03";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","VARIABLE");
      xw.textElement("ITEM","rslt");
      xw.textElement("ORIGINAL","java.lang.String \"The cow jumped over the moon.\"");
      xw.textElement("TARGET","\"The calf jumped over the moon.\"");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}




@Test
public void testRoseNotNull()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test04";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());     
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","VARIABLE");
      xw.textElement("ITEM","baby");
      xw.textElement("ORIGINAL","null");
      xw.textElement("TARGET","Non-Null");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}


@Test
public void testRoseLocation()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test05";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);

      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());      
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","LOCATION");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}



@Test
public void testRoseString()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test06";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","VARIABLE");
      xw.textElement("ITEM","baby");
      xw.textElement("ORIGINAL","java.lang.String piglet");
      xw.textElement("TARGET","piglets");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}



@Test
public void testRoseLocation_3()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test03";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());     
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","LOCATION");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}


@Test
public void testRoseAssertion_3()
{
   String workspace = "rosetest";
   String project = "rosetest";
   String launch = "test03a";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());     
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","ASSERTION");
      xw.textElement("ITEM","org.junit.ComparisonFailure");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}






/********************************************************************************/
/*                                                                              */
/*      Picot Tests                                                             */
/*                                                                              */
/********************************************************************************/

@Test
public void testPicotRomp()
{
   String workspace = "rompspr";
   String project = "romp";
   String launch = "Romp Collide";
   MintControl mc = setupBedrock(workspace,project);
   
   try {
      FrameData fd = setupTest(mc,project,launch,0);
      
      IvyXmlWriter xw = new IvyXmlWriter();
      xw.begin("PROBLEM");
      xw.field("LAUNCH",fd.getLaunchId());     
      xw.field("FRAME",fd.getId());
      xw.field("THREAD",fd.getThreadId());
      xw.field("TYPE","ASSERTION");
      xw.textElement("ITEM","java.lang.AssertionError");
      fd.outputLocation(xw,project,0.5,mc);
      xw.end("PROBLEM");
      String cnts = xw.toString();
      
      runTest(mc,fd,cnts);
    }
   catch (Throwable t) {
      RoseLog.logE("Problem processing test",t);
    }
   finally {
      shutdownBedrock(workspace);
    }
}




/********************************************************************************/
/*                                                                              */
/*      Test Helper methods                                                     */
/*                                                                              */
/********************************************************************************/

private FrameData setupTest(MintControl ctrl,String project,String launch,int cont)
{
   LaunchData ld = startLaunch(ctrl,project,launch);
   for (int i = 0; i < cont; ++i) {
      continueLaunch(ctrl,project,ld);
    }
   FrameData fd = getTopFrame(ctrl,project,ld);
   setupStem(ctrl,true);
   
   Element xml = sendStemReply(ctrl,"START",null,null);
   Assert.assertTrue(IvyXml.isElement(xml,"RESULT"));
   
   return fd;
}



private void runTest(MintControl ctrl,FrameData fd,String oprob)
{
   getChangedVariables(ctrl,fd,oprob);
   String prob = getStartFrame(ctrl,oprob,null);
   getSuggestionsFor(ctrl,fd,prob,null);
   
   Element locs = getLocations(ctrl,fd,prob);
   
   int lline = 0;
   for (Element loc : IvyXml.children(locs,"LOCATION")) {
      String m = IvyXml.getAttrString(loc,"METHOD");
      if (m == null) continue;
      int idx = m.lastIndexOf(".");
      if (idx > 0) m = m.substring(idx+1);
      if (m.startsWith("test") && !m.contains("code")) continue;
      int line = IvyXml.getAttrInt(loc,"LINE");
      if (line <= 0 || line == lline) continue;
      lline = line;
      String loccnts = IvyXml.convertXmlToString(loc);
      String locprob = getStartFrame(ctrl,oprob,loccnts);
     
      getSuggestionsFor(ctrl,fd,locprob,loc);
    }
}
   



private Element getLocations(MintControl ctrl,FrameData fd,String prob)
{
   CommandArgs args = new CommandArgs("TYPE","EXCEPTION",
         "METHOD",fd.getMethod(),
         "LINE",fd.getLine(),
         "CLASS",fd.getClassName(),
         "FILE",fd.getSourceFile(ctrl),
         "PROJECT",fd.getProject(ctrl),
         "LAUNCH",fd.getLaunchId(),
         "FRAME",fd.getId(),
         "THREAD",fd.getThreadId() );
   Element xml = sendStemReply(ctrl,"LOCATIONS",args,prob);
   Assert.assertTrue(IvyXml.isElement(xml,"RESULT"));
   
   return xml;
}



private void getChangedVariables(MintControl ctrl,FrameData fd,String prob)
{
   CommandArgs args = new CommandArgs("TYPE","EXCEPTION",
         "METHOD",fd.getMethod(),
         "LINE",fd.getLine(),
         "CLASS",fd.getClassName(),
         "FILE",fd.getSourceFile(ctrl),
         "PROJECT",fd.getProject(ctrl),
         "LAUNCH",fd.getLaunchId(),
         "FRAME",fd.getId(),
         "THREAD",fd.getThreadId() );
   Element xml = sendStemReply(ctrl,"CHANGEDITEMS",args,prob);
   Assert.assertTrue(IvyXml.isElement(xml,"RESULT"));
   
   String pvals = null;
   for (Element cve : IvyXml.children(xml,"VARIABLE")) {
      String typ = IvyXml.getAttrString(cve,"TYPE");
      String nam = IvyXml.getAttrString(cve,"NAME");
      if (typ == null || nam == null) continue;
      switch (typ) {
         case "PARAMETER" :
            String pt = "<PARAMETER NAME='" + nam + "'/>";
            if (pvals == null) pvals = pt;
            else pvals += pt;
            break;
       }
    }
      
   if (pvals != null) {
      args = new CommandArgs("METHOD",fd.getMethod(),
            "CLASS",fd.getClassName(),
            "LINE",fd.getLine(),
            "FILE",fd.getSourceFile(ctrl),
            "PROJECT",fd.getProject(ctrl),
            "FRAME",fd.getId(),
            "THREAD",fd.getThreadId());
      Element pxml = sendStemReply(ctrl,"PARAMETERVALUES",args,pvals);
      Assert.assertTrue(IvyXml.isElement(pxml,"RESULT"));
    }
}


private String getStartFrame(MintControl ctrl,String prob,String loc)
{
   CommandArgs args = new CommandArgs();
   String cnts = prob;
   if (loc != null) cnts += loc;
   Element rslt = sendStemReply(ctrl,"STARTFRAME",args,cnts);
   String startframe = IvyXml.getAttrString(rslt,"STARTFRAME");
   String startrtn = IvyXml.getAttrString(rslt,"CLASS");
   startrtn += "." + IvyXml.getAttrString(rslt,"METHOD");
   startrtn += IvyXml.getAttrString(rslt,"SIGNATURE");
   RootTestCase rtc = new RootTestCase(startframe,startrtn);
   Element xml = IvyXml.convertStringToXml(prob);
   RootProblem rp = BractFactory.getFactory().createProblemDescription(null,xml);
   rp.setCurrentTest(rtc);
   IvyXmlWriter xw = new IvyXmlWriter();
   rp.outputXml(xw);
   String nprob = xw.toString();
   xw.close();
   return nprob;
}


private void getSuggestionsFor(MintControl ctrl,FrameData fd,String prob,Element locelt) 
{
   String id = "SUGGEST_" + source_id + "_" + random_gen.nextInt(100000);
   CommandArgs args = new CommandArgs("NAME",id);
    
   String loc = null;
   if (locelt != null) loc = IvyXml.convertXmlToString(locelt);
   String cnts = prob;
   if (loc != null) {
      if (cnts == null) cnts = loc;
      else cnts += loc;
    }
   
   SuggestionSet ss = new SuggestionSet();
   suggest_set.put(id,ss);
   
   Element xml = sendStemReply(ctrl,"SUGGEST",args,cnts);
   Assert.assertTrue(IvyXml.isElement(xml,"RESULT"));
   List<Element> rslt = ss.getSuggestions();
   Assert.assertTrue(rslt != null);
   
   suggest_set.remove(id);
}



/********************************************************************************/
/*                                                                              */
/*      Stem setup methods                                                      */
/*                                                                              */
/********************************************************************************/

private void setupStem(MintControl mc,boolean lcl)
{
   String [] args = new String [] {
         "-m", mc.getMintName(), 
         "-w", workspace_path, "-D"  };
   if (lcl) {
      args = new String [] {
            "-m", mc.getMintName(), 
            "-w", workspace_path, "-D", "-local"  };
    }
   
   StemMain.main(args);
   
   mc.register("<ROSEREPLY DO='_VAR_0' />",new SuggestHandler());
}



/********************************************************************************/
/*										*/
/*	Handle sending messages to STEM                 			*/
/*										*/
/********************************************************************************/

private Element sendStemReply(MintControl mc,String cmd,CommandArgs args,String xml)
{
   MintDefaultReply rply = new MintDefaultReply();
   sendStem(mc,cmd,args,xml,rply);
   Element rslt = rply.waitForXml();
   RoseLog.logD("STEM REPLY: " + IvyXml.convertXmlToString(rslt));
   return rslt;
}



private void sendStem(MintControl mc,String cmd,CommandArgs args,String xml,MintReply rply)
{
   IvyXmlWriter msg = new IvyXmlWriter();
   msg.begin("ROSE");
   msg.field("CMD",cmd);
   if (args != null) {
      for (Map.Entry<String,Object> ent : args.entrySet()) {
	 if (ent.getValue() == null) continue;
	 msg.field(ent.getKey(),ent.getValue().toString());
       }
    }
   if (xml != null) msg.xmlText(xml);
   msg.end("ROSE");
   String msgt = msg.toString();
   msg.close();
   
   if (rply == null) {
      mc.send(msgt,rply,MintConstants.MINT_MSG_NO_REPLY);
    }
   else {
      mc.send(msgt,rply,MintConstants.MINT_MSG_FIRST_NON_NULL);
    }
}



/********************************************************************************/
/*										*/
/*	Bedrock setup / shutdown methods					*/
/*										*/
/********************************************************************************/

private MintControl setupBedrock(String dir,String proj)
{
   MintControl mc = mint_map.get(dir);
   if (mc == null) {
      int rint = random_gen.nextInt(1000000);
      String mint = "STEM_TEST_" + dir.toUpperCase() + "_" + rint;
      mc = MintControl.create(mint,MintSyncMode.ONLY_REPLIES);
      mint_map.put(dir,mc);
      mint_count.put(dir,1);
    }
   else {
      int ct = mint_count.get(dir);
      mint_count.put(dir,ct+1);
    }
   
   mc.register("<BEDROCK SOURCE='ECLIPSE' TYPE='_VAR_0' />",new IDEHandler());
   
   RoseLog.logI("STEM","SETTING UP BEDROCK");
   File ec1 = new File("/u/xxx/eclipse-oxygenx/eclipse/eclipse");
   File ec2 = new File("/u/xxx/Eclipse/" + dir);
   if (!ec1.exists()) {
      ec1 = new File(ECLIPSE_PATH_MAC);
      ec2 = new File(ECLIPSE_DIR_MAC + dir);
    }
   if (!ec1.exists()) {
      System.err.println("Can't find bubbles version of eclipse to run");
      System.exit(1);
    }
   workspace_path = ec2.getAbsolutePath();
   
   String cmd = ec1.getAbsolutePath();
   cmd += " -application xxx.x.xx.bubbles.bedrock.application";
   cmd += " -data " + ec2.getAbsolutePath();
   cmd += " -bhide";
   cmd += " -nosplash";
   cmd += " -vmargs -Dedu.x.xx.bubbles.MINT=" + mc.getMintName();
   
   RoseLog.logI("STEM","RUN: " + cmd);
   
   try {
      for (int i = 0; i < 250; ++i) {
	 if (pingEclipse(mc)) {
	    CommandArgs args = new CommandArgs("LEVEL","DEBUG");
	    sendBubblesMessage(mc,"LOGLEVEL",null,args,null);
	    sendBubblesMessage(mc,"ENTER");
	    Element pxml = sendBubblesXmlReply(mc,"OPENPROJECT",proj,null,null);
	    if (!IvyXml.isElement(pxml,"PROJECT")) pxml = IvyXml.getChild(pxml,"PROJECT");
	    return mc;
	  }
	 if (i == 0) new IvyExec(cmd);
	 else {
	    try { Thread.sleep(100); } catch (InterruptedException e) { }
	  }
       }
    }
   catch (IOException e) { }
   
   throw new Error("Problem running Eclipse: " + cmd);
}



private void shutdownBedrock(String dir)
{
   MintControl mc = mint_map.get(dir);
   if (mc == null) return;
   int ctr = mint_count.get(dir);
   if (ctr > 1) {
      mint_count.put(dir,ctr-1);
      return;
    }
   else {
      mint_count.remove(dir);
      mint_map.remove(dir);
    }
   RoseLog.logI("STEM","Shut down bedrock");
   sendBubblesMessage(mc,"EXIT");
   
   File bdir = new File(workspace_path);
   File bbdir = new File(bdir,".bubbles");
   File cdir = new File(bbdir,"CockerIndex");
   LeashIndex idx = new LeashIndex(ROSE_PROJECT_INDEX_TYPE,cdir);
   idx.stop();
}



/********************************************************************************/
/*                                                                              */
/*      Handle launching execution                                              */
/*                                                                              */
/********************************************************************************/

private LaunchData startLaunch(MintControl mc,String proj,String name)
{
   stopped_thread = null;
   
   CommandArgs args = new CommandArgs("NAME",name,"MODE","debug","BUILD","TRUE",
	 "REGISTER","TRUE");
   MintDefaultReply rply = new MintDefaultReply();
   sendBubblesMessage(mc,"START",proj,args,null,rply);
   Element xml = rply.waitForXml();
   Element ldata = IvyXml.getChild(xml,"LAUNCH");
   Assert.assertNotNull(ldata);
   String launchid = IvyXml.getAttrString(ldata,"ID");
   Assert.assertNotNull(launchid);
   String targetid = IvyXml.getAttrString(ldata,"TARGET");
   Assert.assertNotNull(targetid);
   String processid = IvyXml.getAttrString(ldata,"PROCESS");
   Assert.assertNotNull(processid);
   String threadid = waitForStop();
   Assert.assertNotNull(threadid);
   
   return new LaunchData(launchid,targetid,processid,threadid);
}


private void continueLaunch(MintControl mc,String project,LaunchData ld)
{
   stopped_thread = null;
   
   CommandArgs args = new CommandArgs("LAUNCH",ld.getLaunchId(),
	 "TARGET",ld.getTargetId(),
	 "PROCESS",ld.getProcessId(),"ACTION","RESUME");
   MintDefaultReply rply = new MintDefaultReply();
   sendBubblesMessage(mc,"DEBUGACTION",project,args,null,rply);
   String x = rply.waitForString();
   Assert.assertNotNull(x);
   String threadid = waitForStop();
   Assert.assertNotNull(threadid);
   
   ld.setThreadId(threadid);
}




private FrameData getTopFrame(MintControl mc,String project,LaunchData ld) 
{
   List<FrameData> frames = getFrames(mc,project,ld);
   if (frames == null || frames.size() == 0) return null;
   for (FrameData fd : frames) {
      if (fd.isUserFrame()) return fd;
    }
   return null;
}



private List<FrameData> getFrames(MintControl mc,String project,LaunchData ld)
{
   List<FrameData> frames = new ArrayList<>();
   CommandArgs args = new CommandArgs("LAUNCH",ld.getLaunchId(),"THREAD",ld.getThreadId());
   Element frameresult = sendBubblesXmlReply(mc,"GETSTACKFRAMES",project,args,null);
   Element framesrslt = IvyXml.getChild(frameresult,"STACKFRAMES");
   for (Element thrslt : IvyXml.children(framesrslt,"THREAD")) {
      if (ld.getThreadId().equals(IvyXml.getAttrString(thrslt,"ID"))) {
         for (Element frslt : IvyXml.children(thrslt,"STACKFRAME")) {
            frames.add(new FrameData(ld,frslt));
          }
       }
    }
   
   return frames;
}



private static class LaunchData {

   private String lanuch_id;
   private String target_id;
   private String process_id;
   private String thread_id;
   
   LaunchData(String launch,String target,String process,String thread) {
      lanuch_id = launch;
      target_id = target;
      process_id = process;
      thread_id = thread;
    }
   
   String getLaunchId() 			{ return lanuch_id; }
   String getTargetId() 			{ return target_id; }
   String getProcessId()			{ return process_id; }
   String getThreadId() 			{ return thread_id; }
   
   void setThreadId(String id)			{ thread_id = id; }

}	// end of inner class LaunchData



private static class FrameData {
   
   private LaunchData   for_launch;
   private String       frame_id;
   private String       method_name;
   private String       class_name;
   private String       file_name;
   private String       project_name;
   private int          line_number;
   private boolean      is_user;
   
   FrameData(LaunchData ld,Element xml) {
      for_launch = ld;
      frame_id = IvyXml.getAttrString(xml,"ID");
      method_name = IvyXml.getAttrString(xml,"METHOD");
      line_number = IvyXml.getAttrInt(xml,"LINENO");
      class_name = IvyXml.getAttrString(xml,"CLASS");
      file_name = IvyXml.getAttrString(xml,"FILE");
      String sgn = IvyXml.getAttrString(xml,"SIGNATURE");
      if (sgn != null) method_name += sgn;
      project_name = null;
      is_user = true;
      if (file_name == null) is_user = false;
      else if (!(new File(file_name).exists())) is_user = false;
      else if (!IvyXml.getAttrString(xml,"FILETYPE").equals("JAVAFILE")) is_user = false;
    }
   
   String getId()                       { return frame_id; }
   String getMethod()                   { return method_name; }
   String getClassName()                { return class_name; }
   int getLine()                        { return line_number; }
   String getThreadId()                 { return for_launch.getThreadId(); }
   String getLaunchId()                 { return for_launch.getLaunchId(); }
   boolean isUserFrame()                { return is_user; }
   
   String getSourceFile(MintControl ctrl) {
      if (file_name == null) {
         getFileData(ctrl);
       }
      return file_name;
    }
   
   String getProject(MintControl ctrl) {
      if (project_name == null) getFileData(ctrl);
      return project_name;
    }
   
   private void getFileData(MintControl ctrl) {
      CommandArgs args = new CommandArgs("PATTERN",IvyXml.xmlSanitize(class_name),
            "DEFS",true,"REFS",false,"FOR","TYPE");
      Element cxml = sendBubblesXmlReply(ctrl,"PATTERNSEARCH",null,args,null); 
      for (Element lxml : IvyXml.elementsByTag(cxml,"MATCH")) {
         file_name = IvyXml.getAttrString(lxml,"FILE");
         Element ielt = IvyXml.getChild(lxml,"ITEM");
         project_name = IvyXml.getAttrString(ielt,"PROJECT");
         break;
       }
    }
   
   void outputLocation(IvyXmlWriter xw,String proj,double p,MintControl mc) {
      xw.begin("LOCATION");
      xw.field("FILE",getSourceFile(mc));
      xw.field("LINE",getLine());
      xw.field("METHOD",getMethod());
      xw.field("PROJECT",proj);
      // want signature appended to method?
      xw.field("PRIORITY",p);
      xw.end("LOCATION");
    }
   
   
}       // end of inner class FrameData



/********************************************************************************/
/*                                                                              */
/*      Handle bubbles messaging                                                */
/*                                                                              */
/********************************************************************************/

private static boolean pingEclipse(MintControl mc)
{
   MintDefaultReply mdr = new MintDefaultReply();
   sendBubblesMessage(mc,"PING",null,null,null,mdr);
   String r = mdr.waitForString(500);
   return r != null;
}


private static Element sendBubblesXmlReply(MintControl mc,String cmd,String proj,Map<String,Object> flds,String cnts)
{
   MintDefaultReply mdr = new MintDefaultReply();
   sendBubblesMessage(mc,cmd,proj,flds,cnts,mdr);
   Element pxml = mdr.waitForXml();
   RoseLog.logD("STEM","RECEIVE from BUBBLES: " + IvyXml.convertXmlToString(pxml));
   return pxml;
}



private static void sendBubblesMessage(MintControl mc,String cmd)
{
   sendBubblesMessage(mc,cmd,null,null,null,null);
}


private static void sendBubblesMessage(MintControl mc,String cmd,String proj,Map<String,Object> flds,String cnts)
{
   sendBubblesMessage(mc,cmd,proj,flds,cnts,null);
}



private static void sendBubblesMessage(MintControl mc,String cmd,String proj,Map<String,Object> flds,String cnts,
      MintReply rply)
{
   IvyXmlWriter xw = new IvyXmlWriter();
   xw.begin("BUBBLES");
   xw.field("DO",cmd);
   xw.field("BID",source_id);
   if (proj != null && proj.length() > 0) xw.field("PROJECT",proj);
   if (flds != null) {
      for (Map.Entry<String,Object> ent : flds.entrySet()) {
	 xw.field(ent.getKey(),ent.getValue());
       }
    }
   xw.field("LANG","eclipse");
   if (cnts != null) xw.xmlText(cnts);
   xw.end("BUBBLES");
   
   String xml = xw.toString();
   xw.close();
   
   RoseLog.logD("STEM","SEND to BUBBLES: " + xml);
   
   int fgs = MINT_MSG_NO_REPLY;
   if (rply != null) fgs = MINT_MSG_FIRST_NON_NULL;
   mc.send(xml,rply,fgs);
}



/********************************************************************************/
/*                                                                              */
/*      Monitor executions                                                      */
/*                                                                              */
/********************************************************************************/

private class IDEHandler implements MintHandler {

   @Override public void receive(MintMessage msg,MintArguments args) {
      String cmd = args.getArgument(0);
      Element e = msg.getXml();
      if (cmd == null) return;
      
      switch (cmd) {
         case "ELISIION" :
            break;
         case "EDITERROR" :
            break;
         case "FILEERROR" :
            break;
         case "PRIVATEERROR" :
            break;
         case "EDIT" :
            break;
         case "BREAKEVENT" :
            break;
         case "LAUNCHCONFIGEVENT" :
            break;
         case "RUNEVENT" :
            long when = IvyXml.getAttrLong(e,"TIME");
            for (Element re : IvyXml.children(e,"RUNEVENT")) {
               handleRunEvent(re,when);
             }
            msg.replyTo("<OK/>");
            break;
         case "NAMES" :
         case "ENDNAMES" :
            break;
         case "PING" :
            msg.replyTo("<PONG/>");
            break;
         case "PROGRESS" :
            msg.replyTo("<OK/>");
            break;
         case "RESOURCE" :
            break;
         case "CONSOLE" :
            msg.replyTo("<OK/>");
            break;
         case "OPENEDITOR" :
            break;
         case "EVALUATION" :
            if (bedrock_id == null) {
               bedrock_id = IvyXml.getAttrString(e,"BID");
             }
            msg.replyTo("<OK/>");
            break;
         case "BUILDDONE" :
         case "FILECHANGE" :
         case "PROJECTDATA" :
         case "PROJECTOPEN" :
            break;
         case "STOP" :
            break;
         default :
            break;
       }
    }

}	// end of innerclass IDEHandler



private void handleRunEvent(Element xml,long when)
{
   String type = IvyXml.getAttrString(xml,"TYPE");
   if (type == null) return;
   switch (type) {
      case "PROCESS" :
	 break;
      case "THREAD" :
	 handleThreadEvent(xml,when);
	 break;
      case "TARGET" :
	 break;
      default :
	 break;
    }
}


private void handleThreadEvent(Element xml,long when)
{
   String kind = IvyXml.getAttrString(xml,"KIND");
   // String detail = IvyXml.getAttrString(xml,"DETAIL");
   Element thread = IvyXml.getChild(xml,"THREAD");
   if (thread == null) return;
   switch (kind) {
      case "SUSPEND" :
	 synchronized (this) {
	    stopped_thread = IvyXml.getAttrString(thread,"ID");
	    notifyAll();
	  }
	 break;
    }
}



private String waitForStop()
{
   synchronized (this) {
      for (int i = 0; i < 100; ++i) {
         if (stopped_thread != null) break;
	 try {
	    wait(300);
	  }
	 catch (InterruptedException e) { }
       }
      return stopped_thread;
    }
}


/********************************************************************************/
/*                                                                              */
/*      Monitor Suggestiongs                                                    */
/*                                                                              */
/********************************************************************************/

private class SuggestHandler implements MintHandler {

   @Override public void receive(MintMessage msg,MintArguments args) {
      String cmd = args.getArgument(0);
      Element e = msg.getXml();
      String name = IvyXml.getAttrString(e,"NAME");
      SuggestionSet ss = null;
      if (name != null) ss = suggest_set.get(name);
      switch (cmd) {
         case "SUGGEST" :
            if (ss != null) ss.addSuggestion(e);
            break;
         case "ENDSUGGEST" :
            if (ss != null) ss.endSuggestions();
            break;
       }
    }

}       // end of inner class SuggestHandler



private static class SuggestionSet {
   
   private List<Element> suggest_nodes;
   private boolean is_done;
   
   SuggestionSet() {
      suggest_nodes = new ArrayList<>();
      is_done = false;
    }
   
   void addSuggestion(Element xml) {
      for (Element r : IvyXml.children(xml,"REPAIR")) { 
         suggest_nodes.add(r);
       }
    }
   
   synchronized void endSuggestions() {
      is_done = true;
      notifyAll();
    }
   
   synchronized List<Element> getSuggestions() {
      while (!is_done) {
         try {
            wait(1000);
          }
         catch (InterruptedException e) { }
       }
      return suggest_nodes;
    }
   
}       // end of inner class SuggestionSet




}       // end of class StemTest




/* end of StemTest.java */

