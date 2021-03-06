// created by jay 1.0.2 (c) 2002-2004 ats@cs.rit.edu
// skeleton Java 1.0 (c) 2002 ats@cs.rit.edu

// line 2 "Ruby19Parser.y"
/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2008-2009 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jrubyparser.parser;

import java.io.IOException;

import org.jrubyparser.BlockStaticScope;
import org.jrubyparser.IRubyWarnings;
import org.jrubyparser.IRubyWarnings.ID;
import org.jrubyparser.ISourcePositionHolder;
import org.jrubyparser.SourcePosition;
import org.jrubyparser.ast.ArgsNode;
import org.jrubyparser.ast.ArrayNode;
import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.BackRefNode;
import org.jrubyparser.ast.BeginNode;
import org.jrubyparser.ast.BlockAcceptingNode;
import org.jrubyparser.ast.BlockArgNode;
import org.jrubyparser.ast.BlockNode;
import org.jrubyparser.ast.BlockPassNode;
import org.jrubyparser.ast.BreakNode;
import org.jrubyparser.ast.CallNode;
import org.jrubyparser.ast.ClassNode;
import org.jrubyparser.ast.ClassVarNode;
import org.jrubyparser.ast.Colon3Node;
import org.jrubyparser.ast.ConstDeclNode;
import org.jrubyparser.ast.DStrNode;
import org.jrubyparser.ast.DSymbolNode;
import org.jrubyparser.ast.DXStrNode;
import org.jrubyparser.ast.DefinedNode;
import org.jrubyparser.ast.DefnNode;
import org.jrubyparser.ast.DefsNode;
import org.jrubyparser.ast.DotNode;
import org.jrubyparser.ast.EnsureNode;
import org.jrubyparser.ast.EvStrNode;
import org.jrubyparser.ast.FixnumNode;
import org.jrubyparser.ast.FloatNode;
import org.jrubyparser.ast.ForNode;
import org.jrubyparser.ast.GlobalVarNode;
import org.jrubyparser.ast.HashNode;
import org.jrubyparser.ast.IArgumentNode;
import org.jrubyparser.ast.ILiteralNode;
import org.jrubyparser.ast.IfNode;
import org.jrubyparser.ast.ImplicitNilNode;
import org.jrubyparser.ast.InstVarNode;
import org.jrubyparser.ast.IterNode;
import org.jrubyparser.ast.LambdaNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LiteralNode;
import org.jrubyparser.ast.MethodNameNode;
import org.jrubyparser.ast.ModuleNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.NextNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.NotNode;
import org.jrubyparser.ast.OpAsgnAndNode;
import org.jrubyparser.ast.OpAsgnNode;
import org.jrubyparser.ast.OpAsgnOrNode;
import org.jrubyparser.ast.OptArgNode;
import org.jrubyparser.ast.PostExeNode;
import org.jrubyparser.ast.PreExe19Node;
import org.jrubyparser.ast.RedoNode;
import org.jrubyparser.ast.RegexpNode;
import org.jrubyparser.ast.RescueBodyNode;
import org.jrubyparser.ast.RescueNode;
import org.jrubyparser.ast.RestArgNode;
import org.jrubyparser.ast.RetryNode;
import org.jrubyparser.ast.ReturnNode;
import org.jrubyparser.ast.SClassNode;
import org.jrubyparser.ast.SelfNode;
import org.jrubyparser.ast.StarNode;
import org.jrubyparser.ast.StrNode;
import org.jrubyparser.ast.SymbolNode;
import org.jrubyparser.ast.UnnamedRestArgNode;
import org.jrubyparser.ast.UntilNode;
import org.jrubyparser.ast.VAliasNode;
import org.jrubyparser.ast.WhileNode;
import org.jrubyparser.ast.XStrNode;
import org.jrubyparser.ast.YieldNode;
import org.jrubyparser.ast.ZArrayNode;
import org.jrubyparser.ast.ZSuperNode;
import org.jrubyparser.ast.ZYieldNode;
import org.jrubyparser.lexer.Lexer;
import org.jrubyparser.lexer.Lexer.LexState;
import org.jrubyparser.lexer.LexerSource;
import org.jrubyparser.lexer.StrTerm;
import org.jrubyparser.lexer.SyntaxException;
import org.jrubyparser.lexer.SyntaxException.PID;
import org.jrubyparser.lexer.Token;

public class Ruby19Parser implements RubyParser {
	protected ParserSupport19 support;
	protected Lexer lexer;

	public Ruby19Parser() {
		this(new ParserSupport19());
	}

	public Ruby19Parser(final ParserSupport19 support) {
		this.support = support;
		lexer = new Lexer(false);
		lexer.setParserSupport(support);
		support.setLexer(lexer);
	}

	@Override
	public void setWarnings(final IRubyWarnings warnings) {
		support.setWarnings(warnings);
		lexer.setWarnings(warnings);
	}

	// line 141 "-"
	// %token constants
	public static final int kCLASS = 257;
	public static final int kMODULE = 258;
	public static final int kDEF = 259;
	public static final int kUNDEF = 260;
	public static final int kBEGIN = 261;
	public static final int kRESCUE = 262;
	public static final int kENSURE = 263;
	public static final int kEND = 264;
	public static final int kIF = 265;
	public static final int kUNLESS = 266;
	public static final int kTHEN = 267;
	public static final int kELSIF = 268;
	public static final int kELSE = 269;
	public static final int kCASE = 270;
	public static final int kWHEN = 271;
	public static final int kWHILE = 272;
	public static final int kUNTIL = 273;
	public static final int kFOR = 274;
	public static final int kBREAK = 275;
	public static final int kNEXT = 276;
	public static final int kREDO = 277;
	public static final int kRETRY = 278;
	public static final int kIN = 279;
	public static final int kDO = 280;
	public static final int kDO_COND = 281;
	public static final int kDO_BLOCK = 282;
	public static final int kRETURN = 283;
	public static final int kYIELD = 284;
	public static final int kSUPER = 285;
	public static final int kSELF = 286;
	public static final int kNIL = 287;
	public static final int kTRUE = 288;
	public static final int kFALSE = 289;
	public static final int kAND = 290;
	public static final int kOR = 291;
	public static final int kNOT = 292;
	public static final int kIF_MOD = 293;
	public static final int kUNLESS_MOD = 294;
	public static final int kWHILE_MOD = 295;
	public static final int kUNTIL_MOD = 296;
	public static final int kRESCUE_MOD = 297;
	public static final int kALIAS = 298;
	public static final int kDEFINED = 299;
	public static final int klBEGIN = 300;
	public static final int klEND = 301;
	public static final int k__LINE__ = 302;
	public static final int k__FILE__ = 303;
	public static final int k__ENCODING__ = 304;
	public static final int kDO_LAMBDA = 305;
	public static final int tIDENTIFIER = 306;
	public static final int tFID = 307;
	public static final int tGVAR = 308;
	public static final int tIVAR = 309;
	public static final int tCONSTANT = 310;
	public static final int tCVAR = 311;
	public static final int tLABEL = 312;
	public static final int tCHAR = 313;
	public static final int tUPLUS = 314;
	public static final int tUMINUS = 315;
	public static final int tUMINUS_NUM = 316;
	public static final int tPOW = 317;
	public static final int tCMP = 318;
	public static final int tEQ = 319;
	public static final int tEQQ = 320;
	public static final int tNEQ = 321;
	public static final int tGEQ = 322;
	public static final int tLEQ = 323;
	public static final int tANDOP = 324;
	public static final int tOROP = 325;
	public static final int tMATCH = 326;
	public static final int tNMATCH = 327;
	public static final int tDOT = 328;
	public static final int tDOT2 = 329;
	public static final int tDOT3 = 330;
	public static final int tAREF = 331;
	public static final int tASET = 332;
	public static final int tLSHFT = 333;
	public static final int tRSHFT = 334;
	public static final int tCOLON2 = 335;
	public static final int tCOLON3 = 336;
	public static final int tOP_ASGN = 337;
	public static final int tASSOC = 338;
	public static final int tLPAREN = 339;
	public static final int tLPAREN2 = 340;
	public static final int tRPAREN = 341;
	public static final int tLPAREN_ARG = 342;
	public static final int tLBRACK = 343;
	public static final int tRBRACK = 344;
	public static final int tLBRACE = 345;
	public static final int tLBRACE_ARG = 346;
	public static final int tSTAR = 347;
	public static final int tSTAR2 = 348;
	public static final int tAMPER = 349;
	public static final int tAMPER2 = 350;
	public static final int tTILDE = 351;
	public static final int tPERCENT = 352;
	public static final int tDIVIDE = 353;
	public static final int tPLUS = 354;
	public static final int tMINUS = 355;
	public static final int tLT = 356;
	public static final int tGT = 357;
	public static final int tPIPE = 358;
	public static final int tBANG = 359;
	public static final int tCARET = 360;
	public static final int tLCURLY = 361;
	public static final int tRCURLY = 362;
	public static final int tBACK_REF2 = 363;
	public static final int tSYMBEG = 364;
	public static final int tSTRING_BEG = 365;
	public static final int tXSTRING_BEG = 366;
	public static final int tREGEXP_BEG = 367;
	public static final int tWORDS_BEG = 368;
	public static final int tQWORDS_BEG = 369;
	public static final int tSTRING_DBEG = 370;
	public static final int tSTRING_DVAR = 371;
	public static final int tSTRING_END = 372;
	public static final int tLAMBDA = 373;
	public static final int tLAMBEG = 374;
	public static final int tNTH_REF = 375;
	public static final int tBACK_REF = 376;
	public static final int tSTRING_CONTENT = 377;
	public static final int tINTEGER = 378;
	public static final int tFLOAT = 379;
	public static final int tREGEXP_END = 380;
	public static final int tCOMMENT = 381;
	public static final int tWHITESPACE = 382;
	public static final int tDOCUMENTATION = 383;
	public static final int tLOWEST = 384;
	public static final int yyErrorCode = 256;

	/**
	 * number of final state.
	 */
	protected static final int yyFinal = 1;

