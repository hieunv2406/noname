package com.example.config;

import org.hibernate.cfg.Environment;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUltil {

    @Resource
    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public StringBuilder getSQLQueryFromFile(String filePath, String fileName) {
        StringBuilder sql = null;
        BufferedReader br = null;
        InputStreamReader input = null;
        try {
            if (filePath == null || filePath.equals("")) {
                return sql;
            }

            InputStream in = FileUltil.class.getClassLoader().getResourceAsStream(filePath);
            input = new InputStreamReader(in);
            br = new BufferedReader(input);
            String str;
            sql = new StringBuilder("");
            while ((str = br.readLine()) != null) {
                sql.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sql;
    }
}