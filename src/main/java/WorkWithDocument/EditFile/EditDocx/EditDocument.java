package WorkWithDocument.EditFile.EditDocx;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.jvnet.jaxb2_commons.ppp.Child;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

public class EditDocument {


    public static void NormalizeText(WordprocessingMLPackage template) {

        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Normalize(texts);
        template.getParts().getParts().forEach((partName, part) -> {
            if (part.getClass().equals(FooterPart.class)) {
                NormalizeFooterPart(part);
            }
        });
    }

    private static void NormalizeFooterPart(Part part) {
        for (Object obj : ((FooterPart) part).getContent()) {
            List<Object> texts = getAllElementFromObject(obj, Text.class);
            Normalize(texts);
        }
    }

    protected static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    protected static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }

    public static List<String> Helper(List<Object> texts) {
        List<String> rez = new ArrayList<>();
        for (Object text : texts) {
            Text textElement = (Text) text;
            String currentText = textElement.getValue();
            rez.add(currentText);
        }
        return rez;
    }

    public static void Clear(WordprocessingMLPackage finalTemplate) {
        List<Object> texts = getAllElementFromObject(finalTemplate.getMainDocumentPart(), Text.class);
        for (Object text : texts) {
            Text textElement = (Text) text;
            //   currentText = currentText + textElement.getValue();
            String textElValue = textElement.getValue();
            if (textElValue.contains("Evaluation Only. Created with Aspose.Words. Copyright 2003-2020 Aspose Pty Ltd.")) {
                String repText = textElement.getValue();
                repText = repText.replace("Evaluation Only. Created with Aspose.Words. Copyright 2003-2020 Aspose Pty Ltd.", "");
                textElement.setValue(repText);
            }
            if (textElValue.contains("Created with an evaluatio")) {
                String repText = textElement.getValue();
                repText = repText.replace("Created with an evaluation copy of Aspose.Words. To discover the full versions of our APIs please visit: https://products.aspose.com/words/", "");
                textElement.setValue(repText);
            }
        }
    }

    private static void Normalize(List<Object> texts) {

        boolean firstTag = false;
        boolean endTag = false;
        List<Object> ArrayObjText = null;
        for (Object text : texts) {
            Text textElement = (Text) text;
            String currentText = textElement.getValue();
            ;
            // Если содержит тег открытия скобки, тогда
            if (currentText.contains("$$")) {
                boolean dividesByTwo = count(currentText, "$$") % 2 == 0;
                if (dividesByTwo) {
                    continue;
                    //  break;
                }
                if (!firstTag) {
                    for (int index = texts.indexOf(text) + 1; index < texts.size(); index++) {
                        Text currentElement = (Text) texts.get(index);
                        String StringCurrentElement = currentElement.getValue();
                        if (StringCurrentElement.contains("$$")) {
                            String SubStr = StringCurrentElement.substring(0, StringCurrentElement.indexOf("$$") + 2);
                            textElement.setValue(textElement.getValue() + SubStr);
                            currentElement.setValue(StringCurrentElement.substring(StringCurrentElement.indexOf("$$") + 2, StringCurrentElement.length()));
                            break;
                        } else {
                            textElement.setValue(textElement.getValue() + StringCurrentElement);
                            currentElement.setValue("");
                        }
                    }
                }
            }
        }
    }

    protected static void ReplaceVariable(List<Object> texts, String name, String placeholder, boolean variablePrinting) {
        String tagText = "";
        if (variablePrinting) {
            tagText = "DOCVARIABLE  " + placeholder;
        } else {
            tagText = "$$" + placeholder + "$$";
        }
        for (Object text : texts) {
            Text textElement = (Text) text;
            String textElValue = textElement.getValue();
            if (textElValue.equals(tagText)) {
                textElement.setValue(name);
            }
            if (textElValue.contains(tagText)) {

                if (variablePrinting) {
                    Child currentObj = (Child) ((Text) text).getParent();
                    P currentObj2 = (P) currentObj.getParent();
                    R run = new R();
                    Text text1 = new Text();
                    text1.setValue(name);
                    text1.setSpace(((Text) text).getSpace());
                    R r1 = (R) ((Text) text).getParent();
                    run.setRPr(r1.getRPr());
                    run.getContent().add(text1);
                    currentObj2.getContent().add(run);

                } else {
                    String repText = textElement.getValue();
                    repText = repText.replace(tagText, name);
                    textElement.setValue(repText);
                }
            }
        }
    }
}

