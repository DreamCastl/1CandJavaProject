package WorkWithDocument.EditFile.EditDocx.Part;

import javax.xml.bind.JAXBException;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.SectPr;

public class WatermarkUtils {

   private static Logger logger = LoggerFactory.getLogger(WatermarkUtils.class);
   private static ObjectFactory objectFactory = new ObjectFactory();
   /***********************************************************************
    * Adds the water mark on document.
    *
    * @param wmlPackage the wml package
    * @param way the background txt
    * @throws JAXBException the JAXB exception
    * @throws Docx4JException
    **********************************************************************/
   public static WordprocessingMLPackage addWaterMarkOnDocument(final WordprocessingMLPackage wmlPackage, final String way)
                     throws JAXBException, Docx4JException {

      MainDocumentPart mainDocumentPart = wmlPackage.getMainDocumentPart();

      Relationship relationship = null;
      try {
         relationship = createHeaderPart(wmlPackage,way);
      } catch (Exception e) {
         e.printStackTrace();
      }

      createHeaderReference(wmlPackage, relationship);

      return wmlPackage;
   }

   private static Relationship createHeaderPart(
           WordprocessingMLPackage wordprocessingMLPackage, String way)
           throws Exception {

      HeaderPart headerPart = new HeaderPart();
      headerPart.setPackage(wordprocessingMLPackage);
      headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, headerPart,way));
      return wordprocessingMLPackage.getMainDocumentPart()
              .addTargetPart(headerPart);

   }

   private static Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage,
                             Part sourcePart, String way) throws Exception {

      Hdr hdr = objectFactory.createHdr();
      hdr.getEGBlockLevelElts().add(
              newImage(wordprocessingMLPackage,
                      sourcePart, getBytes(way), "filename", "alttext", 1, 2
              )
      );
      return hdr;

   }

   private static byte[] getBytes(String way) throws Exception {

      File file = new File(way );

      java.io.InputStream is = new java.io.FileInputStream(file );
      long length = file.length();
      // You cannot create an array using a long type.
      // It needs to be an int type.
      if (length > Integer.MAX_VALUE) {
         System.out.println("File too large!!");
      }
      byte[] bytes = new byte[(int)length];
      int offset = 0;
      int numRead = 0;
      while (offset < bytes.length
              && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
         offset += numRead;
      }
      // Ensure all the bytes have been read in
      if (offset < bytes.length) {
         System.out.println("Could not completely read file "+file.getName());
      }
      is.close();

      return bytes;

   }


   private static org.docx4j.wml.P newImage( WordprocessingMLPackage wordMLPackage,
                                             Part sourcePart,
                                             byte[] image,
                                             String filenameHint, String altText,
                                             int id1, int id2) throws Exception {
     // File file = new File(way );
//      BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage,
//              sourcePart, file);
      BinaryPartAbstractImage imagePart =  BinaryPartAbstractImage.createImagePart(wordMLPackage, image);
      Inline inline = imagePart.createImageInline( filenameHint, altText,
              id1, id2);

      // Now add the inline in w:p/w:r/w:drawing
      org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();
      org.docx4j.wml.P  p = factory.createP();
      org.docx4j.wml.R  run = factory.createR();
      p.getParagraphContent().add(run);
      org.docx4j.wml.Drawing drawing = factory.createDrawing();
      run.getRunContent().add(drawing);
      drawing.getAnchorOrInline().add(inline);

      return p;

   }


   private static void createHeaderReference(
           WordprocessingMLPackage wordprocessingMLPackage,
           Relationship relationship )
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



