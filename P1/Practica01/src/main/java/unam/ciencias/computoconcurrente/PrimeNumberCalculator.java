package unam.ciencias.computoconcurrente;
import java.lang.Math;
public class PrimeNumberCalculator implements Runnable{

    private int threads;
    
    //No usadas
    private static int numPrimo;
    public static boolean result;
    public static int longitudSubInter; //Dividim`os el intervalo [2,N-1] en this.threads cantidad de sub interbalos, uno por cada hilo

    @Override
    public void run() {

    }

    public PrimeNumberCalculator() {
        this.threads = 1;
    }

    public PrimeNumberCalculator(int threads) {
        this.threads = threads > 1 ? threads : 1;
    }
    


    // public double findAverage(int[][] matrix) throws InterruptedException{
    //     //VERIFICA HILOS
    //     int nThreads = this.threads;
    //     if (this.threads > matrix.length) nThreads = matrix.length;



    //     int rowsCant = (int) Math.floor(matrix.length/nThreads);
    //     int rowsCantOg = rowsCant;
        
        
    //     System.out.println("Participating threads: "+nThreads);

    //     Thread[] threads = new Thread[nThreads];
    //     double[] results = new double[nThreads];

        
    //     for (int i=0; i<nThreads; i++){
    //         //?            
    //         if(i==nThreads-1){
    //             rowsCant = matrix.length - i*rowsCant;
    //         }

    //         //MATRIZ A REPARTIR
    //         int[][] submatrix = new int[rowsCant][matrix.length];
            

    //         //Genera submatriz de acuerdo a vectores que tocan
    //         for (int j=0; j<rowsCant; j++){
    //             submatrix[j]=matrix[i*rowsCantOg + j];
    //         }

    //         /// Start thread and make it get the average of the submatrix ///
    //         int threadIndex = i;
    //         Runnable getAverage = () -> {
    //             results[threadIndex] = Average(submatrix);
    //         };


    //         //INICIA HILOS
    //         threads[i] = new Thread(getAverage);
            
    //         //DATA PRINT
    //         System.out.println("Thread "+i+" is working with this matrix:");
    //         printMatrix(submatrix);
    //         System.out.println();
            
    //         //INICIA
    //         threads[i].start();
    //         ////////////////////////////////////////////////////////////////
    //     }
    //     //JOIN
    //     for (Thread thread : threads) {

    //         thread.join();
    //     }
    //     //AVERAGE OF RESULTS
    //     double sum = 0;
    //     for (int i = 0; i < results.length; i++) {
    //         sum = sum + results[i];
    //     }
    //     return sum/results.length;
    // }

    public boolean isPrime(int n) throws InterruptedException{
        //?
        int numPrimo = n;


        int cantidadDivisores = (int)((Math.floor(Math.sqrt(n)))+1) - 2;
        if(threads > cantidadDivisores) threads = cantidadDivisores;

        boolean[] rangosSonPrimos = new boolean[threads];
        Thread[] threadsForRanges = new Thread[threads];
        
        for(int i = 0; i < threads; i++){
            Runnable esPrimoThread = () -> {
                rangosSonPrimos[i] = isPrimeSequential(n, i);
            };

            threadsForRanges[i] = new Thread(esPrimoThread);
            threadsForRanges[i].start();
        }




        

        for (Thread thread : threadsForRanges) {
            thread.join();
        }

        for(int i = 0; i<threadsForRanges.length; i++){
            if (rangosSonPrimos[i]) 
                return true;
        }
        return false;

    }


    public boolean isPrimeSequential(int n, int threadID){
        int longitudRango = (int)((Math.floor(Math.sqrt(n)))+1) - 2;
        int limiteInferior = (int) ((Math.floor(threadID*(longitudRango)/ threads))+2);
        int limiteSuperior = (int)((Math.floor((threadID+1)*(longitudRango)/ threads))+2);
        
        for(int i = limiteInferior; i<limiteSuperior; i++){
            if ((n%i) == 0) return false;
        }
        return true;
    }
    


    
}
