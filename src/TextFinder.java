
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class TextFinder extends PDFTextStripper {

    public Set<Float> yCoordLines = new HashSet<Float>();
    public static String seek;
    public static String[] seekA;

    public TextFinder()
            throws IOException {
        super.setSortByPosition(true);
    }

    public static void main(String[] args)
            throws Exception {
        PDDocument document = null;
        seekA = args[1].split(",");
        seek = args[1];
        try {
            File input = new File(args[0]);
            document = PDDocument.load(input);
            if (document.isEncrypted()) {
                document.decrypt("");
            }
            TextFinder printer = new TextFinder();
            List allPages = document.getDocumentCatalog().getAllPages();

            for (int i = 0; i < allPages.size(); i++) {
                PDPage page = (PDPage) allPages.get(i);
                PDStream contents = page.getContents();

                if (contents != null) {
                    printer.processStream(page, page.findResources(), page.getContents().getStream());
                }
            }
        } finally {
            if (document != null) {
                //System.out.println(wordList);
                document.close();
            }
        }
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        String tChar = text.getCharacter();
        if( !yCoordLines.contains(text.getY())) {
            System.out.println("String[" + text.getX() + ","
                    + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
                    + text.getXScale() + " height=" + text.getHeightDir() + " space="
                    + text.getWidthOfSpace() + " width="
                    + text.getWidthDirAdj() + "]" + text.getCharacter());
            yCoordLines.add(text.getYDirAdj());
        }
    }

    protected Double roundVal(Float yVal) {
        DecimalFormat rounded = new DecimalFormat("0.0'0'");
        Double yValDub = new Double(rounded.format(yVal));
        return yValDub;
    }
}