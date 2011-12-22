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

import org.nnsoft.hazelmap.builder.MapperBuilder;

public final class Hazelmap
{

    public static <K extends Serializable, V extends Serializable> MapperBuilder<K, V> processInput( InputReader<K, V> inputReader )
    {
        if ( inputReader == null )
        {
            throw new IllegalArgumentException( "The inputReader cannot be null" );
        }

        return null;
    }

    /**
     * Hidden constructor, this class must not be instantiated directly.
     */
    private Hazelmap()
    {
        // do nothing
    }

}
