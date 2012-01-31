package org.nnsoft.hazelmap.builder;

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

import java.io.Serializable;

import org.nnsoft.hazelmap.OutputWriter;

import com.hazelcast.core.MultiMap;

public interface MapReduceInvoker<IK extends Serializable, IV extends Serializable, OK extends Serializable, OV extends Serializable>
{

    MultiMap<OK, OV> invoke();

    <O> O invoke( OutputWriter<OK, OV, O> outputWriter );

}
