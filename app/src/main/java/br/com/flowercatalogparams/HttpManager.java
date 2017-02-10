package br.com.flowercatalogparams;

import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static String getData(RequestPackage p) {

        BufferedReader reader = null;
        String uri = p.getUri();

        //Só faz append se o metodo for get
        if("GET".equals(p.getMethod())){
            uri += "?" + p.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());//GET, POST, ETC

            JSONObject json = new JSONObject(p.getParams());//Parseando o map de params para formato json
            String params = "params=" + json.toString(); //Server espera um package "params"


            if("POST".equals(p.getMethod())){
                con.setDoOutput(true); //Output contents to the body of the request
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream()); //Writer object que escreve no outputstream
                writer.write(p.getEncodedParams());
                writer.write(params);//Em caso de JSON
                writer.flush();
            }


            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

}
