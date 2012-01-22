/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.viewer.scimpi.dispatcher.view.simple;

import java.util.List;

import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facets.object.immutable.ImmutableFacet;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociationFilters;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.viewer.scimpi.dispatcher.Dispatcher;
import org.apache.isis.viewer.scimpi.dispatcher.processor.Request;

public class EditLink extends AbstractLink {

    @Override
    protected boolean valid(final Request request, final ObjectAdapter adapter) {
        final ObjectSpecification specification = adapter.getSpecification();
        final AuthenticationSession session = IsisContext.getAuthenticationSession();
        final List<ObjectAssociation> visibleFields = specification.getAssociations(ObjectAssociationFilters.dynamicallyVisible(session, adapter));
        final ImmutableFacet facet = specification.getFacet(ImmutableFacet.class);
        final boolean isImmutable = facet != null && facet.value() == org.apache.isis.core.metamodel.facets.When.ALWAYS;
        final boolean isImmutableOncePersisted = facet != null && facet.value() == org.apache.isis.core.metamodel.facets.When.ONCE_PERSISTED && adapter.isPersistent();
        return visibleFields.size() > 0 && !isImmutable && !isImmutableOncePersisted;
    }

    @Override
    protected String linkLabel(final String name, final ObjectAdapter object) {
        return "edit";
    }

    @Override
    protected String defaultView() {
        return Dispatcher.GENERIC + Dispatcher.EDIT + "." + Dispatcher.EXTENSION;
    }

    @Override
    public String getName() {
        return "edit-link";
    }

}
