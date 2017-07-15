package cheema.calculater.CalculatorLib;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


/**
 * Created by srb on 7/8/17.
 */

public abstract class SrbCalculator {

    public static String compute(String expression) throws Exception {
        List<Token> tokenList = tokenise(expression);
        List<Token> postFixList = convertPostfix(tokenList);
        double result = calculatePostfix(postFixList);

        //trimmer code
        String res = (new BigDecimal(result)).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
        int cnt=0,ptr=res.length()-1;
        while(res.charAt(ptr)=='0') { //trim last zeros
            cnt++;
            ptr--;
        }
        if(cnt==3)//trim out decemal also
            cnt=4;
        res=res.substring(0,res.length()-cnt);

        return res;
       //return null;
    }

    public static void debugg(List<Token> tokenList){
        for(int i=0;i<tokenList.size();i++){
            System.out.print(tokenList.get(i).getVal()+" , ");
        }
    }

    //returns a list of tokens from a given string
    public static List<Token> tokenise(String str) throws Exception {
        List<Token> reverseTokenList = new ArrayList<>();
        Token temp ;
        while(str.length()!=0){
            temp = Token.lastToken(str);
            reverseTokenList.add(temp);
            str = str.substring(0,str.length()-temp.getVal().length());
        }
        List<Token> tokenList = new ArrayList<>();

        //rearrange them upside down
        int i = reverseTokenList.size()-1;
        while(!reverseTokenList.isEmpty()){
            tokenList.add(reverseTokenList.remove(i));
            i--;
        }
        return tokenList;
    }

    //arrange list into list of postfix tokens
    public static List<Token> convertPostfix(List<Token> tokenList) throws Exception {
        List<Token> postfixList = new ArrayList<>();
        Deque<Token> stack = new ArrayDeque<>();
        for(int i = 0; i<tokenList.size();i++){
            if(tokenList.get(i).getType()==Token.num){
                postfixList.add(tokenList.get(i));
            }
            else if (tokenList.get(i).getType()==Token.sym){
                if(tokenList.get(i).getVal()=="("){
                    stack.add(tokenList.get(i));
                }
                else if(tokenList.get(i).getVal()==")"){
                    while(stack.getLast().getVal()!="("){
                        postfixList.add(stack.removeLast());
                    }
                    stack.removeLast();
                }
                else{
                    while(!stack.isEmpty() && tokenList.get(i).getPrecedence()<=stack.getLast().getPrecedence()){
                        postfixList.add(stack.removeLast());
                    }
                    stack.add(tokenList.get(i));
                }
            }
            else{
                throw new Exception("unwanted token");
            }
        }//end for
        while(!stack.isEmpty()){
            postfixList.add(stack.removeLast());
        }
        debugg(postfixList);
        return postfixList;
    }

    public static double calculatePostfix(List<Token> postfixList) throws Exception {
        Deque<Double> stack = new ArrayDeque<Double>();
        for(int i=0;i<postfixList.size();i++){
            if(postfixList.get(i).getType()==Token.num){
                stack.addLast(postfixList.get(i).parseDouble());
            }
            else {
                double tempResult = 0;
                double temp;

                switch(postfixList.get(i).getVal().charAt(0)) {
                    case '+': temp = stack.removeLast();
                        tempResult = stack.removeLast() + temp;
                        break;

                    case '-': temp = stack.removeLast();
                        tempResult = stack.removeLast() - temp;
                        break;

                    case '×': temp = stack.removeLast();
                        tempResult = stack.removeLast() * temp;
                        break;

                    case '÷': temp = stack.removeLast();
                        if(temp==0)
                            throw new Exception("infinity");
                        else
                            tempResult = stack.removeLast() / temp;
                        break;
                }
                stack.addLast(tempResult);
            }
        }
        return stack.removeLast();
    }
}
