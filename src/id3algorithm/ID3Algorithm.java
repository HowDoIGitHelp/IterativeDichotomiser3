/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id3algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rub account
 */
public class ID3Algorithm {

    /**
     * @param args the command line arguments
     */
    Set<Integer> dataSet;
    Set<Integer> attributeSet;
    String[] attributes;
    int[][] transposedData,transposedTestData;
    public static void main(String[] args){
        // TODO code application logic here

        ID3Algorithm id3 = new ID3Algorithm();
        DecisionTree root = id3.loadData("P+N_Train.csv");
        id3.test(root,"P+N_Train.csv");
        id3.pathSearch(root, "");
        
    }
    public DecisionTree loadData(String dataFileName){
        
        try {
            List<String[]> rawData = new ArrayList();
            Scanner sc = new Scanner(new File(dataFileName));
            while(sc.hasNext()){
                rawData.add(sc.nextLine().split(","));
            }
            attributeSet = new TreeSet();
            dataSet = new TreeSet();
            attributes = new String[rawData.size()];
            transposedData = new int[rawData.get(0).length-1][rawData.size()];
            for (int i = 0; i < rawData.size(); i++) {
                
                attributes[i]=rawData.get(i)[0];
                attributeSet.add(i);
                
                for (int j = 1; j < rawData.get(0).length; j++) {
                    transposedData[j-1][i]=Integer.valueOf(rawData.get(i)[j]);
                    dataSet.add(j-1);
                }
            }
            
            return ID3(dataSet,attributes.length-1,attributeSet);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ID3Algorithm.class.getName()).log(Level.SEVERE, null, ex);
            return new DecisionTree(0,null,null,null);
        }
    }
    DecisionTree ID3(Set<Integer> data,int targetAttribute,Set<Integer> attr){
        DecisionTree yesSubtree,noSubtree;
        int bestAttribute=0;
        double bestIG=-1;
        if(purity(data,1)>0.9)
            return new DecisionTree(-1,"positive",null,null);
        else if(purity(data,0)>0.9)
            return new DecisionTree(-1,"negative",null,null);
        else if(attr.size()==0)
            return new DecisionTree(-1,"indeterminate",null,null);
        else{
            for(int i:attr){
                //System.out.println(i+"---->"+bestAttribute+":"+bestIG+" data size;"+data.size());
                if(informationGain(data,i)>bestIG){
                    bestAttribute=i;
                    bestIG=informationGain(data,bestAttribute);
                }
            }
//            System.out.println("---->"+bestAttribute+":"+bestIG+" data size:"+data.size());
            
            Set<Integer> yesSubset=new TreeSet();
            Set<Integer> noSubset=new TreeSet();
            for(int i:data){
                if(transposedData[i][bestAttribute]>0)
                    yesSubset.add(i);
                else
                    noSubset.add(i);
            }
            
            Set<Integer> attributeSubset = new TreeSet(attr);
            attributeSubset.remove(bestAttribute);
//            System.out.println(yesSubset.size()+","+noSubset.size());
//            System.out.println("attributes:"+attr.size());
            if(yesSubset.size()==0){
                yesSubtree = new DecisionTree(-1,"indeterminate",null,null);
            }
            else{
                yesSubtree = ID3(yesSubset,attributes.length-1,attributeSubset);
                
            }
            if(noSubset.size()==0){
                noSubtree = new DecisionTree(-1,"indeterminate",null,null);
            }
            else{
                noSubtree = ID3(noSubset,attributes.length-1,attributeSubset);
                
            }
                
        }
        System.out.println(attributes[bestAttribute]+":"+yesSubtree.attribute+":"+noSubtree.attribute);
        return new DecisionTree(bestAttribute,attributes[bestAttribute],yesSubtree,noSubtree);
    }
    double entropy(Set<Integer> data){
        if(data.size()==0){
            return 0.d;
        }
        double p=0,n=0;
        for(int i:data){
            if(transposedData[i][transposedData.length-1]==1)
                p++;
            else
                n++;
        }
        if(p==0.d||n==0.d){
            return 0.d;
        }
        else{
            p=p/data.size();
            n=n/data.size();
            return ((-p)*(Math.log(p)/Math.log(2)))+((-n)*(Math.log(n)/Math.log(2)));
        }
    }
    double informationGain(Set<Integer> data,int attribute){
        Set<Integer> yesSubset=new TreeSet();
        Set<Integer> noSubset=new TreeSet();
        for(int i:data){
            if(transposedData[i][attribute]>0)
                yesSubset.add(i);
            else
                noSubset.add(i);
        }
        double IG = entropy(data) - ((((double)yesSubset.size()/(double)data.size())*entropy(yesSubset))+(((double)noSubset.size()/(double)data.size())*entropy(noSubset)));
//        if(IG==0){
//            System.out.println(entropy(data)+"|"+((double)yesSubset.size()/(double)data.size())+" "+entropy(yesSubset)+" "+((double)noSubset.size()/(double)data.size())+" "+entropy(noSubset));
//        }
        return IG;
    }
    boolean purelyPositive(Set<Integer> data){
        for(int i:data){
            if(transposedData[i][attributes.length-1]==0)
                return false;
        }
        return true;
    }
    boolean purelyNegative(Set<Integer> data){
        for(int i:data){
            if(transposedData[i][attributes.length-1]==1)
                return false;
        }
        return true;
    }
    String mostAbundantPrediction(Set<Integer> data){
        double p=0,n=0;
        for(int i:data){
            if(transposedData[i][attributes.length-1]==1)
                p++;
            else
                n++;
        }
        return p>=n?"positive":"negative";
    }
    double purity(Set<Integer> data,int classification){
        double p=0,n=0;
        for(int i:data){
            if(transposedData[i][attributes.length-1]==1)
                p++;
            else
                n++;
        }
        //System.out.println(p/(p+n)+" "+n/(p+n));
        if(classification==1)
            return p/(p+n);
        else
            return n/(p+n);
    }
    void bfs(DecisionTree root){
        Queue<DecisionTree> q = new LinkedList();
        String path="";
        q.offer(root);
        path+=","+root.attribute;
        while(!q.isEmpty()){
            DecisionTree current = q.poll();
            q.offer(current.yes);
            q.offer(current.no);
        }
    }
    void pathSearch(DecisionTree tree,String path){
        if(tree==null){
            return;
        }
        if(tree.attribute.equals("positive")){
            System.out.println(path+"|positive");
        }
        else{
            pathSearch(tree.yes,path+"|"+tree.attributeIndex+":yes");
            pathSearch(tree.no,path+"|"+tree.attributeIndex+":no");
        }
    }
    void test(DecisionTree root,String dataFileName){
        try {
            List<String[]> rawData = new ArrayList();
            Scanner sc = new Scanner(new File(dataFileName));
            while(sc.hasNext()){
                rawData.add(sc.nextLine().split(","));
            }
            
            transposedTestData = new int[rawData.get(0).length-1][rawData.size()];
            
            for (int i = 0; i < rawData.size(); i++) {
                
                for (int j = 1; j < rawData.get(0).length; j++) {
                    transposedTestData[j-1][i]=Integer.valueOf(rawData.get(i)[j]);
                }
            }
            
            int truePositives=0,trueNegatives=0,falsePositives=0,falseNegatives=0;
            for (int i = 0; i < transposedTestData.length; i++) {
                String value=value(root,i);
                if(value.equals("positive")&&transposedTestData[i][transposedTestData[0].length-1]==1){
                    truePositives++;
                }
                if(value.equals("positive")&&transposedTestData[i][transposedTestData[0].length-1]==0){
                    falsePositives++;
                }
                if(value.equals("negative")&&transposedTestData[i][transposedTestData[0].length-1]==0){
                    trueNegatives++;
                }
                if(value.equals("negative")&&transposedTestData[i][transposedTestData[0].length-1]==1){
                   falseNegatives++;
                }
                System.out.println(value+","+transposedTestData[i][transposedTestData[0].length-1]);
            }
            System.out.println("tP"+truePositives);
            System.out.println("fP"+falsePositives);
            System.out.println("tN"+trueNegatives);
            System.out.println("fN"+falseNegatives);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ID3Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    String value(DecisionTree root,int example){
        for (int i = 0; i < transposedTestData[example].length; i++) {
            //System.out.print(root.attributeIndex+",");
            if(root.attributeIndex<0){
                System.out.print("~");
                return root.attribute;
            }
            else if(transposedTestData[example][root.attributeIndex]>0){
                root=root.yes;
            }
            else{
                root=root.no;
            }
        }
        System.out.println("");
        return root.attribute;
    }
}
