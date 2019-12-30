// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.NoSuchElementException;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.options.Options;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.scripts.JO;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.LongAdder;
import java.util.BitSet;
import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import java.util.WeakHashMap;
import java.lang.invoke.SwitchPoint;
import java.util.HashMap;
import java.io.Serializable;

public class PropertyMap implements Iterable<Object>, Serializable
{
    private static final int INITIAL_SOFT_REFERENCE_DERIVATION_LIMIT;
    private static final int NOT_EXTENSIBLE = 1;
    private static final int CONTAINS_ARRAY_KEYS = 2;
    private final int flags;
    private transient PropertyHashMap properties;
    private final int fieldCount;
    private final int fieldMaximum;
    private final int spillLength;
    private final String className;
    private final int softReferenceDerivationLimit;
    private transient SharedPropertyMap sharedProtoMap;
    private transient HashMap<String, SwitchPoint> protoSwitches;
    private transient WeakHashMap<Property, Reference<PropertyMap>> history;
    private transient WeakHashMap<ScriptObject, SoftReference<PropertyMap>> protoHistory;
    private transient PropertyListeners listeners;
    private transient BitSet freeSlots;
    private static final long serialVersionUID = -7041836752008732533L;
    private static LongAdder count;
    private static LongAdder clonedCount;
    private static LongAdder historyHit;
    private static LongAdder protoInvalidations;
    private static LongAdder protoHistoryHit;
    private static LongAdder setProtoNewMapCount;
    
    private PropertyMap(final PropertyHashMap properties, final int flags, final String className, final int fieldCount, final int fieldMaximum, final int spillLength) {
        this.properties = properties;
        this.className = className;
        this.fieldCount = fieldCount;
        this.fieldMaximum = fieldMaximum;
        this.spillLength = spillLength;
        this.flags = flags;
        this.softReferenceDerivationLimit = PropertyMap.INITIAL_SOFT_REFERENCE_DERIVATION_LIMIT;
        if (Context.DEBUG) {
            PropertyMap.count.increment();
        }
    }
    
    private PropertyMap(final PropertyMap propertyMap, final PropertyHashMap properties, final int flags, final int fieldCount, final int spillLength, final int softReferenceDerivationLimit) {
        this.properties = properties;
        this.flags = flags;
        this.spillLength = spillLength;
        this.fieldCount = fieldCount;
        this.fieldMaximum = propertyMap.fieldMaximum;
        this.className = propertyMap.className;
        this.listeners = propertyMap.listeners;
        this.freeSlots = propertyMap.freeSlots;
        this.sharedProtoMap = propertyMap.sharedProtoMap;
        this.softReferenceDerivationLimit = softReferenceDerivationLimit;
        if (Context.DEBUG) {
            PropertyMap.count.increment();
            PropertyMap.clonedCount.increment();
        }
    }
    
