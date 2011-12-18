package org.nnsoft.hazelmap;

/*
 *    Copyright 2011 The 99 Software Foundation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import java.io.Serializable;

/**
 * The <i>Reduce</i> function is applied in parallel to each <i>Map</> group,
 * which in turn produces a collection of values in the same domain:
 * {@code Reduce(k2, list (v2)) &rarr; list(v3)}
 *
 * @param <K>
 * @param <V>
 */
public abstract class Reducer<K extends Serializable, V extends Serializable>
    implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2485928172918908803L;

    private OutputWriter<K, V, ?> outputWriter;

    void init( OutputWriter<K, V, ?> outputWriter )
    {
        if ( this.outputWriter != null )
        {
            throw new IllegalStateException( "Re-entry is not allowed" );
        }

        this.outputWriter = outputWriter;
    }

    void reset()
    {
        outputWriter = null;
    }

    protected final void emit( K key, V value )
    {
        outputWriter.write( key, value );
    }

    /**
     * Each Reduce call typically produces either one value v3 or an empty return,
     * though one call is allowed to return more than one value.
     * The returns of all calls are collected as the desired result list.
     *
     * @param key
     * @param values
     */
    abstract void reduce( K key, Iterable<V> values );

}
