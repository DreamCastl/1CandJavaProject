package WorkWithDocument.EditFile.EditDocx.Part;
import WorkWithDocument.EditFile.EditDocx.EditDocument;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.wml.Text;

import java.util.List;

public class EditHeaderFooter extends EditDocument {

    public static void replaceHeaderFooter(WordprocessingMLPackage finalTemplate, String s2, String s, boolean variablePrinting) {

        finalTemplate.getParts().getParts().forEach((partName, part) -> {
            if (part.getClass().equals(FooterPart.class)) {
                for (Object obj : ((FooterPart) part).getContent()) {
                    List<Object> texts = getAllElementFromObject(obj, Text.class);
                    ReplaceVariable(texts,s2,s,variablePrinting);;
                }
            }
        });

    }
}

