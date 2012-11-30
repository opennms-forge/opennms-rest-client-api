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
import java.util.HashMap;
import java.util.Map;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionCollection;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;

/**
 *
 * @author Markus@OpenNMS.org
 */
//TODO missing structure that adds the requisition for the nodes
public class RestRequisitionManager {

    private ApacheHttpClient httpClient = null;
    private String baseUrl = null;
    private Map<String, RequisitionNode> reqNodesByLable = new HashMap<String, RequisitionNode>();

    public RestRequisitionManager(ApacheHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
        assert this.httpClient != null;
        assert this.baseUrl != null;
    }

    public void loadNodesByLableForRequisition(String parameter) {
        RequisitionCollection requisitions = RestRequisitionProvider.getRequisitions(httpClient, baseUrl, parameter);
        for (Requisition requisition : requisitions) {
            for (RequisitionNode reqNode : requisition.getNodes()) {
                reqNodesByLable.put(reqNode.getNodeLabel(), reqNode);
            }
        }
    }

    public void loadNodesByLableForAllRequisition(String restProvisioningTest) {
        loadNodesByLableForRequisition("");
    }

    public RequisitionNode getReqisitionNode(String nodeLabel) {
        return reqNodesByLable.get(nodeLabel);
    }
}