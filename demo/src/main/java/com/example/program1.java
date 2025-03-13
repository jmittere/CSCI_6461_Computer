package com.example;
import java.util.Scanner;
public class program1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = new int[20];
        // Just reading the 20 integers from the keyboard and printing them in the log. 
        System.out.println("Please provide 20 integers (between -32768 and 32767):");
        for (int i = 0; i < 20; i++) {
            numbers[i] = scanner.nextInt();
        } 
        System.out.println("User Input:");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        // Input from the user
        System.out.print("\nProvide the number to find the closest match: ");
        int userNumber = scanner.nextInt();
        int closestNumber = numbers[0];
        int minDifference = Math.abs(userNumber - closestNumber);
        for (int i = 1; i < 20; i++) {
            int difference = Math.abs(userNumber - numbers[i]);
            if (difference < minDifference) {
                minDifference = difference;
                closestNumber = numbers[i];
            }
        }
        System.out.println("User Input : " + userNumber);
        System.out.println("Closest number found: " + closestNumber);
        scanner.close();
    }
}
