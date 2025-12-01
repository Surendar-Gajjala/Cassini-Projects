package com.cassinisys.platform;

import com.cassinisys.platform.service.security.LicenseService;
import com.cassinisys.platform.util.AES;

import java.util.Scanner;

public class LicenseGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter customer id : ");
        String custId = scanner.nextLine();

        System.out.print("Enter expiration date (yyyy-MM-dd) : ");
        String expDate = scanner.nextLine();

        System.out.print("Enter number of licenses : ");
        String numOfLicense = scanner.nextLine();

        String json = String.format("{\"customer\": \"%s\", \"expiration\": \"%s\", \"licenses\": %d}", custId, expDate, Integer.parseInt(numOfLicense));
        System.out.println(json);

        String license = AES.encrypt(json, LicenseService.SECRET_KEY);
        System.out.println(license);
    }
}
