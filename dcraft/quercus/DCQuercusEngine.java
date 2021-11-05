package dcraft.quercus;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.*;
import com.caucho.quercus.function.AbstractFunction;
import com.caucho.quercus.page.InterpretedPage;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.parser.QuercusParser;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StringWriter;
import com.caucho.vfs.WriteStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DCQuercusEngine extends QuercusEngine {
    protected Map<String, AbstractFunction> funcs = new HashMap<>();

    public DCQuercusEngine() {
        this.setIni("unicode.semantics", "true");

        this.withFunc("dc_debug", new DCDebug());
    }

    public DCQuercusEngine withFunc(String name, AbstractFunction func) {
        this.funcs.put(name, func);

        return this;
    }

    public DCQuercusResult dc_execute(ReadStream reader, Value args) {
        return this.dc_execute(reader, new Consumer<Env>() {
            @Override
            public void accept(Env env) {
                env.setGlobalValue("dc_args", args);
            }
        });
    }

    public DCQuercusResult dc_execute(ReadStream reader, Consumer<Env> prepenv) {
        return this.dc_execute(reader, prepenv, null);
    }

    public DCQuercusResult dc_execute(ReadStream reader, Consumer<Env> prepenv, Consumer<Env> finenv) {
        DCQuercusResult result = new DCQuercusResult();

        result.returned = NullValue.NULL;

        try (reader) {
            reader.setEncoding("utf-8");

            QuercusProgram program = QuercusParser.parse(this.getQuercus(), null, reader);

            DCStringWriter writer = new DCStringWriter();
            //writer.setWriteEncoding("WINDOWS-HACK");

            WriteStream out =  writer.openWrite();
            out.setNewlineString("\n");
            //out.setEncoding("WINDOWS-HACK");

            QuercusPage page = new InterpretedPage(program);

            Env env = new Env(this.getQuercus(), page, out);

            for (String name : this.funcs.keySet())
                env.addFunction(name, this.funcs.get(name));

            if (prepenv != null) {
                prepenv.accept(env);
            }

            try {
                env.start();

                result.returned = program.execute(env);

                if (finenv != null) {
                    finenv.accept(env);
                }
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
