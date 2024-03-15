package io.github.soumyajitsamanta.tools.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDataOperations {

    public static List<Node> toList(NodeList input) {
        int length = input.getLength();
        List<Node> nodes = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            nodes.add(input.item(i));
        }
        return nodes;
    }
}
