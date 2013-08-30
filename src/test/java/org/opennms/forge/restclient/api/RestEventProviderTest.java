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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opennms.forge.restclient.utils.OnmsRestConnectionParameter;
import org.opennms.netmgt.model.OnmsEvent;

import org.slf4j.LoggerFactory;
/**
 *
 * @author Markus@OpenNMS.org
 */
public class RestEventProviderTest {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RestEventProviderTest.class);

    private String baseUrl = "http://demo.opennms.com/opennms/";
    private String username = "demo";
    private String password = "demo";

    private OnmsRestConnectionParameter connectionParameter;

    @Before
    public void setUp() throws MalformedURLException {
        connectionParameter = new OnmsRestConnectionParameter(baseUrl, username, password);
    }

    @Test
    public void testGetEvents() {
        System.out.println("getEvents");
        String parameters = "?limit=42";
        List<OnmsEvent> expResult = null;
        List<OnmsEvent> result = RestEventProvider.getEvents(connectionParameter, parameters);
        System.out.println("result size: " + result.size());
        assertEquals(42, result.size());
    }

    @Test
    public void testGetEventsByUEI() {
        System.out.println("getEventsByUEI");
        String uei = "uei.opennms.org/nodes/nodeDown";
        String parameters = "&limit=23";
        List<OnmsEvent> expResult = null;
        List<OnmsEvent> result = RestEventProvider.getEventsByUEI(connectionParameter, uei, parameters);
        System.out.println("result size: " + result.size());
        assertEquals(23, result.size());
        OnmsEvent myEvent = result.get(0);

        myEvent.getSeverityLabel();
    }

    @Test
    public void testCountEventsByServerity() {
        System.out.println("countEventsByServerity");
        Map<String, Integer> amountOfEventsByServerity = new LinkedHashMap<String, Integer>();
        amountOfEventsByServerity.put("INDETERMINATE", null);
        amountOfEventsByServerity.put("CLEARED", null);
        amountOfEventsByServerity.put("NORMAL", null);
        amountOfEventsByServerity.put("WARNING", null);
        amountOfEventsByServerity.put("MINOR", null);
        amountOfEventsByServerity.put("MAJOR", null);
        amountOfEventsByServerity.put("CRITICAL", null);

        for (String serverity : amountOfEventsByServerity.keySet()) {
            Integer amount = RestEventProvider.countEventsByServerity(connectionParameter, serverity, "&limit=1");
            amountOfEventsByServerity.put(serverity, amount);
        }
        
        for (String serverity: amountOfEventsByServerity.keySet()) {
            System.out.println(amountOfEventsByServerity.get(serverity) + "\t" + serverity);
        }
    }

}