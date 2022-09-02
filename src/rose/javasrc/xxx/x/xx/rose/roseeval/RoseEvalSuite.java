/********************************************************************************/
/*                                                                              */
/*              RoseEvalSuite.java                                              */
/*                                                                              */
/*      Test suite for evaluation                                               */
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Element;

import xxx.x.x.ivy.xml.IvyXml;

class RoseEvalSuite implements RoseEvalConstants
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/

private String          suite_name;
private String          workspace_name;
private String          project_name;
private Map<String,RoseEvalTest> test_set;
private long            default_time;




/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

RoseEvalSuite(Element xml)
{
   suite_name = IvyXml.getAttrString(xml,"NAME");
   workspace_name = IvyXml.getAttrString(xml,"WORKSPACE");
   project_name = IvyXml.getAttrString(xml,"PROJECT");
   test_set = new LinkedHashMap<>();
   default_time = IvyXml.getAttrLong(xml,"TIME",100000);
   
   for (Element telt : IvyXml.children(xml,"TEST")) {
      RoseEvalTest ret = new RoseEvalTest(this,telt);
      addTest(ret);
    }
}



RoseEvalSuite(String ws,String proj)
{
   suite_name = ws;
   workspace_name = ws;
   project_name = proj;
   test_set = new LinkedHashMap<>();
   default_time = 100000;
}



/********************************************************************************/
/*                                                                              */
/*      Setup methods                                                           */
/*                                                                              */
/********************************************************************************/

void addTest(RoseEvalTest t)
{
   if (t == null) return;
   test_set.put(t.getName(),t);
}



/********************************************************************************/
/*                                                                              */
/*      Access methods                                                          */
/*                                                                              */
/********************************************************************************/

String getName()                        { return suite_name; }

String getWorkspace()                   { return workspace_name; }

String getProject()                     { return project_name; }

RoseEvalTest findTest(String name)
{
   return test_set.get(name);
}

Collection<RoseEvalTest> getTests()
{
   return test_set.values();
   
}

long getDefaultTime()                   { return default_time; }






}       // end of class RoseEvalSuite




/* end of RoseEvalSuite.java */

