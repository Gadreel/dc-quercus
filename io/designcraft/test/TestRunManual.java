package io.designcraft.test;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NullValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.lib.db.JdbcDriverContext;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.WriteStream;

import java.io.IOException;
import java.io.OutputStream;

public class TestRunManual {
    public static void main(String[] args) {
        System.out.println("hi");

        QuercusEngine engine = new QuercusEngine();
        engine.init();
        engine.setIni("foo", "bar");
        engine.setOutputStream(System.out);

        //engine.getQuercus().getIniDefinitions().ad("quercus.jdbc_drivers", dbdrivers);

        JdbcDriverContext ctx = engine.getQuercus().getJdbcDriverContext();

        ctx.setProtocol("mysql", "org.mariadb.jdbc.Driver");
        ctx.setDefaultDriver("org.mariadb.jdbc.Driver");

                /*

                ctx.setProtocol("mysql", "com.mysql.cj.jdbc.Driver");
                ctx.setDefaultDriver("com.mysql.cj.jdbc.Driver");

                 */

        System.out.println("a: " + ctx.getDefaultDriver());

        try {
            com.caucho.vfs.Path path = new com.caucho.vfs.FilePath("./php/test-db.php");

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

            Value value = NullValue.NULL;

            try {
                env.start();
            } catch (QuercusExitException x) {
                System.out.println("start: " + x);
            }

            System.out.println("between");

            try {
                value = program.execute(env);
            } catch (QuercusExitException x) {
                System.out.println("execute: " + x);
            }

            out.flushBuffer();
            out.free();

            if (os != null)
                os.flush();

            System.out.println("result: " + value);
        }
        catch (IOException x) {
            System.out.println("error: " + x);
        }

    }
}
