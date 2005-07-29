package org.nakedobjects.viewer.skylark.metal;

import org.nakedobjects.object.NakedObjectApplicationException;
import org.nakedobjects.object.reflect.NameConvertor;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.utility.ExceptionHelper;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.core.AbstractView;

public class AbstractErrorView extends AbstractView {

    protected String message;
    protected String name;
    protected String trace;

    protected AbstractErrorView(Content content, ViewSpecification specification, ViewAxis axis) {
        super(content, specification, axis);

        Exception error = (Exception) ((PojoAdapter) ((ObjectContent) getContent()).getObject()).getObject();
        if(error instanceof NakedObjectApplicationException) {
            Throwable t = ((NakedObjectApplicationException) error).getCause();
	        String name = t.getClass().getName();
	        name = name.substring(name.lastIndexOf('.') + 1);
	        this.name = NameConvertor.naturalName(name);
	        this.message = t.getMessage();
	        this.trace = ExceptionHelper.exceptionTraceAsString(t);
        } else {
            String name = error.getClass().getName();
	        name = name.substring(name.lastIndexOf('.') + 1);
	        this.name = NameConvertor.naturalName(name);
	        this.message = error.getMessage();
	        this.trace = ExceptionHelper.exceptionTraceAsString(error);
        }
        
        if(this.name == null) {
            this.name = "";
        }
        if(this.message == null) {
            this.message = "";
        }
        if(this.trace == null) {
            this.trace = "";
        }
    }
    

}


/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2005  Naked Objects Group Ltd

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

The authors can be contacted via www.nakedobjects.org (the
registered address of Naked Objects Group is Kingsway House, 123 Goldworth
Road, Woking GU21 1NR, UK).
*/