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
package org.apache.isis.applib.fixturescripts;

import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecification;

/**
 * Overrides the {@link FixtureScriptsSpecification#getMultipleExecutionStrategy() globally-defined}
 * {@link FixtureScripts.MultipleExecutionStrategy} strategy, allowing individual fixtures to indicate that they have
 * value semantics (ie be executed as per {@link FixtureScripts.MultipleExecutionStrategy#EXECUTE_ONCE_BY_VALUE}.
 */
public interface ValueFixtureScript {}