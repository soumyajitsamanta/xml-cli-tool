package io.github.soumyajitsamanta.tools.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author serverpc
 *
 */
public class XmlOperations {

    /**
     * Utility or convenience method, given a document removes whitespace around
     * tags which are extra, if fails to remove whitespace ignores failure and
     * converts to string the xml body and prints.
     *
     * @param node
     * @param isIndented
     * @param indentWidth
     * @param printDeclaration
     * @return
     * @throws TransformerException
     */
    public String printXml(final Node node,
            final int indentWidth, final boolean printDeclaration)
            throws TransformerException {
        try {
            removeWhitespace(node);
        } catch (final XPathExpressionException e1) {
            System.err.println("printXml failed. There was error removing the "
                    + "whitespace around tags. Printing with spaces.");
        }

        try {
            return toString(node, indentWidth, printDeclaration);
        } catch (final TransformerFactoryConfigurationError e) {
            System.err.println(
                    "printXml failed. Could not convert document to string.");
            throw e;
        } catch (final TransformerException e) {
            System.err.println(
                    "printXml failed. Could not convert document to string.");
            throw e;
        }
    }

    /**
     * Convenience wrapper around {@link #parseXml(InputStream)}.
     *
     * @param input
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Document readXmlDom(final String input)
            throws SAXException, IOException, ParserConfigurationException {
        return readXmlDom(new StringReader(input));
    }

    /**
     * Given input stream of xml text, parses using DomParser.
     *
     * @param input
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Document readXmlDom(final Reader input)
            throws SAXException, IOException, ParserConfigurationException {
        try {
            final Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new InputSource(input));
            document.normalize();
            return document;
        } catch (final SAXException e) {
            System.err.println("parseXml failed. SAX Parser exception.");
            throw e;
        } catch (final IOException e) {
            System.err.println("parseXml failed. Input read exception.");
            throw e;
        } catch (final ParserConfigurationException e) {
            System.err.println("parseXml failed. Parse configuration wrong.");
            throw e;
        }
    }

    /**
     * Reads XML with into a document using namespace aware document builder. Rest same as 
     * readXmlDom
     * 
     * @param input
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Document readXmlDomWithNamespace(final Reader input)
            throws SAXException, IOException, ParserConfigurationException {
        try {
            DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            final Document document = documentBuilderFactory
                    .newDocumentBuilder().parse(new InputSource(input));
            document.normalize();
            return document;
        } catch (final SAXException e) {
            System.err.println("parseXml failed. SAX Parser exception.");
            throw e;
        } catch (final IOException e) {
            System.err.println("parseXml failed. Input read exception.");
            throw e;
        } catch (final ParserConfigurationException e) {
            System.err.println("parseXml failed. Parse configuration wrong.");
            throw e;
        }
    }

    /**
     * Given a document and options for transformation, converts document to the
     * string representation using transformer.
     *
     * @param node
     * @param shouldIndent
     * @param indentWidth
     * @param printDeclaration
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    private String toString(final Node node,
            final int indentWidth, final boolean printDeclaration)
            throws TransformerFactoryConfigurationError, TransformerException {
        try {
            final Writer out = new StringWriter();
            final TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indentWidth);
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                    printDeclaration ? "no" : "yes");
            transformer.setOutputProperty(OutputKeys.INDENT,
                    indentWidth > 0 ? "yes" : "no");
            transformer.transform(new DOMSource(node), new StreamResult(out));
            return out.toString();
        } catch (final TransformerConfigurationException e) {
            System.err.println(
                    "getString failed. Transformer configuration failed to be "
                            + "created.");
            throw e;
        } catch (final TransformerException e) {
            System.err.println(
                    "getString failed. Transformer failed to transform.");
            throw e;
        }
    }

    /**
     * Given a document it tries to remove the whitespace around tags which are
     * not part of the text node.
     *
     * @param node
     * @throws XPathExpressionException
     */
    public void removeWhitespace(final Node node)
            throws XPathExpressionException {
        node.normalize();
        final XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            final NodeList nodeList =
                    (NodeList) xPath.evaluate("//text()[normalize-space()='']",
                            node, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); ++i) {
                final Node innerNode = nodeList.item(i);
                innerNode.getParentNode().removeChild(innerNode);
            }
        } catch (final DOMException e) {
            System.err.println(
                    "removeWhitespace failed. Failed to remove the child node from current node.");
            throw e;
        } catch (final XPathExpressionException e) {
            System.err.println(
                    "removeWhitespace failed. The XPath to remove spaces around tags could not be "
                            + "created.");
            throw e;
        }
    }

    /**
     * Given document and XPath expression, finds the elements matching the
     * XPath and then return them as list.
     *
     * @param xpathExpression
     * @return
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     */
    public List<Node> findElementsByXpath(final Node node,
            final String xpathExpression)
            throws ParserConfigurationException, XPathExpressionException {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            final XPathExpression expression = xPath.compile(xpathExpression);
            final NodeList evaluate = (NodeList) expression.evaluate(node,
                    XPathConstants.NODESET);
            return XmlDataOperations.toList(evaluate);
        } catch (final XPathExpressionException e) {
            System.err
                    .println("findElementByXpath failed. XPath is wrong. Given "
                            + "XPath: "
                            + xpathExpression);
            throw e;
        }
    }

    /**
     * Creates an empty document with all default settings.
     * DocumentBuilderFactory -> DocumentBuilder -> Document with no settings.
     *
     * @return
     * @throws ParserConfigurationException
     */
    public Document createDefaultDocument()
            throws ParserConfigurationException {
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        return document;
    }

    /**
     * Appends the external node to node in document, importing the external
     * nodes.
     *
     * @param toNode     Document, DocumentType or inner nodes, not null.
     * @param fromNode   Document, DocumentType or inner nodes, nullable.
     * @param isDeepCopy
     * @return The imported and inserted node. Null when no import and insert
     *         were done or inserting node null.
     */
    // TODO: Use overloaded function to simplify the usage.
    public Node appendChild(final Node toNode, final Node fromNode,
            boolean isDeepCopy) {
        Objects.requireNonNull(toNode, "toNode cannot be null");
        if (fromNode == null) {
            return null;
        }
        boolean isToNodeDoc = toNode instanceof Document;
        if (isToNodeDoc && toNode.hasChildNodes()) {
            throw new RuntimeException("toNode is Document or DocumentType. "
                    + "toNode already has root node. "
                    + "Document can have only one root node. "
                    + "Cannot add more root nodes.");
        }
        final Document toDocument;
        if (isToNodeDoc) {
            toDocument = (Document) toNode;
        } else {
            toDocument = toNode.getOwnerDocument();
        }
        final Document fromDocument;
        if (fromNode instanceof Document) {
            fromDocument = (Document) fromNode;
            return appendChild(toNode, fromDocument.getFirstChild(),
                    isDeepCopy);
        } else {
            fromDocument = fromNode.getOwnerDocument();
        }

        if (!toDocument.isSameNode(fromDocument)) {
            return toNode
                    .appendChild(toDocument.importNode(fromNode, isDeepCopy));
        }
        return toNode.appendChild(fromNode);
    }

    /**
     * Convenience method to the {@link #appendChild(Node, Node)} to insert
     * nodes.
     * 
     * @param toNode
     * @param fromNode
     * @param isDeepCopy
     */
    public void appendChild(final Node toNode, final List<Node> fromNode,
            boolean isDeepCopy) {
        fromNode.stream()
                .forEach(node -> appendChild(toNode, node, isDeepCopy));
    }

    public void transformWithXslt(final Reader xsltInput, final Node node,
            final Writer output,
            final int indentWidth, final boolean printDeclaration) {
        try {
            final TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indentWidth);
            transformerFactory
                    .setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,
                    "");
            transformerFactory
                    .setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final Transformer transformer = transformerFactory
                    .newTransformer(new StreamSource(xsltInput));
            transformer.setOutputProperty(OutputKeys.INDENT,
                    indentWidth > 0 ? "yes" : "no");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                    printDeclaration ? "no" : "yes");
            transformer.transform(new DOMSource(node),
                    new StreamResult(output));
            try {
                output.flush();
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (final TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (final TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (final TransformerException e) {
            e.printStackTrace();
        }
    }
}
