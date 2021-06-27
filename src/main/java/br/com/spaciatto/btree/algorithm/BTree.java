package br.com.spaciatto.btree.algorithm;

public class BTree {

    private BTreeNode root;
    private final int minimumDegree;

    public BTree(int minimumDegree) {
        this.root = null;
        this.minimumDegree = minimumDegree;
    }

    public BTreeNode getRoot() {
        return this.root;
    }

    public int getMinimumDegree() {
        return this.minimumDegree;
    }

    public void insert(int key) {
        if (this.root == null) {
            this.root = new BTreeNode(this.minimumDegree, true);
            this.root.getKeys()[0] = key;
            this.root.setNumberKeys(1);
        } else {
            if (root.getNumberKeys() == 2 * this.minimumDegree - 1) {
                BTreeNode newNode = new BTreeNode(this.minimumDegree, false);
                newNode.getChildren()[0] = this.root;
                newNode.splitChild(0, this.root);

                int index = 0;
                if (newNode.getKeys()[0] < key) {
                    index++;
                }
                newNode.getChildren()[index].insert(key);

                this.root = newNode;
            } else {
                this.root.insert(key);
            }
        }
    }

    public BTreeNode search(int key) {
        return this.root == null ? null : this.root.search(key);
    }

    public void delete(int key) {
        if (this.root == null){
            System.out.println("The tree is empty");
            return;
        }

        this.root.delete(key);

        if (this.root.getNumberKeys() == 0) {
            if (this.root.isLeaf()) {
                this.root = null;
            } else {
                this.root = root.getChildren()[0];
            }
        }
    }

    public void traverse() {
        if (this.root != null) {
            System.out.println(this.print(this.root));
        }
    }

    public void traverseFormated() {
        if (this.root != null) {
            this.display(this.root, 1);
        }
    }

    public int getLevels() {
        int level = 0;
        BTreeNode node = this.root;
        while (node != null) {
            level++;
            node = node.getChildren()[0];
        }

        return level;
    }

    private String print(BTreeNode node) {
        String s = "";
        if (node != null) {
            for (int i = 0; i < node.getNumberKeys(); i++) {
                s = s.concat(print(node.getChildren()[i]));
                s = s.concat(node.getKeys()[i] + " ");
            }
            s = s.concat(print(node.getChildren()[node.getNumberKeys()]));
        }

        return s;
    }

    private void display(BTreeNode node, int depth) {
        if (node != null) {
//            int children = node.getChildren().length;
            for (int i = node.getNumberKeys() ; i > 0; i--) {
                if (!node.isLeaf()) {
                    display(node.getChildren()[i], depth + 4);
                }
                if (i -1 < node.getNumberKeys()) {
                    for (int j = 1; j <= depth; j++) System.out.print(" ");
                    System.out.println(node.getKeys()[i-1]);
                }
            }
            display(node.getChildren()[0], depth + 4);
        }
    }
}
