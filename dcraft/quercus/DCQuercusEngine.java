package dcraft.quercus;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.*;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StringWriter;
import com.caucho.vfs.WriteStream;

import java.io.IOException;
import java.util.function.Consumer;

public class DCQuercusEngine extends QuercusEngine {
    public DCQuercusResult dc_execute(ReadStream reader, Value args) {
        return this.dc_execute(reader, new Consumer<Env>() {
            @Override
            public void accept(Env env) {
                env.setGlobalValue("dc_args", args);
            }
        });
    }

    public DCQuercusResult dc_execute(ReadStream reader, Consumer<Env> prepenv) {
        DCQuercusResult result = new DCQuercusResult();

        result.returned = NullValue.NULL;

        try (reader) {
            QuercusProgram program = QuercusParser.parse(this.getQuercus(), null, reader);

            StringWriter writer = new StringWriter();

            WriteStream out =  writer.openWrite();
            out.setNewlineString("\n");

            QuercusPage page = new InterpretedPage(program);

            Env env = new Env(this.getQuercus(), page, out);

            env.addFunction("dc_debug", new DCDebug());

            if (prepenv != null) {
                prepenv.accept(env);
            }

            try {
                env.start();

                result.returned = program.execute(env);
            }
            catch (QuercusExitException x) {
                System.out.println("execute: " + x);
            }
            finally {
                // ensure a writer close even if exception
                result.output = writer.getString();

                env.close();
            }

            //System.out.println("result: " + value);
        }
        catch (IOException x) {
            System.out.println("error: " + x);
        }

        return result;
    }
}
