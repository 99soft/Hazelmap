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

final class MapperRunnable<K extends Serializable, V extends Serializable>
    implements Runnable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -3104075452456356524L;

    public static <K extends Serializable, V extends Serializable> MapperRunnable<K, V> runnableMapper( K key, V value,
                                                                                                        Mapper<K, V, ? extends Serializable, ? extends Serializable> mapper )
    {
        return new MapperRunnable<K, V>( key, value, mapper );
    }

    private final K key;

    private final V value;

    private final Mapper<K, V, ? extends Serializable, ? extends Serializable> mapper;

    private MapperRunnable( K key, V value, Mapper<K, V, ? extends Serializable, ? extends Serializable> mapper )
    {
        this.key = key;
        this.value = value;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        mapper.map( key, value );
    }

}
