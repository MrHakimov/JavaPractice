import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Muhammadjon Hakimov
 * created: 01.05.2019 16:12:55
 */

public class EulerTourWithSegmentTree {
    static List<List<Integer>> g = new ArrayList<>();
    static List<List<Integer>> edgesIndex = new ArrayList<>();
    static List<Integer> edgesList = new ArrayList<>();
    static final int N = 100_001;
    static int H = 20;
    static int[] tin = new int[N];
    static int[] tout = new int[N];
    static boolean[] used = new boolean[N];
    static List<List<Integer>> parent = new ArrayList<>();
    static int timer = 1;

    public static void dfs(int u, int p) {
        used[u] = true;
        tin[u] = timer++;
        parent.get(u).set(0, p);
        for (int i = 1; i < H; i++) {
            int v = parent.get(u).get(i - 1);
            parent.get(u).set(i, parent.get(v).get(i - 1));
        }
        for (int i = 0; i < g.get(u).size(); i++) {
            int v = g.get(u).get(i);
            if (!used[v]) {
                edgesList.add(edgesIndex.get(u).get(i));
                dfs(v, u);
            }
        }
        tout[u] = timer++;
    }

    static boolean isAncestor(int a, int b) {
        return tin[a] <= tin[b] && tout[a] >= tout[b];
    }

    public static int lca(int a, int b) {
        if (isAncestor(a, b)) {
            return a;
        } else if (isAncestor(b, a)) {
            return b;
        } else {
            for (int i = H - 1; i >= 0; i--) {
                if (!isAncestor(parent.get(a).get(i), b)) {
                    a = parent.get(a).get(i);
                }
            }
            return parent.get(a).get(0);
        }
    }

    public class SegmentTree {
        int[] t = new int[4 * N];
        int[] d = new int[4 * N];

        public void push(int v, int len) {
            if (len == 1) {
                t[v] += d[v];
                d[v] = 0;
                return;
            }
            d[2 * v] += d[v];
            d[2 * v + 1] += d[v];
            t[v] += d[v];
            d[v] = 0;
        }

        public void update(int v, int l, int r, int L, int R, int x) {
            push(v, r - l + 1);
            if (L > r || R < l) {
                return;
            }
            if (L <= l && r <= R) {
                d[v] += x;
                push(v, r - l + 1);
                return;
            }
            int m = (l + r) / 2;
            update(2 * v, l, m, L, R, x);
            update(2 * v + 1, m + 1, r, L, R, x);
            t[v] = t[2 * v] + t[2 * v + 1];
        }

        public int query(int v, int l, int r, int L, int R) {
            push(v, r - l + 1);
            if (L > r || R < l) {
                return -1;
            }
            if (L <= l && r <= R) {
                return v;
            }
            int m = (l + r) / 2;
            int sl = query(2 * v, l, m, L, R);
            int sr = query(2 * v + 1, m + 1, r, L, R);
            return sl + sr;
        }
    }

    static SegmentTree ST1;
    static SegmentTree ST2;

    static int[] first1 = new int[N];
    static int[] first2 = new int[N];

    static {
        for (int i = 0; i < N; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < H + 1; j++) {
                list.add(0);
            }
            parent.add(list);
            g.add(new ArrayList<>());
            edgesIndex.add(new ArrayList<>());
        }
        for (int i = 0; i < edgesList.size(); i++) {
            int j = edgesList.get(i);
            if (first1[j] == 0) {
                first1[j] = i;
            } else {
                first2[j] = i;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] left = new int[n - 1];
        int[] right = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            left[i] = u;
            right[i] = v;
            g.get(u).add(v);
            g.get(v).add(u);
            edgesIndex.get(u).add(i + 1);
            edgesIndex.get(v).add(i + 1);
        }
        dfs(1, 1);
        int m = in.nextInt();
        for (int i = 0; i < m; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            ST1.update(1, 1, edgesList.size(), first1[x], first1[y], 1);
            ST2.update(1, 1, edgesList.size(), first2[x], first2[y], 1);
        }
        for (int i = 0; i < n - 1; i++) {
            int l = lca(left[i], right[i]);
            System.out.println(ST1.query(1, 1, edgesList.size(), first1[l], first1[left[i]])
                    - ST2.query(1, 1, edgesList.size(), first2[l], first2[right[i]]));
        }
    }
}
