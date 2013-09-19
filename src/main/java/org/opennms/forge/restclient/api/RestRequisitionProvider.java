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

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
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
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class RestRequisitionProvider {

    /**
     * Path to OpenNMS ReST resource for requisition
     */
    private static final String ONMS_REST_REQUISITION_PATH = "rest/requisitions";

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
     * Logging
     */
    private static Logger logger = LoggerFactory.getLogger(RestRequisitionProvider.class);

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
        } catch (UniformInterfaceException | ClientHandlerException ex) {
            logger.error("Rest call for Requisitions went wrong. Error message '{}'. URL: '{}'", ex, m_webResource.getURI());
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
        m_webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH + "/" + foreignSource + parameters);
        Requisition requisition = null;
        logger.debug("TRY - getRequisition '{}', '{}'", foreignSource, m_webResource.getURI());
        try {
            requisition = m_webResource.header(HEADER_NAME_ACCEPT, MediaType.APPLICATION_XML).get(Requisition.class);
        } catch (UniformInterfaceException | ClientHandlerException ex) {
            logger.error("Rest call for Requisitions went wrong. Error message '{}'.", ex);
        }
        logger.debug("DONE - getRequisition '{}', '{}'", foreignSource, m_webResource.getURI());
        return requisition;
    }

    /**
     * <p>pushNodeToRequisition</p>
     * <p/>
     * Push one node to an OpenNMS remote requisition identified by foreign source. Update is for a single node.
     * This method will *NOT* synchronize the updated node to the OpenNMS database.
     *
     * @param foreignSource   Foreign source name of the requisition as {@link java.lang.String}
     * @param requisitionNode Requisition node for update as {@link org.opennms.netmgt.provision.persist.requisition.RequisitionNode}
     */
    public void pushNodeToRequisition(String foreignSource, RequisitionNode requisitionNode) {
        m_webResource = m_apacheHttpClient.resource(m_baseUrl +
                ONMS_REST_REQUISITION_PATH + "/" + foreignSource +
                ONMS_REST_REQUISITION_NODE_PATH);
        logger.debug("TRY  - pushNodeToRequisition '{}', '{}'", foreignSource, m_webResource.getURI());
        try {
            logger.debug("Requisition node to push: '{}' to URL '{}'", requisitionNode, m_webResource.getURI());
            m_webResource.type(MediaType.APPLICATION_XML).post(RequisitionNode.class, requisitionNode);
        } catch (UniformInterfaceException ex) {
            if (ex.getResponse().getStatus() == 303) {
                logger.debug("Got 303 redirect, not following");
            } else { 
                logger.error("Rest call for Node Update Requisitions went wrong", ex);
            }
        } catch (ClientHandlerException ex) {
            logger.error("Rest call for Node Update Requisitions went wrong", ex);
        }
        logger.debug("DONE - pushNodeToRequisition '{}', '{}'", foreignSource, m_webResource.getURI());
    }

    /**
     * <p>pushRequisition</p>
     * <p/>
     * Push one requisition to an OpenNMS. The requisition is in state pending and is ready to synchronize to
     * the database.
     *
     * @param requisition Requisition to push on a remote OpenNMS as {@link org.opennms.netmgt.provision.persist.requisition.Requisition}
     */
    public void pushRequisition(Requisition requisition) {
        m_webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH);

        try {
            logger.debug("TRY  - pushRequisition '{}', '{}'", requisition.getForeignSource(), m_webResource.getURI());
            m_webResource.type(MediaType.APPLICATION_XML).post(Requisition.class, requisition);
        } catch (UniformInterfaceException ex) {
            if (ex.getResponse().getStatus() == 303) {
                logger.debug("Got 303 redirect, not following");
            } else {
                logger.error("Unable to push requisition '{}' to OpenNMS with '{}'. Error message '{}'.", requisition.getForeignSource(), m_webResource.getURI(), ex.getMessage(), ex);
            }
        } catch (ClientHandlerException ex) {
            logger.error("Unable to push requisition '{}' to OpenNMS with '{}'. Error message '{}'.", requisition.getForeignSource(), m_webResource.getURI(), ex.getMessage(), ex);
        }
        logger.debug("DONE - pushRequisition '{}'", requisition.getForeignSource());
    }

    /**
     * <p>synchronizeForeignSource</p>
     * <p/>
     * Synchronize all nodes in a requisition identified by the foreign source with the OpenNMS database.
     *
     * @param foreignSource Synchronize all nodes identified by foreign source {@link java.lang.String}
     */
    public void synchronizeRequisition(String foreignSource) {
        m_webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH + "/" +
                foreignSource + "/import/");
        try {
            logger.debug("TRY  - to synchronize provisioning requisition: '{}'", m_webResource.getURI());
            m_webResource.type(MediaType.APPLICATION_FORM_URLENCODED).put();
        } catch (UniformInterfaceException ex) {
            if (ex.getResponse().getStatus() == 303) {
                logger.debug("Got 303 redirect, not following");
            } else {
                logger.error("Unable to synchronize provisioning requisition '{}' with '{}'. Error message '{}'.", foreignSource, m_webResource.getURI(), ex.getMessage(), ex);
            }
        } catch (ClientHandlerException ex) {
            logger.error("Unable to synchronize provisioning requisition '{}' with '{}'. Error message '{}'.", foreignSource, m_webResource.getURI(), ex.getMessage(), ex);
        }
        logger.debug("DONE - synchronize provisioning requisition: '{}'", m_webResource.getURI());
    }

    /**
     * TODO: never used
     * <p>synchronizeRequisitionSkipExisting</p>
     * <p/>
     * Synchronize requisition with new nodes identified by foreign source. Use this method to skip existing
     * nodes for re-import and detector rescan to preserve database load and provisioning load.
     *
     * @param foreignSource Foreign source to synchronize only non existing nodes as {@link java.lang.String}
     */
    public void synchronizeRequisitionSkipExisting(String foreignSource) {
        m_webResource = m_apacheHttpClient.resource(m_baseUrl + ONMS_REST_REQUISITION_PATH + "/" +
                foreignSource + ONMS_PROVISIONING_REQUISITION_RESCAN_FALSE);
        try {
            logger.debug("TRY  - to synchronize provisioning requisition: '{}'", m_webResource.getURI());
            m_webResource.type(MediaType.APPLICATION_XML).put();
        } catch (UniformInterfaceException ex) {
            if (ex.getResponse().getStatus() == 303) {
                logger.debug("Got 303 redirect, not following");
            } else {
                logger.error("Unable to synchronize provisioning requisition '{}' with '{}'. Error message '{}'.", foreignSource, m_webResource.getURI(), ex.getMessage(), ex);
            }
        } catch (ClientHandlerException ex) {
            logger.error("Unable to synchronize provisioning requisition '{}' with '{}'. Error message '{}'.", foreignSource, m_webResource.getURI(), ex.getMessage(), ex);
        }
        logger.debug("DONE - synchronize provisioning requisition: '{}'", m_webResource.getURI());
    }
}
