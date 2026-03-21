import java.util.Random;
public static void Medir(int n) {
    StackArray<Integer> stack = new StackArray<>();
    Random random = new Random();
    
    System.out.println("--- Pruebas para tamaño N = " + n + " ---");

    // 1. Medir PUSH (O(1) amortizado) 
    long inicio = System.nanoTime();
    for (int i = 0; i < n; i++) {
        stack.push(random.nextInt(1000000));
    }
    long fin = System.nanoTime();
    System.out.println("Push (" + n + " elementos): " + (fin - inicio) / 1000.0 + " us");

    // 2. Medir PEEK (O(1)) 
    inicio = System.nanoTime();
    stack.peek();
    fin = System.nanoTime();
    System.out.println("Peek: " + (fin - inicio) / 1000.0 + " us");

    // 3. Medir SIZE e ISEMPTY (O(1))
    inicio = System.nanoTime();
    stack.size();
    stack.isEmpty();
    fin = System.nanoTime();
    System.out.println("Size/IsEmpty: " + (fin - inicio) / 1000.0 + " us");

    // 4. Medir DELETE (O(n)) 
    int valorAleatorio = random.nextInt(1000000);
    inicio = System.nanoTime();
    stack.delete(valorAleatorio);
    fin = System.nanoTime();
    System.out.println("Delete: " + (fin - inicio) / 1000.0 + " us");

    // 5. Medir POP (O(1))
    inicio = System.nanoTime();
    stack.pop();
    fin = System.nanoTime();
    System.out.println("Pop: " + (fin - inicio) / 1000.0 + " us");
    
    System.out.println("--------------------------------------\n");
}
public static void main(String[] args) {
    int[] tamaños = {1000, 5000, 10000, 50000, 100000}; // Tamaños crecientes 
    for (int n : tamaños) {
        Medir(n);
    }
}