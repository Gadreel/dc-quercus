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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingQueue;

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

        com.caucho.vfs.Path path = new com.caucho.vfs.FilePath("./php/basic-db.php");

        /*
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> run 1");

        execute(engine, path, 1);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> run 2");

        execute(engine, path, 2);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> run 3");

        execute(engine, path, 3);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> final");

         */

        Deque<Long> queue = new ArrayDeque<>();

        for (long i = 0; i < 1500; i++) {
            queue.addLast(i);
        }

        Runnable work = new Runnable() {
            @Override
            public void run() {
                System.out.println("thread start: " + Thread.currentThread().getName());

                try {
                    Long number = queue.pollFirst();

                    while (number != null) {
                        System.out.println("using thread: " + Thread.currentThread().getName() + " - value: " + number);

                        execute(engine, path, number);

                        Thread.sleep(1);

                        System.out.println("using thread: " + Thread.currentThread().getName() + " - left: " + queue.size());

                        number = queue.pollFirst();
                    }
                }
                catch (InterruptedException x) {
                    System.out.println("thread interrupted: " + Thread.currentThread().getName());
                }

                System.out.println("thread done: " + Thread.currentThread().getName());
            }
        };

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(work);
            t.setName("thread: " + i);
            t.start();
        }

        try {
            System.in.read();
        }
        catch (IOException x) {
            System.out.println("read error: " + x);
        }
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

            System.out.println("result: " + value);
        }
        catch (IOException x) {
            System.out.println("error: " + x);
        }
    }
}
