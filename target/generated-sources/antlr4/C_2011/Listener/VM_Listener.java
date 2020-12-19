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
    public void exitVmmultiplicativeExpression(CParser.VmmultiplicativeExpressionContext ctx) {
        String bf;
        if (ctx.children.size() == 1) { // castExp
            bf = newTexts.get(ctx.vmcastExpression());
        } else {
            String multi = newTexts.get(ctx.vmmultiplicativeExpression());
            String op;
            if (ctx.getChild(1).getText().equals("*")){
                op = getAddCode(getVCode(VCODE.VMULT));
            }else{
                op = getAddCode(getVCode(VCODE.VSUB));
            }
            String cast = newTexts.get(ctx.vmcastExpression());
//            String push = String.format(add, getCode(CODE.VPUSH));
            bf = String.format("%s %s %s", cast, multi, op);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmadditiveExpression(CParser.VmadditiveExpressionContext ctx) {
        String bf;
        String multi = newTexts.get(ctx.vmmultiplicativeExpression());
        if (ctx.children.size() == 1) { // multiplicativeExp
            bf = multi;
        } else {
            String additive = newTexts.get(ctx.vmadditiveExpression());
            String op;
            if (ctx.getChild(1).getText().equals("+")){
                op = getAddCode(getVCode(VCODE.VADD));
            }else{
                op = getAddCode(getVCode(VCODE.VSUB));
            }
//            String push = String.format(add, getCode(CODE.VPUSH));
            bf = String.format("%s %s %s", multi, additive, op);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmshiftExpression(CParser.VmshiftExpressionContext ctx) {
        String bf;
        String additive = newTexts.get(ctx.vmadditiveExpression());
        if (ctx.children.size() == 1) { // additiveExp
            bf = additive;
        } else {
            String shift = newTexts.get(ctx.vmshiftExpression());
            String op = ctx.getChild(1).getText();
            bf = String.format("%s %s %s", shift, op, additive);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmrelationalExpression(CParser.VmrelationalExpressionContext ctx) {
        String bf;
        String shift = newTexts.get(ctx.vmshiftExpression());
        if (ctx.children.size() == 1) { // shiftExp
            bf = shift;
        } else {
            String relation = newTexts.get(ctx.vmrelationalExpression());
            String op = ctx.getChild(1).getText();
            bf = String.format("%s %s %s", relation, op, shift);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmequalityExpression(CParser.VmequalityExpressionContext ctx) {
        String bf;
        String relation = newTexts.get(ctx.vmrelationalExpression());
        if (ctx.children.size() == 1) { // relationalExpression
            bf = relation;
        } else {
            String equality = newTexts.get(ctx.vmequalityExpression());
            String op = ctx.getChild(1).getText();
            bf = String.format("%s %s %s", equality, op, relation);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmandExpression(CParser.VmandExpressionContext ctx) {
        String bf;
        String equal = newTexts.get(ctx.vmequalityExpression());
        if (ctx.children.size() == 1) { // equalExp
            bf = equal;
        } else {
            String and = newTexts.get(ctx.vmandExpression());
            String op = ctx.getChild(1).getText();
            bf = obfus_binaryExp(and, op, equal);
//			bf = String.format("%s %s %s", and, op, equal);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmexclusiveOrExpression(CParser.VmexclusiveOrExpressionContext ctx) {
        String bf;
        String and = newTexts.get(ctx.vmandExpression());
        if (ctx.children.size() == 1) { // andExp
            bf = and;
        } else {
            String exclusive = newTexts.get(ctx.vmexclusiveOrExpression());
            String op = ctx.getChild(1).getText();
            bf = obfus_binaryExp(exclusive, op, and);
//			bf = String.format("%s %s %s", exclusive, op, and);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVminclusiveOrExpression(CParser.VminclusiveOrExpressionContext ctx) {
        String bf;
        String exclusive = newTexts.get(ctx.vmexclusiveOrExpression());
        if (ctx.children.size() == 1) { // exclusiveOr
            bf = exclusive;
        } else {
            String inclusive = newTexts.get(ctx.vminclusiveOrExpression());
            String op = ctx.getChild(1).getText();
            bf = obfus_binaryExp(inclusive, op, exclusive);
//			bf = String.format("%s %s %s", inclusive, op, exclusive);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmlogicalAndExpression(CParser.VmlogicalAndExpressionContext ctx) {
        String bf;
        String incluesive = newTexts.get(ctx.vminclusiveOrExpression());
        if (ctx.children.size() == 1) { // inclusiveAnd
            bf = incluesive;
        } else {
            String logical = newTexts.get(ctx.vmlogicalAndExpression());
            String op = ctx.getChild(1).getText();
            bf = String.format("%s %s %s", logical, op, incluesive);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmlogicalOrExpression(CParser.VmlogicalOrExpressionContext ctx) {
        String bf;
        String logicalAnd = newTexts.get(ctx.vmlogicalAndExpression());
        if (ctx.children.size() == 1) { // logicalAnd
            bf = logicalAnd;
        } else {
            String logicalOr = newTexts.get(ctx.vmlogicalOrExpression());
            String op = ctx.getChild(1).getText();
            bf = String.format("%s %s %s", logicalOr, op, logicalAnd);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmconditionalExpression(CParser.VmconditionalExpressionContext ctx) {
        String bf;
        String logicalOr = newTexts.get(ctx.vmlogicalOrExpression());
        if (ctx.children.size() == 1) {
            bf = logicalOr;
        } else {
            String exp = newTexts.get(ctx.vmexpression());
            String condiExp = newTexts.get(ctx.vmconditionalExpression());
            bf = String.format("%s ? %s : %s", logicalOr, exp, condiExp);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmassignmentExpression(CParser.VmassignmentExpressionContext ctx) {
        String bf = "";
        if (ctx.vmconditionalExpression() != null) { // conditionalExpression
            bf = newTexts.get(ctx.vmconditionalExpression());
            obfus_arg(bf);
        } else if (ctx.vmunaryExpression() != null) { // unaryExpression assignmentOperator assignmentExpression
            String unary = newTexts.get(ctx.vmunaryExpression());
            String assignOP = newTexts.get(ctx.assignmentOperator());
            String assignExp = newTexts.get(ctx.vmassignmentExpression());
            bf = assignExp;
            bf += getAddCode(getVCode(VCODE.VASSGN));;
            bf += unary;

        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmexpression(CParser.VmexpressionContext ctx) {
        String bf = "";
        String assignExp = newTexts.get(ctx.vmassignmentExpression());
        if (ctx.children.size() == 1) {
            bf = assignExp;
        } else if (ctx.children.size() == 3 && (ctx.children.get(0)) instanceof CParser.ExpressionContext) {
            System.out.println("ASf");
        } else {
            String exp = newTexts.get(ctx.vmexpression());
            bf = String.format("%s, %s", exp, assignExp);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitVmexpressionStatement(CParser.VmexpressionStatementContext ctx) {
        String bf = "";
        String exp = (ctx.vmexpression() != null) ? newTexts.get(ctx.vmexpression()) : "";
        bf = String.format("%s;\n", exp);
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