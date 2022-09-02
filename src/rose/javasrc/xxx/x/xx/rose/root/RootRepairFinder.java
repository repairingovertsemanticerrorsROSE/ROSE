/********************************************************************************/
/*                                                                              */
/*              RoseRepairFinder.java                                           */
/*                                                                              */
/*      description of class                                                    */
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



package xxx.x.xx.rose.root;



public interface RootRepairFinder
{

/** 
 *      Setup finder for a particular problem and location
 **/

   void setup(RootProcessor ctrl,RootProblem problem,RootLocation location);


/**
 *      Indicate that a location is required
 **/

   default boolean requiresLocation()            { return true; }
   
   
/**
 *      Return the location if it is present, null otherwise
 **/
   
   default RootLocation getLocation()           { return null; }
   
   
/**
 *      Process to find repair suggestions.
 **/

   void process();
    
/**
 *      Return processor priority
 **/
   default double getFinderPriority()           { return 0.5; }    

}       // end of interface RoseRepairFinder




/* end of RoseRepairFinder.java */

