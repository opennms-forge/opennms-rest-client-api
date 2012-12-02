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
 ******************************************************************************
 */
package de.dertak.opennms.restclientapi;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

/**
 * @author Markus@OpenNMS.org
 */
public class RestRequisitionProvider {

    private static Logger logger = LoggerFactory.getLogger(RestRequisitionProvider.class);

    /**
     * Set path to OpenNMS ReST request for
     */
    private static final String ONMS_REST_REQUISITION_PATH = "rest/requisitions/";

    private static final String ONMS_REST_REQUISITION_NODE_PATH = "/nodes/";

    private static final String ONMS_PROVISIONING_REQUISITION_RESCAN_FALSE = "/import?rescanExisting=false";

    private static final String HEADER_NAME_ACCEPT = "Accept";

    private WebResource m_webResource;

    private ApacheHttpClient m_apacheHttpClient;

    private String m_baseUrl;

    public RestRequisitionProvider(ApacheHttpClient apacheHttpClient, String baseUrl) {
        m_apacheHttpClient = apacheHttpClient;
        m_baseUrl = baseUrl;
    }

    public RequisitionCollection getAllRequisitions(String parameters) {
        m_webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH + parameters);
        logger.debug("ReST request: '{}'", m_webResource.getURI());
        RequisitionCollection requisitions = null;
        try {
            requisitions = m_webResource.header(HEADER_NAME_ACCEPT, MediaType.APPLICATION_XML).get(RequisitionCollection.class);
        } catch (Exception ex) {
            logger.debug("Rest call for Requisitions went wrong. Error message '{}'. URL: '{}'", ex, m_webResource.getURI());
        }
        return requisitions;
    }

    public Requisition getRequisition(String foreignSource, String parameters) {
        m_webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH + foreignSource + parameters);
        Requisition requisition = null;
        logger.debug("Try getRequisition '{}', '{}'", foreignSource, m_webResource.getURI());
        try {
            requisition = m_webResource.header(HEADER_NAME_ACCEPT, MediaType.APPLICATION_XML).get(Requisition.class);
        } catch (Exception ex) {
            logger.debug("Rest call for Requisitions went wrong. Error message '{}'.", ex);
        }
        return requisition;
    }

    /**
     * Method to update the provisioning requisition by node. This method will update just a single node with the applied surveillance
     * categories. It will synchronize the updated node directly to the OpenNMS database.
     *
     * @param requisitionNode Requisition node definition for update as {@link org.opennms.netmgt.provision.persist.requisition.RequisitionNode}
     */
    public void updateCategoriesByNode(String foreignSource, RequisitionNode requisitionNode) {
        WebResource webResource = m_apacheHttpClient.resource(m_baseUrl +
                ONMS_REST_REQUISITION_PATH + foreignSource +
                ONMS_REST_REQUISITION_NODE_PATH);

        try {
            logger.debug("Requisition node to push: '{}' to URL '{}'", requisitionNode, webResource.getURI());
            webResource.type(MediaType.APPLICATION_XML).post(ClientResponse.class, requisitionNode);
        } catch (Exception ex) {
            logger.debug("Rest call for Node Update Requisitions went wrong", ex);
        }
    }

    public void synchronizeNewlyProvisionedNodes(String provisioningRequisition) {
        // http://localhost:8980/opennms/rest/requisitions/renamed-hiphop-artists/import?rescanExisting=false
        WebResource webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH +
                provisioningRequisition + ONMS_PROVISIONING_REQUISITION_RESCAN_FALSE);
        try {
            logger.debug("Try to synchronize provisioning requisition: '{}'", webResource.getURI());
            webResource.type(MediaType.APPLICATION_XML).put();
        } catch (Exception ex) {
            logger.error("Unable to synchronize provisioning requisition '{}' with '{}'.", provisioningRequisition, webResource.getURI());
        }
    }
}
