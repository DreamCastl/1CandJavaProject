package Util;

import com.aspose.words.Document;
import org.simplejavamail.converter.EmailConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class ConverterFormat {


    public static String ConvertToNeedFormat (String fileName, String toExtension){

        Optional<String> fileExtension = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
        if (!fileExtension.get().equals(toExtension)) {
            Document doc = null;
            try {
                String newFileName = fileName.replace(fileExtension.get(),toExtension);
                doc = new Document(fileName);
                doc.save(newFileName);
                return newFileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    public static String ConvertToNeedFormat(String fileWay, String extension, String recipient) {

        if (extension.equals("eml")) {
            File f = new File(fileWay);
            String textEML = EmailConverter.outlookMsgToEML(f);
            FileWriter nFile = null;
            try {
                nFile = new FileWriter(recipient);
                nFile.write(textEML);
                nFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileWay;
        } else {
            return recipient;
        }

    }
}
