package org.dax.api;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 *
 */
public interface DAXReader extends XMLEventReader {
    Stream<Element> elements(Predicate<StartElement> startElementPredicate) throws XMLStreamException;

    Stream<Element> elements(String xpath);

    Stream<Element> elements(Node node);

    Stream<Element> elements(Node node, String xpath);

    Stream<Node> nodes(Node node);
}
