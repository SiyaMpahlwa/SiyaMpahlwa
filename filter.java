import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ExecutionException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;

class filter extends RecursiveTask<double[]>{
   //Static fields
   static int SEQUENTIAL_CUTOFF = 100;
   int median, width;
   String filename;
   double[] array;
   double[] temp;
   double[] mf;
   
   //Constructor
   filter(double[] array, int width, String filename){
      this.array = array;
      this.width = width;
      this.filename = filename;
      median = (width-1)/2;
      }
      
   //Overrun compute()
   @Override
   public double[] compute(){
      if (array.length <= SEQUENTIAL_CUTOFF){
         //Run the median method.
         median(array, width); 
         }
         else{
            filter left = new filter(array, width,filename);
            filter right = new filter(array, width, filename);
            left.fork();
            right.compute();
            left.join();
             
            }
            write(mf, filename);
            return mf;
      }
   /*
    *Filter method with the filtering algorithm.
    *
    */
   private void median(double[] array, int median){
      //Add the first and last elements.
      temp[0] = array[0];
      temp[array.length] = array[array.length];
      //Add the elements in between the edges.
      for(int i =1; i < array.length; i++){
         for(int j = i; j <= width+i; j++){
            temp[i] = array[i];
            }
         Arrays.sort(temp);
         
         mf[i] = temp[median];
         //return mf;
         }
      }
      /*
       *Write method that writes array contents to a file 
       *Pre-condition: array and file name
       *Post-condition: file has array written to it.
       */
       public void write(double[] arr, String filename){
      try{
         BufferedWriter opW = new BufferedWriter(new FileWriter(filename));
         opW.write(Arrays.toString(arr));
         opW.flush();
         opW.close();
         }
         catch(IOException e){e.printStackTrace();}}
   }
class Parallelism{
   public static void main(String[] args) throws InterruptedException, ExecutionException{
      int width = Integer.valueOf(args[1]);
      String output = args[2];
      String dataFile = args[0];
      
      double[] array = toArray(dataFile);
      //double obj = (new ForkJoinPool()).invoke(new filter(toArray(dataFile), width, output));
      filter task = new filter(array, width, output);
      ForkJoinPool pool = new ForkJoinPool();
      pool.execute(task);
      double[] result = task.get();
      }
      /*Method to read data file and send it to array
       *Data File
       *Array
       */
       public static double[] toArray(String file){
         double[] array = new double[10000];
         try{
            File data = new File(file);
            Scanner sc = new Scanner(data);
            
            String line;
            array = new double[Integer.parseInt(sc.nextLine())];
            int num  = 0;
            
            while (sc.hasNextLine()){
               line = sc.nextLine();
               String[] arr = line.split(" ");
               array[num] = Double.parseDouble(arr[1]);
               num++;
               }
            }
            catch(IOException e){
               e.printStackTrace();
               }
               return array;
         }
      
   }