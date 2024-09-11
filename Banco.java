package Semaforo;

import java.util.concurrent.Semaphore;

class Clientes implements Runnable {
    private final Semaphore semaforo;
    private int id;
    private int dineroCliente;
    
    //Inicializamos las varibles de nuestro hilo
    public Clientes(Semaphore semaforo, int id, int dineroCliente) {
        this.semaforo = semaforo;
        this.id = id;
        this.dineroCliente = dineroCliente;
    }

    // Los hilos se forman para entran, el primero que llega entra a la zona critica
    // y los demas esperan al hilo que esta en la zona critica para poder entrar
    // el segundo que llegó entra a la zona critica y los demas los esperan
    @Override
    public void run() {

        try {
            // Los clientes en realidad son los hilos
            System.out.println("Cliente " + id + " esta esperando en la fila");
            semaforo.acquire();// le avisa a los hilos que un hilo entro en la seccion critica
            System.out.println("Cliente " + id + " esta en la ventanilla");
            //Los hilos manipulan la variable en la sección critica "Depositando su dinero"

            int temp = Banco.dineroBanco;
            temp = temp + dineroCliente;

            //Banco.dineroBanco = Banco.dineroBanco + dineroCliente;
            //Un sleep para simular que los hilos son los clientes de un banco y se tardan en depositar
            Thread.sleep(2000);
            Banco.dineroBanco = temp;
        
            System.out.println("Cliente " + id + " ha depositado. Dinero total actual: " + Banco.dineroBanco); 
            semaforo.release(); // le avisa a los demas hilos que salio de la seccion critica
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

public class Banco {
    public static int dineroBanco = 0;
    public static void main(String[] args) {
        // se declara cuantos hilos pueden entrar a la zona critica
        Semaphore semaforo = new Semaphore(1);

        Thread cliente1 = new Thread(new Clientes(semaforo, 1 , 100));
        Thread cliente2 = new Thread(new Clientes(semaforo, 2 , 200));
        Thread cliente3 = new Thread(new Clientes(semaforo, 3 , 300));
        Thread cliente4 = new Thread(new Clientes(semaforo, 4 , 400));
        Thread cliente5 = new Thread(new Clientes(semaforo, 5 , 500));
        
        cliente1.start();
        cliente2.start();
        cliente3.start();
        cliente4.start();
        cliente5.start();
        try {
            cliente1.join();
            cliente2.join();
            cliente3.join();
            cliente4.join();
            cliente5.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("El dinero del banco es:" + dineroBanco);

    }
}
