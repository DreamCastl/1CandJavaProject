package WorkWithDocument.EditFile.EditDocx.Part;

import WorkWithDocument.EditFile.EditDocx.EditDocument;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.Text;

import java.util.List;

public class EditBookmarks extends EditDocument {

    public static void ReplaceBookMark(WordprocessingMLPackage template, String name, String placeholder) {

        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(),org.docx4j.wml.CTBookmark .class);
        for (Object text : texts) {

            String textElement = ((CTBookmark) text).getName();

            if (textElement.equals(placeholder)){

                ((CTBookmark) text).setName(name);
                List<Object> partsTextBookmark = getAllElementFromObject(((CTBookmark) text).getParent(), Text.class);
                int f = 4;
                // TODO необходимо нормализация текста, некоторые символы - отваливаются.
                for (Object textElBookmark : partsTextBookmark) {
                    Text tempTextElement = (Text) textElBookmark;
                    if (tempTextElement.getValue().equals(placeholder)){
                        String repText = tempTextElement.getValue();
                        repText = repText.replace(placeholder,name);
                        tempTextElement.setValue(repText);
                    }
                }
            }
        }
    }
}
