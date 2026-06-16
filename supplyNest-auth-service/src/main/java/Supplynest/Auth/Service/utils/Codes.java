package Supplynest.Auth.Service.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class Codes {

    public String generateBusinessCode(String businessName) {
        // 1️⃣ Extract meaningful acronym (max 3–4 letters)
        String acronym = Arrays.stream(businessName.trim().split("\\s+"))
                .filter(word -> word.length() > 2)      // skip small words like "of", "the"
                .limit(3)
                .map(word -> word.substring(0, 1).toUpperCase())
                .collect(Collectors.joining());

        if (acronym.isBlank()) {
            acronym = businessName.substring(0, Math.min(3, businessName.length())).toUpperCase();
        }

        // 2️⃣ SupplyNest essence
        String productCode = "SN";

        // 3️⃣ Random 3-digit number for uniqueness
        int random = ThreadLocalRandom.current().nextInt(100, 999);

        return acronym + "_" + productCode + "_" + random;
    }

public String generateBusinessGroupCode(String businessGroupName) {
        // 1️⃣ Extract meaningful acronym (max 3–4 letters)
        String acronym = Arrays.stream(businessGroupName.trim().split("\\s+"))
                .filter(word -> word.length() > 2)      // skip small words like "of", "the"
                .limit(3)
                .map(word -> word.substring(0, 1).toUpperCase())
                .collect(Collectors.joining());

        if (acronym.isBlank()) {
            acronym = businessGroupName.substring(0, Math.min(3, businessGroupName.length())).toUpperCase();
        }

        // 2️⃣ Business_Group essence
        String prefix = "BG";

        // 3️⃣ Random 3-digit number for uniqueness
        int random = ThreadLocalRandom.current().nextInt(100, 999);

        return prefix + "_" + acronym + "_" + random;
    }

}

