package WorkWithDocument.EditFile.EditDocx.Part;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Watermark {
    private ObjectFactory objectFactory = new ObjectFactory();

    public WordprocessingMLPackage addWatermark(WordprocessingMLPackage wordMLPackage, String file ) throws Exception{

        byte[] image = Files.readAllBytes(Paths.get(file));

        Relationship relationship = createHeaderPart(wordMLPackage, image);

        createHeaderReference(wordMLPackage, relationship);

        return wordMLPackage;
    }

    private Relationship createHeaderPart(WordprocessingMLPackage wordprocessingMLPackage, byte[] image) throws Exception {

        HeaderPart headerPart = new HeaderPart();
        headerPart.setPackage(wordprocessingMLPackage);
        headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, headerPart, image));
        return wordprocessingMLPackage.getMainDocumentPart().addTargetPart(headerPart);

    }

    private Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage, Part sourcePart, byte[] image) throws Exception {

        Hdr hdr = objectFactory.createHdr();
        //.getEGBlockLevelElts()
        hdr.getContent().add(
                newImage(wordprocessingMLPackage, sourcePart, image, "filename", "alttext", 1, 2));
        return hdr;

    }

    private org.docx4j.wml.P newImage(WordprocessingMLPackage wordMLPackage, Part sourcePart, byte[] bytes,
                                     String filenameHint, String altText, int id1, int id2) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, sourcePart, bytes);

        Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, false);

        org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();

        org.docx4j.wml.P p = factory.createP();
        org.docx4j.wml.R run = factory.createR();
        p.getContent().add(run);

        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);

        drawing.getAnchorOrInline().add(inline);

        return p;
    }

    private void createHeaderReference(WordprocessingMLPackage wordprocessingMLPackage, Relationship relationship)
            throws InvalidFormatException {

        SectPr sectPr = objectFactory.createSectPr();


        HeaderReference headerReference = objectFactory.createHeaderReference();
        headerReference.setId(relationship.getId());
        headerReference.setType(HdrFtrRef.DEFAULT);

        sectPr.getEGHdrFtrReferences().add(headerReference);// add header or
        // footer references

        wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);

    }

}