    protected PropertyMap(final PropertyMap propertyMap) {
        this(propertyMap, propertyMap.properties, propertyMap.flags, propertyMap.fieldCount, propertyMap.spillLength, propertyMap.softReferenceDerivationLimit);
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.properties.getProperties());
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final Property[] props = (Property[])in.readObject();
        this.properties = PropertyHashMap.EMPTY_HASHMAP.immutableAdd(props);
        assert this.className != null;
        final Class<?> structure = Context.forStructureClass(this.className);
        for (final Property prop : props) {
            prop.initMethodHandles(structure);
        }
    }
    
    public static PropertyMap newMap(final Collection<Property> properties, final String className, final int fieldCount, final int fieldMaximum, final int spillLength) {
        final PropertyHashMap newProperties = PropertyHashMap.EMPTY_HASHMAP.immutableAdd(properties);
        return new PropertyMap(newProperties, 0, className, fieldCount, fieldMaximum, spillLength);
    }
    
    public static PropertyMap newMap(final Collection<Property> properties) {
        return (properties == null || properties.isEmpty()) ? newMap() : newMap(properties, JO.class.getName(), 0, 0, 0);
    }
    
    public static PropertyMap newMap(final Class<? extends ScriptObject> clazz) {
        return new PropertyMap(PropertyHashMap.EMPTY_HASHMAP, 0, clazz.getName(), 0, 0, 0);
    }
    
    public static PropertyMap newMap() {
        return newMap(JO.class);
    }
    
    public int size() {
        return this.properties.size();
    }
    
    public int getListenerCount() {
        return (this.listeners == null) ? 0 : this.listeners.getListenerCount();
    }
    
    public void addListener(final String key, final PropertyMap listenerMap) {
        if (listenerMap != this) {
            this.listeners = PropertyListeners.addListener(this.listeners, key, listenerMap);
        }
    }
    
    public void propertyAdded(final Property property, final boolean isSelf) {
        if (!isSelf) {
            this.invalidateProtoSwitchPoint(property.getKey());
        }
        if (this.listeners != null) {
            this.listeners.propertyAdded(property);
        }
    }
    
    public void propertyDeleted(final Property property, final boolean isSelf) {
        if (!isSelf) {
            this.invalidateProtoSwitchPoint(property.getKey());
        }
        if (this.listeners != null) {
            this.listeners.propertyDeleted(property);
        }
    }
    
    public void propertyModified(final Property oldProperty, final Property newProperty, final boolean isSelf) {
        if (!isSelf) {
            this.invalidateProtoSwitchPoint(oldProperty.getKey());
        }
        if (this.listeners != null) {
            this.listeners.propertyModified(oldProperty, newProperty);
        }
    }
    
    public void protoChanged(final boolean isSelf) {
        if (!isSelf) {
            this.invalidateAllProtoSwitchPoints();
        }
        else if (this.sharedProtoMap != null) {
            this.sharedProtoMap.invalidateSwitchPoint();
        }
        if (this.listeners != null) {
            this.listeners.protoChanged();
        }
    }
    
    public synchronized SwitchPoint getSwitchPoint(final String key) {
        if (this.protoSwitches == null) {
            this.protoSwitches = new HashMap<String, SwitchPoint>();
        }
        SwitchPoint switchPoint = this.protoSwitches.get(key);
        if (switchPoint == null) {
            switchPoint = new SwitchPoint();
            this.protoSwitches.put(key, switchPoint);
        }
        return switchPoint;
    }
    
    synchronized void invalidateProtoSwitchPoint(final String key) {
        if (this.protoSwitches != null) {
            final SwitchPoint sp = this.protoSwitches.get(key);
            if (sp != null) {
                this.protoSwitches.remove(key);
                if (Context.DEBUG) {
                    PropertyMap.protoInvalidations.increment();
                }
                SwitchPoint.invalidateAll(new SwitchPoint[] { sp });
            }
        }
    }
    
    synchronized void invalidateAllProtoSwitchPoints() {
        if (this.protoSwitches != null) {
            final int size = this.protoSwitches.size();
            if (size > 0) {
                if (Context.DEBUG) {
                    PropertyMap.protoInvalidations.add(size);
                }
                SwitchPoint.invalidateAll(this.protoSwitches.values().toArray(new SwitchPoint[size]));
                this.protoSwitches.clear();
            }
        }
    }
    
    PropertyMap addPropertyBind(final AccessorProperty property, final Object bindTo) {
        return this.addPropertyNoHistory(new AccessorProperty(property, bindTo));
    }
    
    private int logicalSlotIndex(final Property property) {
        final int slot = property.getSlot();
        if (slot < 0) {
            return -1;
        }
        return property.isSpill() ? (slot + this.fieldMaximum) : slot;
    }
    
    private int newSpillLength(final Property newProperty) {
        return newProperty.isSpill() ? Math.max(this.spillLength, newProperty.getSlot() + 1) : this.spillLength;
    }
    
    private int newFieldCount(final Property newProperty) {
        return newProperty.isSpill() ? this.fieldCount : Math.max(this.fieldCount, newProperty.getSlot() + 1);
    }
    
    private int newFlags(final Property newProperty) {
        return ArrayIndex.isValidArrayIndex(ArrayIndex.getArrayIndex(newProperty.getKey())) ? (this.flags | 0x2) : this.flags;
    }
    
    private void updateFreeSlots(final Property oldProperty, final Property newProperty) {
        boolean freeSlotsCloned = false;
        if (oldProperty != null) {
            final int slotIndex = this.logicalSlotIndex(oldProperty);
            if (slotIndex >= 0) {
                final BitSet newFreeSlots = (BitSet)((this.freeSlots == null) ? new BitSet() : this.freeSlots.clone());
                assert !newFreeSlots.get(slotIndex);
                newFreeSlots.set(slotIndex);
                this.freeSlots = newFreeSlots;
                freeSlotsCloned = true;
            }
        }
        if (this.freeSlots != null && newProperty != null) {
            final int slotIndex = this.logicalSlotIndex(newProperty);
            if (slotIndex > -1 && this.freeSlots.get(slotIndex)) {
                final BitSet newFreeSlots = (BitSet)(freeSlotsCloned ? this.freeSlots : this.freeSlots.clone());
                newFreeSlots.clear(slotIndex);
                this.freeSlots = (newFreeSlots.isEmpty() ? null : newFreeSlots);
            }
        }
    }
    
    public final PropertyMap addPropertyNoHistory(final Property property) {
        this.propertyAdded(property, true);
        return this.addPropertyInternal(property);
    }
    
    public final synchronized PropertyMap addProperty(final Property property) {
        this.propertyAdded(property, true);
        PropertyMap newMap = this.checkHistory(property);
        if (newMap == null) {
            newMap = this.addPropertyInternal(property);
            this.addToHistory(property, newMap);
        }
        return newMap;
    }
    
    private PropertyMap deriveMap(final PropertyHashMap newProperties, final int newFlags, final int newFieldCount, final int newSpillLength) {
        return new PropertyMap(this, newProperties, newFlags, newFieldCount, newSpillLength, (this.softReferenceDerivationLimit == 0) ? 0 : (this.softReferenceDerivationLimit - 1));
    }
    
    private PropertyMap addPropertyInternal(final Property property) {
        final PropertyHashMap newProperties = this.properties.immutableAdd(property);
        final PropertyMap newMap = this.deriveMap(newProperties, this.newFlags(property), this.newFieldCount(property), this.newSpillLength(property));
        newMap.updateFreeSlots(null, property);
        return newMap;
    }
    
    public final synchronized PropertyMap deleteProperty(final Property property) {
        this.propertyDeleted(property, true);
        PropertyMap newMap = this.checkHistory(property);
        final String key = property.getKey();
        if (newMap == null && this.properties.containsKey(key)) {
            final PropertyHashMap newProperties = this.properties.immutableRemove(key);
            final boolean isSpill = property.isSpill();
            final int slot = property.getSlot();
            if (isSpill && slot >= 0 && slot == this.spillLength - 1) {
                newMap = this.deriveMap(newProperties, this.flags, this.fieldCount, this.spillLength - 1);
                newMap.freeSlots = this.freeSlots;
            }
            else if (!isSpill && slot >= 0 && slot == this.fieldCount - 1) {
                newMap = this.deriveMap(newProperties, this.flags, this.fieldCount - 1, this.spillLength);
                newMap.freeSlots = this.freeSlots;
            }
            else {
                newMap = this.deriveMap(newProperties, this.flags, this.fieldCount, this.spillLength);
                newMap.updateFreeSlots(property, null);
            }
            this.addToHistory(property, newMap);
        }
        return newMap;
    }
    
    public final PropertyMap replaceProperty(final Property oldProperty, final Property newProperty) {
        this.propertyModified(oldProperty, newProperty, true);
        final boolean sameType = oldProperty.getClass() == newProperty.getClass();
        assert sameType || (oldProperty instanceof AccessorProperty && newProperty instanceof UserAccessorProperty) : "arbitrary replaceProperty attempted " + sameType + " oldProperty=" + oldProperty.getClass() + " newProperty=" + newProperty.getClass() + " [" + oldProperty.getLocalType() + " => " + newProperty.getLocalType() + "]";
        final int newSpillLength = sameType ? this.spillLength : Math.max(this.spillLength, newProperty.getSlot() + 1);
        final PropertyHashMap newProperties = this.properties.immutableReplace(oldProperty, newProperty);
        final PropertyMap newMap = this.deriveMap(newProperties, this.flags, this.fieldCount, newSpillLength);
        if (!sameType) {
            newMap.updateFreeSlots(oldProperty, newProperty);
        }
        return newMap;
    }
    
    public final UserAccessorProperty newUserAccessors(final String key, final int propertyFlags) {
        return new UserAccessorProperty(key, propertyFlags, this.getFreeSpillSlot());
    }
    
    public final Property findProperty(final String key) {
        return this.properties.find(key);
    }
    
    public final PropertyMap addAll(final PropertyMap other) {
        assert this != other : "adding property map to itself";
        final Property[] otherProperties = other.properties.getProperties();
        final PropertyHashMap newProperties = this.properties.immutableAdd(otherProperties);
        final PropertyMap newMap = this.deriveMap(newProperties, this.flags, this.fieldCount, this.spillLength);
        for (final Property property : otherProperties) {
            assert property.getSlot() == -1;
            assert !ArrayIndex.isValidArrayIndex(ArrayIndex.getArrayIndex(property.getKey()));
        }
        return newMap;
    }
    
    public final Property[] getProperties() {
        return this.properties.getProperties();
    }
    
    public final String getClassName() {
        return this.className;
    }
    
    PropertyMap preventExtensions() {
        return this.deriveMap(this.properties, this.flags | 0x1, this.fieldCount, this.spillLength);
    }
    
    PropertyMap seal() {
        PropertyHashMap newProperties = PropertyHashMap.EMPTY_HASHMAP;
        for (final Property oldProperty : this.properties.getProperties()) {
            newProperties = newProperties.immutableAdd(oldProperty.addFlags(4));
        }
        return this.deriveMap(newProperties, this.flags | 0x1, this.fieldCount, this.spillLength);
    }
    
    PropertyMap freeze() {
        PropertyHashMap newProperties = PropertyHashMap.EMPTY_HASHMAP;
        for (final Property oldProperty : this.properties.getProperties()) {
            int propertyFlags = 4;
            if (!(oldProperty instanceof UserAccessorProperty)) {
                propertyFlags |= 0x1;
            }
            newProperties = newProperties.immutableAdd(oldProperty.addFlags(propertyFlags));
        }
        return this.deriveMap(newProperties, this.flags | 0x1, this.fieldCount, this.spillLength);
    }
    
    private boolean anyConfigurable() {
        for (final Property property : this.properties.getProperties()) {
            if (property.isConfigurable()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean allFrozen() {
        for (final Property property : this.properties.getProperties()) {
            if (!(property instanceof UserAccessorProperty) && property.isWritable()) {
                return false;
            }
            if (property.isConfigurable()) {
                return false;
            }
        }
        return true;
    }
    
    private PropertyMap checkProtoHistory(final ScriptObject proto) {
        PropertyMap cachedMap;
        if (this.protoHistory != null) {
            final SoftReference<PropertyMap> weakMap = this.protoHistory.get(proto);
            cachedMap = ((weakMap != null) ? weakMap.get() : null);
        }
        else {
            cachedMap = null;
        }
        if (Context.DEBUG && cachedMap != null) {
            PropertyMap.protoHistoryHit.increment();
        }
        return cachedMap;
    }
    
    private void addToProtoHistory(final ScriptObject newProto, final PropertyMap newMap) {
        if (this.protoHistory == null) {
            this.protoHistory = new WeakHashMap<ScriptObject, SoftReference<PropertyMap>>();
        }
        this.protoHistory.put(newProto, new SoftReference<PropertyMap>(newMap));
    }
    
    private void addToHistory(final Property property, final PropertyMap newMap) {
        if (this.history == null) {
            this.history = new WeakHashMap<Property, Reference<PropertyMap>>();
        }
        this.history.put(property, (Reference<PropertyMap>)((this.softReferenceDerivationLimit == 0) ? new WeakReference<PropertyMap>(newMap) : new SoftReference<PropertyMap>(newMap)));
    }
    
    private PropertyMap checkHistory(final Property property) {
        if (this.history != null) {
            final Reference<PropertyMap> ref = this.history.get(property);
            final PropertyMap historicMap = (ref == null) ? null : ref.get();
            if (historicMap != null) {
                if (Context.DEBUG) {
                    PropertyMap.historyHit.increment();
                }
                return historicMap;
            }
        }
        return null;
    }
    
    public boolean equalsWithoutType(final PropertyMap otherMap) {
        if (this.properties.size() != otherMap.properties.size()) {
            return false;
        }
        final Iterator<Property> iter = this.properties.values().iterator();
        final Iterator<Property> otherIter = otherMap.properties.values().iterator();
        while (iter.hasNext() && otherIter.hasNext()) {
            if (!iter.next().equalsWithoutType(otherIter.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Debug.id(this));
        sb.append(" = {\n");
        for (final Property property : this.getProperties()) {
            sb.append('\t');
            sb.append(property);
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public Iterator<Object> iterator() {
        return new PropertyMapIterator(this);
    }
    
    public final boolean containsArrayKeys() {
        return (this.flags & 0x2) != 0x0;
    }
    
    boolean isExtensible() {
        return (this.flags & 0x1) == 0x0;
    }
    
    boolean isSealed() {
        return !this.isExtensible() && !this.anyConfigurable();
    }
    
    boolean isFrozen() {
        return !this.isExtensible() && this.allFrozen();
    }
    
    int getFreeFieldSlot() {
        if (this.freeSlots != null) {
            final int freeSlot = this.freeSlots.nextSetBit(0);
            if (freeSlot > -1 && freeSlot < this.fieldMaximum) {
                return freeSlot;
            }
        }
        if (this.fieldCount < this.fieldMaximum) {
            return this.fieldCount;
        }
        return -1;
    }
    
    int getFreeSpillSlot() {
        if (this.freeSlots != null) {
            final int freeSlot = this.freeSlots.nextSetBit(this.fieldMaximum);
            if (freeSlot > -1) {
                return freeSlot - this.fieldMaximum;
            }
        }
        return this.spillLength;
    }
    
    public synchronized PropertyMap changeProto(final ScriptObject newProto) {
        final PropertyMap nextMap = this.checkProtoHistory(newProto);
        if (nextMap != null) {
            return nextMap;
        }
        if (Context.DEBUG) {
            PropertyMap.setProtoNewMapCount.increment();
        }
        final PropertyMap newMap = this.makeUnsharedCopy();
        this.addToProtoHistory(newProto, newMap);
        return newMap;
    }
    
    PropertyMap makeUnsharedCopy() {
        final PropertyMap newMap = new PropertyMap(this);
        newMap.sharedProtoMap = null;
        return newMap;
    }
    
    void setSharedProtoMap(final SharedPropertyMap protoMap) {
        this.sharedProtoMap = protoMap;
    }
    
    public PropertyMap getSharedProtoMap() {
        return this.sharedProtoMap;
    }
    
    boolean isValidSharedProtoMap() {
        return false;
    }
    
    SwitchPoint getSharedProtoSwitchPoint() {
        return null;
    }
    
    boolean isInvalidSharedMapFor(final ScriptObject prototype) {
        return this.sharedProtoMap != null && (!this.sharedProtoMap.isValidSharedProtoMap() || prototype == null || this.sharedProtoMap != prototype.getMap());
    }
    
    public static String diff(final PropertyMap map0, final PropertyMap map1) {
        final StringBuilder sb = new StringBuilder();
        if (map0 != map1) {
            sb.append(">>> START: Map diff");
            boolean found = false;
            for (final Property p : map0.getProperties()) {
                final Property p2 = map1.findProperty(p.getKey());
                if (p2 == null) {
                    sb.append("FIRST ONLY : [").append(p).append("]");
                    found = true;
                }
                else if (p2 != p) {
                    sb.append("DIFFERENT  : [").append(p).append("] != [").append(p2).append("]");
                    found = true;
                }
            }
            for (final Property p3 : map1.getProperties()) {
                final Property p4 = map0.findProperty(p3.getKey());
                if (p4 == null) {
                    sb.append("SECOND ONLY: [").append(p3).append("]");
                    found = true;
                }
            }
            if (!found) {
                sb.append(map0).append("!=").append(map1);
            }
            sb.append("<<< END: Map diff\n");
        }
        return sb.toString();
    }
    
    public static long getCount() {
        return PropertyMap.count.longValue();
    }
    
    public static long getClonedCount() {
        return PropertyMap.clonedCount.longValue();
    }
    
    public static long getHistoryHit() {
        return PropertyMap.historyHit.longValue();
    }
    
    public static long getProtoInvalidations() {
        return PropertyMap.protoInvalidations.longValue();
    }
    
    public static long getProtoHistoryHit() {
        return PropertyMap.protoHistoryHit.longValue();
    }
    
    public static long getSetProtoNewMapCount() {
        return PropertyMap.setProtoNewMapCount.longValue();
    }
    
    static {
        INITIAL_SOFT_REFERENCE_DERIVATION_LIMIT = Math.max(0, Options.getIntProperty("nashorn.propertyMap.softReferenceDerivationLimit", 32));
        if (Context.DEBUG) {
            PropertyMap.count = new LongAdder();
            PropertyMap.clonedCount = new LongAdder();
            PropertyMap.historyHit = new LongAdder();
            PropertyMap.protoInvalidations = new LongAdder();
            PropertyMap.protoHistoryHit = new LongAdder();
            PropertyMap.setProtoNewMapCount = new LongAdder();
        }
    }
    
    private static class PropertyMapIterator implements Iterator<Object>
    {
        final Iterator<Property> iter;
        Property property;
        
        PropertyMapIterator(final PropertyMap propertyMap) {
            this.iter = Arrays.asList(propertyMap.properties.getProperties()).iterator();
            this.property = (this.iter.hasNext() ? this.iter.next() : null);
            this.skipNotEnumerable();
        }
        
        private void skipNotEnumerable() {
            while (this.property != null && !this.property.isEnumerable()) {
                this.property = (this.iter.hasNext() ? this.iter.next() : null);
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.property != null;
        }
        
        @Override
        public Object next() {
            if (this.property == null) {
                throw new NoSuchElementException();
            }
            final Object key = this.property.getKey();
            this.property = this.iter.next();
            this.skipNotEnumerable();
            return key;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
