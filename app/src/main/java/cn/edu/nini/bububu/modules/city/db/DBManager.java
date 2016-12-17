package cn.edu.nini.bububu.modules.city.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import org.mym.plog.PLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.edu.nini.bububu.R;
import cn.edu.nini.bububu.base.BaseApplication;

/**
 * Created by nini on 2016/12/17.
 */

public class DBManager {
    private static String TAG = DBManager.class.getSimpleName();
    public static final String DB_NAME = "china_city.db"; //数据库名字
    public static final String PACKAGE_NAME = "cn.edu.nini.bububu";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() +
            File.separator+ PACKAGE_NAME;  //在手机里存放数据库的位置(/data/data/cn.edu.nini.bububu/china_city.db)
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public DBManager() {
    }

    /**
     * 单例模式
     * @return
     */
    public static DBManager getInstance() {
        return DBManagerHolder.sInstance;
    }

    private static final class DBManagerHolder {
        public static final DBManager sInstance = new DBManager();
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void openDatabase() {
        //PLog.e(TAG, DB_PATH + "/" + DB_NAME);
        mDatabase= this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    /**
     * 尝试打开数据库
     * @param dbfile
     * @return
     */
    private SQLiteDatabase openDatabase(String dbfile) {
        try{
            if(!new File(dbfile).exists()){//如果不存在
                InputStream is = BaseApplication.getAppContext().getResources().openRawResource(R.raw.china_city);
                FileOutputStream fos=new FileOutputStream(dbfile);
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
                fos.close();
                is.close();
            }
            //否则就直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(dbfile,null);
        }catch (FileNotFoundException e) {
            PLog.e("FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            PLog.e("IOException");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对外提供关闭数据库的方法
     */
    public void closeDatabase(){
        if (mDatabase != null) {
            this.mDatabase.close();
        }
    }
}
