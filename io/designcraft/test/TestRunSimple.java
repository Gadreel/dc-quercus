package io.designcraft.test;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NullValue;
import com.caucho.quercus.lib.db.JdbcDriverContext;
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

public class TestRunSimple {
    public static void main(String[] args) {
        System.out.println("hi");

        QuercusEngine engine = new QuercusEngine();
        engine.init();
        engine.setIni("foo", "bar");
        engine.setOutputStream(System.out);

        try {
            engine.executeFile("./php/test-db.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