	/**
	 * parser tables. Order is mandated by <i>jay</i>.
	 */
	protected static final short[] yyLhs = {
			//yyLhs 542
			-1, 117, 0, 34, 33, 35, 35, 35, 35, 120, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 121, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36,
			36, 36, 36, 36, 37, 37, 37, 37, 37, 37, 41, 32, 32, 32, 32, 32, 55, 55, 55, 123, 94, 40, 40, 40, 40, 40, 40, 40, 40, 95, 95,
			106, 106, 96, 96, 96, 96, 96, 96, 96, 96, 96, 96, 67, 67, 81, 81, 85, 85, 68, 68, 68, 68, 68, 68, 68, 68, 73, 73, 73, 73, 73,
			73, 73, 73, 7, 7, 31, 31, 31, 8, 8, 8, 8, 8, 99, 99, 100, 100, 57, 124, 57, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
			9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115,
			115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 38, 38,
			38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38,
			38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 69, 72, 72, 72, 72, 49, 53, 53, 109, 109, 47, 47, 47, 47, 47, 126, 51, 88, 87, 87,
			87, 75, 75, 75, 75, 66, 66, 66, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 127, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
			39, 39, 39, 39, 39, 39, 129, 131, 39, 132, 133, 39, 39, 39, 134, 135, 39, 136, 39, 138, 139, 39, 140, 39, 141, 39, 142, 143, 39,
			39, 39, 39, 39, 42, 128, 128, 128, 130, 130, 45, 45, 43, 43, 108, 108, 110, 110, 80, 80, 111, 111, 111, 111, 111, 111, 111, 111,
			111, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 65, 65, 64, 64, 64, 103, 103, 102, 102, 112, 112, 144, 105, 62,
			62, 104, 104, 145, 93, 54, 54, 54, 24, 24, 24, 24, 24, 24, 24, 24, 24, 146, 92, 147, 92, 70, 44, 44, 97, 97, 71, 71, 71, 46, 46,
			48, 48, 28, 28, 28, 16, 17, 17, 17, 18, 19, 20, 25, 25, 77, 77, 27, 27, 26, 26, 76, 76, 21, 21, 22, 22, 23, 148, 23, 149, 23,
			58, 58, 58, 58, 3, 2, 2, 2, 2, 30, 29, 29, 29, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 52, 98, 59, 59, 50, 150, 50, 50, 61, 61,
			60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 116, 116, 116, 116, 10, 10, 101, 101, 78, 78, 56, 107, 86, 86, 79,
			79, 12, 12, 14, 14, 13, 13, 91, 90, 90, 15, 151, 15, 84, 84, 82, 82, 83, 83, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 11, 11, 118, 118,
			122, 122, 113, 114, 125, 125, 125, 137, 137, 119, 119, 74, 89, },
			yyLen = {
					//yyLen 542
					2, 0, 2, 4, 2, 1, 1, 3, 2, 0, 4, 3, 3, 3, 2, 3, 3, 3, 3, 3, 0, 5, 4, 3, 3, 3, 6, 5, 5, 5, 3, 3, 3, 3, 1, 1, 3, 3, 3, 2,
					1, 1, 1, 1, 2, 2, 2, 1, 4, 4, 0, 5, 2, 3, 4, 5, 4, 5, 2, 2, 1, 3, 1, 3, 1, 2, 3, 5, 2, 4, 2, 4, 1, 3, 1, 3, 2, 3, 1, 3,
					1, 4, 3, 3, 3, 3, 2, 1, 1, 4, 3, 3, 3, 3, 2, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 4, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 5, 3, 5, 6, 5, 5, 5, 5, 4, 3, 3, 3, 3, 3, 3,
					3, 3, 3, 4, 4, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 3, 3, 3, 3, 3, 6, 1, 1, 1, 2, 4, 2, 3, 1, 1, 1, 1, 1,
					2, 2, 4, 1, 0, 2, 2, 2, 1, 1, 1, 2, 3, 4, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 4, 3, 3, 2, 3, 3, 1, 4, 3, 1, 5, 4,
					3, 2, 1, 2, 2, 6, 6, 0, 0, 7, 0, 0, 7, 5, 4, 0, 0, 9, 0, 6, 0, 0, 8, 0, 5, 0, 6, 0, 0, 9, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1,
					1, 5, 1, 2, 1, 1, 1, 3, 1, 3, 1, 4, 6, 3, 5, 2, 4, 1, 3, 6, 8, 4, 6, 4, 2, 6, 2, 4, 6, 2, 4, 2, 4, 1, 1, 1, 3, 1, 4, 1,
					4, 1, 3, 1, 1, 0, 3, 4, 2, 3, 3, 0, 5, 2, 4, 4, 2, 4, 4, 3, 3, 3, 2, 1, 4, 0, 5, 0, 5, 5, 1, 1, 6, 0, 1, 1, 1, 2, 1, 2,
					1, 1, 1, 1, 1, 1, 1, 2, 3, 3, 3, 3, 3, 0, 3, 1, 2, 3, 3, 0, 3, 0, 2, 0, 2, 1, 0, 3, 0, 4, 1, 1, 1, 1, 2, 1, 1, 1, 1, 3,
					1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 4, 2, 3, 2, 6, 8, 4, 6, 4, 6, 2, 4, 6, 2, 4, 2, 4, 1,
					0, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 3, 1, 3, 1, 3, 1, 1, 2, 1, 1, 1, 2, 2, 0, 1, 0, 4, 1, 2, 1, 3, 3, 2, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 2, 2, 0, 1, 1, 1, 1, 1, 2, 0, 0, },
			yyDefRed = {
					//yyDefRed 947
					1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 287, 290, 0, 0, 0, 312, 313, 0, 0, 0, 450, 449, 451, 452, 0, 0, 0, 20, 0, 454, 453,
					455, 0, 0, 446, 445, 0, 448, 405, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 421, 423, 423, 0, 0, 365, 458, 459, 440, 441, 0,
					402, 0, 258, 0, 406, 259, 260, 0, 261, 262, 257, 401, 403, 35, 2, 0, 0, 0, 0, 0, 0, 0, 263, 0, 43, 0, 0, 74, 0, 5, 0, 0,
					60, 0, 0, 310, 311, 274, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 456, 0, 99, 0, 314, 0, 264, 303, 152, 163, 153, 176, 149, 169,
					159, 158, 174, 157, 156, 151, 177, 161, 150, 164, 168, 170, 162, 155, 171, 178, 173, 0, 0, 0, 0, 148, 167, 166, 179,
					180, 181, 182, 183, 147, 154, 145, 146, 0, 0, 0, 0, 103, 0, 137, 138, 134, 116, 117, 118, 125, 122, 124, 119, 120, 139,
					140, 126, 127, 507, 131, 130, 115, 136, 133, 132, 128, 129, 123, 121, 113, 135, 114, 141, 305, 104, 0, 506, 105, 172,
					165, 175, 160, 142, 143, 144, 101, 102, 107, 106, 109, 0, 108, 110, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 536, 537, 0, 0, 0,
					538, 0, 0, 0, 0, 0, 0, 324, 325, 0, 0, 0, 0, 0, 0, 239, 45, 0, 0, 0, 511, 243, 46, 44, 0, 59, 0, 0, 382, 58, 0, 530, 0,
					0, 9, 0, 0, 0, 205, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 230, 0, 0, 0, 509, 0, 0, 0, 0, 0, 0, 0, 0, 221, 39, 220, 437, 436,
					438, 434, 435, 0, 0, 0, 0, 0, 0, 0, 0, 284, 0, 387, 385, 376, 0, 281, 407, 283, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 371, 373, 0, 0, 0, 0, 0, 0, 76, 0, 0, 0, 0, 0, 0, 0,
					442, 443, 0, 96, 0, 98, 0, 461, 298, 460, 0, 0, 0, 0, 0, 0, 525, 526, 307, 111, 0, 0, 266, 0, 316, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 539, 0, 0, 0, 0, 0, 0, 295, 514, 251, 246, 0, 0, 240, 249, 0, 241, 0, 276, 0, 245, 238, 237, 0, 0, 280,
					38, 11, 13, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 269, 0, 0, 272, 0, 534, 231, 0, 233, 510, 273, 0, 78, 0, 0, 0, 0, 0,
					428, 426, 439, 425, 424, 408, 422, 409, 410, 411, 412, 415, 0, 417, 418, 0, 0, 483, 482, 481, 484, 0, 0, 498, 497, 502,
					501, 487, 0, 0, 0, 495, 0, 0, 0, 0, 479, 489, 485, 0, 0, 50, 53, 0, 15, 16, 17, 18, 19, 36, 37, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 520, 0, 0, 521, 380, 0, 0, 0, 0, 379, 0, 381, 0, 518, 519,
					0, 0, 30, 0, 0, 23, 0, 31, 250, 0, 0, 0, 0, 77, 24, 33, 0, 25, 0, 0, 463, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0,
					395, 0, 0, 396, 0, 0, 322, 0, 317, 0, 0, 0, 0, 0, 0, 0, 0, 0, 294, 319, 288, 318, 291, 0, 0, 0, 0, 0, 0, 513, 0, 0, 0,
					247, 512, 275, 531, 234, 279, 10, 0, 0, 22, 0, 0, 0, 0, 268, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 414, 416, 420, 0, 486, 0, 0,
					326, 0, 328, 0, 0, 499, 503, 0, 477, 368, 0, 0, 0, 366, 0, 472, 0, 475, 357, 0, 355, 0, 354, 0, 0, 0, 0, 0, 0, 236, 0,
					377, 235, 0, 0, 378, 0, 0, 0, 48, 374, 49, 375, 0, 0, 0, 75, 0, 0, 0, 301, 0, 0, 384, 304, 508, 0, 465, 0, 308, 112, 0,
					0, 398, 323, 0, 3, 400, 0, 320, 0, 0, 0, 0, 0, 0, 293, 0, 0, 0, 0, 0, 0, 253, 242, 278, 21, 232, 79, 0, 0, 430, 431,
					432, 427, 433, 491, 0, 0, 0, 0, 488, 0, 0, 504, 0, 0, 0, 0, 0, 490, 0, 496, 0, 0, 0, 0, 0, 0, 353, 0, 493, 0, 0, 0, 0,
					0, 27, 0, 28, 0, 55, 29, 0, 0, 57, 0, 532, 0, 0, 0, 0, 0, 0, 462, 299, 464, 306, 0, 0, 0, 0, 0, 397, 0, 399, 0, 285, 0,
					286, 252, 0, 0, 0, 296, 429, 327, 0, 0, 0, 329, 367, 0, 478, 363, 0, 361, 364, 370, 369, 0, 470, 0, 468, 0, 473, 476, 0,
					0, 351, 0, 0, 346, 0, 349, 356, 388, 386, 0, 0, 372, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 390, 389, 391, 289, 292, 0, 0, 0,
					0, 0, 0, 360, 0, 0, 0, 0, 0, 0, 0, 358, 0, 0, 0, 0, 494, 51, 302, 0, 0, 0, 0, 0, 0, 392, 0, 0, 0, 0, 362, 471, 0, 466,
					469, 474, 271, 0, 352, 0, 343, 0, 341, 0, 347, 350, 309, 0, 321, 297, 0, 0, 0, 0, 0, 0, 0, 0, 467, 345, 0, 339, 342,
					348, 0, 340, },
			yyDgoto = {
					//yyDgoto 152
					1, 218, 300, 64, 113, 586, 554, 114, 210, 548, 493, 388, 494, 495, 496, 197, 66, 67, 68, 69, 70, 303, 302, 470, 71, 72,
					73, 478, 74, 75, 76, 115, 77, 215, 216, 79, 80, 81, 82, 83, 84, 220, 270, 730, 874, 731, 723, 428, 727, 556, 378, 256,
					86, 692, 87, 88, 497, 212, 755, 222, 592, 593, 499, 777, 681, 682, 567, 90, 91, 248, 406, 598, 280, 223, 93, 249, 309,
					307, 500, 501, 662, 94, 250, 251, 287, 461, 779, 420, 252, 421, 669, 765, 316, 355, 508, 95, 96, 391, 224, 213, 214,
					503, 836, 670, 674, 310, 278, 782, 240, 430, 663, 664, 837, 425, 698, 199, 504, 2, 229, 230, 437, 267, 426, 685, 595,
					454, 257, 450, 395, 232, 616, 740, 233, 741, 624, 878, 582, 396, 579, 804, 383, 385, 594, 809, 311, 543, 506, 505, 653,
					652, 581, 384, },
			yySindex = {
					//yySindex 947
					0, 0, 14458, 14705, 17042, 17288, 17996, 17888, 14458, 16550, 16550, 12729, 0, 0, 12902, 14828, 14828, 0, 0, 14828,
					-202, -162, 0, 0, 0, 0, 33, 17780, 181, 0, -147, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16673, 16673, -136, -111, 14582, 16550,
					15197, 15566, 5272, 16673, 16796, 18103, 0, 0, 0, 198, 242, 0, 0, 0, 0, 0, 0, 0, -144, 0, -143, 0, 0, 0, -205, 0, 0, 0,
					0, 0, 0, 0, 221, 1036, 25, 3784, 0, 30, 422, 0, -166, 0, -46, 282, 0, 277, 0, 17165, 299, 0, 73, 1036, 0, 0, 0, -202,
					-162, 172, 181, 0, 0, 168, 16550, -90, 14458, 0, -144, 0, 23, 0, 423, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 336, 0,
					0, 213, 295, 283, 0, 25, 94, 444, 225, 543, 275, 94, 0, 0, 221, 347, 573, 0, 16550, 16550, 328, 0, 499, 0, 0, 0, 361,
					16673, 16673, 16673, 16673, 3784, 0, 0, 311, 613, 618, 0, 0, 0, 0, 2848, 0, 14828, 14828, 0, 0, 3341, 0, 16550, -149, 0,
					15689, 306, 14458, 0, 537, 352, 354, 358, 360, 14582, 339, 0, 181, 25, 351, 0, 185, 190, 311, 0, 190, 341, 381, 17411,
					0, 545, 0, 671, 0, 0, 0, 0, 0, 0, 0, 0, -7, 329, 410, -67, 344, 418, 346, -71, 0, 1858, 0, 0, 0, 378, 0, 0, 0, 0, 14334,
					16550, 16550, 16550, 16550, 14705, 16550, 16550, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673,
					16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 16673, 0, 0,
					2354, 3915, 14828, 18705, 18705, 16796, 0, 15812, 14582, 5770, 682, 15812, 16796, 396, 0, 0, 25, 0, 0, 0, 221, 0, 0, 0,
					4766, 4889, 14828, 14458, 16550, 1952, 0, 0, 0, 0, 15935, 468, 0, 360, 0, 14458, 476, 5846, 18265, 14828, 16673, 16673,
					16673, 14458, 347, 16058, 480, 0, 135, 135, 0, 18320, 18375, 14828, 0, 0, 0, 0, 16673, 14951, 0, 0, 15320, 0, 181, 0,
					406, 0, 0, 0, 181, 42, 0, 0, 0, 0, 0, 17888, 16550, 3784, 14458, 386, 5846, 18265, 16673, 16673, 16673, 181, 0, 0, 181,
					0, 15443, 0, 0, 15566, 0, 0, 0, 0, 0, 710, 18430, 18485, 14828, 17411, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 149, 0, 0,
					731, 703, 0, 0, 0, 0, 1514, 1964, 0, 0, 0, 0, 0, 459, 460, 733, 0, 181, -185, 734, 750, 0, 0, 0, 199, 199, 0, 0, 1036,
					0, 0, 0, 0, 0, 0, 0, 352, 3418, 3418, 3418, 3418, 2245, 2245, 2925, 2432, 3418, 3418, 3848, 3848, 1048, 1048, 352, 2747,
					352, 352, 421, 421, 2245, 2245, 2737, 2737, 1580, 199, 467, 0, 477, -162, 0, 0, 481, 0, 482, -162, 0, 0, 0, 181, 0, 0,
					-162, -162, 0, 3784, 16673, 0, 4307, 0, 0, 752, 181, 17411, 782, 0, 0, 0, 0, 0, 5355, 221, 0, 16550, 14458, -162, 0, 0,
					-162, 0, 181, 566, 42, 1964, 221, 14458, 18210, 17888, 0, 0, 493, 0, 14458, 578, 0, 338, 0, 501, 507, 512, 482, 181,
					4307, 468, 590, 62, 0, 0, 0, 0, 0, 0, 0, 0, 0, 181, 16550, 0, 16673, 311, 618, 0, 0, 0, 0, 0, 0, 0, 42, 495, 0, 352,
					352, 3784, 0, 0, 190, 17411, 0, 0, 0, 0, 181, 710, 14458, -40, 0, 0, 0, 16673, 0, 1514, 13, 0, 815, 0, 181, 181, 0, 0,
					1835, 0, 0, 796, 14458, 14458, 0, 1964, 0, 1964, 0, 0, 1083, 0, 14458, 0, 14458, 199, 808, 14458, 16796, 16796, 0, 378,
					0, 0, 16796, 16673, 0, 378, 539, 530, 0, 0, 0, 0, 0, 16673, 16181, 0, 710, 17411, 16673, 0, 221, 614, 0, 0, 0, 181, 0,
					629, 0, 0, 17534, 94, 0, 0, 14458, 0, 0, 16550, 0, 631, 16673, 16673, 16673, 546, 636, 0, 16304, 14458, 14458, 14458, 0,
					135, 0, 0, 0, 0, 0, 0, 0, 526, 0, 0, 0, 0, 0, 0, 181, 835, 853, 1578, 0, 181, 857, 0, 1350, 638, 547, 861, 870, 0, 874,
					0, 857, 860, 879, 181, 880, 881, 0, 574, 0, 669, 585, 14458, 16673, 687, 0, 3784, 0, 3784, 0, 0, 3784, 3784, 0, 16796,
					0, 3784, 16673, 0, 710, 3784, 14458, 0, 0, 0, 0, 1952, 643, 0, 561, 0, 0, 14458, 0, 94, 0, 16673, 0, 0, -117, 692, 694,
					0, 0, 0, 917, 835, 542, 0, 0, 1835, 0, 0, 203, 0, 0, 0, 0, 1835, 0, 1964, 0, 1835, 0, 0, 17657, 1835, 0, 605, 2281, 0,
					2281, 0, 0, 0, 0, 603, 3784, 0, 0, 3784, 0, 705, 14458, 0, 18540, 18595, 14828, 213, 14458, 0, 0, 0, 0, 0, 14458, 835,
					917, 835, 927, 1350, 0, 857, 931, 857, 857, 667, 575, 857, 0, 934, 941, 947, 857, 0, 0, 0, 730, 0, 0, 0, 0, 181, 0, 338,
					732, 917, 835, 0, 0, 1835, 0, 0, 0, 0, 18650, 0, 1835, 0, 2281, 0, 1835, 0, 0, 0, 0, 0, 0, 917, 857, 0, 0, 857, 954,
					857, 857, 0, 0, 1835, 0, 0, 0, 857, 0, },
			yyRindex = {
					//yyRindex 947
					0, 0, 173, 0, 0, 0, 0, 0, 1157, 0, 0, 728, 0, 0, 0, 13067, 13173, 0, 0, 13315, 4523, 4030, 0, 0, 0, 0, 16919, 0, 16427,
					0, 0, 0, 0, 0, 1690, 3053, 0, 0, 2067, 0, 0, 0, 0, 0, 0, 54, 0, 662, 650, 35, 0, 0, 438, 0, 0, 0, 633, 186, 0, 0, 0, 0,
					0, 13418, 0, 15074, 0, 6754, 0, 0, 0, 6855, 0, 0, 0, 0, 0, 0, 0, 529, 686, 4276, 14002, 6999, 14065, 0, 0, 14128, 0,
					13532, 0, 0, 0, 0, 80, 0, 0, 0, 801, 0, 0, 0, 7103, 6060, 0, 675, 11697, 11821, 0, 0, 0, 54, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1528, 1883, 1929, 2306, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 2429, 2922, 3415, 4250, 0, 4392, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4769, 0, 0, 425, 0, 0, 6174, 1354, 0, 0, 6506, 0, 0, 0, 0,
					0, 728, 0, 745, 0, 0, 0, 0, 632, 0, 711, 0, 0, 0, 0, 0, 0, 0, 11429, 0, 0, 1311, 3787, 3787, 0, 0, 0, 0, 680, 0, 0, 7,
					0, 0, 680, 0, 0, 0, 0, 0, 0, 9, 0, 0, 7464, 7216, 7348, 13666, 54, 0, 127, 680, 120, 0, 0, 681, 681, 0, 0, 668, 0, 0, 0,
					1161, 0, 1235, 83, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 13820, 0, 0, 0, 0, 885, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 0, 0, 0, 0, 0, 54,
					132, 176, 0, 0, 0, 0, 0, 223, 0, 12213, 0, 0, 0, 0, 0, 0, 0, -5, 1157, 0, 228, 0, 0, 0, 0, 200, 391, 0, 6640, 0, 1093,
					12337, 0, 0, -5, 0, 0, 0, 673, 0, 0, 0, 0, 0, 0, 883, 0, 0, -5, 0, 0, 0, 0, 0, 13777, 0, 0, 13777, 0, 680, 0, 0, 0, 0,
					0, 680, 680, 0, 0, 0, 0, 0, 0, 0, 10440, 9, 0, 0, 0, 0, 0, 0, 680, 0, 184, 680, 0, 690, 0, 0, -44, 0, 0, 0, 4711, 0,
					180, 0, 0, -5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 88, 0, 0, 0, 0, 0, 102, 0, 0, 0, 0, 0, 165, 0, 48, 0,
					206, 0, 48, 48, 0, 0, 0, 12471, 12606, 0, 0, 869, 0, 0, 0, 0, 0, 0, 0, 7565, 9529, 9651, 9769, 9866, 9074, 9194, 9952,
					10225, 10042, 10139, 10315, 10355, 8494, 8616, 7680, 8739, 7813, 7928, 8277, 8390, 9314, 9411, 8857, 8965, 979, 12471,
					4646, 0, 5016, 4153, 0, 0, 5139, 3176, 5509, 15074, 0, 3546, 0, 697, 0, 0, 5632, 5632, 0, 10536, 0, 0, 1450, 0, 0, 0,
					680, 0, 187, 0, 0, 0, 1885, 0, 11516, 0, 0, 0, 1157, 6308, 11955, 12079, 0, 0, 697, 0, 680, 130, 0, 1157, 0, 0, 0, 44,
					205, 0, 653, 793, 0, 793, 0, 2190, 2560, 2683, 3669, 697, 11602, 793, 0, 0, 0, 0, 0, 0, 0, 1522, 3879, 4853, 1009, 697,
					0, 0, 0, 1811, 3787, 0, 0, 0, 0, 0, 0, 0, 680, 0, 0, 8029, 8145, 10621, 141, 0, 681, 0, 920, 928, 1044, 999, 697, 211,
					9, 0, 0, 0, 0, 0, 0, 0, 136, 0, 145, 0, 680, 11, 0, 0, 0, 0, 0, 251, 148, 9, 0, 0, 0, 0, 0, 0, 32, 0, 148, 0, 9, 12606,
					0, 148, 0, 0, 0, 13881, 0, 0, 0, 0, 0, 13917, 12966, 0, 0, 0, 0, 0, 11181, 0, 0, 0, 276, 0, 0, 0, 0, 0, 0, 0, 0, 680, 0,
					0, 0, 0, 0, 0, 0, 0, 148, 0, 0, 0, 0, 0, 0, 0, 0, 5959, 0, 0, 0, 970, 148, 148, 987, 0, 0, 0, 0, 0, 0, 0, 1192, 0, 0, 0,
					0, 0, 0, 0, 680, 0, 147, 0, 0, 680, 48, 0, 0, 0, 0, 48, 48, 0, 48, 0, 48, 43, 40, 32, 40, 40, 0, 0, 0, 0, 0, 9, 0, 0, 0,
					10682, 0, 10779, 0, 0, 10865, 10974, 0, 0, 0, 11035, 0, 14183, 364, 11121, 1157, 0, 0, 0, 0, 228, 0, 1045, 0, 1214, 0,
					1157, 0, 0, 0, 0, 0, 0, 793, 0, 0, 0, 0, 0, 155, 0, 156, 0, 0, 0, 0, 0, -171, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 41, 0, 0, 0, 0, 0, 0, 0, 11218, 0, 0, 11342, 14241, 0, 1157, 1469, 0, 0, -5, 425, 1093, 0, 0, 0, 0, 0, 148, 0, 157,
					0, 158, 0, 0, 48, 48, 48, 48, 0, 50, 40, 0, 40, 40, 40, 40, 0, 0, 0, 0, 712, 958, 1292, 942, 697, 0, 793, 0, 162, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1101, 0, 0, 166, 48, 1366, 226, 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 40,
					0, },
			yyGindex = {
					//yyGindex 152
					0, 442, 0, -1, 247, -319, 0, -15, 4, -6, 116, 0, 0, 0, 343, 0, 0, 0, 991, 0, 0, 0, 624, -123, 0, 0, 0, 0, 0, 0, 5, 1056,
					46, 811, -374, 0, 257, 775, 1427, 111, 70, 117, 20, -371, 0, 160, 0, 658, 0, 98, 0, -10, 1060, 159, 0, 0, -597, 0, 0,
					619, -237, 259, 0, 0, 0, -395, -241, 16, -34, 126, -388, 0, 0, 725, 36, -33, 0, 0, 5767, 399, -637, 0, 1, 110, 0, -389,
					219, -242, -120, 0, 743, -284, 1004, 0, -558, 1062, 252, 209, 859, 0, -20, -598, 0, -501, 0, 0, -158, -704, 0, -340,
					-656, 424, 194, 392, -411, 0, -658, 0, 39, 1002, 0, 0, -24, 0, 0, -239, 0, 0, -222, 0, -346, 0, 0, 0, 0, 0, 0, 56, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	protected static final short[] yyTable = Ruby19YyTables.yyTable();
	protected static final short[] yyCheck = Ruby19YyTables.yyCheck();

	/**
	 * maps symbol value to printable name.
	 * 
	 * @see #yyExpecting
	 */
	protected static final String[] yyNames = { "end-of-file", null, null, null, null, null, null, null, null, null, "'\\n'", null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "' '", null,
			null, null, null, null, null, null, null, null, null, null, "','", null, null, null, null, null, null, null, null, null, null,
			null, null, null, "':'", "';'", null, "'='", null, "'?'", null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "'['", null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, "kCLASS", "kMODULE", "kDEF", "kUNDEF",
			"kBEGIN", "kRESCUE", "kENSURE", "kEND", "kIF", "kUNLESS", "kTHEN", "kELSIF", "kELSE", "kCASE", "kWHEN", "kWHILE", "kUNTIL",
			"kFOR", "kBREAK", "kNEXT", "kREDO", "kRETRY", "kIN", "kDO", "kDO_COND", "kDO_BLOCK", "kRETURN", "kYIELD", "kSUPER", "kSELF",
			"kNIL", "kTRUE", "kFALSE", "kAND", "kOR", "kNOT", "kIF_MOD", "kUNLESS_MOD", "kWHILE_MOD", "kUNTIL_MOD", "kRESCUE_MOD", "kALIAS",
			"kDEFINED", "klBEGIN", "klEND", "k__LINE__", "k__FILE__", "k__ENCODING__", "kDO_LAMBDA", "tIDENTIFIER", "tFID", "tGVAR",
			"tIVAR", "tCONSTANT", "tCVAR", "tLABEL", "tCHAR", "tUPLUS", "tUMINUS", "tUMINUS_NUM", "tPOW", "tCMP", "tEQ", "tEQQ", "tNEQ",
			"tGEQ", "tLEQ", "tANDOP", "tOROP", "tMATCH", "tNMATCH", "tDOT", "tDOT2", "tDOT3", "tAREF", "tASET", "tLSHFT", "tRSHFT",
			"tCOLON2", "tCOLON3", "tOP_ASGN", "tASSOC", "tLPAREN", "tLPAREN2", "tRPAREN", "tLPAREN_ARG", "tLBRACK", "tRBRACK", "tLBRACE",
			"tLBRACE_ARG", "tSTAR", "tSTAR2", "tAMPER", "tAMPER2", "tTILDE", "tPERCENT", "tDIVIDE", "tPLUS", "tMINUS", "tLT", "tGT",
			"tPIPE", "tBANG", "tCARET", "tLCURLY", "tRCURLY", "tBACK_REF2", "tSYMBEG", "tSTRING_BEG", "tXSTRING_BEG", "tREGEXP_BEG",
			"tWORDS_BEG", "tQWORDS_BEG", "tSTRING_DBEG", "tSTRING_DVAR", "tSTRING_END", "tLAMBDA", "tLAMBEG", "tNTH_REF", "tBACK_REF",
			"tSTRING_CONTENT", "tINTEGER", "tFLOAT", "tREGEXP_END", "tCOMMENT", "tWHITESPACE", "tDOCUMENTATION", "tLOWEST", };

	/**
	 * printable rules for debugging.
	 */
	protected static final String[] yyRule = { "$accept : program", "$$1 :", "program : $$1 compstmt",
			"bodystmt : compstmt opt_rescue opt_else opt_ensure", "compstmt : stmts opt_terms", "stmts : none", "stmts : stmt",
			"stmts : stmts terms stmt", "stmts : error stmt", "$$2 :", "stmt : kALIAS fitem $$2 fitem", "stmt : kALIAS tGVAR tGVAR",
			"stmt : kALIAS tGVAR tBACK_REF", "stmt : kALIAS tGVAR tNTH_REF", "stmt : kUNDEF undef_list", "stmt : stmt kIF_MOD expr_value",
			"stmt : stmt kUNLESS_MOD expr_value", "stmt : stmt kWHILE_MOD expr_value", "stmt : stmt kUNTIL_MOD expr_value",
			"stmt : stmt kRESCUE_MOD stmt", "$$3 :", "stmt : klBEGIN $$3 tLCURLY compstmt tRCURLY", "stmt : klEND tLCURLY compstmt tRCURLY",
			"stmt : lhs '=' command_call", "stmt : mlhs '=' command_call", "stmt : var_lhs tOP_ASGN command_call",
			"stmt : primary_value '[' opt_call_args rbracket tOP_ASGN command_call",
			"stmt : primary_value tDOT tIDENTIFIER tOP_ASGN command_call", "stmt : primary_value tDOT tCONSTANT tOP_ASGN command_call",
			"stmt : primary_value tCOLON2 tIDENTIFIER tOP_ASGN command_call", "stmt : backref tOP_ASGN command_call", "stmt : lhs '=' mrhs",
			"stmt : mlhs '=' arg_value", "stmt : mlhs '=' mrhs", "stmt : expr", "expr : command_call", "expr : expr kAND expr",
			"expr : expr kOR expr", "expr : kNOT opt_nl expr", "expr : tBANG command_call", "expr : arg", "expr_value : expr",
			"command_call : command", "command_call : block_command", "command_call : kRETURN call_args", "command_call : kBREAK call_args",
			"command_call : kNEXT call_args", "block_command : block_call", "block_command : block_call tDOT operation2 command_args",
			"block_command : block_call tCOLON2 operation2 command_args", "$$4 :",
			"cmd_brace_block : tLBRACE_ARG $$4 opt_block_param compstmt tRCURLY", "command : operation command_args",
			"command : operation command_args cmd_brace_block", "command : primary_value tDOT operation2 command_args",
			"command : primary_value tDOT operation2 command_args cmd_brace_block",
			"command : primary_value tCOLON2 operation2 command_args",
			"command : primary_value tCOLON2 operation2 command_args cmd_brace_block", "command : kSUPER command_args",
			"command : kYIELD command_args", "mlhs : mlhs_basic", "mlhs : tLPAREN mlhs_inner rparen", "mlhs_inner : mlhs_basic",
			"mlhs_inner : tLPAREN mlhs_inner rparen", "mlhs_basic : mlhs_head", "mlhs_basic : mlhs_head mlhs_item",
			"mlhs_basic : mlhs_head tSTAR mlhs_node", "mlhs_basic : mlhs_head tSTAR mlhs_node ',' mlhs_post",
			"mlhs_basic : mlhs_head tSTAR", "mlhs_basic : mlhs_head tSTAR ',' mlhs_post", "mlhs_basic : tSTAR mlhs_node",
			"mlhs_basic : tSTAR mlhs_node ',' mlhs_post", "mlhs_basic : tSTAR", "mlhs_basic : tSTAR ',' mlhs_post", "mlhs_item : mlhs_node",
			"mlhs_item : tLPAREN mlhs_inner rparen", "mlhs_head : mlhs_item ','", "mlhs_head : mlhs_head mlhs_item ','",
			"mlhs_post : mlhs_item", "mlhs_post : mlhs_post ',' mlhs_item", "mlhs_node : variable",
			"mlhs_node : primary_value '[' opt_call_args rbracket", "mlhs_node : primary_value tDOT tIDENTIFIER",
			"mlhs_node : primary_value tCOLON2 tIDENTIFIER", "mlhs_node : primary_value tDOT tCONSTANT",
			"mlhs_node : primary_value tCOLON2 tCONSTANT", "mlhs_node : tCOLON3 tCONSTANT", "mlhs_node : backref", "lhs : variable",
			"lhs : primary_value '[' opt_call_args rbracket", "lhs : primary_value tDOT tIDENTIFIER",
			"lhs : primary_value tCOLON2 tIDENTIFIER", "lhs : primary_value tDOT tCONSTANT", "lhs : primary_value tCOLON2 tCONSTANT",
			"lhs : tCOLON3 tCONSTANT", "lhs : backref", "cname : tIDENTIFIER", "cname : tCONSTANT", "cpath : tCOLON3 cname",
			"cpath : cname", "cpath : primary_value tCOLON2 cname", "fname : tIDENTIFIER", "fname : tCONSTANT", "fname : tFID",
			"fname : op", "fname : reswords", "fsym : fname", "fsym : symbol", "fitem : fsym", "fitem : dsym", "undef_list : fitem",
			"$$5 :", "undef_list : undef_list ',' $$5 fitem", "op : tPIPE", "op : tCARET", "op : tAMPER2", "op : tCMP", "op : tEQ",
			"op : tEQQ", "op : tMATCH", "op : tNMATCH", "op : tGT", "op : tGEQ", "op : tLT", "op : tLEQ", "op : tNEQ", "op : tLSHFT",
			"op : tRSHFT", "op : tPLUS", "op : tMINUS", "op : tSTAR2", "op : tSTAR", "op : tDIVIDE", "op : tPERCENT", "op : tPOW",
			"op : tBANG", "op : tTILDE", "op : tUPLUS", "op : tUMINUS", "op : tAREF", "op : tASET", "op : tBACK_REF2",
			"reswords : k__LINE__", "reswords : k__FILE__", "reswords : k__ENCODING__", "reswords : klBEGIN", "reswords : klEND",
			"reswords : kALIAS", "reswords : kAND", "reswords : kBEGIN", "reswords : kBREAK", "reswords : kCASE", "reswords : kCLASS",
			"reswords : kDEF", "reswords : kDEFINED", "reswords : kDO", "reswords : kELSE", "reswords : kELSIF", "reswords : kEND",
			"reswords : kENSURE", "reswords : kFALSE", "reswords : kFOR", "reswords : kIN", "reswords : kMODULE", "reswords : kNEXT",
			"reswords : kNIL", "reswords : kNOT", "reswords : kOR", "reswords : kREDO", "reswords : kRESCUE", "reswords : kRETRY",
			"reswords : kRETURN", "reswords : kSELF", "reswords : kSUPER", "reswords : kTHEN", "reswords : kTRUE", "reswords : kUNDEF",
			"reswords : kWHEN", "reswords : kYIELD", "reswords : kIF_MOD", "reswords : kUNLESS_MOD", "reswords : kWHILE_MOD",
			"reswords : kUNTIL_MOD", "reswords : kRESCUE_MOD", "arg : lhs '=' arg", "arg : lhs '=' arg kRESCUE_MOD arg",
			"arg : var_lhs tOP_ASGN arg", "arg : var_lhs tOP_ASGN arg kRESCUE_MOD arg",
			"arg : primary_value '[' opt_call_args rbracket tOP_ASGN arg", "arg : primary_value tDOT tIDENTIFIER tOP_ASGN arg",
			"arg : primary_value tDOT tCONSTANT tOP_ASGN arg", "arg : primary_value tCOLON2 tIDENTIFIER tOP_ASGN arg",
			"arg : primary_value tCOLON2 tCONSTANT tOP_ASGN arg", "arg : tCOLON3 tCONSTANT tOP_ASGN arg", "arg : backref tOP_ASGN arg",
			"arg : arg tDOT2 arg", "arg : arg tDOT3 arg", "arg : arg tPLUS arg", "arg : arg tMINUS arg", "arg : arg tSTAR2 arg",
			"arg : arg tDIVIDE arg", "arg : arg tPERCENT arg", "arg : arg tPOW arg", "arg : tUMINUS_NUM tINTEGER tPOW arg",
			"arg : tUMINUS_NUM tFLOAT tPOW arg", "arg : tUPLUS arg", "arg : tUMINUS arg", "arg : arg tPIPE arg", "arg : arg tCARET arg",
			"arg : arg tAMPER2 arg", "arg : arg tCMP arg", "arg : arg tGT arg", "arg : arg tGEQ arg", "arg : arg tLT arg",
			"arg : arg tLEQ arg", "arg : arg tEQ arg", "arg : arg tEQQ arg", "arg : arg tNEQ arg", "arg : arg tMATCH arg",
			"arg : arg tNMATCH arg", "arg : tBANG arg", "arg : tTILDE arg", "arg : arg tLSHFT arg", "arg : arg tRSHFT arg",
			"arg : arg tANDOP arg", "arg : arg tOROP arg", "arg : kDEFINED opt_nl arg", "arg : arg '?' arg opt_nl ':' arg", "arg : primary",
			"arg_value : arg", "aref_args : none", "aref_args : args trailer", "aref_args : args ',' assocs trailer",
			"aref_args : assocs trailer", "paren_args : tLPAREN2 opt_call_args rparen", "opt_paren_args : none",
			"opt_paren_args : paren_args", "opt_call_args : none", "opt_call_args : call_args", "call_args : command",
			"call_args : args opt_block_arg", "call_args : assocs opt_block_arg", "call_args : args ',' assocs opt_block_arg",
			"call_args : block_arg", "$$6 :", "command_args : $$6 call_args", "block_arg : tAMPER arg_value",
			"opt_block_arg : ',' block_arg", "opt_block_arg : ','", "opt_block_arg : none_block_pass", "args : arg_value",
			"args : tSTAR arg_value", "args : args ',' arg_value", "args : args ',' tSTAR arg_value", "mrhs : args ',' arg_value",
			"mrhs : args ',' tSTAR arg_value", "mrhs : tSTAR arg_value", "primary : literal", "primary : strings", "primary : xstring",
			"primary : regexp", "primary : words", "primary : qwords", "primary : var_ref", "primary : backref", "primary : tFID",
			"primary : kBEGIN bodystmt kEND", "$$7 :", "primary : tLPAREN_ARG expr $$7 rparen", "primary : tLPAREN compstmt tRPAREN",
			"primary : primary_value tCOLON2 tCONSTANT", "primary : tCOLON3 tCONSTANT", "primary : tLBRACK aref_args tRBRACK",
			"primary : tLBRACE assoc_list tRCURLY", "primary : kRETURN", "primary : kYIELD tLPAREN2 call_args rparen",
			"primary : kYIELD tLPAREN2 rparen", "primary : kYIELD", "primary : kDEFINED opt_nl tLPAREN2 expr rparen",
			"primary : kNOT tLPAREN2 expr rparen", "primary : kNOT tLPAREN2 rparen", "primary : operation brace_block",
			"primary : method_call", "primary : method_call brace_block", "primary : tLAMBDA lambda",
			"primary : kIF expr_value then compstmt if_tail kEND", "primary : kUNLESS expr_value then compstmt opt_else kEND", "$$8 :",
			"$$9 :", "primary : kWHILE $$8 expr_value do $$9 compstmt kEND", "$$10 :", "$$11 :",
			"primary : kUNTIL $$10 expr_value do $$11 compstmt kEND", "primary : kCASE expr_value opt_terms case_body kEND",
			"primary : kCASE opt_terms case_body kEND", "$$12 :", "$$13 :",
			"primary : kFOR for_var kIN $$12 expr_value do $$13 compstmt kEND", "$$14 :",
			"primary : kCLASS cpath superclass $$14 bodystmt kEND", "$$15 :", "$$16 :",
			"primary : kCLASS tLSHFT expr $$15 term $$16 bodystmt kEND", "$$17 :", "primary : kMODULE cpath $$17 bodystmt kEND", "$$18 :",
			"primary : kDEF fname $$18 f_arglist bodystmt kEND", "$$19 :", "$$20 :",
			"primary : kDEF singleton dot_or_colon $$19 fname $$20 f_arglist bodystmt kEND", "primary : kBREAK", "primary : kNEXT",
			"primary : kREDO", "primary : kRETRY", "primary_value : primary", "then : term", "then : kTHEN", "then : term kTHEN",
			"do : term", "do : kDO_COND", "if_tail : opt_else", "if_tail : kELSIF expr_value then compstmt if_tail", "opt_else : none",
			"opt_else : kELSE compstmt", "for_var : lhs", "for_var : mlhs", "f_marg : f_norm_arg", "f_marg : tLPAREN f_margs rparen",
			"f_marg_list : f_marg", "f_marg_list : f_marg_list ',' f_marg", "f_margs : f_marg_list",
			"f_margs : f_marg_list ',' tSTAR f_norm_arg", "f_margs : f_marg_list ',' tSTAR f_norm_arg ',' f_marg_list",
			"f_margs : f_marg_list ',' tSTAR", "f_margs : f_marg_list ',' tSTAR ',' f_marg_list", "f_margs : tSTAR f_norm_arg",
			"f_margs : tSTAR f_norm_arg ',' f_marg_list", "f_margs : tSTAR", "f_margs : tSTAR ',' f_marg_list",
			"block_param : f_arg ',' f_block_optarg ',' f_rest_arg opt_f_block_arg",
			"block_param : f_arg ',' f_block_optarg ',' f_rest_arg ',' f_arg opt_f_block_arg",
			"block_param : f_arg ',' f_block_optarg opt_f_block_arg", "block_param : f_arg ',' f_block_optarg ',' f_arg opt_f_block_arg",
			"block_param : f_arg ',' f_rest_arg opt_f_block_arg", "block_param : f_arg ','",
			"block_param : f_arg ',' f_rest_arg ',' f_arg opt_f_block_arg", "block_param : f_arg opt_f_block_arg",
			"block_param : f_block_optarg ',' f_rest_arg opt_f_block_arg",
			"block_param : f_block_optarg ',' f_rest_arg ',' f_arg opt_f_block_arg", "block_param : f_block_optarg opt_f_block_arg",
			"block_param : f_block_optarg ',' f_arg opt_f_block_arg", "block_param : f_rest_arg opt_f_block_arg",
			"block_param : f_rest_arg ',' f_arg opt_f_block_arg", "block_param : f_block_arg", "opt_block_param : none",
			"opt_block_param : block_param_def", "block_param_def : tPIPE opt_bv_decl tPIPE", "block_param_def : tOROP",
			"block_param_def : tPIPE block_param opt_bv_decl tPIPE", "opt_bv_decl : opt_nl", "opt_bv_decl : opt_nl ';' bv_decls opt_nl",
			"bv_decls : bvar", "bv_decls : bv_decls ',' bvar", "bvar : tIDENTIFIER", "bvar : f_bad_arg", "$$21 :",
			"lambda : $$21 f_larglist lambda_body", "f_larglist : tLPAREN2 f_args opt_bv_decl rparen", "f_larglist : f_args opt_bv_decl",
			"lambda_body : tLAMBEG compstmt tRCURLY", "lambda_body : kDO_LAMBDA compstmt kEND", "$$22 :",
			"do_block : kDO_BLOCK $$22 opt_block_param compstmt kEND", "block_call : command do_block",
			"block_call : block_call tDOT operation2 opt_paren_args", "block_call : block_call tCOLON2 operation2 opt_paren_args",
			"method_call : operation paren_args", "method_call : primary_value tDOT operation2 opt_paren_args",
			"method_call : primary_value tCOLON2 operation2 paren_args", "method_call : primary_value tCOLON2 operation3",
			"method_call : primary_value tDOT paren_args", "method_call : primary_value tCOLON2 paren_args",
			"method_call : kSUPER paren_args", "method_call : kSUPER", "method_call : primary_value '[' opt_call_args rbracket", "$$23 :",
			"brace_block : tLCURLY $$23 opt_block_param compstmt tRCURLY", "$$24 :", "brace_block : kDO $$24 opt_block_param compstmt kEND",
			"case_body : kWHEN args then compstmt cases", "cases : opt_else", "cases : case_body",
			"opt_rescue : kRESCUE exc_list exc_var then compstmt opt_rescue", "opt_rescue :", "exc_list : arg_value", "exc_list : mrhs",
			"exc_list : none", "exc_var : tASSOC lhs", "exc_var : none", "opt_ensure : kENSURE compstmt", "opt_ensure : none",
			"literal : numeric", "literal : symbol", "literal : dsym", "strings : string", "string : tCHAR", "string : string1",
			"string : string string1", "string1 : tSTRING_BEG string_contents tSTRING_END",
			"xstring : tXSTRING_BEG xstring_contents tSTRING_END", "regexp : tREGEXP_BEG xstring_contents tREGEXP_END",
			"words : tWORDS_BEG ' ' tSTRING_END", "words : tWORDS_BEG word_list tSTRING_END", "word_list :",
			"word_list : word_list word ' '", "word : string_content", "word : word string_content", "qwords : tQWORDS_BEG ' ' tSTRING_END",
			"qwords : tQWORDS_BEG qword_list tSTRING_END", "qword_list :", "qword_list : qword_list tSTRING_CONTENT ' '",
			"string_contents :", "string_contents : string_contents string_content", "xstring_contents :",
			"xstring_contents : xstring_contents string_content", "string_content : tSTRING_CONTENT", "$$25 :",
			"string_content : tSTRING_DVAR $$25 string_dvar", "$$26 :", "string_content : tSTRING_DBEG $$26 compstmt tRCURLY",
			"string_dvar : tGVAR", "string_dvar : tIVAR", "string_dvar : tCVAR", "string_dvar : backref", "symbol : tSYMBEG sym",
			"sym : fname", "sym : tIVAR", "sym : tGVAR", "sym : tCVAR", "dsym : tSYMBEG xstring_contents tSTRING_END", "numeric : tINTEGER",
			"numeric : tFLOAT", "numeric : tUMINUS_NUM tINTEGER", "numeric : tUMINUS_NUM tFLOAT", "variable : tIDENTIFIER",
			"variable : tIVAR", "variable : tGVAR", "variable : tCONSTANT", "variable : tCVAR", "variable : kNIL", "variable : kSELF",
			"variable : kTRUE", "variable : kFALSE", "variable : k__FILE__", "variable : k__LINE__", "variable : k__ENCODING__",
			"var_ref : variable", "var_lhs : variable", "backref : tNTH_REF", "backref : tBACK_REF", "superclass : term", "$$27 :",
			"superclass : tLT $$27 expr_value term", "superclass : error term", "f_arglist : tLPAREN2 f_args rparen",
			"f_arglist : f_args term", "f_args : f_arg ',' f_optarg ',' f_rest_arg opt_f_block_arg",
			"f_args : f_arg ',' f_optarg ',' f_rest_arg ',' f_arg opt_f_block_arg", "f_args : f_arg ',' f_optarg opt_f_block_arg",
			"f_args : f_arg ',' f_optarg ',' f_arg opt_f_block_arg", "f_args : f_arg ',' f_rest_arg opt_f_block_arg",
			"f_args : f_arg ',' f_rest_arg ',' f_arg opt_f_block_arg", "f_args : f_arg opt_f_block_arg",
			"f_args : f_optarg ',' f_rest_arg opt_f_block_arg", "f_args : f_optarg ',' f_rest_arg ',' f_arg opt_f_block_arg",
			"f_args : f_optarg opt_f_block_arg", "f_args : f_optarg ',' f_arg opt_f_block_arg", "f_args : f_rest_arg opt_f_block_arg",
			"f_args : f_rest_arg ',' f_arg opt_f_block_arg", "f_args : f_block_arg", "f_args :", "f_bad_arg : tCONSTANT",
			"f_bad_arg : tIVAR", "f_bad_arg : tGVAR", "f_bad_arg : tCVAR", "f_norm_arg : f_bad_arg", "f_norm_arg : tIDENTIFIER",
			"f_arg_item : f_norm_arg", "f_arg_item : tLPAREN f_margs rparen", "f_arg : f_arg_item", "f_arg : f_arg ',' f_arg_item",
			"f_opt : tIDENTIFIER '=' arg_value", "f_block_opt : tIDENTIFIER '=' primary_value", "f_block_optarg : f_block_opt",
			"f_block_optarg : f_block_optarg ',' f_block_opt", "f_optarg : f_opt", "f_optarg : f_optarg ',' f_opt", "restarg_mark : tSTAR2",
			"restarg_mark : tSTAR", "f_rest_arg : restarg_mark tIDENTIFIER", "f_rest_arg : restarg_mark", "blkarg_mark : tAMPER2",
			"blkarg_mark : tAMPER", "f_block_arg : blkarg_mark tIDENTIFIER", "opt_f_block_arg : ',' f_block_arg", "opt_f_block_arg :",
			"singleton : var_ref", "$$28 :", "singleton : tLPAREN2 $$28 expr rparen", "assoc_list : none", "assoc_list : assocs trailer",
			"assocs : assoc", "assocs : assocs ',' assoc", "assoc : arg_value tASSOC arg_value", "assoc : tLABEL arg_value",
			"operation : tIDENTIFIER", "operation : tCONSTANT", "operation : tFID", "operation2 : tIDENTIFIER", "operation2 : tCONSTANT",
			"operation2 : tFID", "operation2 : op", "operation3 : tIDENTIFIER", "operation3 : tFID", "operation3 : op",
			"dot_or_colon : tDOT", "dot_or_colon : tCOLON2", "opt_terms :", "opt_terms : terms", "opt_nl :", "opt_nl : '\\n'",
			"rparen : opt_nl tRPAREN", "rbracket : opt_nl tRBRACK", "trailer :", "trailer : '\\n'", "trailer : ','", "term : ';'",
			"term : '\\n'", "terms : term", "terms : terms ';'", "none :", "none_block_pass :", };

	/**
	 * debugging support, requires the package <tt>jay.yydebug</tt>. Set to <tt>null</tt> to
	 * suppress debugging messages.
	 */
	protected org.jay.yydebug.yyDebug yydebug;

	/**
	 * index-checked interface to {@link #yyNames}.
	 * 
	 * @param token
	 *            single character or <tt>%token</tt> value.
	 * @return token name or <tt>[illegal]</tt> or <tt>[unknown]</tt>.
	 */
	public static final String yyName(final int token) {
		if (token < 0 || token > yyNames.length)
			return "[illegal]";
		String name;
		if ((name = yyNames[token]) != null)
			return name;
		return "[unknown]";
	}

	/**
	 * computes list of expected tokens on error by tracing the tables.
	 * 
	 * @param state
	 *            for which to compute the list.
	 * @return list of token names.
	 */
	protected String[] yyExpecting(final int state) {
		int token, n, len = 0;
		final boolean[] ok = new boolean[yyNames.length];

		if ((n = yySindex[state]) != 0)
			for (token = n < 0 ? -n : 0; token < yyNames.length && n + token < yyTable.length; ++token)
				if (yyCheck[n + token] == token && !ok[token] && yyNames[token] != null) {
					++len;
					ok[token] = true;
				}
		if ((n = yyRindex[state]) != 0)
			for (token = n < 0 ? -n : 0; token < yyNames.length && n + token < yyTable.length; ++token)
				if (yyCheck[n + token] == token && !ok[token] && yyNames[token] != null) {
					++len;
					ok[token] = true;
				}

		final String result[] = new String[len];
		for (n = token = 0; n < len; ++token)
			if (ok[token])
				result[n++] = yyNames[token];
		return result;
	}

	/**
	 * the generated parser, with debugging messages. Maintains a dynamic state and value stack.
	 * 
	 * @param yyLex
	 *            scanner.
	 * @param ayydebug
	 *            debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
	 * @return result of the last reduction, if any.
	 * @throws IOException
	 *             if crap happens
	 */
	public Object yyparse(final Lexer yyLex, final Object ayydebug) throws java.io.IOException {
		this.yydebug = (org.jay.yydebug.yyDebug) ayydebug;
		return yyparse(yyLex);
	}

	/**
	 * initial size and increment of the state/value stack [default 256]. This is not final so that
	 * it can be overwritten outside of invocations of {@link #yyparse}.
	 */
	protected int yyMax;

	/**
	 * executed at the beginning of a reduce action. Used as <tt>$$ = yyDefault($1)</tt>, prior to
	 * the user-specified action, if any. Can be overwritten to provide deep copy, etc.
	 * 
	 * @param first
	 *            value for <tt>$1</tt>, or <tt>null</tt>.
	 * @return first.
	 */
	protected Object yyDefault(final Object first) {
		return first;
	}

	/**
	 * the generated parser. Maintains a dynamic state and value stack.
	 * 
	 * @param yyLex
	 *            scanner.
	 * @return result of the last reduction, if any.
	 * @throws IOException
	 *             if crap happens
	 */
	public Object yyparse(final Lexer yyLex) throws java.io.IOException {
		if (yyMax <= 0)
			yyMax = 256; // initial size
		int yyState = 0, yyStates[] = new int[yyMax]; // state stack
		Object yyVal = null, yyVals[] = new Object[yyMax]; // value stack
		int yyToken = -1; // current input
		int yyErrorFlag = 0; // #tokens to shift

		yyLoop: for (int yyTop = 0;; ++yyTop) {
			if (yyTop >= yyStates.length) { // dynamically increase
				final int[] i = new int[yyStates.length + yyMax];
				System.arraycopy(yyStates, 0, i, 0, yyStates.length);
				yyStates = i;
				final Object[] o = new Object[yyVals.length + yyMax];
				System.arraycopy(yyVals, 0, o, 0, yyVals.length);
				yyVals = o;
			}
			yyStates[yyTop] = yyState;
			yyVals[yyTop] = yyVal;
			if (yydebug != null)
				yydebug.push(yyState, yyVal);

			yyDiscarded: for (;;) { // discarding a token does not change stack
				int yyN;
				if ((yyN = yyDefRed[yyState]) == 0) { // else [default] reduce (yyN)
					if (yyToken < 0) {
						//            yyToken = yyLex.advance() ? yyLex.token() : 0;
						yyToken = yyLex.nextToken();
						if (yydebug != null)
							yydebug.lex(yyState, yyToken, yyName(yyToken), yyLex.value());
					}
					if ((yyN = yySindex[yyState]) != 0 && (yyN += yyToken) >= 0 && yyN < yyTable.length && yyCheck[yyN] == yyToken) {
						if (yydebug != null)
							yydebug.shift(yyState, yyTable[yyN], yyErrorFlag - 1);
						yyState = yyTable[yyN]; // shift to yyN
						yyVal = yyLex.value();
						yyToken = -1;
						if (yyErrorFlag > 0)
							--yyErrorFlag;
						continue yyLoop;
					}
					if ((yyN = yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0 && yyN < yyTable.length && yyCheck[yyN] == yyToken)
						yyN = yyTable[yyN]; // reduce (yyN)
					else
						switch (yyErrorFlag) {

						case 0:
							support.yyerror("syntax error", yyExpecting(yyState), yyNames[yyToken]);
							if (yydebug != null)
								yydebug.error("syntax error");

						case 1:
						case 2:
							yyErrorFlag = 3;
							do {
								if ((yyN = yySindex[yyStates[yyTop]]) != 0 && (yyN += yyErrorCode) >= 0 && yyN < yyTable.length
										&& yyCheck[yyN] == yyErrorCode) {
									if (yydebug != null)
										yydebug.shift(yyStates[yyTop], yyTable[yyN], 3);
									yyState = yyTable[yyN];
									yyVal = yyLex.value();
									continue yyLoop;
								}
								if (yydebug != null)
									yydebug.pop(yyStates[yyTop]);
							} while (--yyTop >= 0);
							if (yydebug != null)
								yydebug.reject();
							support.yyerror("irrecoverable syntax error");

						case 3:
							if (yyToken == 0) {
								if (yydebug != null)
									yydebug.reject();
								support.yyerror("irrecoverable syntax error at end-of-file");
							}
							if (yydebug != null)
								yydebug.discard(yyState, yyToken, yyName(yyToken), yyLex.value());
							yyToken = -1;
							continue yyDiscarded; // leave stack alone
						}
				}
				final int yyV = yyTop + 1 - yyLen[yyN];
				if (yydebug != null)
					yydebug.reduce(yyState, yyStates[yyV - 1], yyN, yyRule[yyN], yyLen[yyN]);
				final ParserState state = states[yyN];
				if (state == null) {
					yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
				} else {
					yyVal = state.execute(support, lexer, yyVal, yyVals, yyTop);
				}
				//        switch (yyN) {
				// ACTIONS_END
				//        }
				yyTop -= yyLen[yyN];
				yyState = yyStates[yyTop];
				final int yyM = yyLhs[yyN];
				if (yyState == 0 && yyM == 0) {
					if (yydebug != null)
						yydebug.shift(0, yyFinal);
					yyState = yyFinal;
					if (yyToken < 0) {
						yyToken = yyLex.nextToken();
						//            yyToken = yyLex.advance() ? yyLex.token() : 0;
						if (yydebug != null)
							yydebug.lex(yyState, yyToken, yyName(yyToken), yyLex.value());
					}
					if (yyToken == 0) {
						if (yydebug != null)
							yydebug.accept(yyVal);
						return yyVal;
					}
					continue yyLoop;
				}
				if ((yyN = yyGindex[yyM]) != 0 && (yyN += yyState) >= 0 && yyN < yyTable.length && yyCheck[yyN] == yyState)
					yyState = yyTable[yyN];
				else
					yyState = yyDgoto[yyM];
				if (yydebug != null)
					yydebug.shift(yyStates[yyTop], yyState);
				continue yyLoop;
			}
		}
	}

	static ParserState[] states = new ParserState[542];
	static {
		states[368] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((ISourcePositionHolder) yyVal).setPosition(support.union(((ArgsNode) yyVals[-1 + yyTop]), ((ListNode) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[33] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
				yyVal = yyVals[-2 + yyTop];
				((MultipleAsgnNode) yyVals[-2 + yyTop])
						.setPosition(support.union(((MultipleAsgnNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[234] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition pos = support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));

				if (((Node) yyVals[-1 + yyTop]) == null) {
					yyVal = new ArrayNode(pos);
				} else {
					yyVal = yyVals[-1 + yyTop];
					((Node) yyVal).setPosition(pos);
				}
				return yyVal;
			}
		};
		states[100] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_colon2(support.union(((Node) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[301] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Integer(support.getInSingle());
				support.setInSingle(0);
				support.pushLocalScope();
				return yyVal;
			}
		};
		states[536] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				return yyVal;
			}
		};
		states[469] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-5 + yyTop]), ((ListNode) yyVals[-3 + yyTop]), null, ((ListNode) yyVals[-1 + yyTop]),
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[402] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* FIXME: We may be intern'ing more than once. */
				yyVal = new SymbolNode(((Token) yyVals[0 + yyTop]).getPosition(),
						((String) ((Token) yyVals[0 + yyTop]).getValue()).intern());
				return yyVal;
			}
		};
		states[335] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[-1 + yyTop])), null,
						support.assignable(((Token) yyVals[0 + yyTop]), null), null);
				return yyVal;
			}
		};
		states[201] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "%", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[67] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-4 + yyTop])), ((ListNode) yyVals[-4 + yyTop]),
						((Node) yyVals[-2 + yyTop]), ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[268] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.warning(ID.GROUPED_EXPRESSION, support.getPosition(((Token) yyVals[-3 + yyTop])),
						"(...) interpreted as grouped expression");
				yyVal = yyVals[-2 + yyTop];
				return yyVal;
			}
		};
		states[503] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (!support.is_local_id(((Token) yyVals[0 + yyTop]))) {
					support.yyerror("block argument must be local variable");
				}

				yyVal = new BlockArgNode(((Token) yyVals[-1 + yyTop]).getPosition().union(((Token) yyVals[0 + yyTop]).getPosition()),
						support.arg_var(support.shadowing_lvar(((Token) yyVals[0 + yyTop]))));
				return yyVal;
			}
		};
		states[369] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[302] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new SClassNode(support.union(((Token) yyVals[-7 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-5 + yyTop]), support.getCurrentScope(), ((Node) yyVals[-1 + yyTop]));
				support.popCurrentScope();
				support.setInDef(((Boolean) yyVals[-4 + yyTop]).booleanValue());
				support.setInSingle(((Integer) yyVals[-2 + yyTop]).intValue());
				return yyVal;
			}
		};
		states[470] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-3 + yyTop]), null, ((RestArgNode) yyVals[-1 + yyTop]), null,
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[336] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[-3 + yyTop])), null,
						support.assignable(((Token) yyVals[-2 + yyTop]), null), ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[202] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "**", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[68] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-1 + yyTop])), ((ListNode) yyVals[-1 + yyTop]),
						new StarNode(support.getPosition(null)), null);
				return yyVal;
			}
		};
		states[269] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition pos = support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));
				final Node implicitNil = ((Node) yyVals[-1 + yyTop]) == null ? new ImplicitNilNode(pos) : ((Node) yyVals[-1 + yyTop]);
				/* compstmt position includes both parens around it */
				if (implicitNil != null)
					implicitNil.setPosition(pos);

				yyVal = implicitNil;
				return yyVal;
			}
		};
		states[1] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_BEG);
				support.initTopLocalVariables();
				return yyVal;
			}
		};
		states[504] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[370] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[303] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.yyerror("module definition in method body");
				}
				support.pushLocalScope();
				return yyVal;
			}
		};
		states[471] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-5 + yyTop]), null, ((RestArgNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]),
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[404] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((Node) yyVals[0 + yyTop]) instanceof EvStrNode
						? new DStrNode(support.getPosition(((Node) yyVals[0 + yyTop]))).add(((Node) yyVals[0 + yyTop]))
						: ((Node) yyVals[0 + yyTop]);
				/*
				 * NODE *node = $1; if (!node) { node = NEW_STR(STR_NEW0()); } else { node =
				 * evstr2dstr(node); } $$ = node;
				 */
				return yyVal;
			}
		};
		states[337] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[0 + yyTop])), null,
						new StarNode(support.getPosition(null)), null);
				return yyVal;
			}
		};
		states[69] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-3 + yyTop])), ((ListNode) yyVals[-3 + yyTop]),
						new StarNode(support.getPosition(null)), ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[270] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_colon2(support.union(((Node) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[2] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				/* ENEBO: Removed !compile_for_eval which probably is to reduce warnings */
				if (((Node) yyVals[0 + yyTop]) != null) {
					/* last expression should not be void */
					if (((Node) yyVals[0 + yyTop]) instanceof BlockNode) {
						support.checkUselessStatement(((BlockNode) yyVals[0 + yyTop]).getLast());
					} else {
						support.checkUselessStatement(((Node) yyVals[0 + yyTop]));
					}
				}
				support.getResult()
						.setAST(support.addRootNode(((Node) yyVals[0 + yyTop]), support.getPosition(((Node) yyVals[0 + yyTop]))));
				return yyVal;
			}
		};
		states[203] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getUnaryCallNode(support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "**", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null)), ((Token) yyVals[-3 + yyTop]));
				return yyVal;
			}
		};
		states[505] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[371] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.pushBlockScope();
				return yyVal;
			}
		};
		states[36] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newAndNode(support.getPosition(((Token) yyVals[-1 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[304] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[-1 + yyTop]);

				yyVal = new ModuleNode(support.union(((Token) yyVals[-4 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Colon3Node) yyVals[-3 + yyTop]), support.getCurrentScope(), body);
				support.popCurrentScope();
				return yyVal;
			}
		};
		states[539] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				return yyVal;
			}
		};
		states[472] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-1 + yyTop]), null, null, null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[405] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new StrNode(((Token) yyVals[-1 + yyTop]).getPosition(), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[338] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[-2 + yyTop])), null, null, ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[70] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[-1 + yyTop])), null, ((Node) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[271] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_colon3(support.union(((Token) yyVals[-1 + yyTop]), ((Token) yyVals[0 + yyTop])),
						(String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[3] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				Node node = ((Node) yyVals[-3 + yyTop]);

				if (((RescueBodyNode) yyVals[-2 + yyTop]) != null) {
					node = new RescueNode(support.getPosition(((Node) yyVals[-3 + yyTop]), true), ((Node) yyVals[-3 + yyTop]),
							((RescueBodyNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]));
				} else if (((Node) yyVals[-1 + yyTop]) != null) {
					support.warn(ID.ELSE_WITHOUT_RESCUE, support.getPosition(((Node) yyVals[-3 + yyTop])),
							"else without rescue is useless");
					node = support.appendToBlock(((Node) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop]));
				}
				if (((Node) yyVals[0 + yyTop]) != null) {
					node = new EnsureNode(support.getPosition(((Node) yyVals[-3 + yyTop])), node, ((Node) yyVals[0 + yyTop]));
				}

				yyVal = node;
				return yyVal;
			}
		};
		states[204] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getUnaryCallNode(support.getOperatorCallNode(((FloatNode) yyVals[-2 + yyTop]), "**",
						((Node) yyVals[0 + yyTop]), support.getPosition(null)), ((Token) yyVals[-3 + yyTop]));
				return yyVal;
			}
		};
		states[506] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (!(((Node) yyVals[0 + yyTop]) instanceof SelfNode)) {
					support.checkExpression(((Node) yyVals[0 + yyTop]));
				}
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[439] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.setState(LexState.EXPR_END);

				/* DStrNode: :"some text #{some expression}" */
				/* StrNode: :"some text" */
				/* EvStrNode :"#{some expression}" */
				/* Ruby 1.9 allows empty strings as symbols */
				if (((Node) yyVals[-1 + yyTop]) == null) {
					yyVal = new SymbolNode(((Token) yyVals[-2 + yyTop]).getPosition(), "");
				} else if (((Node) yyVals[-1 + yyTop]) instanceof DStrNode) {
					yyVal = new DSymbolNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])),
							((DStrNode) yyVals[-1 + yyTop]));
				} else if (((Node) yyVals[-1 + yyTop]) instanceof StrNode) {
					yyVal = new SymbolNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])),
							((StrNode) yyVals[-1 + yyTop]).getValue().toString().intern());
				} else {
					final SourcePosition position = support.union(((Node) yyVals[-1 + yyTop]), ((Token) yyVals[0 + yyTop]));

					/* We substract one since tsymbeg is longer than one */
					/* and we cannot union it directly so we assume quote */
					/* is one character long and subtract for it. */
					position.adjustStartOffset(-1);
					((Node) yyVals[-1 + yyTop]).setPosition(position);

					yyVal = new DSymbolNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
					((DSymbolNode) yyVal).add(((Node) yyVals[-1 + yyTop]));
				}
				return yyVal;
			}
		};
		states[372] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IterNode(support.union(((Token) yyVals[-4 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((ArgsNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
				support.popCurrentScope();
				return yyVal;
			}
		};
		states[104] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.setState(LexState.EXPR_ENDFN);
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[305] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.setInDef(true);
				support.pushLocalScope();
				return yyVal;
			}
		};
		states[37] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newOrNode(support.getPosition(((Token) yyVals[-1 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[540] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[473] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-3 + yyTop]), ((RestArgNode) yyVals[-1 + yyTop]), null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[406] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[339] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-5 + yyTop]), ((ListNode) yyVals[-3 + yyTop]), ((RestArgNode) yyVals[-1 + yyTop]), null,
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[71] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[-3 + yyTop])), null, ((Node) yyVals[-2 + yyTop]),
						((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[272] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition position = support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));
				if (((Node) yyVals[-1 + yyTop]) == null) {
					yyVal = new ZArrayNode(position); /* zero length array */
				} else {
					yyVal = yyVals[-1 + yyTop];
					((ISourcePositionHolder) yyVal).setPosition(position);
				}
				return yyVal;
			}
		};
		states[4] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (((Node) yyVals[-1 + yyTop]) instanceof BlockNode) {
					support.checkUselessStatements(((BlockNode) yyVals[-1 + yyTop]));
				}
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[205] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getUnaryCallNode(((Node) yyVals[0 + yyTop]), ((Token) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[507] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_BEG);
				return yyVal;
			}
		};
		states[440] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[373] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* Workaround for JRUBY-2326 (MRI does not enter this production for some reason) */
				if (((Node) yyVals[-1 + yyTop]) instanceof YieldNode) {
					throw new SyntaxException(PID.BLOCK_GIVEN_TO_YIELD, support.getPosition(((Node) yyVals[-1 + yyTop])),
							"block given to yield");
				}
				if (((BlockAcceptingNode) yyVals[-1 + yyTop]).getIter() instanceof BlockPassNode) {
					throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, support.getPosition(((Node) yyVals[-1 + yyTop])),
							"Both block arg and actual block given.");
				}
				((BlockAcceptingNode) yyVals[-1 + yyTop]).setIter(((IterNode) yyVals[0 + yyTop]));
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Node) yyVals[-1 + yyTop]), ((IterNode) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[239] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newArrayNode(support.getPosition(((Node) yyVals[0 + yyTop])), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[105] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.setState(LexState.EXPR_ENDFN);
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[306] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[-1 + yyTop]);

				yyVal = new DefnNode(support.union(((Token) yyVals[-5 + yyTop]), ((Token) yyVals[0 + yyTop])),
						new MethodNameNode(((Token) yyVals[-4 + yyTop]).getPosition(), (String) ((Token) yyVals[-4 + yyTop]).getValue()),
						((ArgsNode) yyVals[-2 + yyTop]), support.getCurrentScope(), body);
				support.popCurrentScope();
				support.setInDef(false);
				return yyVal;
			}
		};
		states[38] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Token) yyVals[-2 + yyTop]), support.getConditionNode(((Node) yyVals[0 + yyTop])));
				((CallNode) yyVal).setName("!");
				return yyVal;
			}
		};
		states[541] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[474] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-5 + yyTop]), ((RestArgNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]),
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[407] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.literal_concat(support.getPosition(((Node) yyVals[-1 + yyTop])), ((Node) yyVals[-1 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[340] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-7 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-7 + yyTop]), ((ListNode) yyVals[-5 + yyTop]), ((RestArgNode) yyVals[-3 + yyTop]),
						((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[72] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[0 + yyTop])), null,
						new StarNode(support.getPosition(null)), null);
				return yyVal;
			}
		};
		states[273] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new HashNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((ListNode) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[206] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getUnaryCallNode(((Node) yyVals[0 + yyTop]), ((Token) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[508] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (((Node) yyVals[-1 + yyTop]) == null) {
					support.yyerror("can't define single method for ().");
				} else if (((Node) yyVals[-1 + yyTop]) instanceof ILiteralNode) {
					support.yyerror("can't define single method for literals.");
				}
				support.checkExpression(((Node) yyVals[-1 + yyTop]));
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Token) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop])));
				return yyVal;
			}
		};
		states[441] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[374] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);

				return yyVal;
			}
		};
		states[106] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new LiteralNode(((Token) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[307] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_FNAME);
				return yyVal;
			}
		};
		states[39] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Token) yyVals[-1 + yyTop]), support.getConditionNode(((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[240] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.arg_blk_pass(((Node) yyVals[-1 + yyTop]), ((BlockPassNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[475] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-1 + yyTop]), null, null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[408] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];

				((ISourcePositionHolder) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				final int extraLength = ((String) ((Token) yyVals[-2 + yyTop]).getValue()).length() - 1;

				/* We may need to subtract addition offset off of first */
				/* string fragment (we optimistically take one off in */
				/* ParserSupport.literal_concat). Check token length */
				/* and subtract as neeeded. */
				if ((((Node) yyVals[-1 + yyTop]) instanceof DStrNode) && extraLength > 0) {
					final Node strNode = ((DStrNode) ((Node) yyVals[-1 + yyTop])).get(0);
					assert strNode != null;
					strNode.getPosition().adjustStartOffset(-extraLength);
				}
				return yyVal;
			}
		};
		states[341] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]), null, null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[73] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((Token) yyVals[-2 + yyTop])), null,
						new StarNode(support.getPosition(null)), ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[274] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ReturnNode(((Token) yyVals[0 + yyTop]).getPosition(), null);
				return yyVal;
			}
		};
		states[6] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newline_node(((Node) yyVals[0 + yyTop]), support.getPosition(((Node) yyVals[0 + yyTop]), true));
				return yyVal;
			}
		};
		states[207] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "|", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[509] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ArrayNode(support.getPosition(null));
				return yyVal;
			}
		};
		states[442] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.negateInteger(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[375] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);
				return yyVal;
			}
		};
		states[107] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new LiteralNode(((Token) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[308] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.setInSingle(support.getInSingle() + 1);
				support.pushLocalScope();
				lexer.setState(LexState.EXPR_ENDFN); /* force for args */
				return yyVal;
			}
		};
		states[241] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition pos = ((ListNode) yyVals[-1 + yyTop]).getPosition();
				yyVal = support.newArrayNode(pos, new HashNode(pos, ((ListNode) yyVals[-1 + yyTop])));
				yyVal = support.arg_blk_pass((Node) yyVal, ((BlockPassNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[476] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-3 + yyTop]), null, ((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[409] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition position = support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));

				if (((Node) yyVals[-1 + yyTop]) == null) {
					yyVal = new XStrNode(position, null);
				} else if (((Node) yyVals[-1 + yyTop]) instanceof StrNode) {
					yyVal = new XStrNode(position, ((StrNode) yyVals[-1 + yyTop]).getValue());
				} else if (((Node) yyVals[-1 + yyTop]) instanceof DStrNode) {
					yyVal = new DXStrNode(position, ((DStrNode) yyVals[-1 + yyTop]));

					((Node) yyVal).setPosition(position);
				} else {
					yyVal = new DXStrNode(position).add(((Node) yyVals[-1 + yyTop]));
				}
				return yyVal;
			}
		};
		states[342] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-5 + yyTop]), ((ListNode) yyVals[-3 + yyTop]), null, ((ListNode) yyVals[-1 + yyTop]),
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[275] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_yield(support.union(((Token) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[7] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.appendToBlock(((Node) yyVals[-2 + yyTop]),
						support.newline_node(((Node) yyVals[0 + yyTop]), support.getPosition(((Node) yyVals[0 + yyTop]), true)));
				return yyVal;
			}
		};
		states[208] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "^", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[510] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[443] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.negateFloat(((FloatNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[376] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_fcall(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);
				return yyVal;
			}
		};
		states[108] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[309] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[-1 + yyTop]);

				yyVal = new DefsNode(support.union(((Token) yyVals[-8 + yyTop]), ((Token) yyVals[0 + yyTop])), ((Node) yyVals[-7 + yyTop]),
						new MethodNameNode(((Token) yyVals[-4 + yyTop]).getPosition(), (String) ((Token) yyVals[-4 + yyTop]).getValue()),
						((ArgsNode) yyVals[-2 + yyTop]), support.getCurrentScope(), body);
				support.popCurrentScope();
				support.setInSingle(support.getInSingle() - 1);
				return yyVal;
			}
		};
		states[41] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[242] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.arg_append(((Node) yyVals[-3 + yyTop]),
						new HashNode(((ListNode) yyVals[-1 + yyTop]).getPosition(), ((ListNode) yyVals[-1 + yyTop])));
				yyVal = support.arg_blk_pass((Node) yyVal, ((BlockPassNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[477] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((RestArgNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null, null,
						((RestArgNode) yyVals[-1 + yyTop]), null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[410] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newRegexpNode(support.union(((Token) yyVals[-2 + yyTop]), ((RegexpNode) yyVals[0 + yyTop])),
						((Node) yyVals[-1 + yyTop]), ((RegexpNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[343] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-3 + yyTop]), null, ((RestArgNode) yyVals[-1 + yyTop]), null,
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[276] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ZYieldNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[8] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[209] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "&", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[75] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[377] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);
				return yyVal;
			}
		};
		states[109] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[310] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new BreakNode(((Token) yyVals[0 + yyTop]).getPosition(), null);
				return yyVal;
			}
		};
		states[243] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				return yyVal;
			}
		};
		states[478] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((RestArgNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null, null,
						((RestArgNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[411] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ZArrayNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[344] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final RestArgNode rest = new UnnamedRestArgNode(((ListNode) yyVals[-1 + yyTop]).getPosition(),
						support.getCurrentScope().addVariable("*"));
				yyVal = support.new_args(((ListNode) yyVals[-1 + yyTop]).getPosition(), ((ListNode) yyVals[-1 + yyTop]), null, rest, null,
						(BlockArgNode) null);
				return yyVal;
			}
		};
		states[9] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_FNAME);
				return yyVal;
			}
		};
		states[210] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "<=>", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[76] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newArrayNode(((Node) yyVals[-1 + yyTop]).getPosition(), ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[277] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ZYieldNode(((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[512] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).addAll(((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[378] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);
				return yyVal;
			}
		};
		states[110] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newUndef(support.getPosition(((Node) yyVals[0 + yyTop])), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[311] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new NextNode(((Token) yyVals[0 + yyTop]).getPosition(), null);
				return yyVal;
			}
		};
		states[244] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Long(lexer.getCmdArgumentState().begin());
				return yyVal;
			}
		};
		states[479] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(((BlockArgNode) yyVals[0 + yyTop]).getPosition(), null, null, null, null,
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[412] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[345] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-5 + yyTop]), null, ((RestArgNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]),
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[10] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newAlias(support.union(((Token) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[211] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), ">", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[77] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[278] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new DefinedNode(support.union(((Token) yyVals[-4 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[513] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				SourcePosition pos;
				if (((Node) yyVals[-2 + yyTop]) == null && ((Node) yyVals[0 + yyTop]) == null) {
					pos = support.getPosition(((Token) yyVals[-1 + yyTop]));
				} else {
					pos = support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				}

				yyVal = support.newArrayNode(pos, ((Node) yyVals[-2 + yyTop])).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[379] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]), null, null);
				return yyVal;
			}
		};
		states[312] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new RedoNode(((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[44] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ReturnNode(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						support.ret_args(((Node) yyVals[0 + yyTop]), ((Node) yyVals[0 + yyTop]).getPosition()));
				return yyVal;
			}
		};
		states[245] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.getCmdArgumentState().reset(((Long) yyVals[-1 + yyTop]).longValue());
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[111] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_FNAME);
				return yyVal;
			}
		};
		states[480] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.getPosition(null), null, null, null, null, (BlockArgNode) null);
				return yyVal;
			}
		};
		states[413] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ArrayNode(support.getPosition(null));
				return yyVal;
			}
		};
		states[346] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-1 + yyTop]), null, null, null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[11] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new VAliasNode(support.getPosition(((Token) yyVals[-2 + yyTop])), (String) ((Token) yyVals[-1 + yyTop]).getValue(),
						(String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[212] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), ">=", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[78] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newArrayNode(((Node) yyVals[0 + yyTop]).getPosition(), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[279] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Token) yyVals[-3 + yyTop]), support.getConditionNode(((Node) yyVals[-1 + yyTop])));
				((CallNode) yyVal).setPosition(support.union(((Token) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop])));
				((CallNode) yyVal).setName("!");
				return yyVal;
			}
		};
		states[514] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition pos = support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
				yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token) yyVals[-1 + yyTop]).getValue()))
						.add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[380] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-2 + yyTop]), new Token("call", ((Node) yyVals[-2 + yyTop]).getPosition()),
						((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);
				return yyVal;
			}
		};
		states[313] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new RetryNode(((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[45] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new BreakNode(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						support.ret_args(((Node) yyVals[0 + yyTop]), ((Node) yyVals[0 + yyTop]).getPosition()));
				return yyVal;
			}
		};
		states[246] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new BlockPassNode(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[112] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.appendToBlock(((Node) yyVals[-3 + yyTop]),
						support.newUndef(support.getPosition(((Node) yyVals[-3 + yyTop])), ((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[481] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("formal argument cannot be a constant");
				return yyVal;
			}
		};
		states[414] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[-1 + yyTop]) instanceof EvStrNode
						? new DStrNode(support.getPosition(((ListNode) yyVals[-2 + yyTop]))).add(((Node) yyVals[-1 + yyTop]))
						: ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[347] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-3 + yyTop]), ((RestArgNode) yyVals[-1 + yyTop]), null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[12] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new VAliasNode(support.getPosition(((Token) yyVals[-2 + yyTop])), (String) ((Token) yyVals[-1 + yyTop]).getValue(),
						"$" + ((BackRefNode) yyVals[0 + yyTop]).getType());
				return yyVal;
			}
		};
		states[213] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "<", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[79] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[280] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Token) yyVals[-2 + yyTop]), null);

				((CallNode) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				((CallNode) yyVal).setName("!");
				return yyVal;
			}
		};
		states[381] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-2 + yyTop]), new Token("call", ((Node) yyVals[-2 + yyTop]).getPosition()),
						((Node) yyVals[0 + yyTop]), null);
				((IArgumentNode) yyVal).setHasParens(true);

				return yyVal;
			}
		};
		states[46] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new NextNode(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						support.ret_args(((Node) yyVals[0 + yyTop]), ((Node) yyVals[0 + yyTop]).getPosition()));
				return yyVal;
			}
		};
		states[247] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[314] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[482] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("formal argument cannot be an instance variable");
				return yyVal;
			}
		};
		states[348] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-5 + yyTop]), ((RestArgNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]),
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[13] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("can't make alias for the number variables");
				return yyVal;
			}
		};
		states[214] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "<=", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[80] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.assignable(((Token) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[281] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_fcall(((Token) yyVals[-1 + yyTop]), null, ((IterNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[449] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("nil", Tokens.kNIL, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[382] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_super(((Node) yyVals[0 + yyTop]), ((Token) yyVals[-1 + yyTop]));
				((IArgumentNode) yyVal).setHasParens(true);

				return yyVal;
			}
		};
		states[248] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[483] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("formal argument cannot be a global variable");
				return yyVal;
			}
		};
		states[416] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.literal_concat(support.getPosition(((Node) yyVals[-1 + yyTop])), ((Node) yyVals[-1 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[349] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-1 + yyTop]), null, null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[14] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[215] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "==", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[81] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.aryset(((Node) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[450] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("self", Tokens.kSELF, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[383] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ZSuperNode(((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[48] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[484] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("formal argument cannot be a class variable");
				return yyVal;
			}
		};
		states[417] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ZArrayNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[350] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null,
						((ListNode) yyVals[-3 + yyTop]), null, ((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[15] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IfNode(support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])),
						support.getConditionNode(((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]), null);
				return yyVal;
			}
		};
		states[216] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "===", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[82] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.attrset(((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[283] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (((Node) yyVals[-1 + yyTop]) != null && ((BlockAcceptingNode) yyVals[-1 + yyTop]).getIter() instanceof BlockPassNode) {
					throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, support.getPosition(((Node) yyVals[-1 + yyTop])),
							"Both block arg and actual block given.");
				}
				((BlockAcceptingNode) yyVals[-1 + yyTop]).setIter(((IterNode) yyVals[0 + yyTop]));
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Node) yyVals[-1 + yyTop]), ((IterNode) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[451] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("true", Tokens.kTRUE, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[384] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (((Node) yyVals[-3 + yyTop]) instanceof SelfNode) {
					yyVal = support.new_fcall(new Token("[]", support.union(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop]))),
							((Node) yyVals[-1 + yyTop]), null);
				} else {
					yyVal = support.new_call(((Node) yyVals[-3 + yyTop]),
							new Token("[]", support.union(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop]))),
							((Node) yyVals[-1 + yyTop]), null);
				}
				return yyVal;
			}
		};
		states[49] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[250] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newArrayNode(support.getPosition2(((Node) yyVals[0 + yyTop])), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[418] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((ISourcePositionHolder) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[351] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((RestArgNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null, null,
						((RestArgNode) yyVals[-1 + yyTop]), null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[16] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IfNode(support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])),
						support.getConditionNode(((Node) yyVals[0 + yyTop])), null, ((Node) yyVals[-2 + yyTop]));
				return yyVal;
			}
		};
		states[217] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "!=", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[83] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.attrset(((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[284] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[452] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("false", Tokens.kFALSE, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[385] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.pushBlockScope();
				return yyVal;
			}
		};
		states[184] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.node_assign(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				/* FIXME: Consider fixing node_assign itself rather than single case */
				((Node) yyVal).setPosition(support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[50] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.pushBlockScope();
				return yyVal;
			}
		};
		states[251] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newSplatNode(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[486] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.formal_argument(((Token) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[419] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ArrayNode(support.getPosition(null));
				return yyVal;
			}
		};
		states[352] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((RestArgNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])), null, null,
						((RestArgNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[17] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (((Node) yyVals[-2 + yyTop]) != null && ((Node) yyVals[-2 + yyTop]) instanceof BeginNode) {
					yyVal = new WhileNode(support.getPosition(((Node) yyVals[-2 + yyTop])),
							support.getConditionNode(((Node) yyVals[0 + yyTop])), ((BeginNode) yyVals[-2 + yyTop]).getBodyNode(), false);
				} else {
					yyVal = new WhileNode(support.getPosition(((Node) yyVals[-2 + yyTop])),
							support.getConditionNode(((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]), true);
				}
				return yyVal;
			}
		};
		states[218] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getMatchNode(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				/*
				 * ENEBO $$ = match_op($1, $3); if (nd_type($1) == NODE_LIT && TYPE($1->nd_lit) ==
				 * T_REGEXP) { $$ = reg_named_capture_assign($1->nd_lit, $$); }
				 */
				return yyVal;
			}
		};
		states[84] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.attrset(((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[285] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IfNode(support.union(((Token) yyVals[-5 + yyTop]), ((Token) yyVals[0 + yyTop])),
						support.getConditionNode(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[453] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("__FILE__", Tokens.k__FILE__, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[386] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IterNode(support.union(((Token) yyVals[-4 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((ArgsNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
				support.popCurrentScope();
				return yyVal;
			}
		};
		states[51] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IterNode(support.getPosition(((Token) yyVals[-4 + yyTop])), ((ArgsNode) yyVals[-2 + yyTop]),
						((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
				support.popCurrentScope();
				return yyVal;
			}
		};
		states[252] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node lhs = support.splat_array(((Node) yyVals[-2 + yyTop]));

				if (lhs != null) {
					yyVal = support.list_append(lhs, ((Node) yyVals[0 + yyTop]));
				} else {
					yyVal = support.arg_append(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				}
				return yyVal;
			}
		};
		states[185] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition position = support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
				final Node body = ((Node) yyVals[0 + yyTop]);
				yyVal = support.node_assign(((Node) yyVals[-4 + yyTop]),
						new RescueNode(position, ((Node) yyVals[-2 + yyTop]), new RescueBodyNode(position, null, body, null), null));
				return yyVal;
			}
		};
		states[487] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.arg_var(((Token) yyVals[0 + yyTop]));
				/*
				 * $$ = new ArgAuxiliaryNode($1.getPosition(), (String) $1.getValue(), 1);
				 */
				return yyVal;
			}
		};
		states[420] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[353] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.getPosition(((BlockArgNode) yyVals[0 + yyTop])), null, null, null, null,
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[219] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new NotNode(support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])),
						support.getMatchNode(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[85] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.yyerror("dynamic constant assignment");
				}

				final SourcePosition position = support.union(((Node) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));

				yyVal = new ConstDeclNode(position, null,
						support.new_colon2(position, ((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue()), null);
				return yyVal;
			}
		};
		states[286] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IfNode(support.union(((Token) yyVals[-5 + yyTop]), ((Token) yyVals[0 + yyTop])),
						support.getConditionNode(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-1 + yyTop]), ((Node) yyVals[-2 + yyTop]));
				return yyVal;
			}
		};
		states[18] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (((Node) yyVals[-2 + yyTop]) != null && ((Node) yyVals[-2 + yyTop]) instanceof BeginNode) {
					yyVal = new UntilNode(support.getPosition(((Node) yyVals[-2 + yyTop])),
							support.getConditionNode(((Node) yyVals[0 + yyTop])), ((BeginNode) yyVals[-2 + yyTop]).getBodyNode(), false);
				} else {
					yyVal = new UntilNode(support.getPosition(((Node) yyVals[-2 + yyTop])),
							support.getConditionNode(((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]), true);
				}
				return yyVal;
			}
		};
		states[454] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("__LINE__", Tokens.k__LINE__, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[387] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.pushBlockScope();
				return yyVal;
			}
		};
		states[52] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_fcall(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[253] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				Node node = null;

				/* FIXME: lose syntactical elements here (and others like this) */
				if (((Node) yyVals[0 + yyTop]) instanceof ArrayNode && (node = support.splat_array(((Node) yyVals[-3 + yyTop]))) != null) {
					yyVal = support.list_concat(node, ((Node) yyVals[0 + yyTop]));
				} else {
					yyVal = support.arg_concat(support.union(((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop])),
							((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop]));
				}
				return yyVal;
			}
		};
		states[186] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));

				final String asgnOp = (String) ((Token) yyVals[-1 + yyTop]).getValue();
				if (asgnOp.equals("||")) {
					((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
					yyVal = new OpAsgnOrNode(support.union(((AssignableNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])),
							support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), ((AssignableNode) yyVals[-2 + yyTop]));
				} else if (asgnOp.equals("&&")) {
					((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
					yyVal = new OpAsgnAndNode(support.union(((AssignableNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])),
							support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), ((AssignableNode) yyVals[-2 + yyTop]));
				} else {
					((AssignableNode) yyVals[-2 + yyTop]).setValueNode(support.getOperatorCallNode(
							support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), asgnOp, ((Node) yyVals[0 + yyTop])));
					((AssignableNode) yyVals[-2 + yyTop])
							.setPosition(support.union(((AssignableNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
					yyVal = yyVals[-2 + yyTop];
				}
				return yyVal;
			}
		};
		states[488] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				/*
				 * { ID tid = internal_id(); arg_var(tid); if (dyna_in_block()) { $2->nd_value =
				 * NEW_DVAR(tid); } else { $2->nd_value = NEW_LVAR(tid); } $$ = NEW_ARGS_AUX(tid,
				 * 1); $$->nd_next = $2;
				 */
				return yyVal;
			}
		};
		states[421] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new StrNode(((Token) yyVals[0 + yyTop]).getPosition(), "");
				return yyVal;
			}
		};
		states[354] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* was $$ = null; */
				yyVal = support.new_args(support.getPosition(null), null, null, null, null, (BlockArgNode) null);
				return yyVal;
			}
		};
		states[220] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Token) yyVals[-1 + yyTop]), support.getConditionNode(((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[86] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.yyerror("dynamic constant assignment");
				}

				final SourcePosition position = support.union(((Token) yyVals[-1 + yyTop]), ((Token) yyVals[0 + yyTop]));

				yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token) yyVals[0 + yyTop]).getValue()),
						null);
				return yyVal;
			}
		};
		states[287] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.getConditionState().begin();
				return yyVal;
			}
		};
		states[19] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[0 + yyTop]);
				yyVal = new RescueNode(support.getPosition(((Node) yyVals[-2 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						new RescueBodyNode(support.getPosition(((Node) yyVals[-2 + yyTop])), null, body, null), null);
				return yyVal;
			}
		};
		states[455] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Token("__ENCODING__", Tokens.k__ENCODING__, ((Token) yyVals[0 + yyTop]).getPosition());
				return yyVal;
			}
		};
		states[388] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IterNode(support.union(((Token) yyVals[-4 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((ArgsNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
				support.popCurrentScope();
				return yyVal;
			}
		};
		states[53] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_fcall(((Token) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), ((IterNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[254] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node node = support.splat_array(((Node) yyVals[-2 + yyTop]));

				if (node != null) {
					yyVal = support.list_append(node, ((Node) yyVals[0 + yyTop]));
				} else {
					yyVal = support.arg_append(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				}
				return yyVal;
			}
		};
		states[321] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new IfNode(support.getPosition(((Token) yyVals[-4 + yyTop])), support.getConditionNode(((Node) yyVals[-3 + yyTop])),
						((Node) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[187] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[-2 + yyTop]));
				final SourcePosition position = support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
				final Node body = ((Node) yyVals[0 + yyTop]);
				Node rest;

				final String asgnOp = (String) ((Token) yyVals[-3 + yyTop]).getValue();
				if (asgnOp.equals("||")) {
					((AssignableNode) yyVals[-4 + yyTop]).setValueNode(((Node) yyVals[-2 + yyTop]));
					rest = new OpAsgnOrNode(support.union(((AssignableNode) yyVals[-4 + yyTop]), ((Node) yyVals[-2 + yyTop])),
							support.gettable2(((AssignableNode) yyVals[-4 + yyTop])), ((AssignableNode) yyVals[-4 + yyTop]));
				} else if (asgnOp.equals("&&")) {
					((AssignableNode) yyVals[-4 + yyTop]).setValueNode(((Node) yyVals[-2 + yyTop]));
					rest = new OpAsgnAndNode(support.union(((AssignableNode) yyVals[-4 + yyTop]), ((Node) yyVals[-2 + yyTop])),
							support.gettable2(((AssignableNode) yyVals[-4 + yyTop])), ((AssignableNode) yyVals[-4 + yyTop]));
				} else {
					((AssignableNode) yyVals[-4 + yyTop]).setValueNode(support.getOperatorCallNode(
							support.gettable2(((AssignableNode) yyVals[-4 + yyTop])), asgnOp, ((Node) yyVals[-2 + yyTop])));
					((AssignableNode) yyVals[-4 + yyTop])
							.setPosition(support.union(((AssignableNode) yyVals[-4 + yyTop]), ((Node) yyVals[-2 + yyTop])));
					rest = ((AssignableNode) yyVals[-4 + yyTop]);
				}
				yyVal = new RescueNode(((Token) yyVals[-1 + yyTop]).getPosition(), rest,
						new RescueBodyNode(((Token) yyVals[-1 + yyTop]).getPosition(), null, body, null), null);

				return yyVal;
			}
		};
		states[489] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ListNode(((ISourcePositionHolder) yyVals[0 + yyTop]).getPosition()).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[422] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.literal_concat(support.getPosition(((Node) yyVals[-1 + yyTop])), ((Node) yyVals[-1 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[355] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.commandStart = true;
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[221] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[87] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.backrefAssignError(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[288] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.getConditionState().end();
				return yyVal;
			}
		};
		states[20] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				/* FIXME: the == here is gross; need a cleaner way to check t */
				if (support.isInDef() || support.isInSingle() || support.getCurrentScope().getClass() == BlockStaticScope.class) {
					support.yyerror("BEGIN in method, singleton, or block");
				}
				return yyVal;
			}
		};
		states[456] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.gettable(((Token) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[389] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newWhenNode(
						support.union(((Token) yyVals[-4 + yyTop]), support.unwrapNewlineNode(((Node) yyVals[-1 + yyTop]))),
						((Node) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[54] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[255] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				Node node = null;

				if (((Node) yyVals[0 + yyTop]) instanceof ArrayNode && (node = support.splat_array(((Node) yyVals[-3 + yyTop]))) != null) {
					yyVal = support.list_concat(node, ((Node) yyVals[0 + yyTop]));
				} else {
					yyVal = support.arg_concat(support.union(((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop])),
							((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop]));
				}
				return yyVal;
			}
		};
		states[188] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* FIXME: arg_concat missing for opt_call_args */
				yyVal = support.new_opElementAsgnNode(support.union(((Node) yyVals[-5 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[-5 + yyTop]), (String) ((Token) yyVals[-1 + yyTop]).getValue(), ((Node) yyVals[-3 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[490] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[0 + yyTop]));
				yyVal = yyVals[-2 + yyTop];
				return yyVal;
			}
		};
		states[423] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[356] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])), null, null, null, null,
						(BlockArgNode) null);
				((ArgsNode) yyVal).setShadow(((ListNode) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[88] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* if (!($$ = assignable($1, 0))) $$ = NEW_BEGIN(0); */
				yyVal = support.assignable(((Token) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[289] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[-1 + yyTop]);
				yyVal = new WhileNode(support.union(((Token) yyVals[-6 + yyTop]), ((Token) yyVals[0 + yyTop])),
						support.getConditionNode(((Node) yyVals[-4 + yyTop])), body);
				return yyVal;
			}
		};
		states[21] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.getResult().addBeginNode(new PreExe19Node(support.getPosition(((Node) yyVals[-1 + yyTop])),
						support.getCurrentScope(), ((Node) yyVals[-1 + yyTop])));
				yyVal = null;
				return yyVal;
			}
		};
		states[222] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "<<", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[457] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.assignable(((Token) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[256] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newSplatNode(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[323] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[189] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]),
						((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(),
						(String) ((Token) yyVals[-1 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[55] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-4 + yyTop]), ((Token) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]),
						((IterNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[491] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.arg_var(support.formal_argument(((Token) yyVals[-2 + yyTop])));
				yyVal = new OptArgNode(((Token) yyVals[-2 + yyTop]).getPosition(),
						support.assignable(((Token) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[424] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.literal_concat(support.getPosition(((Node) yyVals[-1 + yyTop])), ((Node) yyVals[-1 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[357] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(((Token) yyVals[0 + yyTop]).getPosition(), null, null, null, null, (BlockArgNode) null);
				return yyVal;
			}
		};
		states[89] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.aryset(((Node) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[290] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.getConditionState().begin();
				return yyVal;
			}
		};
		states[22] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.warn(ID.END_IN_METHOD, support.getPosition(((Token) yyVals[-3 + yyTop])), "END in method; use at_exit");
				}
				yyVal = new PostExeNode(support.getPosition(((Node) yyVals[-1 + yyTop])), ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[223] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), ">>", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[458] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[190] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]),
						((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(),
						(String) ((Token) yyVals[-1 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[56] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), ((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[492] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.arg_var(support.formal_argument(((Token) yyVals[-2 + yyTop])));
				yyVal = new OptArgNode(((Token) yyVals[-2 + yyTop]).getPosition(),
						support.assignable(((Token) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[425] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[358] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				((Node) yyVals[-2 + yyTop]).setPosition(support.union(((Token) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop])));
				((ArgsNode) yyVals[-2 + yyTop]).setShadow(((ListNode) yyVals[-1 + yyTop]));
				yyVal = yyVals[-2 + yyTop];
				return yyVal;
			}
		};
		states[90] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.attrset(((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[291] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.getConditionState().end();
				return yyVal;
			}
		};
		states[23] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));
				yyVal = support.node_assign(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[224] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newAndNode(support.getPosition(((Token) yyVals[-1 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[459] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[392] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				Node node;
				if (((Node) yyVals[-3 + yyTop]) != null) {
					node = support.appendToBlock(
							support.node_assign(((Node) yyVals[-3 + yyTop]),
									new GlobalVarNode(support.getPosition(((Token) yyVals[-5 + yyTop])), "$!")),
							((Node) yyVals[-1 + yyTop]));
					if (((Node) yyVals[-1 + yyTop]) != null) {
						node.setPosition(support.unwrapNewlineNode(((Node) yyVals[-1 + yyTop])).getPosition());
					}
				} else {
					node = ((Node) yyVals[-1 + yyTop]);
				}
				final Node body = node;
				yyVal = new RescueBodyNode(support.getPosition(((Token) yyVals[-5 + yyTop]), true), ((Node) yyVals[-4 + yyTop]), body,
						((RescueBodyNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[325] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				return yyVal;
			}
		};
		states[191] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]),
						((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(),
						(String) ((Token) yyVals[-1 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[57] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_call(((Node) yyVals[-4 + yyTop]), ((Token) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]),
						((IterNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[493] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new BlockNode(support.getPosition(((Node) yyVals[0 + yyTop]))).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[426] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = lexer.getStrTerm();
				lexer.setStrTerm(null);
				lexer.setState(LexState.EXPR_BEG);
				return yyVal;
			}
		};
		states[359] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[91] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.attrset(((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[292] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[-1 + yyTop]);
				yyVal = new UntilNode(support.union(((Token) yyVals[-6 + yyTop]), ((Token) yyVals[0 + yyTop])),
						support.getConditionNode(((Node) yyVals[-4 + yyTop])), body);
				return yyVal;
			}
		};
		states[24] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));
				((MultipleAsgnNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
				yyVal = yyVals[-2 + yyTop];
				return yyVal;
			}
		};
		states[225] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newOrNode(support.getPosition(((Token) yyVals[-1 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[460] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[393] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[326] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.assignable(((Token) yyVals[0 + yyTop]), null);
				return yyVal;
			}
		};
		states[192] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("constant re-assignment");
				return yyVal;
			}
		};
		states[58] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_super(((Node) yyVals[0 + yyTop]),
						((Token) yyVals[-1 + yyTop])); /* .setPosFrom($2); */
				return yyVal;
			}
		};
		states[494] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.appendToBlock(((ListNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[427] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.setStrTerm(((StrTerm) yyVals[-1 + yyTop]));
				yyVal = new EvStrNode(support.union(((Token) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[360] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[293] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newCaseNode(support.union(((Token) yyVals[-4 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[25] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));

				final SourcePosition pos = support.union(((AssignableNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				final String asgnOp = (String) ((Token) yyVals[-1 + yyTop]).getValue();
				if (asgnOp.equals("||")) {
					((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
					yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode) yyVals[-2 + yyTop])),
							((AssignableNode) yyVals[-2 + yyTop]));
				} else if (asgnOp.equals("&&")) {
					((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
					yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode) yyVals[-2 + yyTop])),
							((AssignableNode) yyVals[-2 + yyTop]));
				} else {
					((AssignableNode) yyVals[-2 + yyTop]).setValueNode(support.getOperatorCallNode(
							support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), asgnOp, ((Node) yyVals[0 + yyTop])));
					((AssignableNode) yyVals[-2 + yyTop]).setPosition(pos);
					yyVal = yyVals[-2 + yyTop];
				}
				return yyVal;
			}
		};
		states[226] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* ENEBO: arg surrounded by in_defined set/unset */
				yyVal = new DefinedNode(support.getPosition(((Token) yyVals[-2 + yyTop])), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[92] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.attrset(((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[461] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_BEG);
				return yyVal;
			}
		};
		states[394] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newArrayNode(((Node) yyVals[0 + yyTop]).getPosition(), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[327] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[193] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("constant re-assignment");
				return yyVal;
			}
		};
		states[59] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_yield(support.union(((Token) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[495] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new BlockNode(support.getPosition(((Node) yyVals[0 + yyTop]))).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[428] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = lexer.getStrTerm();
				lexer.getConditionState().stop();
				lexer.getCmdArgumentState().stop();
				lexer.setStrTerm(null);
				lexer.setState(LexState.EXPR_BEG);
				return yyVal;
			}
		};
		states[361] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ArrayNode(((Node) yyVals[0 + yyTop]).getPosition(), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[294] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newCaseNode(support.union(((Token) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop])), null,
						((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[26] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* FIXME: arg_concat logic missing for opt_call_args */
				yyVal = support.new_opElementAsgnNode(support.union(((Node) yyVals[-5 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((Node) yyVals[-5 + yyTop]), (String) ((Token) yyVals[-1 + yyTop]).getValue(), ((Node) yyVals[-3 + yyTop]),
						((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[227] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* FIXME: At best we need to subtract one in offset... */
				yyVal = new IfNode(support.getPosition(((Node) yyVals[-5 + yyTop])), support.getConditionNode(((Node) yyVals[-5 + yyTop])),
						((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[93] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.yyerror("dynamic constant assignment");
				}

				final SourcePosition position = support.union(((Node) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));

				yyVal = new ConstDeclNode(position, null,
						support.new_colon2(position, ((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue()), null);
				return yyVal;
			}
		};
		states[462] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[395] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.splat_array(((Node) yyVals[0 + yyTop]));
				if (yyVal == null)
					yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[328] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.newArrayNode(((Node) yyVals[0 + yyTop]).getPosition(), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[194] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.backrefAssignError(((Node) yyVals[-2 + yyTop]));
				return yyVal;
			}
		};
		states[496] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.appendToBlock(((ListNode) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[429] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.setStrTerm(((StrTerm) yyVals[-2 + yyTop]));

				yyVal = support.newEvStrNode(support.union(((Token) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[362] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[295] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.getConditionState().begin();
				return yyVal;
			}
		};
		states[27] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]),
						((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(),
						(String) ((Token) yyVals[-1 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[228] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[94] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.yyerror("dynamic constant assignment");
				}

				final SourcePosition position = support.union(((Token) yyVals[-1 + yyTop]), ((Token) yyVals[0 + yyTop]));

				yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token) yyVals[0 + yyTop]).getValue()),
						null);
				return yyVal;
			}
		};
		states[463] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[329] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[195] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[-2 + yyTop]));
				support.checkExpression(((Node) yyVals[0 + yyTop]));

				final boolean isLiteral = ((Node) yyVals[-2 + yyTop]) instanceof FixnumNode
						&& ((Node) yyVals[0 + yyTop]) instanceof FixnumNode;
				yyVal = new DotNode(support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]), false, isLiteral);
				return yyVal;
			}
		};
		states[61] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((Node) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[430] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new GlobalVarNode(((Token) yyVals[0 + yyTop]).getPosition(), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[363] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_bv(((Token) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[28] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]),
						((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(),
						(String) ((Token) yyVals[-1 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[229] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[0 + yyTop]));
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[95] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.backrefAssignError(((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[296] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.getConditionState().end();
				return yyVal;
			}
		};
		states[531] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[464] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				((ISourcePositionHolder) yyVal).setPosition(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])));
				lexer.setState(LexState.EXPR_BEG);
				return yyVal;
			}
		};
		states[397] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[330] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[0 + yyTop])), ((ListNode) yyVals[0 + yyTop]), null,
						null);
				return yyVal;
			}
		};
		states[196] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.checkExpression(((Node) yyVals[-2 + yyTop]));
				support.checkExpression(((Node) yyVals[0 + yyTop]));

				final boolean isLiteral = ((Node) yyVals[-2 + yyTop]) instanceof FixnumNode
						&& ((Node) yyVals[0 + yyTop]) instanceof FixnumNode;
				yyVal = new DotNode(support.union(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]), true, isLiteral);
				return yyVal;
			}
		};
		states[62] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[431] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new InstVarNode(((Token) yyVals[0 + yyTop]).getPosition(), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[364] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = null;
				return yyVal;
			}
		};
		states[29] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]),
						((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(),
						(String) ((Token) yyVals[-1 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[96] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.yyerror("class/module name must be CONSTANT");
				return yyVal;
			}
		};
		states[297] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				/* ENEBO: Lots of optz in 1.9 parser here */
				yyVal = new ForNode(support.union(((Token) yyVals[-8 + yyTop]), ((Token) yyVals[0 + yyTop])), ((Node) yyVals[-7 + yyTop]),
						((Node) yyVals[-1 + yyTop]), ((Node) yyVals[-4 + yyTop]), support.getCurrentScope());
				return yyVal;
			}
		};
		states[532] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[465] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[331] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-3 + yyTop])), ((ListNode) yyVals[-3 + yyTop]),
						support.assignable(((Token) yyVals[0 + yyTop]), null), null);
				return yyVal;
			}
		};
		states[197] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {

				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "+", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[63] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition pos = support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop]));
				yyVal = new MultipleAsgnNode(pos, support.newArrayNode(pos, ((Node) yyVals[-1 + yyTop])), null, null);
				return yyVal;
			}
		};
		states[499] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				if (!support.is_local_id(((Token) yyVals[0 + yyTop]))) {
					support.yyerror("duplicate rest argument name");
				}

				yyVal = new RestArgNode(((Token) yyVals[-1 + yyTop]).getPosition().union(((Token) yyVals[0 + yyTop]).getPosition()),
						support.arg_var(support.shadowing_lvar(((Token) yyVals[0 + yyTop]))));
				return yyVal;
			}
		};
		states[432] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new ClassVarNode(((Token) yyVals[0 + yyTop]).getPosition(), (String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[365] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				support.pushBlockScope();
				yyVal = lexer.getLeftParenBegin();
				lexer.setLeftParenBegin(lexer.incrementParenNest());
				return yyVal;
			}
		};
		states[30] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				support.backrefAssignError(((Node) yyVals[-2 + yyTop]));
				return yyVal;
			}
		};
		states[231] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-1 + yyTop];
				return yyVal;
			}
		};
		states[298] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				if (support.isInDef() || support.isInSingle()) {
					support.yyerror("class definition in method body");
				}
				support.pushLocalScope();
				return yyVal;
			}
		};
		states[466] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-5 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-5 + yyTop]), ((ListNode) yyVals[-3 + yyTop]), ((RestArgNode) yyVals[-1 + yyTop]), null,
						((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[399] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[0 + yyTop];
				return yyVal;
			}
		};
		states[332] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-5 + yyTop])), ((ListNode) yyVals[-5 + yyTop]),
						support.assignable(((Token) yyVals[-2 + yyTop]), null), ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[198] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "-", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[64] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[0 + yyTop])), ((ListNode) yyVals[0 + yyTop]), null,
						null);
				return yyVal;
			}
		};
		states[265] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_fcall(((Token) yyVals[0 + yyTop]), null, null);
				return yyVal;
			}
		};
		states[500] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new UnnamedRestArgNode(((Token) yyVals[0 + yyTop]).getPosition(),
						support.getCurrentScope().getLocalScope().addVariable("*"));
				return yyVal;
			}
		};
		states[366] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new LambdaNode(support.union(((ArgsNode) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((ArgsNode) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), support.getCurrentScope());
				support.popCurrentScope();
				lexer.setLeftParenBegin(((Integer) yyVals[-2 + yyTop]));
				return yyVal;
			}
		};
		states[31] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.node_assign(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[232] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.arg_append(((Node) yyVals[-3 + yyTop]),
						new HashNode(((ListNode) yyVals[-1 + yyTop]).getPosition(), ((ListNode) yyVals[-1 + yyTop])));
				return yyVal;
			}
		};
		states[98] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_colon3(support.union(((Token) yyVals[-1 + yyTop]), ((Token) yyVals[0 + yyTop])),
						(String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[299] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final Node body = ((Node) yyVals[-1 + yyTop]);

				yyVal = new ClassNode(support.union(((Token) yyVals[-5 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Colon3Node) yyVals[-4 + yyTop]), support.getCurrentScope(), body, ((Node) yyVals[-3 + yyTop]));
				support.popCurrentScope();
				return yyVal;
			}
		};
		states[467] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-7 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-7 + yyTop]), ((ListNode) yyVals[-5 + yyTop]), ((RestArgNode) yyVals[-3 + yyTop]),
						((ListNode) yyVals[-1 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[333] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-2 + yyTop])), ((ListNode) yyVals[-2 + yyTop]),
						new StarNode(support.getPosition(null)), null);
				return yyVal;
			}
		};
		states[199] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "*", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[65] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.union(((Node) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop])),
						((ListNode) yyVals[-1 + yyTop]).add(((Node) yyVals[0 + yyTop])), null, null);
				return yyVal;
			}
		};
		states[266] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new BeginNode(support.union(((Token) yyVals[-2 + yyTop]), ((Token) yyVals[0 + yyTop])),
						((Node) yyVals[-1 + yyTop]));
				return yyVal;
			}
		};
		states[434] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				lexer.setState(LexState.EXPR_END);
				yyVal = yyVals[0 + yyTop];
				((ISourcePositionHolder) yyVal).setPosition(support.union(((Token) yyVals[-1 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[367] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = yyVals[-2 + yyTop];
				((ISourcePositionHolder) yyVal).setPosition(support.union(((Token) yyVals[-3 + yyTop]), ((Token) yyVals[0 + yyTop])));
				return yyVal;
			}
		};
		states[32] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				((MultipleAsgnNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
				yyVal = yyVals[-2 + yyTop];
				return yyVal;
			}
		};
		states[233] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				final SourcePosition pos = ((ListNode) yyVals[-1 + yyTop]).getPosition();
				yyVal = support.newArrayNode(pos, new HashNode(pos, ((ListNode) yyVals[-1 + yyTop])));
				return yyVal;
			}
		};
		states[99] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_colon2(((Token) yyVals[0 + yyTop]).getPosition(), null,
						(String) ((Token) yyVals[0 + yyTop]).getValue());
				return yyVal;
			}
		};
		states[300] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new Boolean(support.isInDef());
				support.setInDef(false);
				return yyVal;
			}
		};
		states[468] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.new_args(support.union(((ListNode) yyVals[-3 + yyTop]), ((BlockArgNode) yyVals[0 + yyTop])),
						((ListNode) yyVals[-3 + yyTop]), ((ListNode) yyVals[-1 + yyTop]), null, null, ((BlockArgNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[334] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-4 + yyTop])), ((ListNode) yyVals[-4 + yyTop]),
						new StarNode(support.getPosition(null)), ((ListNode) yyVals[0 + yyTop]));
				return yyVal;
			}
		};
		states[200] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "/", ((Node) yyVals[0 + yyTop]),
						support.getPosition(null));
				return yyVal;
			}
		};
		states[66] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, Object yyVal, final Object[] yyVals, final int yyTop) {
				yyVal = new MultipleAsgnNode(support.getPosition(((ListNode) yyVals[-2 + yyTop])), ((ListNode) yyVals[-2 + yyTop]),
						((Node) yyVals[0 + yyTop]), (ListNode) null);
				return yyVal;
			}
		};
		states[267] = new ParserState() {
			@Override
			public Object execute(final ParserSupport support, final Lexer lexer, final Object yyVal, final Object[] yyVals,
					final int yyTop) {
				lexer.setState(LexState.EXPR_ENDARG);
				return yyVal;
			}
		};
	}
	// line 2024 "Ruby19Parser.y"

	/**
	 * The parse method use an lexer stream and parse it to an AST node structure
	 */
	@Override
	public ParserResult parse(final ParserConfiguration configuration, final LexerSource source) throws IOException {
		support.reset();
		support.setConfiguration(configuration);
		support.setResult(new ParserResult());

		lexer.reset();
		lexer.setSource(source);

		Object debugger = null;
		if (configuration.isDebug()) {
			try {
				final Class yyDebugAdapterClass = Class.forName("org.jay.yydebug.yyDebugAdapter");
				debugger = yyDebugAdapterClass.newInstance();
			} catch (final IllegalAccessException iae) {
				// ignore, no debugger present
			} catch (final InstantiationException ie) {
				// ignore, no debugger present
			} catch (final ClassNotFoundException cnfe) {
				// ignore, no debugger present
			}
		}
		//yyparse(lexer, new org.jay.yydebug.yyAnim("JRuby", 9));
		yyparse(lexer, debugger);

		return support.getResult();
	}

	// +++
	// Helper Methods
}
// line 8111 "-"
