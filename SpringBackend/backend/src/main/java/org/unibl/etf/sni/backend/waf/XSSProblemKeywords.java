package org.unibl.etf.sni.backend.waf;

public class XSSProblemKeywords {

    private static String[] xssPatterns = {"<script>", "javascript:", "alert(", "onerror=", "onload="};

    public static String[] returnXSSPatterns() {
        return xssPatterns;
    }

}
