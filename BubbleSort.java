import java.util.Scanner;

public class BubbleSort {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of elements: ");
        int x = scanner.nextInt();
        int[] arr = new int[n];
        System.out.println("Enter the elements:");
        for (int i = 0; i < x; i++) {
            arr[i] = scanner.nextInt();
        }
        bubbleSort(arr);
        System.out.println("Sorted array in ascending order:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        scanner.close();
    }
    public static void bubbleSort(int[] arr) {
        int x = arr.length;
        boolean swapped;

        for (int i = 0; i < x - 1; i++) {
            swapped = false;

            for (int j = 0; j < x - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}
