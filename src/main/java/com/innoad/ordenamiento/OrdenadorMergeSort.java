package com.innoad.ordenamiento;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class OrdenadorMergeSort {

    private static final String NOMBRE_ARCHIVO = "numeros_aleatorios.txt";
    private static final int NUMERO_MINIMO = 100;
    private static final int NUMERO_MAXIMO = 999;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   ORDENAMIENTO CON MERGE SORT - EQUIPO INNOAD            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Solicitar cantidad de números al usuario
        System.out.print("Ingrese la cantidad de números enteros aleatorios a generar: ");
        int cantidad = scanner.nextInt();

        // Generar números aleatorios
        List<Integer> numerosAleatorios = generarNumerosAleatorios(cantidad);

        // Guardar números en archivo
        guardarNumerosEnArchivo(numerosAleatorios, NOMBRE_ARCHIVO);
        System.out.println("✓ Números generados y guardados en: " + NOMBRE_ARCHIVO);

        // Leer números desde el archivo
        List<Integer> numerosLeidos = leerNumerosDesdeArchivo(NOMBRE_ARCHIVO);
        System.out.println("✓ Números leídos desde el archivo: " + numerosLeidos.size() + " elementos");

        // Mostrar primeros 10 números originales (si hay al menos 10)
        System.out.println("\n--- NÚMEROS ORIGINALES (primeros 10) ---");
        mostrarPrimerosElementos(numerosLeidos, 10);

        // Iniciar cronómetro
        long tiempoInicio = System.currentTimeMillis();
        System.out.println("\n⏱ Iniciando ordenamiento con Merge Sort...");

        // Ordenar usando Merge Sort
        List<Integer> numerosOrdenados = mergeSort(numerosLeidos);

        // Detener cronómetro
        long tiempoFin = System.currentTimeMillis();
        long tiempoTranscurrido = tiempoFin - tiempoInicio;

        // Mostrar resultados
        System.out.println("✓ Ordenamiento completado");
        System.out.println("\n--- NÚMEROS ORDENADOS (primeros 10) ---");
        mostrarPrimerosElementos(numerosOrdenados, 10);

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  TIEMPO DE ORDENAMIENTO: " + tiempoTranscurrido + " milisegundos");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        // Mostrar todos los números ordenados
        System.out.println("\n¿Desea ver todos los números ordenados? (s/n): ");
        String respuesta = scanner.next();
        if (respuesta.equalsIgnoreCase("s")) {
            System.out.println("\n--- TODOS LOS NÚMEROS ORDENADOS ---");
            mostrarTodosLosNumeros(numerosOrdenados);
        }

        scanner.close();
    }

    /**
     * Generaria una lista de numeros enteros aleatorios
     * @param cantidad Cantidad de numeros a generar
     * @return Lista con numeros aleatorios entre 100 y 999
     */
    private static List<Integer> generarNumerosAleatorios(int cantidad) {
        List<Integer> numeros = new ArrayList<>();
        Random random = new Random();

        System.out.println("\n⚙ Generando " + cantidad + " numeros aleatorios...");

        for (int i = 0; i < cantidad; i++) {
            int numeroAleatorio = random.nextInt(NUMERO_MAXIMO - NUMERO_MINIMO + 1) + NUMERO_MINIMO;
            numeros.add(numeroAleatorio);
        }

        return numeros;
    }

    /**
     * Guarda la lista de números en un archivo de texto
     * @param numeros Lista de numeros a guardar
     * @param nombreArchivo Nombre del archivo donde se guardaran los numeros
     */
    private static void guardarNumerosEnArchivo(List<Integer> numeros, String nombreArchivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Integer numero : numeros) {
                escritor.write(numero.toString());
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar números en el archivo: " + e.getMessage());
        }
    }

    /**
     * Lee numeros desde un archivo de texto y los almacena en una lista
     * @param nombreArchivo Nombre del archivo a leer
     * @return Lista con los numeros leídos del archivo
     */
    private static List<Integer> leerNumerosDesdeArchivo(String nombreArchivo) {
        List<Integer> numeros = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                numeros.add(Integer.parseInt(linea.trim()));
            }
        } catch (IOException e) {
            System.err.println("Error al leer números del archivo: " + e.getMessage());
        }

        return numeros;
    }

    /**
     * Implementacion del algoritmo Merge Sort
     * Divide la lista en sublistas hasta tener elementos individuales,
     * luego las mezcla de forma ordenada
     * @param lista Lista de numeros a ordenar
     * @return Lista ordenada
     */
    private static List<Integer> mergeSort(List<Integer> lista) {
        // Caso base: si la lista tiene 1 o menos elementos, ya está ordenada
        if (lista.size() <= 1) {
            return lista;
        }

        // Dividir la lista en dos mitades
        int puntoMedio = lista.size() / 2;
        List<Integer> izquierda = new ArrayList<>();
        List<Integer> derecha = new ArrayList<>();

        // Llenar la sublista izquierda
        for (int i = 0; i < puntoMedio; i++) {
            izquierda.add(lista.get(i));
        }

        // Llenar la sublista derecha
        for (int i = puntoMedio; i < lista.size(); i++) {
            derecha.add(lista.get(i));
        }

        // Aplicar recursivamente merge sort a ambas mitades
        izquierda = mergeSort(izquierda);
        derecha = mergeSort(derecha);

        // Mezclar las dos mitades ordenadas
        return mezclar(izquierda, derecha);
    }

    /**
     * Mezcla dos listas ordenadas en una sola lista ordenada
     * @param izquierda Primera lista ordenada
     * @param derecha Segunda lista ordenada
     * @return Lista resultante de mezclar ambas listas de forma ordenada
     */
    private static List<Integer> mezclar(List<Integer> izquierda, List<Integer> derecha) {
        List<Integer> resultado = new ArrayList<>();

        int indiceIzq = 0;
        int indiceDer = 0;

        // Comparar elementos de ambas listas y agregar el menor al resultado
        while (indiceIzq < izquierda.size() && indiceDer < derecha.size()) {
            if (izquierda.get(indiceIzq) <= derecha.get(indiceDer)) {
                resultado.add(izquierda.get(indiceIzq));
                indiceIzq++;
            } else {
                resultado.add(derecha.get(indiceDer));
                indiceDer++;
            }
        }

        // Agregar elementos restantes de la lista izquierda
        while (indiceIzq < izquierda.size()) {
            resultado.add(izquierda.get(indiceIzq));
            indiceIzq++;
        }

        // Agregar elementos restantes de la lista derecha
        while (indiceDer < derecha.size()) {
            resultado.add(derecha.get(indiceDer));
            indiceDer++;
        }

        return resultado;
    }

    /**
     * Muestra los primeros N elementos de una lista
     * @param lista Lista de números
     * @param cantidad Cantidad de elementos a mostrar
     */
    private static void mostrarPrimerosElementos(List<Integer> lista, int cantidad) {
        int limite = Math.min(cantidad, lista.size());
        for (int i = 0; i < limite; i++) {
            System.out.print(lista.get(i) + " ");
        }
        if (lista.size() > cantidad) {
            System.out.print("... (y " + (lista.size() - cantidad) + " más)");
        }
        System.out.println();
    }

    /**
     * Muestra todos los numeros de una lista en formato columnar
     * @param lista Lista de numeros a mostrar
     */
    private static void mostrarTodosLosNumeros(List<Integer> lista) {
        int contador = 0;
        for (Integer numero : lista) {
            System.out.print(numero + " ");
            contador++;
            if (contador % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}