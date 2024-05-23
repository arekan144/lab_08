package carshow;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class SerializeCarShow {
    public static void main()
    {
        CarShowroom salon = new CarShowroom("Kraków",null,26);

        salon.addProduct( new Vehicle("TEST","tEGT", ItemCondition.NEW,
                199000,2014,2314,400, "Kraków"));
        salon.addProduct( new Vehicle("PEST","kESD", ItemCondition.NEW,
                199000,2013,634,700, "Kraków"));
        salon.addProduct( new Vehicle("TEST","lEST", ItemCondition.NEW,
                199000,2012,121,533, "Kraków"));
//        try {
//            SerializeCarShow.serializeSalon(salon);
//        }
//        catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
        

    }
    static void serializeSalon(CarShowroom carShowroom) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IllegalAccessException {
        CSVWriter writer = getCsvWriter(carShowroom);

        writer.writeNext(CarShowroom.getColNames());
        writer.writeNext(carShowroom.toTabString());

        writer.writeNext(Vehicle.tabColName());
        Set<Vehicle> cars = carShowroom.getLista_samochodow();
        for(Vehicle v:cars)
        {
            writer.writeNext(v.toTabString());
        }

        writer.close();
    }

    private static CSVWriter getCsvWriter(CarShowroom carShowroom) throws IOException {
        FileOutputStream fileOutputStream =  new FileOutputStream(carShowroom.getNazwa_salonu()+".csv");
        fileOutputStream.write(0xef);
        fileOutputStream.write(0xbb);
        fileOutputStream.write(0xbf);
        OutputStreamWriter out =  new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        CSVWriter writer = new CSVWriter(
                out,
                ';',
                '\0',
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );
        return writer;
    }

    public static Method findMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }
    static CarShowroom deseralizeSalon(String filename) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<List<String>> records = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line;
        line = br.readLine();
        String[] salon_colmns = line.split(";");

        line = br.readLine();
        String[] salon_strings = line.split(";");
        if(salon_strings.length < 2) throw new NegativeArraySizeException();
        line = br.readLine();
        String[] vehicle_colmns = line.split(";");

        Method[] methods = new Method[vehicle_colmns.length];
        for(int i =0; i < vehicle_colmns.length; i++)
        {
            //System.out.println("get"+StringUtils.capitalize(vehicle_colmns[i]));
            methods[i] = findMethod(Vehicle.class,"set"+StringUtils.capitalize(vehicle_colmns[i])+"S");

        }
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            vehicles.add(new Vehicle());
            //vehicles.getLast();
            //System.out.println(line);

            String[] values = line.split(";");
            for(int i = 0; i < vehicle_colmns.length; i++){
                //System.out.println(methods[i]);
                methods[i].invoke(vehicles.getLast(), values[i]);

            }
            vehicles.getLast().setStan(ItemCondition.NEW);
        }
        br.close();
        return new CarShowroom(salon_strings[0], new HashSet<>(vehicles),Integer.parseInt(salon_strings[1]));
    }
}
