/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.shell;

import java.io.DataOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CMD {
    public  enum Command {
        cmd, adb, su, sh
    }

    public static final String TAG = "CommandExecution";
    public final static String COMMAND_EXIT = "exit\n";
    public final static String COMMAND_LINE_END = "\n";

    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    public static void execCommand(String command, Command c) {
        String[] commands = {command};
         execCommand(commands, c);
    }

    public static void execCommand(String[] commands, Command c) {

        DataOutputStream os = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("adb".equals(c.name())?"adb shell":c.name());
            ResultRunnable resultTarget = new ResultRunnable(process.getInputStream());
            ResultRunnable errorTarget = new ResultRunnable(process.getErrorStream());

            Future<String> successFuture = threadPool.submit(resultTarget);
            Future<String> errorFuture = threadPool.submit(errorTarget);

            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command != null) {
                    os.write(command.getBytes());
                    os.writeBytes(COMMAND_LINE_END);
                    os.flush();
                }
            }

//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNext()){
//                String line = scanner.nextLine();
//
//                os.write(line.getBytes());
//                os.writeBytes(COMMAND_LINE_END);
//                os.flush();
//                if ("exit".equals(line)){
//                    break;
//                }
//            };
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            String s = successFuture.get();
            System.out.println("end:"+s);
            String s1 = errorFuture.get();
            System.out.println(s1);

            threadPool.shutdown();


//            ResultLog.i(TAG, commandResult.result + " +++ " + commandResult.successMsg + " +++ " + commandResult.errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = e.getMessage();
            if (errmsg != null) {
                ResultLog.e(TAG, errmsg);
            }
        } finally {
            if (process != null)
                process.destroy();
        }

    }



    public static void main(String[] args) {
        System.out.println(Command.adb);
        CMD.execCommand(new String[]{"dir","dir","dir"}, Command.cmd);

    }

    public void test(){

    }
}

