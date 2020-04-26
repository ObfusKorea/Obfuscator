package listener.main;

import C_2011.Listener.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;

public class ListenerCaller {

    enum OPTIONS {
        ERROR, MBA, INVARIANT, CONTEXTUAL, DYNAMICOP, C, CTYPE, ASM
    }

    public static ArrayList getOption(String[] args) {
        ArrayList options = new ArrayList();
        if (args.length < 1)
            options.add(OPTIONS.C);
        for (int i = 0; i < args.length; i++) {
            String option = args[i];
            if(option.equals("-C"))
                options.add(OPTIONS.C);
            else if(option.equals("-MBA"))
                options.add(OPTIONS.MBA);
            else if(option.equals("-CTYPE"))
                options.add(OPTIONS.CTYPE);
            else if(option.equals("-Dynamic"))
                options.add(OPTIONS.DYNAMICOP);
            else if(option.equals("-Invariant"))
                options.add(OPTIONS.INVARIANT);
            else if(option.equals("-Contextual"))
                options.add(OPTIONS.CONTEXTUAL);
            continue;
        }
        return options;
    }

    public static void callListener(OPTIONS option, ParseTreeWalker walker, ParseTree tree, int count) {
        switch (option) {
            case C:
                walker.walk(new Listener(count), tree);
                break;
            case MBA:
                walker.walk(new MBA_Listener(count), tree);
                break;
            case CTYPE:
                walker.walk(new changeType_Listener(count), tree);
                break;
            case DYNAMICOP:
                walker.walk(new dynamicOpaque_Listener(count), tree);
                break;
            default:
                break;
        }
    }

}
