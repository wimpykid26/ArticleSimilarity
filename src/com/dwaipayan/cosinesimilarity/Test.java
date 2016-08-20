package com.dwaipayan.cosinesimilarity;


import java.io.IOException;
import org.apache.lucene.store.LockObtainFailedException;

public class Test {
    
    public static void main(String[] args) throws LockObtainFailedException, IOException
    {
        getCosineSimilarity();
    }
    
    public static void getCosineSimilarity() throws LockObtainFailedException, IOException
    {
       Indexer index = new Indexer();
       index.index();
       VectorGenerator vectorGenerator = new VectorGenerator();
       vectorGenerator.GetAllTerms();       
       DocVector[] docVector = vectorGenerator.GetDocumentVectors(); // getting document vectors
       for(int i = 0; i < docVector.length; i++)
       {
           double cosineSimilarity = CosineSimilarity.CosineSimilarity(docVector[5], docVector[i]);
           System.out.println("Cosine Similarity Score between document 5 and "+i+"  = " + cosineSimilarity);
       }    
    }
}