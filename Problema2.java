import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Problema2 {
	
	static int MOD = 40009;
	/**
	 * Inmulteste matricele A si B. Operatiile sunt modulo MOD 
	 */
	private static int[][] multiplyMatrix(int[][] A, int[][] B) {
		final int N = A.length;
		final int M = B[0].length;
		final int K = A[0].length;
		int[][] Res = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				for (int k = 0; k < K; k++) {
					Res[i][j] = (Res[i][j] + A[i][k] * B[k][j]) % MOD;
				}
			}
		}
		return Res;
	}
	/**
	 * Ridica la puterea p matricea patratica A. Operatiile sunt modulo MOD
	 */
	private static int[][] logPowMatrix(int[][] A, int p) {
		int[][] Res = new int[A.length][A.length];
		int[][] aux = A;
		for (int i = 0; i < Res.length; i++) {
			Res[i][i] = 1;
		}
        long unu = 1;
		for (int i = 0; ((unu << i) <= p); i++) {
			if (((unu << i) & p) > 0)
				Res = multiplyMatrix(Res, aux);
			aux = multiplyMatrix(aux, aux);
		}
		return Res;
	}
	/**
     * Ridica la puterea p numarul x. Operatiile sunt modulo MOD
	 */
	private static int logPowNum(int x, int p) {
		int ret = 1;
		int aux = x;
		long unu = 1;
		/**
		 * Se scrie x ca putere a lui 2. Daca bitul i este unu, atunci 2 ^ i apare
		 * in descompunere si rezultatul trebuie inmultit cu 2 ^ i
		 * 1 trebuie tinut pe long pentru ca poate sa faca overflow in cazul in care
		 * p este mai mare de 2 ^ 31
		 */
		for (int i = 0; ((unu << i) <= p); i++) {
			if (((unu << i) & p) > 0)
				ret = (ret * aux) % MOD;
			aux = (aux * aux) % MOD;
		}
		return ret;
	}

	private static int solve(int n, int m, int k) {
		int[] v = new int[k + 2];
		v[0] = 1;
		/**
		 * Este evident ca pentru n <= k solutia este 2 ^ k, deoarece este
		 * imposibil sa avem o subsecventa cu lungime mai mare decat k intr-un sir
		 * de lungime mai mica sau egala cu k. v[0] = 1 din cauza recurentei
		 */
		for (int i = 1; i <= k + 1; i++)
			v[i] = (v[i - 1] * 2) % MOD;
		v[k + 1]--;
		/**
		 * Construim matricea asociata recurentei liniare
		 * v[i] = 2 * v[i - 1] - v[i - k - 2]. Vezi README pentru explicatii detaliate
		 * despre de ce recurenta este asa
		 */
		int[][] A = new int[k + 2][k + 2];
		for (int i = 0; i < k + 2; i++)
			for (int j = 0; j < k + 2; j++)
				A[i][j] = 0;
		for (int i = 1; i <= k + 1; i++)
			A[i][i - 1] = 1;
		A[0][k + 1] = MOD - 1;
		A[k + 1][k + 1] = 2;
		/**
		 * Se calculeaza rezultatul pentru o singura coloana si se ridica la m,
		 * deoarece coloanele sunt indepedente
		 */
		if (n <= k + 1)
			return logPowNum(v[n], m);
		else {
			A = logPowMatrix(A, n - k - 1);
			int ans = 0;
			for (int i = 0; i < k + 2; i++)
				ans = (ans + (v[i] * A[i][k + 1])) % MOD;
			if (ans < 0)
				ans += MOD;
			return logPowNum(ans, m);
		}
	}
	   
	   public static void main(String[] args) throws FileNotFoundException {
		   Scanner sc = new Scanner(new File("date.in"));
		   int n = sc.nextInt();
		   int m = sc.nextInt();
		   int k = sc.nextInt();
		   PrintWriter pw = new PrintWriter("date.out");
		   pw.write(solve(n, m, k) + "\n");
		   sc.close();
		   pw.close();
	   }
}
