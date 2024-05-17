package indentProgram;

public class IndentExec {
	static String strLine;
	static String strLineBuffer;
	static int currentIndent = 0;
	static boolean isCurrentComment = false;
	static boolean isNextComment = false;
	static boolean commentIndentConfig = false;
	
	public static String indentExec(String str) { 
		strLine = strLineBuffer = str;
		checkLiteral();									// リテラルチェック	
		checkComments();									// コメント行チェック

		if (!isCurrentComment){					// 現在の行がコメント行でなければインデント実行																// 行中のコメントは除く
			strLine = strLine.trim();				// 現在のインデントを削除
//		System.out.println(strLineBuffer);			// テスト用　評価用行表示
			indent();								// インデント実行
		} else {									// 現在の行がコメント行であれば次を実行
			if(!isNextComment) {						// 次の行がコメント行でなければ次を実行
				isCurrentComment = false;			// コメントフラグを削除
			}
		}
		return strLine;
	}
	
	// コメントチェック
	// コメント行内 {} スルー
	// 	& コメント行用のインデント設定
	//		& コメント行を評価用行バッファから削除
	public static void checkComments() {
//		System.out.println("before:" + this.strLine);	// テスト用
		if (isCurrentComment) {						// 現行がコメント行ならば次を実行
			int i = strLineBuffer.indexOf("*/");		// コメント終了文字チェック
			if (i != -1) {								// コメント終了文字があれば次を実行
				isNextComment = false;					// 次行コメントフラグ設定
				strLineBuffer = strLineBuffer.substring(i + 2);		// 評価用行バッファからコメント削除
			} else {
				isNextComment = true;					// 次行コメントフラグ設定
				strLineBuffer = "";						//  評価用行バッファからコメント削除
			}
		}
	 	int k = strLineBuffer.indexOf("/*");		// コメント開始文字チェック
		if (k != -1) {						// コメント開始文字があれば次を実行
			isCurrentComment = true;	// 現行・次行コメントフラグ設定
			isNextComment = true;
			int j = strLineBuffer.indexOf("*/");				// コメント終了文字チェック
			if (j != -1 && j > k) {						// コメント終了文字があり、開始文字より後に終了文字があれば次を実行
				isCurrentComment = commentIndentConfig ? false : true;
				isNextComment = false;	//現行・次行コメントフラグ設定
				strLineBuffer = strLineBuffer.substring(0, k) + strLineBuffer.substring(j + 2);	// 評価用行バッファからコメント削除
			} else {
				strLineBuffer = strLineBuffer.substring(0, k);		// 評価用行バッファからコメント削除
			}
		}
		int m = strLineBuffer.indexOf("//");			// コメント文字チェック
		if (m != -1) {							// コメント文字があれば次を実行
			strLineBuffer = strLineBuffer.substring(0, m);	// 評価用行バッファからコメント削除
			strLineBuffer = strLineBuffer.trim();
			if (strLineBuffer.isEmpty()) {
				isCurrentComment = commentIndentConfig ? false : true;
			}
		}
//			System.out.println("after :" + strLineBuffer);	// テスト用
		return;
	}
	
	// リテラルスルー
	// 文字列リテラル内波括弧のスルー設定
	public static void checkLiteral() {
		// エスケープシーケンス文字チェック
		char a;
		int flagIndex = 0;
		boolean isLiteral = false;						// 文字列リテラル開始フラッグ

		for (int index = 0; index < strLineBuffer.length(); index++) {
			a = strLineBuffer.charAt(index);
			if (isLiteral) {							// 文字列リテラル内であれば次を実行
				if (a == '\\'){
					if ((index - 1) != flagIndex) {			// \文字1文字目検出
						flagIndex = index;					// \文字の次文字用検出フラグ
					} else  {
						flagIndex = 0;						// \\文字検出時、何もせずフラグリセット
					}
				}
			}
			if (a == '"') {									// " 文字を検出した場合次を実行
				if (isLiteral && (index - 1) == flagIndex) {		// 文字列リテラル内 \" 検出
//					System.out.println(index + ":" + flagIndex + strLineBuffer);		// テスト用
					strLineBuffer = strLineBuffer.substring(0, flagIndex) + "  " + strLineBuffer.substring(flagIndex + 2, strLineBuffer.length());	// \" 文字を空白に変換
//					System.out.println("after:" + strLineBuffer);				// テスト用
					flagIndex = 0;								// \文字検出フラグリセット
				} else {
					isLiteral = !isLiteral;					// 文字列リテラル開始フラッグリセット
				}
			}
		}
		
		// 文字リテラル｛｝スルー
		// 文字リテラルを評価用行から削除
		String[] moji = { "\"" , "'" };
		for (String m : moji) {
			String[] s = strLineBuffer.split("[" + m + "]");
			int i =0;
			for (String w : s) {
				if (i % 2 == 1) {
					strLineBuffer = strLineBuffer.replace(m + w + m, "");
				}
				i++;
			}
		}
		return;
	}
	
	// インデント実行
	// 評価用行バッファ文字列を評価してインデントを設定、インデント挿入
	public static void indent() {
		int nextIndent = 0;										// 次の行のインデント変数
		int closeBrace		= strLineBuffer.indexOf("}");		// 閉じ波括弧のチェック(前検索)
		int openBrace 		= strLineBuffer.indexOf("{");		// 波括弧のチェック(前検索)
		int closeBraceLast 	= strLineBuffer.lastIndexOf("}");	// 閉じ波括弧のチェック(後検索)
		int openBraceLast  	= strLineBuffer.lastIndexOf("{");	// 波括弧のチェック(後検索)
		if (closeBrace != -1) {									// 最初に閉じ括弧があれば次を実行
			if (openBrace != -1) {
				if (closeBrace < openBrace) {
					currentIndent--;								// 現行インデント上げ
				}
			} else {
				currentIndent--;								// 現行インデント上げ
			}
		}
		if (openBraceLast != -1) {								// 最後に波括弧があれば次を実行
			if (closeBraceLast != -1) {
				if (openBraceLast > closeBraceLast){
					nextIndent++;								// 次行インデント下げ
				}	
			} else {
				nextIndent++;									// 次行インデント下げ
			}
		}
		indentInsert();
		currentIndent += nextIndent;							// 次行インデント設定
		return;
		
	}
	// インデント挿入
	public static void indentInsert() {
		for(int i = 0; i < currentIndent; i++) {
			strLine = "\t" + strLine;
		}
		return;
	}
}
