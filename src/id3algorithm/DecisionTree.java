/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id3algorithm;

/**
 *
 * @author rub account
 */
public class DecisionTree {
    String attribute;
    int attributeIndex;
    DecisionTree yes,no;
    DecisionTree(int i,String s,DecisionTree y,DecisionTree n){
        attributeIndex=i;
        attribute = s;
        yes=y;
        no=n;
    }
}
