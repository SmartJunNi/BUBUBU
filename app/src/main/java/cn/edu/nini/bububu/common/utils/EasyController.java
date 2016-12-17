package cn.edu.nini.bububu.common.utils;

import org.mym.plog.config.LogController;

/**
 * Created by nini on 2016/12/17.
 */
public class EasyController implements LogController {

    private boolean isLogEnabled;
    private boolean isTimingLogEnabled;

    public EasyController(boolean isTimingLogEnabled, boolean isLogEnabled) {
        this.isTimingLogEnabled = isTimingLogEnabled;
        this.isLogEnabled = isLogEnabled;
    }

    @Override
    public boolean isLogEnabled(int level, String tag, String msg) {
        return isLogEnabled;
    }

    @Override
    public boolean isTimingLogEnabled() {
        return isTimingLogEnabled;
    }
}
