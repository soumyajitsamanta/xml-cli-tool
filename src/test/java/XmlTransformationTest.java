import java.io.FileReader;
import java.io.FileWriter;

import org.junit.jupiter.api.Test;

import io.github.soumyajitsamanta.tools.xml.XmlOperations;

public class XmlTransformationTest {
    XmlOperations operations = new XmlOperations();

    @Test
    void testName() throws Exception {
        String xslt = "/home/serverpc/Project/Checkout/xsdtohtml/xs3p.xsl";
        String sourceXsdFile =
                "/home/serverpc/Project/DatabaseFiles/XML/maven-4.0.0.xsd";
        String destinationHtmlFile =
                "/home/serverpc/Project/DatabaseFiles/XML/maven-4.0.0.xsd.test.html";
        operations.transformWithXslt(new FileReader(xslt), operations.readXmlDom(new FileReader(sourceXsdFile))
                , new FileWriter(destinationHtmlFile), 2, true);
    }
}
