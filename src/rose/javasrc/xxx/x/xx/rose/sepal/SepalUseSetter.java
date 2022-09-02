/********************************************************************************/
/*                                                                              */
/*              SepalUseSetter.java                                             */
/*                                                                              */
/*      Try using non-trivial setter if available                               */
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



package xxx.x.xx.rose.sepal;

import java.util.List;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import xxx.x.xx.ivy.jcomp.JcompAst;
import xxx.x.xx.ivy.jcomp.JcompSymbol;
import xxx.x.xx.ivy.jcomp.JcompType;
import xxx.x.xx.rose.root.RootRepairFinderDefault;

public class SepalUseSetter extends RootRepairFinderDefault
{


/********************************************************************************/
/*                                                                              */
/*      Private Storage                                                         */
/*                                                                              */
/********************************************************************************/



/********************************************************************************/
/*                                                                              */
/*      Constructors                                                            */
/*                                                                              */
/********************************************************************************/

public SepalUseSetter()
{ }



/********************************************************************************/
/*                                                                              */
/*      Processing methods                                                      */
/*                                                                              */
/********************************************************************************/

@Override public double getFinderPriority()
{
   return 0.5;
}




@SuppressWarnings("unchecked")
@Override public void process()
{
   ASTNode stmt = getResolvedStatementForLocation(null);
   if (stmt == null) return;
   if (!(stmt instanceof ExpressionStatement)) return;
   ExpressionStatement exprstmt = (ExpressionStatement) stmt;
   Expression expr = exprstmt.getExpression();
   if (!(expr instanceof Assignment)) return;
   Assignment asgn = (Assignment) expr;
   if (asgn.getOperator() != Assignment.Operator.ASSIGN) return;
   
   ASTNode mthd = null;
   for (mthd = stmt; mthd != null; mthd = mthd.getParent()) {
      if (mthd instanceof MethodDeclaration) break;
    }
   if (mthd == null) return;
   MethodDeclaration md = (MethodDeclaration) mthd;
   if (!md.isConstructor()) return;
   
   String nm = null;
   Expression lhs = asgn.getLeftHandSide();
   if (lhs instanceof FieldAccess) {
      FieldAccess fldacc = (FieldAccess) lhs;
      if (!(fldacc.getExpression() instanceof ThisExpression)) return;
      nm = fldacc.getName().getIdentifier();
    }
   else if (lhs instanceof SimpleName) {
      SimpleName sn = (SimpleName) lhs;
      JcompSymbol ref = JcompAst.getReference(sn);
      if (ref.isFieldSymbol()) nm = sn.getIdentifier();
    }
   if (nm == null) return;
   
   JcompSymbol csym = JcompAst.getDefinition(mthd);
   JcompType ctyp = csym.getClassType();
   List<JcompSymbol> mthds = ctyp.getDefinedMethods(JcompAst.getTyper(stmt));
   for (JcompSymbol m : mthds) {
      if (m.getName().equalsIgnoreCase("set"+nm)) {
         if (isGoodSetter(m)) {
            AST ast = stmt.getAST();
            MethodInvocation mi = ast.newMethodInvocation();
            SimpleName mnm = JcompAst.getSimpleName(ast,m.getName());
            mi.setName(mnm);
            Expression arg = (Expression) ASTNode.copySubtree(ast,asgn.getRightHandSide());
            mi.arguments().add(arg);
            ASTRewrite rw = ASTRewrite.create(ast);
            rw.replace(expr,mi,null);
            addRepair(rw,"Use " + m.getName() + " rather than assignment",null,0.5);
          }
       }
    }
}



private boolean isGoodSetter(JcompSymbol m)
{
   if (m.isStatic()) return false;
   JcompType mtyp = m.getType();
   if (mtyp == null) return false;
   if (mtyp.getComponents().size() != 1) return false;
   
   ASTNode n = m.getDefinitionNode();
   if (n == null) return false;
   if (!(n instanceof MethodDeclaration)) return false;
   MethodDeclaration md = (MethodDeclaration) n;
   Block blk = md.getBody();
   if (blk == null) return false;
   if (blk.statements().size() > 2) return false;
   
   return true;
}


}       // end of class SepalUseSetter




/* end of SepalUseSetter.java */

