import java.util.*;
class Main{
    public static void main(String[] args){
        //1.在控制台读取一个不超过10的正整数
        int n = Integer.parseInt(args[0]);
        //2.代入公式
        double result = Math.pow(2,n);
        int pow = (int)result;
        //3.根据输出的格式，格式化需要的结果
        System.out.println("2^"+n+" = "+pow);
    }
}


import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        //1.在控制台读取一个不超过10的正整数
        int n = scanner.nextInt();
        //2.代入公式
        double result = Math.pow(2,n);
        int pow = (int)result;
        //3.根据输出的格式，格式化需要的结果
        System.out.println("2^"+n+" = "+pow);
    }
}