package org.dax.api.impl;

import org.dax.api.DAXReader;
import org.dax.api.Parsers;
import org.dax.utils.Nodes;
import org.junit.Test;
import org.w3c.dom.Element;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class DAXReaderImplTest {
    @Test
    public void testEvents() throws Exception {
        URL xmlResource = ClassLoader.getSystemClassLoader().getResource("bookstore-example.xml");
        assertNotNull(xmlResource);

        Path xmlFilePath = Paths.get(xmlResource.toURI());
        DAXReader daxParser = Parsers.createDAXParser(xmlFilePath);

        daxParser.elements(s -> s.getName().getLocalPart().equals("book")).map(this::convert).forEach(System.out::println);
    }

    public Book convert(Element bookElement) {
        List<String> authors = Nodes.nodes(bookElement.getElementsByTagName("author")).map(n -> n.getTextContent()).collect(Collectors.toList());
        String title = Nodes.coalescedText(bookElement, "title");
        String year = Nodes.nodes(bookElement.getElementsByTagName("year")).map(n -> n.getTextContent()).findFirst().get();
        String price = Nodes.nodes(bookElement.getElementsByTagName("price")).map(n -> n.getTextContent()).findFirst().get();

        return new Book(title, authors, year, price);
    }

    private static class Book {
        private String title;
        private List<String> author;
        private String year;
        private String price;

        public Book(String title, List<String> author, String year, String price) {
            this.title = title;
            this.author = author;
            this.year = year;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Book{'title='" + title + "', author=" + author + ", year='" + year + '\'' + ", price='" + price + '\'' + '}';
        }
    }
}
