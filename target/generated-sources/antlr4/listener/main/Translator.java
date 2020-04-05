package listener.main;

import obfusListener.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import generated.*;

import java.util.ArrayList;
import java.util.List;

public class Translator {
    enum OPTIONS {
        PRETTYPRINT, BYTECODEGEN, UCODEGEN, ERROR, DEFAULT, MBA, CTYPE, INVARIANT, CONTEXTUAL, DYNAMICOP
    }

    private static ArrayList getOption(String[] args) {
        ArrayList options = new ArrayList();
        if (args.length < 1)
            options.add(OPTIONS.PRETTYPRINT);
        for (int i = 0; i < args.length; i++) {
            String option = args[i];
            if (option.startsWith("-pr") || option.startsWith("-PR"))
                options.add(OPTIONS.PRETTYPRINT);
            else if (option.startsWith("-b") || option.startsWith("-B"))
                options.add(OPTIONS.BYTECODEGEN);
            else if (option.startsWith("-u") || option.startsWith("-U"))
                options.add(OPTIONS.UCODEGEN);
            else if (option.equals("-default"))
                options.add(OPTIONS.DEFAULT);
            else if(option.equals("-MBA"))
                options.add(OPTIONS.MBA);
            else if(option.equals("-Ctype"))
                options.add(OPTIONS.CTYPE);
            else if(option.equals("-Invariant"))
                options.add(OPTIONS.INVARIANT);
            else if(option.equals("-Contextual"))
                options.add(OPTIONS.CONTEXTUAL);
            else if(option.equals("-Dynamic"))
                options.add(OPTIONS.DYNAMICOP);
            continue;
        }
        return options;
    }

    private static void callListener(OPTIONS option, ParseTreeWalker walker, ParseTree tree, int count) {
        switch (option) {
            case PRETTYPRINT:
                walker.walk(new MiniCPrintListener(count), tree);
                break;
            case BYTECODEGEN:
                walker.walk(new BytecodeGenListener(), tree);
                break;
            case UCODEGEN:
                walker.walk(new UCodeGenListener(), tree);
                break;
            case DEFAULT:
                walker.walk(new Listener(count), tree);
                break;
            case MBA:
                walker.walk(new MBA_Listener(count), tree);
                break;
            case CTYPE:
                walker.walk(new changeType_Listener(count), tree);
                break;
            case INVARIANT:
                walker.walk(new invariant_Listener(count), tree);
                break;
            case CONTEXTUAL:
                walker.walk(new contextual_Listener(count), tree);
                break;
            case DYNAMICOP:
                walker.walk(new dynamicOpaque_Listener(count), tree);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        CharStream codeCharStream;
        String filename = "test.c";

        String[] flags = {"-default", "-MBA", "-MBA"};
        List options = getOption(flags);
//        List options = getOption(args);

        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                filename = String.format("result%d.c", i - 1);
            }
            codeCharStream = CharStreams.fromFileName(filename);
            MiniCLexer lexer = new MiniCLexer(codeCharStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiniCParser parser = new MiniCParser(tokens);
            ParseTree tree = parser.program();
            ParseTreeWalker walker = new ParseTreeWalker();

            callListener((OPTIONS) options.get(i), walker, tree, i);
        }

    }
}