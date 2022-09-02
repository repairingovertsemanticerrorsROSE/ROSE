/********************************************************************************/
/*                                                                              */
/*              RootConstants.java                                              */
/*                                                                              */
/*      Global constants for ROOT and ROSE                                      */
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



public interface RootConstants
{

enum RoseValueKind {
   UNKNOWN, PRIMITIVE, STRING, CLASS, OBJECT, ARRAY
}

enum RoseProblemType {
   EXCEPTION,
   ASSERTION,
   VARIABLE,
   EXPRESSION,
   LOCATION,
   NONE,
   OTHER
}


String ROSE_PROJECT_INDEX_TYPE = "SHARPFIXLOCAL";
String ROSE_GLOBAL_INDEX_TYPE = "KGRAM3WORDMD";




/********************************************************************************/
/*                                                                              */
/*      Priorities                                                              */
/*                                                                              */
/********************************************************************************/

double DEFAULT_PRIORITY = 0.5;


interface PriorityTask {
   
   /**
    *   Return the priority.  Higher numbers will be executed before lower ones,
    *   Tasks that do not implement PriorityTask will be considered to have priority 0.
    *   Note this is used by RootThreadPool.
    **/ 
   double getTaskPriority();
   
}


}       // end of interface RootConstants




/* end of RootConstants.java */

