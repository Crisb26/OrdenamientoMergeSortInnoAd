
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
        boolean repetir = true;

        while (repetir) {
            System.out.println("==============================================================");
            System.out.println("   ORDENAMIENTO CON MERGE SORT - EQUIPO INNOAD            ");
            System.out.println("==============================================================");
            System.out.println();

            // Solicitar cantidad de numeros al usuario
            System.out.print("Ingrese la cantidad de numeros enteros aleatorios a generar: ");
            int cantidad = scanner.nextInt();

            if (cantidad < 1) {
                System.out.println("Error: La cantidad debe ser mayor a 0.");
                System.out.println();
                continue;
            }

            // Generar numeros aleatorios
            List<Integer> numerosAleatorios = generarNumerosAleatorios(cantidad);

            // Guardar numeros en archivo
            guardarNumerosEnArchivo(numerosAleatorios, NOMBRE_ARCHIVO);
            System.out.println("[OK] Numeros generados y guardados en: " + NOMBRE_ARCHIVO);

            // Leer numeros desde el archivo
            List<Integer> numerosLeidos = leerNumerosDesdeArchivo(NOMBRE_ARCHIVO);
            System.out.println("[OK] Numeros leidos desde el archivo: " + numerosLeidos.size() + " elementos");

            // Mostrar primeros 10 numeros originales (si hay al menos 10)
            System.out.println("\n--- NUMEROS ORIGINALES (primeros 10) ---");
            mostrarPrimerosElementos(numerosLeidos, 10);

            // Iniciar cronometro
            long tiempoInicio = System.currentTimeMillis();
            System.out.println("\nIniciando ordenamiento con Merge Sort...");

            // Ordenar usando Merge Sort
            List<Integer> numerosOrdenados = mergeSort(numerosLeidos);

            // Detener cronometro
            long tiempoFin = System.currentTimeMillis();
            long tiempoTranscurrido = tiempoFin - tiempoInicio;

            // Mostrar resultados
            System.out.println("[OK] Ordenamiento completado");
            System.out.println("\n--- NUMEROS ORDENADOS (primeros 10) ---");
            mostrarPrimerosElementos(numerosOrdenados, 10);

            System.out.println("\n==============================================================");
            System.out.println("  TIEMPO DE ORDENAMIENTO: " + tiempoTranscurrido + " milisegundos");
            System.out.println("==============================================================");

            // Mostrar todos los numeros ordenados
            System.out.println("\nDesea ver todos los numeros ordenados? (s/n): ");
            String respuesta = scanner.next();
            if (respuesta.equalsIgnoreCase("s")) {
                System.out.println("\n--- TODOS LOS NUMEROS ORDENADOS ---");
                mostrarTodosLosNumeros(numerosOrdenados);
            }

            // Preguntar si desea repetir
            System.out.println("\nDesea realizar otra prueba? (s/n): ");
            String respuestaRepetir = scanner.next();
            if (!respuestaRepetir.equalsIgnoreCase("s")) {
                repetir = false;
                System.out.println("\nGracias por usar el ordenador Merge Sort. Hasta luego!");
            }
            System.out.println();
        }

        scanner.close();
    }

    /**
     * Genera una lista de numeros enteros aleatorios
     * @param cantidad Cantidad de numeros a generar
     * @return Lista con numeros aleatorios entre 100 y 999
     */
    private static List<Integer> generarNumerosAleatorios(int cantidad) {
        List<Integer> numeros = new ArrayList<>();
        Random random = new Random();

        System.out.println("\nGenerando " + cantidad + " numeros aleatorios...");

        for (int i = 0; i < cantidad; i++) {
            int numeroAleatorio = random.nextInt(NUMERO_MAXIMO - NUMERO_MINIMO + 1) + NUMERO_MINIMO;
            numeros.add(numeroAleatorio);
        }

        return numeros;
    }

    /**
     * Guarda la lista de numeros en un archivo de texto
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
            System.err.println("Error al guardar numeros en el archivo: " + e.getMessage());
        }
    }

    /**
     * Lee numeros desde un archivo de texto y los almacena en una lista
     * @param nombreArchivo Nombre del archivo a leer
     * @return Lista con los numeros leidos del archivo
     */
    private static List<Integer> leerNumerosDesdeArchivo(String nombreArchivo) {
        List<Integer> numeros = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                numeros.add(Integer.parseInt(linea.trim()));
            }
        } catch (IOException e) {
            System.err.println("Error al leer numeros del archivo: " + e.getMessage());
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
        // Caso base: si la lista tiene 1 o menos elementos, ya esta ordenada
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
     * @param lista Lista de numeros
     * @param cantidad Cantidad de elementos a mostrar
     */
    private static void mostrarPrimerosElementos(List<Integer> lista, int cantidad) {
        int limite = Math.min(cantidad, lista.size());
        for (int i = 0; i < limite; i++) {
            System.out.print(lista.get(i) + " ");
        }
        if (lista.size() > cantidad) {
            System.out.print("... (y " + (lista.size() - cantidad) + " mas)");
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