/********************************************************************************/
/*										*/
/*		BractConstants.java						*/
/*										*/
/*	Basic Resources for Accessing Common Technology -- QuickRepair controls */
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
import java.util.HashMap;

public interface BractConstants
{



/********************************************************************************/
/*										*/
/*	Pattern letters 							*/
/*										*/
/********************************************************************************/

char ANY_AST = 'A';
char ANY_EXPR = 'E';
char ANY_STMT = 'S';
char ANY_VAR = 'V';
char ANY_INT = 'I';
char ANY_STRING = 'G';
char ANY_PATTERN = 'P';
char MATCH_PATTERN = 'R';
char ESCAPE_PATTERN = 'X';


public class PatternMap extends HashMap<String,Object> {

   private static final long serialVersionUID = 1;

   public PatternMap(PatternMap omap) {
      if (omap != null) putAll(omap);
    }

   public PatternMap(Object ... vals) {
      for (int i = 0; i+1 < vals.length; i += 2) {
	 String s = vals[i].toString();
	 put(s,vals[i+1]);
       }
    }

}	// end of inner class PatternMap


public interface BractSearchResult {
   
   double getScore();
   File getFile();
   String getFileContents();
   int getLineNumber();
   int getColumnNumber();
   
}



}	// end of interface BractConstants




/* end of BractConstants.java */

