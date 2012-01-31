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
import java.util.Collection;
import java.util.logging.Logger;

import org.nnsoft.hazelmap.Reducer;

public final class ReducerRunnable<K extends Serializable, V extends Serializable>
    implements Runnable, Serializable
{

    private static final Logger logger = Logger.getLogger( "org.nnsoft.hazelmap.reducer" );

    /**
     *
     */
    private static final long serialVersionUID = 1103886616850434641L;

    public static <K extends Serializable, V extends Serializable> ReducerRunnable<K, V> toRunnable( K key,
                                                                                                     Collection<V> values,
                                                                                                     Reducer<K, V> reducer )
    {
        return new ReducerRunnable<K, V>( key, values, reducer );
    }

    private final K key;

    private final Collection<V> values;

    private final Reducer<K, V> reducer;

    private ReducerRunnable( K key, Collection<V> values, Reducer<K, V> reducer )
    {
        this.key = key;
        this.values = values;
        this.reducer = reducer;
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        if ( logger.isLoggable( INFO ) )
        {
            logger.info( format( "Reduce( %s, list( %s ) )", key, values ) );
        }

        try
        {
            reducer.reduce( key, values );
        }
        catch ( Throwable t )
        {
            if ( logger.isLoggable( SEVERE ) )
            {
                logger.log( SEVERE,
                            format( "Reduce( %s, list( %s ) ) produced the following error: ", key, values ),
                            t );
            }
        }
    }

}
