/********************************************************************************/
/*                                                                              */
/*              RoseEvalTestResult.java                                         */
/*                                                                              */
/*      Test case that results from Picot for a problem                         */
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



package xxx.x.xx.rose.roseeval;

import org.w3c.dom.Element;

class RoseEvalTestResult implements RoseEvalConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private Element         result_element;


/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

RoseEvalTestResult()
{
   result_element = null;
}



/********************************************************************************/
/*                                                                              */
/*      Handle test result                                                      */
/*                                                                              */
/********************************************************************************/

synchronized void handleTestResult(Element xml)
{
   result_element = xml;
   notifyAll();
}


synchronized void waitForResult()
{
   while (result_element == null) {
      try {
         wait(3000);
       }
      catch (InterruptedException e) { }
    }
}



}       // end of class RoseEvalTestResult




/* end of RoseEvalTestCase.java */

