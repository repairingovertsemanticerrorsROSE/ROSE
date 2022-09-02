/********************************************************************************/
/*                                                                              */
/*              PicotValueProblem.java                                          */
/*                                                                              */
/*      A mismatch between source and target to be rectified                    */
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



package xxx.x.xx.rose.picot;

import xxx.x.xx.rose.root.RootValidate.RootTraceValue;

class PicotValueProblem implements PicotConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private PicotValueAccessor value_accessor;
private RootTraceValue    source_value;
private RootTraceValue    target_value;
private RootTraceValue    target_base;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

PicotValueProblem(PicotValueAccessor acc,RootTraceValue src,
      RootTraceValue tgt,RootTraceValue base)
{
   value_accessor = acc;
   source_value = src;
   target_value = tgt;
   target_base = base;
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

PicotValueAccessor getAccessor()                { return value_accessor; }

RootTraceValue getSourceValue()                 { return source_value; }

RootTraceValue getTargetValue()                 { return target_value; }

RootTraceValue getTargetBaseValue()             { return target_base; }



@Override public String toString()
{
   StringBuffer buf = new StringBuffer();
   buf.append("PROBLEM[");
   if (value_accessor != null){
      buf.append(value_accessor.toString());
      buf.append(": ");
    }
   buf.append(source_value);
   buf.append("->");
   buf.append(target_value);
   if (target_base != null) {
      buf.append(" @" + target_base);
    }
   return buf.toString();
}



}       // end of class PicotValueProblem




/* end of PicotValueProblem.java */

