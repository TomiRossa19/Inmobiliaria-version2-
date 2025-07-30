package org.example;

import org.example.dto.*;
import org.example.enums.PropertyTypeEnum;
import org.example.services.Logica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logica logica = Logica.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar){
            mostrarMenu();
            
            int opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1 -> agregarPago();
                case 2 -> obtenerContratos();
                case 3 -> obtenerResumenDeContratosCompletados();
                case 4 -> obtenerListadoDeContratosNoCompletados();
                case 5 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private static void mostrarMenu(){
        System.out.println("------Gestión de Contratos y pagos------");
        System.out.println("1. Registrar un pago");
        System.out.println("2. Buscar contratos por inquilino");
        System.out.println("3. Obtener un resumen de contratos completados en un rango de fechas");
        System.out.println("4. Obtener un listado de contratos no completados con resumen financiero");
        System.out.println("5. Salir");
        System.out.println("Elija una opción");
    }

    private static int obtenerOpcion(){
        try {
            return Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            return -1;
        }
    }

    private static void agregarPago(){
        System.out.println("Ingrese el ID del contrato:");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.println("Ingrese el monto:");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        logica.addPayment(new AddPaymentParamsDTO(id, amount));
    }

    private static void obtenerContratos(){
        System.out.println("Ingrese el nombre del inquilino:");
        String name = scanner.nextLine();

        System.out.println("Ingrese el tipo de propiedad");
        String input1 = scanner.nextLine();
        PropertyTypeEnum propertyType = input1.isEmpty() ? null : PropertyTypeEnum.valueOf(input1);

        System.out.println("Ingrese la fecha inicial del rango de fechas:");
        String input2 = scanner.nextLine();
        LocalDate date1 = input2.isEmpty() ? null : LocalDate.parse(input2, dateTimeFormatter);

        System.out.println("Ingrese la fecha final del rango de fechas:");
        String input3 = scanner.nextLine();
        LocalDate date2 = input3.isEmpty() ? null : LocalDate.parse(input3, dateTimeFormatter);

        System.out.println("Ingrese el monto principal del rango de montos:");
        String input4 = scanner.nextLine();
        BigDecimal amount1 = input4.isEmpty() ? null : new BigDecimal(scanner.nextLine());

        System.out.println("Ingrese el monto final del rango de montos:");
        String input5 = scanner.nextLine();
        BigDecimal amount2 = input5.isEmpty() ? null : new BigDecimal(scanner.nextLine());

        List<RentalContractDTO> rentalContractDTOS = logica.getContracts(new GetContractsParamsDTO((PropertyTypeEnum) propertyType, date1, name, date2, amount1, amount2));
        mostrarContratos(rentalContractDTOS);
    }

    private static void mostrarContratos(List<RentalContractDTO> rentalContractDTOS){
        if (rentalContractDTOS.isEmpty()){
            System.out.println("No se encontraron contratos");
        }
        for (RentalContractDTO rentalContractDTO : rentalContractDTOS){
            System.out.println("Nombre del inquilino: " + rentalContractDTO.getTenantName());
            System.out.println("Tipo de propiedad: " + rentalContractDTO.getPropertyType());
            System.out.println("Renta mensual: " + rentalContractDTO.getMonthlyRent());
            System.out.println("Fecha de inicio: " + rentalContractDTO.getStartDate());
            System.out.println("Fecha de vencimiento: " + rentalContractDTO.getEndDate());
            System.out.println("Status: " + rentalContractDTO.getStatus());
        }
    }

    private static void obtenerResumenDeContratosCompletados(){
        System.out.println("Ingrese la fecha inicial del rango de fechas:");
        String input1 = scanner.nextLine();
        LocalDate date1 = input1.isEmpty() ? null : LocalDate.parse(input1, dateTimeFormatter);

        System.out.println("Ingrese la fecha final del rango de fechas:");
        String input2 = scanner.nextLine();
        LocalDate date2 = input2.isEmpty() ? null : LocalDate.parse(input2, dateTimeFormatter);

        List<GetCompletedContractsDTO> getCompletedContractsDTOS = logica.getCompletedContracts(new GetCompletedContractsParamsDTO(date1, date2));
        mostrarResumenDeContratosCompletados(getCompletedContractsDTOS);
    }

    private static void mostrarResumenDeContratosCompletados(List<GetCompletedContractsDTO> getCompletedContractsDTOS){
        if (getCompletedContractsDTOS.isEmpty()){
            System.out.println("No se encontraron contratos");
        }
        for (GetCompletedContractsDTO getCompletedContractsDTO : getCompletedContractsDTOS){
            System.out.println("Tipo de propiedad: " + getCompletedContractsDTO.getPropertyType());
            System.out.println("Cantidad de contratos : " + getCompletedContractsDTO.getContractsQuantity());
            System.out.println("Monto total: " + getCompletedContractsDTO.getTotalAmount());
        }
    }

    private static void obtenerListadoDeContratosNoCompletados(){
        List<GetNotFinishedContractsDTO> getNotFinishedContractsDTOS = logica.getNotFinishedContracts();
        mostrarListadoDeContratosNoCompletados(getNotFinishedContractsDTOS);
    }

    private static void mostrarListadoDeContratosNoCompletados(List<GetNotFinishedContractsDTO> getNotFinishedContractsDTOS){
        if (getNotFinishedContractsDTOS.isEmpty()){
            System.out.println("No se encontraron contratos");
        }
        for (GetNotFinishedContractsDTO getNotFinishedContractsDTO : getNotFinishedContractsDTOS){
            System.out.println("Monto esperado: " + getNotFinishedContractsDTO.getExpectedAmount());
            System.out.println("Monto pagado: " + getNotFinishedContractsDTO.getPayedAmount());
        }
    }
}