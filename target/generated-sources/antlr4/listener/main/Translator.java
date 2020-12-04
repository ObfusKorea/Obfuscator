package listener.main;

import C_2011.Listener.MBA_Listener;
import obfusListener.*;
import obfusListener.changeType_Listener;
import obfusListener.dynamicOpaque_Listener;
import oldMiniCFiles.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import C_2011.Listener.*;
import C_2011.generated.*;

import java.util.ArrayList;
import java.util.List;

public class Translator {

    public static void main(String[] args) throws Exception {
        CharStream codeCharStream;
        String filename = "input.c";


        ListenerCaller caller = new ListenerCaller();
//        String[] flags = {"-default", "-MBA", "-MBA"};
//        List options = caller.getOption(flags);
        List options = caller.getOption(args);


        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                filename = String.format("result_C_%d.c", i - 1);
            }
            codeCharStream = CharStreams.fromFileName(filename);
            CLexer lexer = new CLexer(codeCharStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CParser parser = new CParser(tokens);
            ParseTree tree = parser.compilationUnit();
            ParseTreeWalker walker = new ParseTreeWalker();

            caller.callListener((ListenerCaller.OPTIONS) options.get(i), walker, tree, i);
        }

    }
}