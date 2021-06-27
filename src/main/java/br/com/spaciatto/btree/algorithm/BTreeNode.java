package br.com.spaciatto.btree.algorithm;

public class BTreeNode {

    private int[] keys;
    private BTreeNode[] children;
    private int minimumDegree;
    private boolean isLeaf;
    private int numberKeys;

    public BTreeNode(int minimumDegree, boolean isLeaf) {
        this.minimumDegree = minimumDegree;
        this.isLeaf = isLeaf;

        this.keys = new int[2 * minimumDegree - 1];
        this.children = new BTreeNode[2 * minimumDegree];

        this.numberKeys = 0;
    }

    public int[] getKeys() {
        return keys;
    }

    public void setKeys(int[] keys) {
        this.keys = keys;
    }

    public BTreeNode[] getChildren() {
        return children;
    }

    public void setChildren(BTreeNode[] children) {
        this.children = children;
    }

    public int getMinimumDegree() {
        return minimumDegree;
    }

    public void setMinimumDegree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public int getNumberKeys() {
        return numberKeys;
    }

    public void setNumberKeys(int numberKeys) {
        this.numberKeys = numberKeys;
    }

    public void traverse() {
        int i;
        for (i = 0; i < this.numberKeys; i++) {
            if (!this.isLeaf)
                this.children[i].traverse();
            System.out.printf(" %d", this.keys[i]);
        }

        if (!this.isLeaf) {
            this.children[i].traverse();
        }
    }

    public BTreeNode search(int key){
        int index = 0;
        while (index < this.numberKeys && key > this.keys[index])
            index++;

        if (this.keys[index] == key)
            return this;
        if (this.isLeaf)
            return null;

        return this.children[index].search(key);
    }

    private  void fillNode(int index) {
        if (index != 0 && this.children[index - 1].numberKeys >= this.minimumDegree) {
            BTreeNode child = children[index];
            BTreeNode brother = children[index - 1];
            for (int c = child.numberKeys - 1; c >= 0; --c)
                child.keys[c + 1] = child.keys[c];

            if (!child.isLeaf) {
                for (int c = child.numberKeys; c >= 0; --c)
                    child.children[c + 1] = child.children[c];
            }

            child.keys[0] = keys[index - 1];
            if (!child.isLeaf)
                child.children[0] = brother.children[brother.numberKeys];

            keys[index - 1] = brother.keys[brother.numberKeys - 1];
            child.numberKeys += 1;
            brother.numberKeys -= 1;
        } else if (index != 0 && this.children[index + 1].numberKeys >= this.minimumDegree) {
            BTreeNode child = children[index];
            BTreeNode brother = children[index + 1];

            child.keys[child.numberKeys] = keys[index];
            if (!child.isLeaf)
                child.children[child.numberKeys + 1] = brother.children[0];

            this.keys[index] = brother.keys[0];

            for (int c = 1; c < brother.numberKeys; ++c)
                brother.keys[c - 1] = brother.keys[c];

            if (!brother.isLeaf){
                for (int c = 1; c <= brother.numberKeys; ++c)
                    brother.children[c - 1] = brother.children[c];
            }

            child.numberKeys += 1;
            brother.numberKeys -= 1;
        } else {
            if (index != this.numberKeys)
                mergeNodes(index);
            else
                mergeNodes(index - 1);
        }
    }

    public void insert(int key) {
        int index = this.numberKeys - 1;
        if (this.isLeaf) {
            while (index >= 0 && this.keys[index] > key) {
                this.keys[index + 1] = this.keys[index];
                index--;
            }
            this.keys[index + 1] = key;
            this.numberKeys++;
        } else {
            while (index >= 0 && this.keys[index] > key)
                index--;

            if (this.children[index + 1].numberKeys == 2 * this.minimumDegree - 1) {
                splitChild(index + 1, this.children[index + 1]);
                if (this.keys[index + 1] < key)
                    index++;
            }
            this.children[index + 1].insert(key);
        }
    }

    public void splitChild(int index, BTreeNode node) {
        BTreeNode newNode = new BTreeNode(node.minimumDegree, node.isLeaf);
        newNode.numberKeys = this.minimumDegree - 1;

        for (int j = 0; j < this.minimumDegree - 1; j++)
            newNode.keys[j] = node.keys[j + this.minimumDegree];
        if (!node.isLeaf){
            for (int j = 0; j < this.minimumDegree; j++)
                newNode.children[j] = node.children[j + this.minimumDegree];
        }
        node.numberKeys = this.minimumDegree - 1;

        for (int j = this.numberKeys; j >= index + 1; j--)
            this.children[j + 1] = this.children[j];
        this.children[index + 1] = newNode;

        for (int j = this.numberKeys - 1; j >= index; j--)
            this.keys[j + 1] = this.keys[j];
        this.keys[index] = node.keys[this.minimumDegree - 1];

        this.numberKeys++;
    }

    public void delete(int key) {
        int index = findKey(key);
        if (index < this.numberKeys && keys[index] == key) {
            if (this.isLeaf) {
                deleteInLeafNode(index);
            } else {
                deleteInNomLeafNode(index);
            }
        } else {
            if (!this.isLeaf) {
                boolean indexFlag = (index == this.numberKeys);
                if (this.children[index].numberKeys < this.minimumDegree)
                    fillNode(index);

                if (indexFlag && index > this.numberKeys)
                    this.children[index - 1].delete(key);
                else
                    this.children[index].delete(key);
            } else {
                throw new RuntimeException();
            }
        }

    }

    private void deleteInLeafNode(int index) {
        for (int c = index + 1; c < this.numberKeys; ++c)
            this.keys[c - 1] = this.keys[c];

        this.numberKeys--;
    }

    private void deleteInNomLeafNode(int index) {
        int key = this.keys[index];
        if (this.children[index].numberKeys >= this.minimumDegree) {
            BTreeNode node = this.children[index];
            while (!node.isLeaf)
                node = node.children[node.numberKeys];

            int nodeIndex = node.keys[node.numberKeys - 1];

            this.keys[index] = nodeIndex;
            this.children[index].delete(nodeIndex);
        } else if (this.children[index + 1].numberKeys >= this.minimumDegree) {
            BTreeNode node = this.children[index + 1];
            while (!this.isLeaf)
                node = node.children[0];

            int nodeIndex = node.keys[0];

            this.keys[index] = nodeIndex;
            this.children[index + 1].delete(nodeIndex);
        } else {
            mergeNodes(index);
            this.children[index].delete(key);
        }
    }

    private void mergeNodes(int index) {
        BTreeNode child = children[index];
        BTreeNode brother = children[index + 1];

        child.keys[this.minimumDegree - 1] = keys[index];

        for (int c =0 ; c < brother.numberKeys; ++c)
            child.keys[c + this.minimumDegree] = brother.keys[c];

        if (!child.isLeaf) {
            for (int c = 0; c <= brother.numberKeys; ++c)
                child.children[c + this.minimumDegree] = brother.children[c];
        }

        for (int c = index + 1; c < this.numberKeys; ++c)
            this.keys[c - 1] = keys[c];
        for (int c = index + 2; c <= this.numberKeys; ++c)
            this.children[c - 1] = this.children[c];

        child.numberKeys += brother.numberKeys + 1;
        this.numberKeys--;
    }

    private int findKey(int key) {
        int index = 0;
        while (index < this.numberKeys && keys[index] < key)
            ++index;

        return index;
    }

}
