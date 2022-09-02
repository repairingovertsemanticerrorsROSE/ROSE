/********************************************************************************/
/*										*/
/*		PicotConstants.java						*/
/*										*/
/*	Program Interface for Creation Of Testcases -- constants		*/
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



package xxx.x.xx.rose.picot;

import java.util.HashMap;

import xxx.x.x.ivy.jcomp.JcompSymbol;


public interface PicotConstants
{

/**
 *	Abstract representation of a generated test case
 **/

enum PicotTestStatus {
   FAIL,
   NO_DUP,
   SUCCESS
}

interface PicotTestCase {
   PicotTestStatus getStatus();
   PicotCodeFragment getRunCode();
   String getTestCode();
   String getTestClassName();
   String getTestPackageName();
   String getTestMethodName();
   String getTestProject();
   void updateRunCode(String code);
}


enum PicotItemType {
   THIS,
   PARAMETER,
   FIELD,
   CONSTANT,
   EXPRESSION,
   NEW_OBJECT
}


enum PicotEffectType {
   SET_FIELD,
   RETURN,
   ADD_TO_COLLECTION,
   REMOVE_FROM_COLLECTION,
   ADD_TO_MAP,
   REMOVE_FROM_MAP,
}


enum PicotAccessorType {
   VARIABLE,
   FIELD,
   ARRAY ,
}



class PicotLocalMap extends HashMap<JcompSymbol,PicotEffectItem> {
   private static final long serialVersionUID = 1;
}


class PicotFieldMap extends HashMap<JcompSymbol,PicotCodeFragment> {
   private static final long serialVersionUID = 1;
}




/********************************************************************************/
/*                                                                              */
/*      Search definitions                                                      */
/*                                                                              */
/********************************************************************************/

enum PicotAlternativeType { 
   NONE,                // not computed
   FAIL,                // no more alternatives
   CREATE,              // next is a create
   FIX                  // next is a fix
}          



/********************************************************************************/
/*                                                                              */
/*      Code Strings                                                            */
/*                                                                              */
/********************************************************************************/

String START_STRING = "/*START*/\n";
String END_STRING = "/*END*/\n";
String TEST_START = "/*TESTSTART*/\n";
String TEST_END = "/*TESTEND*/\n";



}	// end of interface PicotConstants




/* end of PicotConstants.java */

