/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.engine;

import cn.pompip.util.Constant;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by harry on 2017/5/11.
 */
public class JavaScripts {

    public static void test() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        NashornScriptEngine nashornScriptEngine = (NashornScriptEngine) engine;


        try {
            Reader fReader = new InputStreamReader(new FileInputStream(Constant.getResourceFile("init.js")));
            engine.eval(fReader);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        System.out.println("...");
    }
}
