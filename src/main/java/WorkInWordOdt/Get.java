package WorkInWordOdt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import java.io.*;
import java.util.*;

public class Get {

    public static String mainInt(String paramJSON) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map result = objectMapper.readValue(paramJSON, HashMap.class);
//
//        LinkedHashMap<String, String> subject = (LinkedHashMap<String, String>) result.get("Субъекты");
//        LinkedHashMap<String, Object> Area = (LinkedHashMap<String, Object>) result.get("ОписаниеОбластей");
//        LinkedHashMap<String, Object> Area2 = (LinkedHashMap<String, Object>) Area.get("СогласиеНаОбработкуПерсональныхДанных(MSWord)");
//
//        List<String> ListArea = new ArrayList<>();
//        Area2.forEach((s, s2) -> ListArea.add(s));
//        String fileName = (String) result.get("М.акет");
        String fileName = paramJSON;
        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file.getAbsolutePath());

        XWPFDocument document = new XWPFDocument(fis); // Вот и объект описанного нами класса

        fis.close();

//        for (String s : ListArea) {
//            XWPFParagraph toDelete = document.getParagraphs().stream()
//                    .filter(p -> p.getParagraphText().contains("{v8 Область." + s + "}"))
//                    .findFirst().orElse(null);
//            if (toDelete != null) {
//                document.removeBodyElement(document.getPosOfParagraph(toDelete));
//            }
//            toDelete = document.getParagraphs().stream()
//                    .filter(p -> p.getParagraphText().contains("{/v8 Область." + s + "}"))
//                    .findFirst().orElse(null);
//            if (toDelete != null) {
//                document.removeBodyElement(document.getPosOfParagraph(toDelete));
//            }
//        }

