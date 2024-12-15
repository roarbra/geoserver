/* (c) 2024 Open Source Geospatial Foundation - all rights reserved
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

    @Override
    protected void setUpInternal(SystemTestData data) throws Exception {

        data.addVectorLayer(
                new QName(SystemTestData.DEFAULT_URI, "Fylker", SystemTestData.DEFAULT_PREFIX),
                Collections.emptyMap(),
                "fylke.properties",
                getClass(),
                getCatalog());
    }

    @Test
    public void testGetFeatureFilterValueReferenceLeadingZero() throws Exception {
        testCall("ValueReference", "03");
    }

    @Test
    public void testGetFeatureFilterValueReference() throws Exception {
        testCall("ValueReference", "32");
    }

    @Test
    public void testGetFeatureFilterPropertyNameLeadingZero() throws Exception {
        testCall("PropertyName", "03");
    }

    @Test
    public void testGetFeatureFilterPropertyName() throws Exception {
        testCall("PropertyName", "32");
    }

    private void testCall(String element, String value) throws Exception {
        final String typeName = "Fylker";

        String xml =
                "<GetFeature service=\"WFS\" version=\"2.0.0\" outputFormat=\"text/xml; subtype=gml/3.2\">"
                        + "<Query typeNames=\""
                        + typeName
                        + "\">"
                        + "<Filter>"
                        + "<PropertyIsEqualTo>"
                        + "<"
                        + element
                        + ">nummer</"
                        + element
                        + ">"
                        + "<Literal>"
                        + value
                        + "</Literal>"
                        + "</PropertyIsEqualTo>"
                        + "</Filter>"
                        + "</Query>"
                        + "</GetFeature>";
        Document doc = postAsDOM("wfs", xml);

        NodeList features = doc.getElementsByTagName("wfs:member");
        assertEquals(1, features.getLength());
    }
}
