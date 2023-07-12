/*
 * Copyright 2023 kenna.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package converter;

import converter.supporting.DatapointToModelConverter;
import converter.supporting.ModelToDatapointConverter;
import datapoint.Datapoint;


import java.util.Map;


/**
 * Generic class to provide support for converting Model class to Datapoint.
 *  A Converter works with one Model class, but different converters can be constructors.
 *  Reflection operations abstracted into its Converters, which can be provided
 * 
 * @author kenna
 * @param <T>
 */
public class ModelDatapointConverter<T> {
    
    // Attributes
    private final Class refClass;
    private final DatapointToModelConverter<T> datapointConverter;
    private final ModelToDatapointConverter<T> modelConverter;
    
    
    /**
     * Construct with converters
     * @param refClass
     */
    public ModelDatapointConverter(Class refClass) {
        this.refClass = refClass;
        this.datapointConverter = new DatapointToModelConverter<>(refClass);
        this.modelConverter = new ModelToDatapointConverter<>(refClass);
    }
    
    
    /**
     * Convert a input Model class of type T to Datapoint
     * 
     * @param model
     * @return - Datapoint
     */
    public Datapoint convertModel(T model) {
        return modelConverter.datapointFromModel(model);
    }
    
    
    /**
     * Convert Datapoint back into Original Model class
     * 
     * @param record
     * @return - T Model
     */
    public T convertDatapoint(Datapoint record) {
        return datapointConverter.convert(record);
    }
    
    
    /**
     * Update the aliases of a Datapoint
     * 
     * @param record
     * @param aliasUpdateMap 
     */
    public void updateDatapointAliases(Datapoint record, Map<String, String> aliasUpdateMap) {
        modelConverter.updateAttributeAliases(record, aliasUpdateMap);
    }
    
    
    /**
     * Wrapper Method to convert a Model to Datapoint, using provided aliases.
     *  Map is of form<br><br>Key = Attribute Name<br>Value = Alias
     * <br><br>Example for Person would be Key='fName', Value='First Name'
     * 
     * @param model
     * @param aliasMap
     * @return - Datapoint
     */
    public Datapoint convertModel(T model, Map<String, String> aliasMap) {
        Datapoint output = convertModel(model);
        updateDatapointAliases(output, aliasMap);
        return output;
    }

    
    /**
     * Provide the Datapoint Converter, which converts Datapoints to Models
     * 
     * @return - DatapointToModelConverter of T type
     */
    public DatapointToModelConverter<T> getDatapointConverter() {
        return datapointConverter;
    }

    
    /**
     * Provide the Model Converter, which converts Models to Datapoints
     * 
     * @return - ModelToDatapointConverter
     */
    public ModelToDatapointConverter getModelConverter() {
        return modelConverter;
    }
    
    
    /**
     * Get reference class
     * 
     * @return - Reference Class
     */
    public Class getReferenceClass() {
        return this.refClass;
    }
}