//        subject.forEach((s, s2) -> {
//            String s1 = "{v8 " + s + "}";
//            for (XWPFParagraph p : document.getParagraphs()) {
//                try {
//                    if (p.getParagraphText().contains(s1)) {
//                        List<XWPFRun> runs = p.getRuns();
//                        if (runs != null) {
//                            for (XWPFRun r : runs) {
//                                String text = r.getText(0);
//                                if (text != null && text.contains(s1)) {
//                                    text = text.replace(s1, s2);
//                                    r.setText(text, 0);
//                                }
//                            }
//                        }
//                        if (p.getParagraphText().contains(s1)) {
//                            for (XWPFRun r : runs) {
//                                String text = r.getText(0);
//                                if (text != null && text.contains(s)) {
//
//                                    int indexR = runs.indexOf(r);
//
//                                    if (text.contains("}")) {
//                                        int asddd = 4;
//                                    }
//
//
//                                    if ((text +
//                                            runs.get(runs.indexOf(r) + 1).getText(0)).equals(s1)
//                                    ) {
//                                        text = text.replace(s, s2);
//                                        r.setText(text, 0);
//                                        runs.get(runs.indexOf(r) + 1).setText("", 0);
//                                    } else if (indexR - 1 > 0) {
//                                        if ((runs.get(runs.indexOf(r) - 1).getText(0) +
//                                                text +
//                                                runs.get(runs.indexOf(r) + 1).getText(0)).equals(s1)
//                                        ) {
//                                            text = text.replace(s, s2);
//                                            r.setText(text, 0);
//                                            runs.get(runs.indexOf(r) - 1).setText("", 0);
//                                            runs.get(runs.indexOf(r) + 1).setText("", 0);
//                                        }
//                                    } else if (indexR - 2 > 0) {
//                                        if ((runs.get(runs.indexOf(r) - 2).getText(0) +
//                                                runs.get(runs.indexOf(r) - 1).getText(0) +
//                                                text +
//                                                runs.get(runs.indexOf(r) + 1).getText(0)).equals(s1)
//                                        ) {
//                                            text = text.replace(s, s2);
//                                            r.setText(text, 0);
//                                            runs.get(runs.indexOf(r) - 2).setText("", 0);
//                                            runs.get(runs.indexOf(r) - 1).setText("", 0);
//                                            runs.get(runs.indexOf(r) + 1).setText("", 0);
//                                        }
//                                    } else if (indexR - 3 > 0) {
//                                        if ((runs.get(runs.indexOf(r) - 3).getText(0) +
//                                                runs.get(runs.indexOf(r) - 2).getText(0) +
//                                                runs.get(runs.indexOf(r) - 1).getText(0) +
//                                                text +
//                                                runs.get(runs.indexOf(r) + 1).getText(0)).equals(s1)
//                                        ) {
//                                            text = text.replace(s, s2);
//                                            r.setText(text, 0);
//                                            runs.get(runs.indexOf(r) - 3).setText("", 0);
//                                            runs.get(runs.indexOf(r) - 2).setText("", 0);
//                                            runs.get(runs.indexOf(r) - 1).setText("", 0);
//                                            runs.get(runs.indexOf(r) + 1).setText("", 0);
//                                        }
//                                    } else if (indexR - 4 > 0) {
//                                        if ((runs.get(runs.indexOf(r) - 4).getText(0) +
//                                                runs.get(runs.indexOf(r) - 3).getText(0) +
//                                                runs.get(runs.indexOf(r) - 2).getText(0) +
//                                                runs.get(runs.indexOf(r) - 1).getText(0) +
//                                                text +
//                                                runs.get(runs.indexOf(r) + 1).getText(0)).equals(s1)
//                                        ) {
//                                            text = text.replace(s, s2);
//                                            r.setText(text, 0);
//                                            runs.get(runs.indexOf(r) - 4).setText("", 0);
//                                            runs.get(runs.indexOf(r) - 3).setText("", 0);
//                                            runs.get(runs.indexOf(r) - 2).setText("", 0);
//                                            runs.get(runs.indexOf(r) - 1).setText("", 0);
//                                            runs.get(runs.indexOf(r) + 1).setText("", 0);
//                                        }
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    int f = 4;
//                }
//            }
//        });
        OutputStream fos = new FileOutputStream(fileName);
        document.write(fos);
        fos.close();

        return fileName;
    }

    private static void replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range range = doc.getRange();
        for (int numSec = 0; numSec < range.numSections(); ++numSec) {
            Section sec = range.getSection(numSec);
            for (int numPara = 0; numPara < sec.numParagraphs(); numPara++) {
                Paragraph para = sec.getParagraph(numPara);
                for (int numCharRun = 0; numCharRun < para.numCharacterRuns(); numCharRun++) {
                    CharacterRun charRun = para.getCharacterRun(numCharRun);
                    String text = charRun.text();
                    if (text.contains(findText)) {
                        charRun.replaceText(findText, replaceText);
                    }
                }
            }
        }
        // return doc;
    }

    public static void main(String[] args) {

        try {
            String TesxJ = "{ \n" +
                    "\"Макет\": \"D:/мауке.docx\", \n" +
                    "\"ОписаниеОбластей\": { \n" +
                    "\"СогласиеНаОбработкуПерсональныхДанных(MSWord)\": { \n" +
                    "\"Заголовок\": { \n" +
                    "\"ИмяОбласти\": \"Заголовок\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "}, \n" +
                    "\"НомерДата\": { \n" +
                    "\"ИмяОбласти\": \"НомерДата\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "}, \n" +
                    "\"Преамбула\": { \n" +
                    "\"ИмяОбласти\": \"Преамбула\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "}, \n" +
                    "\"ОсновнойТекст\": { \n" +
                    "\"ИмяОбласти\": \"ОсновнойТекст\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "}, \n" +
                    "\"РеквизитыОператора\": { \n" +
                    "\"ИмяОбласти\": \"РеквизитыОператора\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "}, \n" +
                    "\"РеквизитыСубъекта\": { \n" +
                    "\"ИмяОбласти\": \"РеквизитыСубъекта\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "}, \n" +
                    "\"Подпись\": { \n" +
                    "\"ИмяОбласти\": \"Подпись\", \n" +
                    "\"ТипОбласти\": \"Общая\" \n" +
                    "} \n" +
                    "} \n" +
                    "}, \n" +
                    "\"Субъекты\": { \n" +
                    "\"ФИО\": \"<ФИО субъекта>\", \n" +
                    "\"Адрес\": \"<Адрес субъекта>\", \n" +
                    "\"ПаспортныеДанные\": \"<Паспортные данные субъекта>\", \n" +
                    "\"ДатаСогласия\": \"6 сентября 2022 г.\", \n" +
                    "\"Организация\": \"ООО \\\"Альфа-Лизинг\\\"\", \n" +
                    "\"АдресОрганизации\": \"<Адрес организации>\", \n" +
                    "\"ОтветственныйЗаОбработкуПерсональныхДанных\": \"<ФИО ответственного лица>\" \n" +
                    "} \n" +
                    "}";
            System.out.println(mainInt(TesxJ));

            //     System.out.println(result.entrySet());

        } catch (Exception e) {
            Class<? extends Exception> cls = e.getClass();
            System.out.println(cls.getCanonicalName());
        }
    }
}

