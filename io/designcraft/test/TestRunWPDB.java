package io.designcraft.test;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.*;
import com.caucho.quercus.lib.db.JdbcDriverContext;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.WriteStream;
import dcraft.quercus.DCDebug;
import dcraft.quercus.DCQuercusEngine;

import java.io.IOException;
import java.io.OutputStream;

public class TestRunWPDB {
    public static void main(String[] args) {
        System.out.println("hi");

        QuercusEngine engine = new QuercusEngine();
        engine.setIni("foo", "bar");
        engine.setIni("unicode.semantics", "true");
        engine.init();
        engine.setOutputStream(System.out);

        JdbcDriverContext ctx = engine.getQuercus().getJdbcDriverContext();

        ctx.setProtocol("mysql", "org.mariadb.jdbc.Driver");
        ctx.setDefaultDriver("org.mariadb.jdbc.Driver");

        com.caucho.vfs.Path path = new com.caucho.vfs.FilePath("./php-wp/test.php");

        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> run 1");

        execute(engine, path, 1);

        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> final");
    }

    static public void execute(QuercusEngine engine, com.caucho.vfs.Path path, long runnum) {

        try {
            ReadStream reader = path.openRead();

            QuercusProgram program = QuercusParser.parse(engine.getQuercus(), null, reader);

            OutputStream os = engine.getOutputStream();
            WriteStream out;

            if (os != null) {
                QuercusEngine.OutputStreamStream s = new QuercusEngine.OutputStreamStream(os);
                WriteStream ws = new WriteStream(s);

                ws.setNewlineString("\n");

                try {
                    ws.setEncoding("iso-8859-1");
                } catch (Exception e) {
                }

                out = ws;
            } else
                out = new WriteStream(StdoutStream.create());

            QuercusPage page = new InterpretedPage(program);

            Env env = new Env(engine.getQuercus(), page, out, null, null);

            env.addFunction("funTest", new TestFuncCall());
            env.addFunction("dcdebug", new DCDebug());
            env.addFunction("dc_debug", new DCDebug());
            env.setGlobalValue("runnumber", LongValue.create(runnum));

            Value value = NullValue.NULL;

            try {
                env.start();
            } catch (QuercusExitException x) {
                System.out.println("start: " + x);
            }

            //System.out.println("between");

            try {
                value = program.execute(env);
            } catch (QuercusExitException x) {
                System.out.println("execute: " + x);
            }
            finally {
                env.close();
            }

            out.flushBuffer();
            out.free();

            if (os != null)
                os.flush();

            //System.out.println("result: " + value);
        }
        catch (IOException x) {
            System.out.println("error: " + x);
        }
    }
}
