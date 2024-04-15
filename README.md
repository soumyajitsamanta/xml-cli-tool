# xml-cli-tool
XML cli tool created in Java with minimum dependency.

This is has these features added till now:

- Use XPath to query the contents of a document.
- Pretty print a document or print in one line.

The features and their capability is not yet done
to the capacity required for it to have all
configurations in the process. Do not use in
production or any place.

# TODOS

- Add tests for scenarios related to the xml operations and also the file input and output set in cli utils
- Improve the assembly plugin to include the lib folder with the jar so the zip can be unzipped and executed with no other dependencies
- Use graalvm to create executable native image
- Add the capability of xml stylesheet trasformations