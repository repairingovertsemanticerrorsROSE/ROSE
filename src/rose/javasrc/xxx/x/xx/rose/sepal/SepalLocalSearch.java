/********************************************************************************/
/*                                                                              */
/*              SepalLocalSearch.java                                           */
/*                                                                              */
/*      Find similar statements or contexts in project as suggestions           */
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
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import xxx.x.xx.rose.bract.BractSearch;
import xxx.x.xx.rose.bract.BractConstants.BractSearchResult;
import xxx.x.xx.rose.root.RootControl;
import xxx.x.xx.rose.root.RootRepairFinderDefault;
import xxx.x.xx.rose.root.RoseLog;
import sharpfix.global.PatchAsASTRewriteWithScore;
import sharpfix.patchgen.LocalPatchGenerator;

public class SepalLocalSearch extends RootRepairFinderDefault
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private BractSearch     search_engine;

private static final int MAX_LOCAL_CHECK = 40;
private static final int MAX_LOCAL_RESULTS = 10;
private static final int MAX_CHECK_PER_RESULT = 4;
private static final double SEARCH_THRESHOLD = 1.0;
private static final int MAX_RETURN = 128;




/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

public SepalLocalSearch()
{
   search_engine = null;
}


@Override synchronized protected void localSetup()
{
   if (search_engine == null) {
      search_engine = BractSearch.getProjectSearch(getProcessor().getController());
    }
}



/********************************************************************************/
/*                                                                              */
/*      Processing methods                                                      */
/*                                                                              */
/********************************************************************************/

@Override public double getFinderPriority()
{
   return 0.40;
}



@Override public void process()
{
   RootControl ctrl = getProcessor().getController();
   Statement stmt = (Statement) getResolvedStatementForLocation(null);
   if (stmt == null) return;
   List<BractSearchResult> rslts = search_engine.getResults(stmt,SEARCH_THRESHOLD,MAX_RETURN);
   if (rslts == null || rslts.isEmpty()) return; 
   
   File bfile = getLocation().getFile();
   try {
      bfile = bfile.getCanonicalFile();
    }
   catch (IOException e) { }
   int lno = getLocation().getLineNumber();
   String bcnts = ctrl.getSourceContents(bfile);
   ASTNode bnode = stmt;
   
   int rct = 0;
   int fnd = 0;
   for (BractSearchResult sr : rslts) {
      if (sr.getFile().equals(bfile) && sr.getLineNumber() == lno) continue;
      if (!sr.getFile().exists() || !sr.getFile().canRead()) continue;
      if (++rct > MAX_LOCAL_RESULTS) break;
      String ccnts = ctrl.getSourceContents(sr.getFile());
      ASTNode cnode = ctrl.getNewSourceStatement(sr.getFile(),sr.getLineNumber(),sr.getColumnNumber());
      if (cnode == null) {
         --rct;
         continue;
       }
      List<PatchAsASTRewriteWithScore> patches;
     
      synchronized (search_engine) {
         try {
            patches = LocalPatchGenerator.makePatches(bcnts,bnode,ccnts,cnode);
          }
         catch (Throwable t) {
            RoseLog.logE("SEPAL","Problem with sharpFix",t);
            continue;
          }
       }
      
      restrictPatches(lno,stmt,patches);
      int ct = patches.size();
      int lct = 0;
      for (int i = 0; i < ct; ++i) {
         PatchAsASTRewriteWithScore r = patches.get(i);
         ASTRewrite rw = r.getASTRewrite();
        
         // need to get a description from the rewrite -- replace Search-based repair with that
         double score = r.getScore();
         // might want to manipulate score a bit
         // rw.rewriteAST().getLength() < MAX_CHANGE_LENGTH
         String logdata = getClass().getName() + "@" + i + "@" + r.getType();
         String desc = r.getDescription();
         addRepair(rw,desc,logdata,score);
//       System.err.println("LOCAL " + i + " " + fnd + " " + rct + " " + lct + " " + desc);
         if (++lct > MAX_CHECK_PER_RESULT) break;
         if (++fnd > MAX_LOCAL_CHECK) break;
       }
      // add repair for each returned patch
      RoseLog.logD("SEPAL","Find local search results " + ct + " " + fnd);
      if (fnd > MAX_LOCAL_CHECK) break;
    }
}



