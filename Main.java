package indentProgram;

import static indentProgram.IndentExec.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {

	public static void main(String[] args) {
		
		commentIndentConfig = false;	// コメント行インデント対象設定(true:対象　false:対象外)

		try (
		// 読み込みファイル設定
			FileInputStream f = new FileInputStream("C:/test.txt");
//			FileInputStream f = new FileInputStream("test.txt");  // テスト用
			InputStreamReader fr = new InputStreamReader(f, "utf-8"); 
			BufferedReader br = new BufferedReader(fr);
				
		//書き込みファイル設定
			FileOutputStream nf = new FileOutputStream("C:/test/Bb.java");
//			FileOutputStream nf = new FileOutputStream("Bb.java");  // テスト用
			OutputStreamWriter nfr = new OutputStreamWriter(nf, "utf-8"); 
			BufferedWriter nbr = new BufferedWriter(nfr)) {
			
			String strLine;						// 一行読込み文字列変数
		// メイン
			while ((strLine = br.readLine()) != null) {
				strLine = indentExec(strLine);
				nbr.write(strLine);				// インデント実行結果をファイルに書込み
				nbr.newLine();					// 改行を出力
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}