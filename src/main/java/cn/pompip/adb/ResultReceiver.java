package cn.pompip.adb;

import com.android.ddmlib.MultiLineReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class ResultReceiver extends MultiLineReceiver {
    List<String> resultList = new ArrayList<>();

    @Override
    public void processNewLines(String[] lines) {
        if (lines == null || lines[0].isEmpty()) {
            return;
        }
        resultList.addAll(Arrays.asList(lines));
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public List<String> getResult() {
        return resultList;
    }
}
