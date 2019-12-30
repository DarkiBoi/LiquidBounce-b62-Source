// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;

public final class PropertyHashMap implements Map<String, Property>
{
    private static final int INITIAL_BINS = 32;
    private static final int LIST_THRESHOLD = 8;
    public static final PropertyHashMap EMPTY_HASHMAP;
    private final int size;
    private final int threshold;
    private final Element list;
    private final Element[] bins;
    private Property[] properties;
    
    private PropertyHashMap() {
        this.size = 0;
        this.threshold = 0;
        this.bins = null;
        this.list = null;
    }
    
    private PropertyHashMap(final PropertyHashMap map) {
        this.size = map.size;
        this.threshold = map.threshold;
        this.bins = map.bins;
        this.list = map.list;
    }
    
    private PropertyHashMap(final int size, final Element[] bins, final Element list) {
        this.size = size;
        this.threshold = ((bins != null) ? threeQuarters(bins.length) : 0);
        this.bins = bins;
        this.list = list;
    }
    
    public PropertyHashMap immutableReplace(final Property property, final Property newProperty) {
        assert property.getKey().equals(newProperty.getKey()) : "replacing properties with different keys: '" + property.getKey() + "' != '" + newProperty.getKey() + "'";
        assert this.findElement(property.getKey()) != null : "replacing property that doesn't exist in map: '" + property.getKey() + "'";
        return this.cloneMap().replaceNoClone(property.getKey(), newProperty);
    }
    
    public PropertyHashMap immutableAdd(final Property property) {
        final int newSize = this.size + 1;
        PropertyHashMap newMap = this.cloneMap(newSize);
        newMap = newMap.addNoClone(property);
        return newMap;
    }
    
    public PropertyHashMap immutableAdd(final Property... newProperties) {
        final int newSize = this.size + newProperties.length;
        PropertyHashMap newMap = this.cloneMap(newSize);
        for (final Property property : newProperties) {
            newMap = newMap.addNoClone(property);
        }
        return newMap;
    }
    
    public PropertyHashMap immutableAdd(final Collection<Property> newProperties) {
        if (newProperties != null) {
            final int newSize = this.size + newProperties.size();
            PropertyHashMap newMap = this.cloneMap(newSize);
            for (final Property property : newProperties) {
                newMap = newMap.addNoClone(property);
            }
            return newMap;
        }
        return this;
    }
    
    public PropertyHashMap immutableRemove(final Property property) {
        return this.immutableRemove(property.getKey());
    }
    
    public PropertyHashMap immutableRemove(final String key) {
        if (this.bins != null) {
            final int binIndex = binIndex(this.bins, key);
            final Element bin = this.bins[binIndex];
            if (findElement(bin, key) != null) {
                final int newSize = this.size - 1;
                Element[] newBins = null;
                if (newSize >= 8) {
                    newBins = this.bins.clone();
                    newBins[binIndex] = removeFromList(bin, key);
                }
                final Element newList = removeFromList(this.list, key);
                return new PropertyHashMap(newSize, newBins, newList);
            }
        }
        else if (findElement(this.list, key) != null) {
            final int newSize2 = this.size - 1;
            return (newSize2 != 0) ? new PropertyHashMap(newSize2, null, removeFromList(this.list, key)) : PropertyHashMap.EMPTY_HASHMAP;
        }
        return this;
    }
    
    public Property find(final String key) {
        final Element element = this.findElement(key);
        return (element != null) ? element.getProperty() : null;
    }
    
    Property[] getProperties() {
        if (this.properties == null) {
            final Property[] array = new Property[this.size];
            int i = this.size;
            for (Element element = this.list; element != null; element = element.getLink()) {
                array[--i] = element.getProperty();
            }
            this.properties = array;
        }
        return this.properties;
    }
    
    private static int binIndex(final Element[] bins, final String key) {
        return key.hashCode() & bins.length - 1;
    }
    
    private static int binsNeeded(final int n) {
        return 1 << 32 - Integer.numberOfLeadingZeros(n + (n >>> 1) | 0x1F);
    }
    
    private static int threeQuarters(final int n) {
        return (n >>> 1) + (n >>> 2);
    }
    
    private static Element[] rehash(final Element list, final int binSize) {
        final Element[] newBins = new Element[binSize];
        for (Element element = list; element != null; element = element.getLink()) {
            final Property property = element.getProperty();
            final String key = property.getKey();
            final int binIndex = binIndex(newBins, key);
            newBins[binIndex] = new Element(newBins[binIndex], property);
        }
        return newBins;
    }
    
    private Element findElement(final String key) {
        if (this.bins != null) {
            final int binIndex = binIndex(this.bins, key);
            return findElement(this.bins[binIndex], key);
        }
        return findElement(this.list, key);
    }
    
    private static Element findElement(final Element elementList, final String key) {
        final int hashCode = key.hashCode();
        for (Element element = elementList; element != null; element = element.getLink()) {
            if (element.match(key, hashCode)) {
                return element;
            }
        }
        return null;
    }
    
    private PropertyHashMap cloneMap() {
        return new PropertyHashMap(this.size, (Element[])((this.bins == null) ? null : ((Element[])this.bins.clone())), this.list);
    }
    
    private PropertyHashMap cloneMap(final int newSize) {
        Element[] newBins;
        if (this.bins == null && newSize <= 8) {
            newBins = null;
        }
        else if (newSize > this.threshold) {
            newBins = rehash(this.list, binsNeeded(newSize));
        }
        else {
            newBins = this.bins.clone();
        }
        return new PropertyHashMap(newSize, newBins, this.list);
    }
    
