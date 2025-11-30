package com.innoad.ordenamiento;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PruebasAutomaticas {

    private static final String ARCHIVO_RESULTADOS = "resultados_pruebas.txt";
    private static final int NUMERO_MINIMO = 100;
    private static final int NUMERO_MAXIMO = 999;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║   PRUEBAS AUTOMATICAS - MERGE SORT - EQUIPO INNOAD         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Cantidades de números a probar (mínimo 5 pruebas)
        int[] cantidadesPruebas = {1000, 5000, 10000, 25000, 50000};

        List<ResultadoPrueba> todosLosResultados = new ArrayList<>();

        // Realizar pruebas para cada cantidad
        for (int cantidad : cantidadesPruebas) {
            System.out.println("\n════════════════════════════════════════════════════════");
            System.out.println("║PRUEBA CON " + cantidad + " NÚMEROS");
            System.out.println("════════════════════════════════════════════════════════");

            // Primera ejecución
            long tiempoEjecucion1 = ejecutarPrueba(cantidad, 1);

            // Segunda ejecución con los mismos datos
            long tiempoEjecucion2 = ejecutarPrueba(cantidad, 2);

            // Guardar resultados
            ResultadoPrueba resultado = new ResultadoPrueba(
                    cantidad,
                    tiempoEjecucion1,
                    tiempoEjecucion2
            );
            todosLosResultados.add(resultado);

            // Mostrar resumen de esta prueba
            System.out.println("\n--- RESUMEN ---");
            System.out.println("Cantidad de números: " + cantidad);
            System.out.println("Tiempo Ejecución 1: " + tiempoEjecucion1 + " ms");
            System.out.println("Tiempo Ejecución 2: " + tiempoEjecucion2 + " ms");
            System.out.println("Promedio: " + resultado.obtenerPromedio() + " ms");
            System.out.println("Diferencia: " + resultado.obtenerDiferencia() + " ms");
        }

        // Generar reporte final
        generarReporte(todosLosResultados);

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  PRUEBAS COMPLETADAS - Resultados guardados en:            ║");
        System.out.println("║  " + ARCHIVO_RESULTADOS);
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Ejecuta una prueba de ordenamiento con una cantidad especIfica de nUmeros
     * @param cantidad Cantidad de números a generar y ordenar
     * @param numeroEjecucion NUmero de ejecucion (1 o 2)
     * @return Tiempo en milisegundos que tardo el ordenamiento
     */
    private static long ejecutarPrueba(int cantidad, int numeroEjecucion) {
        System.out.println("\n→ Ejecución " + numeroEjecucion + ":");

        // Generar números aleatorios
        List<Integer> numeros = generarNumerosAleatorios(cantidad);
        System.out.println("  ✓ Generados " + cantidad + " números aleatorios");

        // Iniciar cronómetro
        long tiempoInicio = System.currentTimeMillis();

        // Ordenar usando Merge Sort
        List<Integer> numerosOrdenados = mergeSort(numeros);

        // Detener cronómetro
        long tiempoFin = System.currentTimeMillis();
        long tiempoTranscurrido = tiempoFin - tiempoInicio;

        System.out.println("  ✓ Ordenamiento completado en: " + tiempoTranscurrido + " ms");

        // Verificar que el ordenamiento sea correcto
        if (verificarOrdenamiento(numerosOrdenados)) {
            System.out.println("  ✓ Verificación: Lista correctamente ordenada");
        } else {
            System.out.println("  ✗ ERROR: Lista NO está ordenada correctamente");
        }

        return tiempoTranscurrido;
    }

    /**
     * Genera una lista de numeros enteros aleatorios
     * @param cantidad Cantidad de números a generar
     * @return Lista con números aleatorios entre 100 y 999
     */
    private static List<Integer> generarNumerosAleatorios(int cantidad) {
        List<Integer> numeros = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < cantidad; i++) {
            int numeroAleatorio = random.nextInt(NUMERO_MAXIMO - NUMERO_MINIMO + 1) + NUMERO_MINIMO;
            numeros.add(numeroAleatorio);
        }

        return numeros;
    }

    /**
     * Implementacion del algoritmo Merge Sort
     * @param lista Lista de numeros a ordenar
     */
    private static List<Integer> mergeSort(List<Integer> lista) {
        if (lista.size() <= 1) {
            return lista;
        }

        int puntoMedio = lista.size() / 2;
        List<Integer> izquierda = new ArrayList<>();
        List<Integer> derecha = new ArrayList<>();

        for (int i = 0; i < puntoMedio; i++) {
            izquierda.add(lista.get(i));
        }

        for (int i = puntoMedio; i < lista.size(); i++) {
            derecha.add(lista.get(i));
        }

        izquierda = mergeSort(izquierda);
        derecha = mergeSort(derecha);

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

        while (indiceIzq < izquierda.size() && indiceDer < derecha.size()) {
            if (izquierda.get(indiceIzq) <= derecha.get(indiceDer)) {
                resultado.add(izquierda.get(indiceIzq));
                indiceIzq++;
            } else {
                resultado.add(derecha.get(indiceDer));
                indiceDer++;
            }
        }

        while (indiceIzq < izquierda.size()) {
            resultado.add(izquierda.get(indiceIzq));
            indiceIzq++;
        }

        while (indiceDer < derecha.size()) {
            resultado.add(derecha.get(indiceDer));
            indiceDer++;
        }

        return resultado;
    }

    /**
     * Verifica si una lista está correctamente ordenada
     * @param lista Lista a verificar
     * @return true si está ordenada, false en caso contrario
     */
    private static boolean verificarOrdenamiento(List<Integer> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            if (lista.get(i) > lista.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Genera un reporte con todos los resultados de las pruebas
     * @param resultados Lista de resultados de pruebas
     */
    private static void generarReporte(List<ResultadoPrueba> resultados) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO_RESULTADOS))) {
            escritor.write("═══════════════════════════════════════════════════════════════════\n");
            escritor.write("  REPORTE DE PRUEBAS - MERGE SORT - EQUIPO INNOAD\n");
            escritor.write("═══════════════════════════════════════════════════════════════════\n\n");

            escritor.write("TABLA DE RESULTADOS:\n");
            escritor.write("─────────────────────────────────────────────────────────────────\n");
            escritor.write(String.format("%-20s %-20s %-20s %-20s %-20s\n",
                    "Cantidad Números", "Resultado 1 (ms)", "Resultado 2 (ms)", "Promedio (ms)", "Diferencia (ms)"));
            escritor.write("─────────────────────────────────────────────────────────────────\n");

            for (ResultadoPrueba resultado : resultados) {
                escritor.write(String.format("%-20d %-20d %-20d %-20.2f %-20d\n",
                        resultado.cantidad,
                        resultado.tiempo1,
                        resultado.tiempo2,
                        resultado.obtenerPromedio(),
                        resultado.obtenerDiferencia()));
            }

            escritor.write("─────────────────────────────────────────────────────────────────\n\n");

            // Análisis de complejidad
            escritor.write("ANÁLISIS DE COMPLEJIDAD:\n");
            escritor.write("El algoritmo Merge Sort tiene una complejidad temporal de O(n log n)\n");
            escritor.write("en todos los casos (mejor, promedio y peor caso).\n\n");

            // Observaciones
            escritor.write("OBSERVACIONES:\n");
            for (ResultadoPrueba resultado : resultados) {
                double porcentajeDiferencia = (resultado.obtenerDiferencia() / (double)resultado.tiempo1) * 100;
                escritor.write("- Con " + resultado.cantidad + " números: ");
                escritor.write("Diferencia de " + resultado.obtenerDiferencia() + " ms ");
                escritor.write("(" + String.format("%.2f", porcentajeDiferencia) + "%)\n");
            }

            escritor.write("\n═══════════════════════════════════════════════════════════════════\n");

        } catch (IOException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    /**
     * Clase interna para almacenar resultados de una prueba
     */
    private static class ResultadoPrueba {
        int cantidad;
        long tiempo1;
        long tiempo2;

        public ResultadoPrueba(int cantidad, long tiempo1, long tiempo2) {
            this.cantidad = cantidad;
            this.tiempo1 = tiempo1;
            this.tiempo2 = tiempo2;
        }

        public double obtenerPromedio() {
            return (tiempo1 + tiempo2) / 2.0;
        }

        public long obtenerDiferencia() {
            return Math.abs(tiempo1 - tiempo2);
        }
    }
}