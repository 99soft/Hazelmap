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

import com.hazelcast.core.MultiMap;

/**
 * "Map" step: The master node takes the input, partitions it up into smaller sub-problems,
 * and distributes them to worker nodes.
 *
 * @param <IK>
 * @param <IV>
 * @param <OK>
 * @param <OV>
 */
public abstract class Mapper<IK extends Serializable, IV extends Serializable, OK extends Serializable, OV extends Serializable>
    implements Serializable
{

    private static final long serialVersionUID = 7647677113647110866L;

    /**
     * The intermediate
     */
    private MultiMap<OK, OV> intermediate;

    void setIntermediate( MultiMap<OK, OV> intermediate )
    {
        if ( this.intermediate != null )
        {
            throw new IllegalStateException( "Re-entry not allowed." );
        }

        this.intermediate = intermediate;
    }

    void resetIntermediate()
    {
        intermediate = null;
    }

    protected final void emitIntermediate( OK key, OV value )
    {
        intermediate.put( key, value );
    }

    /**
     * <i>Map</i> takes one pair of data with a type in one data domain,
     * and returns a list of pairs in a different domain {@code Map(k1,v1) &rarr; list(k2,v2)}
     *
     * @param inputKey
     * @param inputValue
     */
    abstract public void map( IK inputKey, IV inputValue );

}
