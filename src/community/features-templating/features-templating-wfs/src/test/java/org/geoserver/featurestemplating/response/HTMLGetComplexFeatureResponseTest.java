package org.geoserver.featurestemplating.response;

import java.io.IOException;
import org.geoserver.featurestemplating.configuration.SupportedFormat;
import org.geoserver.featurestemplating.configuration.TemplateIdentifier;
import org.junit.Test;
import org.w3c.dom.Document;

public class HTMLGetComplexFeatureResponseTest extends TemplateComplexTestSupport {

    @Test
    public void getMappedFeature() throws IOException {
        String requestParam = "HTMLGetMappedFeature";
        String condition = "requestParam('" + requestParam + "')='true'";
        setUpTemplate(
                condition,
                SupportedFormat.HTML,
                "HTMLMappedFeature.xhtml",
                requestParam,
                ".xhtml",
                "gsml",
                mappedFeature);
        Document doc =
                getAsDOM(
                        "wfs?request=GetFeature&version=1.1.0&typename=gsml:MappedFeature"
                                + "&outputFormat=text/html"
                                + "&"
                                + requestParam
                                + "=true");

        assertXpathCount(1, "//html/head/script", doc);
        assertXpathCount(1, "//html/head/style", doc);
        assertXpathCount(4, "//html/body/ul/li[./span = 'MappedFeature']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul[./li = 'mf1']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul[./li = 'mf2']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul[./li = 'mf3']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul[./li = 'mf4']", doc);

        assertXpathCount(1, "//html/body/ul/li/ul/li/ul[./li = 'GUNTHORPE FORMATION']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul/li/ul[./li = 'MERCIA MUDSTONE GROUP']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul/li/ul[./li = 'CLIFTON FORMATION']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul/li/ul[./li = 'MURRADUC BASALT']", doc);

        assertXpathCount(4, "//html/body/ul/li/ul/li[./span = 'Shape']", doc);

        assertXpathCount(4, "//html/body/ul/li/ul/li[./span = 'Specifications']", doc);
        assertXpathCount(4, "//html/body/ul/li/ul/li/ul/li[./span = 'Geologic Unit']", doc);
        assertXpathCount(4, "//html/body/ul/li/ul/li/ul/li/ul/li[./span = 'Purpose']", doc);
        assertXpathCount(4, "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = 'instance']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = 'New Group']", doc);
        assertXpathCount(1, "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = '-Xy']", doc);
        assertXpathCount(
                1, "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = 'Yaugher Volcanic Group']", doc);
        assertXpathCount(
                2,
                "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = 'Yaugher Volcanic Group 1']",
                doc);
        assertXpathCount(
                2,
                "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = 'Yaugher Volcanic Group 2']",
                doc);
        assertXpathCount(3, "//html/body/ul/li/ul/li/ul/li/ul/li/ul[./li = '-Py']", doc);
        assertXpathCount(
                6, "//html/body/ul/li/ul/li/ul/li/ul/li[./span = 'Composition Parts']", doc);
        assertXpathCount(6, "//html/body/ul/li/ul/li/ul/li/ul/li/ul/li[./span = 'Part']", doc);
        assertXpathCount(
                6, "//html/body/ul/li/ul/li/ul/li/ul/li/ul/li/ul/li[./span = 'Role']", doc);
        assertXpathCount(
                5,
                "//html/body/ul/li/ul/li/ul/li/ul/li/ul/li/ul/li/ul[./li = 'interbedded component']",
                doc);
        assertXpathCount(
                1,
                "//html/body/ul/li/ul/li/ul/li/ul/li/ul/li/ul/li/ul[./li = 'fictitious component']",
                doc);
    }

    @Override
    protected String getTemplateFileName() {
        return TemplateIdentifier.HTML.getFilename();
    }
}
