/********************************************************************************/
/*                                                                              */
/*              RoseControl.java                                                */
/*                                                                              */
/*       Controller for ROSE-Bubbles-Fait interactions                          */
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

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;
import org.w3c.dom.Element;

import xxx.x.x.ivy.leash.LeashIndex;
import xxx.x.x.ivy.mint.MintControl;
import xxx.x.x.ivy.mint.MintConstants.CommandArgs;

public interface RootControl
{


/**
 *      Send a message to FAIT and wait for reply
 **/

Element sendFaitMessage(String cmd,CommandArgs args,String xmlcnts);

Element sendSeedeMessage(String sid,String cmd,CommandArgs args,String xmlcnts);


/**
 *      Send a message to Bubbles back end (BEDROCK) and wait for reply
 **/

Element sendBubblesMessage(String cmd,CommandArgs args,String xmlcnts);


/**
 *      Wait for an evaluation to return and return the evaluation result
 **/ 

Element waitForEvaluation(String eid);


/**
 *      Send a message out.  If wait is < 0, no reply is needed.  Otherwise
 *      wait for <wait> ms for a reply.  (0 implies forever).
 **/

Element sendRoseMessage(String cmd,CommandArgs args,String xmlcnts,long wait);


/**
 *      Get AST Node for a location
 **/

ASTNode getSourceNode(String proj,File f,int offset,int line,boolean resolve,boolean stmt);

ASTNode getNewSourceStatement(File f,int line,int col);


public default ASTNode getSourceNode(RootLocation loc,boolean resolve,boolean stmt)
{
   int line = -1;
   if (loc.getStartOffset() < 0) line = loc.getLineNumber();
   
   return getSourceNode(loc.getProject(),loc.getFile(),loc.getStartOffset(),line,resolve,stmt);
}


public default ASTNode getSourceStatement(RootLocation loc,boolean resolve)
{
   return getSourceNode(loc,resolve,true);
}


public default ASTNode getSourceStatement(String proj,File f,int offset,int line,boolean resolve)
{
   return getSourceNode(proj,f,offset,line,resolve,true);
}


/**
 *      return document to be used to create text edit for a change
 **/
 
IDocument getSourceDocument(File f);


/**
 *      Return the contents of the given file (cached)
 **/

String getSourceContents(File file);


/** 
 *      Compile a set of files together for later access
 **/

default void compileAll(Collection<File> f) 
{
   compileAll(null,f);
}

void compileAll(String proj,Collection<File> f);

/**
 *      Compile source from string
 **/
CompilationUnit compileSource(RootLocation loc,String code);


/**
 *      Return locations associated with a problem
 **/

List<RootLocation> getLocations(RootProblem prob);



/**
 *      Return node that is the cause of an exception problem
 **/

ASTNode getExceptionNode(RootProblem prob);


/**
 *      Return information about an assertion problem
 **/

AssertionData getAssertionData(RootProblem prob);


interface AssertionData {
   ASTNode getExpression();
   String getOriginalValue();
   String getTargetValue();
   boolean isLocation();
}




/**
 *      Find project associated with a given file
 **/

String getProjectForFile(File f);


/**
 *      Return mint handle for local commands
 **/

MintControl getMintControl();


/**
 *      Return index to project database for cocker
 ***/

LeashIndex getProjectIndex();


/**
 *      Return index to global database for cocker
 ***/

LeashIndex getGlobalIndex();

Set<File> getLoadedFiles();
Set<File> getSeedeFiles(String threadid); 
void loadFilesIntoFait(String threadid,Element files,boolean all) throws RoseException;
void useFaitFilesForSeede();
File getFileForClass(String cls);


}       // end of interface RoseControl




/* end of RoseControl.java */

