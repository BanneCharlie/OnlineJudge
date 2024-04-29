package com.yupi.oj;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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

class Main1 {
    public static void main(String[] args)
    {
        String s;
        int a[];
        a = new int[10];
        Scanner reader = new Scanner(System.in);
        s = reader.nextLine();
        for(int i=0;i<s.length();i++)
        {
            for(int j=0;j<10;j++)
            {
                if(s.charAt(i)-48==j)//判断字符串s中第i个字符是否等于j
//s.chatAt(i)-48是用来获取字符对应的数字，因为字符“0”的ASCII码值为48，减去48就可以得到该字符对应的数字
                {
                    a[j]++;
                }
            }
        }
        for(int i=0;i<10;i++)//遍历数组a，输出每个非零元素的下标
        {
            if(a[i]!=0)
            {
                System.out.print(i+":"+a[i] + " ");
            }
        }

    }
}
