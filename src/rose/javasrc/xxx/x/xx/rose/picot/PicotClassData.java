/********************************************************************************/
/*                                                                              */
/*              PicotClassData.java                                             */
/*                                                                              */
/*      Data about all the methods of a class                                   */
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import xxx.x.x.ivy.jcomp.JcompAst;
import xxx.x.x.ivy.jcomp.JcompSymbol;
import xxx.x.x.ivy.jcomp.JcompType;
import xxx.x.x.ivy.jcomp.JcompTyper;

class PicotClassData implements PicotConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private JcompType                        for_type;
private Map<JcompSymbol,PicotMethodData> method_data;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

PicotClassData(JcompType typ,JcompTyper typer)
{
   for_type = typ;
   method_data = new LinkedHashMap<>();
   
   JcompSymbol tsym = for_type.getDefinition();
   if (tsym == null) {
      return;
    }
   
   AbstractTypeDeclaration atd = (AbstractTypeDeclaration) tsym.getDefinitionNode();
   if (atd == null) {
      List<JcompSymbol> syms = for_type.getDefinedMethods(typer);
      for (JcompSymbol js : syms) {
         if (js.isPublic()) {
            method_data.put(js,new PicotMethodDataBinary(js,typer));
          }
       }
      return;
    }
   
   for (Object o : atd.bodyDeclarations()) {
      if (o instanceof MethodDeclaration) {
         MethodDeclaration md = (MethodDeclaration) o;
         JcompSymbol msym = JcompAst.getDefinition(md);
         if (msym != null && !msym.isPrivate()) {
            PicotMethodData pmd = new PicotMethodDataAst(md);
            pmd.getEffects();                              // force processing for now
            method_data.put(msym,pmd);
          }
       }
      else if (o instanceof FieldDeclaration) {
         // handle field initializations
       }
    }
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

Collection<JcompSymbol> getMethods()
{
   return method_data.keySet();
}


PicotMethodData getDataForMethod(JcompSymbol js)
{
   return method_data.get(js);
}



}       // end of class PicotClassData




/* end of PicotClassData.java */

