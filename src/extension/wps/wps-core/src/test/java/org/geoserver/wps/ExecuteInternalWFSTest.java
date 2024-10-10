package org.geoserver.wps;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class ExecuteInternalWFSTest extends WPSTestSupport {

    private static final String WFS10_SchemaURI = "http://www.opengis.net/wfs";

    private static final String WFS20_SchemaURI = "http://www.opengis.net/wfs/2.0.0";

    private String xml(String version) {

        String wfsReq;
        if ("1.0".equals(version)) {
            wfsReq =
                    "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\">\n"
                            + "     <wfs:Query typeName=\"cite:Lakes\">\n"
                            + "     </wfs:Query>\n"
                            + "</wfs:GetFeature>\n";
        } else {
            wfsReq =
                    "<wfs:GetFeature service=\"WFS\" version=\"2.0.0\" outputFormat=\"GML2\">\n"
                            + "     <wfs:Query typeNames=\"cite:Lakes\">\n"
                            + "     </wfs:Query>\n"
                            + "</wfs:GetFeature>\n";
        }

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xmlns:wps=\"http://www.opengis.net/wps/1.0.0\"  xmlns:wfs=\""
                + ("1.0".equals(version) ? WFS10_SchemaURI : WFS20_SchemaURI)
                + "\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:cite=\"http://www.opengis.net/cite\""
                + " xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                + "xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">\n"
                + "  <ows:Identifier>geo:area</ows:Identifier>\n"
                + "  <wps:DataInputs>\n"
                + "    <wps:Input>\n"
                + "      <ows:Identifier>geom</ows:Identifier>\n"
                + "      <wps:Reference mimeType=\"application/gml-3.1.1\" xlink:href=\"http://geoserver/wps\" method=\"POST\">\n"
                + "        <wps:Body>\n"
                + "          <wps:Execute version=\"1.0.0\" service=\"WPS\">\n"
                + "            <ows:Identifier>vec:CollectGeometries</ows:Identifier>\n"
                + "            <wps:DataInputs>\n"
                + "                <wps:Input>\n"
                + "                    <ows:Identifier>features</ows:Identifier>\n"
                + "                    <wps:Reference mimeType=\"text/xml\" xlink:href=\"http://geoserver/wfs\" method=\"POST\">\n"
                + "                        <wps:Body>\n"
                + wfsReq
                + "                        </wps:Body>\n"
                + "                    </wps:Reference>\n"
                + "                </wps:Input>\n"
                + "            </wps:DataInputs>\n"
                + "            <wps:ResponseForm>\n"
                + "                <wps:RawDataOutput mimeType=\"application/gml-3.1.1\">\n"
                + "                    <ows:Identifier>result</ows:Identifier>\n"
                + "                </wps:RawDataOutput>\n"
                + "            </wps:ResponseForm>\n"
                + "          </wps:Execute>\n"
                + "        </wps:Body>\n"
                + "      </wps:Reference>\n"
                + "    </wps:Input>\n"
                + "  </wps:DataInputs>\n"
                + "  <wps:ResponseForm>\n"
                + "    <wps:RawDataOutput>\n"
                + "      <ows:Identifier>result</ows:Identifier>\n"
                + "    </wps:RawDataOutput>\n"
                + "  </wps:ResponseForm>\n"
                + "</wps:Execute>\n";
    }

    @Test
    public void testVersion10() throws Exception {
        String xml = xml("1.0");
        MockHttpServletResponse response = postAsServletResponse(root(), xml);
        String area = response.getContentAsString();
        Assert.assertEquals("2.195E-6", area);
    }

    @Test
    public void testPostVersion20() throws Exception {
        String xml = xml("2.0");
        MockHttpServletResponse response = postAsServletResponse(root(), xml);
        String area = response.getContentAsString();
        Assert.assertEquals("2.195E-6", area);
    }
}
