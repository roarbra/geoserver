/* (c) 2025 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.v2_0;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import javax.xml.namespace.QName;
import org.geoserver.data.test.SystemTestData;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class GetFeaturePropertyIsEqualTest extends WFS20TestSupport {

    private final String PREFIX = "app";
    private final String URI = "http://kartverket.no/";

    @Override
    protected void setUpInternal(SystemTestData data) throws Exception {

        data.addVectorLayer(
                new QName(URI, "Fylker", PREFIX), Collections.emptyMap(), "fylke.properties", getClass(), getCatalog());
    }

    @Test
    public void testGetFeatureFilterLeadingZero() throws Exception {
        testCall("03");
    }

    @Test
    public void testGetFeatureFilter() throws Exception {
        testCall("32");
    }

    private void testCall(String value) throws Exception {
        final String typeName = PREFIX + ":Fylker";
        String xml = "<GetFeature service=\"WFS\" version=\"2.0.0\" xmlns:" + PREFIX + "=\"" + URI + "\""
                + " xmlns:fes=\"http://www.opengis.net/fes/2.0\" outputFormat=\"text/xml; subtype=gml/3.2\">"
                + "<Query typeNames=\""
                + typeName
                + "\">"
                + "<fes:Filter>"
                + "<fes:PropertyIsEqualTo>"
                + "<fes:ValueReference>nummer</fes:ValueReference>"
                + "<fes:Literal>"
                + value
                + "</fes:Literal>"
                + "</fes:PropertyIsEqualTo>"
                + "</fes:Filter>"
                + "</Query>"
                + "</GetFeature>";
        Document doc = postAsDOM("wfs", xml);

        NodeList features = doc.getElementsByTagName("wfs:member");
        assertEquals(1, features.getLength());
    }
}
