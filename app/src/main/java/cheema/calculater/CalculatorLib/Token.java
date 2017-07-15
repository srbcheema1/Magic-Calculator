package cheema.calculater.CalculatorLib;

import android.widget.Toast;

import cheema.calculater.MainActivity;

/**
 * Created by srb on 7/10/17.
 */

public class Token {
    public static final String sym = "symbol";
    public static final String num = "number";
    public static final String nul = "empty";
    public static final String spl = "special";
    public String type;
    public String val;

    public Token(){
        type=nul;
        val="0";
    }

    public Token(String val,String type){
        this.type= type;
        this.val=val;
    }

    public String getVal() {
        return val;
    }

    public String getType(){
        return type;
    }

    public static Token lastToken(String str) throws Exception {
        Token temp= new Token();
        int lastsym=-1;//stores index of last symbol seen till now
        int symcnt=1;
        for(int i=0;i<str.length();i++){// checks for last occurance of symbol
            if(isSym(str.charAt(i))){//it is symbol
                if(symcnt==0){
                    if(i>lastsym){
                        lastsym=i;//update index of last sym
                    }
                }
                else if(symcnt==1 && str.charAt(i)=='-'){//this symbol is - and is part of number

                }
                else{
                    throw new Exception("wrong use of symbols");
                }
                symcnt++;
            }
            else{
                symcnt=0;
            }
        }// end for loop

        if(str.length()==0){//exceptional case where str is of length 0
            temp.type=nul;
            temp.val="";
            return temp;
        }
        if(str.contains("∞")){
            throw new Exception("infinity");
        }

        if(lastsym==str.length()-1){
            temp.type=sym;
            temp.val=str.substring(lastsym,lastsym+1);
        }
        else{
            temp.type=num;
            temp.val=str.substring(lastsym+1,str.length());
        }

        return temp;
    }

    public double parseDouble(){
        return parseDouble(this);
    }

    public static double parseDouble(Token t){
        if (t.getType()==Token.sym || t.getType()==Token.nul)
            return 0;
        else{//type is num
            String str=t.getVal();
            if(str.compareTo("-")==0)
                str = "-0";
            if(str.charAt(0)=='.'){
                str = "0"+str;
            }
            if(str.charAt(str.length()-1)=='.'){
                str = str+"0";
            }
            try {
                double d = Double.parseDouble(str);
                return d;
            }catch (Exception e){
                Toast.makeText(MainActivity.activity, "cant parse the token " + str, Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
    }

    public int getPrecedence(){
        return getPrecedence(this);
    }

    public static int getPrecedence(Token temp){
        if(temp.getType()==Token.sym){
            char ch = temp.getVal().charAt(0);
            if(ch=='+'||ch=='-'){
                return 1;
            }
            else if(ch=='×'||ch=='÷'){
                return 2;
            }
            else if(ch=='^'){
                return 3;
            }
            else if(ch=='('){
                return 0;
            }
        }else{
            return 0;
        }
        return 0;
    }

    public static boolean isSym(Character ch){
        if(ch=='+' || ch=='-' || ch=='×' || ch=='÷' || ch=='^' || ch=='(' || ch==')')
            return true;
        return false;
    }
}
