package factorial;

import java.io.IOException;

public class Get {
    private native void log(String paramString);

    public static String mainInt(int number,String JSON)  {
       // return JSON;
        try {
            return WorkInWordOdt.Get.mainInt(JSON);
        } catch (IOException e) {

            return e.toString();
        }
    }
    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            try {
                System.out.println(mainInt(1, args[i]));
            } catch (Exception e) {
                Class<? extends Exception> cls = e.getClass();
                System.out.println(cls.getCanonicalName());
            }
        }
    }
}
