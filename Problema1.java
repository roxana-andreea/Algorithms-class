import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

public class Problema1 {

	public static final int MASTER = 1;
	public static final int SLAVE = 0;
	/**
	 * Formam perechile, dupa care din prima alegem sclavul(SLAVE), iar din a doua stapanul(MASTER)
	 */
    static class Tuple implements Comparable<Tuple>{
        public int first;
        public int second;
        public int idx;
        public Tuple(int first, int second, int idx) {
            this.first = first;
            this.second = second;
            this.idx = idx;
        }
        /**
         * Comparam diferenta stapan-sclav
         */
        @Override
        public int compareTo(Tuple p) {
            return -((first - second) - (p.first - p.second));
        }    
        
        public String toString() {
            return (first + " " + second);
        }
    }
    
    Vector<Tuple> data;
    int []state;
    
    public Problema1(Vector<Tuple> data) {
        this.data = data;
        state = new int[data.size() + 1];
    }
    /**
     * Facem un heap in care retinem perechile si pe care-l prelucram. 
     * Vezi README pentru implementarea completa
     */
    
    int[] solve() {
        PriorityQueue<Tuple> heap = new PriorityQueue<Tuple>();
        heap.add(data.get(1));
        int sum = data.get(0).second + data.get(1).first;
        state[0] = SLAVE;
        state[1] = MASTER;
        for(int i = 1; i < data.size() / 2; i++) {
            Tuple p1 = data.get(2 * i);
            Tuple p2 = data.get(2 * i + 1);
            if(heap.peek().compareTo(p1) < 0) {
                Tuple aux = heap.poll();
                sum = sum - aux.first + aux.second + p1.first + p2.first;
                state[aux.idx] = SLAVE;
                state[p1.idx] = MASTER;
                state[p2.idx] = MASTER;
                heap.add(p1);
                heap.add(p2);
            } else {
                sum = sum + p1.second + p2.first;
                heap.add(p2);
                state[p1.idx] = SLAVE;
                state[p2.idx] = MASTER;
            }
        }
        state[state.length - 1] = sum;
        return state;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("date.in"));
        PrintWriter pw = new PrintWriter(new File("date.out"));
        int n = sc.nextInt();
        Vector<Tuple> data = new Vector<Tuple>();
        for(int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            data.add(new Tuple(x, y, i));
        }
        Problema1 solver = new Problema1(data);
        int[] state = solver.solve();
        int cnt = 0, im = 1, is = 0;
        pw.write(state[state.length - 1] + "\n");
        while(cnt < n / 2) {
           while(state[is] != Problema1.SLAVE) is++;
           while(state[im] != Problema1.MASTER) im++;
           pw.write((im + 1) + " " + (is + 1) + "\n");
           cnt++;
           is++;
           im++;
        }
        pw.close();
        sc.close();
    }
}