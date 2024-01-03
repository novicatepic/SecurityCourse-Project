package org.unibl.etf.sni.backend.waf;

public class SQLProblemKeywords {

    private static String[] sqlKeywords = {"DROP",
            "DELETE", "UPDATE",
            "INSERT", "TRUNCATE",
            "SELECT", "UNION", "ALTER",
            "MODIFY"};

    public static String[] returnSQLKeywords() {
        return sqlKeywords;
    }

}
