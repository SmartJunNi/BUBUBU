package cn.edu.nini.bububu.component;

import com.litesuits.orm.LiteOrm;

import cn.edu.nini.bububu.BuildConfig;
import cn.edu.nini.bububu.base.BaseApplication;
import cn.edu.nini.bububu.base.C;

/**
 * Created by nini on 2016/12/17.
 */

public class OrmLite {
    static LiteOrm sLiteOrm;

    public static LiteOrm getLiteOrm() {
        getInstance();//这步仅仅为了加载OrmLoader的静态成员变量，如果没有则会出现空指针
        return sLiteOrm;
    }

    private static OrmLite getInstance() {
        return OrmLoader.defaultInstance;
    }

    private OrmLite() {
        if (sLiteOrm == null) {
            sLiteOrm = LiteOrm.newSingleInstance(BaseApplication.getAppContext(), C.ORM_NAME);
        }
        sLiteOrm.setDebugged(BuildConfig.DEBUG);
    }

    private static class OrmLoader {
        private static final OrmLite defaultInstance = new OrmLite();
    }
}
