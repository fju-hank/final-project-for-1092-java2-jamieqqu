package com.fju;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * CSV檔分析
 */
public class Parse {


    public ArrayList<Map<String, String>> parse()  {
        final ArrayList<Map<String, String>> list;
        final CSVParser parser;
        final CSVFormat csvFormat = CSVFormat.DEFAULT;
        final Charset charset = Charset.forName("MS950");
        list = new ArrayList<Map<String, String>> ();
        try {
            File file = new File("C:\\Users\\a0989\\IdeaProjects\\final-project-for-1092-java2-jamieqqu\\src\\main\\java\\resource\\source.cvs");
            parser = CSVParser.parse(file, charset, csvFormat);
            for (CSVRecord record : parser ) {
                Iterator<String> it = record.iterator();
                while (it.hasNext()) {
                    Map<String, String> temp = new HashMap<String, String>();
                    temp.put("Name", it.next());
                    temp.put("PurchaseDate",it.next());
                    list.add(temp);
                }
            }
        }
        catch (IOException e) {
        }
        return list;

    }

    public static boolean exportToCSV(File file) throws ParseException {

        try {



            FileWriter csv = new FileWriter(file);
            csv.write("餐點名稱,過期日期,價格,後折扣金額");
            csv.write("\n");
            for(List<String> strList : OrderCache.order) {
                Food food = null;
                if("Hamburger".equals(strList.get(2))) {
                    food = new Hamburger();
                }
                else if ("Sub".equals(strList.get(2))) {
                    food = new Sub();
                }
                else if ("Sausage".equals(strList.get(2))) {
                    food = new Sausage();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(Tester.formatter.parse(strList.get(1)));
                cal.add(Calendar.HOUR_OF_DAY, food.getExpireHour());

                csv.write(strList.get(2) + ",");
                csv.write(Tester.formatter.format(cal.getTime()) + ",");
                csv.write(food.getPrice() + ",");
                csv.write(strList.get(3));
                csv.write("\n");
            }

            csv.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
