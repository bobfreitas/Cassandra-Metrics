/* Copyright 2015 Eric Evans <eevans@wikimedia.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.freitas.cassandra.metrics;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.management.ObjectName;

import org.junit.Test;

import com.freitas.cassandra.metrics.CarbonVisitor;
import com.freitas.cassandra.metrics.JmxSample;
import com.freitas.cassandra.metrics.JmxSample.Type;

public class GraphiteMetricNamesTest {


    @Test
    public void test1() throws Exception {
    	String prefix = "cassandra.host";
    	ObjectName oName = new ObjectName("org.apache.cassandra.metrics:type=Table,keyspace=system_traces,scope=events,name=MemtableLiveDataSize");
    	JmxSample sample = newSample(Type.CASSANDRA_METRIC, oName, "bytes");
    	String expected = String.format("%s.org.apache.cassandra.metrics.Table.system_traces.events.MemtableLiveDataSize.bytes", prefix);
    	assertThat(CarbonVisitor.metricName(sample, prefix), is(expected));
    }
    
    @Test
    public void test2() throws Exception {
    	String prefix = "cassandra.host";
    	ObjectName oName = new ObjectName("org.apache.cassandra.metrics:type=ThreadPools,path=internal,scope=Sampler,name=MaxPoolSize");
    	JmxSample sample = newSample(Type.CASSANDRA_METRIC, oName, "pool size");
    	String expected = String.format("%s.org.apache.cassandra.metrics.ThreadPools.internal.Sampler.MaxPoolSize.pool-size", prefix);
    	assertThat(CarbonVisitor.metricName(sample, prefix), is(expected));
    }
    
    @Test
    public void test3() throws Exception {
    	String prefix = "cassandra.host";
    	ObjectName oName = new ObjectName("org.apache.cassandra.metrics:type=Connection,scope=127.0.0.1,name=CommandCompletedTasks");
    	JmxSample sample = newSample(Type.CASSANDRA_METRIC, oName, "count");
    	String expected = String.format("%s.org.apache.cassandra.metrics.Connection.127.0.0.1.CommandCompletedTasks.count", prefix);
    	assertThat(CarbonVisitor.metricName(sample, prefix), is(expected));
    }


    private static JmxSample newSample(Type type, ObjectName oName, String metricName) {
        return new JmxSample(type, oName, metricName, Double.valueOf(1.0d), 1);
    }

}
