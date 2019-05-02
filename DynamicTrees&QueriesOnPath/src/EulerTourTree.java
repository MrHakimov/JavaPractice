import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;

/**
 * @author: Muhammadjon Hakimov
 * created: 03.05.2019 01:32:23
 */

public class EulerTourTree {
    private static HashMap<String, Node> tree = new HashMap<>();

    private static int generate() {
        int result = new Random().nextInt();
        for (int i = 0; i < 3; i++) {
            result <<= 16;
            result += new Random().nextInt();
        }
        return Math.abs(result % Integer.MAX_VALUE);
    }

    public static class Node {
        Node leftChild;
        Node rightChild;
        Node parent;
        String edge;
        int priority;
        int size;

        Node(String edge) {
            this.edge = edge;
            this.priority = generate();
            this.size = 1;
        }
    }

    private static int getSize(Node node) {
        return (node != null ? node.size : 0);
    }

    private static void update(Node node) {
        if (node != null) {
            node.size = 1 + getSize(node.leftChild) + getSize(node.rightChild);
        }
    }

    private static Node merge(Node first, Node second) {
        if (first == null && second == null) {
            return null;
        } else if (first == null) {
            update(second);
            return second;
        } else if (second == null) {
            update(first);
            return first;
        } else {
            update(first);
            update(second);
            if (first.priority < second.priority) {
                second.leftChild = merge(first, second.leftChild);
                if (second.leftChild != null) {
                    second.leftChild.parent = second;
                }
                update(first);
                update(second);
                second.parent = null;
                return second;
            } else {
                first.rightChild = merge(first.rightChild, second);
                if (first.rightChild != null) {
                    first.rightChild.parent = first;
                }
                update(first);
                update(second);
                first.parent = null;
                return first;
            }
        }
    }

    public static class Pair {
        Node first;
        Node second;

        Pair(Node first, Node second) {
            this.first = first;
            this.second = second;
        }
    }

    private static Pair split(Node node, int key) {
        if (node == null) {
            return new Pair(null, null);
        }
        update(node);
        int index = getSize(node.leftChild);
        if (index < key) {
            Pair pair = split(node.rightChild, key - index - 1);
            node.rightChild = pair.first;
            update(node);
            node.parent = null;
            if (pair.second != null) {
                pair.second.parent = null;
            }
            return new Pair(node, pair.second);
        } else {
            Pair pair = split(node.leftChild, key);
            node.leftChild = pair.second;
            update(node);
            node.parent = null;
            if (pair.first != null) {
                pair.first.parent = null;
            }
            return new Pair(pair.first, node);
        }
    }

    private static Node getRoot(Node node) {
        while (node.parent != null) {
            node = node.parent;
        }
        return node;
    }

    private static int getIndex(Node node) {
        int index = getSize(node.leftChild);

        while (node != null) {
            if (node.parent != null && node.parent.rightChild == node) {
                index += getSize(node.parent.leftChild) + 1;
            }
            node = node.parent;
        }
        return index;
    }

    private static void link(int u, int v) {
        String pairUV = u + " " + v;
        String pairVU = v + " " + u;
        String pairUU = u + " " + u;
        String pairVV = v + " " + v;
        Node u_v = new Node(pairUV);
        Node v_u = new Node(pairVU);
        tree.put(pairUV, u_v);
        tree.put(pairVU, v_u);
        Pair first = split(getRoot(tree.get(pairUU)), getIndex(tree.get(pairUU)));
        Pair second = split(getRoot(tree.get(pairVV)), getIndex(tree.get(pairVV)));
        Node tmp = merge(first.first, u_v);
        tmp = merge(tmp, second.second);
        tmp = merge(tmp, second.first);
        tmp = merge(tmp, v_u);
        tmp = merge(tmp, first.second);
    }

    private static void cut(int u, int v) {
        Node u_v = tree.get(u + " " + v);
        Node v_u = tree.get(v + " " + u);
        if (getSize(u_v) > getIndex(v_u)) {
            Node tmp = u_v;
            u_v = v_u;
            v_u = tmp;
        }

        Pair tmp1 = split(getRoot(u_v), getIndex(u_v));
        Pair tmp2 = split(tmp1.second, getIndex(u_v) + 1);
        Pair tmp3 = split(getRoot(v_u), getIndex(v_u));
        Pair tmp4 = split(tmp3.second, getIndex(v_u) + 1);
        merge(tmp1.first, tmp4.second);
    }

    public static void printTree(Node node) {
        if (node == null) {
            return;
        }
        printTree(node.leftChild);
        System.out.print((Integer.parseInt(node.edge.split(" ")[0]) + 1) + " " + (Integer.parseInt(node.edge.split(" ")[1]) + 1) + " | ");
        printTree(node.rightChild);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] line = in.nextLine().split(" ");
        int n = Integer.parseInt(line[0]);
        for (int i = 0; i < n; i++) {
            String edge = i + " " + i;
            Node tmp = new Node(edge);
            tree.put(edge, tmp);
        }
        int m = Integer.parseInt(line[1]);
        for (int i = 0; i < m; i++) {
            line = in.nextLine().split(" ");
            switch (line[0]) {
                case "link":
                    int u = Integer.parseInt(line[1]);
                    int v = Integer.parseInt(line[2]);
                    u--;
                    v--;
                    link(u, v);
                    break;
                case "cut":
                    u = Integer.parseInt(line[1]);
                    v = Integer.parseInt(line[2]);
                    u--;
                    v--;
                    cut(u, v);
                    break;
                case "size":
                    u = Integer.parseInt(line[1]);
                    u--;
                    System.out.println((getRoot(tree.get(u + " " + u)).size + 2) / 3);
                    break;
            }
        }
    }
}
