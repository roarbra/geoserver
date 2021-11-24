/* (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.featurestemplating.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.data.test.SystemTestData;
import org.geoserver.featurestemplating.configuration.SupportedFormat;
import org.junit.Test;

public class GeoJSONGetComplexFeaturesResponseWFSTest extends TemplateComplexTestSupport {

    private static final String GEOJSON_MF_TEMPLATE = "NormalGeoJSONMappedFeature";
    private static final String GEOJSON_MF_PARAM = "&" + GEOJSON_MF_TEMPLATE + "=true";

    @Override
    public void onSetUp(SystemTestData testData) throws IOException {
        Catalog catalog = getCatalog();
        FeatureTypeInfo mappedFeature = catalog.getFeatureTypeByName("gsml", "MappedFeature");
        String templateMappedFeature = "MappedFeatureGeoJSON.json";
        setUpTemplate(
                "requestParam('" + GEOJSON_MF_TEMPLATE + "')='true'",
                SupportedFormat.GEOJSON,
                templateMappedFeature,
                GEOJSON_MF_TEMPLATE,
                ".json",
                "gsml",
                mappedFeature);
    }

    @Test
    public void testGeoJSONResponse() throws Exception {
        StringBuffer sb = new StringBuffer("wfs?request=GetFeature&version=2.0");
        sb.append("&TYPENAME=gsml:MappedFeature&outputFormat=");
        sb.append("application/json");
        sb.append(GEOJSON_MF_PARAM);
        JSONObject result = (JSONObject) getJson(sb.toString());
        JSONArray features = (JSONArray) result.get("features");
        assertEquals(4, features.size());
        for (int i = 0; i < features.size(); i++) {
            JSONObject feature = (JSONObject) features.get(i);
            checkMappedFeatureGeoJSON(feature);
        }
        checkAdditionalInfo(result);
    }

    @Test
    public void testGeoJSONQueryWithGET() throws Exception {
        StringBuilder sb =
                new StringBuilder("wfs?request=GetFeature&version=2.0")
                        .append("&TYPENAME=gsml:MappedFeature&outputFormat=")
                        .append("application/json")
                        .append(
                                "&cql_filter=features.gsml:GeologicUnit.description = 'Olivine basalt'");
        sb.append(GEOJSON_MF_PARAM);
        JSONObject result = (JSONObject) getJson(sb.toString());
        JSONArray features = (JSONArray) result.get("features");
        assertTrue(features.size() == 1);
        assertEquals(((JSONObject) features.get(0)).get("@id").toString(), "mf4");
        checkMappedFeatureGeoJSON(features.getJSONObject(0));
        checkAdditionalInfo(result);
    }

    @Test
    public void testGeoJSONQueryPointingToExpr() throws Exception {
        StringBuilder sb =
                new StringBuilder("wfs?request=GetFeature&version=2.0")
                        .append("&TYPENAME=gsml:MappedFeature&outputFormat=")
                        .append("application/json")
                        .append("&cql_filter= features.name = 'FeatureName: MURRADUC BASALT'");
        sb.append(GEOJSON_MF_PARAM);
        JSONObject result = (JSONObject) getJson(sb.toString());
        JSONArray features = (JSONArray) result.get("features");
        assertTrue(features.size() == 1);
        JSONObject feature = features.getJSONObject(0);
        assertEquals("FeatureName: MURRADUC BASALT", feature.getString("name"));
        checkMappedFeatureGeoJSON(feature);
        checkAdditionalInfo(result);
    }

    @Test
    public void testGeoJSONQueryWithPOST() throws Exception {
        StringBuilder xml =
                new StringBuilder("<wfs:GetFeature ")
                        .append(" service=\"WFS\" ")
                        .append(" outputFormat=\"application/json\" ")
                        .append(" version=\"1.0.0\" ")
                        .append(" xmlns:gsml=\"urn:cgi:xmlns:CGI:GeoSciML:2.0\" ")
                        .append(" xmlns:wfs=\"http://www.opengis.net/wfs\" ")
                        .append(" xmlns:ogc=\"http://www.opengis.net/ogc\" ")
                        .append(">")
                        .append(" <wfs:Query typeName=\"gsml:MappedFeature\">")
                        .append(" <ogc:Filter><ogc:PropertyIsEqualTo> ")
                        .append(
                                "<ogc:PropertyName>features.gsml:GeologicUnit.description</ogc:PropertyName>")
                        .append("<ogc:Literal>Olivine basalt</ogc:Literal>")
                        .append("</ogc:PropertyIsEqualTo></ogc:Filter></wfs:Query>")
                        .append("</wfs:GetFeature>");
        JSONObject result =
                (JSONObject) postJson("wfs?" + GEOJSON_MF_TEMPLATE + "=true", xml.toString());
        JSONArray features = (JSONArray) result.get("features");
        assertTrue(features.size() == 1);
        assertEquals(((JSONObject) features.get(0)).get("@id").toString(), "mf4");
        checkMappedFeatureGeoJSON(features.getJSONObject(0));
        checkAdditionalInfo(result);
    }
}
