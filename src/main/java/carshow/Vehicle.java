package carshow;
import jakarta.persistence.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "Vehicle",schema = "carshows")
public class Vehicle implements Comparable<Vehicle>, Serializable {
    @Id
    @GeneratedValue
    int id_s;
    private String marka;
    private String model;
    private Double cena;
    private Integer rok_produkcji;
    private Double przebieg;
    private Double pojemnosc_silnika;
    @Transient
    private String nazwa_salonu;
    private Integer ilosc;

    @ManyToOne
    @JoinColumn(name="id_salonu",nullable=false)

    private CarShowroom id_salonu;

    @Transient
    private transient ItemCondition stan;

    public Vehicle(){

    }
    public Vehicle(String marka, String model,
                   ItemCondition stan, double cena,
                   int rok_produkcji, double przebieg,
                   double pojemnosc_silnika, String nazwa_salonuk) {
        this.marka = marka;
        this.model = model;
        this.stan = stan;
        this.cena = cena;
        this.rok_produkcji = rok_produkcji;
        this.przebieg = przebieg;
        this.pojemnosc_silnika = pojemnosc_silnika;
        this.ilosc = 1;
        this.nazwa_salonu = nazwa_salonuk;

    }
    static String[] tabColName()
    {
        List<Field> allFields = Arrays.asList(Vehicle.class.getDeclaredFields());
        String[] toRet = new String[allFields.size()-2];
        int i = 0;
        for(Field field: allFields)
            if(!field.getName().equals("stan") && !field.getName().equals("nazwa_salonu"))
                toRet[i++] = field.getName();
        return toRet;
    }
    public void setIloscS(String ilosc) {
        this.ilosc = Integer.parseInt(ilosc);
    }
    public String[] toTabString() throws IllegalAccessException {
        List<Field> allFields = Arrays.asList(Vehicle.class.getDeclaredFields());
        String[] toRet = new String[allFields.size()-2];
        int i = 0;

        for(Field field: allFields)
            if(!field.getName().equals("stan") && !field.getName().equals("nazwa_salonu")){
                field.setAccessible(true);
                toRet[i++] = field.get(this).toString();
            }
        return toRet;
    }
    public void setMarkaS(String marka) {
        this.marka = marka;
    }

    public void setModelS(String model) {
        this.model = model;
    }

    public void setStan(ItemCondition stan) {
        this.stan = stan;
    }

    public void setCenaS(String cena) {
        this.cena = Double.parseDouble(cena);
    }

    public void setRok_produkcjiS(String rok_produkcji) {
        this.rok_produkcji = Integer.parseInt(rok_produkcji);
    }

    public void setPrzebiegS(String przebieg) {
        this.przebieg = Double.parseDouble(przebieg);
    }

    public void setPojemnosc_silnikaS(String pojemnosc_silnika) {
        this.pojemnosc_silnika = Double.parseDouble(pojemnosc_silnika);
    }
    public void setNazwa_salonuk(String nazwa_salonu) {
        this.nazwa_salonu = nazwa_salonu;
    }
    @Override
    public String toString()
    {
        return STR." Marka: \{marka}, Model \{model}, Stan: \{stan}, Cena \{cena}, Rok produkcji \{rok_produkcji}, Przebieg \{przebieg}, Pojemność silnika \{pojemnosc_silnika}";
    }
    public void print()
    {
        System.out.printf(Locale.US,
                "Marka: %s; Model: %s; %n" +
                        "Stan: %s; Cena %.2f; %n" +
                        "Rok produkcji: %d; Przebieg %.2f; %n" +
                        "Pojemność silnika: %.2f; Dostępna ilość: %d %n" ,
                this.marka,
                this.model,
                this.stan.toString(),
                this.cena,
                this.rok_produkcji,
                this.przebieg,
                this.pojemnosc_silnika,
                this.ilosc
                );
    }

    @Override
    public int compareTo(Vehicle o) {
        return this.model.compareTo(o.model);
    }

    public void setId_s(int id_s) {
        this.id_s = id_s;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public void setRok_produkcji(Integer rok_produkcji) {
        this.rok_produkcji = rok_produkcji;
    }

    public void setPrzebieg(Double przebieg) {
        this.przebieg = przebieg;
    }

    public void setPojemnosc_silnika(Double pojemnosc_silnika) {
        this.pojemnosc_silnika = pojemnosc_silnika;
    }

    public void setNazwa_salonu(String nazwa_salonu) {
        this.nazwa_salonu = id_salonu.getNazwa_salonu();
    }

    public void setIlosc(Integer ilosc) {
        this.ilosc = ilosc;
    }

    public void setSalon(CarShowroom salon) {
        this.id_salonu = salon;
    }

    public int getId_s() {
        return id_s;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public Double getCena() {
        return cena;
    }

    public Integer getRok_produkcji() {
        return rok_produkcji;
    }

    public Double getPrzebieg() {
        return przebieg;
    }

    public Double getPojemnosc_silnika() {
        return pojemnosc_silnika;
    }

    public String getNazwa_salonu() {
        return id_salonu.getNazwa_salonu();
    }

    public Integer getIlosc() {
        return ilosc;
    }

    public CarShowroom getSalon() {
        return id_salonu;
    }

    public ItemCondition getStan() {
        return stan;
    }

    public void inc() {
        this.ilosc++;
    }

    public void dec()
    {
        this.ilosc--;
    }


}
