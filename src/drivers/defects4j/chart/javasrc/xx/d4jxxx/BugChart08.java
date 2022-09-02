/********************************************************************************/
/*                                                                              */
/*              BugChart08.java                                                 */
/*                                                                              */
/*      Defects4J bug Chart_8                                                   */
/*                                                                              */
/********************************************************************************/
/*      Copyright 2011  --                     */



package xx.d4jspr;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.jfree.data.time.Week;

public class BugChart08 implements BugConstants
{


public static void main(String [] args)
{
   testChart08();
}



public static void testChart08() 
{
   GregorianCalendar cal =  (GregorianCalendar) Calendar.getInstance(TimeZone.getDefault());
   Date t = cal.getTime();
   Week w = new Week(t, TimeZone.getTimeZone("Europe/Copenhagen"));
   assert w.getWeek() == 35;
}




}       // end of class BugChart08




/* end of BugChart08.java */

