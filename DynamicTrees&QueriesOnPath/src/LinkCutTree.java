import java.util.Scanner;

/**
 * @author: Muhammadjon Hakimov
 * created: 02.05.2019 02:52:22
 */

public class LinkCutTree {
    private static final int N = 100001;

    private static Node[] tree = new Node[N];

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n;
        n = in.nextInt();
        for (int i = 0; i <= n; i++) {
            tree[i] = new Node();
        }
        int m;
        m = in.nextInt();
        for (int i = 0; i < m; i++) {
            String command = in.next();
            switch (command) {
                case "link":
                    int u = in.nextInt();
                    int v = in.nextInt();
                    link(tree[u], tree[v]);
                    break;
                case "cut":
                    u = in.nextInt();
                    v = in.nextInt();
                    cut(tree[u], tree[v]);
                    break;
                case "size":
                    u = in.nextInt();
                    System.out.println(calcSize(tree[u]));
            }
        }
    }

    public static class Node {
        Node leftChild;
        Node rightChild;
        Node parent;
        boolean isReversed;
        int size = 1;

        boolean isRoot() {
            return parent == null || (parent.leftChild != this && parent.rightChild != this);
        }

        void push() {
            if (isReversed) {
                isReversed = false;
                Node tmp = leftChild;
                leftChild = rightChild;
                rightChild = tmp;
                if (leftChild != null) {
                    leftChild.isReversed ^= true;
                }
                if (rightChild != null) {
                    rightChild.isReversed ^= true;
                }
            }
            size = 1 + getSize(leftChild) + getSize(rightChild);
        }
    }

    static int getSize(Node node) {
        return (node != null ? node.size : 0);
    }

    static int calcSize(Node node) {
        expose(node);
        while (node.parent != null) {
            node = node.parent;
        }
        node.push();
        return node.size;
    }

    static void connect(Node child, Node parent, Boolean isLeftChild) {
        if (child != null) {
            child.parent = parent;
        }
        if (isLeftChild != null) {
            if (isLeftChild) {
                parent.leftChild = child;
            } else {
                parent.rightChild = child;
            }
        }
    }

    static void rotate(Node node) {
        Node parent = node.parent;
        Node grandpa = parent.parent;
        boolean isRootParent = parent.isRoot();
        boolean isLeftChild = (node == parent.leftChild);

        connect((isLeftChild ? node.rightChild : node.leftChild), parent, isLeftChild);
        connect(parent, node, !isLeftChild);
        connect(node, grandpa, !isRootParent ? parent == grandpa.leftChild : null);
    }

    static void splay(Node node) {
        if (node == null) {
            return;
        }
        while (!node.isRoot()) {
            Node parent = node.parent;
            Node grandpa = parent.parent;
            if (!parent.isRoot()) {
                grandpa.push();
            }
            parent.push();
            node.push();
            if (!parent.isRoot()) {
                rotate((node == parent.leftChild) == (parent == grandpa.leftChild) ? parent : node);
            }
            rotate(node);
        }
        node.push();
    }
    static Node expose(Node node) {
        Node last = null;
        for (Node current = node; current != null; current = current.parent) {
            splay(current);
            current.leftChild = last;
            last = current;
        }
        splay(node);
        return last;
    }

    public static void makeRoot(Node node) {
        expose(node);
        node.isReversed ^= true;
    }

    public static boolean connected(Node x, Node y) {
        if (x == y) {
            return true;
        }
        expose(x);
        expose(y);
        return x.parent != null;
    }

    public static void link(Node x, Node y) {
        if (connected(x, y)) {
            return;
        }
        makeRoot(x);
        x.parent = y;
    }

    public static void cut(Node x, Node y) {
        makeRoot(x);
        expose(y);
        if (y.rightChild != x || x.leftChild != null || x.rightChild != null) {
            return;
        }
        y.rightChild.parent = null;
        y.rightChild = null;
    }
}
