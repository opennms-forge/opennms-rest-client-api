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
package org.opennms.forge.restclient.api;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.opennms.forge.restclient.utils.RestConnectionParameter;
import org.opennms.forge.restclient.utils.RestHelper;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.MediaType;

/**
 * <p>RestRequisitionProvider class.</p>
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>*
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class RestRequisitionProvider {

    private static Logger logger = LoggerFactory.getLogger(RestRequisitionProvider.class);

    /**
     * Path to OpenNMS ReST resource for requisition
     */
    private static final String ONMS_REST_REQUISITION_PATH = "rest/requisitions/";

    /**
     * Path to OpenNMS ReST resource for requisition nodes
     */
    private static final String ONMS_REST_REQUISITION_NODE_PATH = "/nodes/";

    /**
     * Parameter to allow single requisition node synchronization
     */
    private static final String ONMS_PROVISIONING_REQUISITION_RESCAN_FALSE = "/import?rescanExisting=false";

    /**
     * ReST request header
     */
    private static final String HEADER_NAME_ACCEPT = "Accept";

    /**
     * Handle ReST request
     */
    private WebResource m_webResource;

    /**
     * HTTP client for ReST
     */
    private ApacheHttpClient m_apacheHttpClient;

    /**
     * OpenNMS base URL to web application
     */
    private String m_baseUrl;

    /**
     * Constructor to provide basic functionality to receive requisitions
     * with nodes from an OpenNMS instance
     *
     * @param restConnectionParameter Connection parameter for initializing HTTP client for ReST requests as {@link org.opennms.forge.restclient.utils.RestConnectionParameter}
     */
    public RestRequisitionProvider(RestConnectionParameter restConnectionParameter) {
        m_apacheHttpClient = RestHelper.createApacheHttpClient(restConnectionParameter);
        m_baseUrl = restConnectionParameter.getBaseUrl().toString();
    }

    /**
     * <p>getAllRequisitions</p>
     * <p/>
     * Get a collection of all provided OpenNMS requisitions.
     *
     * @param parameters Additional filter parameter as {@link java.lang.String}
     * @return Collection with all requisitions as {@link  org.opennms.netmgt.provision.persist.requisition.RequisitionCollection}
     */
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

    /**
     * <p>getRequisition</p>
     * <p/>
     * Get a specific requisition identified by its foreign source.
     *
     * @param foreignSource Foreign source name of the requisition as {@link java.lang.String}
     * @param parameters    Additional filter parameter for the requisition as {@link java.lang.String}
     * @return Requisition as {@link org.opennms.netmgt.provision.persist.requisition.Requisition}
     */
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
     * <p>updateCategoriesByNode</p>
     * <p/>
     * Method to update the provisioning requisition by node. This method will update just a single node with the
     * applied surveillance categories. It will *NOT* synchronize the updated node to the OpenNMS database.
     *
     * @param requisitionNode Requisition node for update as {@link org.opennms.netmgt.provision.persist.requisition.RequisitionNode}
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

    /**
     * <p>synchronizeForeignSource</p>
     * <p/>
     * Synchronize all nodes in a requisition identified by the foreign source.
     *
     * @param foreignSource Synchronize all nodes identified by foreign source {@link java.lang.String}
     */
    public void synchronizeRequisition(String foreignSource) {
        WebResource webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH +
                foreignSource);
        try {
            logger.debug("Try to synchronize provisioning requisition: '{}'", webResource.getURI());
            webResource.type(MediaType.APPLICATION_XML).put();
        } catch (Exception ex) {
            logger.error("Unable to synchronize provisioning requisition '{}' with '{}'.", foreignSource, webResource.getURI());
        }
    }

    /**
     * <p>synchronizeRequisitionNewly</p>
     * <p/>
     * Synchronize and scan non existing nodes to preserve unnecessary service detection or database load.
     *
     * @param foreignSource Foreign source to synchronize only non existing nodes as {@link java.lang.String}
     */
    public void synchronizeRequisitionNewly(String foreignSource) {
        WebResource webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH +
                foreignSource + ONMS_PROVISIONING_REQUISITION_RESCAN_FALSE);

        try {
            logger.debug("Try to synchronize provisioning requisition: '{}'", webResource.getURI());
            webResource.type(MediaType.APPLICATION_XML).put();
        } catch (Exception ex) {
            logger.error("Unable to synchronize provisioning requisition '{}' with '{}'.", foreignSource, webResource.getURI());
        }
    }
}
