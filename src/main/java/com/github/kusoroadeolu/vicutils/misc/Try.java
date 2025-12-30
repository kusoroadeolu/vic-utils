package com.github.kusoroadeolu.vicutils.misc;

public class Try {
     private Try(){}

     public static void run(ExceptionRunnable er){
         try {
             er.run();
         }catch (Exception _){

         }
     }

     public static <T>T supply(ExceptionSupplier<T> es){
         try {
             return es.supply();
         }catch (Exception _){
             return null;
         }
     }
}
