package C_2011.Listener;

import C_2011.generated.CParser;
import listener.main.Obfuscator;

import java.util.*;

public class VM_Listener extends Listener {
    enum VCODE {
        VPUSH, VADD, VSUB, VMULT, VASSGN
    }

    String getVCode(VCODE c) {
        switch (c) {
            case VPUSH:
                return "VPUSH";
            case VADD:
                return "VADD";
            case VSUB:
                return "VSUB";
            case VMULT:
                return "VMULT";
            case VASSGN:
                return "VASSGN";
            default:
                return "NULL";
        }
    }

    String add = "add(&encoded_bytes, %s);\n";
    SymbolTable symbolTable = new SymbolTable();
    boolean isInitVM = false;

    public VM_Listener(int count) {
        super(count);
    }

    @Override
    public void exitExpressionStatement(CParser.ExpressionStatementContext ctx) {
        String bf = "";
        String prolog = "tempvars[%d] = %s; \n";
        String epilog = "%s = tempvars[%d]; \n";
        if (ctx.children.size() > 2) {
            // INITIALIZE VM OBFUSCATOR
            if (!isInitVM) {
                bf += "struct code encoded_bytes; // VM  byte code \n" +
                        "int tempvars[MAX];\t\t\t// mapping : real var => vm only var\n" +
                        "\n" +
                        "begin_add(&encoded_bytes);\n";
                isInitVM = true;
            }

            for (int i = 0; i < ctx.vmexpressionStatement().size(); i++) {
                bf += newTexts.get(ctx.vmexpressionStatement(i));
            }
            bf += "end_add(&encoded_bytes);\n";
            bf += symbolTable.getPrologCode(prolog);
            bf += "run_on_vm(&encoded_bytes, &tempvars); \n";
            bf += symbolTable.getEpilogCode(epilog);

        } else {
            String exp = (ctx.expression() != null) ? newTexts.get(ctx.expression()) : "";
            bf = String.format("%s;\n", exp);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmprimaryExpression(CParser.VmprimaryExpressionContext ctx) {
        String program = "";
        if (ctx.Identifier() != null) {
            symbolTable.putLocalVar(ctx.Identifier().getText());
            String push = getAddCode(getVCode(VCODE.VPUSH));
            program = push + getAddCode(Integer.toString(symbolTable.getLocalVarID(ctx.Identifier().getText())));
//            program = push + String.format(add, symbolTable.getLocalVarID(ctx.Identifier().getText()) + " " + ctx.Identifier().getText());
        } else if (ctx.Constant() != null) {
            program = ctx.Constant().getText();
        } else if (ctx.vmexpression() != null) {
            program = String.format("%s", newTexts.get(ctx.vmexpression()));
        } else if (ctx.StringLiteral() != null) {
            for (int i = 0; i < ctx.StringLiteral().size(); i++) {
                program = ctx.StringLiteral(i).getText();
            }
        }

        newTexts.put(ctx, program);
    }

    @Override
    public void exitVmpostfixExpression(CParser.VmpostfixExpressionContext ctx) {
        String bf = "";
        bf = newTexts.get(ctx.vmprimaryExpression());

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmargumentExpressionList(CParser.VmargumentExpressionListContext ctx) {
        String bf;
        String assignExp = newTexts.get(ctx.vmassignmentExpression());

        if (ctx.vmargumentExpressionList() == null) {
            bf = assignExp;
        } else {
            bf = String.format("%s, %s", newTexts.get(ctx.vmargumentExpressionList()), assignExp);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmunaryExpression(CParser.VmunaryExpressionContext ctx) {
        String bf = "";
        if (ctx.vmpostfixExpression() != null) { // postfixExp
            bf = newTexts.get(ctx.vmpostfixExpression());
        } else if (ctx.vmunaryExpression() != null) { // ++ / -- / sizeof unaryExp
            bf = String.format("%s%s", ctx.getChild(0).getText(), newTexts.get(ctx.vmunaryExpression()));
        } else if (ctx.unaryOperator() != null) { // unaryOp castExp
            bf = String.format("%s%s", newTexts.get(ctx.unaryOperator()), newTexts.get(ctx.vmcastExpression()));
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmcastExpression(CParser.VmcastExpressionContext ctx) {
        String bf = "";
        if (ctx.vmunaryExpression() != null) {
            String unary = newTexts.get(ctx.vmunaryExpression());
            bf = unary;
        }
        newTexts.put(ctx, bf);
    }


    @Override
    public String insertINIT(String input){
        String init = Obfuscator.getVMInit();
        return init+input;
    }

    String getAddCode(String s) {
        return String.format(add, s);
    }
}

class SymbolTable {
    class Var {
        int id;
        String name;
        int value;

        public Var(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private Map<String, Var> _lsymtable = new HashMap<>();
    private int _localVarID = 0;

    void putLocalVar(String varName) {
        if (_lsymtable.get(varName) == null) {
            _lsymtable.put(varName, new Var(_localVarID++, varName));
        }
    }

    int getLocalVarID(String varName) {
        Var lVar = _lsymtable.get(varName);
        if (lVar != null) {
            return lVar.id;
        }
        return -1;
    }

    String getPrologCode(String prolog){
        StringBuilder sb = new StringBuilder();
        for (String k :
                _lsymtable.keySet()) {
            sb.append(String.format(prolog,getLocalVarID(k),k));
        }
        return sb.toString();
    }

    String getEpilogCode(String epilog){
        StringBuilder sb = new StringBuilder();
        for (String k :
                _lsymtable.keySet()) {
            sb.append(String.format(epilog,k,getLocalVarID(k)));
        }
        return sb.toString();
    }
}