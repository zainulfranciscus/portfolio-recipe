package com.safe.stack.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TikeTest {

    public static void main(String[] args) throws IOException, SAXException, TikaException {
	TikeTest t = new TikeTest();
	 
	InputStream gifImageIS = t.getClass().getClassLoader().getResourceAsStream("gif_test.gif");
	InputStream docxIS = t.getClass().getClassLoader().getResourceAsStream("doc_test.docx");
	InputStream pngIS = t.getClass().getClassLoader().getResourceAsStream("png_test.png");
	InputStream docIS = t.getClass().getClassLoader().getResourceAsStream("test_doc.doc");
	InputStream pdfIS = t.getClass().getClassLoader().getResourceAsStream("test_pdf.pdf");
	
	
	parse(gifImageIS,"gif");
	parse(docxIS, "docx");
	parse(pngIS, "png");
	parse(docIS, "doc");
	parse(pdfIS, "pdf");
		
	
    }
    
    private static void parse(InputStream is, String docType) throws IOException, SAXException, TikaException
    {
	System.out.println("Parsing doc type: " + docType);
	Tika tika = new Tika();
	String mimeType = tika.detect(is);

	Metadata metadata = new Metadata();
	metadata.set(Metadata.CONTENT_TYPE, mimeType);

	BodyContentHandler handler = new BodyContentHandler(10000000);
	Parser parser = new AutoDetectParser();
	parser.parse(is, handler, metadata, new ParseContext());

	for (String name : metadata.names()) {
	    System.out.println(name + ":\t" + metadata.get(name));
	}
    }
    

}
