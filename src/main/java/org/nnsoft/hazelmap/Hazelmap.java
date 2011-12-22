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

import org.nnsoft.hazelmap.builder.MapReduceInvoker;
import org.nnsoft.hazelmap.builder.MapperBuilder;
import org.nnsoft.hazelmap.builder.ReducerBuilder;

import com.hazelcast.core.MultiMap;

public final class Hazelmap
{

    public static <K extends Serializable, V extends Serializable> MapperBuilder<K, V> processInput( InputReader<K, V> inputReader )
    {
        if ( inputReader == null )
        {
            throw new IllegalArgumentException( "The inputReader cannot be null" );
        }

        return new DefaultMapperBuilder<K, V>( inputReader );
    }

    /**
     * Hidden constructor, this class must not be instantiated directly.
     */
    private Hazelmap()
    {
        // do nothing
    }

    private static final class DefaultMapperBuilder<IK extends Serializable, IV extends Serializable>
        implements MapperBuilder<IK, IV>
    {

        private final InputReader<IK, IV> inputReader;

        DefaultMapperBuilder( InputReader<IK, IV> inputReader )
        {
            this.inputReader = inputReader;
        }

        public <OK extends Serializable, OV extends Serializable> ReducerBuilder<IK, IV, OK, OV> usingMapper( Mapper<IK, IV, OK, OV> mapper )
        {
            return new DefaultReducerBuilder<IK, IV, OK, OV>();
        }

    }

    private static final class DefaultReducerBuilder<IK extends Serializable, IV extends Serializable, OK extends Serializable, OV extends Serializable>
        implements ReducerBuilder<IK, IV, OK, OV>
    {

        public MapReduceInvoker<IK, IV, OK, OV> withReducer( Reducer<OK, OV> reducer )
        {
            return new DefaultMapReduceInvoker<IK, IV, OK, OV>();
        }

    }

    private static final class DefaultMapReduceInvoker<IK extends Serializable, IV extends Serializable, OK extends Serializable, OV extends Serializable>
        implements MapReduceInvoker<IK, IV, OK, OV>
    {

        public MultiMap<OK, OV> invoke()
        {
            return null;
        }

        public <O> O invoke( OutputWriter<OK, OV, O> outputWriter )
        {
            return null;
        }

    }

}
