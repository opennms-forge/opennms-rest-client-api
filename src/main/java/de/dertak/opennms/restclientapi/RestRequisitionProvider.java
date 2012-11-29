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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus@OpenNMS.org
 */
public class RestRequisitionProvider {

    private static Logger logger = LoggerFactory.getLogger(RestRequisitionProvider.class);

    public static RequisitionCollection getRequisitions(ApacheHttpClient httpClient, String baseUrl, String parameters) {
        WebResource webResource = httpClient.resource(baseUrl + "rest/requisitions/" + parameters);
        RequisitionCollection requisitions = null;
        try {
            requisitions = webResource.header("Accept", "application/xml").get(RequisitionCollection.class);
        } catch (Exception ex) {
            logger.debug("Rest-Call for Requisitions went wrong", ex);
        }
        return requisitions;
    }

    public static void updateRequisionNodeCategories(ApacheHttpClient httpClient, String baseUrl, String parameters, RequisitionNode requisitionNode) {
        WebResource webResource = httpClient.resource(baseUrl + "rest/requisitions/" + requisitionNode.getParentForeignSource() + "/nodes/"+ requisitionNode.getForeignId() +"/categories" + parameters);
        try {
            webResource.type("application/xml").post(ClientResponse.class, requisitionNode.getCategories());
        } catch (Exception ex) {
            logger.debug("Rest-Call for Node Update Requisitions went wrong", ex);
        }
    }
}
