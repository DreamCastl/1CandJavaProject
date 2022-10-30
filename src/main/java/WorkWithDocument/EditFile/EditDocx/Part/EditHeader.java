package WorkWithDocument.EditFile.EditDocx.Part;

import WorkWithDocument.EditFile.EditDocx.EditDocument;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.*;

import java.io.*;

public class EditHeader extends EditDocument {
    private WordprocessingMLPackage wordMLPackage;
    private ObjectFactory factory;
    private Hdr header;


    public static void SetSubStrate(WordprocessingMLPackage finalTemplate, String substrateWay) {


        EditHeader editHeader = new EditHeader(finalTemplate,Context.getWmlObjectFactory());

        File file = new File(substrateWay);
        byte[] bytes = new byte[0];
        try {
            bytes = editHeader.convertImageToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            editHeader.addImageInline(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finalTemplate = editHeader.wordMLPackage;
    }

    private void addImageInline(byte[] bytes) throws Exception {

        BinaryPartAbstractImage imagePart
                = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        int docPrId = 1;
        int cNvPrId = 2;
        Inline inLine = imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false);

        if (header != null) {

            addInlineImageToHeader(inLine);
        }
    }
    private void addInlineImageToHeader(Inline inline) {
        // Now add the in-line image to a paragraph
        ObjectFactory factory2 = new ObjectFactory();
        P paragraph2 = factory2.createP();
        R run = factory.createR();
        paragraph2.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        header.getContent().add(paragraph2);
    }

    private byte[] convertImageToByteArray(File file)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long, it needs to be an int.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length -
                offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "
                    + file.getName());
        }
        is.close();
        return bytes;
    }
    public EditHeader(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
        this.wordMLPackage = wordMLPackage;
        this.factory = factory;
        HeaderPart headerPart = null;
        try {
            headerPart = new HeaderPart();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        headerPart.setPackage(wordMLPackage);

        headerPart.setJaxbElement(createHeader("Text"));

        try {
            Relationship relationship = wordMLPackage.getMainDocumentPart().addTargetPart(headerPart);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


        this.header = header;
    }
    private Hdr createHeader(String content) {
        header = factory.createHdr();
        P paragraph = factory.createP();
        R run = factory.createR();
        Text text = new Text();
        text.setValue(content);
        run.getContent().add(text);
        paragraph.getContent().add(run);
        header.getContent().add(paragraph);
        return header;
    }
}
