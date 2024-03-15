package io.github.soumyajitsamanta.tools.xml;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.Callable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * @author serverpc
 *
 */
// TODO: Add parameters for whether indented or not?
// TODO: Add parameters for indent depth, deep copy or not
@Command(
        name = "xpath-query",
        aliases = { "xpq" },
        description = { "", "Perform query on the xml using XPath. "
                + "XPath syntax: /=root, *=any sub element,...", "" }

)
public class XpathQueryCommand implements Callable<Integer> {
    @Option(
            names = { "-if", "--infile" },
            required = true,
            defaultValue = "-",
            description = "The input file path of xml. "
                    + "Empty or - for standard in, use CTRL+D to stop input.")
    String inputFilePath;
    @Option(
            names = { "-of", "--outfile" },
            required = true,
            defaultValue = "-",
            description = "The output file path of xml. Empty or - for standard in.")
    String outputFilePath;

    @Parameters(
            description = "The xpath expression to use for querying the xml.")
    String xpathExpression;

    @Override
    public Integer call() throws Exception {
        final Reader r = CliUtils.getInputFileOrStdin(inputFilePath);

        final XmlOperations op = new XmlOperations();
        final Document document = op.readXmlDom(r);
        final List<Node> list =
                op.findElementsByXpath(document, xpathExpression);
        final Document outputDocument = op.createDefaultDocument();
        if (list.size() == 1) {
            op.appendChild(outputDocument, list.get(0), false);
        } else {
            final Element rootElement = outputDocument.createElement("result");
            op.appendChild(outputDocument, rootElement, true);
            op.appendChild(rootElement, list, false);
        }

        final String printXml = op.printXml(outputDocument, true, 2, false);

        final Writer writer = CliUtils.getOutputFileOrStdout(outputFilePath);
        writer.write(printXml);
        return 0;
    }

}
