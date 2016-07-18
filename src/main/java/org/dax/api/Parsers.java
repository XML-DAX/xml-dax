package org.dax.api;

import org.dax.api.impl.DAXReaderImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by fkhan on 7/16/16.
 */
public final class Parsers {
    private Parsers() {
    }

    public static DAXReader createDAXParser(Path filePath) throws FileNotFoundException, XMLStreamException {
        Objects.requireNonNull(filePath, "File path is null.");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Reader fileReader = new FileReader(filePath.toFile());
        return new DAXReaderImpl(factory.createXMLEventReader(fileReader));
    }

    public static DAXReader createDAXParser(InputStream inputStream) throws FileNotFoundException, XMLStreamException {
        return createDAXParser(inputStream, StandardCharsets.UTF_8);
    }

    public static DAXReader createDAXParser(InputStream inputStream, Charset charset) throws FileNotFoundException, XMLStreamException {
        Objects.requireNonNull(inputStream, "Input stream is null.");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Charset nonNullCharset = charset == null ? StandardCharsets.UTF_8 : charset;
        return new DAXReaderImpl(factory.createXMLEventReader(inputStream, nonNullCharset.toString()));
    }

    public static Element createElement(String elementAsString) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(elementAsString));
        Document document = documentBuilder.parse(inputSource);
        return (Element) document.getFirstChild();
    }

}