private boolean isRelevant(int lno,Statement stmt,PatchAsASTRewriteWithScore r)
{
   // ignore method replace
   if (r.getType().equals("METHODREPLACE")) return false;
   
   // ensure the fix is at or near the line being checked
   if (r.getType().startsWith("REPLACE")) {
      if (r.getLineNumber() != lno) return false;
    }
   else if (r.getType().startsWith("INSERT")) {
      if (Math.abs(r.getLineNumber()-lno) > 1) return false;
    }
   else if (r.getType().contains("GUARD")) {
      if (Math.abs(r.getLineNumber()-lno) > 1) return false;
    }
   
   // ignore large changes other than expressions
   if (!r.getType().equals("REPLACE_EXPR")) {
      if (r.getHeight() >= 3.0) return false;
    }
   else {
      if (r.getHeight() == 1) {                 // constant replace
         return false;
       }
    }
   if (r.getType().equals("REPLACE_OP")) {
      if (r.getDescription().contains("=") ||
            r.getDescription().contains(">") || 
            r.getDescription().contains("<")) return false;
    }
   
   return true;
}
  


/********************************************************************************/
/*                                                                              */
/*      Sort patches                                                            */
/*                                                                              */
/*  This takes into account the other patch generation strategies and tries to  */
/*  prioritize search patches that are not likely to be found by them.   It     */
/*  also removes patches that are too complex and therefore out of the scope of */
/*  ROSE.                                                                       */        
/*                                                                              */
/********************************************************************************/

private void restrictPatches(int lno,Statement stmt,List<PatchAsASTRewriteWithScore> patches)
{
   // first remove irrelevant patches
   for (Iterator<PatchAsASTRewriteWithScore> it = patches.iterator(); it.hasNext(); ) {
      PatchAsASTRewriteWithScore p = it.next();
      if (!isRelevant(lno,stmt,p)) it.remove();
    }
   
   patches.sort(new PatchComparer());
   
   int patch_num = patches.size();
   float basic_score = 1f / ((1f + patch_num) * patch_num / 2);
   for (int i=0; i<patch_num; i++) {
      PatchAsASTRewriteWithScore p = patches.get(i);
      p.setScore(basic_score * (patch_num-i));
    }
}


private static Map<String,Integer> patch_type;
static {
   int ct = 0;
   patch_type = new HashMap<>();
   patch_type.put("INSERT",++ct);
   patch_type.put("REPLACE_EXPR",++ct);
   patch_type.put("REPLACE_STATEMENT",++ct);
   patch_type.put("EXPRGUARD",++ct);
   patch_type.put("REPLACE_OP",++ct);
   patch_type.put("IFCONDGUARD",++ct);
   patch_type.put("REPLACE_BLOCK",++ct);
   patch_type.put("REPLACE_LIST",++ct);
   patch_type.put("REPLACE_STMTS",++ct);
   patch_type.put("REPLACE_NAME",++ct);
   patch_type.put("REPLACE",++ct);
   patch_type.put("DELETE",++ct);
   patch_type.put("METHODREPLACE",++ct);
}


private static int getTypeScore(String t)
{
   Integer v = patch_type.get(t);
   if (v != null) return v;
   RoseLog.logE("SEPAL","Unknown search patch type " + t);
   return patch_type.size() + 1;
}

private static class PatchComparer implements Comparator<PatchAsASTRewriteWithScore> {
   
   @Override public int compare(PatchAsASTRewriteWithScore p1,PatchAsASTRewriteWithScore p2) {
      String t1 = p1.getType();
      String t2 = p2.getType();
      if (t1.equals(t2)) {
         return Float.compare(p1.getScore(),p2.getScore());
       }
      return Integer.compare(getTypeScore(t1),getTypeScore(t2));
    }
   
}       // end of inner class PatchComparer






}       // end of class SepalLocalSearch




/* end of SepalLocalSearch.java */

