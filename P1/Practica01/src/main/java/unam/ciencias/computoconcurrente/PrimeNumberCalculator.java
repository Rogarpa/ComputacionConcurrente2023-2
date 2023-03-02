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
    
        /**
     * Metodo que calcula si un número es primo concurrentemente, con la cantidad de hilos threads.
     * @param n número a saber si es primo
     * @return true si el número es primo, false en cualquier otro caso.
     */
    
    public boolean isPrime(int n) throws InterruptedException{
        n = Math.abs(n);        
        if(n == 0 || n ==1) return false;


        int cantidadDivisores = (int)((Math.floor(Math.sqrt(n)))+1) - 2;
        if(threads > cantidadDivisores) threads = cantidadDivisores;

        boolean[] rangosSonPrimos = new boolean[threads];
        Thread[] threadsForRanges = new Thread[threads];
        
        for(int i = 0; i < threads; i++){
            int threadIndex = i;
            int number = n;
            Runnable esPrimoThread = () -> {
                rangosSonPrimos[threadIndex] = isPrimeSequential(number, threadIndex);
            };

            threadsForRanges[i] = new Thread(esPrimoThread);
            threadsForRanges[i].start();
        }

        for (Thread thread : threadsForRanges) {
            thread.join();
        }

        for(int i = 0; i<threadsForRanges.length; i++){
            if (!rangosSonPrimos[i]) 
                return false;
        }
        return true;

    }

    /**
     * Metodo que calcula si un número tiene divisores en el rango que cubre su thread
     * @param n número a saber si tiene divisores
     * @param threadID ID del 0-threads del hilo que tomara ese rango de divisores
     * @return false si hay divisores en el rango que le corresponde a ese hilo de el conjunto de divisores de n, true en cualquier otro caso.
     */

    public boolean isPrimeSequential(int n, int threadID){
        if(n == 0 || n ==1) return false;



        int longitudRango = (int)((Math.floor(Math.sqrt(n)))+1) - 2;
        int limiteInferior = (int) ((Math.floor(threadID*(longitudRango)/ threads))+2);
        int limiteSuperior = (int)((Math.floor((threadID+1)*(longitudRango)/ threads))+2);
        
        for(int i = limiteInferior; i<limiteSuperior; i++){
            if ((n%i) == 0) return false;
        }
        return true;
    }
    


    
}
