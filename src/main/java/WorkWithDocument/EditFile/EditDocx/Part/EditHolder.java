package WorkWithDocument.EditFile.EditDocx.Part;

import WorkWithDocument.EditFile.EditDocx.EditDocument;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.jvnet.jaxb2_commons.ppp.Child;

import javax.xml.bind.JAXBElement;
import java.util.List;

public class EditHolder extends EditDocument {

    public static void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder, boolean variablePrinting)  {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        ReplaceVariable(texts,name,placeholder,variablePrinting);

    }
}
