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
package de.dertak.opennms.restclientapi;

import de.dertak.opennms.restclientapi.helper.RestHelper;
import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.provision.persist.requisition.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 */
public class RestRequisitionProviderTest {

    private static Logger logger = LoggerFactory.getLogger(RestRequisitionProviderTest.class);

    private String baseUrl = "http://localhost:8980/opennms/";
    private String username = "admin";
    private String password = "admin";
    private RestRequisitionProvider m_restRequisitionProvider;

    @Before
    public void setUp() {
        m_restRequisitionProvider = new RestRequisitionProvider(RestHelper.createApacheHttpClient(username, password), baseUrl);
    }

    @Test
    public void testGetRequisitions() {

        logger.info("Getting Requisitions from '{}'", baseUrl);
        RequisitionCollection requisitions = m_restRequisitionProvider.getAllRequisitions("?limit=0");
        logger.info("Received '{}' Requisitions from '{}'", requisitions.size(), baseUrl);
        for (Requisition requisition : requisitions.getRequisitions()) {
            for (RequisitionNode requNode : requisition.getNodes()) {
                for (RequisitionCategory requCategory : requNode.getCategories()) {
                    logger.info("Found Category Name '{}'", requCategory.getName());
                }
            }
        }
        logger.info("Thanks for computing with OpenNMS!");
    }

    @Test
    public void testUpdateCategoriesByNode() {
        Collection<RequisitionCategory> requisitionCategoryCollection = new ArrayList<RequisitionCategory>();
        Collection<RequisitionInterface> requisitionInterfaceCollection = new ArrayList<RequisitionInterface>();

        RequisitionInterface requisitionInterfaceV4 = new RequisitionInterface();
        RequisitionInterface requisitionInterfaceV6 = new RequisitionInterface();
        requisitionInterfaceV4.setIpAddr("127.0.0.1");
        requisitionInterfaceV4.setSnmpPrimary(PrimaryType.PRIMARY);
        requisitionInterfaceV6.setIpAddr("::1");
        requisitionInterfaceV6.setSnmpPrimary(PrimaryType.NOT_ELIGIBLE);

        requisitionInterfaceCollection.add(requisitionInterfaceV4);
        requisitionInterfaceCollection.add(requisitionInterfaceV6);

        requisitionCategoryCollection.add(new RequisitionCategory("TestSurveillanceCategory"));

        logger.info("Getting requisitions from '{}'", baseUrl);
        Requisition requisition = m_restRequisitionProvider.getRequisition("Amazon", "");
        RequisitionNode requisitionNode = new RequisitionNode();
        requisitionNode.setBuilding("Amazon");
        requisitionNode.setNodeLabel("TestNode");
        requisitionNode.setForeignId("UnitTest");
        requisitionNode.setCategories(requisitionCategoryCollection);
        requisitionNode.setInterfaces(requisitionInterfaceCollection);

        logger.info("Received: '{}' requisitions from '{}'", requisition.getNodeCount(), baseUrl);

        m_restRequisitionProvider.updateCategoriesByNode("Amazon", requisitionNode);
        m_restRequisitionProvider.synchronizeNewlyProvisionedNodes("Amazon");
    }
}
