package io.github.soumyajitsamanta.tools.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class CliUtils {
    public static final Reader getInputFileOrStdin(
            final String path) {
        if (path.equals("-")) {
            return new InputStreamReader(System.in);
        }
        try {
            final File input = new File(path);
            if (!input.exists()) {
                throw new FileNotFoundException(
                        "File does not exist for reading.");
            }
            return new FileReader(path);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(
                    "The input file provided does not exist. File name: "
                            + path,
                    e);
        }
    }

    public static final Writer getOutputFileOrStdout(final String path) {
        if (path.equals("-")) {
            return new OutputStreamWriter(System.out);
        } else {
            try {
                return new FileWriter(path);
            } catch (final IOException e) {
                throw new RuntimeException(
                        "Could not create writer for file. File name: " + path);
            }
        }
    }
}
