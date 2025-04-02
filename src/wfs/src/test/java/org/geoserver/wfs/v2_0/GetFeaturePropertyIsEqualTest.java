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
import org.w3c.dom.Node;
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
        Document doc = testCallStrict("03");

        NodeList features = doc.getElementsByTagName("wfs:member");
        assertEquals(1, features.getLength());
    }

    @Test
    public void testGetFeatureFilter() throws Exception {
        Document doc = testCallStrict("32");

        NodeList features = doc.getElementsByTagName("wfs:member");
        assertEquals(1, features.getLength());
    }

    @Test
    public void testGetFeatureWithOgcFilter() throws Exception {
        Document doc = testCallOgcFilter("32");

        Node root = doc.getFirstChild();
        assertEquals("ExceptionReport", root.getLocalName());
    }

    private Document testCallStrict(String value) throws Exception {
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
        return postAsDOM("wfs", xml);
    }

    private Document testCallOgcFilter(String value) throws Exception {
        final String typeName = PREFIX + ":Fylker";
        String xml = "<GetFeature service=\"WFS\" version=\"2.0.0\" xmlns:" + PREFIX + "=\"" + URI + "\""
                + " xmlns:ogc=\"http://www.opengis.net/ogc\" outputFormat=\"text/xml; subtype=gml/3.2\">"
                + "<Query typeNames=\""
                + typeName
                + "\">"
                + "<ogc:Filter>"
                + "<ogc:PropertyIsEqualTo>"
                + "<ogc:PropertyName>nummer</ogc:PropertyName>"
                + "<ogc:Literal>"
                + value
                + "</ogc:Literal>"
                + "</ogc:PropertyIsEqualTo>"
                + "</ogc:Filter>"
                + "</Query>"
                + "</GetFeature>";
        return postAsDOM("wfs", xml);
    }
}
