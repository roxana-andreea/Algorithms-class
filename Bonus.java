import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Bonus {

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
		for (int i = 0; i < Res.length; ++i) {
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
		int ret = 1, aux = x;
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
		 * de lungime mai mica sau egala cu K. v[0] = 1 din cauza recurentei
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
		 * Se calculeaza rezultatul pentru o singura coloana si se ridica la m
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
		PrintWriter pw = new PrintWriter(new File("date.out"));
		int n = sc.nextInt();
		int m1 = sc.nextInt();
		int m2 = sc.nextInt();
		int k = sc.nextInt();
		/**
		 * Calculam cate coloane cu mai mult de k sunt, si cate cu mai putin de k.
		 * Deaorece cele 2 seturi sunt disjuncte, atunci numarul celor cu mai mult de k
		 * este 2 ^ n - numarul celor cu mai putin de k
		 */
		int less_k = solve(n, m1, k);
		int more_k = logPowNum(2, n) - solve(n, 1, k);
		/**
		 * Modulo nu poate fi negativ, si de aceea adunam MOD
		 */
		if(more_k < 0)	
			more_k += MOD;
		/**
		 * Calculam Comb(m1 + m2, m2), inmultind numaratorul cu inversul numitorului modulo MOD
		 */
		int numarator = 1, numitor = 1;
		for(int i = 1; i <= m1; i++) {
			numitor = (numitor * i) % MOD;
			numarator = (numarator * (m2 + i)) % MOD;
		}		
		/**
		 * Din teorema mica a lui Fermat daca MOD e prim (si este in cazul nostru), atunci
		 * inversul unui numar fata de mod este chiar numarul la puterea MOD - 2
		 * x ^ (p - 1) = 1 modulo p
		 */
		numitor = logPowNum(numitor, MOD - 2);		
		int comb = (numarator * numitor) % MOD;
		int rez = (((comb * less_k) % MOD) * logPowNum(more_k, m2)) % MOD;
		pw.write(rez + "\n");
		sc.close();
		pw.close();
	}
}
