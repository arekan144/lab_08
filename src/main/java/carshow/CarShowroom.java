package carshow;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "CarShowroom")
public class CarShowroom implements Serializable {
    @Id
    @GeneratedValue
    @Column(name= "id_salonu")
    private long id_salonu;

    @Column(name = "nazwa_salonu")
    private String nazwa_salonu;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "id_salonu",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Vehicle> lista_samochodow = new HashSet<>();

    private Integer maks_pojem_salonu;

    public CarShowroom()
    {

    }
    public CarShowroom(String nazwa_salonu, Set<Vehicle> lista_samochodow, int maks_pojem_salonu) {
        this.nazwa_salonu = nazwa_salonu;
        this.lista_samochodow = Objects.requireNonNullElseGet(lista_samochodow, HashSet::new);
        this.maks_pojem_salonu = maks_pojem_salonu;
    }

    static public String[] getColNames()
    {
        return new String[]{"nazwa_salonu", "maks_ilosc"};
    }
    public String[] toTabString()
    {
        return new String[]{nazwa_salonu,maks_pojem_salonu.toString()};
    }
    public Set<Vehicle> getLista_samochodow() {
        return lista_samochodow;
    }

    public void addProduct(Vehicle vh)
    {
        if(this.maks_pojem_salonu == this.lista_samochodow.size())
        {
            System.err.println("Osiągnięto limit samochodów.");
            return;
        }
        for (Vehicle vehicle : this.lista_samochodow) {
            if (Objects.equals(vehicle.getModel(), vh.getModel()) && Objects.equals(vehicle.getMarka(), vh.getMarka())) {
                vehicle.inc();
                return;
            }
        }
        this.lista_samochodow.add(vh);
    }
    public void getProduct(String marka, String model)
    {
        for(Vehicle vehicle : this.lista_samochodow)
        {
            if(Objects.equals(vehicle.getMarka(), marka) && Objects.equals(vehicle.getModel(), model)){
                vehicle.dec();
                System.out.println("Sprzedano!");
//                if(vehicle.getIlosc() == 0)
//                {
//                    System.out.println("Ostatni!");
//                    this.lista_samochodow.remove(vehicle);
//                    return;
//                }
                return;
            }
        }
        System.err.println("Nie ma takiego pojazdu!");
    }
    public int removeProduct(Vehicle vh)
    {
        for(Vehicle vehicle : this.lista_samochodow)
        {
            if(vehicle.compareTo(vh) == 0){
                this.lista_samochodow.remove(vehicle);
                return 0;
            }
        }

        return -1;
    }
    public Vehicle search(String nazwa)
    {
        for (Vehicle vehicle : this.lista_samochodow)
            if (Objects.equals(vehicle.getModel(), nazwa))
                return vehicle;
        return null;
    }
    public List<Vehicle> searchPartial(String cz_nazwa){
        List<Vehicle> toreturn = new ArrayList<Vehicle>();
        for (Vehicle vehicle : this.lista_samochodow)
            if (vehicle.getModel().contains(cz_nazwa))
                toreturn.add(vehicle);

        return toreturn;
    }
    public int countByCondition(ItemCondition itc)
    {
        int toreturn = 0;
        for (Vehicle vehicle : this.lista_samochodow)
            if(vehicle.getStan() == itc)
                toreturn++;
        return toreturn;
    }
    public void summary()
    {
        System.out.printf(Locale.US,
                "Nazwa salonu: %s; Pojemność salomu: %d; %n",
                this.nazwa_salonu, this.maks_pojem_salonu);
        for (Vehicle vehicle : this.lista_samochodow) {
            System.out.println("-----------------");
            vehicle.print();
        }
    }

    public List<Vehicle> sortByName()
    {
        List<Vehicle> toreturn = new ArrayList<>(this.lista_samochodow);
        toreturn.sort(Comparator.comparing(Vehicle::getMarka));
        return toreturn;
    }
    public int getSize()
    {
        return this.lista_samochodow.size();
    }
    public String getNazwa_salonu() { return this.nazwa_salonu; }
    public int getMaxSize()
    {
        return this.maks_pojem_salonu;
    }
    public List<Vehicle> sortByAmount()
    {
        List<Vehicle> toreturn = new ArrayList<>(this.lista_samochodow);
        toreturn.sort((v1,v2)-> v2.getIlosc() - v1.getIlosc());
        return toreturn;
    }
    public Vehicle max()
    {
        return Collections.max(this.lista_samochodow, Comparator.comparingInt(Vehicle::getIlosc));
    }
}
