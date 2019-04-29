package cn.pompip.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class ResultRunnable implements Callable<String> {
    private final static String TAG = "ResultRunnable";
    private InputStream inputStream;

    public ResultRunnable(InputStream inputStream ) {
        this.inputStream = inputStream;

    }

    @Override
    public String call() throws Exception {
        BufferedReader successResult = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("GB2312")));
        StringBuilder successMsg = new StringBuilder();
        String s;
        do {
            try {
                s = successResult.readLine();
            } catch (IOException e) {
                s = null;
                e.printStackTrace();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (s != null) {
                successMsg.append(s);

                ResultLog.i(TAG, s);
            }
        } while (s != null);
        return successMsg.toString();
    }



}
