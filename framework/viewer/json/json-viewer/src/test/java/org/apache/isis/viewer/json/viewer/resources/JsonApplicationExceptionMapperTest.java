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
package org.apache.isis.viewer.json.viewer.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Response;

import org.apache.isis.viewer.json.applib.JsonRepresentation;
import org.apache.isis.viewer.json.applib.RestfulResponse.HttpStatusCode;
import org.apache.isis.viewer.json.applib.util.JsonMapper;
import org.apache.isis.viewer.json.viewer.JsonApplicationException;
import org.apache.isis.viewer.json.viewer.JsonApplicationExceptionMapper;
import org.junit.Before;
import org.junit.Test;

public class JsonApplicationExceptionMapperTest {

    private JsonApplicationExceptionMapper exceptionMapper;

    @Before
    public void setUp() throws Exception {
        exceptionMapper = new JsonApplicationExceptionMapper();
    }

    @Test
    public void simpleNoMessage() throws Exception {

        // given
        final HttpStatusCode status = HttpStatusCode.BAD_REQUEST;
        final JsonApplicationException ex = JsonApplicationException.create(status);

        // when
        final Response response = exceptionMapper.toResponse(ex);

        // then
        assertThat(HttpStatusCode.lookup(response.getStatus()), is(status));
        assertThat(response.getMetadata().get("Warning"), is(nullValue()));

        // and then
        final String entity = (String) response.getEntity();
        assertThat(entity, is(not(nullValue())));
        final JsonRepresentation jsonRepr = JsonMapper.instance().read(entity, JsonRepresentation.class);

        // then
        assertThat(jsonRepr.getString("message"), is(nullValue()));
        assertThat(jsonRepr.getArray("stackTrace"), is(not(nullValue())));
        assertThat(jsonRepr.getArray("stackTrace").size(), is(greaterThan(0)));
        assertThat(jsonRepr.getRepresentation("causedBy"), is(nullValue()));
    }

    @Test
    public void entity_withMessage() throws Exception {

        // givens
        final JsonApplicationException ex = JsonApplicationException.create(HttpStatusCode.BAD_REQUEST, "foobar");

        // when
        final Response response = exceptionMapper.toResponse(ex);

        // then
        assertThat((String) response.getMetadata().get("Warning").get(0), is(ex.getMessage()));

        // and then
        final String entity = (String) response.getEntity();
        assertThat(entity, is(not(nullValue())));
        final JsonRepresentation jsonRepr = JsonMapper.instance().read(entity, JsonRepresentation.class);

        // then
        assertThat(jsonRepr.getString("message"), is(ex.getMessage()));
    }

    @Test
    public void entity_withCause() throws Exception {
        // given
        final Exception cause = new Exception("barfoo");
        final JsonApplicationException ex = JsonApplicationException.create(HttpStatusCode.BAD_REQUEST, cause, "foobar");

        // when
        final Response response = exceptionMapper.toResponse(ex);
        final String entity = (String) response.getEntity();
        assertThat(entity, is(not(nullValue())));
        final JsonRepresentation jsonRepr = JsonMapper.instance().read(entity, JsonRepresentation.class);

        // then
        assertThat(jsonRepr.getString("message"), is(ex.getMessage()));
        final JsonRepresentation causedByRepr = jsonRepr.getRepresentation("causedBy");
        assertThat(causedByRepr, is(not(nullValue())));
        assertThat(causedByRepr.getString("message"), is(cause.getMessage()));
    }

}
