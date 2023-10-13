package com.example.wcc;

import java.util.ArrayList;
import java.util.List;

public class RecursionReport {
    private String line;
    private int recursionComplexity;

    public RecursionReport(String line, int recursionComplexity) {
        this.line = line;
        this.recursionComplexity = recursionComplexity;
    }

    public String getLine() {
        return line;
    }

    public int getRecursionComplexity() {
        return recursionComplexity;
    }


}

