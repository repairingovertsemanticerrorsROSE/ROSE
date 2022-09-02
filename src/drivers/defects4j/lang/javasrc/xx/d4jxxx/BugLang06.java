/********************************************************************************/
/*										*/
/*		BugLang06.java							*/
/*										*/
/*	Defects4J bug Lang_6							*/
/*										*/
/********************************************************************************/
/*	Copyright 2011  -- 		      */


package xx.d4jspr;

import java.util.Arrays;

import org.apache.commons.lang3.StringEscapeUtils;

public class BugLang06 implements BugConstants
{


public static void main(String [] args)
{
   testLang06();
}



public static void testLang06()
{
   String  s =  StringEscapeUtils.escapeCsv("\uD83D\uDE30");
   assert s.equals("\uD83D\uDE30");
}



}	// end of class BugLang06




/* end of BugLang06.java */

