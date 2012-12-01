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
package de.dertak.opennms.restclientapi.manager;

import com.sun.jersey.client.apache.ApacheHttpClient;
import de.dertak.opennms.restclientapi.RestRequisitionProvider;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Markus@OpenNMS.org
 */
//TODO missing structure that adds the requisition for the nodes
public class RestRequisitionManager {

    private ApacheHttpClient httpClient = null;
    private String baseUrl = null;
    private Map<String, RequisitionNode> reqNodesByLabel = new HashMap<String, RequisitionNode>();
    private RestRequisitionProvider m_restRequisitionProvider;
    private Requisition m_requisition;

    public RestRequisitionManager(ApacheHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
        m_restRequisitionProvider = new RestRequisitionProvider(httpClient, baseUrl);
    }

    public void loadNodesByLabelForRequisition(String requisitionName, String parameter) {
        m_requisition = m_restRequisitionProvider.getRequisition(requisitionName, parameter);
        for (RequisitionNode reqNode : m_requisition.getNodes()) {
            reqNodesByLabel.put(reqNode.getNodeLabel(), reqNode);
        }
    }

    public RequisitionNode getRequisitionNode(String nodeLabel) {
        return reqNodesByLabel.get(nodeLabel);
    }

    public Requisition getRequisition() {
        return m_requisition;
    }
}