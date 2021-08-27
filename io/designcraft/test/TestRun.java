package io.designcraft.test;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NullValue;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.function.FunSpecialCall;
import com.caucho.quercus.lib.db.JdbcDriverContext;
import com.caucho.quercus.lib.db.MysqlModule;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.quercus.servlet.api.QuercusHttpServletRequest;
import com.caucho.quercus.servlet.api.QuercusHttpServletResponse;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StreamImpl;
import com.caucho.vfs.WriteStream;

import java.io.IOException;

public class TestRun {
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
                QuercusProgram program = QuercusParser.parse(engine.getQuercus(), (com.caucho.vfs.Path)null, reader);

                StreamImpl s = new StreamImpl() {
                    public boolean canWrite() {
                        return true;
                    }

                    public void write(byte[] buffer, int offset, int length, boolean isEnd) throws IOException {
                        System.out.write(buffer, offset, length);
                    }
                };

                WriteStream ws = new WriteStream(new QuercusEngine.OutputStreamStream(engine.getOutputStream()));
                ws.setNewlineString("\n");

                try {
                    ws.setEncoding("iso-8859-1");
                } catch (Exception var11) {
                }

                QuercusPage page = new InterpretedPage(program);
                Env env = new Env(engine.getQuercus(), page, ws, (QuercusHttpServletRequest) null, (QuercusHttpServletResponse) null);

                env.addFunction("funTest", new TestFuncCall());

                Object value = NullValue.NULL;

                try {
                    env.start();
                } catch (QuercusExitException var10) {
                }

                //System.out.flush();

        /*
        try {
            engine.executeFile("./php/test-db.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

                //Value conn = MysqlModule.mysql_connect(env, (StringValue) StringValue.create("localhost"),
                //        (StringValue)StringValue.create("writes"), (StringValue)StringValue.create("GbKbJBuvTuj7"),
                //        false, 0);

                //System.out.println("t: " + conn);

            //System.out.println("before");

                try {
                    value = program.execute(env);
                } catch (QuercusExitException var10) {
                }

                System.out.flush();

                System.out.println("f: " + value);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("a: " + engine.getQuercus().getConnection(null));
    }
}
