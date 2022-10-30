package WorkWithDocument.EditFile.EditDocx.Part;

import WorkWithDocument.EditFile.EditDocx.EditDocument;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.jvnet.jaxb2_commons.ppp.Child;

import javax.xml.bind.JAXBException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EditTable extends EditDocument {
    public static void ReplaceBookMark(WordprocessingMLPackage template, LinkedList<Map<String, String>> table, String tableName) {

        List<Object> listBookmark = getAllElementFromObject(template.getMainDocumentPart(), org.docx4j.wml.CTBookmark.class);

        for (Object exampleBookmark : listBookmark) {

            if (((CTBookmark) exampleBookmark).getName().equals(tableName)) {
                // Находим нужную закладку.
                // ищем родителя, нужного класса
                Child currentObj = (Child) ((Child) exampleBookmark);
                while (!currentObj.getClass().equals(Tbl.class)) {
                    currentObj = (Child) currentObj.getParent();
                }
                Tbl tempTable = (Tbl) currentObj;
                // List<Object> rows = getAllElementFromObject(tempTable, Tr.class);
                try {
                    replaceTable(tempTable, table, template);
                } catch (Docx4JException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
                int f = 4;

            }
        }

    }

    private static void replaceTable(Tbl tempTable, LinkedList<Map<String, String>> textToAdd,
                                     WordprocessingMLPackage template) throws Docx4JException, JAXBException {
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

        // 1. find the table
        List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 2) {
            // this is our template row
            Tr templateRow = (Tr) rows.get(1);

            for (Map<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements, 0);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        } else if (rows.size() == 1) {
            Tr templateRow = (Tr) rows.get(0);
            for (Map<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements, 0);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
        else if (rows.size() > 2)
        {
            for (int i = 0 ; i < rows.size()-1; i ++)
            {
                Tr templateRow = (Tr) rows.get(i);
                List<String> texts = Helper(getAllElementFromObject(templateRow,Text.class));
                boolean findMatch = false;

                for (Map<String, String> replacements : textToAdd) {

                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        String s = entry.getKey();
                        String s2 = entry.getValue();
                        for (String txt : texts) {
                            if (txt.contains("$$" + s + "$$")
                            ) {
                                findMatch = true;
                                break;
                            }
                        }
                        if (findMatch) break;
                    }
                }
                if (findMatch)
                {
                    boolean addNumber = false;
                    List<String> textsTmp = Helper(getAllElementFromObject(templateRow,Text.class));
                    for (String t : textsTmp
                         ) {
                        if (t.contains("$$НомерСтроки$$"))
                        {
                            addNumber = true;
                            break;
                        }

                    }

                    int numb = 1;
                    for (Map<String, String> replacements : textToAdd) {
                       if ( addNumber) {
                           replacements.put("$$НомерСтроки$$", String.valueOf(numb));
                           numb++;
                       }

                        addRowToTable(tempTable, templateRow, replacements, i);
                        // 4. remove the template row
                        tempTable.getContent().remove(templateRow);
                    }
                    break;
                }

                }
        }
    }

    protected static Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
        for (Iterator<Object> iterator = tables.iterator(); iterator.hasNext(); ) {
            Object tbl = iterator.next();
            List<?> textElements = getAllElementFromObject(tbl, Text.class);
            for (Object text : textElements) {
                Text textElement = (Text) text;
                if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
                    return (Tbl) tbl;
            }
        }
        return null;
    }

    private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements, int index ) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List textElements = getAllElementFromObject(workingRow, Text.class);
       // TODO нормализация
        for (Object object : textElements) {
            Text text = (Text) object;

            String replacementValue = (String) replacements.get(text.getValue());
            if (replacementValue == null && text.getValue().contains("$$")) {
                replacementValue = (String) replacements.get(text.getValue().replace("$$",""));
            }
            if (replacementValue != null){
                text.setValue(replacementValue);
            }
        }
        if (index == 0) {
        reviewtable.getContent().add(workingRow);
        } else {
            reviewtable.getContent().add(index,workingRow);
        }
    }
}
