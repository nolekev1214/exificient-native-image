package org.example;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.grammars.Grammars;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.grammars.GrammarFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExiProcessor {
    EXIFactory exiFactory;

    public ExiProcessor() throws EXIException {
        exiFactory = DefaultEXIFactory.newInstance();
        Grammars grammars = GrammarFactory
                .newInstance()
                .createGrammars("./schemas/UCI_MessageDefinitions_v2_5_0.xsd");
        exiFactory.setGrammars(grammars);
    }

    public ByteArrayOutputStream encode(ByteArrayInputStream xml) throws EXIException, IOException, SAXException {
        ByteArrayOutputStream exiOut = new ByteArrayOutputStream();
        EXIResult exiResult = new EXIResult(exiFactory);
        exiResult.setOutputStream(exiOut);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(exiResult.getHandler());
        xmlReader.parse(new InputSource(xml));
        exiOut.close();

        return exiOut;
    }

    public ByteArrayOutputStream decode(ByteArrayInputStream exi) throws EXIException, TransformerException {
        ByteArrayOutputStream xmlOut = new ByteArrayOutputStream();
        Result result = new StreamResult(xmlOut);
        SAXSource exiSource = new EXISource(exiFactory);
        InputSource is = new InputSource(exi);
        exiSource.setInputSource(is);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(exiSource, result);

        return xmlOut;
    }
}
