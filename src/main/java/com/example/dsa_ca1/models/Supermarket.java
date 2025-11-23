package com.example.dsa_ca1.models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Supermarket {
    // fields
    private String name = "";
    private CustomLinkedList<FloorArea> floorAreas = new CustomLinkedList<>();

    // constructor
    public Supermarket(String name) {
        this.name = name;
    }

    // getters & setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public CustomLinkedList<FloorArea> getFloorAreas() {
        return floorAreas;
    }
    public void setFloorAreas(CustomLinkedList<FloorArea> floorAreas) {
        this.floorAreas = floorAreas;
    }

    // methods
    public void addFloorArea(FloorArea fA) {
        floorAreas.add(fA);
    }

    public void removeFloorArea(FloorArea fA) {
        floorAreas.removeValue(fA);
    }

    // persistence
    public void save(String fileName) throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName + ".xml"));
        out.writeObject(this);
        out.close();
    }

    public static Supermarket load(String fileName) throws Exception {
        Class<?>[] classes = new Class[] {
                Supermarket.class,
                FloorArea.class,
                Aisle.class,
                Shelf.class,
                Product.class};

        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        ObjectInputStream is = xstream.createObjectInputStream(new FileReader(fileName + ".xml"));
        Supermarket loaded = (Supermarket) is.readObject();
        is.close();
        return loaded;
    }

}