    private PropertyHashMap addNoClone(final Property property) {
        int newSize = this.size;
        final String key = property.getKey();
        Element newList = this.list;
        if (this.bins != null) {
            final int binIndex = binIndex(this.bins, key);
            Element bin = this.bins[binIndex];
            if (findElement(bin, key) != null) {
                --newSize;
                bin = removeFromList(bin, key);
                newList = removeFromList(this.list, key);
            }
            this.bins[binIndex] = new Element(bin, property);
        }
        else if (findElement(this.list, key) != null) {
            --newSize;
            newList = removeFromList(this.list, key);
        }
        newList = new Element(newList, property);
        return new PropertyHashMap(newSize, this.bins, newList);
    }
    
    private PropertyHashMap replaceNoClone(final String key, final Property property) {
        if (this.bins != null) {
            final int binIndex = binIndex(this.bins, key);
            Element bin = this.bins[binIndex];
            bin = replaceInList(bin, key, property);
            this.bins[binIndex] = bin;
        }
        Element newList = this.list;
        newList = replaceInList(newList, key, property);
        return new PropertyHashMap(this.size, this.bins, newList);
    }
    
    private static Element removeFromList(final Element list, final String key) {
        if (list == null) {
            return null;
        }
        final int hashCode = key.hashCode();
        if (list.match(key, hashCode)) {
            return list.getLink();
        }
        Element previous;
        final Element head = previous = new Element(null, list.getProperty());
        for (Element element = list.getLink(); element != null; element = element.getLink()) {
            if (element.match(key, hashCode)) {
                previous.setLink(element.getLink());
                return head;
            }
            final Element next = new Element(null, element.getProperty());
            previous.setLink(next);
            previous = next;
        }
        return list;
    }
    
    private static Element replaceInList(final Element list, final String key, final Property property) {
        assert list != null;
        final int hashCode = key.hashCode();
        if (list.match(key, hashCode)) {
            return new Element(list.getLink(), property);
        }
        Element previous;
        final Element head = previous = new Element(null, list.getProperty());
        for (Element element = list.getLink(); element != null; element = element.getLink()) {
            if (element.match(key, hashCode)) {
                previous.setLink(new Element(element.getLink(), property));
                return head;
            }
            final Element next = new Element(null, element.getProperty());
            previous.setLink(next);
            previous = next;
        }
        return list;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        if (key instanceof String) {
            return this.findElement((String)key) != null;
        }
        assert key instanceof String;
        return false;
    }
    
    public boolean containsKey(final String key) {
        return this.findElement(key) != null;
    }
    
    @Override
    public boolean containsValue(final Object value) {
        if (value instanceof Property) {
            final Property property = (Property)value;
            final Element element = this.findElement(property.getKey());
            return element != null && element.getProperty().equals(value);
        }
        return false;
    }
    
    @Override
    public Property get(final Object key) {
        if (key instanceof String) {
            final Element element = this.findElement((String)key);
            return (element != null) ? element.getProperty() : null;
        }
        assert key instanceof String;
        return null;
    }
    
    public Property get(final String key) {
        final Element element = this.findElement(key);
        return (element != null) ? element.getProperty() : null;
    }
    
    @Override
    public Property put(final String key, final Property value) {
        throw new UnsupportedOperationException("Immutable map.");
    }
    
    @Override
    public Property remove(final Object key) {
        throw new UnsupportedOperationException("Immutable map.");
    }
    
    @Override
    public void putAll(final Map<? extends String, ? extends Property> m) {
        throw new UnsupportedOperationException("Immutable map.");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Immutable map.");
    }
    
    @Override
    public Set<String> keySet() {
        final HashSet<String> set = new HashSet<String>();
        for (Element element = this.list; element != null; element = element.getLink()) {
            set.add(element.getKey());
        }
        return Collections.unmodifiableSet((Set<? extends String>)set);
    }
    
    @Override
    public Collection<Property> values() {
        return (Collection<Property>)Collections.unmodifiableList((List<?>)Arrays.asList((T[])this.getProperties()));
    }
    
    @Override
    public Set<Entry<String, Property>> entrySet() {
        final HashSet<Entry<String, Property>> set = new HashSet<Entry<String, Property>>();
        for (Element element = this.list; element != null; element = element.getLink()) {
            set.add(element);
        }
        return Collections.unmodifiableSet((Set<? extends Entry<String, Property>>)set);
    }
    
    static {
        EMPTY_HASHMAP = new PropertyHashMap();
    }
    
    static final class Element implements Entry<String, Property>
    {
        private Element link;
        private final Property property;
        private final String key;
        private final int hashCode;
        
        Element(final Element link, final Property property) {
            this.link = link;
            this.property = property;
            this.key = property.getKey();
            this.hashCode = this.key.hashCode();
        }
        
        boolean match(final String otherKey, final int otherHashCode) {
            return this.hashCode == otherHashCode && this.key.equals(otherKey);
        }
        
        @Override
        public boolean equals(final Object other) {
            assert this.property != null && other != null;
            return other instanceof Element && this.property.equals(((Element)other).property);
        }
        
        @Override
        public String getKey() {
            return this.key;
        }
        
        @Override
        public Property getValue() {
            return this.property;
        }
        
        @Override
        public int hashCode() {
            return this.hashCode;
        }
        
        @Override
        public Property setValue(final Property value) {
            throw new UnsupportedOperationException("Immutable map.");
        }
        
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append('[');
            Element elem = this;
            do {
                sb.append(elem.getValue());
                elem = elem.link;
                if (elem != null) {
                    sb.append(" -> ");
                }
            } while (elem != null);
            sb.append(']');
            return sb.toString();
        }
        
        Element getLink() {
            return this.link;
        }
        
        void setLink(final Element link) {
            this.link = link;
        }
        
        Property getProperty() {
            return this.property;
        }
    }
}
