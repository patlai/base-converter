//package conv;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Arrays;
import java.util.regex.Pattern;

public class tester {

    public static void main(String[] args) {
        class Result{
            short[] quotient;
            int remainder;
        }
        class Number{
            //=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
            // your method for converting belongs here...
            //=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
            public void printArrayList(ArrayList l){
                for(int i = 0; i<l.size(); i++){
                    System.out.print(l.get(i));
                }
                System.out.println("\n");
            }
            //-----MULTIPLICATION ALGORITHM------
            public Result multiply(short[] A, short B, short Base){
                //B = old base      base = new base
                ArrayList<Integer> product = new ArrayList<>();
                //tmp1 = 1st product       tmp2 = 2nd product
                ArrayList<Integer> tmp1 = new ArrayList<>();
                ArrayList<Integer> tmp2 = new ArrayList<>();
                ArrayList<Integer> remainder = new ArrayList<>();
                //bottom number is the number on the bottom of the multiplication in columns with {[1],[0]}
                //express Base as a string to count how many digits it has
                int[] bottomNumber = new int[2];
                String s = Short.toString(Base);
                if(Base >= 10){
                    bottomNumber[0] = Character.getNumericValue(s.charAt(1));
                    bottomNumber[1] = Character.getNumericValue(s.charAt(0));
                } else {
                    bottomNumber[0] = Character.getNumericValue(s.charAt(0));
                    bottomNumber[1] = 0;
                }
                int carry = 0;
                int i = 0;
                while(i < A.length){
                    //if it's the last step, don't save a carry
                    if(i == A.length - 1){
                        tmp1.add(bottomNumber[0] * A[i] + carry);
                        carry = 0;
                    }
                    else if(bottomNumber[0] * A[i] + carry < B) {
                        tmp1.add(bottomNumber[0] * A[i] + carry);
                        carry = 0;
                    } else if (bottomNumber[0] * A[i] + carry >= B){
                        tmp1.add(((bottomNumber[0] * A[i]) % B + carry) % B);
                        carry = ((bottomNumber[0] * A[i]) + carry) / B;
                    }
                    i++;
                }
                tmp2.add(0);
                for(int j = 0; j < A.length; j++){
                    if(bottomNumber[1] == 0){
                        break;
                    }else if(j == A.length - 1){
                        tmp2.add(bottomNumber[1] * A[j] + carry);
                        carry = 0;
                    } else if(bottomNumber[1] * A[j] + carry < B){
                        tmp2.add(bottomNumber[1] * A[j] + carry);
                        carry = 0;
                    } else if (bottomNumber[1] * A[j] + carry >= B){
                        tmp2.add((bottomNumber[1] * A[j] % B + carry) % B);
                        carry = ((bottomNumber[1] * A[j] + carry)  / B);
                    }
                }
                //the products were added in reverse order, reverse them back and add:
                //------ADDITION PART OF MULTIPLICATION------
                ArrayList<Integer> sum = new ArrayList<>();
                int additionCarry =0 ;
                int m = 0;
                while(m < 2147483647){
                    //if there is no second number, the sum is just the 1st number
                    if(tmp2.size() == 1 && tmp2.get(0) == 0){
                        //Collections.reverse(tmp1);
                        sum = tmp1;
                        break;
                    }
                    if(m == tmp2.size() - 1) {
                        if (tmp2.size() > tmp1.size()) {
                            sum.add(tmp2.get(m) + carry);
                            break;
                        } else {
                            sum.add(tmp1.get(m) + tmp2.get(m) + carry);
                            break;
                        }
                    } else if(tmp1.get(m) + tmp2.get(m) < B){
                        sum.add(tmp1.get(m) + tmp2.get(m) + additionCarry);
                        additionCarry = 0;
                    } else if(tmp1.get(m) + tmp2.get(m) >= B){
                        sum.add((tmp1.get(m) + tmp2.get(m) + additionCarry) % B);
                        additionCarry = (tmp1.get(m) + tmp2.get(m)) / B;
                    }
                    m++;
                }
                short[] p = new short[sum.size()];
                for(int j = 0; j < sum.size(); j++){
                    p[j] = sum.get(j).shortValue();
                    //System.out.print(p[j]);
                }
                Result pp = new Result();
                pp.quotient = p;
                pp.remainder = 0;
                return pp;
            }
            //-----LONG DIVISION ALGORITHM-----
            public Result longDivide(short[] A, short B, short Base){
                //B = old base
                int  i = A.length - 1;
                ArrayList<Integer> quotient = new ArrayList<>();
                ArrayList<Integer> remainder = new ArrayList<>();
                //tmp = new quotient after subtracting
                int tmp = A[i];
                // System.out.println(tmp);
                while(i >= 0 ){
                    try {
                        //if you can't divide anymore, break the loop
                        if(A[i] < Base && A.length < String.valueOf(Base).length()){
                            break;
                        }
                        //if the leftmost digit of the number is divisible by the new base, divide it
                        if(i == 0){
                            quotient.add(tmp / Base);
                        } else if (tmp >= Base ) {
                            quotient.add(tmp / Base);
                            tmp = (B * (tmp - Base * quotient.get(A.length - i - 1))) + A[i-1];
                            // ----- CASE 2 -----
                        } else if (tmp < Base && Base <= (B * tmp) + A[i - 1]) { //add (inital base) * leftmost digit + next digit to the right if new base is greater
                            quotient.add(0);
                            quotient.add((B * tmp + A[i - 1]) / Base);
                            //remainder.add((B * tmp + A[i + 1]) % Base);
                            //the new tmp is the result of the intermediate subtraction + the next number dropped down
                            try {
                                tmp = B * (B * tmp + A[i - 1] - Base * quotient.get(A.length - i)) + A[i - 2];
                                //if the end of the dividend is reached and [i - 2] doesn't exist which means no more digit to drop:
                            } catch (ArrayIndexOutOfBoundsException a){
                                tmp = B * tmp + A[i - 1];
                                break;
                            }
                            if(tmp < Base){
                                quotient.add(0);
                                tmp = B * tmp + A[i-3];
                                i--;
                            } else {
                            }
                            i--;
                            // ----- CASE 3 -----
                        } else if (B * tmp + A[i-1] < Base ) { //3 digits
                            quotient.add(0);
                            quotient.add(0);
                            // System.out.println((B * B * tmp + B * A[i-1] + A[i - 2]));
                            //System.out.println(Base * quotient.get(A.length -i+1));
                            try {
                                quotient.add((B * B * tmp + B * A[i-1] + A[i - 2]) / Base);
                                tmp = ((B * B * tmp + B * A[i - 1] + A[i - 2]) - (Base * quotient.get(A.length - i + 1))) * B + A[i - 3];
                                //if there is no more digit to drop:
                            } catch (ArrayIndexOutOfBoundsException b){
                                try {
                                    tmp = (B * B * tmp) + (B * A[i - 1] + A[i - 2]);
                                    break;
                                } catch(ArrayIndexOutOfBoundsException bb){
                                    tmp = (B * tmp) + (A[i-1]);
                                    break;
                                }
                            }
                            //remainder.add((B * B * tmp + B * A[i + 1] + A[i + 2]) % Base);
                            i -= 2;
                        } else {

                            break;
                        }
                        i--;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("du ma");
                        break;
                    }
                }
                //last remainder of the long division:
                int rmd = 0;
                if(tmp < (quotient.get(quotient.size() - 1) * Base)){
                    rmd = tmp;
                } else {
                    rmd = tmp - (quotient.get(quotient.size() - 1) * Base);
                }
                Collections.reverse(remainder);
                int x = 0;
                while(x < 2147483647) {
                    if (quotient.get(x) == 0 && quotient.size() > 1) {
                        quotient.remove(x);
                    } else {
                        break;
                    }
                }
                //change the quotient array list to an array of shorts
                Collections.reverse(quotient);
                short[] q = new short[quotient.size()];
                for(int j = 0; j < quotient.size(); j++){
                    q[j] = quotient.get(j).shortValue();
                    System.out.print(q[j]);
                }
                Result r = new Result();
                r.quotient = q;
                r.remainder = rmd;
                return r;
            }
            public ArrayList<Integer> divisionLoop(short[] S, short B, short Base){
                //B = Old Base,
                //Base = new base
                short[] dividend = S;
                ArrayList<Integer> remainders = new ArrayList<>();
                int i = 0;
                while (i < 2147483647) {
                    try {
                        Result divisionResult = longDivide(dividend, B, Base);
                        //if there's nothing left to divide, the remainder will be what's left of the dividend
                        if(divisionResult.quotient[0] == 0 && divisionResult.remainder == 0 && (divisionResult.quotient.length == 0 || divisionResult.quotient.length ==1)){
                            remainders.add(0 - dividend[0]);
                            break;
                        }
                        remainders.add(divisionResult.remainder);
                        // if(divisionResult.quotient[0] == 0)
                        dividend = divisionResult.quotient;
                        i++;
                    } catch(ArrayIndexOutOfBoundsException e){
                        //if there's nothing left to divide, the remainder will be what's left of the dividend
                        remainders.add(0 + dividend[0]);
                        System.out.println("it's done");
                        break;
                    }
                }
                return remainders;
            }
            public Number convert(Number A, short Base){
                Number conversionResult = new Number();
                //B = Old Base,
                //Base = new base
                short B = A.Base;
                short[] dividend = A.Int;
                ArrayList<Integer> remainders = new ArrayList<>();
                remainders = divisionLoop(A.Int, A.Base, Base);
                //the integer part of the converted number will be the remainders array list in reverse
                Collections.reverse(remainders);
                //to convert the fractional part of the number:
                //keep multiplying by the new base until the product creates a new nonzero digit -> save that digit and remove it:
                //ex: 0.4 in B10 to B2:
                //0.4 * 2 = 0.8 -> save 0
                //0.8 * 2 = 1.6 -> save 1 and remove it from 1.6
                //0.6 * 2 = 1.2 -> save 1 and remove it from 1.2
                //the conversion will be the collection of all the saved digits.
                ArrayList<Integer> mCoefficients = new ArrayList<>();
                Result repetitionCheck = multiply(A.NonRep, A.Base, Base);
                int m = 0;
                short[] multiplier = A.NonRep;
                ArrayList<Integer> repeats = new ArrayList<>();
                //if the fractional part doesn't repeat, represent the number up to 100 decimal places
                while(m<=100){
                    Result multiplicationResult = multiply(multiplier, A.Base, Base);

                    try {
                        //A.Base = old base
                        //check if the initial product of the multiplication (repetitionCheck) comes up again, if it does, there is repitition
                        //store whatever coefficients have already been computed as the repeated part.
                        if(m >= 1 && Arrays.equals(multiplicationResult.quotient, repetitionCheck.quotient)){
                            conversionResult.Rep = new short[m];
                            for(int q = 0; q < m; q++){
                               repeats.add(mCoefficients.get(q));
                            }
                            break;
                        } else
                        if (multiplicationResult.quotient[multiplicationResult.quotient.length - 1] >= A.Base) {
                            mCoefficients.add((int) (multiplicationResult.quotient[multiplicationResult.quotient.length - 1] / A.Base));
                            //short[] temp = new short[A.NonRep.length - 1];
                            multiplier = multiplicationResult.quotient;
                            multiplier[multiplicationResult.quotient.length-1] = (short) (multiplicationResult.quotient[multiplicationResult.quotient.length -1] % A.Base);
                        } else {
                            mCoefficients.add(0);
                            multiplier = multiplicationResult.quotient;
                        }
                        m++;
                    } catch(ArrayIndexOutOfBoundsException werwe){
                        System.out.println("du ma");
                        break;
                    }
                }
                //printArrayList(mCoefficients);
                conversionResult.Base = Base;
                conversionResult.Int = new short[remainders.size()];
                conversionResult.NonRep = new short[mCoefficients.size()];
                conversionResult.Rep = new short[repeats.size()];
                Collections.reverse(remainders);
                for(int i = 0; i < remainders.size(); i++){
                    conversionResult.Int[i] = remainders.get(i).shortValue();
                }
                //transform the mcoefficients arraylist into the NonRep array of shorts
                Collections.reverse(mCoefficients);
                for(int j = 0; j < mCoefficients.size(); j++){
                    conversionResult.NonRep[j] = mCoefficients.get(j).shortValue();
                }
                Collections.reverse(repeats);
                for(int k = 0; k < conversionResult.Rep.length; k++){
                    conversionResult.Rep[k] = repeats.get(k).shortValue();
                }
                System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*");
                printNumber(A);
                System.out.print("=");
                printNumber(conversionResult);
                System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*");
                return conversionResult;
            }

            //=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+

            public void printShortArray(short[] S) {
                for (int i = S.length-1; i>=0; i--) {
                    System.out.print(S[i]);
                }
            }
            public void printNumber(Number N) {
                System.out.print("(");
                N.printShortArray(N.Int);
                System.out.print(".");
                N.printShortArray(N.NonRep);
                System.out.print("{");
                N.printShortArray(N.Rep);
                System.out.print("})_");
                System.out.println(N.Base);
            }
            short Base; short[] Int,NonRep,Rep;
        }

        Number N1=new Number() ;
        //number N1 = 9194.247 in base 10     integer part: {9,1,9,4}     nonrep part: {2,4}      rep part: {0}
        N1.Base=10;
        N1.Int=new short[10];
        N1.NonRep=new short[3];
        N1.Int[9] = 8;
        N1.Int[8] = 7;
        N1.Int[7] = 9;
        N1.Int[6] = 4;
        N1.Int[5] = 0;
        N1.Int[4] = 0;
        N1.Int[3] = 0;
        N1.Int[2] = 0;
        N1.Int[1] = 0;
        N1.Int[0] = 1;
        N1.NonRep[2]=4;
        N1.NonRep[1]=0;
        N1.NonRep[0]=0;
        N1.Rep=new short[0];
        //N1.printNumber(N1);

        Number N4 = new Number();
        N4.Base = 3;
        N4.Int = new short[3];
        N4.Int[0] = 2;
        N4.Int[1] = 1;
        N4.Int[2] = 2;


        for(int b1 = 2; b1 <= 60; b1++){
            for(int b2 = 2; b2 < 60; b2++){
                Number N3 = new Number();
                N3.Base =  (short)b1;
                N3.Int = new short[20];
                Random rn = new Random();
                for(int pp = 0; pp < N3.Int.length; pp++){
                    //System.out.println((short)(rn.nextInt(N3.Base - 1)));
                    N3.Int[pp] = (short)(rn.nextInt(N3.Base));
                }
                System.out.println("");
                N3.NonRep = new short[20];
                for(int pp = 0; pp < N3.Int.length; pp++){
                    N3.NonRep[pp] = (short)(rn.nextInt(N3.Base));
                    //System.out.print(N3.NonRep[pp]);
                }
                N3.Rep = new short[0];

                Number C = new Number();
                short r = 60;
                C = N1.convert(N3, (short)b2);
            }
        }

        // C.printNumber(C);

//        Number N2=new Number() ;
//        short R=2;
//        N2=N1.convert(N1,R);
//        N2.printNumber(N2);



    }

}
