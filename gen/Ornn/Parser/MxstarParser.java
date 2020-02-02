// Generated from C:/code/Ornn/src/Ornn/Parser\Mxstar.g4 by ANTLR 4.8
package Ornn.Parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxstarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, IntConstant=33, StringConstant=34, NullConstant=35, BoolConstant=36, 
		Int=37, Bool=38, String=39, Null=40, Void=41, True=42, False=43, If=44, 
		Else=45, For=46, While=47, Return=48, Break=49, Continue=50, New=51, Class=52, 
		This=53, Identifier=54, LineComment=55, BlockComment=56, WhiteSpace=57;
	public static final int
		RULE_program = 0, RULE_programSection = 1, RULE_functionDeclaration = 2, 
		RULE_classDeclaration = 3, RULE_variableDeclaration = 4, RULE_parameterDeclarationList = 5, 
		RULE_parameterDeclaration = 6, RULE_variableDeclarationList = 7, RULE_singleVariableDeclaration = 8, 
		RULE_constructiveFunctionDeclaration = 9, RULE_constant = 10, RULE_type = 11, 
		RULE_nonarrayType = 12, RULE_returnType = 13, RULE_newType = 14, RULE_expression = 15, 
		RULE_parameterList = 16, RULE_statement = 17, RULE_block = 18, RULE_conditionStatement = 19, 
		RULE_loopStatement = 20, RULE_controlStatement = 21, RULE_keyword = 22;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "programSection", "functionDeclaration", "classDeclaration", 
			"variableDeclaration", "parameterDeclarationList", "parameterDeclaration", 
			"variableDeclarationList", "singleVariableDeclaration", "constructiveFunctionDeclaration", 
			"constant", "type", "nonarrayType", "returnType", "newType", "expression", 
			"parameterList", "statement", "block", "conditionStatement", "loopStatement", 
			"controlStatement", "keyword"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'{'", "'}'", "';'", "','", "'='", "'['", "']'", 
			"'++'", "'--'", "'.'", "'+'", "'-'", "'!'", "'~'", "'*'", "'/'", "'%'", 
			"'<<'", "'>>'", "'<'", "'<='", "'>'", "'>='", "'!='", "'=='", "'&'", 
			"'^'", "'|'", "'&&'", "'||'", null, null, null, null, "'int'", "'bool'", 
			"'string'", "'null'", "'void'", "'true'", "'false'", "'if'", "'else'", 
			"'for'", "'while'", "'return'", "'break'", "'continue'", "'new'", "'class'", 
			"'this'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "IntConstant", 
			"StringConstant", "NullConstant", "BoolConstant", "Int", "Bool", "String", 
			"Null", "Void", "True", "False", "If", "Else", "For", "While", "Return", 
			"Break", "Continue", "New", "Class", "This", "Identifier", "LineComment", 
			"BlockComment", "WhiteSpace"
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
	public String getGrammarFileName() { return "Mxstar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxstarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxstarParser.EOF, 0); }
		public List<ProgramSectionContext> programSection() {
			return getRuleContexts(ProgramSectionContext.class);
		}
		public ProgramSectionContext programSection(int i) {
			return getRuleContext(ProgramSectionContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << Class) | (1L << Identifier))) != 0)) {
				{
				{
				setState(46);
				programSection();
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(52);
			match(EOF);
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

	public static class ProgramSectionContext extends ParserRuleContext {
		public FunctionDeclarationContext functionDeclaration() {
			return getRuleContext(FunctionDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ProgramSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programSection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterProgramSection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitProgramSection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitProgramSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramSectionContext programSection() throws RecognitionException {
		ProgramSectionContext _localctx = new ProgramSectionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_programSection);
		try {
			setState(57);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				functionDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				classDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				variableDeclaration();
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

	public static class FunctionDeclarationContext extends ParserRuleContext {
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ParameterDeclarationListContext parameterDeclarationList() {
			return getRuleContext(ParameterDeclarationListContext.class,0);
		}
		public FunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterFunctionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitFunctionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclarationContext functionDeclaration() throws RecognitionException {
		FunctionDeclarationContext _localctx = new FunctionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_functionDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			returnType();
			setState(60);
			match(Identifier);
			setState(61);
			match(T__0);
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(62);
				parameterDeclarationList();
				}
			}

			setState(65);
			match(T__1);
			setState(66);
			block();
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

	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(MxstarParser.Class, 0); }
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public List<VariableDeclarationContext> variableDeclaration() {
			return getRuleContexts(VariableDeclarationContext.class);
		}
		public VariableDeclarationContext variableDeclaration(int i) {
			return getRuleContext(VariableDeclarationContext.class,i);
		}
		public List<FunctionDeclarationContext> functionDeclaration() {
			return getRuleContexts(FunctionDeclarationContext.class);
		}
		public FunctionDeclarationContext functionDeclaration(int i) {
			return getRuleContext(FunctionDeclarationContext.class,i);
		}
		public List<ConstructiveFunctionDeclarationContext> constructiveFunctionDeclaration() {
			return getRuleContexts(ConstructiveFunctionDeclarationContext.class);
		}
		public ConstructiveFunctionDeclarationContext constructiveFunctionDeclaration(int i) {
			return getRuleContext(ConstructiveFunctionDeclarationContext.class,i);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterClassDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitClassDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitClassDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(Class);
			setState(69);
			match(Identifier);
			setState(70);
			match(T__2);
			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(74);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(71);
					variableDeclaration();
					}
					break;
				case 2:
					{
					setState(72);
					functionDeclaration();
					}
					break;
				case 3:
					{
					setState(73);
					constructiveFunctionDeclaration();
					}
					break;
				}
				}
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(79);
			match(T__3);
			setState(80);
			match(T__4);
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

	public static class VariableDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclarationListContext variableDeclarationList() {
			return getRuleContext(VariableDeclarationListContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_variableDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			type(0);
			setState(83);
			variableDeclarationList();
			setState(84);
			match(T__4);
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

	public static class ParameterDeclarationListContext extends ParserRuleContext {
		public List<ParameterDeclarationContext> parameterDeclaration() {
			return getRuleContexts(ParameterDeclarationContext.class);
		}
		public ParameterDeclarationContext parameterDeclaration(int i) {
			return getRuleContext(ParameterDeclarationContext.class,i);
		}
		public ParameterDeclarationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclarationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterParameterDeclarationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitParameterDeclarationList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitParameterDeclarationList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationListContext parameterDeclarationList() throws RecognitionException {
		ParameterDeclarationListContext _localctx = new ParameterDeclarationListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parameterDeclarationList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			parameterDeclaration();
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(87);
				match(T__5);
				setState(88);
				parameterDeclaration();
				}
				}
				setState(93);
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

	public static class ParameterDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public ParameterDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterParameterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitParameterDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitParameterDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationContext parameterDeclaration() throws RecognitionException {
		ParameterDeclarationContext _localctx = new ParameterDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parameterDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			type(0);
			setState(95);
			match(Identifier);
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

	public static class VariableDeclarationListContext extends ParserRuleContext {
		public List<SingleVariableDeclarationContext> singleVariableDeclaration() {
			return getRuleContexts(SingleVariableDeclarationContext.class);
		}
		public SingleVariableDeclarationContext singleVariableDeclaration(int i) {
			return getRuleContext(SingleVariableDeclarationContext.class,i);
		}
		public VariableDeclarationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterVariableDeclarationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitVariableDeclarationList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitVariableDeclarationList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationListContext variableDeclarationList() throws RecognitionException {
		VariableDeclarationListContext _localctx = new VariableDeclarationListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_variableDeclarationList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			singleVariableDeclaration();
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(98);
				match(T__5);
				setState(99);
				singleVariableDeclaration();
				}
				}
				setState(104);
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

	public static class SingleVariableDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SingleVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleVariableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterSingleVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitSingleVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitSingleVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleVariableDeclarationContext singleVariableDeclaration() throws RecognitionException {
		SingleVariableDeclarationContext _localctx = new SingleVariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_singleVariableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(Identifier);
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(106);
				match(T__6);
				setState(107);
				expression(0);
				}
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

	public static class ConstructiveFunctionDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ConstructiveFunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructiveFunctionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterConstructiveFunctionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitConstructiveFunctionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitConstructiveFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructiveFunctionDeclarationContext constructiveFunctionDeclaration() throws RecognitionException {
		ConstructiveFunctionDeclarationContext _localctx = new ConstructiveFunctionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_constructiveFunctionDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(Identifier);
			setState(111);
			match(T__0);
			setState(112);
			match(T__1);
			setState(113);
			block();
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

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode StringConstant() { return getToken(MxstarParser.StringConstant, 0); }
		public TerminalNode IntConstant() { return getToken(MxstarParser.IntConstant, 0); }
		public TerminalNode NullConstant() { return getToken(MxstarParser.NullConstant, 0); }
		public TerminalNode BoolConstant() { return getToken(MxstarParser.BoolConstant, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant))) != 0)) ) {
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

	public static class TypeContext extends ParserRuleContext {
		public NonarrayTypeContext nonarrayType() {
			return getRuleContext(NonarrayTypeContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(118);
			nonarrayType();
			}
			_ctx.stop = _input.LT(-1);
			setState(125);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TypeContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(120);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(121);
					match(T__7);
					setState(122);
					match(T__8);
					}
					} 
				}
				setState(127);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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

	public static class NonarrayTypeContext extends ParserRuleContext {
		public TerminalNode Bool() { return getToken(MxstarParser.Bool, 0); }
		public TerminalNode Int() { return getToken(MxstarParser.Int, 0); }
		public TerminalNode Void() { return getToken(MxstarParser.Void, 0); }
		public TerminalNode String() { return getToken(MxstarParser.String, 0); }
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public NonarrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonarrayType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterNonarrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitNonarrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNonarrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonarrayTypeContext nonarrayType() throws RecognitionException {
		NonarrayTypeContext _localctx = new NonarrayTypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_nonarrayType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) ) {
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

	public static class ReturnTypeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Void() { return getToken(MxstarParser.Void, 0); }
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_returnType);
		try {
			setState(132);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(130);
				type(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(131);
				match(Void);
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

	public static class NewTypeContext extends ParserRuleContext {
		public NonarrayTypeContext nonarrayType() {
			return getRuleContext(NonarrayTypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public NewTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterNewType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitNewType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitNewType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewTypeContext newType() throws RecognitionException {
		NewTypeContext _localctx = new NewTypeContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_newType);
		try {
			int _alt;
			setState(178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(134);
				nonarrayType();
				setState(139); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(135);
						match(T__7);
						setState(136);
						expression(0);
						setState(137);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(141); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(145); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(143);
						match(T__7);
						setState(144);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(147); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(153); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(149);
						match(T__7);
						setState(150);
						expression(0);
						setState(151);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(155); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				nonarrayType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(158);
				nonarrayType();
				setState(159);
				match(T__0);
				setState(160);
				match(T__1);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(162);
				nonarrayType();
				setState(167); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(163);
						match(T__7);
						setState(164);
						expression(0);
						setState(165);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(169); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(175);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(171);
						match(T__7);
						setState(172);
						match(T__8);
						}
						} 
					}
					setState(177);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext name;
		public ExpressionContext src1;
		public Token op;
		public ExpressionContext src2;
		public ExpressionContext index;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode New() { return getToken(MxstarParser.New, 0); }
		public NewTypeContext newType() {
			return getRuleContext(NewTypeContext.class,0);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TerminalNode This() { return getToken(MxstarParser.This, 0); }
		public TerminalNode Identifier() { return getToken(MxstarParser.Identifier, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
			case T__10:
				{
				setState(181);
				((ExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
					((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(182);
				expression(20);
				}
				break;
			case T__12:
			case T__13:
				{
				setState(183);
				((ExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
					((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(184);
				expression(19);
				}
				break;
			case T__14:
			case T__15:
				{
				setState(185);
				((ExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__14 || _la==T__15) ) {
					((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(186);
				expression(18);
				}
				break;
			case New:
				{
				setState(187);
				match(New);
				setState(188);
				newType();
				}
				break;
			case IntConstant:
			case StringConstant:
			case NullConstant:
			case BoolConstant:
				{
				setState(189);
				constant();
				}
				break;
			case This:
				{
				setState(190);
				match(This);
				}
				break;
			case Identifier:
				{
				setState(191);
				match(Identifier);
				}
				break;
			case T__0:
				{
				setState(192);
				match(T__0);
				setState(193);
				expression(0);
				setState(194);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(252);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(250);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(198);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(199);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << T__18))) != 0)) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(200);
						((ExpressionContext)_localctx).src2 = expression(17);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(201);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(202);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(203);
						((ExpressionContext)_localctx).src2 = expression(16);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(204);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(205);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__19 || _la==T__20) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(206);
						((ExpressionContext)_localctx).src2 = expression(15);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(207);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(208);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__21 || _la==T__22) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(209);
						((ExpressionContext)_localctx).src2 = expression(14);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(210);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(211);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__23 || _la==T__24) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(212);
						((ExpressionContext)_localctx).src2 = expression(13);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(213);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(214);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__25 || _la==T__26) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(215);
						((ExpressionContext)_localctx).src2 = expression(12);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(216);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(217);
						((ExpressionContext)_localctx).op = match(T__27);
						setState(218);
						((ExpressionContext)_localctx).src2 = expression(11);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(219);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(220);
						((ExpressionContext)_localctx).op = match(T__28);
						setState(221);
						((ExpressionContext)_localctx).src2 = expression(10);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(222);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(223);
						((ExpressionContext)_localctx).op = match(T__29);
						setState(224);
						((ExpressionContext)_localctx).src2 = expression(9);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(225);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(226);
						((ExpressionContext)_localctx).op = match(T__30);
						setState(227);
						((ExpressionContext)_localctx).src2 = expression(8);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(228);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(229);
						((ExpressionContext)_localctx).op = match(T__31);
						setState(230);
						((ExpressionContext)_localctx).src2 = expression(7);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.src1 = _prevctx;
						_localctx.src1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(231);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(232);
						((ExpressionContext)_localctx).op = match(T__6);
						setState(233);
						((ExpressionContext)_localctx).src2 = expression(5);
						}
						break;
					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(234);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(235);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__10) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 14:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(236);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(237);
						match(T__0);
						setState(239);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
							{
							setState(238);
							parameterList();
							}
						}

						setState(241);
						match(T__1);
						}
						break;
					case 15:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.name = _prevctx;
						_localctx.name = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(242);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(243);
						match(T__7);
						setState(244);
						((ExpressionContext)_localctx).index = expression(0);
						setState(245);
						match(T__8);
						}
						break;
					case 16:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(247);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(248);
						match(T__11);
						setState(249);
						match(Identifier);
						}
						break;
					}
					} 
				}
				setState(254);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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

	public static class ParameterListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			expression(0);
			setState(260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(256);
				match(T__5);
				setState(257);
				expression(0);
				}
				}
				setState(262);
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

	public static class StatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConditionStatementContext conditionStatement() {
			return getRuleContext(ConditionStatementContext.class,0);
		}
		public LoopStatementContext loopStatement() {
			return getRuleContext(LoopStatementContext.class,0);
		}
		public ControlStatementContext controlStatement() {
			return getRuleContext(ControlStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_statement);
		try {
			setState(272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(263);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(264);
				variableDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(265);
				expression(0);
				setState(266);
				match(T__4);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(268);
				conditionStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(269);
				loopStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(270);
				controlStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(271);
				match(T__4);
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

	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(T__2);
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__4) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << Int) | (1L << Bool) | (1L << String) | (1L << Void) | (1L << If) | (1L << For) | (1L << While) | (1L << Return) | (1L << Break) | (1L << Continue) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				{
				setState(275);
				statement();
				}
				}
				setState(280);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(281);
			match(T__3);
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

	public static class ConditionStatementContext extends ParserRuleContext {
		public StatementContext thenStmt;
		public StatementContext elseStmt;
		public TerminalNode If() { return getToken(MxstarParser.If, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode Else() { return getToken(MxstarParser.Else, 0); }
		public ConditionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterConditionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitConditionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitConditionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionStatementContext conditionStatement() throws RecognitionException {
		ConditionStatementContext _localctx = new ConditionStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_conditionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(If);
			setState(284);
			match(T__0);
			setState(285);
			expression(0);
			setState(286);
			match(T__1);
			setState(287);
			((ConditionStatementContext)_localctx).thenStmt = statement();
			setState(290);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(288);
				match(Else);
				setState(289);
				((ConditionStatementContext)_localctx).elseStmt = statement();
				}
				break;
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

	public static class LoopStatementContext extends ParserRuleContext {
		public ExpressionContext init;
		public ExpressionContext cond;
		public ExpressionContext step;
		public TerminalNode For() { return getToken(MxstarParser.For, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode While() { return getToken(MxstarParser.While, 0); }
		public LoopStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterLoopStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitLoopStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitLoopStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopStatementContext loopStatement() throws RecognitionException {
		LoopStatementContext _localctx = new LoopStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_loopStatement);
		int _la;
		try {
			setState(313);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case For:
				enterOuterAlt(_localctx, 1);
				{
				setState(292);
				match(For);
				setState(293);
				match(T__0);
				setState(295);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
					{
					setState(294);
					((LoopStatementContext)_localctx).init = expression(0);
					}
				}

				setState(297);
				match(T__4);
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
					{
					setState(298);
					((LoopStatementContext)_localctx).cond = expression(0);
					}
				}

				setState(301);
				match(T__4);
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
					{
					setState(302);
					((LoopStatementContext)_localctx).step = expression(0);
					}
				}

				setState(305);
				match(T__1);
				setState(306);
				statement();
				}
				break;
			case While:
				enterOuterAlt(_localctx, 2);
				{
				setState(307);
				match(While);
				setState(308);
				match(T__0);
				setState(309);
				expression(0);
				setState(310);
				match(T__1);
				setState(311);
				statement();
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

	public static class ControlStatementContext extends ParserRuleContext {
		public TerminalNode Return() { return getToken(MxstarParser.Return, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Break() { return getToken(MxstarParser.Break, 0); }
		public TerminalNode Continue() { return getToken(MxstarParser.Continue, 0); }
		public ControlStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterControlStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitControlStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitControlStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlStatementContext controlStatement() throws RecognitionException {
		ControlStatementContext _localctx = new ControlStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_controlStatement);
		int _la;
		try {
			setState(324);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Return:
				enterOuterAlt(_localctx, 1);
				{
				setState(315);
				match(Return);
				setState(317);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << IntConstant) | (1L << StringConstant) | (1L << NullConstant) | (1L << BoolConstant) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
					{
					setState(316);
					expression(0);
					}
				}

				setState(319);
				match(T__4);
				}
				break;
			case Break:
				enterOuterAlt(_localctx, 2);
				{
				setState(320);
				match(Break);
				setState(321);
				match(T__4);
				}
				break;
			case Continue:
				enterOuterAlt(_localctx, 3);
				{
				setState(322);
				match(Continue);
				setState(323);
				match(T__4);
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

	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode Int() { return getToken(MxstarParser.Int, 0); }
		public TerminalNode Bool() { return getToken(MxstarParser.Bool, 0); }
		public TerminalNode String() { return getToken(MxstarParser.String, 0); }
		public TerminalNode Null() { return getToken(MxstarParser.Null, 0); }
		public TerminalNode Void() { return getToken(MxstarParser.Void, 0); }
		public TerminalNode True() { return getToken(MxstarParser.True, 0); }
		public TerminalNode False() { return getToken(MxstarParser.False, 0); }
		public TerminalNode If() { return getToken(MxstarParser.If, 0); }
		public TerminalNode Else() { return getToken(MxstarParser.Else, 0); }
		public TerminalNode For() { return getToken(MxstarParser.For, 0); }
		public TerminalNode While() { return getToken(MxstarParser.While, 0); }
		public TerminalNode Break() { return getToken(MxstarParser.Break, 0); }
		public TerminalNode Continue() { return getToken(MxstarParser.Continue, 0); }
		public TerminalNode Return() { return getToken(MxstarParser.Return, 0); }
		public TerminalNode New() { return getToken(MxstarParser.New, 0); }
		public TerminalNode Class() { return getToken(MxstarParser.Class, 0); }
		public TerminalNode This() { return getToken(MxstarParser.This, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxstarListener ) ((MxstarListener)listener).exitKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxstarVisitor ) return ((MxstarVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_keyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Int) | (1L << Bool) | (1L << String) | (1L << Null) | (1L << Void) | (1L << True) | (1L << False) | (1L << If) | (1L << Else) | (1L << For) | (1L << While) | (1L << Return) | (1L << Break) | (1L << Continue) | (1L << New) | (1L << Class) | (1L << This))) != 0)) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11:
			return type_sempred((TypeContext)_localctx, predIndex);
		case 15:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 16);
		case 2:
			return precpred(_ctx, 15);
		case 3:
			return precpred(_ctx, 14);
		case 4:
			return precpred(_ctx, 13);
		case 5:
			return precpred(_ctx, 12);
		case 6:
			return precpred(_ctx, 11);
		case 7:
			return precpred(_ctx, 10);
		case 8:
			return precpred(_ctx, 9);
		case 9:
			return precpred(_ctx, 8);
		case 10:
			return precpred(_ctx, 7);
		case 11:
			return precpred(_ctx, 6);
		case 12:
			return precpred(_ctx, 5);
		case 13:
			return precpred(_ctx, 24);
		case 14:
			return precpred(_ctx, 23);
		case 15:
			return precpred(_ctx, 22);
		case 16:
			return precpred(_ctx, 21);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3;\u014b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2\7\2\62"+
		"\n\2\f\2\16\2\65\13\2\3\2\3\2\3\3\3\3\3\3\5\3<\n\3\3\4\3\4\3\4\3\4\5\4"+
		"B\n\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\7\5M\n\5\f\5\16\5P\13\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\7\7\\\n\7\f\7\16\7_\13\7\3\b\3\b"+
		"\3\b\3\t\3\t\3\t\7\tg\n\t\f\t\16\tj\13\t\3\n\3\n\3\n\5\no\n\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\7\r~\n\r\f\r\16\r\u0081"+
		"\13\r\3\16\3\16\3\17\3\17\5\17\u0087\n\17\3\20\3\20\3\20\3\20\3\20\6\20"+
		"\u008e\n\20\r\20\16\20\u008f\3\20\3\20\6\20\u0094\n\20\r\20\16\20\u0095"+
		"\3\20\3\20\3\20\3\20\6\20\u009c\n\20\r\20\16\20\u009d\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\20\6\20\u00aa\n\20\r\20\16\20\u00ab\3\20"+
		"\3\20\7\20\u00b0\n\20\f\20\16\20\u00b3\13\20\5\20\u00b5\n\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\5\21\u00c7\n\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\5\21\u00f2\n\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\7\21\u00fd\n\21\f\21\16\21\u0100\13\21\3\22\3\22\3\22\7\22\u0105\n\22"+
		"\f\22\16\22\u0108\13\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5"+
		"\23\u0113\n\23\3\24\3\24\7\24\u0117\n\24\f\24\16\24\u011a\13\24\3\24\3"+
		"\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0125\n\25\3\26\3\26\3\26"+
		"\5\26\u012a\n\26\3\26\3\26\5\26\u012e\n\26\3\26\3\26\5\26\u0132\n\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u013c\n\26\3\27\3\27\5\27"+
		"\u0140\n\27\3\27\3\27\3\27\3\27\3\27\5\27\u0147\n\27\3\30\3\30\3\30\2"+
		"\4\30 \31\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\2\r\3\2#&\5"+
		"\2\')++88\3\2\f\r\3\2\17\20\3\2\21\22\3\2\23\25\3\2\26\27\3\2\30\31\3"+
		"\2\32\33\3\2\34\35\3\2\'\67\2\u016f\2\63\3\2\2\2\4;\3\2\2\2\6=\3\2\2\2"+
		"\bF\3\2\2\2\nT\3\2\2\2\fX\3\2\2\2\16`\3\2\2\2\20c\3\2\2\2\22k\3\2\2\2"+
		"\24p\3\2\2\2\26u\3\2\2\2\30w\3\2\2\2\32\u0082\3\2\2\2\34\u0086\3\2\2\2"+
		"\36\u00b4\3\2\2\2 \u00c6\3\2\2\2\"\u0101\3\2\2\2$\u0112\3\2\2\2&\u0114"+
		"\3\2\2\2(\u011d\3\2\2\2*\u013b\3\2\2\2,\u0146\3\2\2\2.\u0148\3\2\2\2\60"+
		"\62\5\4\3\2\61\60\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64"+
		"\66\3\2\2\2\65\63\3\2\2\2\66\67\7\2\2\3\67\3\3\2\2\28<\5\6\4\29<\5\b\5"+
		"\2:<\5\n\6\2;8\3\2\2\2;9\3\2\2\2;:\3\2\2\2<\5\3\2\2\2=>\5\34\17\2>?\7"+
		"8\2\2?A\7\3\2\2@B\5\f\7\2A@\3\2\2\2AB\3\2\2\2BC\3\2\2\2CD\7\4\2\2DE\5"+
		"&\24\2E\7\3\2\2\2FG\7\66\2\2GH\78\2\2HN\7\5\2\2IM\5\n\6\2JM\5\6\4\2KM"+
		"\5\24\13\2LI\3\2\2\2LJ\3\2\2\2LK\3\2\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2\2"+
		"OQ\3\2\2\2PN\3\2\2\2QR\7\6\2\2RS\7\7\2\2S\t\3\2\2\2TU\5\30\r\2UV\5\20"+
		"\t\2VW\7\7\2\2W\13\3\2\2\2X]\5\16\b\2YZ\7\b\2\2Z\\\5\16\b\2[Y\3\2\2\2"+
		"\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\r\3\2\2\2_]\3\2\2\2`a\5\30\r\2ab\78\2"+
		"\2b\17\3\2\2\2ch\5\22\n\2de\7\b\2\2eg\5\22\n\2fd\3\2\2\2gj\3\2\2\2hf\3"+
		"\2\2\2hi\3\2\2\2i\21\3\2\2\2jh\3\2\2\2kn\78\2\2lm\7\t\2\2mo\5 \21\2nl"+
		"\3\2\2\2no\3\2\2\2o\23\3\2\2\2pq\78\2\2qr\7\3\2\2rs\7\4\2\2st\5&\24\2"+
		"t\25\3\2\2\2uv\t\2\2\2v\27\3\2\2\2wx\b\r\1\2xy\5\32\16\2y\177\3\2\2\2"+
		"z{\f\3\2\2{|\7\n\2\2|~\7\13\2\2}z\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2"+
		"\177\u0080\3\2\2\2\u0080\31\3\2\2\2\u0081\177\3\2\2\2\u0082\u0083\t\3"+
		"\2\2\u0083\33\3\2\2\2\u0084\u0087\5\30\r\2\u0085\u0087\7+\2\2\u0086\u0084"+
		"\3\2\2\2\u0086\u0085\3\2\2\2\u0087\35\3\2\2\2\u0088\u008d\5\32\16\2\u0089"+
		"\u008a\7\n\2\2\u008a\u008b\5 \21\2\u008b\u008c\7\13\2\2\u008c\u008e\3"+
		"\2\2\2\u008d\u0089\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u008d\3\2\2\2\u008f"+
		"\u0090\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u0092\7\n\2\2\u0092\u0094\7\13"+
		"\2\2\u0093\u0091\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0093\3\2\2\2\u0095"+
		"\u0096\3\2\2\2\u0096\u009b\3\2\2\2\u0097\u0098\7\n\2\2\u0098\u0099\5 "+
		"\21\2\u0099\u009a\7\13\2\2\u009a\u009c\3\2\2\2\u009b\u0097\3\2\2\2\u009c"+
		"\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00b5\3\2"+
		"\2\2\u009f\u00b5\5\32\16\2\u00a0\u00a1\5\32\16\2\u00a1\u00a2\7\3\2\2\u00a2"+
		"\u00a3\7\4\2\2\u00a3\u00b5\3\2\2\2\u00a4\u00a9\5\32\16\2\u00a5\u00a6\7"+
		"\n\2\2\u00a6\u00a7\5 \21\2\u00a7\u00a8\7\13\2\2\u00a8\u00aa\3\2\2\2\u00a9"+
		"\u00a5\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2"+
		"\2\2\u00ac\u00b1\3\2\2\2\u00ad\u00ae\7\n\2\2\u00ae\u00b0\7\13\2\2\u00af"+
		"\u00ad\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u0088\3\2\2\2\u00b4"+
		"\u009f\3\2\2\2\u00b4\u00a0\3\2\2\2\u00b4\u00a4\3\2\2\2\u00b5\37\3\2\2"+
		"\2\u00b6\u00b7\b\21\1\2\u00b7\u00b8\t\4\2\2\u00b8\u00c7\5 \21\26\u00b9"+
		"\u00ba\t\5\2\2\u00ba\u00c7\5 \21\25\u00bb\u00bc\t\6\2\2\u00bc\u00c7\5"+
		" \21\24\u00bd\u00be\7\65\2\2\u00be\u00c7\5\36\20\2\u00bf\u00c7\5\26\f"+
		"\2\u00c0\u00c7\7\67\2\2\u00c1\u00c7\78\2\2\u00c2\u00c3\7\3\2\2\u00c3\u00c4"+
		"\5 \21\2\u00c4\u00c5\7\4\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00b6\3\2\2\2\u00c6"+
		"\u00b9\3\2\2\2\u00c6\u00bb\3\2\2\2\u00c6\u00bd\3\2\2\2\u00c6\u00bf\3\2"+
		"\2\2\u00c6\u00c0\3\2\2\2\u00c6\u00c1\3\2\2\2\u00c6\u00c2\3\2\2\2\u00c7"+
		"\u00fe\3\2\2\2\u00c8\u00c9\f\22\2\2\u00c9\u00ca\t\7\2\2\u00ca\u00fd\5"+
		" \21\23\u00cb\u00cc\f\21\2\2\u00cc\u00cd\t\5\2\2\u00cd\u00fd\5 \21\22"+
		"\u00ce\u00cf\f\20\2\2\u00cf\u00d0\t\b\2\2\u00d0\u00fd\5 \21\21\u00d1\u00d2"+
		"\f\17\2\2\u00d2\u00d3\t\t\2\2\u00d3\u00fd\5 \21\20\u00d4\u00d5\f\16\2"+
		"\2\u00d5\u00d6\t\n\2\2\u00d6\u00fd\5 \21\17\u00d7\u00d8\f\r\2\2\u00d8"+
		"\u00d9\t\13\2\2\u00d9\u00fd\5 \21\16\u00da\u00db\f\f\2\2\u00db\u00dc\7"+
		"\36\2\2\u00dc\u00fd\5 \21\r\u00dd\u00de\f\13\2\2\u00de\u00df\7\37\2\2"+
		"\u00df\u00fd\5 \21\f\u00e0\u00e1\f\n\2\2\u00e1\u00e2\7 \2\2\u00e2\u00fd"+
		"\5 \21\13\u00e3\u00e4\f\t\2\2\u00e4\u00e5\7!\2\2\u00e5\u00fd\5 \21\n\u00e6"+
		"\u00e7\f\b\2\2\u00e7\u00e8\7\"\2\2\u00e8\u00fd\5 \21\t\u00e9\u00ea\f\7"+
		"\2\2\u00ea\u00eb\7\t\2\2\u00eb\u00fd\5 \21\7\u00ec\u00ed\f\32\2\2\u00ed"+
		"\u00fd\t\4\2\2\u00ee\u00ef\f\31\2\2\u00ef\u00f1\7\3\2\2\u00f0\u00f2\5"+
		"\"\22\2\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3"+
		"\u00fd\7\4\2\2\u00f4\u00f5\f\30\2\2\u00f5\u00f6\7\n\2\2\u00f6\u00f7\5"+
		" \21\2\u00f7\u00f8\7\13\2\2\u00f8\u00fd\3\2\2\2\u00f9\u00fa\f\27\2\2\u00fa"+
		"\u00fb\7\16\2\2\u00fb\u00fd\78\2\2\u00fc\u00c8\3\2\2\2\u00fc\u00cb\3\2"+
		"\2\2\u00fc\u00ce\3\2\2\2\u00fc\u00d1\3\2\2\2\u00fc\u00d4\3\2\2\2\u00fc"+
		"\u00d7\3\2\2\2\u00fc\u00da\3\2\2\2\u00fc\u00dd\3\2\2\2\u00fc\u00e0\3\2"+
		"\2\2\u00fc\u00e3\3\2\2\2\u00fc\u00e6\3\2\2\2\u00fc\u00e9\3\2\2\2\u00fc"+
		"\u00ec\3\2\2\2\u00fc\u00ee\3\2\2\2\u00fc\u00f4\3\2\2\2\u00fc\u00f9\3\2"+
		"\2\2\u00fd\u0100\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff"+
		"!\3\2\2\2\u0100\u00fe\3\2\2\2\u0101\u0106\5 \21\2\u0102\u0103\7\b\2\2"+
		"\u0103\u0105\5 \21\2\u0104\u0102\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104"+
		"\3\2\2\2\u0106\u0107\3\2\2\2\u0107#\3\2\2\2\u0108\u0106\3\2\2\2\u0109"+
		"\u0113\5&\24\2\u010a\u0113\5\n\6\2\u010b\u010c\5 \21\2\u010c\u010d\7\7"+
		"\2\2\u010d\u0113\3\2\2\2\u010e\u0113\5(\25\2\u010f\u0113\5*\26\2\u0110"+
		"\u0113\5,\27\2\u0111\u0113\7\7\2\2\u0112\u0109\3\2\2\2\u0112\u010a\3\2"+
		"\2\2\u0112\u010b\3\2\2\2\u0112\u010e\3\2\2\2\u0112\u010f\3\2\2\2\u0112"+
		"\u0110\3\2\2\2\u0112\u0111\3\2\2\2\u0113%\3\2\2\2\u0114\u0118\7\5\2\2"+
		"\u0115\u0117\5$\23\2\u0116\u0115\3\2\2\2\u0117\u011a\3\2\2\2\u0118\u0116"+
		"\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011b\3\2\2\2\u011a\u0118\3\2\2\2\u011b"+
		"\u011c\7\6\2\2\u011c\'\3\2\2\2\u011d\u011e\7.\2\2\u011e\u011f\7\3\2\2"+
		"\u011f\u0120\5 \21\2\u0120\u0121\7\4\2\2\u0121\u0124\5$\23\2\u0122\u0123"+
		"\7/\2\2\u0123\u0125\5$\23\2\u0124\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125"+
		")\3\2\2\2\u0126\u0127\7\60\2\2\u0127\u0129\7\3\2\2\u0128\u012a\5 \21\2"+
		"\u0129\u0128\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u012d"+
		"\7\7\2\2\u012c\u012e\5 \21\2\u012d\u012c\3\2\2\2\u012d\u012e\3\2\2\2\u012e"+
		"\u012f\3\2\2\2\u012f\u0131\7\7\2\2\u0130\u0132\5 \21\2\u0131\u0130\3\2"+
		"\2\2\u0131\u0132\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0134\7\4\2\2\u0134"+
		"\u013c\5$\23\2\u0135\u0136\7\61\2\2\u0136\u0137\7\3\2\2\u0137\u0138\5"+
		" \21\2\u0138\u0139\7\4\2\2\u0139\u013a\5$\23\2\u013a\u013c\3\2\2\2\u013b"+
		"\u0126\3\2\2\2\u013b\u0135\3\2\2\2\u013c+\3\2\2\2\u013d\u013f\7\62\2\2"+
		"\u013e\u0140\5 \21\2\u013f\u013e\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0141"+
		"\3\2\2\2\u0141\u0147\7\7\2\2\u0142\u0143\7\63\2\2\u0143\u0147\7\7\2\2"+
		"\u0144\u0145\7\64\2\2\u0145\u0147\7\7\2\2\u0146\u013d\3\2\2\2\u0146\u0142"+
		"\3\2\2\2\u0146\u0144\3\2\2\2\u0147-\3\2\2\2\u0148\u0149\t\f\2\2\u0149"+
		"/\3\2\2\2 \63;ALN]hn\177\u0086\u008f\u0095\u009d\u00ab\u00b1\u00b4\u00c6"+
		"\u00f1\u00fc\u00fe\u0106\u0112\u0118\u0124\u0129\u012d\u0131\u013b\u013f"+
		"\u0146";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}