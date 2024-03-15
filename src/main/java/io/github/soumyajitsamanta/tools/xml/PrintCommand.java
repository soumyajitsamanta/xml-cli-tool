package io.github.soumyajitsamanta.tools.xml;

import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Callable;

import org.w3c.dom.Document;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "print",
        aliases = { "p" },
        description = { "", "Prints the xml in one line or indented. "
                + "Indentation is by stripping the empty spaces around tags, not inside",
                "" })
//TODO: Add parameters for whether indented or not?
//TODO: Add parameters for indent depth, deep copy or not
public class PrintCommand implements Callable<Integer> {

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

    @Override
    public Integer call() throws Exception {
        Reader reader = CliUtils.getInputFileOrStdin(inputFilePath);
        XmlOperations op = new XmlOperations();
        Document document = op.readXmlDom(reader);
        String printXml = op.printXml(document, true, 2, false);
        Writer writer = CliUtils.getOutputFileOrStdout(outputFilePath);
        writer.write(printXml);
        return 0;
    }

}
