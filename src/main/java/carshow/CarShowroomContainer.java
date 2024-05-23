package carshow;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.Database;

import java.io.Serializable;
import java.util.*;

public class CarShowroomContainer {

    public CarShowroomContainer() {
        this.salons = new ArrayList<>();
    }
    public void loadSalons()
    {
       this.salons.addAll(DatabaseController.loadDB());
    }

    public ArrayList<CarShowroom> getSalons() {
        return salons;
    }

    private ArrayList<CarShowroom> salons;
    public void addCenter(CarShowroom showroom) throws ValueAlreadyExistException {
        if(showroom == null) return;
        for(CarShowroom carShowroom:salons)
        {
            if(Objects.equals(carShowroom.getNazwa_salonu(), showroom.getNazwa_salonu()))
                throw new ValueAlreadyExistException(carShowroom.getNazwa_salonu());
        }
        salons.add(showroom);
    }
    public void addCenter(String nazwa, int max)
    {
        if(Objects.equals(nazwa, "dowolny")) return;
        salons.add(new CarShowroom(nazwa,null,max));

    }
    public void buyCar(String nazwaSalonu, String marka, String model )
    {
        for(CarShowroom crs : this.salons ){
            if(Objects.equals(crs.getNazwa_salonu(), nazwaSalonu))
            {
                crs.getProduct(marka,model);

                return;
            }
        }
    }
    public ArrayList<Vehicle> getVehicles(String nazwa, String model)
    {

        if(Objects.equals(nazwa, "dowolny"))
        {
            ArrayList<Vehicle> toReturn = new ArrayList<>();
            for(CarShowroom csh : this.salons)
                for(Vehicle v : csh.getLista_samochodow())
                    if(model == null || v.getModel().contains(model))
                        toReturn.add(v);


            Collections.sort(toReturn);
            return toReturn;
        }
        else for(CarShowroom csh : this.salons)
                if(Objects.equals(nazwa, csh.getNazwa_salonu())) {
                    ArrayList<Vehicle> toReturn = new ArrayList<>();
                    for(Vehicle v : csh.getLista_samochodow())
                        if(model == null || v.getModel().contains(model))
                            toReturn.add(v);
                    Collections.sort(toReturn);
                    return toReturn;
                }
        return null;
    }
    public void removeCenter(String nazwa)
    {
        salons.removeIf(n->(Objects.equals(n.getNazwa_salonu(), nazwa)));

    }
    public ArrayList<String> getNames()
    {
        System.out.println( this.salons.size() );
        ArrayList<String> toReturn = new ArrayList<>(this.salons.size());
        for(CarShowroom crs : this.salons )
            toReturn.add(crs.getNazwa_salonu());
        toReturn.add("dowolny");

        return toReturn;
    }

    public void addProduct(String nazwa, Vehicle vh)
    {
        for(CarShowroom crs : this.salons ){
            if(Objects.equals(crs.getNazwa_salonu(), nazwa))
            {
                crs.addProduct(vh);

                return;
            }
        }

    }
    public List<CarShowroom> findEmpty()
    {
        List<CarShowroom> toReturn = new ArrayList<>();
        for (CarShowroom entry : this.salons)
            if(entry.getSize() == 0)
                toReturn.add(entry);

        return toReturn;
    }
    public void summary(){
        System.out.print("---------- Dostępne salony: \n");
        for (CarShowroom entry : this.salons)
            System.out.printf(Locale.US,">> Nazwa: %s; Zapełnienie: %.2f %% %n",entry.getNazwa_salonu(), (double)entry.getSize()/(double)entry.getMaxSize() * 100);
    }
}
