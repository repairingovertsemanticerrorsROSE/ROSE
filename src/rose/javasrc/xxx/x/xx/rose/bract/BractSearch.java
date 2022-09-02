/********************************************************************************/
/*										*/
/*		BractSearch.java						*/
/*										*/
/*	Interface to COCKER search engine					*/
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



package xxx.x.xx.rose.bract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import xxx.x.x.ivy.jcomp.JcompAst;
import xxx.x.x.ivy.leash.LeashIndex;
import xxx.x.x.ivy.leash.LeashResult;
import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RoseLog;

public class BractSearch implements BractConstants
{



/********************************************************************************/
/*										*/
/*	Static methods								*/
/*										*/
/********************************************************************************/

public synchronized static BractSearch getProjectSearch(RootControl ctrl)
{
   if (local_engine == null) {
      local_engine = new BractSearch(ctrl);
    }
   return local_engine;
}


public static BractSearch getGlobalSearch()
{
   if (global_engine == null) {
      global_engine = new BractSearch();
    }
   return global_engine;
}



/********************************************************************************/
/*										*/
/*	Private Storage 							*/
/*										*/
/********************************************************************************/

private LeashIndex	cocker_index;
private boolean 	is_local;

private static BractSearch local_engine;
private static BractSearch global_engine;

private static final String	GLOBAL_HOST = "cocker.x.x.xxx";
private static final int	GLOBAL_PORT = 10268;		// STMTSEARCHGLOBAL



/********************************************************************************/
/*										*/
/*	Constructors								*/
/*										*/
/********************************************************************************/

private BractSearch(RootControl ctrl)
{
   is_local = true;
   if (is_local) {
      cocker_index = ctrl.getProjectIndex();
    }
}


private BractSearch()
{
   is_local = false;
   cocker_index = new LeashIndex(GLOBAL_HOST,GLOBAL_PORT);
}


/********************************************************************************/
/*										*/
/*	Search methods								*/
/*										*/
/********************************************************************************/

public List<BractSearchResult> getResults(ASTNode stmt,double thresh,int max)
{
   List<BractSearchResult> rslt = new ArrayList<>();
   if (stmt == null || cocker_index == null) return rslt;

   String filename = JcompAst.getSource(stmt).getFileName();
   File file = new File(filename);
   int off = stmt.getStartPosition();
   CompilationUnit cu = (CompilationUnit) stmt.getRoot();
   int line = cu.getLineNumber(off);
   int col = cu.getColumnNumber(off);

   List<LeashResult> base = cocker_index.queryStatements(file,line,col,max);
   if (base == null || base.isEmpty()) return rslt;

   RoseLog.logD("BRACT","Leash query for " + filename + "@" + line + ":  " + stmt);
   for (LeashResult lr : base) {
      if (lr.getScore() < thresh) continue;
      RoseLog.logD("BRACT","Leash result: " + lr.getFilePath() + " " +
	    lr.getLines() + " " + lr.getColumns() + " " + lr.getScore());
      if (file.equals(lr.getFilePath()) && lr.getLines().contains(line)) continue;
      SearchResult sr = new SearchResult(lr);
      rslt.add(sr);
    }

   return rslt;
}



/********************************************************************************/
/*                                                                              */
/*      Search Result                                                           */
/*                                                                              */
/********************************************************************************/

private class SearchResult implements BractSearchResult {
   
   private LeashResult  leash_result;
   
   SearchResult(LeashResult lr) { 
      leash_result = lr;
    }
   
   @Override public double getScore()           { return leash_result.getScore(); }
   @Override public File getFile()              { return leash_result.getFilePath(); }
   @Override public String getFileContents()    { return leash_result.getFileContents(); }
   @Override public int getLineNumber()         { return leash_result.getLines().get(0); }
   @Override public int getColumnNumber()       { return leash_result.getColumns().get(0); }
   
}       // end of inner class SearchResult



}	// end of class BractSearch




/* end of BractSearch.java */

