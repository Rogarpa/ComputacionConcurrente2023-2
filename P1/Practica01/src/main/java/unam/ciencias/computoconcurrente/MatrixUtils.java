package unam.ciencias.computoconcurrente;

public class MatrixUtils implements Runnable{
    private int threads;
    private static int[] posiblesMinimos; // Arreglo para que cada hilo guarde su minimo encontrado
    private static int[] matrixGlobal; 
    private static int secciones; 

    public MatrixUtils() {
        this.threads = 1;
    }

    public MatrixUtils(int threads) {
        this.threads = threads;
    }

    @Override
    public void run() {

    }

    public double findAverage(int[][] matrix) throws InterruptedException{
        int nThreads = this.threads;
        if (this.threads > matrix.length) nThreads = matrix.length;
        int rowsCant = (int) Math.floor(matrix.length/nThreads);
        int rowsCantOg = rowsCant;
        System.out.println("Participating threads: "+nThreads);

        Thread[] threads = new Thread[nThreads];
        double[] results = new double[nThreads];

        for (int i=0; i<nThreads; i++){
            if(i==nThreads-1){
                rowsCant = matrix.length - i*rowsCant;
            }
            int[][] submatrix = new int[rowsCant][matrix.length];
            for (int j=0; j<rowsCant; j++){
                submatrix[j]=matrix[i*rowsCantOg + j];
            }

            /// Start thread and make it get the average of the submatrix ///
            int threadIndex = i;
            Runnable getAverage = () -> {
                results[threadIndex] = Average(submatrix);
            };
            threads[i] = new Thread(getAverage);
            System.out.println("Thread "+i+" is working with this matrix:");
            printMatrix(submatrix);
            System.out.println();
            threads[i].start();
            ////////////////////////////////////////////////////////////////
        }

        for (Thread thread : threads) {
            thread.join();
        }

        double sum = 0;
        for (int i = 0; i < results.length; i++) {
            sum = sum + results[i];
        }
        return sum/results.length;
    }

    /**
     * Metodo que recorre una matriz de dos dimensiones 
     * @param matrix - matriz de dos dimensiones 
     * @return promedio - promedio de la matriz
     */
    public double Average(int[][] matrix) {
        int suma = 0;
        int count = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                suma += matrix[i][j];
                count++;
            }
        }

        double avg = (double) suma / count;

        return avg;
    }
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println(); // Add a new line after each row
        }
    }

    public static void printMatrixDouble(double[] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(matrix[i] + " ");
        }
    }

}
