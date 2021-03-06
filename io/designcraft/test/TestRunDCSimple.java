package io.designcraft.test;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.*;
import com.caucho.quercus.lib.db.JdbcDriverContext;
import com.caucho.quercus.lib.json.JsonModule;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.WriteStream;
import dcraft.quercus.DCQuercusEngine;
import dcraft.quercus.DCQuercusResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class TestRunDCSimple {
    public static void main(String[] args) {
        System.out.println("hi");

        DCQuercusEngine engine = new DCQuercusEngine();
        engine.init();
        engine.setIni("foo", "bar");
        engine.setOutputStream(System.out);

        com.caucho.vfs.Path path = new com.caucho.vfs.FilePath("./php/test-args.php");

        ArrayValue p = new ArrayValueImpl();

        p.put("title", "in p");

        ArrayValue phpargs = new ArrayValueImpl();

        phpargs.put(StringValue.create("p"), p);
        phpargs.put("number", 25);

        //Value phpargs = JsonModule.json_decode(null, (StringValue) StringValue.create("{ \"title\": \"top level\", \"number\": 55, \"p\": { \"title\": \"from p\" } }"), true);

        try {
            DCQuercusResult result = engine.dc_execute(path.openRead(), new Consumer<Env>() {
                @Override
                public void accept(Env env) {
                    phpargs.put(env, "title", "top level: \uD83D\uDE4F plus");

                    env.setGlobalValue("dc_args", phpargs);
                }
            });

            System.out.println("value: " + result.returned);
            System.out.println("output: " + result.output);
            System.out.println("unicode: \uD83D\uDE4F");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> final");
    }
}
