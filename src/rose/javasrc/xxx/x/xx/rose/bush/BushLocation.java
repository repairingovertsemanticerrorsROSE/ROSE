/********************************************************************************/
/*                                                                              */
/*              BushLocation.java                                               */
/*                                                                              */
/*      Location for use inside BUSH interface                                  */
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



package xxx.x.xx.rose.bush;

import xxx.x.x.bubbles.bump.BumpLocation;
import xxx.x.x.bubbles.bump.BumpConstants.BumpStackFrame;
import xxx.x.xx.rose.root.RootLocation;

class BushLocation extends RootLocation implements BushConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private BumpLocation    bump_location;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

BushLocation(BumpLocation loc,double pri) 
{
   super(loc.getFile(),loc.getOffset(),loc.getEndOffset(),-1,loc.getProject(),null,pri);
   
   setMethodData(loc.getKey(),loc.getDefinitionOffset(),
         loc.getDefinitionEndOffset()-loc.getDefinitionOffset());
}






BushLocation(BumpStackFrame frm)
{
   super(frm.getFile(),-1,-1,frm.getLineNumber(),
         frm.getThread().getLaunch().getConfiguration().getProject(),
         frm.getMethod() + frm.getRawSignature(),
         0.5);
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

BumpLocation getBumpLocation()
{
   return bump_location;
}


}       // end of class BushLocation




/* end of BushLocation.java */

