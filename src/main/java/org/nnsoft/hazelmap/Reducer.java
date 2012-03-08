package org.nnsoft.hazelmap;

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

import static com.hazelcast.core.Hazelcast.getCluster;
import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static org.nnsoft.hazelmap.utils.Assertions.checkState;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Logger;

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


    private static final Logger logger = Logger.getLogger( "org.nnsoft.hazelmap.reducer" );

    private OutputWriter<K, V, ?> outputWriter;

    void init( OutputWriter<K, V, ?> outputWriter )
    {
        checkState( this.outputWriter == null, "Re-entry is not allowed" );

        this.outputWriter = outputWriter;
    }

    void reset()
    {
        outputWriter = null;
    }

    protected final void emit( K key, V value )
    {
        if ( logger.isLoggable( INFO ) )
        {
            logger.info( format( "[%s] emit( %s, %s )", getCluster().getLocalMember(), key, value ) );
        }
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
    abstract public void reduce( K key, Iterable<V> values );

    final Runnable newRunnable( K key, Collection<V> values )
    {
        return new ReducerRunnable<K, V>( key, values, this );
    }

    private static final class ReducerRunnable<K extends Serializable, V extends Serializable>
        implements Runnable, Serializable
    {

        private static final long serialVersionUID = 1103886616850434641L;

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
                logger.info( format( "[%s] Reduce( %s, list( %s ) )", getCluster().getLocalMember(), key, values ) );
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
                                format( "[%s] Reduce( %s, list( %s ) ) produced the following error: %s",
                                        getCluster().getLocalMember(), key, values, t.getMessage() ),
                                t );
                }
            }
        }

    }

}
