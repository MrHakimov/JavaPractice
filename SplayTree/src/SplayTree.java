import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * @author: Muhammadjon Hakimov
 * created: 20.04.2019 14:02:46
 */

public class SplayTree {
    private static Node root;
    private static final int INF = 2000000001;

    public static class Node {
        int value;
        Node leftChild;
        Node rightChild;
        Node parent;

        Node(int value) {
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
            this.parent = null;
        }
    }

    public static void zig(Node node) {
        Node parent = node.parent;
        if (parent.leftChild == node) {
            Node right = node.rightChild;
            node.rightChild = parent;
            parent.leftChild = right;
            if (right != null) {
                right.parent = parent;
            }
        } else {
            Node left = node.leftChild;
            node.leftChild = parent;
            parent.rightChild = left;
            if (left != null) {
                left.parent = parent;
            }
        }
        node.parent = null;
        parent.parent = node;
    }

    public static void zig_zig(Node node) {
        Node parent = node.parent;
        Node grandpa = parent.parent;
        if (parent.leftChild == node) {
            Node first = node.rightChild;
            Node second = parent.rightChild;
            node.parent = grandpa.parent;
            node.rightChild = parent;
            parent.parent = node;
            parent.leftChild = first;
            parent.rightChild = grandpa;
            grandpa.parent = parent;
            grandpa.leftChild = second;
            if (first != null) {
                first.parent = parent;
            }
            if (second != null) {
                second.parent = grandpa;
            }
            if (node.parent != null) {
                if (node.parent.leftChild == grandpa) {
                    node.parent.leftChild = node;
                } else {
                    node.parent.rightChild = node;
                }
            }
        } else {
            Node first = node.leftChild;
            Node second = parent.leftChild;
            node.parent = grandpa.parent;
            node.leftChild = parent;
            parent.parent = node;
            parent.leftChild = grandpa;
            parent.rightChild = second;
            grandpa.parent = parent;
            grandpa.leftChild = second;
            if (first != null) {
                first.parent = grandpa;
            }
            if (second != null) {
                second.parent = parent;
            }
            if (node.parent != null) {
                if (node.parent.leftChild == grandpa) {
                    node.parent.leftChild = node;
                } else {
                    node.parent.rightChild = node;
                }
            }
        }
    }

    public static void zig_zag(Node node) {
        Node parent = node.parent;
        Node grandpa = parent.parent;
        if (parent.rightChild == node) {
            Node first = node.leftChild;
            Node second = node.rightChild;
            node.leftChild = parent;
            node.rightChild = grandpa;
            node.parent = grandpa.parent;
            parent.rightChild = first;
            parent.parent = node;
            grandpa.leftChild = second;
            grandpa.parent = node;
            if (first != null) {
                first.parent = parent;
            }
            if (second != null) {
                second.parent = grandpa;
            }
            if (node.parent != null) {
                if (node.parent.leftChild == grandpa) {
                    node.parent.leftChild = node;
                } else {
                    node.parent.rightChild = node;
                }
            }
        } else {
            Node first = node.leftChild;
            Node second = node.rightChild;
            node.leftChild = grandpa;
            node.rightChild = parent;
            node.parent = grandpa.parent;
            parent.leftChild = second;
            parent.parent = node;
            grandpa.rightChild = first;
            grandpa.parent = node;
            if (first != null) {
                first.parent = grandpa;
            }
            if (second != null) {
                second.parent = parent;
            }
            if (node.parent != null) {
                if (node.parent.leftChild == grandpa) {
                    node.parent.leftChild = node;
                } else {
                    node.parent.rightChild = node;
                }
            }
        }
    }

    private static Node header = new Node(0);

    private static void splay(int value) {
        Node l, r, t, y;
        l = r = header;
        t = root;
        header.leftChild = header.rightChild = null;
        while (true) {
            if (value < t.value) {
                if (t.leftChild == null) {
                    break;
                }
                if (value < t.leftChild.value) {
                    y = t.leftChild;
                    t.leftChild = y.rightChild;
                    y.rightChild = t;
                    t = y;
                    if (t.leftChild == null) {
                        break;
                    }
                }
                r.leftChild = t;
                r = t;
                t = t.leftChild;
            } else if (value > t.value) {
                if (t.rightChild == null) {
                    break;
                }
                if (value > t.rightChild.value) {
                    y = t.rightChild;
                    t.rightChild = y.leftChild;
                    y.leftChild = t;
                    t = y;
                    if (t.rightChild == null) {
                        break;
                    }
                }
                l.rightChild = t;
                l = t;
                t = t.rightChild;
            } else {
                break;
            }
        }
        l.rightChild = t.leftChild;
        r.leftChild = t.rightChild;
        t.leftChild = header.rightChild;
        t.rightChild = header.leftChild;
        root = t;
    }

