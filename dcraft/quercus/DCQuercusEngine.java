package dcraft.quercus;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.*;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.WriteStream;
import io.designcraft.test.TestFuncCall;

import java.io.IOException;
import java.io.OutputStream;

public class DCQuercusEngine extends QuercusEngine {
    public Value dc_execute(ReadStream reader, Value args) {
        Value value = NullValue.NULL;

        try (reader) {
            QuercusProgram program = QuercusParser.parse(this.getQuercus(), null, reader);

            OutputStream os = this.getOutputStream();
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

            Env env = new Env(this.getQuercus(), page, out, null, null);

            env.addFunction("dc_debug", new DCDebug());
            env.setGlobalValue("dc_args", args);

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

        return value;
    }
}
