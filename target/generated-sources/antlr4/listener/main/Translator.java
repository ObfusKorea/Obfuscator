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
//    enum OPTIONS {
//        PRETTYPRINT, BYTECODEGEN, UCODEGEN, ERROR, DEFAULT, MBA, CTYPE, INVARIANT, CONTEXTUAL, DYNAMICOP, C, ASM, C_MBA,C_DYANAMIC,C_CHANGETYPE
//    }
//
//    private static ArrayList getOption(String[] args) {
//        ArrayList options = new ArrayList();
//        if (args.length < 1)
//            options.add(OPTIONS.C);
//        for (int i = 0; i < args.length; i++) {
//            String option = args[i];
//            if (option.startsWith("-pr") || option.startsWith("-PR"))
//                options.add(OPTIONS.PRETTYPRINT);
//            else if (option.startsWith("-b") || option.startsWith("-B"))
//                options.add(OPTIONS.BYTECODEGEN);
//            else if (option.startsWith("-u") || option.startsWith("-U"))
//                options.add(OPTIONS.UCODEGEN);
//            else if (option.equals("-default"))
//                options.add(OPTIONS.DEFAULT);
//            else if(option.equals("-MBA"))
//                options.add(OPTIONS.MBA);
//            else if(option.equals("-Ctype"))
//                options.add(OPTIONS.CTYPE);
//            else if(option.equals("-Invariant"))
//                options.add(OPTIONS.INVARIANT);
//            else if(option.equals("-Contextual"))
//                options.add(OPTIONS.CONTEXTUAL);
//            else if(option.equals("-Dynamic"))
//                options.add(OPTIONS.DYNAMICOP);
//            else if(option.equals("-ASM"))
//                options.add(OPTIONS.ASM);
//            else if(option.equals("-C_MBA"))
//                options.add(OPTIONS.C_MBA);
//            else if(option.equals("-C_Dynamic"))
//                options.add(OPTIONS.C_DYANAMIC);
//            else if(option.equals("-C_ChangeType"))
//                options.add(OPTIONS.C_CHANGETYPE);
//            continue;
//        }
//        return options;
//    }
//
//    private static void callListener(OPTIONS option, ParseTreeWalker walker, ParseTree tree, int count) {
//        switch (option) {
//            case PRETTYPRINT:
//                walker.walk(new MiniCPrintListener(count), tree);
//                break;
//            case BYTECODEGEN:
//                walker.walk(new BytecodeGenListener(), tree);
//                break;
//            case UCODEGEN:
//                walker.walk(new UCodeGenListener(), tree);
//                break;
//            case DEFAULT:
//                walker.walk(new obfusListener.Listener(count), tree);
//                break;
//            case MBA:
//                walker.walk(new MBA_Listener(count), tree);
//                break;
//            case CTYPE:
//                walker.walk(new changeType_Listener(count), tree);
//                break;
//            case INVARIANT:
//                walker.walk(new invariant_Listener(count), tree);
//                break;
//            case CONTEXTUAL:
//                walker.walk(new contextual_Listener(count), tree);
//                break;
//            case DYNAMICOP:
//                walker.walk(new dynamicOpaque_Listener(count), tree);
//                break;
//            case C:
//                walker.walk(new C_2011.Listener.Listener(count), tree);
//                break;
//            case ASM:
//                walker.walk(new asm_Listener(count), tree);
//            case C_MBA:
//                walker.walk(new C_2011.Listener.MBA_Listener(count), tree);
//                break;
//            case C_DYANAMIC:
//                walker.walk(new C_2011.Listener.dynamicOpaque_Listener(count), tree);
//                break;
//            case C_CHANGETYPE:
//                walker.walk(new C_2011.Listener.changeType_Listener(count), tree);
//                break;
//            default:
//                break;
//        }
//    }

    public static void main(String[] args) throws Exception {
        CharStream codeCharStream;
        String filename = "input.c";


//        String[] flags = {"-default", "-MBA", "-MBA"};
//        List options = getOption(flags);
        List options = getOption(args);


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