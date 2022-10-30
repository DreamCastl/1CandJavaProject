package WorkWithDocument;

import Util.ConverterFormat;
import WorkWithDocument.EditFile.EditDocx.EditDocument;
import WorkWithDocument.EditFile.EditDocx.Part.*;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.*;

import java.io.*;
import java.util.Map;

public class SubstituteInTemplate {


    public String SubstituteInTemplateWord(ParamDocument param ) {

        String fileName = ConverterFormat.ConvertToNeedFormat(param.getFileName(), "docx");
        WordprocessingMLPackage template = null;
        try {
            template = getTemplate(fileName);
        } catch (Docx4JException | FileNotFoundException e) {
            e.printStackTrace();
        }
        WordprocessingMLPackage finalTemplate = template;

        EditDocument.NormalizeText(template);

        param.getHeaders().forEach((s, s2) -> EditHolder.replacePlaceholder(finalTemplate, s2, s, param.isVariablePrinting()));
        param.getHeaders().forEach((s, s2) -> EditBookmarks.ReplaceBookMark(finalTemplate, s2, s));
        param.getTable().forEach((s, s2) -> EditTable.ReplaceBookMark(finalTemplate, s2, s));
        param.getHeaders().forEach((s, s2) -> EditHeaderFooter.replaceHeaderFooter(finalTemplate, s2, s, param.isVariablePrinting()));


        if (param.isSubstrate()) {

            try {
                template = BackgroundImage.addBackground(finalTemplate, param.getSubstrateWay());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (param.isAddQR())
        {
            for (Map<String,String> val:param.getParamQR()) {

                try {
                    AddQR.addImageToPackage(finalTemplate,val.get("way"));
                break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        EditDocument.Clear(finalTemplate);
        try {

            fileName = param.getFileName().substring(0, param.getFileName().lastIndexOf('.'));
           // fileName = "D:\\test";
            File f = new File(fileName+"1"+ param.getFileExtension());
           // File f = new File(fileName+"1"+ ".docx");

            template.save(f);

            return f.getAbsolutePath();
            //    substituteInTemplate.writeDocxToStream(finalTemplate,"D:\\v8_5D73_8.docx");
        } catch (Docx4JException e) {
            e.printStackTrace();
        }

        return "";
    }

    private WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
    }


    private void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    private static void addImageToPackage(WordprocessingMLPackage wordMLPackage,
                                          byte[] bytes) throws Exception {
        BinaryPartAbstractImage imagePart =
                BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        int docPrId = 1;
        int cNvPrId = 2;
        Inline inline = imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false);

        P paragraph = addInlineImageToParagraph(inline);

        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    /**
     * We create an object factory and use it to create a paragraph and a run.
     * Then we add the run to the paragraph. Next we create a drawing and
     * add it to the run. Finally we add the inline object to the drawing and
     * return the paragraph.
     *
     * @param inline The inline object containing the image.
     * @return the paragraph containing the image
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

    /**
     * Convert the image from the file into an array of bytes.
     *
     * @param file the image file to be converted
     * @return the byte array containing the bytes from the image
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] convertImageToByteArray(File file)
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
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
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
}
