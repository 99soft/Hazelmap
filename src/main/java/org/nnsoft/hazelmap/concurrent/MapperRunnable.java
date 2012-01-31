package org.nnsoft.hazelmap.concurrent;

/*
 *    Copyright 2011 - 2012 The 99 Software Foundation
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

import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.io.Serializable;
import java.util.logging.Logger;

import org.nnsoft.hazelmap.Mapper;

public final class MapperRunnable<K extends Serializable, V extends Serializable>
    implements Runnable, Serializable
{

    private static final Logger logger = Logger.getLogger( "org.nnsoft.hazelmap.mapper" );

    /**
     *
     */
    private static final long serialVersionUID = -3104075452456356524L;

    public static <K extends Serializable, V extends Serializable> MapperRunnable<K, V> toRunnable( K key, V value,
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
        if ( logger.isLoggable( INFO ) )
        {
            logger.info( format( "Map( %s, %s )", key, value ) );
        }

        try
        {
            mapper.map( key, value );
        }
        catch ( Throwable t )
        {
            if ( logger.isLoggable( SEVERE ) )
            {
                logger.log( SEVERE,
                            format( "Map( %s, %s ) produced the following error: ", key, value ),
                            t );
            }
        }
    }

}
