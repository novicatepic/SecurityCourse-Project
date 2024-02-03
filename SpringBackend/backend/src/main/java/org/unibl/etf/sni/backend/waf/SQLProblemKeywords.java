package org.unibl.etf.sni.backend.waf;

public class SQLProblemKeywords {

    private static String[] sqlKeywords = {"DROP",
            "DELETE", "UPDATE",
            "INSERT", "TRUNCATE",
            "SELECT", "UNION", "ALTER",
            "MODIFY", "#", "` OR 1=1", "--", "SHUTDOWN"};

    public static String[] returnSQLKeywords() {
        return sqlKeywords;
    }

}
