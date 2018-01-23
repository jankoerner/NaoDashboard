package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALLeds;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LEDModel {
    @FXML ComboBox cb_LEDS;
    Controller controller;
    private ALLeds alLeds;
    private ObservableList ledList;
    public void test(Application app) {
        try {
            alLeds = new ALLeds(app.session());
            ledList = new ObservableList() {
                @Override
                public void addListener(ListChangeListener listener) {

                }

                @Override
                public void removeListener(ListChangeListener listener) {

                }

                @Override
                public boolean addAll(Object[] elements) {
                    return false;
                }

                @Override
                public boolean setAll(Object[] elements) {
                    return false;
                }

                @Override
                public boolean setAll(Collection col) {
                    return false;
                }

                @Override
                public boolean removeAll(Object[] elements) {
                    return false;
                }

                @Override
                public boolean retainAll(Object[] elements) {
                    return false;
                }

                @Override
                public void remove(int from, int to) {

                }

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @Override
                public Iterator iterator() {
                    return null;
                }

                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public Object[] toArray(Object[] a) {
                    return new Object[0];
                }

                @Override
                public boolean add(Object o) {
                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection c) {
                    return false;
                }

                @Override
                public boolean addAll(Collection c) {
                    return false;
                }

                @Override
                public boolean addAll(int index, Collection c) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection c) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection c) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public Object get(int index) {
                    return null;
                }

                @Override
                public Object set(int index, Object element) {
                    return null;
                }

                @Override
                public void add(int index, Object element) {

                }

                @Override
                public Object remove(int index) {
                    return null;
                }

                @Override
                public int indexOf(Object o) {
                    return 0;
                }

                @Override
                public int lastIndexOf(Object o) {
                    return 0;
                }

                @Override
                public ListIterator listIterator() {
                    return null;
                }

                @Override
                public ListIterator listIterator(int index) {
                    return null;
                }

                @Override
                public List subList(int fromIndex, int toIndex) {
                    return null;
                }

                @Override
                public void addListener(InvalidationListener listener) {

                }

                @Override
                public void removeListener(InvalidationListener listener) {

                }
            };
            ledList = FXCollections.observableList(alLeds.listLEDs());
            cb_LEDS.setItems(ledList);
            System.out.println(alLeds.listLEDs());
            } catch (Exception e){
            e.printStackTrace();
        }
    }

}
