
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JPM
 */
    public class ProgtestMELI extends Thread {
        
        private static final int EVAL_NUM = 1000000;
        
        static List<Integer> circprime_list = new ArrayList<>(EVAL_NUM);
        
        private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
        
        private static final int BLOCK = 200;
        
        private final List<Integer> startNums = new ArrayList<>();
    
        /* Constructor de la clase */
        public ProgtestMELI(String name) {super(name);}
 
        public void addStartNum(int startNum) {this.startNums.add(startNum);}
     
        /* Funcion para validar si un numero es primo circular */
        public static boolean isCircularPrime(int num) {
            String number = Integer.toString(num);
            if(isPrime(num)&& num>=10) //valida si el numero es primo y ademas mayor que 10 ya que no existen numeros primos circulares menores a 10
            {   
                if(number.contains("2")||number.contains("4")||number.contains("6")||number.contains("8")||number.contains("5")||number.contains("0"))
                    {return false;}
                else //verifica que no sea un "illegal" number y que la rotacion del numero tambien sea primo                
                {
                    for(int i = 0; i < number.length(); i++)
                    {
                        num = rotate(num);
			if(!isPrime(num)) 
                            {return false;}
                    }
		    return true; 
                }
            }             
            else
                {return false;}
        }
 
        /* Funcion para validar si un numero es primo */
        public static boolean isPrime(int num) {
            if(num == 1)
		{return false;}
            else if(num == 2)
                    {return true;}
            for(int i = 2; i <= num/2 + 1; i++)
		{
                    if(num%i==0)
                        {return false;}
		}
		return true;
	}
    
        /* Funcion para realizar la rotacion de un numero */
        public static int rotate(long number) {
            int numdigits = (int) Math.log10((double)number);
            int multiplier;
            multiplier = (int) Math.pow(10.0, (double)numdigits);
            int num = 0;
            long r = number % 10;
            number = number / 10;
            number = number + multiplier * r;
            num = (int) number;
            return num;
	}
        
        /* Codigo a ser ejecutado por los Threads */
        public void run() {
            for (final Integer startNum : startNums) {
                int i=0;                   
      		for (i=startNum; i <= startNum + BLOCK; i++)    
                {   //Ciclo para recorrer los numeros hasta el num 1000000
                    if(isCircularPrime(i))
                        {circprime_list.add(i);} //Agrega a un array los numeros econtrados que sean primos circulares
                }                         
            }          
        }     
       
        /* Metodo principal */
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Evaluando " + EVAL_NUM + " con " + MAX_THREADS + " Threads");
 
            //Inicializa el pool de threads
            ProgtestMELI[] threadPool = new ProgtestMELI[MAX_THREADS];
            for (int i = 0; i < threadPool.length; i++) 
            {threadPool[i] = new ProgtestMELI("Thread-" + i);}
 
            //Asigna porciones de valores a cada thread
            int j = 0;
            for (int i = 0; i < EVAL_NUM; i += BLOCK) 
            {
                threadPool[j++].addStartNum(i);
                if (j >= MAX_THREADS) 
                {j = 0;}
            }
 
            //Lanza todos los theads
            for (int i = 0; i < threadPool.length; i++) 
            {threadPool[i].start();}
 
            //Espera que terminen todos los threads antes de terminar el principal
            for (int i = 0; i < threadPool.length; i++) 
            {threadPool[i].join();}
            
            System.out.println("Cantidad de numeros primos circulares menores a " + EVAL_NUM + ": " + circprime_list.size()); 
            System.out.println("Listado de numeros primos circulares menores a " + EVAL_NUM + ": " + circprime_list.toString());              
        }
    }