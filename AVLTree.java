import java.util.Scanner;

class AVLTree {
    // Node class
    class Node {
        int key, height;
        Node left, right;

        Node(int d) {
            key = d;
            height = 1;
        }
    }

    private Node root;

    // Get height of node
    int height(Node N) {
        return (N == null) ? 0 : N.height;
    }

    // Get balance factor of node
    int getBalance(Node N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }

    // Right rotate
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    // Left rotate
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Insert key into AVL tree
    Node insert(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node; // Duplicate keys not allowed

        // Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Check balance
        int balance = getBalance(node);

        // LL Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // RR Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // LR Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Return node if no rotation needed
        return node;
    }

    // Public insert
    public void insert(int key) {
        root = insert(root, key);
    }

    // Inorder traversal
    void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.key + " ");
            inorder(node.right);
        }
    }

    public void inorderTraversal() {
        inorder(root);
        System.out.println();
    }

    // Main menu
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== AVL Tree Menu ===");
            System.out.println("1. Insert");
            System.out.println("2. Inorder Traversal");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter value to insert: ");
                    int val = sc.nextInt();
                    tree.insert(val);
                    break;

                case 2:
                    System.out.println("Inorder Traversal (sorted):");
                    tree.inorderTraversal();
                    break;

                case 3:
                    System.out.println("Exiting.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
