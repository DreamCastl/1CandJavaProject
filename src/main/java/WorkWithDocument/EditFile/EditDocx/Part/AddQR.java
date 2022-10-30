package WorkWithDocument.EditFile.EditDocx.Part;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;

import java.io.*;

public class AddQR {

    public static void addImageToPackage(WordprocessingMLPackage wordMLPackage,
                                         String PathQR) throws Exception {
        byte[] bytesImage = convertImageToByteArray(PathQR);
        BinaryPartAbstractImage imagePart =
                BinaryPartAbstractImage.createImagePart(wordMLPackage, bytesImage);

        int docPrId = 1;
        int cNvPrId = 2;
        Inline inline = imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false,5000);

        P paragraph = addInlineImageToParagraph(inline);

        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    /**
     *  We create an object factory and use it to create a paragraph and a run.
     *  Then we add the run to the paragraph. Next we create a drawing and
     *  add it to the run. Finally we add the inline object to the drawing and
     *  return the paragraph.
     *
     * @param   inline The inline object containing the image.
     * @return  the paragraph containing the image
     */
    private static P addInlineImageToParagraph(Inline inline) {
        // Now add the in-line image to a paragraph
        ObjectFactory factory = new ObjectFactory();
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return paragraph;
    }


    private static byte[] convertImageToByteArray(String PathQR)
            throws FileNotFoundException, IOException {

        File file = new File(PathQR);
        InputStream is = new FileInputStream(file );
        long length = file.length();
        // You cannot create an array using a long, it needs to be an int.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "
                    +file.getName());
        }
        is.close();
        return bytes;
    }
}
