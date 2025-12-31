package com.hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();
    public static ArrayList<Integer> randomGenerados = new ArrayList<Integer>();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Llistar reserves per tipus");
        System.out.println("5. Obtindre una reserva");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
       //TODO:
        switch(opcio){
            case 1: 
                reservarHabitacio();
                break;
            case 2:
                alliberarHabitacio();
                break;
            case 3:
                consultarDisponibilitat();
                break;
            case 4:
                //llistarReservesPerTipus(int[] codis, String tipus);
                break;
            case 5:
                obtindreReserva();
                break;
        }

    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        System.out.println("1. Estándar - " + capacitatInicial.get(TIPUS_ESTANDARD) + " disponibles - " + preusHabitacions.get(TIPUS_ESTANDARD) +"e" );
        System.out.println("2. Suite - " + capacitatInicial.get(TIPUS_SUITE) + " disponibles - " + preusHabitacions.get(TIPUS_SUITE) +"e" );
        System.out.println("3. Deluxe - " + capacitatInicial.get(TIPUS_DELUXE) + " disponibles - " + preusHabitacions.get(TIPUS_DELUXE) +"e" );
        
            String habitacionParaReserva = seleccionarTipusHabitacioDisponible();
            ArrayList<String> serviciosElegidos = seleccionarServeis();
            float precioTotal = calcularPreuTotal(habitacionParaReserva, serviciosElegidos);
            int numeroReserva = generarCodiReserva();
            System.out.println("Num Reserva: " + numeroReserva+ "; Habitación: " + habitacionParaReserva + "; Servicios: " + serviciosElegidos + "; Precio Total Reserva: " + precioTotal + "e");
            ArrayList<String> datosReserva = new ArrayList<String>();

            datosReserva.add(habitacionParaReserva);
            datosReserva.add(serviciosElegidos.toString());
            datosReserva.add(Float.toString(precioTotal));
            reserves.put(numeroReserva, datosReserva);
            System.out.println(reserves);
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio(int habitacion) {
        String tipoHabitacion = "";
        switch(habitacion){
            case 1: 
                tipoHabitacion = TIPUS_ESTANDARD;
                break;
            case 2:
                tipoHabitacion = TIPUS_SUITE;
                break;
            case 3:
                tipoHabitacion = TIPUS_DELUXE;
                break;
        }
        return tipoHabitacion;
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d'habitació disponibles:");
        
        System.out.println("1. Estándar - " + disponibilitatHabitacions.get(TIPUS_ESTANDARD) + " disponibles - " + preusHabitacions.get(TIPUS_ESTANDARD) +"e" );
        System.out.println("2. Suite - " + disponibilitatHabitacions.get(TIPUS_SUITE) + " disponibles - " + preusHabitacions.get(TIPUS_SUITE) +"e" );
        System.out.println("3. Deluxe - " + disponibilitatHabitacions.get(TIPUS_DELUXE) + " disponibles - " + preusHabitacions.get(TIPUS_DELUXE) +"e" );
        int habitacion = 0;
        int capacidad = 0;
        String tipoHabitacion = "";
        habitacion = llegirEnter("Seleccione una habitación: ");

        switch(habitacion){
            case 1: 
                capacidad = disponibilitatHabitacions.get(TIPUS_ESTANDARD);
                if(capacidad > 0 ){
                    capacidad = capacidad -1;
                    disponibilitatHabitacions.replace(TIPUS_ESTANDARD,capacidad);
                    tipoHabitacion = seleccionarTipusHabitacio(habitacion);
                } 
                break;
            case 2:
                capacidad = disponibilitatHabitacions.get(TIPUS_SUITE);
                if(capacidad > 0 ){
                    capacidad = capacidad -1;
                    disponibilitatHabitacions.replace(TIPUS_SUITE,capacidad);
                    tipoHabitacion = seleccionarTipusHabitacio(habitacion);
                } 
                break;
            case 3:
                capacidad = disponibilitatHabitacions.get(TIPUS_DELUXE);
                if(capacidad > 0 ){
                    capacidad = capacidad -1;
                    disponibilitatHabitacions.replace(TIPUS_DELUXE,capacidad);
                    tipoHabitacion = seleccionarTipusHabitacio(habitacion);
                }
                break;
        }
        
        if(tipoHabitacion != ""){
            return tipoHabitacion;
        }else{
            return null;
        }
        
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        
        ArrayList<String> servicios = new ArrayList<>();
        boolean esmorzarElegido = false;
        boolean gimnasElegido = false;
        boolean spaElegido = false;
        boolean piscinaElegido = false;
        System.out.println("0. Finalizr");
        System.out.println("1. Esmorzar - " + preusServeis.get(SERVEI_ESMORZAR) + "e" );
        System.out.println("2. Gimnas - " + preusServeis.get(SERVEI_GIMNAS) + "e" );
        System.out.println("3. Spa - " + preusServeis.get(SERVEI_SPA) + "e" );
        System.out.println("4. Piscina - " + preusServeis.get(SERVEI_PISCINA) + "e" );

        int opcio = 0;
        do {
            opcio = llegirEnter("Seleccione un servicio: ");
            switch(opcio){
                case 0:
                    break;
                case 1:
                    if(esmorzarElegido == false){
                        servicios.add(SERVEI_ESMORZAR);
                        esmorzarElegido = true;
                    }else{
                        System.out.println(SERVEI_ESMORZAR + " ya elegido");
                    }
                    break;
                case 2:
                    if(gimnasElegido == false){
                        servicios.add(SERVEI_GIMNAS);
                        gimnasElegido = true;
                    }else {
                        System.out.println(SERVEI_GIMNAS + " ya elegido");
                    }
                    break;
                case 3:
                    if(spaElegido == false){
                        servicios.add(SERVEI_SPA);
                        spaElegido = true;
                    }else {
                        System.out.println(SERVEI_SPA + " ya elegido");
                    }
                    break;
                case 4:
                    if(piscinaElegido == false){
                        servicios.add(SERVEI_PISCINA);
                        piscinaElegido = true;
                    }else {
                        System.out.println(SERVEI_PISCINA + " ya elegida");
                    }
                    break;

            }
        } while (opcio != 0);

        return servicios;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        
        float precioHabitacion = 0;
        float precioServicios = 0;
        float precioTotalReserva = 0;
        if(tipusHabitacio.equals(TIPUS_ESTANDARD))
          precioHabitacion = preusHabitacions.get(TIPUS_ESTANDARD);

        if(tipusHabitacio.equals(TIPUS_SUITE))
          precioHabitacion = preusHabitacions.get(TIPUS_SUITE);

        if(tipusHabitacio.equals(TIPUS_DELUXE))
          precioHabitacion = preusHabitacions.get(TIPUS_DELUXE);

        precioHabitacion = precioHabitacion;
        System.out.println("Precio de la habitación: " + precioHabitacion +"e");

        for(int i=0; i < serveisSeleccionats.size(); i++){
            if(serveisSeleccionats.get(i).equals(SERVEI_ESMORZAR)){
                precioServicios = precioServicios + (preusServeis.get(SERVEI_ESMORZAR));
                System.out.println("Precio Servicio " + SERVEI_ESMORZAR + "(" + preusServeis.get(SERVEI_ESMORZAR)+"e)");
            }
            if(serveisSeleccionats.get(i).equals(SERVEI_GIMNAS)){
                precioServicios = precioServicios + (preusServeis.get(SERVEI_GIMNAS));
                System.out.println("Precio Servicio " + SERVEI_GIMNAS + "(" + preusServeis.get(SERVEI_GIMNAS)+"e)");
            }
            if(serveisSeleccionats.get(i).equals(SERVEI_SPA)){
                precioServicios = precioServicios + (preusServeis.get(SERVEI_SPA));
                System.out.println("Precio Servicio " + SERVEI_SPA + "(" + preusServeis.get(SERVEI_SPA)+"e)");
            }
            if(serveisSeleccionats.get(i).equals(SERVEI_PISCINA)){
                precioServicios = precioServicios + (preusServeis.get(SERVEI_PISCINA));
                System.out.println("Precio Servicio " + SERVEI_PISCINA + "(" + preusServeis.get(SERVEI_PISCINA)+"e)");
            }
                
        }

        precioTotalReserva = precioHabitacion + precioServicios;
        System.out.println("Subtotal: (" + precioTotalReserva +"e)");
        System.out.println("IVA (21%):" + (precioTotalReserva * IVA) +"e");
        precioTotalReserva = precioTotalReserva + (precioTotalReserva * IVA);
        System.out.println("TOTAL:" + precioTotalReserva+ "e");

        return precioTotalReserva;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        int numeroReserva = random.nextInt(900)+100;
        
        for(int i=0; i < randomGenerados.size(); i++){
            if(randomGenerados.contains(numeroReserva)){
                numeroReserva = random.nextInt(900)+100;
            }else{
                randomGenerados.add(numeroReserva);
                break;
            }
        }

        if(randomGenerados.isEmpty()){
            randomGenerados.add(numeroReserva);
        }

        return numeroReserva;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
         // TODO: Demanar codi, tornar habitació i eliminar reserva

         ArrayList<String> datosReserva = new ArrayList<String>();
         int codigoReserva = llegirEnter("Seleccione una reserva a eliminar: ");
         for(int i = 0; i < reserves.size(); i++){
            if(reserves.containsKey(codigoReserva)){
                datosReserva = reserves.get(codigoReserva);
                    if(datosReserva.get(0).equals(TIPUS_ESTANDARD)){
                        int capacidad = disponibilitatHabitacions.get(TIPUS_ESTANDARD) +1;
                        disponibilitatHabitacions.replace(TIPUS_ESTANDARD,capacidad);
                    }
                    if(datosReserva.get(0).equals(TIPUS_SUITE)){
                        int capacidad = disponibilitatHabitacions.get(TIPUS_SUITE) +1;
                        disponibilitatHabitacions.replace(TIPUS_SUITE,capacidad);
                    }
                    if(datosReserva.get(0).equals(TIPUS_DELUXE)){
                        int capacidad = disponibilitatHabitacions.get(TIPUS_DELUXE) +1;
                        disponibilitatHabitacions.replace(TIPUS_DELUXE,capacidad);
                    }
                reserves.remove(codigoReserva);
                randomGenerados.remove(Integer.valueOf(codigoReserva));
                break;
            }else{
                System.out.println("Num Reserva introducido no encontrado");
                codigoReserva = llegirEnter("Seleccione una reserva a eliminar: ");
            }
         }

    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        // TODO: Mostrar lliures i ocupades
        int ocupadasEstandard = 0;
        int ocupadasSuite = 0;
        int ocupadasDeluxe = 0;

        ocupadasEstandard = CAPACITAT_ESTANDARD - disponibilitatHabitacions.get(TIPUS_ESTANDARD);
        ocupadasSuite = CAPACITAT_SUITE - disponibilitatHabitacions.get(TIPUS_SUITE);
        ocupadasDeluxe = CAPACITAT_DELUXE - disponibilitatHabitacions.get(TIPUS_DELUXE);

        System.out.println("===== DISPONIBILIDAD DE HABITACIONES =======");
        System.out.println("| TIPO      | DISPONIBLES | OCUPADAS |");
        System.out.println("| "+ TIPUS_ESTANDARD +" | "+ disponibilitatHabitacions.get(TIPUS_ESTANDARD) + "          | "+ ocupadasEstandard + "       |");
        System.out.println("| "+ TIPUS_SUITE +"     | "+ disponibilitatHabitacions.get(TIPUS_SUITE) + "          | "+ ocupadasSuite + "       |");
        System.out.println("| "+ TIPUS_DELUXE +"    | "+ disponibilitatHabitacions.get(TIPUS_DELUXE) + "          | "+ ocupadasDeluxe + "       |");


    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
         // TODO: Implementar recursivitat
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        // TODO: Mostrar dades d'una reserva concreta
        int codigoReserva = llegirEnter("Indique Num Reserva ha mostrar: ");
        mostrarDadesReserva(codigoReserva);

 
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        // TODO: Llistar reserves per tipus
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
       // TODO: Imprimir tota la informació d'una reserva
        System.out.println(reserves.get(codi));
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
                System.out.print(missatge);
                valor = sc.nextInt();
                correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
