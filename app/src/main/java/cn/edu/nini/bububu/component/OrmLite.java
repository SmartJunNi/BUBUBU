package cn.edu.nini.bububu.component;

import com.litesuits.orm.LiteOrm;

import org.mym.plog.PLog;

import cn.edu.nini.bububu.BuildConfig;
import cn.edu.nini.bububu.base.BaseApplication;
import cn.edu.nini.bububu.base.C;
import cn.edu.nini.bububu.base.SimpleSubscribe;
import cn.edu.nini.bububu.common.utils.RxUtil;
import cn.edu.nini.bububu.modules.main.domain.CityORM;
import rx.Observable;

/**
 * Created by nini on 2016/12/17.
 */

public class OrmLite {
    static LiteOrm sLiteOrm;

    public static LiteOrm getInstance() {
        getOrmHolder();//这步仅仅为了加载OrmLoader的静态成员变量，如果没有则会出现空指针
        return sLiteOrm;
    }

    private static OrmLite getOrmHolder() {
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

    public static <T> void  OrmTest(Class<T> t){
        Observable.from(getInstance().query(t))
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpleSubscribe<T>() {
                    @Override
                    public void onNext(T t) {
                        if(t instanceof CityORM){
                            PLog.w(((CityORM) t).getName());
                        }
                    }
                });
    }
}
