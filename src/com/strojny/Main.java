package com.strojny;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        int[] nums = new int[]{2,7,11,15};
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Collections.sort(list);

//        int result = main.reverse(-2147483648);
        System.out.println(list.get(0));
    }

    public String frequencySort(String s) {
        HashMap<Character, Integer> freq = new HashMap<>();
        for(int i = 0; i < s.length(); i++){
            if(freq.containsKey(s.charAt(i))){
                Integer counter = freq.get(s.charAt(i));
                freq.put(s.charAt(i), counter+1);
            } else {
                freq.put(s.charAt(i), 1);
            }
        }
        List<Occurance> list = new LinkedList<>();


        for(Character c : freq.keySet()){
            list.add(new Occurance(freq.get(c), c));

        }
        Collections.sort(list);
        StringBuilder builder = new StringBuilder();
        for(Occurance o : list){
            for(int i = 0; i < o.n; i++){
                builder.append(o.c);
            }
        }
        return builder.toString();
    }

    class Occurance implements Comparable<Occurance>{
        int n;
        char c;

        public Occurance(int n, char c){
            this.n = n;
            this.c = c;
        }

        @Override
        public int compareTo(Occurance o){
            return n - o.n;
        }
    }
}
