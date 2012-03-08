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
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger( "org.nnsoft.hazelmap.mapper" );

    /**
     * The intermediate
     */
    private MultiMap<OK, OV> intermediate;

    void setIntermediate( MultiMap<OK, OV> intermediate )
    {
        checkState( this.intermediate == null, "Re-entry not allowed." );

        this.intermediate = intermediate;
    }

    void resetIntermediate()
    {
        intermediate = null;
    }

    protected final void emitIntermediate( OK key, OV value )
    {
        if ( logger.isLoggable( INFO ) )
        {
            logger.info( format( "[%s] emitIntermediate( %s, %s )", getCluster().getLocalMember(), key, value ) );
        }
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

    final Runnable newRunnable( IK inputKey, IV inputValue )
    {
        return new MapperRunnable<IK, IV, OK, OV>( inputKey, inputValue, this );
    }

    private static final class MapperRunnable<IK extends Serializable, IV extends Serializable, OK extends Serializable, OV extends Serializable>
        implements Runnable, Serializable
    {

        private static final long serialVersionUID = -3104075452456356524L;

        private final IK key;

        private final IV value;

        private final Mapper<IK, IV, OK, OV> mapper;

        public MapperRunnable( IK key, IV value, Mapper<IK, IV, OK, OV> mapper )
        {
            this.key = key;
            this.value = value;
            this.mapper = mapper;
        }

        public void run()
        {
            if ( logger.isLoggable( INFO ) )
            {
                logger.info( format( "[%s] Map( %s, %s )", getCluster().getLocalMember(), key, value ) );
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
                                format( "[%s] Map( %s, %s ) produced the following error: %s",
                                        getCluster().getLocalMember(), key, value, t.getMessage() ),
                                t );
                }
            }
        }

    }

}
