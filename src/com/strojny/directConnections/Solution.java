package com.strojny.directConnections;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * Created by vreal on 10/12/16.
 * problem address: https://www.hackerrank.com/challenges/direct-connections
 *
 * Congrats, you solved this challenge! :D
 */
public class Solution {

    static class City implements Comparable{
        int p;          //population
        int x;          //distance to left border
        int index;      //order from left

        public City(int x, int p){
            this.x = x;
            this.p = p;
        }

        @Override
        public int compareTo(Object o) {        //orders city by population increasing
            if(o instanceof City){
                City c = (City)o;
                return p - c.p;
            }
            return 0;
        }
    }

    public static long getCables(int[] x, int[] p){
        City[] cities = new City[x.length];
        for(int i = 0; i < cities.length; i++){
            cities[i] = new City(x[i], p[i]);
        }
        return getCables(cities);
    }

    /**
     * Main method with magic algorithm
    * */
    private static long getCables(City[] cities){
        Arrays.sort(cities, (o1, o2) -> o1.x - o2.x);      //orders city by position increasing
        for(int i = 0; i < cities.length; i++){
            cities[i].index = i;
        }
        Arrays.sort(cities);
        long sum = 0L;
        SegmentTree segmentTree = new SegmentTree(cities.length);
        for(int i = 0; i < cities.length; i++){
            City city = cities[i];
            long currentSum = segmentTree.getSum(0, city.index);
            long citiesOnLeft = segmentTree.getLeavesOnLeft(city.index);
            long cablesToLeft = (citiesOnLeft * city.x - currentSum);
            long citiesToRight = (i - citiesOnLeft);
            long cablesToRight = segmentTree.getSum(0, cities.length - 1) -
                    currentSum - (city.x * citiesToRight);
            sum += mod(mod(cablesToLeft + cablesToRight) * city.p);
            sum = mod(sum);
            //update segment Tree
            segmentTree.addToNode(city.index, city.x);
        }
        return sum;
    }

    private static long mod(long i){
        return i % 1_000_000_007;
    }

    public static void main(String[] args){
        MyScanner sc = new MyScanner();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        int t = sc.nextInt();
        for(int block = 0; block < t; block++){
            int n = sc.nextInt();
            int[] x = new int[n];
            int[] p = new int[n];
            for(int i = 0; i < n; i++){
                x[i] = sc.nextInt();
            }
            for(int i = 0; i < n; i++){
                p[i] = sc.nextInt();
            }
            out.println(getCables(x, p));
        }
        out.close();
    }

    public static void test(){
//        int[] x = new int[]{5,20,50,30};
//        int[] p = new int[]{20,10,5,15};

//        int[] x = new int[]{1,3,6};
//        int[] p = new int[]{10,20,30};

        int[] x = new int[]{5,55,555,55555,555555 };
        int[] p = new int[]{3333,333,333,33,35};
        City[] cities = new City[x.length];
        for(int i = 0; i < cities.length; i++){
            cities[i] = new City(x[i], p[i]);
        }
        System.out.println(getCables(cities));
    }

    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;

        public MyScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

class SegmentTree {
    private long[] treeX;
    private int[] treeI;
    private int leaves;

    //create empty tree
    public SegmentTree(int leaves){
        this.leaves = leaves;
        int h = (int)Math.ceil(Math.log(leaves) / Math.log(2)) + 1;
        int size = (int)Math.pow(2, h) - 1;
        this.treeX = new long[size];
        this.treeI = new int[size];
    }

    public SegmentTree(int[] array){
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
}