    private static Node find(int value) {
        Node result = null;
        Node current = root;
        Node last = null;
        while (current != null) {
            last = current;
            if (current.value == value) {
                result = current;
                break;
            }
            if (current.value > value) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        if (result != null) {
            splay(result.value);
        } else if (last != null) {
            splay(last.value);
        }
        return result;
    }

    private static void insert(int value) {
        if (root == null) {
            root = new Node(value);
            return;
        }
        splay(value);
        if (value == root.value) {
            return;
        }
        Node node = new Node(value);
        if (value < root.value) {
            node.leftChild = root.leftChild;
            node.rightChild = root;
            root.leftChild = null;
        } else {
            node.rightChild = root.rightChild;
            node.leftChild = root;
            root.rightChild = null;
        }
        root = node;
    }

    private static void printTree(Node node) {
        if (node == null) {
            return;
        }
        printTree(node.leftChild);
        System.out.print(node.value + " ");
        printTree(node.rightChild);
    }

    private static Node getMin(Node node) {
        Node temp = node;
        while (temp.leftChild != null) {
            temp = temp.leftChild;
        }
        return temp;
    }

    private static void delete(int value) {
        Node current = find(value);
        if (current == null) {
            return;
        }
        Node left = current.leftChild;
        Node right = current.rightChild;
        if (left == null && right == null) {
            root = null;
            return;
        }
        if (left == null) {
            root = right;
            root.parent = null;
            return;
        }
        if (right == null) {
            root = left;
            root.parent = null;
            return;
        }
        Node temp = getMin(right);
        splay(temp.value);
        temp.leftChild = left;
        left.parent = temp;
    }

    private static int next(int value) {
        int result = INF;
        Node current = root;
        while (current != null) {
            if (current.value > value) {
                if (result == INF || current.value < result) {
                    result = current.value;
                }
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        return result;
    }

    private static int prev(int value) {
        int result = INF;
        Node current = root;
        while (current != null) {
            if (current.value < value) {
                if (result == INF || current.value > result) {
                    result = current.value;
                }
                current = current.rightChild;
            } else {
                current = current.leftChild;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        FastScanner sc = new FastScanner();
        while (sc.hasNext()) {
            String command = sc.next();
            int key = sc.nextInt();
            switch (command) {
                case "insert":
                    insert(key);
                    break;
                case "delete":
                    delete(key);
                    break;
                case "exists":
                    System.out.println(find(key) == null ? "false" : "true");
                    break;
                case "next": {
                    int current = next(key);
                    System.out.println(current == INF ? "none" : current);
                    break;
                }
                default: {
                    int current = prev(key);
                    System.out.println(current == INF ? "none" : current);
                    break;
                }
            }
        }
    }
}

class FastScanner {
    private final java.io.InputStream in = System.in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;

    private boolean hasNextByte() {
        if (ptr < buflen) {
            return true;
        } else {
            ptr = 0;
            try {
                buflen = in.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buflen > 0;
        }
    }

    private int readByte() {
        if (hasNextByte()) return buffer[ptr++];
        else return -1;
    }

    private static boolean isPrintableChar(int c) {
        return 33 <= c && c <= 126;
    }

    private void skipUnprintable() {
        while (hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++;
    }

    boolean hasNext() {
        skipUnprintable();
        return hasNextByte();
    }

    String next() {
        if (!hasNext()) throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while (isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }

    public long nextLong() {
        if (!hasNext()) throw new NoSuchElementException();
        long n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while (true) {
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            } else if (b == -1 || !isPrintableChar(b)) {
                return (minus ? -n : n);
            } else {
                throw new NumberFormatException();
            }
            b = readByte();
        }
    }

    int nextInt() {
        if (!hasNext()) throw new NoSuchElementException();
        long n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while (true) {
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            } else if (b == -1 || !isPrintableChar(b)) {
                return (int) (minus ? -n : n);
            } else {
                throw new NumberFormatException();
            }
            b = readByte();
        }
    }
}
