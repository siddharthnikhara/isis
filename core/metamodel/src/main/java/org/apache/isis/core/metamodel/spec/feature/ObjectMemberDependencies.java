/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.core.metamodel.spec.feature;

import org.apache.isis.core.metamodel.adapter.QuerySubmitter;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;
import org.apache.isis.core.metamodel.spec.SpecificationLoader;

public class ObjectMemberDependencies {

    private final SpecificationLoader specificationLoader;
    private final AdapterManager adapterManager;
    private final QuerySubmitter querySubmitter;
    private final ServicesInjector servicesInjector;

    public ObjectMemberDependencies(
            final SpecificationLoader specificationLoader,
            final AdapterManager adapterManager,
            final QuerySubmitter querySubmitter,
            final ServicesInjector servicesInjector) {

        this.specificationLoader = specificationLoader;
        this.adapterManager = adapterManager;
        this.querySubmitter = querySubmitter;

        this.servicesInjector = servicesInjector;
    }

    public SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }

    public AdapterManager getAdapterManager() {
        return adapterManager;
    }

    public ServicesInjector getServicesInjector() {
        return servicesInjector;
    }
    
    public QuerySubmitter getQuerySubmitter() {
        return querySubmitter;
    }
}