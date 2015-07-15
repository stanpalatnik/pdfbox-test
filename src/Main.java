import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDXFA;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, COSVisitorException, ParserConfigurationException, SAXException {
        PDDocument pdfDoc = PDDocument.load("C:\\Users\\home\\Downloads\\contribution_card_cc_eng.pdf");
        PDDocumentCatalog catalog = pdfDoc.getDocumentCatalog();
        PDAcroForm form = catalog.getAcroForm();
        List<PDField> pdFields = form.getFields();
        form.setXFA(null);
        for( PDField field : pdFields) {
            field.setValue("test");
        }

        pdfDoc.save("C:\\Users\\home\\Downloads\\contribution_card_cc_eng_saved.pdf");
        pdfDoc.close();
    }
}
