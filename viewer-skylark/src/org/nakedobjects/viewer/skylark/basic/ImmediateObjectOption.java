package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.Action;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.control.Allow;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.utility.Assert;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.MenuOption;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Workspace;
import org.nakedobjects.viewer.skylark.core.BackgroundTask;
import org.nakedobjects.viewer.skylark.core.BackgroundThread;


/**
 * Options for an underlying object determined dynamically by looking for
 * methods starting with action, veto and option for specifying the action,
 * vetoing the option and giving the option an name respectively.
 */
class ImmediateObjectOption extends MenuOption {

    public static ImmediateObjectOption createOption(Action action, NakedObject object) {
        Assert.assertTrue("Only suitable for 0 param methods", action.getParameterTypes().length == 0);
        if (! action.isAuthorised() || object.isVisible(action).isVetoed()) {
            return null;
        }
        String labelName = action.getName();
        ImmediateObjectOption option = new ImmediateObjectOption(labelName, object, action);

	    return option;
    }

    private final Action action;
    private final NakedObject object;

    private ImmediateObjectOption(String name, NakedObject object, Action action) {
        super(name);
        this.object = object;
        this.action = action;
    }

    public Consent disabled(View view) {
        Consent valid = object.isValid(action, null);
        if (valid.isAllowed()) {
            String description = getName(view) + ": " + action.getDescription();
            if (action.hasReturn()) {
                description += " returns a " + action.getReturnType().getSingularName();
            }
            return new Allow(description);
        } else {
            return valid;
        }
    }

    // TODO this method is very similar to ActionDialogSpecification.execute()
    public void execute(final Workspace workspace, final View view, final Location at) {
        BackgroundThread.run(view, new BackgroundTask() {
            public void execute() {
                Naked returnedObject;
                returnedObject = object.execute(action, null);
                if (returnedObject != null) {
                    view.objectActionResult(returnedObject, at);
                }
                view.getViewManager().showMessages();
            }
            
            public String getName() {
                return "Action " + action.getId();
            }

            public String getDescription() {
                return "Running action " + getName() + " on  " + view.getContent().getNaked();
            }
        });
    }

    public String toString() {
        return "ObjectOption for " + action.getId();
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */
