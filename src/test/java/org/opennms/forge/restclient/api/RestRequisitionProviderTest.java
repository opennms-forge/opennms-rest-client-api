/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with OpenNMS(R). If not, see:
 * http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org> http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */

package org.opennms.forge.restclient.api;

import java.net.MalformedURLException;
import org.junit.Before;
import org.junit.Test;
import org.opennms.forge.restclient.utils.OnmsRestConnectionParameter;

import org.slf4j.LoggerFactory;
/**
 *
 * @author Markus@OpenNMS.org
 */
public class RestRequisitionProviderTest {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RestRequisitionProviderTest.class);

    private String baseUrl = "http://localhost:8980/opennms/";
    private String username = "DANGER";
    private String password = "DONTRUNTHISWITHOUTBRAIN";

    private OnmsRestConnectionParameter connectionParameter;
    
    private RestRequisitionProvider restRequisitionProvider;

//    public RestRequisitionProviderTest() {
//    }
//
    @Before
    public void setUp() throws MalformedURLException {
        connectionParameter = new OnmsRestConnectionParameter(baseUrl, username, password);
        restRequisitionProvider = new RestRequisitionProvider(connectionParameter);
    }

    @Test
    public void testSynchronizeRequisition() {
        restRequisitionProvider.synchronizeRequisition("RestProvisioningTest");
    }

//    @Test
//    public void testGetAllRequisitions() {
//    }
//
//    @Test
//    public void testGetRequisition() {
//    }
//
//    @Test
//    public void testPushNodeToRequisition() {
//    }
//
//    @Test
//    public void testPushRequisition() {
//    }

//    @Test
//    public void testSynchronizeRequisitionSkipExisting() {
//    }

}