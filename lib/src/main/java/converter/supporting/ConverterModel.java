/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converter.supporting;

import datapoint.ModelAttribute;
import java.lang.reflect.Method;

/**
 * Model class to support the conversion of a Datapoint to Model class, and vice versa
 * 
 * @author kenna
 */
public class ConverterModel {
    
    private ModelAttribute modelAttrib;
    private Method setter, getter;
    private Object ref;
    private Class canonicalClass;
    
    public ConverterModel(){}
    
    public ConverterModel(ModelAttribute modelAttrib, Class canonicalClass, Object ref, Method setter) {
        this.modelAttrib = modelAttrib;
        this.canonicalClass = canonicalClass;
        this.ref = ref;
        this.setter = setter;
    }
    
    public ConverterModel(ModelAttribute modelAttrib, Class canonicalClass, Object ref, Method setter, Method getter) {
        this.modelAttrib = modelAttrib;
        this.canonicalClass = canonicalClass;
        this.ref = ref;
        this.setter = setter;
        this.getter = getter;
    }

    public ModelAttribute getModelAttrib() {
        return modelAttrib;
    }

    public void setModelAttrib(ModelAttribute modelAttrib) {
        this.modelAttrib = modelAttrib;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }

    public Class getCanonicalClass() {
        return canonicalClass;
    }

    public void setCanonicalClass(Class canonicalClass) {
        this.canonicalClass = canonicalClass;
    }

    @Override
    public String toString() {
        return "ModelDatapointBridge{" + "modelAttrib=" + modelAttrib + ", setter=" + setter + ", getter=" + getter + ", ref=" + ref + ", canonicalClass=" + canonicalClass + '}';
    }
}
