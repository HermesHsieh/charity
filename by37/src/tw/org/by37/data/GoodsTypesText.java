package tw.org.by37.data;

import static tw.org.by37.config.SysConfig.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import tw.org.by37.R;

import android.content.Context;
import android.util.Log;

/**
 * 物品類別資料,儲存於手機內部
 * 
 * @author Hermes.Hsieh
 * 
 */
public class GoodsTypesText {
        private final static String TAG = GoodsTypesText.class.getName();

        public final static String fileName = "goods_types.txt";

        private static String filePath = SD_DB_PATH;

        public GoodsTypesText() {
        }

        /** 檔案是否存在 **/
        public static boolean existFile() {
                File dbf = new File(filePath + fileName);
                if (dbf.exists()) {
                        return true;
                } else {
                        return false;
                }
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
                GoodsTypesText mText = new GoodsTypesText();
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
                GoodsTypesText mText = new GoodsTypesText();
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

        public static void copyGoodsTypesData(Context mContext) throws IOException {
                Log.i(TAG, "copyGoodsTypesData ---*");
                // Open your local db as the input stream
                InputStream myInput = mContext.getResources().openRawResource(R.raw.goods_types);
                // Path to the just created empty db
                String outFileName = filePath + fileName;
                // Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(outFileName);
                // transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                        myOutput.write(buffer, 0, length);
                }
                // Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
        }

}
