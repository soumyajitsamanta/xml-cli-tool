package io.github.soumyajitsamanta.tools.xml;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * @author serverpc
 *
 */
// TODO Correctly capture CTRL+c to close stream.
@Command(
        name = "xmltoolsj",
        subcommands = {
                XpathQueryCommand.class,
                PrintCommand.class
                },
        mixinStandardHelpOptions = true)
public class XmlToolsCli {
    public static void main(String[] args) {
        int execute = new CommandLine(XmlToolsCli.class)
                .execute(
//                        new String[] { "--help"}
                        new String[] {"p",  "--help"}
//                        new String[] { "xpq","--help"}
//                        new String[] { "xpq","/" }
//                        new String[] { "xpq","-if","pom.xml","/*/*" }
                        );
        System.exit(execute);
    }
}
