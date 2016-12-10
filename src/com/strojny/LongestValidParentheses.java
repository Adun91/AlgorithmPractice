package com.strojny;

import java.util.HashMap;

/**
 * Created by vreal on 18/11/16.
 *
 * Given a string containing just the characters '(' and ')', find the length of the longest valid (well-formed) parentheses substring.

 For "(()", the longest valid parentheses substring is "()", which has length = 2.

 Another example is ")()())", where the longest valid parentheses substring is "()()", which has length = 4.

 Subscribe to see which companies asked this question
 */
public class LongestValidParentheses {

    public static void main(String[] args){
        LongestValidParentheses lvp = new LongestValidParentheses();
        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("(()"), 2);
        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("()"), 2);
        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("(())"), 4);
        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("()()"), 4);

        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("()(())"), 6);

        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("))))))))))((((((("), 0);
//        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("))(())"), 4);
        System.out.printf("was %d, should be %d\n", lvp.longestValidParentheses("(()))))))((()))()"), 8);
    }
    /*
    *
    * )) (( () () )) )
    * )) (( () () )) (((((())))))
    * -- 12 32 32 10 12
    *
    * () ()
    * 10 10
    *
    * (())
    * 1210
    *
    *
    * ((((((((((())
    * */
    public int longestValidParentheses(String s) {
        if(s == null || s.isEmpty()){
            return 0;
        }
        HashMap<Integer, Integer> start = new HashMap<>();
        HashMap<Integer, Integer> end = new HashMap<>();
        int counter = 0;
        int totalMax = 0;
        for(int i = s.indexOf('('); i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '('){
                counter++;
                if(!start.containsKey(counter)){
                    start.put(counter, i);
                }
            } else {
                counter--;
                end.put(counter, i);
            }
            if(counter == -1 || i == (s.length() - 1)) {
                int localMax = 0;
                for(int startCounter : start.keySet()) {
                    int startPoint = start.get(startCounter);
                    if(end.containsKey(startCounter - 1)) {
                        int distance = end.get(startCounter - 1) - startPoint + 1;
                        localMax = Math.max(distance, localMax);
                    }
                }
                totalMax = Math.max(totalMax, localMax);
                start.clear();
                end.clear();
                counter = 0;
            }
        }
        return totalMax;
    }
}
