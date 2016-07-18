package org.dax.api.impl;

import com.dax.exception.UncheckedXMLStreamException;
import org.dax.api.DAXReader;
import org.dax.api.Parsers;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 */
public class DAXReaderImpl implements DAXReader {
    private final XMLEventReader xmlEventReader;

    public DAXReaderImpl(XMLEventReader xmlEventReader) {
        Objects.requireNonNull(xmlEventReader, "The XML Events reader object is null.");
        this.xmlEventReader = xmlEventReader;
    }

    @Override
    public Stream<Element> elements(Predicate<StartElement> startElementPredicate) throws XMLStreamException {
        Objects.requireNonNull(startElementPredicate, "The start element predicate is null.");
        ElementsIterator elementsIterator = new ElementsIterator(startElementPredicate);

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                elementsIterator, Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    @Override
    public Stream<Element> elements(String xpath) {
        return null;
    }

    @Override
    public Stream<Element> elements(Node node) {
        return null;
    }

    @Override
    public Stream<Element> elements(Node node, String xpath) {
        return null;
    }

    @Override
    public Stream<Node> nodes(Node node) {
        return null;
    }

    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        return xmlEventReader.nextEvent();
    }

    @Override
    public boolean hasNext() {
        return xmlEventReader.hasNext();
    }

    @Override
    public Object next() {
        return xmlEventReader.next();
    }

    @Override
    public XMLEvent peek() throws XMLStreamException {
        return xmlEventReader.peek();
    }

    @Override
    public String getElementText() throws XMLStreamException {
        return xmlEventReader.getElementText();
    }

    @Override
    public XMLEvent nextTag() throws XMLStreamException {
        return xmlEventReader.nextTag();
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return xmlEventReader.getProperty(name);
    }

    @Override
    public void close() throws XMLStreamException {
        xmlEventReader.close();
    }

    private class ElementsIterator implements Iterator<Element> {
        private final Predicate<StartElement> startElementPredicate;
        private Element element;

        private ElementsIterator(Predicate<StartElement> startElementPredicate) {
            this.startElementPredicate = startElementPredicate;
        }

        @Override
        public boolean hasNext() {
            StringBuilder elementBuilder = new StringBuilder();
            String startElementName = null;
            boolean isStartElement = false;

            while (xmlEventReader.hasNext()) {
                try {
                    XMLEvent xmlEvent = xmlEventReader.nextEvent();

                    if (xmlEvent.isStartElement()) {
                        StartElement startElement = (StartElement) xmlEvent;

                        if (startElementPredicate.test(startElement)) {
                            startElementName = startElement.getName().getLocalPart();
                            isStartElement = true;
                        }

                    } else if (xmlEvent.isEndElement()) {
                        EndElement endElement = (EndElement) xmlEvent;

                        if (Objects.equals(endElement.getName().getLocalPart(), startElementName)) {
                            elementBuilder.append(endElement.toString());
                            element = Parsers.createElement(elementBuilder.toString());
                            return true;
                        }
                    }

                    if (isStartElement) {
                        elementBuilder.append(xmlEvent.toString());
                    }
                } catch (Exception e) {
                    throw new UncheckedXMLStreamException(e.getMessage(), e);
                }
            }

            return false;
        }

        @Override
        public Element next() {
            if (element == null) {
                throw new NoSuchElementException("Element not found. Use hasNext method first to check " +
                        "availability of next element.");
            }

            return element;
        }
    }
}
