package com.example.wcc;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ArrayDeclarationCalculator {

    @PostMapping("/calculate-array-declaration-complexity")
    public int calculateArrayDeclarationComplexity(@RequestBody String code) {


        String[] lines = code.split("\n");
        int totalComplexity = 0;

        for (String line : lines) {
            // Remove curly braces and brackets
            String cleanedLine = line.replaceAll("[{}]", "").replaceAll("\\b(public|private|protected)\\b", "").trim();

            // Remove single-line and multi-line comments
            cleanedLine = cleanedLine.replaceAll("//.*|/\\*(.|[\\r\\n])*?\\*/", "");

            // Calculate Sj: size of the executable statement (count of tokens)
//            int sj = cleanedLine.split("\\s+").length;

            // Calculate Wt: total weight of the executable statement
            int wt = 1;

            int ca = 0;

            if(cleanedLine.contains("=") && cleanedLine.contains("new")){
                int ndValue = calculateNdValue(cleanedLine);

                int DdSum = 0;

                // Extract and process array sizes for multi-dimensional arrays
                int[] sizes = getArraySizes(cleanedLine);
                for (int size : sizes) {
                    DdSum += size;
                }

//                System.out.println("Sj values = " + sj);
                System.out.println("Nd Value = " + ndValue);
                System.out.println("Dd Sum = " + DdSum);

                ca += ndValue * DdSum;
            }


//            // Check if the line contains array declarations and calculate Nd value
//            int ndValue = calculateNdValue(cleanedLine);
//
//            int DdSum = 0;
//
//            // Extract and process array sizes for multi-dimensional arrays
//            int[] sizes = getArraySizes(cleanedLine);
//            for (int size : sizes) {
//                DdSum += size;
//            }
//
//            System.out.println("Sj values = " + sj);
//            System.out.println("Nd Value = " + ndValue);
//            System.out.println("Dd Sum = " + DdSum);
//
//            totalComplexity += (sj + (ndValue*DdSum));

            totalComplexity += ( ca);
        }
        return totalComplexity;


    }

    //    1.	Number of Dimensions (Nd): The number of dimensions in the array. In this case, it's a one-dimensional array, so Nd = 1.
//    it's a two-dimensional array, 2.
    public int calculateNdValue(String cleanedLine) {
        int ndValue = 0;

        // Count the occurrences of "[]" in the cleanedLine
        int countBrackets = cleanedLine.split("\\[]").length - 1;

        System.out.println("Count Brackets: " + countBrackets);

        if(countBrackets == 1){
            ndValue = 1;
        }else if(countBrackets == 2){
            ndValue = 2;
        } else if (countBrackets == 3) {
            ndValue = 3;
        }

        return ndValue;
    }

    //    The size of each dimension in the array. For a one-dimensional array, the size is simply the number of elements specified within the brackets. In this example, Dd = 5.
    public int[] getArraySizes(String cleanedLine) {
        // Extract and parse array sizes from the cleaned line
        // Assuming the sizes are specified as integers within square brackets []
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
        Matcher matcher = pattern.matcher(cleanedLine);
        int[] sizes = new int[3]; // Assuming a maximum of two dimensions

        int dimension = 0;

        while (matcher.find() && dimension < 3) {
            sizes[dimension] = Integer.parseInt(matcher.group(1));
            dimension++;
        }

        System.out.println("Size length: " + sizes[0]);


        return sizes;
    }

}
