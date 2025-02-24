package com.example;

//class of helper functions to convert Decimal, Octal, Binary numbers
public class Conversion {

    //converts a String num in decimal to a binary string with a specified number of characters len
    public static String convertToBinaryString(String num, int len){
        //ensures that the number of characters is equal to len
        return String.format("%" + len +"s", Integer.toBinaryString(Integer.parseInt(num))).replace(' ', '0');
    }

    //converts an Integer num in decimal to a binary string with a specified number of characters len
    public static String convertToBinaryString(int num, int len){
        //ensures that the number of characters is equal to len
        return String.format("%" + len +"s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    
    //converts a decimal number to octal - base 8
    public static String convertToOctal(int decimal) {
        String octalNum = Integer.toOctalString(decimal);
        int octalLength = octalNum.length();
        if (octalLength == 6){
            return octalNum;
        }else{
            return "0".repeat(6-octalLength) + octalNum;
        }
    }

    //converts a binary string to an octal string
    public static String convertToOctal(String binaryNum){
        int decimalNum = Integer.parseInt(binaryNum, 2);
        return convertToOctal(decimalNum);
    }

    //converts an octal string to its binary representation
    public static String convertToBinary(String octalNum){
        int decimalNumber = Integer.parseInt(octalNum, 8);   
        String binaryNumber = Integer.toBinaryString(decimalNumber);
        return binaryNumber;
    }

    //converts an octal string to its decimal int representation
    public static int convertToDecimal(String octalNum){
        return Integer.parseInt(octalNum, 8);
    }

    //converts an octal string to its decimal string representation
    public static String convertToDecimalString(String octalNum){
        return Integer.toString(Integer.parseInt(octalNum, 8));
    }

}
