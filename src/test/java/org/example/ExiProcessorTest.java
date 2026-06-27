package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ExiProcessorTest {

    private ExiProcessor processor;

    @BeforeEach
    void setUp() throws Exception {
        processor = new ExiProcessor();
    }

    @Test
    void encodeXmlToExi() throws Exception {
        byte[] xmlBytes = loadResource("/PositionReport.xml");

        ByteArrayOutputStream exiOut = processor.encode(new ByteArrayInputStream(xmlBytes));

        assertNotNull(exiOut);
        assertTrue(exiOut.size() > 0, "EXI output should be non-empty");
        assertNotEquals(xmlBytes.length, exiOut.size(), "EXI output should differ in size from XML input");
    }

    @Test
    void decodeExiToXml() throws Exception {
        byte[] xmlBytes = loadResource("/PositionReport.xml");
        ByteArrayOutputStream exiOut = processor.encode(new ByteArrayInputStream(xmlBytes));
        ByteArrayOutputStream xmlOut = processor.decode(new ByteArrayInputStream(exiOut.toByteArray()));

        assertNotNull(xmlOut);
        assertTrue(xmlOut.size() > 0, "Round-tripped XML output should be non-empty");
        String xmlString = xmlOut.toString();
        assertTrue(xmlString.contains("PositionReport"), "Output should contain PositionReport element");
    }

    private byte[] loadResource(String path) throws Exception {
        try (var stream = getClass().getResourceAsStream(path)) {
            assertNotNull(stream, "Test resource not found: " + path);
            return stream.readAllBytes();
        }
    }
}
