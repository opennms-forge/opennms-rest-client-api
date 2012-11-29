/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package de.dertak.opennms.restclientapi.manager;

import de.dertak.opennms.restclientapi.helper.RestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus@OpenNMS.org
 */
public class RestRequisitionManagerTest {

    private static Logger logger = LoggerFactory.getLogger(RestRequisitionManagerTest.class);

    private String baseUrl = "http://demo.opennms.com/opennms/";
    private String username = "demo";
    private String password = "demo";

    private RestRequisitionManager manager;

    @Before
    public void setup() {
        manager = new RestRequisitionManager(RestHelper.createApacheHttpClient(username, password), baseUrl);
    }
    
    @Test
    public void testSomeMethod() {
        manager.loadNodesByLableForAllRequisitions();
        RequisitionNode reqNode = manager.getReqisitionNode("www.nasdaq.com");
        Assert.assertNotNull(reqNode);
    }
}
