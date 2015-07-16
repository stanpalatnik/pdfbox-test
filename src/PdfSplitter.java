import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.util.Splitter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;


public class PdfSplitter {

    public static void main(String[] args) throws IOException, COSVisitorException, ParserConfigurationException, SAXException {
        PDDocument pdfDoc = PDDocument.load(args[0]);
        pdfDoc.setAllSecurityToBeRemoved(true);

        Splitter docSplitter = new Splitter();
        docSplitter.setEndPage(1);
        List<PDDocument> pdDocumentList = docSplitter.split(pdfDoc);

        System.out.println("Number of pages in original document: " + pdfDoc.getNumberOfPages());
        System.out.println("Number of documents: " + pdDocumentList.size());

        PDDocument firstPageOnly = pdDocumentList.get(0);
        firstPageOnly.save(args[0] + ".first.out.pdf");
        firstPageOnly.close();
    }
}