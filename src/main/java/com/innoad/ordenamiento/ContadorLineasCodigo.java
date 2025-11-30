package com.innoad.ordenamiento;

import java.io.*;
        import java.nio.file.*;

public class ContadorLineasCodigo {

    private static int totalLineas = 0;
    private static int lineasCodigo = 0;
    private static int lineasComentarios = 0;
    private static int lineasBlanco = 0;

    // Constantes COCOMO para modo Orgánico
    private static final double A_ORGANICO = 2.4;
    private static final double B_ORGANICO = 1.05;
    private static final double C_ORGANICO = 2.5;
    private static final double D_ORGANICO = 0.38;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║   CONTADOR DE LINEAS DE CODIGO Y COCOMO - INNOAD          ");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Directorio actual del proyecto
        String directorioActual = System.getProperty("user.dir");
        System.out.println("Analizando directorio: " + directorioActual);
        System.out.println();

        // Contar líneas en todos los archivos .java del directorio
        contarLineasEnDirectorio(directorioActual);

        // Mostrar resultados del conteo
        mostrarEstadisticas();

        // Calcular COCOMO
        calcularCOCOMO();
    }

    /**
     * Con este contaria las lineas de codigo en todos los archivos .java de un directorio
     * @param rutaDirectorio
     */
    private static void contarLineasEnDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        File[] archivos = directorio.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().endsWith(".java")) {
                    System.out.println("Analizando: " + archivo.getName());
                    contarLineasArchivo(archivo);
                } else if (archivo.isDirectory()) {
                    // Recursivamente analizar subdirectorios
                    contarLineasEnDirectorio(archivo.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Cuenta las lineas de un archivo Java especifico
     * @param archivo Archivo a analizar
     */
    private static void contarLineasArchivo(File archivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean enComentarioMultilinea = false;

            while ((linea = lector.readLine()) != null) {
                totalLineas++;
                String lineaLimpia = linea.trim();

                // Línea en blanco
                if (lineaLimpia.isEmpty()) {
                    lineasBlanco++;
                    continue;
                }

                // Detectar inicio de comentario multilinea
                if (lineaLimpia.startsWith("/*")) {
                    enComentarioMultilinea = true;
                    lineasComentarios++;
                    if (lineaLimpia.contains("*/")) {
                        enComentarioMultilinea = false;
                    }
                    continue;
                }

                // Detectar fin de comentario multilinea
                if (enComentarioMultilinea) {
                    lineasComentarios++;
                    if (lineaLimpia.contains("*/")) {
                        enComentarioMultilinea = false;
                    }
                    continue;
                }

                // Comentario de una línea
                if (lineaLimpia.startsWith("//")) {
                    lineasComentarios++;
                    continue;
                }

                // Línea de código
                lineasCodigo++;
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo " + archivo.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Muestra estadisticas del conteo de lineas
     */
    private static void mostrarEstadisticas() {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("ESTADISTICAS DE LINEAS DE CODIGO");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("Total de líneas:        " + totalLineas);
        System.out.println("Líneas de código:       " + lineasCodigo);
        System.out.println("Líneas de comentarios:  " + lineasComentarios);
        System.out.println("Líneas en blanco:       " + lineasBlanco);
        System.out.println("════════════════════════════════════════════════════════════");

        double porcentajeCodigo = (lineasCodigo * 100.0) / totalLineas;
        double porcentajeComentarios = (lineasComentarios * 100.0) / totalLineas;

        System.out.println("\nPorcentaje de código:      " + String.format("%.2f", porcentajeCodigo) + "%");
        System.out.println("Porcentaje de comentarios: " + String.format("%.2f", porcentajeComentarios) + "%");
    }

    /**
     * Calcula las estimaciones usando el modelo COCOMO Orgánico
     */
    private static void calcularCOCOMO() {
        // Según el documento, multiplicar por 100
        double kloc = (lineasCodigo * 100.0) / 1000.0;

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║   ESTIMACION DE COSTOS CON COCOMO ORGANICO                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("DATOS DE ENTRADA:");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Lineas de codigo reales:  " + lineasCodigo + " LOC");
        System.out.println("Multiplicado por 100:     " + (lineasCodigo * 100) + " LOC");
        System.out.println("KLOC (miles de lineas):   " + String.format("%.2f", kloc) + " KLOC");
        System.out.println();

        // Calcular Esfuerzo: E = a * (KLOC)^b
        double esfuerzo = A_ORGANICO * Math.pow(kloc, B_ORGANICO);

        // Calcular Tiempo de desarrollo: Tdev = c * (E)^d
        double tiempoDesarrollo = C_ORGANICO * Math.pow(esfuerzo, D_ORGANICO);

        // Calcular Personas necesarias: P = E / Tdev
        double personasNecesarias = esfuerzo / tiempoDesarrollo;

        // Calcular Productividad: Pr = LOC / E
        double productividad = (lineasCodigo * 100.0) / esfuerzo;

        System.out.println("FÓRMULAS COCOMO ORGÁNICO:");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("E = a · (KLOC)^b");
        System.out.println("  donde a=" + A_ORGANICO + ", b=" + B_ORGANICO);
        System.out.println();
        System.out.println("Tdev = c · (E)^d");
        System.out.println("  donde c=" + C_ORGANICO + ", d=" + D_ORGANICO);
        System.out.println();
        System.out.println("P = E / Tdev");
        System.out.println();

        System.out.println("RESULTADOS:");
        System.out.println("═════════════════════════════════════════════════════════════");
        System.out.println("Esfuerzo (E):              " + String.format("%.2f", esfuerzo) + " personas/mes");
        System.out.println("Tiempo de desarrollo:      " + String.format("%.2f", tiempoDesarrollo) + " meses");
        System.out.println("Personas necesarias:       " + String.format("%.2f", personasNecesarias) + " ≈ " + Math.round(personasNecesarias) + " personas");
        System.out.println("Productividad:             " + String.format("%.2f", productividad) + " LOC/persona·mes");
        System.out.println("═════════════════════════════════════════════════════════════");
        System.out.println();

        // Calcular costos (usando salario promedio de programador en Colombia 2025)
        double salarioMensual = 2000000; // $2,000,000 COP según el documento
        int personasRedondeadas = (int) Math.ceil(personasNecesarias);
        double costoTotal = salarioMensual * personasRedondeadas * tiempoDesarrollo;

        System.out.println("ESTIMACIÓN DE COSTOS:");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Salario mensual por persona:  $" + String.format("%,.0f", salarioMensual) + " COP");
        System.out.println("Personas en el equipo:        " + personasRedondeadas + " personas");
        System.out.println("Duración del proyecto:        " + String.format("%.2f", tiempoDesarrollo) + " meses");
        System.out.println("───────────────────────────────────────────────────────────── ");
        System.out.println("COSTO TOTAL DEL PROYECTO:     $" + String.format("%,.0f", costoTotal) + " COP");
        System.out.println("═════════════════════════════════════════════════════════════");
        System.out.println();

        // Escenario alternativo: terminar en menos tiempo
        System.out.println("ESCENARIO ALTERNATIVO - Terminar en tiempo reducido:");
        System.out.println("─────────────────────────────────────────────────────────────");
        double tiempoDeseado = Math.ceil(tiempoDesarrollo / 2); // La mitad del tiempo
        double personasParaTiempoReducido = esfuerzo / tiempoDeseado;
        double costoReducido = salarioMensual * Math.ceil(personasParaTiempoReducido) * tiempoDeseado;

        System.out.println("Si se desea terminar en:      " + String.format("%.0f", tiempoDeseado) + " meses");
        System.out.println("Se necesitarían:              " + String.format("%.2f", personasParaTiempoReducido) + " ≈ " + (int)Math.ceil(personasParaTiempoReducido) + " personas");
        System.out.println("Costo en este escenario:      $" + String.format("%,.0f", costoReducido) + " COP");
        System.out.println("═════════════════════════════════════════════════════════════");

        // Guardar resultados en archivo
        guardarResultadosCOCOMO(kloc, esfuerzo, tiempoDesarrollo, personasNecesarias,
                productividad, costoTotal, tiempoDeseado,
                personasParaTiempoReducido, costoReducido);
    }

    /**
     * Guarda los resultados de COCOMO en un archivo de texto
     */
    private static void guardarResultadosCOCOMO(double kloc, double esfuerzo, double tiempoDesarrollo,
                                                double personasNecesarias, double productividad,
                                                double costoTotal, double tiempoReducido,
                                                double personasReducido, double costoReducido) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("resultados_cocomo.txt"))) {
            escritor.write("═══════════════════════════════════════════════════════════════════\n");
            escritor.write("  RESULTADOS COCOMO - MODO ORGANICO - EQUIPO INNOAD\n");
            escritor.write("═══════════════════════════════════════════════════════════════════\n\n");

            escritor.write("LÍNEAS DE CÓDIGO:\n");
            escritor.write("- LOC reales:               " + lineasCodigo + "\n");
            escritor.write("- LOC × 100:                " + (lineasCodigo * 100) + "\n");
            escritor.write("- KLOC:                     " + String.format("%.2f", kloc) + "\n\n");

            escritor.write("ESTIMACIONES:\n");
            escritor.write("- Esfuerzo:                 " + String.format("%.2f", esfuerzo) + " personas/mes\n");
            escritor.write("- Tiempo desarrollo:        " + String.format("%.2f", tiempoDesarrollo) + " meses\n");
            escritor.write("- Personas necesarias:      " + String.format("%.2f", personasNecesarias) + " ≈ " + Math.round(personasNecesarias) + " personas\n");
            escritor.write("- Productividad:            " + String.format("%.2f", productividad) + " LOC/persona·mes\n\n");

            escritor.write("COSTOS:\n");
            escritor.write("- Salario mensual:          $2,000,000 COP\n");
            escritor.write("- Costo total proyecto:     $" + String.format("%,.0f", costoTotal) + " COP\n\n");

            escritor.write("ESCENARIO ALTERNATIVO (" + String.format("%.0f", tiempoReducido) + " meses):\n");
            escritor.write("- Personas necesarias:      " + (int)Math.ceil(personasReducido) + " personas\n");
            escritor.write("- Costo estimado:           $" + String.format("%,.0f", costoReducido) + " COP\n\n");

            escritor.write("═══════════════════════════════════════════════════════════════════\n");

            System.out.println("\n✓ Resultados guardados en: resultados_cocomo.txt");

        } catch (IOException e) {
            System.err.println("Error al guardar resultados COCOMO: " + e.getMessage());
        }
    }
}