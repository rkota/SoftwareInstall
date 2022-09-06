package com.rkota.ncr.collab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MainTest {
    
    public static void main(String[] args)
    {
        Set<Component> installed = new HashSet<Component>();
        List<Operation> operations = new LinkedList<>();
        System.out.println("Input : ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line;
        try {
            while ((line = br.readLine()) != null)
            {
                if(line.equalsIgnoreCase("END"))
                    break;
                operations.add(new Operation(installed, line));

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("==============OUTPUT=============");
        for(Operation op : operations){
            op.process();
        }
    }

}
