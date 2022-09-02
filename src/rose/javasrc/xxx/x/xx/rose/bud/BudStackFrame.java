/********************************************************************************/
/*                                                                              */
/*              BudStackFrame.java                                              */
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



package xxx.x.xx.rose.bud;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import xxx.x.x.ivy.file.IvyFormat;
import xxx.x.x.ivy.xml.IvyXml;

public class BudStackFrame implements BudConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private String frame_id;
private File   source_file;
private String class_name;
private String method_name;
private String method_signature;
private String format_signature;
private Map<String,BudLocalVariable> frame_variables;
private int     line_number;
private boolean is_userframe;



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

BudStackFrame(Element xml)
{
   frame_id = IvyXml.getAttrString(xml,"ID");
   class_name = IvyXml.getAttrString(xml,"CLASS");
   method_name = IvyXml.getAttrString(xml,"METHOD");
   int idx = method_name.lastIndexOf(".");
   if (idx > 0) method_name = method_name.substring(idx+1);
   
   String fnm = IvyXml.getAttrString(xml,"FILE");
   if (fnm == null) source_file = null;
   else source_file = new File(fnm);
   
   String typ = IvyXml.getAttrString(xml,"FILETYPE");
   if (source_file == null || !source_file.exists() || !typ.equals("JAVAFILE")) is_userframe = false;
   else is_userframe = true;
   
   String sgn = IvyXml.getAttrString(xml,"SIGNATURE");
   method_signature = sgn;
   if (sgn != null) {
      int sidx = sgn.lastIndexOf(")");
      if (sidx > 0) sgn = sgn.substring(0,sidx+1);
      String fsgn = IvyFormat.formatTypeName(sgn);
      format_signature = fsgn;
    }
   else format_signature = null;
   
   line_number = IvyXml.getAttrInt(xml,"LINENO");
   
   frame_variables = new HashMap<>();
   for (Element e : IvyXml.children(xml,"VALUE")) {
      BudLocalVariable blv = new BudLocalVariable(e);
      frame_variables.put(blv.getName(),blv);
    }
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

public String getFrameId()                      { return frame_id; }
public String getClassName()                    { return class_name; }
public String getMethodName()                   { return method_name; }
public String getMethodSignature()              { return method_signature; }
public String getFormatSignature()              { return format_signature; }
public int getLineNumber()                      { return line_number; }
public File getSourceFile()                     { return source_file; }
public boolean isUserFrame()                    { return is_userframe; }

public Collection<String> getLocals()           { return frame_variables.keySet(); }
public BudLocalVariable getLocal(String nm)     { return frame_variables.get(nm); }

   



}       // end of class BudStackFrame




/* end of BudStackFrame.java */

