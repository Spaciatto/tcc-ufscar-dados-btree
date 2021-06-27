package br.com.spaciatto.btree;

import br.com.spaciatto.btree.algorithm.BTree;

public class BTreeApp {

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        BTree btree = new BTree(2);
        btree.insert(1);
        btree.insert(3);
        btree.insert(7);
        btree.insert(10);
        btree.insert(11);
        btree.insert(13);
        btree.insert(14);
        btree.insert(15);
        btree.insert(18);
        btree.insert(16);
        btree.insert(19);
        btree.insert(24);
        btree.insert(25);
        btree.insert(26);
        btree.insert(21);
        btree.insert(4);
        btree.insert(5);
        btree.insert(20);
        btree.insert(22);
        btree.insert(2);
        btree.insert(17);
        btree.insert(12);
        btree.insert(6);

        System.out.printf("BTree levels: %d\n", btree.getLevels());

        System.out.println("Traversal of tree constructed is");
        btree.traverse();
        btree.traverseFormated();
        System.out.println();

        btree.delete(6);
        System.out.println("Traversal of tree after removing 6");
        btree.traverse();
        System.out.println();

        btree.delete(13);
        System.out.println("Traversal of tree after removing 13");
        btree.traverse();
        System.out.println();

        btree.delete(7);
        System.out.println("Traversal of tree after removing 7");
        btree.traverse();
        System.out.println();

        btree.delete(4);
        System.out.println("Traversal of tree after removing 4");
        btree.traverse();
        System.out.println();

        btree.delete(2);
        System.out.println("Traversal of tree after removing 2");
        btree.traverse();
        System.out.println();

        btree.delete(16);
        System.out.println("Traversal of tree after removing 16");
        btree.traverse();
        System.out.println();

        System.out.printf("BTree levels: %d\n", btree.getLevels());
        btree.traverseFormated();

    }

}
