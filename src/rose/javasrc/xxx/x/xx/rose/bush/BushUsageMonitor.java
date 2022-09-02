/********************************************************************************/
/*										*/
/*		BushUsageMonitor.java						*/
/*										*/
/*	Monitor programmer usage of Bush facilities				*/
/*										*/
/********************************************************************************/
/*********************************************************************************
 *										 *
 *			  All Rights Reserved					 *
 *										 *
 *  Permission to use, copy, modify, and distribute this software and its	 *
 *  documentation for any purpose other than its incorporation into a		 *
 *  commercial product is hereby granted without fee, provided that the 	 *
 *  above copyright notice appear in all copies and that both that		 *
 *  copyright notice and this permission notice appear in supporting		 *
 *  documentation, and that the name of Anonymous Institution X not be used in 	 *
 *  advertising or publicity pertaining to distribution of the software 	 *
 *  without specific, written prior permission. 				 *
 *										 *
 *  x UNIVERSITY DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS		 *
 *  SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND		 *
 *  FITNESS FOR ANY PARTICULAR PURPOSE.  IN NO EVENT SHALL x UNIVERSITY	 *
 *  BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY 	 *
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,		 *
 *  WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS		 *
 *  ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE 	 *
 *  OF THIS SOFTWARE.								 *
 *										 *
 ********************************************************************************/



package xxx.x.xx.rose.bush;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu;

import xxx.x.x.bubbles.bale.BaleFactory;
import xxx.x.x.bubbles.bale.BaleConstants.BaleContextConfig;
import xxx.x.x.bubbles.bale.BaleConstants.BaleContextListener;
import xxx.x.x.bubbles.bale.BaleConstants.BaleWindow;
import xxx.x.x.bubbles.buda.BudaBubble;
import xxx.x.x.bubbles.bump.BumpLocation;
import xxx.x.x.bubbles.buss.BussBubble;
import xxx.x.x.bubbles.buss.BussConstants.BussEntry;
import xxx.x.x.bubbles.buss.BussConstants.BussListener;

class BushUsageMonitor implements BushConstants, BussListener
{


/********************************************************************************/
/*										*/
/*	Private Storage 							*/
/*										*/
/********************************************************************************/

private Map<BumpLocation,String> location_data;
private String		metric_id;

private static List<BushUsageMonitor> all_monitors = new ArrayList<>();

static {
   BaleFactory.getFactory().addContextListener(new EditorListener());
}


/********************************************************************************/
/*										*/
/*	Constructors								*/
/*										*/
/********************************************************************************/

BushUsageMonitor(BussBubble buss,String id,Map<BumpLocation,String> locs)
{
   location_data = locs;
   metric_id = id;
   all_monitors.add(this);
   if (buss != null) buss.addBussListener(this);
}



static void remove(BushUsageMonitor um)
{
   all_monitors.remove(um);
}



/********************************************************************************/
/*										*/
/*	Handle bubble stack events						*/
/*										*/
/********************************************************************************/

@Override public void entrySelected(BussEntry ent)
{
   handleEvent(ent,"SHOW_SELECTED");
}


@Override public void entryExpanded(BussEntry ent)
{
   handleEvent(ent,"SHOW_EXPANDED");
}


@Override public void entryHovered(BussEntry ent)
{
   handleEvent(ent,"SHOW_HOVERED");
}


private void handleEvent(BussEntry ent,String what)
{
   Collection<BumpLocation> locs = ent.getLocations();
   for (BumpLocation loc : locs) {
      String reason = location_data.get(loc);
      if (reason != null) {
	 BushFactory.metrics(what,metric_id,reason);
       }
    }
}



/********************************************************************************/
/*										*/
/*	Track editors								*/
/*										*/
/********************************************************************************/

private static class EditorListener implements BaleContextListener {

   @Override public BudaBubble getHoverBubble(BaleContextConfig cfg) {
      return null;
    }

   @Override public void addPopupMenuItems(BaleContextConfig cfg,JPopupMenu menu) { }

   @Override public String getToolTipHtml(BaleContextConfig cfg) {
      return null;
    }

   @Override public void noteEditorAdded(BaleWindow win) {
      // check window versus locations
    }

   @Override public void noteEditorRemoved(BaleWindow win) {
      // check window versus locations
    }

}	// end of inner class EditorListener



}	// end of class BushUsageMonitor




/* end of BushUsageMonitor.java */

