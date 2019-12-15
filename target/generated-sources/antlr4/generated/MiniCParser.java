// Generated from MiniC.g4 by ANTLR 4.7.2

package generated;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiniCParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, VOID=21, INT=22, CHAR=23, DOUBLE=24, WHILE=25, 
		FOR=26, IF=27, ELSE=28, RETURN=29, OR=30, AND=31, LE=32, GE=33, EQ=34, 
		NE=35, IDENT=36, CHARACTER=37, DOUBLE_Lit=38, LITERAL=39, DecimalConstant=40, 
		OctalConstant=41, HexadecimalConstant=42, WS=43;
	public static final int
		RULE_program = 0, RULE_decl = 1, RULE_var_decl = 2, RULE_type_spec = 3, 
		RULE_fun_decl = 4, RULE_params = 5, RULE_param = 6, RULE_stmt = 7, RULE_expr_stmt = 8, 
		RULE_while_stmt = 9, RULE_for_stmt = 10, RULE_compound_stmt = 11, RULE_local_decl = 12, 
		RULE_array_init_val = 13, RULE_if_stmt = 14, RULE_return_stmt = 15, RULE_expr = 16, 
		RULE_args = 17;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "decl", "var_decl", "type_spec", "fun_decl", "params", "param", 
			"stmt", "expr_stmt", "while_stmt", "for_stmt", "compound_stmt", "local_decl", 
			"array_init_val", "if_stmt", "return_stmt", "expr", "args"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'='", "'['", "']'", "'''", "'('", "')'", "','", "'{'", 
			"'}'", "'-'", "'+'", "'--'", "'++'", "'*'", "'/'", "'%'", "'<'", "'>'", 
			"'!'", "'void'", "'int'", "'char'", "'double'", "'while'", "'for'", "'if'", 
			"'else'", "'return'", "'or'", "'and'", "'<='", "'>='", "'=='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "VOID", "INT", 
			"CHAR", "DOUBLE", "WHILE", "FOR", "IF", "ELSE", "RETURN", "OR", "AND", 
			"LE", "GE", "EQ", "NE", "IDENT", "CHARACTER", "DOUBLE_Lit", "LITERAL", 
			"DecimalConstant", "OctalConstant", "HexadecimalConstant", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MiniC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MiniCParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public List<DeclContext> decl() {
			return getRuleContexts(DeclContext.class);
		}
		public DeclContext decl(int i) {
			return getRuleContext(DeclContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(36);
				decl();
				}
				}
				setState(39); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << VOID) | (1L << INT) | (1L << CHAR) | (1L << DOUBLE))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclContext extends ParserRuleContext {
		public Var_declContext var_decl() {
			return getRuleContext(Var_declContext.class,0);
		}
		public Fun_declContext fun_decl() {
			return getRuleContext(Fun_declContext.class,0);
		}
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitDecl(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_decl);
		try {
			setState(43);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(41);
				var_decl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(42);
				fun_decl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Var_declContext extends ParserRuleContext {
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(MiniCParser.IDENT, 0); }
		public TerminalNode LITERAL() { return getToken(MiniCParser.LITERAL, 0); }
		public TerminalNode DOUBLE_Lit() { return getToken(MiniCParser.DOUBLE_Lit, 0); }
		public TerminalNode CHARACTER() { return getToken(MiniCParser.CHARACTER, 0); }
		public Var_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterVar_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitVar_decl(this);
		}
	}

	public final Var_declContext var_decl() throws RecognitionException {
		Var_declContext _localctx = new Var_declContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_var_decl);
		int _la;
		try {
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(45);
				type_spec();
				setState(46);
				match(IDENT);
				setState(47);
				match(T__0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				type_spec();
				setState(50);
				match(IDENT);
				setState(51);
				match(T__1);
				setState(52);
				_la = _input.LA(1);
				if ( !(_la==DOUBLE_Lit || _la==LITERAL) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(53);
				match(T__0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(55);
				type_spec();
				setState(56);
				match(IDENT);
				setState(57);
				match(T__2);
				setState(58);
				match(LITERAL);
				setState(59);
				match(T__3);
				setState(60);
				match(T__0);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(62);
				type_spec();
				setState(63);
				match(IDENT);
				setState(64);
				match(T__1);
				setState(65);
				match(T__4);
				setState(66);
				match(CHARACTER);
				setState(67);
				match(T__4);
				setState(68);
				match(T__0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_specContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(MiniCParser.VOID, 0); }
		public TerminalNode INT() { return getToken(MiniCParser.INT, 0); }
		public TerminalNode CHAR() { return getToken(MiniCParser.CHAR, 0); }
		public TerminalNode DOUBLE() { return getToken(MiniCParser.DOUBLE, 0); }
		public Type_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterType_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitType_spec(this);
		}
	}

	public final Type_specContext type_spec() throws RecognitionException {
		Type_specContext _localctx = new Type_specContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_type_spec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << VOID) | (1L << INT) | (1L << CHAR) | (1L << DOUBLE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Fun_declContext extends ParserRuleContext {
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(MiniCParser.IDENT, 0); }
		public ParamsContext params() {
			return getRuleContext(ParamsContext.class,0);
		}
		public Compound_stmtContext compound_stmt() {
			return getRuleContext(Compound_stmtContext.class,0);
		}
		public Fun_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fun_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterFun_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitFun_decl(this);
		}
	}

	public final Fun_declContext fun_decl() throws RecognitionException {
		Fun_declContext _localctx = new Fun_declContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_fun_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			type_spec();
			setState(75);
			match(IDENT);
			setState(76);
			match(T__5);
			setState(77);
			params();
			setState(78);
			match(T__6);
			setState(79);
			compound_stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamsContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public TerminalNode VOID() { return getToken(MiniCParser.VOID, 0); }
		public ParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_params; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitParams(this);
		}
	}

	public final ParamsContext params() throws RecognitionException {
		ParamsContext _localctx = new ParamsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_params);
		int _la;
		try {
			setState(91);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				param();
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(82);
					match(T__7);
					setState(83);
					param();
					}
					}
					setState(88);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				match(VOID);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(MiniCParser.IDENT, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitParam(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_param);
		try {
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(93);
				type_spec();
				setState(94);
				match(IDENT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				type_spec();
				setState(97);
				match(IDENT);
				setState(98);
				match(T__2);
				setState(99);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmtContext extends ParserRuleContext {
		public Expr_stmtContext expr_stmt() {
			return getRuleContext(Expr_stmtContext.class,0);
		}
		public Compound_stmtContext compound_stmt() {
			return getRuleContext(Compound_stmtContext.class,0);
		}
		public If_stmtContext if_stmt() {
			return getRuleContext(If_stmtContext.class,0);
		}
		public While_stmtContext while_stmt() {
			return getRuleContext(While_stmtContext.class,0);
		}
		public For_stmtContext for_stmt() {
			return getRuleContext(For_stmtContext.class,0);
		}
		public Return_stmtContext return_stmt() {
			return getRuleContext(Return_stmtContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitStmt(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_stmt);
		try {
			setState(109);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__19:
			case IDENT:
			case DOUBLE_Lit:
			case LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				expr_stmt();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				compound_stmt();
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 3);
				{
				setState(105);
				if_stmt();
				}
				break;
			case WHILE:
				enterOuterAlt(_localctx, 4);
				{
				setState(106);
				while_stmt();
				}
				break;
			case FOR:
				enterOuterAlt(_localctx, 5);
				{
				setState(107);
				for_stmt();
				}
				break;
			case RETURN:
				enterOuterAlt(_localctx, 6);
				{
				setState(108);
				return_stmt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expr_stmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Expr_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterExpr_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitExpr_stmt(this);
		}
	}

	public final Expr_stmtContext expr_stmt() throws RecognitionException {
		Expr_stmtContext _localctx = new Expr_stmtContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expr_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			expr(0);
			setState(112);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class While_stmtContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(MiniCParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public While_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_while_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterWhile_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitWhile_stmt(this);
		}
	}

	public final While_stmtContext while_stmt() throws RecognitionException {
		While_stmtContext _localctx = new While_stmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_while_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(WHILE);
			setState(115);
			match(T__5);
			setState(116);
			expr(0);
			setState(117);
			match(T__6);
			setState(118);
			stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_stmtContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(MiniCParser.FOR, 0); }
		public Local_declContext local_decl() {
			return getRuleContext(Local_declContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public For_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterFor_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitFor_stmt(this);
		}
	}

	public final For_stmtContext for_stmt() throws RecognitionException {
		For_stmtContext _localctx = new For_stmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_for_stmt);
		try {
			setState(146);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(120);
				match(FOR);
				setState(121);
				match(T__5);
				setState(122);
				local_decl();
				setState(123);
				expr(0);
				setState(124);
				match(T__0);
				setState(125);
				expr(0);
				setState(126);
				match(T__6);
				setState(127);
				stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(129);
				match(FOR);
				setState(130);
				match(T__5);
				setState(131);
				local_decl();
				setState(132);
				expr(0);
				setState(133);
				match(T__0);
				setState(134);
				match(T__6);
				setState(135);
				stmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(137);
				match(FOR);
				setState(138);
				match(T__5);
				setState(139);
				match(T__0);
				setState(140);
				expr(0);
				setState(141);
				match(T__0);
				setState(142);
				expr(0);
				setState(143);
				match(T__6);
				setState(144);
				stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Compound_stmtContext extends ParserRuleContext {
		public List<Local_declContext> local_decl() {
			return getRuleContexts(Local_declContext.class);
		}
		public Local_declContext local_decl(int i) {
			return getRuleContext(Local_declContext.class,i);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Compound_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterCompound_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitCompound_stmt(this);
		}
	}

	public final Compound_stmtContext compound_stmt() throws RecognitionException {
		Compound_stmtContext _localctx = new Compound_stmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_compound_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(T__8);
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << VOID) | (1L << INT) | (1L << CHAR) | (1L << DOUBLE))) != 0)) {
				{
				{
				setState(149);
				local_decl();
				}
				}
				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__8) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__19) | (1L << WHILE) | (1L << FOR) | (1L << IF) | (1L << RETURN) | (1L << IDENT) | (1L << DOUBLE_Lit) | (1L << LITERAL))) != 0)) {
				{
				{
				setState(155);
				stmt();
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Local_declContext extends ParserRuleContext {
		public Type_specContext type_spec() {
			return getRuleContext(Type_specContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(MiniCParser.IDENT, 0); }
		public TerminalNode LITERAL() { return getToken(MiniCParser.LITERAL, 0); }
		public TerminalNode DOUBLE_Lit() { return getToken(MiniCParser.DOUBLE_Lit, 0); }
		public Array_init_valContext array_init_val() {
			return getRuleContext(Array_init_valContext.class,0);
		}
		public TerminalNode CHARACTER() { return getToken(MiniCParser.CHARACTER, 0); }
		public Local_declContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_local_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterLocal_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitLocal_decl(this);
		}
	}

	public final Local_declContext local_decl() throws RecognitionException {
		Local_declContext _localctx = new Local_declContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_local_decl);
		int _la;
		try {
			setState(209);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				type_spec();
				setState(164);
				match(IDENT);
				setState(165);
				match(T__0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				type_spec();
				setState(168);
				match(IDENT);
				setState(169);
				match(T__1);
				setState(170);
				_la = _input.LA(1);
				if ( !(_la==DOUBLE_Lit || _la==LITERAL) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(171);
				match(T__0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(173);
				type_spec();
				setState(174);
				match(IDENT);
				setState(175);
				match(T__2);
				setState(176);
				match(LITERAL);
				setState(177);
				match(T__3);
				setState(178);
				match(T__0);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(180);
				type_spec();
				setState(181);
				match(IDENT);
				setState(182);
				match(T__2);
				setState(183);
				match(T__3);
				setState(184);
				match(T__1);
				setState(185);
				match(T__8);
				setState(186);
				array_init_val();
				setState(187);
				match(T__9);
				setState(188);
				match(T__0);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(190);
				type_spec();
				setState(191);
				match(IDENT);
				setState(192);
				match(T__2);
				setState(193);
				match(LITERAL);
				setState(194);
				match(T__3);
				setState(195);
				match(T__1);
				setState(196);
				match(T__8);
				setState(197);
				array_init_val();
				setState(198);
				match(T__9);
				setState(199);
				match(T__0);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(201);
				type_spec();
				setState(202);
				match(IDENT);
				setState(203);
				match(T__1);
				setState(204);
				match(T__4);
				setState(205);
				match(CHARACTER);
				setState(206);
				match(T__4);
				setState(207);
				match(T__0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_init_valContext extends ParserRuleContext {
		public List<TerminalNode> LITERAL() { return getTokens(MiniCParser.LITERAL); }
		public TerminalNode LITERAL(int i) {
			return getToken(MiniCParser.LITERAL, i);
		}
		public Array_init_valContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_init_val; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterArray_init_val(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitArray_init_val(this);
		}
	}

	public final Array_init_valContext array_init_val() throws RecognitionException {
		Array_init_valContext _localctx = new Array_init_valContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_array_init_val);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(LITERAL);
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(212);
				match(T__7);
				setState(213);
				match(LITERAL);
				}
				}
				setState(218);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_stmtContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(MiniCParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MiniCParser.ELSE, 0); }
		public If_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterIf_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitIf_stmt(this);
		}
	}

	public final If_stmtContext if_stmt() throws RecognitionException {
		If_stmtContext _localctx = new If_stmtContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_if_stmt);
		try {
			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(219);
				match(IF);
				setState(220);
				match(T__5);
				setState(221);
				expr(0);
				setState(222);
				match(T__6);
				setState(223);
				stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(225);
				match(IF);
				setState(226);
				match(T__5);
				setState(227);
				expr(0);
				setState(228);
				match(T__6);
				setState(229);
				stmt();
				setState(230);
				match(ELSE);
				setState(231);
				stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Return_stmtContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(MiniCParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Return_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterReturn_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitReturn_stmt(this);
		}
	}

	public final Return_stmtContext return_stmt() throws RecognitionException {
		Return_stmtContext _localctx = new Return_stmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_return_stmt);
		try {
			setState(241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(235);
				match(RETURN);
				setState(236);
				match(T__0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(237);
				match(RETURN);
				setState(238);
				expr(0);
				setState(239);
				match(T__0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public TerminalNode LITERAL() { return getToken(MiniCParser.LITERAL, 0); }
		public TerminalNode DOUBLE_Lit() { return getToken(MiniCParser.DOUBLE_Lit, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode IDENT() { return getToken(MiniCParser.IDENT, 0); }
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public TerminalNode EQ() { return getToken(MiniCParser.EQ, 0); }
		public TerminalNode NE() { return getToken(MiniCParser.NE, 0); }
		public TerminalNode LE() { return getToken(MiniCParser.LE, 0); }
		public TerminalNode GE() { return getToken(MiniCParser.GE, 0); }
		public TerminalNode AND() { return getToken(MiniCParser.AND, 0); }
		public TerminalNode OR() { return getToken(MiniCParser.OR, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(244);
				match(LITERAL);
				}
				break;
			case 2:
				{
				setState(245);
				match(DOUBLE_Lit);
				}
				break;
			case 3:
				{
				setState(246);
				match(T__5);
				setState(247);
				expr(0);
				setState(248);
				match(T__6);
				}
				break;
			case 4:
				{
				setState(250);
				match(IDENT);
				}
				break;
			case 5:
				{
				setState(251);
				match(IDENT);
				setState(252);
				match(T__2);
				setState(253);
				expr(0);
				setState(254);
				match(T__3);
				}
				break;
			case 6:
				{
				setState(256);
				match(IDENT);
				setState(257);
				match(T__5);
				setState(258);
				args();
				setState(259);
				match(T__6);
				}
				break;
			case 7:
				{
				setState(261);
				match(T__10);
				setState(262);
				expr(20);
				}
				break;
			case 8:
				{
				setState(263);
				match(T__11);
				setState(264);
				expr(19);
				}
				break;
			case 9:
				{
				setState(265);
				match(T__12);
				setState(266);
				expr(18);
				}
				break;
			case 10:
				{
				setState(267);
				match(T__13);
				setState(268);
				expr(17);
				}
				break;
			case 11:
				{
				setState(269);
				match(T__19);
				setState(270);
				expr(5);
				}
				break;
			case 12:
				{
				setState(271);
				match(IDENT);
				setState(272);
				match(T__1);
				setState(273);
				expr(2);
				}
				break;
			case 13:
				{
				setState(274);
				match(IDENT);
				setState(275);
				match(T__2);
				setState(276);
				expr(0);
				setState(277);
				match(T__3);
				setState(278);
				match(T__1);
				setState(279);
				expr(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(324);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(322);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(283);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(284);
						match(T__14);
						setState(285);
						expr(17);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(286);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(287);
						match(T__15);
						setState(288);
						expr(16);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(289);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(290);
						match(T__16);
						setState(291);
						expr(15);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(292);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(293);
						match(T__11);
						setState(294);
						expr(14);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(295);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(296);
						match(T__10);
						setState(297);
						expr(13);
						}
						break;
					case 6:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(298);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(299);
						match(EQ);
						setState(300);
						expr(12);
						}
						break;
					case 7:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(301);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(302);
						match(NE);
						setState(303);
						expr(11);
						}
						break;
					case 8:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(304);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(305);
						match(LE);
						setState(306);
						expr(10);
						}
						break;
					case 9:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(307);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(308);
						match(T__17);
						setState(309);
						expr(9);
						}
						break;
					case 10:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(310);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(311);
						match(GE);
						setState(312);
						expr(8);
						}
						break;
					case 11:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(313);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(314);
						match(T__18);
						setState(315);
						expr(7);
						}
						break;
					case 12:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(316);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(317);
						match(AND);
						setState(318);
						expr(5);
						}
						break;
					case 13:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(319);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(320);
						match(OR);
						setState(321);
						expr(4);
						}
						break;
					}
					} 
				}
				setState(326);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ArgsContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).enterArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniCListener ) ((MiniCListener)listener).exitArgs(this);
		}
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_args);
		int _la;
		try {
			setState(336);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__19:
			case IDENT:
			case DOUBLE_Lit:
			case LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(327);
				expr(0);
				setState(332);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(328);
					match(T__7);
					setState(329);
					expr(0);
					}
					}
					setState(334);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 16:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 16);
		case 1:
			return precpred(_ctx, 15);
		case 2:
			return precpred(_ctx, 14);
		case 3:
			return precpred(_ctx, 13);
		case 4:
			return precpred(_ctx, 12);
		case 5:
			return precpred(_ctx, 11);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 9);
		case 8:
			return precpred(_ctx, 8);
		case 9:
			return precpred(_ctx, 7);
		case 10:
			return precpred(_ctx, 6);
		case 11:
			return precpred(_ctx, 4);
		case 12:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3-\u0155\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\6\2(\n\2\r\2\16\2)\3\3\3\3\5\3.\n\3\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\5\4I\n\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\7"+
		"\7W\n\7\f\7\16\7Z\13\7\3\7\3\7\5\7^\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\5\bh\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5\tp\n\t\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u0095\n\f\3\r\3"+
		"\r\7\r\u0099\n\r\f\r\16\r\u009c\13\r\3\r\7\r\u009f\n\r\f\r\16\r\u00a2"+
		"\13\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00d4\n\16\3\17\3\17\3\17\7\17"+
		"\u00d9\n\17\f\17\16\17\u00dc\13\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00ec\n\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\5\21\u00f4\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\5\22\u011c\n\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\7\22\u0145\n\22\f\22\16\22\u0148\13\22\3\23\3\23\3\23\7\23\u014d"+
		"\n\23\f\23\16\23\u0150\13\23\3\23\5\23\u0153\n\23\3\23\2\3\"\24\2\4\6"+
		"\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\4\3\2()\3\2\27\32\2\u0177\2\'"+
		"\3\2\2\2\4-\3\2\2\2\6H\3\2\2\2\bJ\3\2\2\2\nL\3\2\2\2\f]\3\2\2\2\16g\3"+
		"\2\2\2\20o\3\2\2\2\22q\3\2\2\2\24t\3\2\2\2\26\u0094\3\2\2\2\30\u0096\3"+
		"\2\2\2\32\u00d3\3\2\2\2\34\u00d5\3\2\2\2\36\u00eb\3\2\2\2 \u00f3\3\2\2"+
		"\2\"\u011b\3\2\2\2$\u0152\3\2\2\2&(\5\4\3\2\'&\3\2\2\2()\3\2\2\2)\'\3"+
		"\2\2\2)*\3\2\2\2*\3\3\2\2\2+.\5\6\4\2,.\5\n\6\2-+\3\2\2\2-,\3\2\2\2.\5"+
		"\3\2\2\2/\60\5\b\5\2\60\61\7&\2\2\61\62\7\3\2\2\62I\3\2\2\2\63\64\5\b"+
		"\5\2\64\65\7&\2\2\65\66\7\4\2\2\66\67\t\2\2\2\678\7\3\2\28I\3\2\2\29:"+
		"\5\b\5\2:;\7&\2\2;<\7\5\2\2<=\7)\2\2=>\7\6\2\2>?\7\3\2\2?I\3\2\2\2@A\5"+
		"\b\5\2AB\7&\2\2BC\7\4\2\2CD\7\7\2\2DE\7\'\2\2EF\7\7\2\2FG\7\3\2\2GI\3"+
		"\2\2\2H/\3\2\2\2H\63\3\2\2\2H9\3\2\2\2H@\3\2\2\2I\7\3\2\2\2JK\t\3\2\2"+
		"K\t\3\2\2\2LM\5\b\5\2MN\7&\2\2NO\7\b\2\2OP\5\f\7\2PQ\7\t\2\2QR\5\30\r"+
		"\2R\13\3\2\2\2SX\5\16\b\2TU\7\n\2\2UW\5\16\b\2VT\3\2\2\2WZ\3\2\2\2XV\3"+
		"\2\2\2XY\3\2\2\2Y^\3\2\2\2ZX\3\2\2\2[^\7\27\2\2\\^\3\2\2\2]S\3\2\2\2]"+
		"[\3\2\2\2]\\\3\2\2\2^\r\3\2\2\2_`\5\b\5\2`a\7&\2\2ah\3\2\2\2bc\5\b\5\2"+
		"cd\7&\2\2de\7\5\2\2ef\7\6\2\2fh\3\2\2\2g_\3\2\2\2gb\3\2\2\2h\17\3\2\2"+
		"\2ip\5\22\n\2jp\5\30\r\2kp\5\36\20\2lp\5\24\13\2mp\5\26\f\2np\5 \21\2"+
		"oi\3\2\2\2oj\3\2\2\2ok\3\2\2\2ol\3\2\2\2om\3\2\2\2on\3\2\2\2p\21\3\2\2"+
		"\2qr\5\"\22\2rs\7\3\2\2s\23\3\2\2\2tu\7\33\2\2uv\7\b\2\2vw\5\"\22\2wx"+
		"\7\t\2\2xy\5\20\t\2y\25\3\2\2\2z{\7\34\2\2{|\7\b\2\2|}\5\32\16\2}~\5\""+
		"\22\2~\177\7\3\2\2\177\u0080\5\"\22\2\u0080\u0081\7\t\2\2\u0081\u0082"+
		"\5\20\t\2\u0082\u0095\3\2\2\2\u0083\u0084\7\34\2\2\u0084\u0085\7\b\2\2"+
		"\u0085\u0086\5\32\16\2\u0086\u0087\5\"\22\2\u0087\u0088\7\3\2\2\u0088"+
		"\u0089\7\t\2\2\u0089\u008a\5\20\t\2\u008a\u0095\3\2\2\2\u008b\u008c\7"+
		"\34\2\2\u008c\u008d\7\b\2\2\u008d\u008e\7\3\2\2\u008e\u008f\5\"\22\2\u008f"+
		"\u0090\7\3\2\2\u0090\u0091\5\"\22\2\u0091\u0092\7\t\2\2\u0092\u0093\5"+
		"\20\t\2\u0093\u0095\3\2\2\2\u0094z\3\2\2\2\u0094\u0083\3\2\2\2\u0094\u008b"+
		"\3\2\2\2\u0095\27\3\2\2\2\u0096\u009a\7\13\2\2\u0097\u0099\5\32\16\2\u0098"+
		"\u0097\3\2\2\2\u0099\u009c\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2"+
		"\2\2\u009b\u00a0\3\2\2\2\u009c\u009a\3\2\2\2\u009d\u009f\5\20\t\2\u009e"+
		"\u009d\3\2\2\2\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2"+
		"\2\2\u00a1\u00a3\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a4\7\f\2\2\u00a4"+
		"\31\3\2\2\2\u00a5\u00a6\5\b\5\2\u00a6\u00a7\7&\2\2\u00a7\u00a8\7\3\2\2"+
		"\u00a8\u00d4\3\2\2\2\u00a9\u00aa\5\b\5\2\u00aa\u00ab\7&\2\2\u00ab\u00ac"+
		"\7\4\2\2\u00ac\u00ad\t\2\2\2\u00ad\u00ae\7\3\2\2\u00ae\u00d4\3\2\2\2\u00af"+
		"\u00b0\5\b\5\2\u00b0\u00b1\7&\2\2\u00b1\u00b2\7\5\2\2\u00b2\u00b3\7)\2"+
		"\2\u00b3\u00b4\7\6\2\2\u00b4\u00b5\7\3\2\2\u00b5\u00d4\3\2\2\2\u00b6\u00b7"+
		"\5\b\5\2\u00b7\u00b8\7&\2\2\u00b8\u00b9\7\5\2\2\u00b9\u00ba\7\6\2\2\u00ba"+
		"\u00bb\7\4\2\2\u00bb\u00bc\7\13\2\2\u00bc\u00bd\5\34\17\2\u00bd\u00be"+
		"\7\f\2\2\u00be\u00bf\7\3\2\2\u00bf\u00d4\3\2\2\2\u00c0\u00c1\5\b\5\2\u00c1"+
		"\u00c2\7&\2\2\u00c2\u00c3\7\5\2\2\u00c3\u00c4\7)\2\2\u00c4\u00c5\7\6\2"+
		"\2\u00c5\u00c6\7\4\2\2\u00c6\u00c7\7\13\2\2\u00c7\u00c8\5\34\17\2\u00c8"+
		"\u00c9\7\f\2\2\u00c9\u00ca\7\3\2\2\u00ca\u00d4\3\2\2\2\u00cb\u00cc\5\b"+
		"\5\2\u00cc\u00cd\7&\2\2\u00cd\u00ce\7\4\2\2\u00ce\u00cf\7\7\2\2\u00cf"+
		"\u00d0\7\'\2\2\u00d0\u00d1\7\7\2\2\u00d1\u00d2\7\3\2\2\u00d2\u00d4\3\2"+
		"\2\2\u00d3\u00a5\3\2\2\2\u00d3\u00a9\3\2\2\2\u00d3\u00af\3\2\2\2\u00d3"+
		"\u00b6\3\2\2\2\u00d3\u00c0\3\2\2\2\u00d3\u00cb\3\2\2\2\u00d4\33\3\2\2"+
		"\2\u00d5\u00da\7)\2\2\u00d6\u00d7\7\n\2\2\u00d7\u00d9\7)\2\2\u00d8\u00d6"+
		"\3\2\2\2\u00d9\u00dc\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db"+
		"\35\3\2\2\2\u00dc\u00da\3\2\2\2\u00dd\u00de\7\35\2\2\u00de\u00df\7\b\2"+
		"\2\u00df\u00e0\5\"\22\2\u00e0\u00e1\7\t\2\2\u00e1\u00e2\5\20\t\2\u00e2"+
		"\u00ec\3\2\2\2\u00e3\u00e4\7\35\2\2\u00e4\u00e5\7\b\2\2\u00e5\u00e6\5"+
		"\"\22\2\u00e6\u00e7\7\t\2\2\u00e7\u00e8\5\20\t\2\u00e8\u00e9\7\36\2\2"+
		"\u00e9\u00ea\5\20\t\2\u00ea\u00ec\3\2\2\2\u00eb\u00dd\3\2\2\2\u00eb\u00e3"+
		"\3\2\2\2\u00ec\37\3\2\2\2\u00ed\u00ee\7\37\2\2\u00ee\u00f4\7\3\2\2\u00ef"+
		"\u00f0\7\37\2\2\u00f0\u00f1\5\"\22\2\u00f1\u00f2\7\3\2\2\u00f2\u00f4\3"+
		"\2\2\2\u00f3\u00ed\3\2\2\2\u00f3\u00ef\3\2\2\2\u00f4!\3\2\2\2\u00f5\u00f6"+
		"\b\22\1\2\u00f6\u011c\7)\2\2\u00f7\u011c\7(\2\2\u00f8\u00f9\7\b\2\2\u00f9"+
		"\u00fa\5\"\22\2\u00fa\u00fb\7\t\2\2\u00fb\u011c\3\2\2\2\u00fc\u011c\7"+
		"&\2\2\u00fd\u00fe\7&\2\2\u00fe\u00ff\7\5\2\2\u00ff\u0100\5\"\22\2\u0100"+
		"\u0101\7\6\2\2\u0101\u011c\3\2\2\2\u0102\u0103\7&\2\2\u0103\u0104\7\b"+
		"\2\2\u0104\u0105\5$\23\2\u0105\u0106\7\t\2\2\u0106\u011c\3\2\2\2\u0107"+
		"\u0108\7\r\2\2\u0108\u011c\5\"\22\26\u0109\u010a\7\16\2\2\u010a\u011c"+
		"\5\"\22\25\u010b\u010c\7\17\2\2\u010c\u011c\5\"\22\24\u010d\u010e\7\20"+
		"\2\2\u010e\u011c\5\"\22\23\u010f\u0110\7\26\2\2\u0110\u011c\5\"\22\7\u0111"+
		"\u0112\7&\2\2\u0112\u0113\7\4\2\2\u0113\u011c\5\"\22\4\u0114\u0115\7&"+
		"\2\2\u0115\u0116\7\5\2\2\u0116\u0117\5\"\22\2\u0117\u0118\7\6\2\2\u0118"+
		"\u0119\7\4\2\2\u0119\u011a\5\"\22\3\u011a\u011c\3\2\2\2\u011b\u00f5\3"+
		"\2\2\2\u011b\u00f7\3\2\2\2\u011b\u00f8\3\2\2\2\u011b\u00fc\3\2\2\2\u011b"+
		"\u00fd\3\2\2\2\u011b\u0102\3\2\2\2\u011b\u0107\3\2\2\2\u011b\u0109\3\2"+
		"\2\2\u011b\u010b\3\2\2\2\u011b\u010d\3\2\2\2\u011b\u010f\3\2\2\2\u011b"+
		"\u0111\3\2\2\2\u011b\u0114\3\2\2\2\u011c\u0146\3\2\2\2\u011d\u011e\f\22"+
		"\2\2\u011e\u011f\7\21\2\2\u011f\u0145\5\"\22\23\u0120\u0121\f\21\2\2\u0121"+
		"\u0122\7\22\2\2\u0122\u0145\5\"\22\22\u0123\u0124\f\20\2\2\u0124\u0125"+
		"\7\23\2\2\u0125\u0145\5\"\22\21\u0126\u0127\f\17\2\2\u0127\u0128\7\16"+
		"\2\2\u0128\u0145\5\"\22\20\u0129\u012a\f\16\2\2\u012a\u012b\7\r\2\2\u012b"+
		"\u0145\5\"\22\17\u012c\u012d\f\r\2\2\u012d\u012e\7$\2\2\u012e\u0145\5"+
		"\"\22\16\u012f\u0130\f\f\2\2\u0130\u0131\7%\2\2\u0131\u0145\5\"\22\r\u0132"+
		"\u0133\f\13\2\2\u0133\u0134\7\"\2\2\u0134\u0145\5\"\22\f\u0135\u0136\f"+
		"\n\2\2\u0136\u0137\7\24\2\2\u0137\u0145\5\"\22\13\u0138\u0139\f\t\2\2"+
		"\u0139\u013a\7#\2\2\u013a\u0145\5\"\22\n\u013b\u013c\f\b\2\2\u013c\u013d"+
		"\7\25\2\2\u013d\u0145\5\"\22\t\u013e\u013f\f\6\2\2\u013f\u0140\7!\2\2"+
		"\u0140\u0145\5\"\22\7\u0141\u0142\f\5\2\2\u0142\u0143\7 \2\2\u0143\u0145"+
		"\5\"\22\6\u0144\u011d\3\2\2\2\u0144\u0120\3\2\2\2\u0144\u0123\3\2\2\2"+
		"\u0144\u0126\3\2\2\2\u0144\u0129\3\2\2\2\u0144\u012c\3\2\2\2\u0144\u012f"+
		"\3\2\2\2\u0144\u0132\3\2\2\2\u0144\u0135\3\2\2\2\u0144\u0138\3\2\2\2\u0144"+
		"\u013b\3\2\2\2\u0144\u013e\3\2\2\2\u0144\u0141\3\2\2\2\u0145\u0148\3\2"+
		"\2\2\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147#\3\2\2\2\u0148\u0146"+
		"\3\2\2\2\u0149\u014e\5\"\22\2\u014a\u014b\7\n\2\2\u014b\u014d\5\"\22\2"+
		"\u014c\u014a\3\2\2\2\u014d\u0150\3\2\2\2\u014e\u014c\3\2\2\2\u014e\u014f"+
		"\3\2\2\2\u014f\u0153\3\2\2\2\u0150\u014e\3\2\2\2\u0151\u0153\3\2\2\2\u0152"+
		"\u0149\3\2\2\2\u0152\u0151\3\2\2\2\u0153%\3\2\2\2\25)-HX]go\u0094\u009a"+
		"\u00a0\u00d3\u00da\u00eb\u00f3\u011b\u0144\u0146\u014e\u0152";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}