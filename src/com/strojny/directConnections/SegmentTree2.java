package com.strojny.directConnections;

/**
 * Created by vreal on 09/12/16.
 */
public class SegmentTree2 {
    private long[] treeX;
    private int[] treeI;
    private int leaves;

    //create empty tree
    public SegmentTree2(int leaves){
        this.leaves = leaves;
        int h = (int)Math.ceil(Math.log(leaves) / Math.log(2)) + 1;
        int size = (int)Math.pow(2, h) - 1;
        this.treeX = new long[size];
        this.treeI = new int[size];
    }

    public SegmentTree2(int[] array){
        this(array.length);
        initTree(array, 0, array.length - 1, 0);
    }

    private long initTree(int[] array, int start, int end, int nodeIndex){
        if(start == end){
            treeX[nodeIndex] = array[start];
            return treeX[nodeIndex];
        }
        int mid = getMid(start, end);
        treeX[nodeIndex] = initTree(array, start, mid, leftSon(nodeIndex)) +
                initTree(array, mid+1, end, rightSon(nodeIndex));
        return treeX[nodeIndex];
    }

    public long getSum(int leftQuery, int rightQuery){
        int startNode = 0;
        return getSum(startNode, leftQuery, rightQuery, 0, leaves -1);
    }

    public int getLeavesOnLeft(int node){
        return getLeavesOnLeft(node, 0, 0, leaves -1);
    }

    private int getLeavesOnLeft(int targetNode, int currentNode, int leftNodeRange, int rightNodeRange){
        if(leftNodeRange == rightNodeRange){
            return treeI[currentNode];
        }
        int mid = getMid(leftNodeRange, rightNodeRange);
        if(targetNode <= mid){
            return getLeavesOnLeft(targetNode, leftSon(currentNode), leftNodeRange, mid);
        }
        return treeI[leftSon(currentNode)] + getLeavesOnLeft(targetNode, rightSon(currentNode), mid+1, rightNodeRange);
    }

    private long getSum(int node, int leftQuery, int rightQuery, int leftNodeRange, int rightNodeRange){
        if(leftQuery <= leftNodeRange && rightQuery >= rightNodeRange){
            return treeX[node];
        }
        if(rightQuery < leftNodeRange || rightNodeRange < leftQuery){
            return 0L;
        }
        int mid = getMid(leftNodeRange, rightNodeRange);
        return getSum(leftSon(node), leftQuery, rightQuery, leftNodeRange, mid) +
                getSum(rightSon(node), leftQuery, rightQuery, mid+1, rightNodeRange);
    }
    //Updates index element by diff
    public void addToNode(int index, int diff){
        addToNode(index, diff, 0, 0, leaves -1);
    }

    private void addToNode(int index, int diff, int node, int leftNodeRange, int rightNodeRage){
        if(leftNodeRange <= index && index <= rightNodeRage) {
            treeX[node] += diff;
            treeI[node]++;
            if(leftNodeRange < rightNodeRage) {
                int mid = getMid(leftNodeRange, rightNodeRage);
                addToNode(index, diff, leftSon(node), leftNodeRange, mid);
                addToNode(index, diff, rightSon(node), mid + 1, rightNodeRage);
            }
        }
    }

    private int getMid(int start, int end){
        return (start + end) / 2;
    }

    private int leftSon(int index){
        return 2 * index + 1;
    }

    private int rightSon(int index){
        return 2 * index + 2;
    }

    public void printTree(){
        for(int i = 0; i < treeX.length - 1; i++){
            System.out.printf("%d,", treeX[i]);
        }
        System.out.printf("%d\n", treeX[treeX.length - 1]);
    }

    public void printTreeI(){
        for(int i = 0; i < treeI.length - 1; i++){
            System.out.printf("%d,", treeI[i]);
        }
        System.out.printf("%d\n", treeI[treeI.length - 1]);
    }

    public static void main(String[] args){
        int[] array = new int[]{1,2,3,4,5,6};
        SegmentTree2 segmentTree = new SegmentTree2(array);
        segmentTree.printTree();
        segmentTree.addToNode(2, 10);
        segmentTree.printTreeI();
        segmentTree.addToNode(1, 10);
        segmentTree.printTreeI();
        System.out.println(segmentTree.getLeavesOnLeft(4));
    }
}
