package no.iskra.ccalculator;

import no.iskra.ccalculator.exchangeApiException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.net.HttpURLConnection;
import java.lang.NullPointerException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CurrencyRates {
  public static JSONObject getCurrencyRates(String baseCurrency) throws exchangeApiException {
    JSONObject obj;

    try {
      String s = "https://api.exchangeratesapi.io/latest?base="+baseCurrency;
      //s = URLEncoder.encode(s, "UTF-8");
      URL url = new URL(s);
      Scanner scan = new Scanner(url.openStream());
      String str = new String();
      while (scan.hasNext()) {
        str += scan.nextLine();
      }
      scan.close();

      obj = new JSONObject(str);
      JSONObject currentRates = new JSONObject(obj.getJSONObject("rates"));
    } catch (NullPointerException e) {
      throw new exchangeApiException("API error", e);
    } catch (UnknownHostException e) {
      throw new exchangeApiException("Connection to API failed, missing network connection?", e);
    }
    catch(Exception e) {
      e.printStackTrace();
      return new JSONObject();
    }

    return obj;
  }

  public static String getHTML(String urlToRead) throws Exception {
      StringBuilder result = new StringBuilder();
      URL url = new URL(urlToRead);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = rd.readLine()) != null) {
         result.append(line);
      }
      rd.close();
      return result.toString();
  }
}
