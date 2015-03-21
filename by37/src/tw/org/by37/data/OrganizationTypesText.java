package tw.org.by37.data;

import static tw.org.by37.config.SysConfig.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * 基構類別資料,儲存於手機內部
 * 
 * @author Hermes.Hsieh
 * 
 */
public class OrganizationTypesText {
        private final static String TAG = "OrganizationTypesText";

        public final static String fileName = "organization_types.txt";

        private static String filePath = SD_DB_PATH;

        public OrganizationTypesText() {
        }

        public static boolean existFile() {
                File dbf = new File(filePath + fileName);
                if (dbf.exists())
                        return true;
                else
                        return false;
        }

        public static String getDBPath() {
                return filePath;
        }

        public static String getFileName() {
                return fileName;
        }

        public static String getTextPath() {
                return filePath + fileName;
        }

        public static void deleteText() {
                File dbf = new File(filePath + fileName);
                if (dbf.exists())
                        dbf.delete();
        }

        public static void saveText(String context) {
                Log.v(TAG, "saveText");
                /* 取得儲存 goods_types.txt 路徑 */
                OrganizationTypesText mText = new OrganizationTypesText();
                String dbPath = mText.getDBPath();
                String fileName = mText.getFileName();

                try {
                        File root = new File(dbPath);
                        if (!root.exists()) {
                                root.mkdirs();
                        }
                        File gpxfile = new File(root, fileName);
                        FileWriter writer = new FileWriter(gpxfile);
                        writer.append(context);
                        writer.flush();
                        writer.close();
                        Log.v(TAG, "Save Done!");
                } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "IOException");
                }
        }

        public static String readText() {
                Log.v(TAG, "readText");
                /*
                 * 取得儲存 GoodsTypesText mText = new GoodsTypesText();.txt 路徑
                 */
                OrganizationTypesText mText = new OrganizationTypesText();
                String path = mText.getTextPath();

                InputStream inputStream = null;
                BufferedReader br = null;
                StringBuilder sb = null;

                try {
                        /**
                         * Read this file into InputStream
                         **/
                        inputStream = new FileInputStream(path);

                        br = new BufferedReader(new InputStreamReader(inputStream));

                        sb = new StringBuilder();

                        String line;
                        while ((line = br.readLine()) != null) {
                                sb.append(line);
                        }

                        Log.v(TAG, "Read Done!");
                        Log.v(TAG, sb.toString());

                } catch (IOException e) {
                        e.printStackTrace();
                } finally {
                        if (inputStream != null) {
                                try {
                                        inputStream.close();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                        if (br != null) {
                                try {
                                        br.close();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                }

                return sb.toString();
        }

}
