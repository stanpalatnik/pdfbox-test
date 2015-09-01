import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextbox;
import org.apache.pdfbox.pdmodel.interactive.form.PDXFA;
import org.apache.pdfbox.util.TextPosition;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, COSVisitorException, ParserConfigurationException, SAXException {
        PDDocument pdfDoc = PDDocument.load(args[0]);
        System.out.println(pdfDoc.getNumberOfPages()); pdfDoc.getPageMap(); //
        pdfDoc.setAllSecurityToBeRemoved(true);
        PDDocumentCatalog catalog = pdfDoc.getDocumentCatalog();
        PDPage page = (PDPage)catalog.getAllPages().get(0);
        PDAcroForm form = catalog.getAcroForm();
        List<PDField> pdFields = form.getFields();

        PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page, true, false);
        for( PDField field : pdFields) {
            if(field instanceof PDTextbox) {
                field.setReadonly(true);
                field.setValue("");

                COSDictionary fieldDict = field.getDictionary();
                COSArray fieldAreaArray = (COSArray) fieldDict.getDictionaryObject(COSName.RECT);
                PDRectangle result = new PDRectangle(fieldAreaArray);
                printRect(contentStream, result);
                printString(contentStream, result);
                System.out.println(result);
            }
        }

        /*
        Float upperRightY = page.getMediaBox().getUpperRightY();
        if(upperRightY != 0) {
            contentStream.fillRect(164.5F, page.getMediaBox().getUpperRightY() - 241F - 23F, 365F, 23F);   //invert Y-coordinate and offset for writing UP
        }
        else {
            contentStream.fillRect(164.5F, 241F, 365F, 23F);
        } */

        contentStream.close();
        pdfDoc.save(args[0] + ".out.pdf");
        pdfDoc.close();
    }

    public static void printString(final PDPageContentStream contentStream, final PDRectangle rect) throws IOException {
        int fontSize = 16; // Or whatever font size you want.
        float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Hello World") / 1000 * fontSize;
        float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        //float rectHeight = rect.getLowerLe
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
        contentStream.moveTextPositionByAmount( rect.getLowerLeftX() +1 , rect.getLowerLeftY() +1 );
        contentStream.moveTextPositionByAmount((rect.getWidth() - titleWidth) / 2, rect.getHeight() - titleHeight);
        contentStream.drawString( "Hello World" );
        contentStream.endText();
    }

    public static void printRect(final PDPageContentStream contentStream, final PDRectangle rect) throws IOException {
        contentStream.setStrokingColor(Color.YELLOW);
        contentStream.drawLine(rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getLowerLeftX(), rect.getUpperRightY()); // left
        contentStream.drawLine(rect.getLowerLeftX(), rect.getUpperRightY(), rect.getUpperRightX(), rect.getUpperRightY()); // top
        contentStream.drawLine(rect.getUpperRightX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getUpperRightY()); // right
        contentStream.drawLine(rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getLowerLeftY()); // bottom
        contentStream.setStrokingColor(Color.BLACK);
    }

}
