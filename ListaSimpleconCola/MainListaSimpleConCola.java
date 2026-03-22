package ListaSimpleconCola;

import java.util.Random;

public class MainListaSimpleConCola {

    public static volatile double dummy = 0;

    public static ListaSimpleConCola crearLista(int n, Random rand) {
        ListaSimpleConCola l = new ListaSimpleConCola();
        for (int i = 0; i < n; i++) {
            l.pushBack(rand.nextDouble());
        }
        return l;
    }

    public static void main(String[] args) {

        int[] sizes = {10, 100, 10000, 1000000, 100000000};
        Random rand = new Random();

        for (int n : sizes) {

            System.out.println("\nTamaño: " + n);

            ListaSimpleConCola lsc;

            // pushFront
            lsc = new ListaSimpleConCola();
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                lsc.pushFront(rand.nextDouble());
            }
            long fin = System.nanoTime();
            System.out.println("pushFront: " + (fin - inicio) + " ns");

            // pushBack
            lsc = new ListaSimpleConCola();
            inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                lsc.pushBack(rand.nextDouble());
            }
            fin = System.nanoTime();
            System.out.println("pushBack: " + (fin - inicio) + " ns");

            int repeticiones = 100;

            // find
            lsc = crearLista(n, rand);
            double objetivo = rand.nextDouble();
            lsc.pushBack(objetivo);

            inicio = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                dummy += (lsc.find(objetivo) != null ? 1 : 0);
            }
            fin = System.nanoTime();
            System.out.println("find (promedio): " + ((fin - inicio) / repeticiones) + " ns");

            // addAfter
            lsc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            lsc.pushBack(objetivo);

            inicio = System.nanoTime();
            lsc.addAfter(objetivo, rand.nextDouble());
            fin = System.nanoTime();
            System.out.println("addAfter: " + (fin - inicio) + " ns");

            // addBefore
            lsc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            lsc.pushBack(objetivo);

            inicio = System.nanoTime();
            lsc.addBefore(objetivo, rand.nextDouble());
            fin = System.nanoTime();
            System.out.println("addBefore: " + (fin - inicio) + " ns");

            // popFront
            lsc = crearLista(n, rand);

            inicio = System.nanoTime();
            lsc.popFront();
            fin = System.nanoTime();
            System.out.println("popFront: " + (fin - inicio) + " ns");

            // popBack
            lsc = crearLista(n, rand);

            inicio = System.nanoTime();
            lsc.popBack();
            fin = System.nanoTime();
            System.out.println("popBack: " + (fin - inicio) + " ns");

            // erase
            lsc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            lsc.pushBack(objetivo);

            inicio = System.nanoTime();
            lsc.erase(objetivo);
            fin = System.nanoTime();
            System.out.println("erase: " + (fin - inicio) + " ns");

            dummy = 0;
        }
    }
}