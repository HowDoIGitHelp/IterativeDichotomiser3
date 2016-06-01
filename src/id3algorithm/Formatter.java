/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id3algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rub account
 */
public class Formatter {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("positives.csv"));
            List<String> positives = new ArrayList();
            while(sc.hasNext()){
                positives.add(sc.nextLine());
            }
            sc = new Scanner(new File("negatives.csv"));
            List<String> negatives = new ArrayList();
            while(sc.hasNext()){
                negatives.add(sc.nextLine());
            }
            sc = new Scanner(new File("p+nheaders.csv"));
            List<String> headersPositive = new ArrayList();
            List<String> headersNegative = new ArrayList();
            
            while(sc.hasNext()){
                String[] headers = sc.nextLine().split(",");
                headersPositive.add(headers[0]);
                headersNegative.add(headers[1]);
            }
            PrintWriter writer = new PrintWriter("P+N_DATA.csv", "UTF-8");
            for (int i = 0,j=0; i < headersNegative.size()&&j < headersPositive.size();) {
                //System.out.println(i+","+j);
                //System.out.println(headersNegative.get(i)+","+headersPositive.get(j));
                if(headersNegative.get(i).equals(headersPositive.get(j))){
                    System.out.println(headersNegative.get(i)+","+negatives.get(i)+","+positives.get(j));
                    writer.println(headersNegative.get(i)+","+negatives.get(i)+","+positives.get(j));
                    i++;
                    j++;
                }
                else{
                    j++;
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Formatter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Formatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
