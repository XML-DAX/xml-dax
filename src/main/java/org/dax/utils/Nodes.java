package org.dax.utils;

import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The utility class which provides utility methods to facilitate handling of XML nodes and elements.
 *
 * @author fkhan
 */
public final class Nodes {
    private Nodes() {
    }

    /**
     * Gets the stream for the given nodes. By default, the returned is stream will be sequential (not parallel) and the
     * characteristics of the returned stream will be {@code Spliterator.ORDERED | Spliterator.NONNULL |
     * Spliterator.SIZED | Spliterator.IMMUTABLE}. Use {@link #nodes(NodeList, int, boolean)}, if the parallel stream
     * with different characteristics is needed.
     *
     * @param nodeList the list of nodes.
     *
     * @return the stream for the given nodes.
     */
    public static Stream<Node> nodes(final NodeList nodeList) {
        return nodes(nodeList, Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.IMMUTABLE, false);
    }

    public static Stream<Node> nodes(final NodeList nodesList, final int characteristics, final boolean isParallel) {
        Objects.requireNonNull(nodesList, "The 'nodesList' object is null.");

        Iterator<Node> nodesIterator = new Iterator<Node>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < nodesList.getLength();
            }

            @Override
            public Node next() {
                return nodesList.item(currentIndex++);
            }
        };

        return StreamSupport.stream(Spliterators.spliterator(nodesIterator, nodesList.getLength(), characteristics), isParallel);
    }

    public static String coalescedText(Element parentElement, String childElementName) {
        return Nodes.nodes(parentElement.getElementsByTagName(childElementName))
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node)
                .map(Nodes::coalescedText)
                .findFirst().orElse("");
    }

    public static String coalescedText(Element element) {
        return nodes(element.getChildNodes())
                .filter(n -> n instanceof CharacterData || n instanceof Text)
                .map(n -> n.getTextContent())
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
